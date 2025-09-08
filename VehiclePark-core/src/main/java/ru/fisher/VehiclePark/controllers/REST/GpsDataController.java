package ru.fisher.VehiclePark.controllers.REST;//package ru.fisher.VehiclePark.controllers.REST;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import ru.fisher.VehiclePark.services.TrackGenerationService;
//
//@RestController
//@Slf4j
//@RequestMapping("/api/gps")
//public class GpsDataController {
//
//    private final TrackGenerationService trackGenerationService;
//
//    @Autowired
//    public GpsDataController(TrackGenerationService trackGenerationService) {
//        this.trackGenerationService = trackGenerationService;
//    }
//
//    @PostMapping("/generate-track/{vehicleId}")
//    public ResponseEntity<String> generateTrack(@PathVariable Long vehicleId,
//                                                @RequestParam double centerLat,
//                                                @RequestParam double centerLon,
//                                                @RequestParam double radius,
//                                                @RequestParam double trackLengthKm) {
//        try {
//            // Вызываем метод сервиса для генерации трека
//            trackGenerationService.generateTrack(vehicleId, centerLat, centerLon, radius, trackLengthKm);
//            return ResponseEntity.ok("Track generation started successfully.");
//        } catch (Exception e) {
//            log.error("Error generating track", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to generate track: " + e.getMessage());
//        }
//    }
//}
