package ru.fisher.VehiclePark.controllers.REST;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.fisher.VehiclePark.dto.*;
import ru.fisher.VehiclePark.exceptions.*;
import ru.fisher.VehiclePark.mapper.EnterpriseMapper;
import ru.fisher.VehiclePark.mapper.VehicleMapper;
import ru.fisher.VehiclePark.models.*;
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

@Slf4j
@RestController
@RequestMapping("/api/managers")
public class ManagerRestController {

    private final EnterpriseService enterpriseService;
    private final DriverService driverService;
    private final VehicleService vehicleService;
    private final ModelMapper modelMapper;
    private final VehicleMapper vehicleMapper;
    private final EnterpriseMapper enterpriseMapper;
    private final ManagerService managerService;
    private final GpsDataService gpsDataService;
    private final TripService tripService;
    private final GeoCoderService geoCoderService;

    @Autowired
    public ManagerRestController(EnterpriseService enterpriseService, DriverService driverService,
                                 VehicleService vehicleService, ModelMapper modelMapper,
                                 VehicleMapper vehicleMapper, EnterpriseMapper enterpriseMapper,
                                 ManagerService managerService, GpsDataService gpsDataService, TripService tripService, GeoCoderService geoCoderService) {
        this.enterpriseService = enterpriseService;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
        this.modelMapper = modelMapper;
        this.vehicleMapper = vehicleMapper;
        this.enterpriseMapper = enterpriseMapper;
        this.managerService = managerService;
        this.gpsDataService = gpsDataService;
        this.tripService = tripService;
        this.geoCoderService = geoCoderService;
    }

    @GetMapping
    public ModelAndView start(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//      ManagerDetails managerDetails = (ManagerDetails) authentication.getPrincipal();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String username = personDetails.getPerson().getUsername();
        log.info(username);

        model.addAttribute("manager", managerService.findByUsername(username));
        return new ModelAndView("start");
    }

    public void checkManager(Long id) {
        // Получаем текущего менеджера
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//      ManagerDetails managerDetails = (ManagerDetails) authentication.getPrincipal();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        // Проверяем, соответствует ли id менеджера
        if (!personDetails.getPerson().getId().equals(id)) {
            throw new AccessDeniedException("Доступ запрещен");
        }
    }

    @GetMapping("/gps/vehicle/{vehicleId}")
    public List<GpsDataDTO> getGPSDataByVehicle(@PathVariable Long vehicleId) {
        return gpsDataService.findByVehicleId(vehicleId)
                .stream()
                .map(this::convertToPointGpsDTO)
                .toList();
    }

//    @GetMapping("/vehicle/{vehicleId}/range")
//    public List<GpsData> getGPSDataByVehicleAndTimeRange(@PathVariable Long vehicleId,
//                                                         @RequestParam LocalDateTime startTime,
//                                                         @RequestParam LocalDateTime endTime) {
//        return gpsDataService.findByVehicleAndTimeRange(vehicleId, startTime, endTime);
//    }

//    @GetMapping("/vehicle/{vehicleId}/range")
//    public List<GpsDataDTO> getGPSDataByVehicleAndTimeRange(
//            @PathVariable Long vehicleId,
//            @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone,
//            @RequestParam(value = "dateFrom", defaultValue = "", required = false) LocalDateTime dateFrom,
//            @RequestParam(value = "dateTo", defaultValue = "", required = false) LocalDateTime dateTo) {
//
//        return gpsDataService.findByVehicleAndTimeRange(vehicleId, dateFrom, dateTo)
//                .stream()
//                .map(gpsData -> convertToPointGpsDTO_forAPI(gpsData, clientTimeZone))
//                .collect(Collectors.toList());
//    }

//    @GetMapping("/vehicle/{vehicleId}/track")
//    public List<GpsDataDTO> getTrackPoints(
//            @PathVariable Long vehicleId,
//            @RequestParam Long enterpriseId, // ID предприятия
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateFrom,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateTo
//    ) {
//        List<GpsDataDTO> points = gpsDataService.findByVehicleAndTimeRange(vehicleId, dateFrom, dateTo)
//                .stream()
//                .map(this::convertToPointGpsDTO)
//                .collect(Collectors.toList());
//        return convertToLocalTime(points, enterpriseId);
//    }
//
//    public List<GpsDataDTO> convertToLocalTime(List<GpsDataDTO> points, Long enterpriseId) {
//        String timezone = enterpriseService.getTimezoneByEnterpriseId(enterpriseId);
//        ZoneId zoneId = ZoneId.of(timezone);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
//        return points.stream()
//                .peek(point -> {
//                    ZonedDateTime localTime = point.getTimestamp().withZoneSameInstant(zoneId);
//                    point.setTimestamp(ZonedDateTime.parse(localTime.format(formatter), formatter.withZone(zoneId)));
//                })
//                .collect(Collectors.toList());
//    }

