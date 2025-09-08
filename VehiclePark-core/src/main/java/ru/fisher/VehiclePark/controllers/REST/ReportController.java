package ru.fisher.VehiclePark.controllers.REST;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.dto.MileageReportDTO;
import ru.fisher.VehiclePark.dto.TripDTO;
import ru.fisher.VehiclePark.exceptions.ResourceNotFoundException;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.models.Period;
import ru.fisher.VehiclePark.models.ReportType;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.repositories.jpa.ManagerRepository;
import ru.fisher.VehiclePark.services.AuthContextService;
import ru.fisher.VehiclePark.services.ReportService;
import ru.fisher.VehiclePark.services.TripService;
import ru.fisher.VehiclePark.services.VehicleService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final AuthContextService authContextService;
    private final ManagerRepository managerRepository;
    private final VehicleService vehicleService;
    private final TripService tripService;

    public ReportController(ReportService reportService, AuthContextService authContextService,
                            ManagerRepository managerRepository, VehicleService vehicleService, TripService tripService) {
        this.reportService = reportService;
        this.authContextService = authContextService;
        this.managerRepository = managerRepository;
        this.vehicleService = vehicleService;
        this.tripService = tripService;
    }

    @GetMapping("/mileage")
    public ResponseEntity<MileageReportDTO> getMileageReport(
            @RequestParam(required = false) String vehicleNumber,
            @RequestParam(required = false) Long enterpriseId,
            @RequestParam ReportType reportType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam Period period) {

        MileageReportDTO report;
        Manager currentManager = authContextService.getCurrentManager();

        switch (reportType) {
            case VEHICLE_MILEAGE -> report = reportService.
                    generateMileageReport(currentManager, vehicleNumber, startDate, endDate, period);
            case ENTERPRISE_MILEAGE -> report = reportService.
                    generateEnterpriseMileageReport(currentManager, enterpriseId, startDate, endDate, period);
            case TOTAL_MILEAGE -> report = reportService.
                    generateTotalMileageReport(currentManager, startDate, endDate, period);
            default -> throw new IllegalArgumentException("Неизвестный тип отчета: " + reportType);
        }
        return ResponseEntity.ok(report);
    }


    @GetMapping("/test/vehicleMileage")
    public ResponseEntity<MileageReportDTO> testVehicleMileageReport(
            @RequestParam(required = false) String vehicleNumber,
            @RequestParam(required = false) Long enterpriseId,
            @RequestParam(required = false) Period period) {

        MileageReportDTO report;
        Optional<Manager> currentManager = managerRepository.findByUsername("Ivan");
        LocalDateTime startDate = LocalDateTime.now().minusYears(5);
        LocalDateTime endDate = LocalDateTime.now();
        period = Period.DAY;
        report = reportService.generateMileageReport(Objects.requireNonNull(currentManager.orElse(null)),
                "Х537ВН16", startDate, endDate, period);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/test/enterpriseMileage")
    public ResponseEntity<MileageReportDTO> testEnterpriseMileageReport(
            @RequestParam(required = false) String vehicleNumber,
            @RequestParam(required = false) Long enterpriseId,
            @RequestParam(required = false) Period period) {

        MileageReportDTO report;
        Optional<Manager> currentManager = managerRepository.findByUsername("Ivan");
        LocalDateTime startDate = LocalDateTime.now().minusYears(5);
        LocalDateTime endDate = LocalDateTime.now();
        period = Period.DAY;
        report = reportService.generateEnterpriseMileageReport
                (Objects.requireNonNull(currentManager.orElse(null)),
                        1L, startDate, endDate, period);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/test/totalMileage")
    public ResponseEntity<MileageReportDTO> testTotalMileageReport(
            @RequestParam(required = false) String vehicleNumber,
            @RequestParam(required = false) Long enterpriseId,
            @RequestParam(required = false) Period period) {

        MileageReportDTO report;
        Optional<Manager> currentManager = managerRepository.findByUsername("Ivan");
        LocalDateTime startDate = LocalDateTime.now().minusYears(5);
        LocalDateTime endDate = LocalDateTime.now();
        period = Period.DAY;
        report = reportService.generateTotalMileageReport
                (Objects.requireNonNull(currentManager.orElse(null)), startDate, endDate, period);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/test/{vehicleId}/trips")
    public List<TripDTO> testTripsByVehicle(
            @PathVariable Long vehicleId,
            @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone) {
        // authContextService.checkManager(managerId);
        Vehicle vehicle = vehicleService.findOne(vehicleId);
        LocalDateTime startTime = LocalDateTime.now().minusYears(5);
        LocalDateTime endTime = LocalDateTime.now();
        return tripService.getTripsForVehicleWithTimezone(vehicle, clientTimeZone, startTime, endTime);
    }

}
