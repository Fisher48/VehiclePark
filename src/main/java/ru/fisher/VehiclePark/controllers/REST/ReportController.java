package ru.fisher.VehiclePark.controllers.REST;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.dto.MileageReportDTO;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.models.Period;
import ru.fisher.VehiclePark.models.ReportType;
import ru.fisher.VehiclePark.services.AuthContextService;
import ru.fisher.VehiclePark.services.ReportService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final AuthContextService authContextService;

    public ReportController(ReportService reportService, AuthContextService authContextService) {
        this.reportService = reportService;
        this.authContextService = authContextService;
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

}
