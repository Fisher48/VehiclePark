package ru.fisher.VehiclePark.controllers.REST;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.fisher.VehiclePark.dto.*;
import ru.fisher.VehiclePark.exceptions.*;
import ru.fisher.VehiclePark.mapper.EnterpriseMapper;
import ru.fisher.VehiclePark.mapper.GpsDataMapper;
import ru.fisher.VehiclePark.mapper.VehicleMapper;
import ru.fisher.VehiclePark.models.*;
import ru.fisher.VehiclePark.repositories.jpa.ManagerRepository;
import ru.fisher.VehiclePark.services.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Tag(name = "Контроллер Менеджера API",
        description = "Основной контроллер по управлению всеми объектами")
@Slf4j
@RestController
@RequestMapping("/api/managers")
public class ManagerRestController {

    private final EnterpriseService enterpriseService;
    private final DriverService driverService;
    private final VehicleService vehicleService;
    private final GpsDataMapper gpsDataMapper;
    private final VehicleMapper vehicleMapper;
    private final EnterpriseMapper enterpriseMapper;
    private final ManagerService managerService;
    private final GpsDataService gpsDataService;
    private final TripService tripService;
    private final GpsTrackService gpsTrackService;
    private final AuthContextService authContextService;
    private final ManagerRepository managerRepository;

    @Autowired
    public ManagerRestController(EnterpriseService enterpriseService, DriverService driverService,
                                 VehicleService vehicleService, GpsDataMapper gpsDataMapper,
                                 VehicleMapper vehicleMapper, EnterpriseMapper enterpriseMapper,
                                 ManagerService managerService, GpsDataService gpsDataService,
                                 TripService tripService, GpsTrackService gpsTrackService,
                                 AuthContextService authContextService, ManagerRepository managerRepository) {
        this.enterpriseService = enterpriseService;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
        this.gpsDataMapper = gpsDataMapper;
        this.vehicleMapper = vehicleMapper;
        this.enterpriseMapper = enterpriseMapper;
        this.managerService = managerService;
        this.gpsDataService = gpsDataService;
        this.tripService = tripService;
        this.gpsTrackService = gpsTrackService;
        this.authContextService = authContextService;
        this.managerRepository = managerRepository;
    }

    @GetMapping
    public ModelAndView start(Model model) {
        String username = authContextService.getCurrentManager().getUsername();
        log.info(username);
        model.addAttribute("manager", managerService.findByUsername(username));
        return new ModelAndView("start");
    }

    @Operation(summary = "ChatID telegram аккаунта",
            description = "Позволяет получить ChatID менеджера в телеграмме")
    @GetMapping("/{id}/chat-id")
    public ResponseEntity<String> getChatId(@PathVariable
                                            @Parameter(description = "ID менеджера") Long id) {
        return managerRepository.findById(id)
                .map(manager -> ResponseEntity.ok(String.valueOf(manager.getChatId())))
                .orElse(ResponseEntity.notFound().build());
    }

//    @GetMapping("/gps/vehicle/{vehicleId}")
//    public List<GpsDataDTO> getGPSDataByVehicle(@PathVariable
//                                                @Parameter(description = "ID автомобиля")
//                                                Long vehicleId) {
//        return gpsDataService.findByVehicleId(vehicleId)
//                .stream()
//                .map(gpsDataMapper::convertToPointGpsDTO)
//                .toList();
//    }

//    @GetMapping("/gps/vehicle/{vehicleId}")
//    public Flux<GpsDataDTO> getGPSDataByVehicle(@PathVariable Long vehicleId) {
//        return gpsDataService.findByVehicleIdReactive(vehicleId)
//                .map(gpsDataMapper::convertToPointGpsDTO);
//    }

//    @GetMapping("/allPoints")
//    public List<GpsDataDTO> indexAllPointsGPS() {
//        return gpsDataService.findAll()
//                .stream()
//                .map(gpsDataMapper::convertToPointGpsDTO)
//                .toList();
//    }

    @Operation(summary = "Список предприятий",
            description = "Позволяет получить список предприятий для менеджера")
    @GetMapping("/{id}/enterprises")
    @PreAuthorize("#id == authentication.principal.manager.id")
    @ResponseStatus(HttpStatus.OK)
    public List<Enterprise> indexEnterprise(@PathVariable("id")
                                            @Parameter(description = "ID менеджера") Long id) {
        authContextService.checkManager(id);
        return enterpriseService.findAllForManager(id);
    }

