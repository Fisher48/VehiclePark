package ru.fisher.VehiclePark.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VehicleServiceIntegrationTest {

    @Autowired
    private VehicleService vehicleService;
    private final Long testManagerId = 1L;

    private void logPerformance(String methodName, long withoutCache, long withCache) {
        System.out.println("\n--- " + methodName + " ---");
        System.out.printf("Без кэша: %.3f ms%n", withoutCache / 1_000_000.0);
        System.out.printf("С кэшем: %.3f ms%n", withCache / 1_000_000.0);
        System.out.printf("Ускорение: %.1f раз%n", (double) withoutCache / withCache);
    }

    @Test
    void findAllForManager_shouldAccelerateWithCache() {
        // Первый вызов (без кэша)
        long start1 = System.nanoTime();
        vehicleService.findAllForManager(testManagerId, 1, 10);
        long durationWithoutCache = System.nanoTime() - start1;

        // Второй вызов (с кэшем)
        long start2 = System.nanoTime();
        vehicleService.findAllForManager(testManagerId, 1, 10);
        long durationWithCache = System.nanoTime() - start2;

        logPerformance("---allVehiclesForManager---", durationWithoutCache, durationWithCache);

        // Утверждаем, что вызов с кэшем как минимум в 2 раза быстрее (с запасом)
        assertTrue(durationWithCache < durationWithoutCache / 2,
                "Кэш не дал ускорения: " + durationWithoutCache + " vs " + durationWithCache);
    }

    @Test
    void findAllForManagerByEnterpriseId_shouldAccelerateWithCache() {
        // Первый вызов (без кэша)
        long start1 = System.nanoTime();
        Long enterpriseId = 2L;
        vehicleService.findAllForManagerByEnterpriseId(testManagerId, enterpriseId, 1,20);
        long durationWithoutCache = System.nanoTime() - start1;

        // Второй вызов (с кэшем)
        long start2 = System.nanoTime();
        vehicleService.findAllForManagerByEnterpriseId(testManagerId, enterpriseId, 1,20);
        long durationWithCache = System.nanoTime() - start2;

        logPerformance("---vehiclesForManagerByEnterprise---",
                durationWithoutCache, durationWithCache);

        // Утверждаем, что вызов с кэшем как минимум в 2 раза быстрее (с запасом)
        assertTrue(durationWithCache < durationWithoutCache / 2,
                "Кэш не дал ускорения: " + durationWithoutCache + " vs " + durationWithCache);
    }

    @Test
    void findVehicleByVehicleNumber() {
        String vehicleNumber = "Р414АЕ39";

        // Первый вызов (без кэша)
        long start1 = System.nanoTime();
        vehicleService.findVehicleByNumber(vehicleNumber);
        long durationWithoutCache = System.nanoTime() - start1;

        // Второй вызов (с кэшем)
        long start2 = System.nanoTime();
        vehicleService.findVehicleByNumber(vehicleNumber);
        long durationWithCache = System.nanoTime() - start2;

        logPerformance("---vehicleByNumber---",
                durationWithoutCache, durationWithCache);

        // Утверждаем, что вызов с кэшем как минимум в 2 раза быстрее (с запасом)
        assertTrue(durationWithCache < durationWithoutCache / 2,
                "Кэш не дал ускорения: " + durationWithoutCache + " vs " + durationWithCache);

    }
}
