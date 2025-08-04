//package ru.fisher.VehiclePark.services;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.cache.CacheManager;
//import org.springframework.test.context.ActiveProfiles;
//import ru.fisher.VehiclePark.dto.MileageReportDTO;
//import ru.fisher.VehiclePark.models.Manager;
//import ru.fisher.VehiclePark.models.Period;
//import ru.fisher.VehiclePark.models.Vehicle;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class ReportServiceIntegrationTest {
//
//    @Autowired
//    private ReportService reportService;
//    @Autowired
//    private ManagerService managerService;
//    @Autowired
//    private CacheManager cacheManager;
//    @Autowired
//    private VehicleService vehicleService;
//
//    private void logPerformance(String methodName, long withoutCache, long withCache) {
//        System.out.println("\n--- " + methodName + " ---");
//        System.out.printf("Без кэша: %.3f ms%n", withoutCache / 1_000_000.0);
//        System.out.printf("С кэшем: %.3f ms%n", withCache / 1_000_000.0);
//        System.out.printf("Ускорение: %.1f раз%n", (double) withoutCache / withCache);
//    }
//
//    @Test
//    void generateMileageReportForVehicle_shouldCacheResults() {
//        // Подготовка данных
//        Manager manager = managerService.findOne(1L);
//        Vehicle vehicle = vehicleService.findOne(1L);
//        LocalDateTime start = LocalDateTime.now().minusYears(5);
//        LocalDateTime end = LocalDateTime.now();
//
//        // Первый вызов (без кэша)
//        long start1 = System.nanoTime();
//        MileageReportDTO firstCall = reportService
//                .generateMileageReport(manager, vehicle.getNumber(), start, end, Period.MONTH);
//        long durationWithoutCache = System.nanoTime() - start1;
//
//        // Второй вызов (с кэшем)
//        long start2 = System.nanoTime();
//        MileageReportDTO secondCall = reportService
//                .generateMileageReport(manager, vehicle.getNumber(), start, end, Period.MONTH);
//        long durationWithCache = System.nanoTime() - start2;
//
//        logPerformance("generateMileageReportForVehicle", durationWithoutCache, durationWithCache);
//
//        // Проверки
//        assertEquals(firstCall, secondCall, "Результаты должны быть равны по содержимому");
//    }
//
//    @Test
//    void generateMileageReportForEnterprise_shouldCacheResults() {
//        // Подготовка данных
//        Manager manager = managerService.findOne(1L);
//        Long enterpriseId = 1L;
//        LocalDateTime start = LocalDateTime.now().minusMonths(3);
//        LocalDateTime end = LocalDateTime.now();
//
//        // Первый вызов (без кэша)
//        long start1 = System.nanoTime();
//        MileageReportDTO firstCall = reportService
//                .generateEnterpriseMileageReport(manager, enterpriseId, start, end, Period.MONTH);
//        long durationWithoutCache = System.nanoTime() - start1;
//
//        // Второй вызов (с кэшем)
//        long start2 = System.nanoTime();
//        MileageReportDTO secondCall = reportService
//                .generateEnterpriseMileageReport(manager, enterpriseId, start, end, Period.MONTH);
//        long durationWithCache = System.nanoTime() - start2;
//
//        logPerformance("generateMileageReportForEnterprise", durationWithoutCache, durationWithCache);
//
//        // Проверки
//        assertEquals(firstCall, secondCall, "Результаты должны быть равны по содержимому");
//    }
//}