    @Operation(summary = "Предприятие",
            description = "Конкретное предприятие")
    @GetMapping("/{id}/enterprises/{enterpriseId}")
    @PreAuthorize("#id == authentication.principal.manager.id")
    @ResponseStatus(HttpStatus.OK)
    public Enterprise indexOneEnterprise(@PathVariable("id")
                                         @Parameter(description = "ID менеджера") Long id,
                                         @PathVariable("enterpriseId")
                                         @Parameter(description = "ID предприятия") Long enterpriseId) {
        authContextService.checkManager(id);
        return enterpriseService.findOne(enterpriseId);
    }

    @Operation(summary = "Список автомобилей",
            description = "Позволяет получить список автомобилей для менеджера")
    @GetMapping("/{id}/vehicles")
    public List<VehicleDTO> indexVehicles(
            @PathVariable("id") @Parameter(description = "ID менеджера") Long id,
            @RequestParam(defaultValue = "1", value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "10", value = "size", required = false) Integer size,
            @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone) {
       // authContextService.checkManager(id);
        Page<Vehicle> vehiclePage = vehicleService.findAllForManager(id, page, size);
        return vehiclePage.getContent()
                .stream()
                .map(vehicle -> vehicleService.convertToVehicleDTO(vehicle, clientTimeZone))
                .toList();
    }

    @Operation(summary = "Автомобиль",
            description = "Информация об конкретном авто")
    @GetMapping("/{id}/vehicles/{vehicleId}")
    // @PreAuthorize("#id == authentication.principal.manager.id")
    @ResponseStatus(HttpStatus.OK)
    public VehicleDTO indexOneVehicle(@PathVariable("id")
                                      @Parameter(description = "ID менеджера") Long id,
                                      @PathVariable("vehicleId")
                                      @Parameter(description = "ID автомобиля") Long vehicleId,
                                      @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone) {
        // authContextService.checkManager(id);
        return vehicleService.convertToVehicleDTO(vehicleService.findOne(vehicleId), clientTimeZone);
    }

    @Operation(summary = "Список водителей",
            description = "Позволяет получить список водителей для менеджера")
    @GetMapping("/{id}/drivers")
    public List<DriverDTO> indexDrivers(@PathVariable("id") @Parameter(description = "ID менеджера") Long id,
                                        @RequestParam(defaultValue = "1", value = "page", required = false) Integer page,
                                        @RequestParam(defaultValue = "5", value = "size", required = false) Integer size) {
        // authContextService.checkManager(id);
        Page<Driver> driverPage = driverService.findAllForManager(id, page, size);
        return driverPage.getContent()
                .stream()
                .map(driverService::convertToDriverDTO)
                .toList();
    }

    @Operation(summary = "Водитель",
            description = "Информация о водителе")
    @GetMapping("/{id}/drivers/{driverId}")
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO indexOneDriver(@PathVariable("id")
                                    @Parameter(description = "ID менеджера") Long id,
                                    @PathVariable("driverId")
                                    @Parameter(description = "ID водителя") Long driverId) {
        // authContextService.checkManager(id);
        return driverService.convertToDriverDTO(driverService.findOne(driverId));
    }

    @Operation(summary = "Поездки",
            description = "Позволяет получить список поездок для автомобиля за определенный промежуток времени")
    // время (возвращается) с учетом таймзоны клиента
    @GetMapping("/{managerId}/vehicle/{vehicleId}/trips")
    public List<TripDTO> getTripsByVehicle(
            @PathVariable @Parameter(description = "ID менеджера") Long managerId,
            @PathVariable @Parameter(description = "ID автомобиля") Long vehicleId,
            @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone,
            @RequestParam @Parameter(description = "Дата начала поездок")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam  @Parameter(description = "Дата конца поездок")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        authContextService.checkManager(managerId);
        Vehicle vehicle = vehicleService.findOne(vehicleId);
        return tripService.getTripsForVehicleWithTimezone(vehicle, clientTimeZone, startTime, endTime);
    }

