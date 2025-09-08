package ru.fisher.VehiclePark.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.RecordVideoSize;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.fisher.VehiclePark.bot.TelegramBot;
import ru.fisher.VehiclePark.config.TelegramBotConfig;
import ru.fisher.VehiclePark.config.TestContainersConfig;
import ru.fisher.VehiclePark.kafka.NotificationKafkaProducer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@EnableAutoConfiguration(exclude = {
//        DataSourceAutoConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class,
//        KafkaAutoConfiguration.class
//})
//@DataJpaTest
@ActiveProfiles("test")
//@TestPropertySource("classpath:application-test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VehicleParkE2ETest extends TestContainersConfig {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

//    @MockBean
//    private NotificationKafkaProducer notificationKafkaProducer;
//
//    @MockBean
//    private TelegramBotConfig telegramBotConfig;
//
//    @MockBean
//    private TelegramBot telegramBot;


    @BeforeAll
    static void setUpAll() {
        playwright = Playwright.create();
        browser = playwright.firefox().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false) // true для CI/CD
        );
        // Используем системный Chrome если установлен
//        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
//                .setChannel("chrome") // Использует системный Chrome
//                .setHeadless(false)
//                .setTimeout(120000)
//                .setArgs(Arrays.asList(
//                        "--no-sandbox",
//                        "--disable-dev-shm-usage",
//                        "--disable-gpu",
//                        "--disable-software-rasterizer",
//                        "--disable-web-security",
//                        "--disable-features=VizDisplayCompositor"
//                ))
//        );
    }

    @AfterAll
    static void tearDownAll() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setJavaScriptEnabled(true));
        // Включаем логирование сетевых запросов
        context.onRequest(request ->
                log.info("→ {} {}", request.method(), request.url()));
        context.onResponse(response ->
                log.info("← {} {}", response.status(), response.url()));
        page = context.newPage();
        // Глобальный таймаут для всех действий
//        page.setDefaultTimeout(60000); // 60 секунд вместо 30
//        page.setDefaultNavigationTimeout(60000);
    }

    @AfterEach
    void tearDown() {
        // Делаем скриншот если тест упал
        if (page != null && !page.isClosed()) {
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("target/screenshots/test-" +
                            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".png")));
        }
        // Сохраняем видео
      //  saveVideoRecording();
        context.close();
    }

