package ru.fisher.VehiclePark.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.fisher.VehiclePark.dto.MileageReportDTO;
import ru.fisher.VehiclePark.dto.TripMapDTO;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.dto.VehicleDetailsDTO;
import ru.fisher.VehiclePark.kafka.NotificationEvent;
import ru.fisher.VehiclePark.kafka.NotificationKafkaProducer;
import ru.fisher.VehiclePark.models.*;
import ru.fisher.VehiclePark.services.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;


@Slf4j
@Controller
@RequestMapping("/managers")
public class ManagerController {

    @Value("${kafka.topic.notifications}")
    private String topic;

    private final EnterpriseService enterpriseService;
    private final VehicleService vehicleService;
    private final BrandService brandService;
    private final TripService tripService;
    private final ReportService reportService;
    private final AuthContextService authContextService;
    private final NotificationKafkaProducer notificationKafkaProducer;

    @Autowired
    public ManagerController(EnterpriseService enterpriseService, VehicleService vehicleService,
                             BrandService brandService, TripService tripService, ReportService reportService, AuthContextService authContextService, NotificationKafkaProducer notificationKafkaProducer) {
        this.enterpriseService = enterpriseService;
        this.vehicleService = vehicleService;
        this.brandService = brandService;
        this.tripService = tripService;
        this.reportService = reportService;
        this.authContextService = authContextService;
        this.notificationKafkaProducer = notificationKafkaProducer;
    }

    @GetMapping("/enterprises")
    public ModelAndView indexEnterprises() {
        Long idManager = authContextService.getCurrentManagerId();
        ModelAndView enterprises = new ModelAndView("enterprises/index");
        enterprises.addObject("enterprises", enterpriseService.findAllForManager(idManager));
        return enterprises;
    }

    @GetMapping("/enterprises/{enterpriseId}/vehicles")
    public String indexVehiclesForEnterprise
            (@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
             @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
             @PathVariable("enterpriseId") Long enterpriseId,
             @RequestParam(value = "clientTimeZone", required = false, defaultValue = "UTC") String clientTimeZone,
             Model model) {

        Long managerId = authContextService.getCurrentManagerId();

        // Проверяем, что предприятие принадлежит менеджеру
        authContextService.validateManagerAccessToEnterprise(enterpriseId);

        Page<Vehicle> vehiclesPage = vehicleService.findAllForManagerByEnterpriseId
                (managerId, enterpriseId, page, size);

        List<VehicleDTO> vehicleDTOs = vehiclesPage.getContent()
                .stream()
                .map(vehicle -> vehicleService.convertToVehicleDTO(vehicle, clientTimeZone))
                .toList();

        model.addAttribute("vehicles", vehicleDTOs);
        model.addAttribute("currentPage", vehiclesPage.getNumber() + 1);
        model.addAttribute("totalPages", vehiclesPage.getTotalPages());
        model.addAttribute("hasNext", vehiclesPage.hasNext());
        model.addAttribute("hasPrevious", vehiclesPage.hasPrevious());
        model.addAttribute("enterpriseId", enterpriseId);
        model.addAttribute("managerId", managerId);

        return "vehicles/index";
    }