    @Operation(summary = "GPS-точки",
            description = "Позволяет получить gps-точки определенной поездки " +
                    "для автомобиля в указанном диапазоне")
    // время (возвращается) с учетом таймзоны клиента
    @GetMapping("/{managerId}/vehicle/{vehicleId}/track")
    public ResponseEntity<?> getTrackByVehicleAndTimeRange(
            @PathVariable @Parameter(description = "ID менеджера") Long managerId,
            @PathVariable @Parameter(description = "ID автомобиля") Long vehicleId,
            @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone,
            @RequestParam @Parameter(description = "Дата начала поездок")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @Parameter(description = "Дата конца поездок")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "geojson")
            @Parameter(description = "Формат данных: Json/Geojson")
            String format) {
        authContextService.checkManager(managerId);
        return ResponseEntity.of(Optional.ofNullable(gpsTrackService.getTrackForVehicle
                (vehicleId, startTime, endTime, clientTimeZone, format)));
    }

    @Operation(summary = "Создать новый автомобиль",
            description = "Позволяет создать авто для предприятия")
    @PostMapping("/{id}/vehicles")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HttpStatus> create(@RequestBody
                                             @Parameter(description = "Автомобиль")
                                             @Valid VehicleDTO vehicleDTO,
                                             BindingResult bindingResult,
                                             @PathVariable("id")
                                             @Parameter(description = "ID менеджера") Long id) {
        authContextService.checkManager(id);
        if (bindingResult.hasErrors()) {
            throw new VehicleNotCreatedException("Vehicle not created");
        }
        vehicleService.save(vehicleService.convertToVehicle(vehicleDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить автомобиль",
            description = "Позволяет обновлять информация об автомобиле")
    @PutMapping("/{id}/vehicles/{idVehicle}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HttpStatus> update(@RequestBody
                                             @Parameter(description = "Автомобиль")
                                             @Valid VehicleUpdateDTO vehicleUpdateDTO,
                                             BindingResult bindingResult,
                                             @PathVariable("idVehicle")
                                             @Parameter(description = "ID автомобиля") Long id,
                                             @PathVariable("id")
                                             @Parameter(description = "ID менеджера") Long managerId) {
        authContextService.checkManager(managerId);
        if (bindingResult.hasErrors()) {
            throw new VehicleNotUpdatedException("Vehicle not found");
        }
        var vehicle = vehicleService.findOne(id);
        vehicleMapper.update(vehicleUpdateDTO, vehicle);
        vehicleService.update(id, vehicle);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @Operation(summary = "Создать предприятие",
            description = "Позволяет создать новое предприятие для менеджера")
    @PostMapping("/{id}/enterprises")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HttpStatus> create(@RequestBody
                                             @Parameter(description = "Предприятие")
                                             @Valid EnterpriseDTO enterpriseDTO,
                                             BindingResult bindingResult,
                                             @PathVariable("id")
                                             @Parameter(description = "ID менеджера") Long managerId) {
        authContextService.checkManager(managerId);
        if (bindingResult.hasErrors()) {
            throw new EnterpriseNotCreatedException("Enterprise not created");
        }
        Enterprise enterprise = enterpriseService.convertToEnterprise(enterpriseDTO);
        enterpriseService.save(enterprise, managerId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить предприятие",
            description = "Позволяет обновить информацию о предприятии")
    @PutMapping("/{id}/enterprises/{idEnterprise}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HttpStatus> update(@RequestBody
                                             @Parameter(description = "Предприятие")
                                             @Valid EnterpriseUpdateDTO enterpriseUpdateDTO,
                                             @PathVariable("id")
                                             @Parameter(description = "ID менеджера") Long idManager,
                                             BindingResult bindingResult,
                                             @PathVariable("idEnterprise")
                                             @Parameter(description = "ID предприятия") Long idEnterprise) {
        authContextService.checkManager(idManager);
        if (bindingResult.hasErrors()) {
            throw new EnterpriseNotUpdatedException
                    (EnterpriseNotUpdatedException.class.descriptorString());
        }
        Enterprise enterprise = enterpriseService.findOne(idEnterprise);
        enterpriseMapper.update(enterpriseUpdateDTO, enterprise);
        enterpriseService.update(idManager, idEnterprise, enterprise);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Удалить предприятие",
            description = "Удаляет предприятие у менеджера")
    @DeleteMapping("/{id}/enterprises/{idEnterprise}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<HttpStatus> delete(@PathVariable("id")
                                             @Parameter(description = "ID менеджера") Long idManager,
                                             @PathVariable("idEnterprise")
                                             @Parameter(description = "ID предприятия") Long idEnterprise) {
        authContextService.checkManager(idManager);
        enterpriseService.delete(idManager, idEnterprise);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Удалить автомобиль",
            description = "Позволяет удалить автомобиль предприятия")
    @DeleteMapping("/{id}/vehicles/{idVehicle}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<HttpStatus> deleteVehicle(@PathVariable("idVehicle")
                                                    @Parameter(description = "ID автомобиля") Long id,
                                                    @PathVariable("id")
                                                    @Parameter(description = "ID менеджера") Long idManager) {
        authContextService.checkManager(idManager);
        vehicleService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
