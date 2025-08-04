//package ru.fisher.VehiclePark.services;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.test.context.ActiveProfiles;
//import ru.fisher.VehiclePark.dto.TripMapDTO;
//import ru.fisher.VehiclePark.models.Trip;
//import ru.fisher.VehiclePark.models.Vehicle;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class TripServiceIntegrationTest {
//
//    private final TripService tripService;
//    private final GpsDataService gpsDataService;
//    private final CacheManager cacheManager;
//    private final VehicleService vehicleService;
//
//    @Autowired
//    public TripServiceIntegrationTest(TripService tripService, GpsDataService gpsDataService, CacheManager cacheManager, VehicleService vehicleService) {
//        this.tripService = tripService;
//        this.gpsDataService = gpsDataService;
//        this.cacheManager = cacheManager;
//        this.vehicleService = vehicleService;
//    }
//
//    private void logPerformance(String methodName, long withoutCache, long withCache) {
//        System.out.println("\n--- " + methodName + " ---");
//        System.out.printf("Без кэша: %.3f ms%n", withoutCache / 1_000_000.0);
//        System.out.printf("С кэшем: %.3f ms%n", withCache / 1_000_000.0);
//        System.out.printf("Ускорение: %.1f раз%n", (double) withoutCache / withCache);
//    }
//
//    @Test
//    void findTripsForVehicleInTimeRange_shouldCacheResults() {
//        // Подготовка данных
//        Vehicle vehicle = vehicleService.findOne(1L);
//        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
//        LocalDateTime end = LocalDateTime.now();
//
//        // Первый вызов - должен загрузить из БД
//        List<Trip> firstCall = tripService.findTripsForVehicleInTimeRange(vehicle.getId(), start, end);
//
//        // Второй вызов - должен взять из кэша
//        List<Trip> secondCall = tripService.findTripsForVehicleInTimeRange(vehicle.getId(), start, end);
//
//        // Проверки
//        assertEquals(firstCall, secondCall, "Результаты должны быть равны по содержимому");
//    }
//
//    @Test
//    void testTripMapDataCaching() {
//        Long enterpriseId = 1L;
//        Long vehicleId = 1L;
//        LocalDateTime from = LocalDateTime.of(2020, 1, 1, 0, 0);
//        LocalDateTime to = LocalDateTime.of(2025, 6, 2, 0, 0);
//        String clientTz = "Europe/Moscow";
//
//        // Первый вызов — кэшируется
//        List<TripMapDTO> first = tripService.getTripMapData(enterpriseId, vehicleId, from, to, clientTz);
//
//        // Кэш должен быть заполнен
//        Cache cache = cacheManager.getCache("tripMaps");
//        Object key = Arrays.asList(enterpriseId, vehicleId, from.toString(), to.toString());
//        Assertions.assertNotNull(cache.get(key));
//
//        // Повторный вызов должен вернуть то же
//        List<TripMapDTO> second = tripService.getTripMapData(enterpriseId, vehicleId, from, to, clientTz);
//        Assertions.assertEquals(first, second);
//    }
//
//    @Test
//    void findTripsForVehicleInTimeRange_shouldAccelerateWithCache() {
//        Long vehicleId = 1L;
//        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
//        LocalDateTime end = LocalDateTime.of(2025, 6, 2, 0, 0);
//
//        // Первый вызов (без кэша)
//        long start1 = System.nanoTime();
//        tripService.findTripsForVehicleInTimeRange(vehicleId, start, end);
//        long durationWithoutCache = System.nanoTime() - start1;
//
//        // Второй вызов (с кэшем)
//        long start2 = System.nanoTime();
//        tripService.findTripsForVehicleInTimeRange(vehicleId, start, end);
//        long durationWithCache = System.nanoTime() - start2;
//
//        logPerformance("findTripsForVehicle", durationWithoutCache, durationWithCache);
//
//        // Утверждаем, что вызов с кэшем как минимум в 2 раза быстрее (с запасом)
//        assertTrue(durationWithCache < durationWithoutCache / 2,
//                "Кэш не дал ускорения: " + durationWithoutCache + " vs " + durationWithCache);
//    }
//
//    @Test
//    void getTripsOnMap_shouldAccelerateWithCache() {
//        Long enterpriseId = 1L;
//        Long vehicleId = 1L;
//        LocalDateTime from = LocalDateTime.of(2020, 1, 1, 0, 0);
//        LocalDateTime to = LocalDateTime.of(2025, 6, 2, 0, 0);
//        String clientTz = "Europe/Moscow";
//
//        // Первый вызов (без кэша)
//        long start1 = System.nanoTime();
//        tripService.getTripMapData(enterpriseId, vehicleId, from, to, clientTz);
//        long durationWithoutCache = System.nanoTime() - start1;
//
//        // Второй вызов (с кэшем)
//        long start2 = System.nanoTime();
//        tripService.getTripMapData(enterpriseId, vehicleId, from, to, clientTz);
//        long durationWithCache = System.nanoTime() - start2;
//
//        logPerformance("getTripsOnMap", durationWithoutCache, durationWithCache);
//
//        // Утверждаем, что вызов с кэшем как минимум в 2 раза быстрее (с запасом)
//        assertTrue(durationWithCache < durationWithoutCache / 2,
//                "Кэш не дал ускорения: " + durationWithoutCache + " vs " + durationWithCache);
//    }
//
//}
