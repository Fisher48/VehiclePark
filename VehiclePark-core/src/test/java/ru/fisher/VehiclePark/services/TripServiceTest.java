package ru.fisher.VehiclePark.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.models.Trip;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.repositories.jpa.TripRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock private TripRepository tripRepository;
    @Mock private GpxParserService gpxParserService;
    @Mock private GpsDataService gpsDataService;
    @Mock private VehicleService vehicleService;

    @InjectMocks
    private TripService tripService;

    @Test
    void uploadTripFromGpx_shouldCreateTripSuccessfully() throws Exception {
        // 🔹 Данные для теста
        Long vehicleId = 12L;
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        LocalDateTime start = LocalDateTime.of(2025, 4, 6, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 4, 6, 11, 0);

        MultipartFile gpxFile = new MockMultipartFile("file",
                "track.gpx", "text/xml", "<gpx></gpx>".getBytes());

        GpsData gps1 = new GpsData();
        gps1.setTimestamp(start);
        gps1.setCoordinates(new GeometryFactory().createPoint(new Coordinate(37.6173, 55.7558)));

        GpsData gps2 = new GpsData();
        gps2.setTimestamp(end);
        gps2.setCoordinates(new GeometryFactory().createPoint(new Coordinate(37.6180, 55.7560)));

        List<GpsData> gpsDataList = List.of(gps1, gps2);

        // 🔹 Настройка моков
        when(tripRepository.findByVehicleIdAndOverlapTimeRange(anyLong(), any(), any())).thenReturn(List.of());
        when(gpxParserService.parseGpxFile(vehicle, gpxFile, start, end)).thenReturn(gpsDataList);

        // 🔹 Вызов тестируемого метода
        Trip savedTrip = tripService.uploadTripFromGpx(vehicle, start, end, gpxFile);

        // 🔹 Проверки
        assertNotNull(savedTrip);
        assertEquals(start, savedTrip.getStartTime());
        assertEquals(end, savedTrip.getEndTime());
        assertEquals(vehicle, savedTrip.getVehicle());
        assertEquals(gps1, savedTrip.getStartGpsData());
        assertEquals(gps2, savedTrip.getEndGpsData());

        BigDecimal mileage = savedTrip.getMileage();
        assertNotNull(mileage);
        assertTrue(mileage.doubleValue() > 0.0);

    }

    @Test
    void uploadTripFromGpx_shouldThrowWhenOverlapping() {
        Long vehicleId = 12L;
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        LocalDateTime start = LocalDateTime.of(2025, 4, 6, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 4, 6, 11, 0);
        MultipartFile gpxFile = new MockMultipartFile
                ("file", "track.gpx", "text/xml", "<gpx></gpx>".getBytes());

        Trip existingTrip = new Trip();
        when(tripRepository.findByVehicleIdAndOverlapTimeRange(vehicleId, start, end)).thenReturn(List.of(existingTrip));

        assertThrows(IllegalArgumentException.class, () ->
                tripService.uploadTripFromGpx(vehicle, start, end, gpxFile)
        );
    }
}
