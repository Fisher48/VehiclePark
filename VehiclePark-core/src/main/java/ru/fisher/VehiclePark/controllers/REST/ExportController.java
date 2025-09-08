package ru.fisher.VehiclePark.controllers.REST;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.dto.ExportDTO;
import ru.fisher.VehiclePark.dto.TripDTO;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.kafka.NotificationEvent;
import ru.fisher.VehiclePark.kafka.NotificationKafkaProducer;
import ru.fisher.VehiclePark.mapper.TripMapper;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.services.EnterpriseService;
import ru.fisher.VehiclePark.services.ExportService;
import ru.fisher.VehiclePark.services.TripService;
import ru.fisher.VehiclePark.services.VehicleService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Value("${app.kafka.topic}")
    private String topic;

    private final EnterpriseService enterpriseService;
    private final VehicleService vehicleService;
    private final TripService tripService;
    private final ExportService exportService;
    private final TripMapper tripMapper;
    private final NotificationKafkaProducer notificationKafkaProducer;

    public ExportController(EnterpriseService enterpriseService, VehicleService vehicleService,
                            TripService tripService, ExportService exportService, TripMapper tripMapper, NotificationKafkaProducer notificationKafkaProducer) {
        this.enterpriseService = enterpriseService;
        this.vehicleService = vehicleService;
        this.tripService = tripService;
        this.exportService = exportService;
        this.tripMapper = tripMapper;
        this.notificationKafkaProducer = notificationKafkaProducer;
    }

    @GetMapping(value = "/enterprise/{enterpriseId}", produces = {MediaType.APPLICATION_JSON_VALUE, "text/csv"})
    public ResponseEntity<?> exportEnterpriseData(
            @PathVariable Long enterpriseId,
            @RequestParam String format,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo) {
        try {
            Enterprise enterprise = enterpriseService.findOne(enterpriseId);

            // Получаем списки машин и поездок
            List<VehicleDTO> vehicles = vehicleService.findAllByEnterpriseId(enterpriseId)
                    .stream()
                    .map(vehicleService::convertToVehicleDTO)
                    .toList();
            List<TripDTO> trips = tripService.findTripsForEnterpriseInRange(enterpriseId, dateFrom, dateTo)
                    .stream()
                    .map(tripMapper::convertToTripDTO)
                    .toList();

            // Экспортируем данные согласно формата
            ExportDTO exportDTO = new ExportDTO(enterprise, vehicles, trips);
            String result = exportService.export(exportDTO, format);

            MediaType mediaType = switch (format.toLowerCase()) {
                case "json" -> MediaType.APPLICATION_JSON;
                case "csv" -> MediaType.valueOf("text/csv");
                default -> MediaType.TEXT_PLAIN;
            };

            NotificationEvent event = new NotificationEvent(
                    format,
                    1L,
                    "Экспортированы данные предприятия: " + enterpriseId,
                    LocalDateTime.now().toString()
            );

            //notificationKafkaProducer.sendNotification();
            return ResponseEntity.ok().contentType(mediaType).body(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error exporting data: " + e.getMessage());
        }
    }

}
