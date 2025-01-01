package ru.fisher.VehiclePark.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.fisher.VehiclePark.dto.TripDTO;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.mapper.VehicleMapper;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.models.Trip;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.security.PersonDetails;
import ru.fisher.VehiclePark.services.*;
import ru.fisher.VehiclePark.util.GeoCoderService;
import ru.fisher.VehiclePark.util.TimeZoneUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/managers")
public class ManagerController {

    private final EnterpriseService enterpriseService;
    private final ManagerService managerService;
    private final PersonDetailsService personDetailsService;
    private final VehicleService vehicleService;
    private final BrandService brandService;
    private final VehicleMapper vehicleMapper;
    private final ModelMapper modelMapper;
    private final GpsDataService gpsDataService;
    private final TripService tripService;
    private final GeoCoderService geoCoderService;

    @Autowired
    public ManagerController(EnterpriseService enterpriseService, ManagerService managerService,
                             PersonDetailsService personDetailsService, VehicleService vehicleService,
                             BrandService brandService, VehicleMapper vehicleMapper, ModelMapper modelMapper, GpsDataService gpsDataService, TripService tripService, GeoCoderService geoCoderService) {
        this.enterpriseService = enterpriseService;
        this.managerService = managerService;
        this.personDetailsService = personDetailsService;
        this.vehicleService = vehicleService;
        this.brandService = brandService;
        this.vehicleMapper = vehicleMapper;
        this.modelMapper = modelMapper;
        this.gpsDataService = gpsDataService;
        this.tripService = tripService;
        this.geoCoderService = geoCoderService;
    }

    @GetMapping("/enterprises")
    public ModelAndView indexEnterprises() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String username = personDetails.getPerson().getUsername();

        Long id = managerService.findByUsername(username).getId();

        ModelAndView enterprises = new ModelAndView("enterprises/index");

        enterprises.addObject("enterprises", enterpriseService.findAllForManager(id));

        return enterprises;
    }

//    public VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
//        return modelMapper.map(vehicle, VehicleDTO.class);
//    }

    public Vehicle convertToVehicle(VehicleDTO vehicleDTO) {
        return modelMapper.map(vehicleDTO, Vehicle.class);
    }

    public VehicleDTO convertToVehicleDTO(Vehicle vehicle, String clientTimeZone) {
        VehicleDTO vehicleDTO = modelMapper.map(vehicle, VehicleDTO.class);

        // Преобразуем время покупки из UTC в таймзону клиента
        LocalDateTime utcPurchaseTime = vehicle.getPurchaseTime();
        ZoneId clientZoneId = ZoneId.of(clientTimeZone);

        LocalDateTime clientPurchaseTime = utcPurchaseTime
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(clientZoneId)
                .toLocalDateTime();

        // Форматируем время для отображения
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        vehicleDTO.setPurchaseTime(clientPurchaseTime.format(formatter));

        return vehicleDTO;
    }

    @GetMapping("enterprises/{enterpriseId}/vehicles")
    public String indexVehiclesForEnterprise(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                             @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                             @PathVariable("enterpriseId") Long enterpriseId,
                                             @RequestParam(value = "clientTimeZone", required = false, defaultValue = "UTC") String clientTimeZone,
                                             Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String username = personDetails.getPerson().getUsername();
        Long idManager = managerService.findByUsername(username).getId();

        Page<Vehicle> vehiclesPage = vehicleService.findAllForManagerByEnterpriseId(idManager, enterpriseId, page, size);

        List<VehicleDTO> vehicleDTOs = vehiclesPage.getContent()
                .stream()
                .map(vehicle -> convertToVehicleDTO(vehicle, clientTimeZone))
                .collect(Collectors.toList());

        model.addAttribute("vehicles", vehicleDTOs);
        model.addAttribute("currentPage", vehiclesPage.getNumber() + 1);
        model.addAttribute("totalPages", vehiclesPage.getTotalPages());
        model.addAttribute("hasNext", vehiclesPage.hasNext());
        model.addAttribute("hasPrevious", vehiclesPage.hasPrevious());
        model.addAttribute("enterpriseId", enterpriseId);

        return "vehicles/index";
    }