    // Детали машины и список поездок
    @GetMapping("/enterprises/{enterpriseId}/vehicles/{vehicleId}")
    public String showVehicleDetails(@PathVariable("enterpriseId") Long enterpriseId,
                                     @PathVariable("vehicleId") Long vehicleId,
                                     @RequestParam(value = "startTime", required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                     @RequestParam(value = "endTime", required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                     @RequestParam(value = "clientTimeZone", required = false, defaultValue = "UTC") String clientTimeZone,
                                     Model model) {

        // Проверяем, что предприятие принадлежит менеджеру
        authContextService.validateManagerAccessToEnterprise(enterpriseId);

        VehicleDetailsDTO detailsDTO = vehicleService.getVehicleDetailsForDisplay(
                enterpriseId, vehicleId, startTime, endTime, clientTimeZone
        );

        model.addAttribute("enterprise", detailsDTO.getEnterprise());
        model.addAttribute("vehicle", detailsDTO.getVehicle());
        model.addAttribute("trips", detailsDTO.getTrips());
        model.addAttribute("clientPurchaseTime", detailsDTO.getClientPurchaseTime());
        return "vehicles/show";
    }


    @GetMapping("enterprises/{enterpriseId}/vehicles/new")
    public String newVehicle(@ModelAttribute("vehicle") VehicleDTO vehicleDTO,
                             @PathVariable("enterpriseId") Long enterpriseId, Model model) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("enterprise", enterpriseService.findOne(enterpriseId));

        return "vehicles/new";
    }

    @PostMapping("/enterprises/{enterpriseId}/vehicles/new")
    public String create(@RequestParam("brandId") Long brandId,
                         @PathVariable("enterpriseId") Long enterpriseId,
                         @ModelAttribute("vehicle") @Valid VehicleDTO vehicleDTO,
                         BindingResult bindingResult,
                         @RequestParam("purchaseTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime purchaseTime,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("brands", brandService.findAll());
            model.addAttribute("enterprise", enterpriseService.findOne(enterpriseId));
            return "vehicles/new";
        }

        Vehicle vehicle = vehicleService.convertToVehicle(vehicleDTO);
        vehicle.setPurchaseTime(purchaseTime); // Устанавливаем время покупки
        vehicleService.save(vehicle, brandId, enterpriseId);

        return "redirect:/managers/enterprises/" + enterpriseId + "/vehicles";
    }

    @GetMapping("/enterprises/{enterpriseId}/vehicles/{vehicleId}/edit")
    public String edit(@PathVariable("enterpriseId") Long enterpriseId,
                       @PathVariable("vehicleId") Long vehicleId, Model model) {
        model.addAttribute("enterprise", enterpriseService.findOne(enterpriseId));
        model.addAttribute("vehicle", vehicleService.findOne(vehicleId));
        model.addAttribute("brands", brandService.findAll());

        return "vehicles/edit";
    }

    @PutMapping("/enterprises/{enterpriseId}/vehicles/{vehicleId}")
    public String update(@PathVariable("enterpriseId") Long enterpriseId,
                         @PathVariable("vehicleId") Long vehicleId,
                         @RequestParam("brandId") Long brandId,
                         @ModelAttribute("vehicle") @Valid VehicleDTO vehicleDTO,
                         Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMsg", "Некорректные данные, введите запрос заново");
            model.addAttribute("enterprise", enterpriseService.findOne(enterpriseId));
            model.addAttribute("vehicle", vehicleService.findOne(vehicleId));
            model.addAttribute("brands", brandService.findAll());
            return "vehicles/edit";
        }

        vehicleService.updateVehicle(vehicleId, vehicleDTO, brandId, enterpriseId);

        return "redirect:/managers/enterprises/" + enterpriseId + "/vehicles";
    }

    @DeleteMapping("/enterprises/{enterpriseId}/vehicles/{vehicleId}")
    public String delete(@PathVariable("enterpriseId") Long enterpriseId,
                         @PathVariable("vehicleId") Long vehicleId) {
        vehicleService.delete(vehicleId);
        return "redirect:/managers/enterprises/" + enterpriseId + "/vehicles";
    }

    @GetMapping("/enterprises/{enterpriseId}/edit")
    public String edit(@PathVariable("enterpriseId") Long enterpriseId,
                       Model model) {
        model.addAttribute("enterprise", enterpriseService.findOne(enterpriseId));

        // Список всех временных зон
        model.addAttribute("timezones", TimeZone.getAvailableIDs());

        return "enterprises/edit";
    }

