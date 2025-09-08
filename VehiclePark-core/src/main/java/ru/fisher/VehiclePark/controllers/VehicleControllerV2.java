package ru.fisher.VehiclePark.controllers;//package ru.fisher.VehiclePark.controllers;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import ru.fisher.VehiclePark.dto.TripDTO;
//import ru.fisher.VehiclePark.mapper.TripMapper;
//import ru.fisher.VehiclePark.models.Vehicle;
//import ru.fisher.VehiclePark.services.TripService;
//import ru.fisher.VehiclePark.services.reactive.VehicleReactiveService;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//@RestController
//@RequestMapping("/api/v2")
//@RequiredArgsConstructor
//public class VehicleControllerV2 {
//
//    private final VehicleReactiveService vehicleService;
//    private final TripService tripService;
//    private final TripMapper tripMapper;
//
//    @GetMapping("/{managerId}/vehicles")
//    public Flux<Vehicle> getVehicles(@PathVariable Long managerId) {
//        return vehicleService.findAllForManager(managerId);
//    }
//
//    @GetMapping("/trips")
//    public CompletableFuture<ResponseEntity<List<TripDTO>>> getAllTrips() {
//        return tripService.findAllTripsAsync().thenApply(ResponseEntity::ok);
//    }
//
//}
