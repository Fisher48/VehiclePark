package ru.fisher.VehiclePark.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {
    @Mock
    private VehicleService vehicleService;
    @Mock
    private BrandService brandService;
    private final Long testManagerId = 1L;

    @BeforeEach
    void clearCache() {
        when(vehicleService.findAllForManager(testManagerId))
                .thenReturn(Collections.emptyList()); // Возвращаемое значение
    }

    @Test
    void shouldAccelerateWithCache() {
        // Первый вызов (без кэша)
        long start1 = System.nanoTime();
        vehicleService.findAllForManager(testManagerId);
        long durationWithoutCache = System.nanoTime() - start1;

        // Второй вызов (с кэшем)
        long start2 = System.nanoTime();
        vehicleService.findAllForManager(testManagerId);
        long durationWithCache = System.nanoTime() - start2;

        System.out.println("Скорость без кеша: " + durationWithoutCache / 1_000_000.0 + " ms");
        System.out.println("Скорость с кешем: " + durationWithCache / 1_000_000.0 + " ms");
        System.out.println("Во сколько раз быстрее: " + durationWithoutCache / durationWithCache);

        // Утверждаем, что вызов с кэшем как минимум в 2 раза быстрее (с запасом)
        assertTrue(durationWithCache < durationWithoutCache / 2,
                "Кэш не дал ускорения: " + durationWithoutCache + " vs " + durationWithCache);
    }

}