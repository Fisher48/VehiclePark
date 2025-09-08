package ru.fisher.VehiclePark.controllers.REST;//package ru.fisher.VehiclePark.controllers.REST;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import ru.fisher.VehiclePark.dto.EnterpriseDTO;
//import ru.fisher.VehiclePark.dto.VehicleCreateDTO;
//import ru.fisher.VehiclePark.dto.VehicleDTO;
//import ru.fisher.VehiclePark.dto.VehicleUpdateDTO;
//import ru.fisher.VehiclePark.mapper.EnterpriseMapper;
//import ru.fisher.VehiclePark.mapper.GpsDataMapper;
//import ru.fisher.VehiclePark.mapper.VehicleMapper;
//import ru.fisher.VehiclePark.services.reactive.*;
//
//@RestController
//@RequestMapping("/api/reactive/managers")
//@RequiredArgsConstructor
//@Slf4j
//public class ReactiveManagerRestController {
//
//
//   // private final EnterpriseReactiveService enterpriseService;
//    private final VehicleReactiveService vehicleReactiveService;
//   // private final DriverReactiveService driverService;
//   // private final GpsDataReactiveService gpsDataService;
//   // private final TripReactiveService tripService;
//    private final GpsDataMapper gpsDataMapper;
//    private final VehicleMapper vehicleMapper;
//    private final EnterpriseMapper enterpriseMapper;
//   // private final AuthContextReactiveService authContextService;
//
//    @GetMapping("/{id}/vehicles/{vehicleId}")
//    public Mono<ResponseEntity<VehicleDTO>> getVehicle(
//            @PathVariable("id") Long id,
//            @PathVariable("vehicleId") Long vehicleId,
//            @RequestParam(defaultValue = "UTC") String clientTimeZone) {
//        return vehicleReactiveService.findById(vehicleId, clientTimeZone)
//                .map(ResponseEntity::ok)
//                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
//    }
//
//    @PostMapping("/{id}/vehicles")
//    public Mono<ResponseEntity<VehicleDTO>> createVehicle(@RequestBody @Valid Mono<VehicleDTO> vehicleDTO) {
//        return vehicleReactiveService.save(vehicleDTO)
//                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
//    }
//
//    @PutMapping("/{id}/vehicles/{vehicleId}")
//    public Mono<ResponseEntity<VehicleDTO>> updateVehicle(
//            @PathVariable("vehicleId") Long vehicleId,
//            @RequestBody @Valid Mono<VehicleUpdateDTO> updateDTO) {
//        return vehicleReactiveService.update(vehicleId, updateDTO)
//                .map(ResponseEntity::ok);
//    }
//
//    @DeleteMapping("/{id}/vehicles/{vehicleId}")
//    public Mono<ResponseEntity<Void>> deleteVehicle(@PathVariable("vehicleId") Long vehicleId) {
//        return vehicleReactiveService.deleteById(vehicleId)
//                .then(Mono.just(ResponseEntity.noContent().build()));
//    }
//}
