package ru.fisher.VehiclePark.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.fisher.VehiclePark.bot.TelegramBot;
import ru.fisher.VehiclePark.config.TelegramBotConfig;
import ru.fisher.VehiclePark.kafka.NotificationKafkaProducer;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VehicleServiceIntegrationTest {

    @Autowired
    private VehicleService vehicleService;

    @MockBean
    private NotificationKafkaProducer notificationKafkaProducer;

    @MockBean
    private TelegramBotConfig telegramBotConfig;

    @MockBean
    private TelegramBot telegramBot;

    private void logPerformance(String methodName, long withoutCache, long withCache) {
        System.out.println("\n--- " + methodName + " ---");
        System.out.printf("Без кэша: %.3f ms%n", withoutCache / 1_000_000.0);
        System.out.printf("С кэшем: %.3f ms%n", withCache / 1_000_000.0);
        System.out.printf("Ускорение: %.1f раз%n", (double) withoutCache / withCache);
    }

    @Test
    void findOneVehicle_shouldAccelerateWithCache() {
        // Первый вызов (без кэша)
        long start1 = System.nanoTime();
        vehicleService.findOne(1L);
        long durationWithoutCache = System.nanoTime() - start1;

        // Второй вызов (с кэшем)
        long start2 = System.nanoTime();
        vehicleService.findOne(1L);
        long durationWithCache = System.nanoTime() - start2;

        logPerformance("---findOneVehicle---",
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