    @PutMapping("/enterprises/{enterpriseId}")
    public String update(@PathVariable("enterpriseId") Long enterpriseId,
                         @ModelAttribute("enterprise") @Valid Enterprise enterprise,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMsg", "Введены некорректные данные. Попробуйте еще!");
            model.addAttribute("enterprise", enterpriseService.findOne(enterpriseId));
            return "enterprises/edit";
        }
        Long managerId = authContextService.getCurrentManagerId();
        enterpriseService.update(managerId, enterpriseId, enterprise);
        return "redirect:/managers/enterprises";
    }

    @DeleteMapping("/enterprises/{enterpriseId}/delete")
    public String delete(@PathVariable("enterpriseId") Long enterpriseId) {
        Long managerId = authContextService.getCurrentManagerId();
        enterpriseService.delete(managerId, enterpriseId);
        return "redirect:/managers/enterprises";
    }

    @GetMapping("/enterprises/{enterpriseId}/vehicles/{vehicleId}/trips/map")
    public String showTripsOnMap(@PathVariable("enterpriseId") Long enterpriseId,
                                 @PathVariable("vehicleId") Long vehicleId,
                                 @RequestParam(value = "startTime", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                 @RequestParam(value = "endTime", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                 @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone,
                                 Model model) {
        Vehicle vehicle = vehicleService.findOne(vehicleId);
        Enterprise enterprise = enterpriseService.findOne(enterpriseId);

        // Проверяем, что предприятие принадлежит менеджеру
        authContextService.validateManagerAccessToEnterprise(enterpriseId);

        List<TripMapDTO> tripMapDTOS = tripService.
                getTripMapData(enterpriseId, vehicleId, startTime, endTime, clientTimeZone);

        model.addAttribute("tripCoordinates", tripMapDTOS.stream().map(TripMapDTO::getCoordinates).toList());
        model.addAttribute("trips", tripMapDTOS.stream().map(TripMapDTO::getTrip).toList()); // Передаем список поездок в модель
        model.addAttribute("startPoints", tripMapDTOS.stream().map(TripMapDTO::getStartPoint).toList());
        model.addAttribute("endPoints", tripMapDTOS.stream().map(TripMapDTO::getEndPoint).toList());
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("enterprise", enterprise);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        return "trips/map";
    }

    @GetMapping("/reports")
    public String showReportsPage(Model model) {
        Long managerId = authContextService.getCurrentManagerId();

        // Передаём типы отчётов, периоды и доступные автомобили
        model.addAttribute("reportTypes", ReportType.values());
        model.addAttribute("periods", Period.values());
        model.addAttribute("enterprises", enterpriseService.findAllForManager(managerId));
        return "reports/index"; // Имя шаблона
    }

    @PostMapping("/reports/generate")
    public String generateReport(
            @RequestParam ReportType reportType,
            @RequestParam(required = false) Long enterpriseId,
            @RequestParam(required = false) String vehicleNumber,
            @RequestParam Period period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate,
            Model model) {

        MileageReportDTO report;
        Manager currentManager = authContextService.getCurrentManager();

        switch (reportType) {
            case VEHICLE_MILEAGE -> report = reportService.
                    generateMileageReport(currentManager, vehicleNumber, startDate, endDate, period);
            case ENTERPRISE_MILEAGE -> report = reportService.
                    generateEnterpriseMileageReport(currentManager, enterpriseId, startDate, endDate, period);
            case TOTAL_MILEAGE -> report = reportService.
                    generateTotalMileageReport(currentManager, startDate, endDate, period);
            default -> throw new IllegalArgumentException("Неизвестный тип отчета: " + reportType);
        }

        model.addAttribute("report", report);
        NotificationEvent event = new NotificationEvent(
                null,
                currentManager.getId(),
                "Выгрузил отчет - " + reportType.getTitle(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        );

        notificationKafkaProducer.sendNotification(currentManager.getId(), event.getMessage());
        return "reports/view";
    }

    @GetMapping("/enterprises/{enterpriseId}/vehicles/{vehicleId}/trips/upload")
    public String loadTripGPX(@PathVariable("enterpriseId") Long enterpriseId,
                              @PathVariable("vehicleId") Long vehicleId,
                              Model model) {
        Vehicle vehicle = vehicleService.findOne(vehicleId);
        Enterprise enterprise = enterpriseService.findOne(enterpriseId);

        // Проверяем, что предприятие принадлежит менеджеру
        authContextService.validateManagerAccessToEnterprise(enterpriseId);

        model.addAttribute("vehicle", vehicle);
        model.addAttribute("enterprise", enterprise);
        return "trips/upload";
    }

    @PostMapping("/enterprises/{enterpriseId}/vehicles/{vehicleId}/trips/upload")
    public ResponseEntity<?> uploadTrip(
            @PathVariable("vehicleId") Long vehicleId,
            @RequestParam("startTime") LocalDateTime startTime,
            @RequestParam("endTime") LocalDateTime endTime,
            @RequestParam("gpxFile") MultipartFile gpxFile) {

        try {
            tripService.uploadTripFromGpx(vehicleService.findOne(vehicleId), startTime, endTime, gpxFile);
            return ResponseEntity.ok("Поездка успешно добавлена!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка импорта: " + e.getMessage());
        }

    }

}