//    private void saveVideoRecording() {
//        try {
//            if (context != null) {
//                Path videoPath = page.video().path();
//                if (videoPath != null && Files.exists(videoPath)) {
//                    String timestamp = LocalDateTime.now()
//                            .format(DateTimeFormatter.ofPattern("HH-mm-ss"));
//                    String videoName = "video-" + timestamp + ".mp4";
//                    Path targetPath = Paths.get("target/videos", videoName);
//
//                    Files.createDirectories(targetPath.getParent());
//                    Files.move(videoPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
//                    log.debug("Видео записано: {}", targetPath);
//                }
//            }
//        } catch (Exception e) {
//            log.warn("Ошибка при сохранении видео: {}", e.getMessage());
//        }
//    }

    @Test
    @Order(1)
    @DisplayName("Переход к списку машин")
    void viewVehiclesList() {
        loginAsManager();
        // Ждем пока страница менеджера полностью загрузится
        page.waitForSelector("a[href*='/vehicles']",
                new Page.WaitForSelectorOptions().setTimeout(15000));

        // Кликаем по ссылке с ожиданием
        page.click("a[href*='/vehicles']");

        // Ждем не только URL, но и загрузки контента
        page.waitForURL("**/vehicles**",
                new Page.WaitForURLOptions().setTimeout(30000));

        // Ждем пока таблица с машинами загрузится
        page.waitForSelector("table",
                new Page.WaitForSelectorOptions()
                        .setTimeout(15000)
                        .setState(WaitForSelectorState.VISIBLE));

        // Проверяем что таблица не пустая
        boolean hasVehicles = page.locator("table tbody tr").count() > 0;
        assertTrue(hasVehicles, "Таблица машин не должна быть пустой");

        log.info("Список машин загружен, найдено {} строк",
                page.locator("table tbody tr").count());
    }

    @Test
    @Order(2)
    @DisplayName("Создание нового автомобиля")
    void createVehicle() {
        loginAsManager();

        viewVehiclesList();
        page.waitForURL("**/vehicles**");

        page.click("text=Добавить транспортное средство");
        page.fill("input[name='number']", "А555АА777");
        page.fill("input[name='purchaseTime']", "2025-08-08T22:00");
        page.fill("input[name='yearOfCarProduction']", "2023");
        page.fill("input[name='price']", "1500000");
        page.fill("input[name='mileage']", "253100");
        page.selectOption("select[name='brandId']", "2");
        page.click("form >> text=Сохранить");

        // Переходим на последнюю страницу
        navigateToLastPage();
        page.waitForSelector("text=А555АА777");
    }

    @Test
    @Order(3)
    @DisplayName("Просмотр деталей автомобиля")
    void viewVehicleDetails() {
        loginAsManager();

        viewVehiclesList();
        page.waitForURL("**/vehicles**");

        // Переходим на последнюю страницу
        navigateToLastPage();

        // Открываем детали конкретного автомобиля
        page.click("a[href*='/vehicles/']:has-text('А555АА777')"); // Ищем по номеру

        assertTrue(page.locator("h1").innerText().contains("Детали автомобиля"));
        //assertTrue(page.isVisible("table"));
        log.info("Детали авто загружены");
    }

    @Test
    @Order(4)
    @DisplayName("Загрузка GPX-трека")
    void uploadTrackFile() {
        loginAsManager();

        // Переходим в список машин
        viewVehiclesList();
        page.waitForURL("**/vehicles**");

        // Переходим на последнюю страницу
        navigateToLastPage();

        // Открываем детали конкретного автомобиля
        page.click("a[href*='/vehicles/']:has-text('А555АА777')"); // Ищем по номеру

        // Кликаем по кнопке "Загрузить трек"
        page.click("a:has-text('Загрузить трек')");
        page.waitForURL("**/upload**");

        // Загружаем файл
        page.setInputFiles("input[type='file']", Paths.get("src/test/resources/Sample-test-gpx.gpx"));

        // Заполняем даты
        page.fill("input[name='startTime']", "2025-05-10T15:25");
        page.fill("input[name='endTime']", "2025-05-10T15:43");

        // Проверяем что поля заполнились
        assertFalse(page.inputValue("input[name='startTime']").isEmpty());
        assertFalse(page.inputValue("input[name='endTime']").isEmpty());

        // Отправляем форму
        page.click("button[type='submit']");

        // Ждем успешного сообщения
        page.waitForSelector("text=Поездка успешно добавлена!",
                new Page.WaitForSelectorOptions().setTimeout(10000));

        assertTrue(page.isVisible("text=Поездка успешно добавлена!"));
        log.info("Поездка успешно добавлена!");
    }

    @Test
    @Order(5)
    @DisplayName("Фильтрация поездок и просмотр карты треков")
    void filterTripsAndViewMap() {
        loginAsManager();

        // Переходим в список машин
        viewVehiclesList();
        page.waitForURL("**/vehicles**");

        // Переходим на последнюю страницу
        navigateToLastPage();

        // Открываем детали конкретного автомобиля
        page.click("a[href*='/vehicles/']:has-text('А555АА777')"); // Ищем по номеру

        // Кликаем по кнопке "Просмотреть треки"
        page.locator("a.btn.btn-success:has-text('Просмотреть треки')").click();

        // Заполняем даты
        page.fill("#startTime", "2021-08-01T00:00");
        page.fill("#endTime", "2025-08-21T23:59");

        // Заполняем фильтр
        page.fill("#startTime", "2021-08-01T00:00");
        page.fill("#endTime", "2025-08-21T23:59");
        page.click("button:has-text('Показать треки')");

        // Нажимаем "Показать треки"
        page.locator("button:has-text('Показать треки')").click();

        // Ждём таблицу с поездками
        page.waitForSelector("table.table-bordered tbody tr",
                new Page.WaitForSelectorOptions().setTimeout(10000));

        // Проверяем, что есть хотя бы одна поездка
        int tripCount = page.locator("table.table-bordered tbody tr").count();
        assertTrue(tripCount > 0, "Ожидались поездки в таблице");
        log.info("Поездок в таблице: {}", tripCount);

        // Проверяем карту
        page.waitForSelector("#map");
        assertTrue(page.isVisible("#map"));
    }

    @Test
    @Order(6)
    @DisplayName("Генерация отчета по пробегу")
    void generateMileageReport() {
        loginAsManager();

        page.navigate("http://localhost:8888/managers/reports");
        page.waitForLoadState(LoadState.NETWORKIDLE); // ждем рендер страницы

        // Выбираем тип отчета
        page.waitForSelector("#reportType");
        page.selectOption("#reportType", "VEHICLE_MILEAGE");
        page.fill("#vehicleNumber", "А555АА777");

        page.waitForSelector("#period");
        page.selectOption("#period", "MONTH");

        // Заполняем даты
        page.fill("#startDate", "2024-01-01T00:00");
        page.fill("#endDate", "2025-12-31T00:00");

        // Жмём кнопку
        page.click("#submitButton");

        // Проверяем результат
        page.waitForSelector("text=К форме создания отчета");
        assertTrue(page.isVisible("table"));
        assertTrue(page.content().contains("Пробег автомобиля за период"));
    }

    @Test
    @Order(7)
    @DisplayName("Удаление автомобиля")
    void deleteVehicle() {
        loginAsManager();

        viewVehiclesList();
        page.waitForURL("**/vehicles**");
        navigateToLastPage();

        // Ждем загрузки таблицы
        page.waitForSelector("table tbody tr",
                new Page.WaitForSelectorOptions().setTimeout(30000));

        // Ищем автомобиль по номеру
        page.waitForSelector("text=А555АА777",
                new Page.WaitForSelectorOptions().setTimeout(10000));

        // Нажимаем кнопку удаления
        page.onceDialog(Dialog::accept);
        page.locator("tr:has-text('А555АА777')")
                .locator("button.btn-danger")
                .click();

        // Ждем завершения навигации после удаления
        page.waitForURL("**/vehicles**", new Page.WaitForURLOptions()
                .setTimeout(15000));

        // Проверяем что автомобиль удалён
        assertFalse(page.content().contains("А555АА777"), "Автомобиль удален");
    }

    private void loginAsManager() {
        page.navigate("http://localhost:8888/auth/login");
        page.fill("#username", "Ivan");
        page.fill("#password", "12345");
        page.click("button[type='submit']");
        page.waitForURL("**/managers/enterprises**");
    }

    private void navigateToLastPage() {
        page.click(".pagination li:last-child a");
    }

}
