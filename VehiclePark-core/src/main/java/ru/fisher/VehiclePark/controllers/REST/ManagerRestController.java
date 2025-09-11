package ru.fisher.VehiclePark.controllers.REST;

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

    @GetMapping("/{id}/chat-id")
    public ResponseEntity<String> getChatId(@PathVariable Long id) {
        return managerRepository.findById(id)
                .map(manager -> ResponseEntity.ok(String.valueOf(manager.getChatId())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/gps/vehicle/{vehicleId}")
    public List<GpsDataDTO> getGPSDataByVehicle(@PathVariable Long vehicleId) {
        return gpsDataService.findByVehicleId(vehicleId)
                .stream()
                .map(gpsDataMapper::convertToPointGpsDTO)
                .toList();
    }

//    @GetMapping("/gps/vehicle/{vehicleId}")
//    public Flux<GpsDataDTO> getGPSDataByVehicle(@PathVariable Long vehicleId) {
//        return gpsDataService.findByVehicleIdReactive(vehicleId)
//                .map(gpsDataMapper::convertToPointGpsDTO);
//    }

    @GetMapping("/allPoints")
    public List<GpsDataDTO> indexAllPointsGPS() {
        return gpsDataService.findAll()
                .stream()
                .map(gpsDataMapper::convertToPointGpsDTO)
                .toList();
    }

    @GetMapping("/{id}/enterprises")
    @PreAuthorize("#id == authentication.principal.manager.id")
    @ResponseStatus(HttpStatus.OK)
    public List<Enterprise> indexEnterprise(@PathVariable("id") Long id) {
        authContextService.checkManager(id);
        return enterpriseService.findAllForManager(id);
    }

    @GetMapping("/{id}/enterprises/{enterpriseId}")
    @PreAuthorize("#id == authentication.principal.manager.id")
    @ResponseStatus(HttpStatus.OK)
    public Enterprise indexOneEnterprise(@PathVariable("id") Long id,
                                         @PathVariable("enterpriseId") Long enterpriseId) {
        authContextService.checkManager(id);
        return enterpriseService.findOne(enterpriseId);
    }


    @GetMapping("/{id}/vehicles")
    public List<VehicleDTO> indexVehicles(
            @PathVariable("id") Long id,
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

    @GetMapping("/{id}/vehicles/{vehicleId}")
    // @PreAuthorize("#id == authentication.principal.manager.id")
    @ResponseStatus(HttpStatus.OK)
    public VehicleDTO indexOneVehicle(@PathVariable("id") Long id,
                                      @PathVariable("vehicleId") Long vehicleId,
                                      @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone) {
        // authContextService.checkManager(id);
        return vehicleService.convertToVehicleDTO(vehicleService.findOne(vehicleId), clientTimeZone);
    }

    @GetMapping("/{id}/drivers")
    public List<DriverDTO> indexDrivers(@PathVariable("id") Long id,
                                        @RequestParam(defaultValue = "1", value = "page", required = false) Integer page,
                                        @RequestParam(defaultValue = "5", value = "size", required = false) Integer size) {
        // authContextService.checkManager(id);
        Page<Driver> driverPage = driverService.findAllForManager(id, page, size);
        return driverPage.getContent()
                .stream()
                .map(driverService::convertToDriverDTO)
                .toList();
    }

    @GetMapping("/{id}/drivers/{driverId}")
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO indexOneDriver(@PathVariable("id") Long id,
                                    @PathVariable("driverId") Long driverId) {
        // authContextService.checkManager(id);
        return driverService.convertToDriverDTO(driverService.findOne(driverId));
    }

    // время (возвращается) с учетом таймзоны клиента
    @GetMapping("/{managerId}/vehicle/{vehicleId}/trips")
    public List<TripDTO> getTripsByVehicle(
            @PathVariable Long managerId,
            @PathVariable Long vehicleId,
            @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        authContextService.checkManager(managerId);
        Vehicle vehicle = vehicleService.findOne(vehicleId);
        return tripService.getTripsForVehicleWithTimezone(vehicle, clientTimeZone, startTime, endTime);
    }

    // время (возвращается) с учетом таймзоны клиента
    @GetMapping("/{managerId}/vehicle/{vehicleId}/track")
    public ResponseEntity<?> getTrackByVehicleAndTimeRange(
            @PathVariable Long managerId,
            @PathVariable Long vehicleId,
            @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "geojson") String format) {
        authContextService.checkManager(managerId);
        return ResponseEntity.of(Optional.ofNullable(gpsTrackService.getTrackForVehicle
                (vehicleId, startTime, endTime, clientTimeZone, format)));
    }

    @PostMapping("/{id}/vehicles")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid VehicleDTO vehicleDTO,
                                             BindingResult bindingResult,
                                             @PathVariable("id") Long id) {
        authContextService.checkManager(id);
        if (bindingResult.hasErrors()) {
            throw new VehicleNotCreatedException("Vehicle not created");
        }
        vehicleService.save(vehicleService.convertToVehicle(vehicleDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/{id}/vehicles/{idVehicle}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid VehicleUpdateDTO vehicleUpdateDTO,
                                             BindingResult bindingResult,
                                             @PathVariable("idVehicle") Long id,
                                             @PathVariable("id") Long managerId) {
        authContextService.checkManager(id);
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
        authContextService.checkManager(id);
        if (bindingResult.hasErrors()) {
            throw new EnterpriseNotCreatedException("Enterprise not created");
        }
        Enterprise enterprise = enterpriseService.convertToEnterprise(enterpriseDTO);
        enterpriseService.save(enterprise, id);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/{id}/enterprises/{idEnterprise}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid EnterpriseUpdateDTO enterpriseUpdateDTO,
                                             @PathVariable("id") Long idManager,
                                             BindingResult bindingResult,
                                             @PathVariable("idEnterprise") Long idEnterprise) {
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

    @DeleteMapping("/{id}/enterprises/{idEnterprise}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long idManager,
                                             @PathVariable("idEnterprise") Long idEnterprise) {
        authContextService.checkManager(idManager);
        enterpriseService.delete(idManager, idEnterprise);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/vehicles/{idVehicle}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<HttpStatus> deleteVehicle(@PathVariable("idVehicle") Long id,
                                                    @PathVariable("id") Long idManager) {
        authContextService.checkManager(idManager);
        vehicleService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
