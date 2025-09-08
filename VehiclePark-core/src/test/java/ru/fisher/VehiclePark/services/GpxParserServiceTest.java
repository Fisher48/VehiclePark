package ru.fisher.VehiclePark.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.models.Trip;
import ru.fisher.VehiclePark.models.Vehicle;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GpxParserServiceTest {

    @Mock
    private VehicleService vehicleService;

    @Mock
    private GpxParserService gpxParserService;

    @Test
    void testParseGpxFile_validFile_returnsGpsDataList() throws Exception {
        Long vehicleId = 1L;
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        // Подготовим GPX-строку с одной точкой
        String gpx = """
            <gpx>
              <trk><trkseg>
                <trkpt lat="55.7558" lon="37.6173">
                  <time>2025-04-06T15:00:00Z</time>
                <trkpt lat="55.7558" lon="37.373">
                  <time>2025-04-06T15:00:10Z</time>
                </trkpt>
              </trkseg></trk>
            </gpx>
            """;

        MultipartFile file = new MockMultipartFile
                ("file", "track.gpx", "text/xml", gpx.getBytes());

        LocalDateTime start = LocalDateTime.of(2025, 4, 6, 14, 0);
        LocalDateTime end = LocalDateTime.of(2025, 4, 6, 16, 0);

        Mockito.when(vehicleService.findOne(vehicleId)).thenReturn(vehicle);

        List<GpsData> result = gpxParserService.parseGpxFile(vehicle, file, start, end);

        assertEquals(1, result.size());

        GpsData data = result.getFirst();
        assertEquals(55.7558, data.getCoordinates().getY(), 0.0001);
        assertEquals(37.6173, data.getCoordinates().getX(), 0.0001);
        assertEquals(vehicle, data.getVehicle());
    }

    @Test
    void testParseGpxFile_outOfRange_throwsException() {
        String gpx = """
            <gpx>
              <trk><trkseg>
                <trkpt lat="55.7558" lon="37.6173">
                  <time>2025-04-10T15:00:00Z</time>
                </trkpt>
              </trkseg></trk>
            </gpx>
            """;

        MultipartFile file = new MockMultipartFile
                ("file", "track.gpx", "text/xml", gpx.getBytes());

        LocalDateTime start = LocalDateTime.of(2025, 4, 6, 14, 0);
        LocalDateTime end = LocalDateTime.of(2025, 4, 6, 16, 0);

        assertThrows(RuntimeException.class, () ->
                gpxParserService.parseGpxFile(new Vehicle(), file, start, end)
        );
    }
}