    @GetMapping("/allPoints")
    public List<GpsDataDTO> indexAllPointsGPS() {
        return gpsDataService.findAll()
                .stream()
                .map(this::convertToPointGpsDTO)
                .collect(Collectors.toList());
    }

    private GpsDataDTO convertToPointGpsDTO(GpsData gpsData) {
        return modelMapper.map(gpsData, GpsDataDTO.class);
    }

//    private GpsDataDTO convertToPointGpsDTO_forAPI(GpsData gpsData, String clientTimeZone) {
//
//        GpsDataDTO gpsDataDTO = modelMapper.map(gpsData, GpsDataDTO.class);
//
//        // Преобразование времени
//        LocalDateTime utcPurchaseTime = gpsData.getTimestamp();
//        ZoneId clientZoneId = ZoneId.of(clientTimeZone);
//
//        // Преобразуем время из UTC в таймзону клиента
//        LocalDateTime clientPurchaseTime = utcPurchaseTime
//                .atZone(ZoneId.of("UTC"))
//                .withZoneSameInstant(clientZoneId)
//                .toLocalDateTime();
//
//        // Форматируем дату для удобства чтения
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
//        gpsDataDTO.setTimestamp(LocalDateTime.parse(clientPurchaseTime.format(formatter)));
//
//        return gpsDataDTO;
//    }

    @GetMapping("/{id}/enterprises")
    @PreAuthorize("#id == authentication.principal.person.id")
    @ResponseStatus(HttpStatus.OK)
    public List<Enterprise> indexEnterprise(@PathVariable("id") Long id) {
        checkManager(id);
        return enterpriseService.findAllForManager(id);
    }

    @GetMapping("/{id}/enterprises/{enterpriseId}")
    @PreAuthorize("#id == authentication.principal.person.id")
    @ResponseStatus(HttpStatus.OK)
    public Enterprise indexOneEnterprise(@PathVariable("id") Long id,
                                         @PathVariable("enterpriseId") Long enterpriseId) {
        checkManager(id);
        return enterpriseService.findOne(enterpriseId);
    }


    @GetMapping("/{id}/vehicles")
    public List<VehicleDTO> indexVehicles(@PathVariable("id") Long id,
                                          @RequestParam(defaultValue = "1", value = "page", required = false) Integer page,
                                          @RequestParam(defaultValue = "20", value = "size", required = false) Integer size,
                                          @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone) {
        checkManager(id);
        if (page == null || size == null) {
            return vehicleService.findAllForManager(id)
                    .stream()
                    .map(vehicle -> convertToVehicleDTO(vehicle, clientTimeZone))
                    .collect(Collectors.toList());
        }
        Page<Vehicle> vehiclePage = vehicleService.findAllForManager(id, page, size);
        return vehiclePage.getContent()
                .stream()
                .map(vehicle -> convertToVehicleDTO(vehicle, clientTimeZone))
                .collect(Collectors.toList());
    }

//    @GetMapping("/{id}/vehicles")
//    @PreAuthorize("#id == authentication.principal.person.id")
//    @ResponseStatus(HttpStatus.OK)
//    public List<VehicleDTO> indexVehicles(@PathVariable("id") Long id) {
//        checkManager(id);
//        return vehicleService.findAllForManager(id).stream()
//                .map(this::convertToVehicleDTO)
//                .toList();
//    }

