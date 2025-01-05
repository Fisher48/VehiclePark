package ru.fisher.VehiclePark.controllers.REST;

import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.dto.TripDTO;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Trip;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.services.EnterpriseService;
import ru.fisher.VehiclePark.services.ExportService;
import ru.fisher.VehiclePark.services.TripService;
import ru.fisher.VehiclePark.services.VehicleService;
import ru.fisher.VehiclePark.util.GeoCoderService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    private final EnterpriseService enterpriseService;
    private final VehicleService vehicleService;
    private final TripService tripService;
    private final ExportService exportService;
    private final GeoCoderService geoCoderService;
    private final ModelMapper modelMapper;

    public ExportController(EnterpriseService enterpriseService, VehicleService vehicleService,
                            TripService tripService, ExportService exportService, GeoCoderService geoCoderService, ModelMapper modelMapper) {
        this.enterpriseService = enterpriseService;
        this.vehicleService = vehicleService;
        this.tripService = tripService;
        this.exportService = exportService;
        this.geoCoderService = geoCoderService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/enterprise/{enterpriseId}", produces = {MediaType.APPLICATION_JSON_VALUE, "text/csv"})
    public ResponseEntity<?> exportEnterpriseData(
            @PathVariable Long enterpriseId,
            @RequestParam String format,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo) {
        try {
            Enterprise enterprise = enterpriseService.findOne(enterpriseId);

            // Get vehicles and trips
            List<VehicleDTO> vehicles = vehicleService.findAllByEnterpriseId(enterpriseId)
                    .stream()
                    .map(this::convertToVehicleDTO)
                    .toList();
            List<TripDTO> trips = tripService.findTripsForEnterpriseInRange(enterpriseId, dateFrom, dateTo)
                    .stream()
                    .map(this::convertToTripDTO)
                    .toList();

            // Export data based on format
            if ("json".equalsIgnoreCase(format)) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(exportService.exportToJson(enterprise, vehicles, trips));
            } else if ("csv".equalsIgnoreCase(format)) {
                return ResponseEntity.ok()
                        .contentType(MediaType.valueOf("text/csv"))
                        .body(exportService.exportToCsv(enterprise, vehicles, trips));
            } else {
                return ResponseEntity.badRequest().body("Unsupported format: " + format);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error exporting data: " + e.getMessage());
        }
    }

    public VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
        return modelMapper.map(vehicle, VehicleDTO.class);
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



}
