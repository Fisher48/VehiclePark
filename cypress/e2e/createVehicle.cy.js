describe("Create, Edit and Delete Vehicle", () => {

    beforeEach(() => {
      cy.visit("http://localhost:8888/auth/login");
      cy.get("#username").type("Ivan");
      cy.get("#password").type("12345");
      cy.get("button[type='submit']").click();
      cy.url().should("include", "/managers/enterprises");
    });

    it("Создание нового автомобиля", () => {
        cy.navigateToVehicles();
        cy.contains('Добавить транспортное средство').click();

        cy.get('input[name="number"]').type("А555АА777");
        cy.get('input[name="purchaseTime"]').type("2025-08-08T22:00:00");
        cy.get('input[name="yearOfCarProduction"]').clear().type("2023");
        cy.get('input[name="price"]').clear().type("1500000");
         cy.get('input[name="mileage"]').clear().type("253100");
        cy.get('select[name="brandId"]').select("2");

        cy.get('form').submit();

        // Переходим на последнюю страницу
        cy.get('.pagination li:last-child a').click();
        cy.contains("А555АА777");
        cy.url().should("include", "/vehicles");
    });


    it("Изменение в новом автомобиле", () => {
        cy.navigateToVehicles();

        // Переходим на последнюю страницу где наш автомобиль
        cy.get('.pagination li:last-child a').click();

        // Находим нашу машину и кликаем "Изменить"
        cy.contains('tr', 'А555АА777').within(() => {
            cy.contains('Изменить').click();
        });

        // Меняем данные
        cy.get('input[name="yearOfCarProduction"]').clear().type("2022");
        cy.get('input[name="price"]').clear().type("1232141");
        cy.get('input[name="mileage"]').clear().type("255000");
        cy.get('select[name="brandId"]').select("1");

        // Сохраняем изменения
        cy.get('form').submit();

        // Проверяем что вернулись к списку
        cy.url().should("include", "/vehicles");

        // Переходим на последнюю страницу и проверяем изменения
        cy.get('.pagination li:last-child a').click();

        cy.contains('tr', 'А555АА777').within(() => {
            cy.contains('А555АА777').click()
            cy.contains('2022'); // Проверяем год
            cy.contains('1232141'); // Проверяем цену
            cy.contains('255000');
            cy.contains('Kia');
        });
    });

    it('Успешная генерация отчета по пробегу автомобиля', () => {

        // Переходим на страницу отчетов
        cy.visit('http://localhost:8888/managers/reports');

        // Проверяем что форма загрузилась
        cy.get('h1').should('contain', 'Форма создания отчета');
        cy.get('#reportType').should('be.visible');

        // Выбираем тип отчета - Пробег автомобиля
        cy.get('#reportType').select('VEHICLE_MILEAGE');

        // Проверяем что появилось поле для номера автомобиля
        cy.get('#vehicleNumber').should('be.visible');

        // Заполняем номер автомобиля
        cy.get('#vehicleNumber').type('А555АА777');

        // Выбираем период
        cy.get('#period').select('MONTH');

        // Заполняем даты
        cy.get('#startDate').type('2024-01-01T00:00');
        cy.get('#endDate').type('2024-12-31T23:59');

        // Нажимаем кнопку генерации
        cy.get('#submitButton').click();

        // Должна появиться кнопка загрузки
        cy.get('#loadingButton').should('be.visible');
        cy.get('#submitButton').should('not.be.visible');

        // Ждем завершения генерации (появление результатов или скрытие лоадера)
        cy.get('#loadingButton', { timeout: 30000 }).should('not.be.visible');

        // Проверяем что отчет сгенерирован
        cy.contains('Отчет по пробегу').should('be.visible');
        cy.get('table').should('be.visible');
        cy.contains('Общий пробег').should('be.visible');
      });


    it("Удаление автомобиля", () => {
        // Переходим к списку автомобилей
        cy.navigateToVehicles();
        cy.url().should("include", "/vehicles");

        // Переходим на последнюю страницу где наш автомобиль
        cy.get('.pagination li:last-child a').click();

        // Удаляем
        cy.contains('tr', 'А555АА777').within(() => {
          cy.get('button.btn-danger').click();
        });

        // Подтверждаем удаление
        cy.on('window:confirm', () => true);

        // Проверяем что автомобиль исчез
        cy.contains("А555АА777").should('not.exist');
      });

});