    @GetMapping("/{id}/vehicles/{vehicleId}")
    @PreAuthorize("#id == authentication.principal.person.id")
    @ResponseStatus(HttpStatus.OK)
    public VehicleDTO indexOneVehicle(@PathVariable("id") Long id,
                                      @PathVariable("vehicleId") Long vehicleId,
                                      @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone) {
        checkManager(id);
        return convertToVehicleDTO(vehicleService.findOne(vehicleId), clientTimeZone);
    }

//    @GetMapping("/{id}/drivers")
//    @PreAuthorize("#id == authentication.principal.person.id")
//    @ResponseStatus(HttpStatus.OK)
//    public List<DriverDTO> indexDrivers(@PathVariable("id") Long id) {
//        checkManager(id);
//        return driverService.findAllForManager(id).stream()
//                .map(this::convertToDriverDTO)
//                .toList();
//    }

    @GetMapping("/{id}/drivers")
    public List<DriverDTO> indexDrivers(@PathVariable("id") Long id,
                                        @RequestParam(defaultValue = "1", value = "page", required = false) Integer page,
                                        @RequestParam(defaultValue = "20", value = "size", required = false) Integer size) {
        checkManager(id);
        if (page == null || size == null) {
            return driverService.findAllForManager(id)
                    .stream()
                    .map(this::convertToDriverDTO)
                    .collect(Collectors.toList());
        }
        Page<Driver> driverPage = driverService.findAllForManager(id, page, size);
        return driverPage.getContent()
                .stream()
                .map(this::convertToDriverDTO)
                .collect(Collectors.toList());
    }

    private VehicleDTO convertToVehicleDTO(Vehicle vehicle, String clientTimeZone) {
        VehicleDTO vehicleDTO = modelMapper.map(vehicle, VehicleDTO.class);

        // Преобразование времени
        LocalDateTime utcPurchaseTime = vehicle.getPurchaseTime();
        ZoneId clientZoneId = ZoneId.of(clientTimeZone);

        // Преобразуем время из UTC в таймзону клиента
        LocalDateTime clientPurchaseTime = utcPurchaseTime
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(clientZoneId)
                .toLocalDateTime();

        // Форматируем дату для удобства чтения
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        vehicleDTO.setPurchaseTime(clientPurchaseTime.format(formatter));

        return vehicleDTO;
    }

    // выводит время в зоне UTC и запрос в том же
    @GetMapping("/vehicle/{vehicleId}/track")
    public Object getTrackByVehicleAndTimeRange(
            @PathVariable Long vehicleId,
            @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone,
            @RequestParam LocalDateTime dateFrom,
            @RequestParam LocalDateTime dateTo,
            @RequestParam(defaultValue = "geojson") String format) {

        // Получаем предприятие, к которому принадлежит автомобиль
        Vehicle vehicle = vehicleService.findOne(vehicleId);
        Enterprise enterprise = enterpriseService.findOne(vehicle.getEnterprise().getId());

        // Получаем таймзону предприятия
        String enterpriseTimeZone = enterprise.getTimezone() != null ? enterprise.getTimezone() : "UTC";

        // Получаем данные GPS в UTC
        List<GpsData> gpsDataList = gpsDataService.findByVehicleAndTimeRange(vehicleId, dateFrom, dateTo);

        // Преобразуем данные GPS в DTO с учетом таймзон
        List<GpsDataDTO> gpsDataDTOList = gpsDataList.stream()
                .map(gpsData -> convertToPointGpsDTO_forAPI(gpsData, clientTimeZone, enterpriseTimeZone))
                .collect(Collectors.toList());

        // Возвращаем данные в зависимости от формата
        if ("geojson".equalsIgnoreCase(format)) {
            return convertToGeoJSON(gpsDataDTOList);
        } else {
            return gpsDataDTOList;
        }
    }

    private GeoJSONResponse convertToGeoJSON(List<GpsDataDTO> gpsDataDTOList) {
        List<GeoJSONResponse.Feature> features = gpsDataDTOList.stream()
                .map(gpsDataDTO -> new GeoJSONResponse.Feature(
                        new GeoJSONResponse.Geometry(
                                        List.of(gpsDataDTO.getLongitude(),
                                                gpsDataDTO.getLatitude()) // Каждая точка [longitude, latitude]
                        ),
                        new GeoJSONResponse.Properties(gpsDataDTO.getTimestamp()) // Временная метка
                ))
                .collect(Collectors.toList());

        return new GeoJSONResponse(features);
    }

