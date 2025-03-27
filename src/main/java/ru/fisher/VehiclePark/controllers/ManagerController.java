package ru.fisher.VehiclePark.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ru.fisher.VehiclePark.dto.MileageReportDTO;
import ru.fisher.VehiclePark.dto.TripDTO;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.exceptions.AccessDeniedException;
import ru.fisher.VehiclePark.models.*;
import ru.fisher.VehiclePark.security.ManagerDetails;
import ru.fisher.VehiclePark.services.*;
import ru.fisher.VehiclePark.util.GeoCoderService;
import ru.fisher.VehiclePark.util.TimeZoneUtil;
import ru.fisher.VehiclePark.util.SnapToRoads;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;


@Slf4j
@Controller
@RequestMapping("/managers")
public class ManagerController {

    private final EnterpriseService enterpriseService;
    private final ManagerService managerService;
    private final VehicleService vehicleService;
    private final BrandService brandService;
    private final ModelMapper modelMapper;
    private final GpsDataService gpsDataService;
    private final TripService tripService;
    private final GeoCoderService geoCoderService;
    private final ReportService reportService;
    private final SnapToRoads snapToRoads;

    @Autowired
    public ManagerController(EnterpriseService enterpriseService, ManagerService managerService,
                             VehicleService vehicleService, BrandService brandService, ModelMapper modelMapper,
                             GpsDataService gpsDataService, TripService tripService, GeoCoderService geoCoderService,
                             ReportService reportService, SnapToRoads snapToRoads) {
        this.enterpriseService = enterpriseService;
        this.managerService = managerService;
        this.vehicleService = vehicleService;
        this.brandService = brandService;
        this.modelMapper = modelMapper;
        this.gpsDataService = gpsDataService;
        this.tripService = tripService;
        this.geoCoderService = geoCoderService;
        this.reportService = reportService;
        this.snapToRoads = snapToRoads;
    }

    @GetMapping("/enterprises")
    public ModelAndView indexEnterprises() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ManagerDetails managerDetails = (ManagerDetails) authentication.getPrincipal();
        String username = managerDetails.getManager().getUsername();

        Long idManager = managerService.findByUsername(username).getId();

        ModelAndView enterprises = new ModelAndView("enterprises/index");

        enterprises.addObject("enterprises", enterpriseService.findAllForManager(idManager));

