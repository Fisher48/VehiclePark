package ru.fisher.VehiclePark.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fisher.VehiclePark.dto.TripMapDTO;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TripServiceUnitTest {

    @Mock
    private TripService tripService;

    // Общие тестовые данные
    private final Long enterpriseId = 1L;
    private final Long vehicleId = 1L;
    private final LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
    private final LocalDateTime end = LocalDateTime.of(2025, 1, 2, 0, 0);
    private final String clientTz = "Europe/Moscow";


    @Test
    void getTripMapData_shouldAccelerateWithCache() {
        // Настройка мока с тестовыми данными
        List<TripMapDTO> mockData = Collections.singletonList(
                new TripMapDTO()
        );
        when(tripService.getTripMapData(enterpriseId, vehicleId, start, end, clientTz))
                .thenReturn(mockData);

        // Первый вызов (без кэша)
        long startTime = System.nanoTime();
        tripService.getTripMapData(enterpriseId, vehicleId, start, end, clientTz);
        long durationWithoutCache = System.nanoTime() - startTime;

        // Второй вызов (с кэшем)
        startTime = System.nanoTime();
        tripService.getTripMapData(enterpriseId, vehicleId, start, end, clientTz);
        long durationWithCache = System.nanoTime() - startTime;

        logPerformance("getTripMapData", durationWithoutCache, durationWithCache);

        assertTrue(durationWithCache < durationWithoutCache / 2,
                "Кэш не дал ускорения: " + durationWithoutCache + " vs " + durationWithCache);
    }

    private void logPerformance(String methodName, long withoutCache, long withCache) {
        System.out.println("\n--- " + methodName + " ---");
        System.out.printf("Без кэша: %.4f ms%n", withoutCache / 1_000_000.0);
        System.out.printf("С кэшем: %.4f ms%n", withCache / 1_000_000.0);
        System.out.printf("Ускорение: %.1f раз%n", (double) withoutCache / withCache);
    }
}