    private GpsDataDTO convertToPointGpsDTO_forAPI(GpsData gpsData, String clientTimeZone, String enterpriseTimeZone) {

        GpsDataDTO gpsDataDTO = modelMapper.map(gpsData, GpsDataDTO.class);

        // Преобразование времени
        LocalDateTime utcTimestamp = gpsData.getTimestamp();
        ZoneId clientZoneId = ZoneId.of(clientTimeZone);
        ZoneId enterpriseZoneId = ZoneId.of(enterpriseTimeZone);

        // Преобразуем время из UTC в таймзону предприятия
        LocalDateTime enterpriseTimestamp = utcTimestamp.atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(enterpriseZoneId)
                .toLocalDateTime();

        // Преобразуем время из таймзоны предприятия в таймзону клиента
        LocalDateTime clientTimestamp = enterpriseTimestamp.atZone(enterpriseZoneId)
                .withZoneSameInstant(clientZoneId)
                .toLocalDateTime();

        // Форматируем дату для удобства чтения
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        // Устанавливаем преобразованное время в DTO
        gpsDataDTO.setTimestamp(clientTimestamp.format(formatter));

        return gpsDataDTO;
    }

    // время (возвращается) с учетом таймзоны предприятия, в запросе время предприятия!
    @GetMapping("/{managerId}/vehicle/{vehicleId}/trips")
    public List<TripDTO> getTripsByVehicle(
            @PathVariable Long managerId,
            @PathVariable Long vehicleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        checkManager(managerId);

        // Получаем информацию о машине и предприятии
        Vehicle vehicle = vehicleService.findOne(vehicleId);
        Enterprise enterprise = vehicle.getEnterprise();

        String enterpriseTimeZone = enterprise.getTimezone() != null ? enterprise.getTimezone() : "UTC";

        // Конвертируем время из таймзоны предприятия в UTC
        LocalDateTime startUtc = TimeZoneUtil.convertToUtc(startTime, enterpriseTimeZone);
        LocalDateTime endUtc = TimeZoneUtil.convertToUtc(endTime, enterpriseTimeZone);

        // Получаем поездки
        List<Trip> trips = tripService.findTripsForVehicleInTimeRange(vehicleId, startUtc, endUtc);

        // Конвертируем поездки в DTO
        return trips.stream()
                .map(this::convertToTripDTO)
                .collect(Collectors.toList());
    }

