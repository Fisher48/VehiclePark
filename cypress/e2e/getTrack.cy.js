describe("Get routing", () => {

  beforeEach(() => {
      cy.visit("http://localhost:8888/auth/login");
      cy.get("#username").type("Ivan");
      cy.get("#password").type("12345");
      cy.get("button[type='submit']").click();
      cy.url().should("include", "/managers/enterprises");
    });

    it("Просмотр списка машин", () => {
      cy.get('a[href="/managers/enterprises/1/vehicles"]').click();
      cy.url().should("include", "/vehicles");
      cy.get("table").should("exist");
      cy.get("table tbody tr").should("have.length.greaterThan", 0);
    });

    it("Просмотр деталей автомобиля", () => {
      cy.get('a[href="/managers/enterprises/1/vehicles"]').click();
      cy.get('a[href="/managers/enterprises/1/vehicles/1"]').click();
      cy.url().should("include", "/vehicles/1");
      cy.contains("Детали автомобиля");
      cy.get("table").should("exist");
    });

    it("Фильтрация поездок и карта треков", () => {
      cy.get('a[href="/managers/enterprises/1/vehicles"]').click();
      cy.get('a[href="/managers/enterprises/1/vehicles/1"]').click();

      // Находим кнопку по тексту
      cy.contains('a.btn.btn-success', 'Просмотреть треки').click();

      cy.get("#startTime").type("2021-08-01T00:00");
      cy.get("#endTime").type("2025-08-21T23:59");
      cy.get('button[type="submit"]').contains('Показать треки').click();

      cy.get('#map').should('exist');
      cy.get('#map').should('be.visible');
      cy.get('.leaflet-map-pane').should('exist');
    });

     it("Загрузка трека", () => {
            cy.navigateToVehicles();
            cy.get('a[href="/managers/enterprises/1/vehicles/1"]').first().click();

            // Более надежный селектор для кнопки загрузки
            cy.contains('a', 'Загрузить трек').click();

            cy.fixture('Sample-test-gpx.gpx', 'binary')
                .then(Cypress.Blob.binaryStringToBlob)
                .then(fileContent => {
                    cy.get('input[type="file"]').attachFile({
                        fileContent,
                        fileName: 'Sample-test-gpx.gpx',
                        mimeType: 'application/gpx+xml'
                    });
                });

            // Заполняем обязательные поля
            cy.get('input[name="startTime"]').type("2025-05-10T15:25");
            cy.get('input[name="endTime"]').type("2025-05-10T15:43");

            cy.contains('button', 'Загрузить').click();
            cy.contains("Поездка успешно добавлена", { timeout: 10000 });
        });

    it("Редактирование данных автомобиля", () => {
      cy.get('a[href="/managers/enterprises/1/vehicles"]').click();
      cy.get('a[href="/managers/enterprises/1/vehicles/1/edit"]').click();

      cy.get('input[name="number"]').clear().type("А111АА777");
      cy.get('input[name="mileage"]').clear().type("145000");
      // Сохраняем изменения
      cy.get('input[type="submit"]').click();
      cy.contains("А111АА777");
    });


});