//    @GetMapping("/enterprises/{enterpriseId}/vehicles/{vehicleId}")
//    public String show(@PathVariable("enterpriseId") Long enterpriseId,
//                       @PathVariable("vehicleId") Long vehicleId, Model model,
//                       @ModelAttribute("vehicle") Vehicle vehicle,
//                       @RequestParam(value = "clientTimeZone", required = false, defaultValue = "UTC") String clientTimeZone) {
//        model.addAttribute("vehicle", vehicleService.findOne(vehicleId));
//        model.addAttribute("enterprise", enterpriseService.findOne(enterpriseId));
//        model.addAttribute("trips", tripService.findTripsByVehicle(vehicle.getId()));
//
//        return "vehicles/show";
//    }

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
        Vehicle vehicle = vehicleService.findOne(vehicleId);
        List<Trip> trips = (startTime != null && endTime != null)
                ? tripService.findTripsForVehicleInTimeRange(vehicleId, startTime, endTime)
                : tripService.findTripsByVehicle(vehicleId);

        // Преобразуем время покупки из UTC в таймзону клиента
        LocalDateTime utcPurchaseTime = vehicle.getPurchaseTime();
        ZoneId clientZoneId = ZoneId.of(clientTimeZone);

        LocalDateTime clientPurchaseTime = utcPurchaseTime
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(clientZoneId)
                .toLocalDateTime();

        model.addAttribute("enterprise", enterpriseService.findOne(enterpriseId));
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("trips", trips);
        model.addAttribute("clientPurchaseTime", clientPurchaseTime);
        return "vehicles/show";
    }

    private TripDTO convertToTripDTO(Trip trip) {
        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(trip.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        tripDTO.setStartTime(trip.getStartTime().format(formatter));
        tripDTO.setEndTime(trip.getEndTime().format(formatter));

        tripDTO.setDuration(formatDuration(Duration.between(trip.getStartTime(), trip.getEndTime())));

        // Проверяем наличие GPS-данных для начальной точки
        if (trip.getStartGpsData() != null && trip.getStartGpsData().getCoordinates() != null) {
            tripDTO.setStartPointAddress(geoCoderService.getAddressFromOpenRouteService(
                    trip.getStartGpsData().getCoordinates().getY(),
                    trip.getStartGpsData().getCoordinates().getX()
            ));
        } else {
            tripDTO.setStartPointAddress("Адрес отсутствует");
        }

        // Проверяем наличие GPS-данных для конечной точки
        if (trip.getEndGpsData() != null && trip.getEndGpsData().getCoordinates() != null) {
            tripDTO.setEndPointAddress(geoCoderService.getAddressFromOpenRouteService(
                    trip.getEndGpsData().getCoordinates().getY(),
                    trip.getEndGpsData().getCoordinates().getX()
            ));
        } else {
            tripDTO.setEndPointAddress("Адрес отсутствует");
        }

        return tripDTO;
    }

    public static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        StringBuilder formatted = new StringBuilder();
        if (hours > 0) {
            formatted.append(hours).append(" hour").append(hours > 1 ? "s " : " ");
        }
        if (minutes > 0) {
            formatted.append(minutes).append(" minute").append(minutes > 1 ? "s " : " ");
        }
        if (seconds > 0) {
            formatted.append(seconds).append(" second").append(seconds > 1 ? "s" : "");
        }
        return formatted.toString().trim();
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
                         Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("brands", brandService.findAll());
            model.addAttribute("enterprise", enterpriseService.findOne(enterpriseId));
            return "vehicle/new";
        }
        vehicleService.save(convertToVehicle(vehicleDTO), brandId, enterpriseId);

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

        vehicleService.update(vehicleId, convertToVehicle(vehicleDTO), brandId, enterpriseId);

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Long managerId = personDetails.getPerson().getId();

        enterpriseService.update(managerId, enterpriseId, enterprise);

        return "redirect:/managers/enterprises";
    }

    @GetMapping("/enterprises/{enterpriseId}/vehicles/{vehicleId}/trips/map")
    public String showTripsOnMap(@PathVariable("enterpriseId") Long enterpriseId,
                                 @PathVariable("vehicleId") Long vehicleId,
                                 @RequestParam(value = "startTime", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                 @RequestParam(value = "endTime", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                 Model model) {
        Vehicle vehicle = vehicleService.findOne(vehicleId);
        Enterprise enterprise = enterpriseService.findOne(enterpriseId);

        String enterpriseTimeZone = enterprise.getTimezone() != null ? enterprise.getTimezone() : "UTC";

        // Устанавливаем значения по умолчанию, если даты не указаны
        if (startTime == null) {
            startTime = LocalDateTime.now().minusDays(1); // Например, последние 24 часа
        }
        if (endTime == null) {
            endTime = LocalDateTime.now(); // Текущее время
        }

        // Конвертируем время из таймзоны предприятия в UTC
        LocalDateTime startUtc = TimeZoneUtil.convertToUtc(startTime, enterpriseTimeZone);
        LocalDateTime endUtc = TimeZoneUtil.convertToUtc(endTime, enterpriseTimeZone);

        // Получаем поездки
        List<Trip> trips = tripService.findTripsForVehicleInTimeRange(vehicleId, startUtc, endUtc);

        // Формируем список координат для каждого трека
        List<List<double[]>> tripCoordinates = trips.stream()
                .map(this::getCoordinatesForTrip)
                .collect(Collectors.toList());

        // Формируем список начальных и конечных точек для каждой поездки
        List<double[]> startPoints = trips.stream()
                .map(trip -> new double[]{
                        trip.getStartGpsData().getLatitude(),
                        trip.getStartGpsData().getLongitude()})
                .collect(Collectors.toList());

        List<double[]> endPoints = trips.stream()
                .map(trip -> new double[]{
                        trip.getEndGpsData().getLatitude(),
                        trip.getEndGpsData().getLongitude()})
                .collect(Collectors.toList());

        model.addAttribute("tripCoordinates", tripCoordinates);
        model.addAttribute("trips",
                tripService.findTripsForVehicleInTimeRange(vehicleId, startUtc, endUtc)
                .stream().map(this::convertToTripDTO)); // Передаем список поездок в модель
        model.addAttribute("startPoints", startPoints);
        model.addAttribute("endPoints", endPoints);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("enterprise", enterprise);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        return "trips/map";
    }

    private List<double[]> getCoordinatesForTrip(Trip trip) {
        List<double[]> coordinates = new ArrayList<>();

        // Добавляем начальную точку
        GpsData startGpsData = trip.getStartGpsData();
        if (startGpsData != null) {
            coordinates.add(new double[]{startGpsData.getLatitude(), startGpsData.getLongitude()});
        }

        // Получаем точки между начальной и конечной
        List<GpsData> gpsDataList = gpsDataService.findByVehicleAndTimeRange(
                trip.getVehicle().getId(),
                trip.getStartTime(),
                trip.getEndTime()
        );

        for (GpsData gpsData : gpsDataList) {
            coordinates.add(new double[]{gpsData.getLatitude(), gpsData.getLongitude()});
        }

        // Добавляем конечную точку
        GpsData endGpsData = trip.getEndGpsData();
        if (endGpsData != null) {
            coordinates.add(new double[]{endGpsData.getLatitude(), endGpsData.getLongitude()});
        }

        return coordinates;
    }

}