    // время (возвращается) с учетом таймзоны предприятия, в запросе время предприятия!
    @GetMapping("/{managerId}/vehicle/{vehicleId}/track")
    public Object getTrackByVehicleAndTimeRange(
            @PathVariable Long managerId,
            @PathVariable Long vehicleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "geojson") String format) {
        checkManager(managerId);

        // Получаем информацию о машине и предприятии
        Vehicle vehicle = vehicleService.findOne(vehicleId);
        Enterprise enterprise = vehicle.getEnterprise();

        String enterpriseTimeZone = enterprise.getTimezone() != null ? enterprise.getTimezone() : "UTC";

        // Конвертируем время из таймзоны предприятия в UTC
        LocalDateTime startUtc = TimeZoneUtil.convertToUtc(startTime, enterpriseTimeZone);
        LocalDateTime endUtc = TimeZoneUtil.convertToUtc(endTime, enterpriseTimeZone);

        // Получаем поездки
        List<Trip> trips = tripService.findTripsForVehicleInTimeRange(vehicleId, startUtc, endUtc);

        // Собираем точки GPS из всех поездок
        List<GpsData> gpsDataList = new ArrayList<>();
        for (Trip trip : trips) {
            gpsDataList.addAll(gpsDataService.findByVehicleAndTimeRange(vehicleId, trip.getStartTime(), trip.getEndTime()));
        }

        // Преобразуем GPS-данные в DTO
        List<GpsDataDTO> gpsDataDTOList = gpsDataList.stream()
                .map(gpsData -> convertToPointGpsDTO_forAPI(gpsData, enterpriseTimeZone, enterpriseTimeZone))
                .collect(Collectors.toList());

        // Возвращаем данные в зависимости от формата
        if ("geojson".equalsIgnoreCase(format)) {
            return convertToGeoJSON(gpsDataDTOList);
        }

        return gpsDataDTOList;
    }

    private TripDTO convertToTripDTO(Trip trip) {
        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(trip.getId());

        // Форматируем даты для удобства чтения
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        tripDTO.setStartTime(trip.getStartTime().format(formatter));
        tripDTO.setEndTime(trip.getEndTime().format(formatter));

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

        // Рассчитываем продолжительность
        Duration duration = Duration.between(trip.getStartTime(), trip.getEndTime());
        tripDTO.setDuration(formatDuration(duration));

        return tripDTO;
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return String.format("%d hours, %d minutes", hours, minutes);
    }

    public VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    public Vehicle convertToVehicle(VehicleDTO vehicleDTO) {
        return modelMapper.map(vehicleDTO, Vehicle.class);
    }

    public DriverDTO convertToDriverDTO(Driver driver) {
        return modelMapper.map(driver, DriverDTO.class);
    }

    public Enterprise convertToEnterprise(EnterpriseDTO enterpriseDTO) {
        return modelMapper.map(enterpriseDTO, Enterprise.class);
    }

    @PostMapping("/{id}/vehicles")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid VehicleDTO vehicleDTO,
                                             BindingResult bindingResult,
                                             @PathVariable("id") Long id) {
        checkManager(id);
        if (bindingResult.hasErrors()) {
            throw new VehicleNotCreatedException("Vehicle not created");
        }
        vehicleService.save(convertToVehicle(vehicleDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/{id}/vehicles/{idVehicle}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid VehicleUpdateDTO vehicleUpdateDTO,
                                             BindingResult bindingResult,
                                             @PathVariable("idVehicle") Long id,
                                             @PathVariable("id") Long managerId) {
        checkManager(managerId);
        if (bindingResult.hasErrors()) {
            throw new VehicleNotUpdatedException("Vehicle not found");
        }
        var vehicle = vehicleService.findOne(id);
        vehicleMapper.update(vehicleUpdateDTO, vehicle);
        vehicleService.update(id, vehicle);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PostMapping("/{id}/enterprises")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid EnterpriseDTO enterpriseDTO,
                                             BindingResult bindingResult,
                                             @PathVariable("id") Long id) {
        checkManager(id);
        if (bindingResult.hasErrors()) {
            throw new EnterpriseNotCreatedException("Enterprise not created");
        }
        enterpriseService.save(convertToEnterprise(enterpriseDTO), id);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/{id}/enterprises/{idEnterprise}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid EnterpriseUpdateDTO enterpriseUpdateDTO,
                                             @PathVariable("id") Long idManager,
                                             BindingResult bindingResult,
                                             @PathVariable("idEnterprise") Long idEnterprise) {
        checkManager(idManager);
        if (bindingResult.hasErrors()) {
            throw new EnterpriseNotUpdatedException
                    (EnterpriseNotUpdatedException.class.descriptorString());
        }
        var enterprise = enterpriseService.findOne(idEnterprise);
        enterpriseMapper.update(enterpriseUpdateDTO, enterprise);
        enterpriseService.update(idManager, idEnterprise, enterprise);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/enterprises/{idEnterprise}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long idManager,
                                             @PathVariable("idEnterprise") Long idEnterprise) {
        checkManager(idManager);
        enterpriseService.delete(idManager, idEnterprise);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/vehicles/{idVehicle}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<HttpStatus> deleteVehicle(@PathVariable("idVehicle") Long id,
                                                    @PathVariable("id") Long idManager) {
        checkManager(idManager);
        vehicleService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }




    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(VehicleNotCreatedException.class)
    public ResponseEntity<String> handleVehicleNotCreatedException(VehicleNotCreatedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<String> handleVehicleNotFoundException(VehicleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(VehicleNotUpdatedException.class)
    public ResponseEntity<String> handleVehicleNotUpdatedException(VehicleNotUpdatedException ex) {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(ex.getMessage());
    }

    @ExceptionHandler(EnterpriseNotCreatedException.class)
    public ResponseEntity<String> handleEnterpriseNotCreatedException(EnterpriseNotCreatedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EnterpriseNotUpdatedException.class)
    public ResponseEntity<String> handleEnterpriseNotUpdatedException(EnterpriseNotUpdatedException ex) {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(ex.getMessage());
    }



}
