package ru.fisher.VehiclePark.controllers.REST;//package ru.fisher.VehiclePark.controllers.REST;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//import ru.fisher.VehiclePark.dto.MileageReportDTO;
//import ru.fisher.VehiclePark.models.Manager;
//import ru.fisher.VehiclePark.models.Period;
//import ru.fisher.VehiclePark.repositories.jpa.ManagerRepository;
//import ru.fisher.VehiclePark.services.reactive.ReportServiceReactive;
//
//import java.time.LocalDateTime;
//import java.util.Objects;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/reports/reactive")
//public class ReportReactiveController {
//
//
//    private final ReportServiceReactive reportService;
//
//    @Autowired
//    public ReportReactiveController(ReportServiceReactive reportService) {
//        this.reportService = reportService;
//    }
//
//    @GetMapping("/totalMileage")
//    public Mono<ResponseEntity<MileageReportDTO>> getTotalMileage(
//            @RequestParam Period period,
//            @RequestParam LocalDateTime start,
//            @RequestParam LocalDateTime end) {
//        start = LocalDateTime.now().minusYears(5);
//        end = LocalDateTime.now();
//
//        return reportService.generateTotalMileageReport(start, end, period)
//                .map(ResponseEntity::ok);
//    }
//}