        return enterprises;
    }

    private void validateManagerAccessToEnterprise(Long enterpriseId, Long managerId) {
        if (!enterpriseService.isEnterpriseManagedByManager(enterpriseId, managerId)) {
            throw new AccessDeniedException("Доступ запрещен");
        }
    }

    public Long getManagerId() {
        // Получаем текущего менеджера
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ManagerDetails managerDetails = (ManagerDetails) authentication.getPrincipal();
        return managerDetails.getManager().getId();
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

    @GetMapping("/enterprises/{enterpriseId}/vehicles")
    public String indexVehiclesForEnterprise
            (@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
             @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
             @PathVariable("enterpriseId") Long enterpriseId,
             @RequestParam(value = "clientTimeZone", required = false, defaultValue = "UTC") String clientTimeZone,
             Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ManagerDetails managerDetails = (ManagerDetails) authentication.getPrincipal();
        String username = managerDetails.getManager().getUsername();
        Long managerId = managerService.findByUsername(username).getId();

        // Проверяем, что предприятие принадлежит менеджеру
        validateManagerAccessToEnterprise(enterpriseId, managerId);

        Page<Vehicle> vehiclesPage = vehicleService.findAllForManagerByEnterpriseId(managerId, enterpriseId, page, size);

        List<VehicleDTO> vehicleDTOs = vehiclesPage.getContent()
                .stream()
                .map(vehicle -> convertToVehicleDTO(vehicle, clientTimeZone))
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

        validateManagerAccessToEnterprise(enterpriseId, getManagerId());

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
        tripDTO.setMileage(String.valueOf(BigDecimal.valueOf(trip.getMileage())
                .setScale(2, RoundingMode.HALF_UP).doubleValue()));

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
                         @RequestParam("purchaseTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime purchaseTime,
                         Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("brands", brandService.findAll());
            model.addAttribute("enterprise", enterpriseService.findOne(enterpriseId));
            return "vehicle/new";
        }

        Vehicle vehicle = convertToVehicle(vehicleDTO);
        vehicle.setPurchaseTime(purchaseTime); // Устанавливаем время покупки
        vehicleService.save(vehicle, brandId, enterpriseId);
        //vehicleService.save(convertToVehicle(vehicleDTO), brandId, enterpriseId);

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

        Vehicle vehicle = convertToVehicle(vehicleDTO);

        // Убедиться, что `purchaseTime` не null
        if (vehicle.getPurchaseTime() == null) {
            vehicle.setPurchaseTime(vehicleService.findOne(vehicleId).getPurchaseTime());
        }

        vehicleService.update(vehicleId, vehicle, brandId, enterpriseId);

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ManagerDetails managerDetails = (ManagerDetails) authentication.getPrincipal();
        Long managerId = managerDetails.getManager().getId();

        enterpriseService.update(managerId, enterpriseId, enterprise);

        return "redirect:/managers/enterprises";
    }

    @DeleteMapping("/enterprises/{enterpriseId}/delete")
    public String delete(@PathVariable("enterpriseId") Long enterpriseId) {
        enterpriseService.delete(enterpriseId);
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

        validateManagerAccessToEnterprise(enterpriseId, getManagerId());

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

        log.info("=== Vehicle ID: {}", vehicleId);
        log.info("=== Временной диапазон: {} - {}", startUtc, endUtc);

        // Получаем поездки
        List<Trip> trips = tripService.findTripsForVehicleInTimeRange(vehicleId, startUtc, endUtc);

        // Сортируем поездки по времени начала
        trips.sort(Comparator.comparing(Trip::getStartTime));

        // Формируем список координат для каждого трека
        List<List<double[]>> tripCoordinates = trips.stream()
                .map(this::getCoordinatesForTrip)
                .toList();

        // Формируем список начальных и конечных точек для каждой поездки
        List<double[]> startPoints = trips.stream()
                .map(trip -> new double[]{
                        trip.getStartGpsData().getCoordinates().getY(),
                        trip.getStartGpsData().getCoordinates().getX()})
                .toList();

        List<double[]> endPoints = trips.stream()
                .map(trip -> new double[]{
                        trip.getEndGpsData().getCoordinates().getY(),
                        trip.getEndGpsData().getCoordinates().getX()})
                .toList();

        model.addAttribute("tripCoordinates", tripCoordinates);
        model.addAttribute("trips", trips.stream().map(this::convertToTripDTO)); // Передаем список поездок в модель
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
            coordinates.add(new double[]{startGpsData.getCoordinates().getY(), startGpsData.getCoordinates().getX()});
        }

        // Получаем точки между начальной и конечной
        List<GpsData> gpsDataList = gpsDataService.findByVehicleAndTimeRange(
                trip.getVehicle().getId(),
                trip.getStartTime(),
                trip.getEndTime(),
                Sort.by(Sort.Direction.ASC, "timestamp")
        );

        log.info("=== Найдено точек: {}", gpsDataList.size());

        for (GpsData gpsData : gpsDataList) {
            log.info("GPS точка: {}, {}", gpsData.getCoordinates().getY(), gpsData.getCoordinates().getX());
            coordinates.add(new double[]{gpsData.getCoordinates().getY(), gpsData.getCoordinates().getX()});
        }

        // Добавляем конечную точку
        GpsData endGpsData = trip.getEndGpsData();
        if (endGpsData != null) {
            coordinates.add(new double[]{endGpsData.getCoordinates().getY(), endGpsData.getCoordinates().getX()});
        }

        return coordinates;
    }

    @GetMapping("/reports")
    public String showReportsPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ManagerDetails managerDetails = (ManagerDetails) authentication.getPrincipal();
        String username = managerDetails.getManager().getUsername();
        Long managerId = managerService.findByUsername(username).getId();

        // Передаём типы отчётов, периоды и доступные автомобили
        model.addAttribute("reportTypes", ReportType.values());
        model.addAttribute("periods", Period.values());
        model.addAttribute("enterprises", enterpriseService.findAllForManager(managerId));
        model.addAttribute("vehicles", vehicleService.findAllForManager(managerId)); // Получение списка автомобилей
        return "reports/index"; // Имя шаблона
    }

    @PostMapping("/reports/generate")
    public String generateReport(
            @RequestParam ReportType reportType,
            @RequestParam(required = false) Long enterpriseId,
            @RequestParam(required = false) Long vehicleId,
            @RequestParam Period period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate,
            Model model) {

        MileageReportDTO report;

        switch (reportType) {
            case VEHICLE_MILEAGE -> report = reportService.
                    generateMileageReport(vehicleId, startDate, endDate, period);
            case ENTERPRISE_MILEAGE -> report = reportService.
                    generateEnterpriseMileageReport(enterpriseId, startDate, endDate, period);
            case TOTAL_MILEAGE -> report = reportService.
                    generateTotalMileageReport(startDate, endDate, period);
            default -> throw new IllegalArgumentException("Неизвестный тип отчета: " + reportType);
        }

        model.addAttribute("report", report);
        return "reports/view";
    }

    @GetMapping("/enterprises/{enterpriseId}/vehicles/{vehicleId}/trips/upload")
    public String loadTripGPX(@PathVariable("enterpriseId") Long enterpriseId,
                              @PathVariable("vehicleId") Long vehicleId,
                              Model model) {
        Vehicle vehicle = vehicleService.findOne(vehicleId);
        Enterprise enterprise = enterpriseService.findOne(enterpriseId);

        validateManagerAccessToEnterprise(enterpriseId, getManagerId());

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

        // Проверяем, что временной диапазон не пересекается с существующими поездками
        if (tripService.isTimeRangeOverlapping(vehicleId, startTime, endTime)) {
            return ResponseEntity.internalServerError().body("Ошибка: Наложение с существующей поездкой");
        }

        try {
            // Парсим GPX-файл и создаем новые точки GPS
            List<GpsData> gpsDataList = parseGpxFile(vehicleId, gpxFile, startTime, endTime);

            // Привязываем точки к дорогам
           // gpsDataList = snapToRoads.optimizeTrackWithOpenRouteService(gpsDataList);

            // Сохраняем точки GPS
            gpsDataService.saveAll(gpsDataList);

            // Создаем новую поездку
            Trip newTrip = new Trip();
            newTrip.setVehicle(vehicleService.findOne(vehicleId));
            newTrip.setStartTime(startTime);
            newTrip.setEndTime(endTime);
            newTrip.setStartGpsData(gpsDataList.getFirst());
            newTrip.setEndGpsData(gpsDataList.getLast());

            // Рассчитываем расстояние
            double mileage = calculateMileageFromGpx(gpsDataList);

            newTrip.setMileage(mileage);
            tripService.save(newTrip);

            return ResponseEntity.ok("Поездка успешно добавлена!");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при импорте данных: " + e.getMessage());
        }
    }

    private List<GpsData> parseGpxFile(Long vehicleId, MultipartFile gpxFile,
                                       LocalDateTime startTime,
                                       LocalDateTime endTime) throws Exception {
        // Чтение содержимого файла
        String gpxContent = new String(gpxFile.getBytes(), StandardCharsets.UTF_8);

        // Парсинг GPX-файла
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(gpxContent));
        Document doc = builder.parse(is);

        // Получение всех точек из GPX
        NodeList trackPoints = doc.getElementsByTagName("trkpt");
        List<GpsData> gpsDataList = new ArrayList<>();
        GeometryFactory geometryFactory = new GeometryFactory();

        for (int i = 0; i < trackPoints.getLength(); i++) {
            Element trkpt = (Element) trackPoints.item(i);

            // Извлечение координат
            double latitude = Double.parseDouble(trkpt.getAttribute("lat"));
            double longitude = Double.parseDouble(trkpt.getAttribute("lon"));

            // Извлечение времени
            String timeStr = trkpt.getElementsByTagName("time").item(0).getTextContent();
            LocalDateTime pointTime = LocalDateTime.parse(timeStr, DateTimeFormatter.ISO_DATE_TIME);

            // Проверка, что время точки находится в диапазоне поездки
            if (pointTime.isBefore(startTime) || pointTime.isAfter(endTime)) {
                throw new IllegalArgumentException("GPX содержит точки, выходящие за пределы диапазона поездки");
            }

            // Создание новой точки GPS
            GpsData gpsData = new GpsData();
            gpsData.setVehicle(vehicleService.findOne(vehicleId));
            gpsData.setCoordinates(geometryFactory.createPoint(new Coordinate(longitude, latitude)));
            gpsData.setTimestamp(pointTime);

            gpsDataList.add(gpsData);
        }

        return gpsDataList;
    }

    private double calculateMileageFromGpx(List<GpsData> gpsDataList) {
        double totalDistance = 0.0;

        for (int i = 1; i < gpsDataList.size(); i++) {
            GpsData prevPoint = gpsDataList.get(i - 1);
            GpsData currPoint = gpsDataList.get(i);

            // Расчет расстояния между двумя точками
            totalDistance += calculateDistance(
                    prevPoint.getLatitude(),
                    prevPoint.getLongitude(),
                    currPoint.getLatitude(),
                    currPoint.getLongitude()
            );
        }

        return totalDistance;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Радиус Земли в километрах
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

}
