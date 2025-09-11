package ru.fisher.VehiclePark.e2e;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.fisher.VehiclePark.kafka.NotificationKafkaProducer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VehicleCrudE2ETest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @MockBean
    private NotificationKafkaProducer notificationKafkaProducer;

    @BeforeAll
    static void setupAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false) // true для CI
        );
    }

    @AfterAll
    static void teardownAll() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void setup() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void cleanup() {
        context.close();
    }

    private void login() {
        page.navigate("http://localhost:8888/auth/login");
        page.fill("#username", "Ivan");
        page.fill("#password", "12345");
        page.click("button[type='submit']");
        assertTrue(page.url().contains("/managers/enterprises"));
    }

    private void goToVehicles() {
        page.click("a[href='/managers/enterprises/1/vehicles']");
        assertTrue(page.url().contains("/vehicles"));
    }

    @Test
    @Order(1)
    @DisplayName("Создание нового автомобиля")
    void createVehicle() {
        login();
        goToVehicles();

        page.click("text=Добавить транспортное средство");
        page.fill("input[name='number']", "А555АА777");
        page.fill("input[name='purchaseTime']", "2025-08-08T22:00");
        page.fill("input[name='yearOfCarProduction']", "2023");
        page.fill("input[name='price']", "1500000");
        page.fill("input[name='mileage']", "253100");
        page.selectOption("select[name='brandId']", "2");
        page.click("form >> text=Сохранить");

        // Переходим на последнюю страницу
        page.click(".pagination li:last-child a");
        page.waitForSelector("text=А555АА777");
    }

    @Test
    @Order(2)
    @DisplayName("Редактирование автомобиля")
    void editVehicle() {
        login();
        goToVehicles();

        // Переходим на последнюю страницу
        page.click(".pagination li:last-child a");

        // Кликаем "Изменить" для А555АА777
        page.locator("tr:has-text('А555АА777')").locator("text=Изменить").click();

        // Изменяем данные
        page.fill("input[name='yearOfCarProduction']", "2022");
        page.fill("input[name='price']", "1232141");
        page.fill("input[name='mileage']", "255000");
        page.selectOption("select[name='brandId']", "1");

        // Сохраняем
        page.click("form >> text=Сохранить");

        // Проверка
        page.click(".pagination li:last-child a");
        page.locator("tr:has-text('А555АА777')").locator("a:has-text('А555АА777')").click();

        // Проверяем содержимое страницы
        assertTrue(page.locator("body").innerText().contains("2022"));
        assertTrue(page.locator("body").innerText().contains("1232141"));
        assertTrue(page.locator("body").innerText().contains("255000"));
    }

    @Test
    @Order(3)
    @DisplayName("Удаление автомобиля")
    void deleteVehicle() {
        login();
        goToVehicles();
        page.click(".pagination li:last-child a");

        // Нажимаем кнопку удаления
        page.onceDialog(Dialog::accept);
        page.locator("tr:has-text('А555АА777')").locator("button.btn-danger").click();

        // Проверяем что автомобиль удалён
        assertFalse(page.content().contains("А555АА777"));
    }
}
