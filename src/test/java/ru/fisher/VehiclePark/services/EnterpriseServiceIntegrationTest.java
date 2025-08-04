//package ru.fisher.VehiclePark.services;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.cache.CacheManager;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class EnterpriseServiceIntegrationTest {
//
//    @Autowired
//    private EnterpriseService enterpriseService;
//
//    @Autowired
//    private CacheManager cacheManager;
//
//    private final Long testManagerId = 1L;
//
//    @Test
//    void shouldAccelerateWithCache() {
//        // 1-й вызов
//        long start1 = System.nanoTime();
//        enterpriseService.findAllForManager(testManagerId);
//        long durationWithoutCache = System.nanoTime() - start1;
//
//        // 2-й вызов
//        long start2 = System.nanoTime();
//        enterpriseService.findAllForManager(testManagerId);
//        long durationWithCache = System.nanoTime() - start2;
//
//        System.out.println("---findAllEnterprisesForManager---");
//        System.out.println("Скорость без кеша: " + durationWithoutCache / 1_000_000.0 + " ms");
//        System.out.println("Скорость с кешем: " + durationWithCache / 1_000_000.0 + " ms");
//        System.out.println("Во сколько раз быстрее: " + durationWithoutCache / durationWithCache);
//
//        assertTrue(durationWithCache < durationWithoutCache / 2,
//                "Кэш не дал ускорения: " + durationWithoutCache + " vs " + durationWithCache);
//    }
//}
