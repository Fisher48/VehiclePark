package ru.fisher.VehiclePark.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.web.multipart.MultipartFile;
import ru.fisher.VehiclePark.dto.ImportDTO;
import ru.fisher.VehiclePark.dto.TripImportData;
import ru.fisher.VehiclePark.dto.VehicleDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class ImportServiceTest {

    @Mock
    private MultipartFile file;

    @InjectMocks
    private ImportService importService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testParseCsvFile() throws Exception {
        // Подготовка тестовых данных
        String csvContent = "Enterprise ID,10,Компания CSV,Воронеж,Europe/Moscow\n" +
                "Vehicle ID,Brand ID,Number,Price,Year,Mileage\n" +
                "1001,1,А123ВС771,1500000,2022,15000\n" +
                "1002,2,Х456ХХ747,2000000,2023,5000\n" +
                "1003,3,Е789ЕЕ774,2500000,2021,30000\n" +
                "Trip ID,VehicleNumber,Start Time,End Time,Start Latitude,Start Longitude,End Latitude,End Longitude, Mileage\n" +
                "2001,А123ВС771,2024/12/31 08:00,2024/12/31 10:00,52.610762,39.573269,52.516900,39.796858,50\n" +
                "2002,Х456ХХ747,2025/01/01 09:00,2025/01/02 11:00,52.516900,39.796858,52.610762,39.573269,60\n" +
                "2003,Е789ЕЕ774,2025/01/02 10:00,2025/01/02 12:00,52.595338,39.504177,52.617823,39.608342,40";

        when(file.getInputStream()).thenReturn(new java.io.ByteArrayInputStream(csvContent.getBytes()));

        // Вызов метода
        ImportDTO result = importService.parseCsvFile(file);

        // Проверка результатов
        assertEquals("Компания CSV", result.getEnterprise().getName());
        assertEquals(3, result.getVehicles().size());
        assertEquals(3, result.getTrips().size());

        VehicleDTO vehicle = result.getVehicles().getFirst();
        assertEquals("А123ВС771", vehicle.getNumber());

        TripImportData trip = result.getTrips().getFirst();
        assertEquals("2024/12/31 08:00", trip.getStartTime());
    }
}