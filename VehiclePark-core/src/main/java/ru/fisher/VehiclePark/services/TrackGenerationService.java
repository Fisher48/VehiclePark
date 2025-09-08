package ru.fisher.VehiclePark.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.repositories.jpa.GpsDataRepository;
import ru.fisher.VehiclePark.repositories.jpa.VehicleRepository;
import ru.fisher.VehiclePark.util.OpenRouteServiceClient;

import java.time.LocalDateTime;
import java.util.*;

@Service
@ShellComponent
@Slf4j
public class TrackGenerationService {

    private final GpsDataRepository gpsDataRepository;
    private final VehicleRepository vehicleRepository;
    private final OpenRouteServiceClient routeServiceClient;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Для работы с JSON
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final Random random = new Random();

    @Autowired
    public TrackGenerationService(GpsDataRepository gpsDataRepository, VehicleRepository vehicleRepository,
                                  OpenRouteServiceClient routeServiceClient) {
        this.gpsDataRepository = gpsDataRepository;
        this.vehicleRepository = vehicleRepository;
        this.routeServiceClient = routeServiceClient;
    }

    @ShellMethod(value = "Generate a track for a vehicle", key = "generate-track")
    public void genTrack(@ShellOption(defaultValue = "spring") Long vehicleId, double centerLat,
            double centerLon, double radius, double trackLengthKm) {
        generateTrack(vehicleId, centerLat, centerLon, radius, trackLengthKm);
        System.out.println("Track generation started for vehicle " + vehicleId);
    }

    public void generateTrack(Long vehicleId, double centerLat, double centerLon, double radius, double trackLengthKm) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle with id " + vehicleId + " does not exist."));

        Timer timer = new Timer();
        double[] lastPoint = {centerLat, centerLon};
        double[] distanceRemaining = {trackLengthKm * 1000}; // Длина трека в метрах

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (distanceRemaining[0] <= 0) {
                    timer.cancel();
                    return;
                }
                // Генерация случайной конечной точки в пределах радиуса
                double randomLat = centerLat + (random.nextDouble() - 0.5) * radius / 111.0;
                double randomLon = centerLon + (random.nextDouble() - 0.5) * radius / 111.0;

                // Запрос маршрута через OpenRouteService
                Mono<String> routeMono = routeServiceClient.getRoute(lastPoint, new double[]{randomLat, randomLon});
                String routeJson = routeMono.block(); // Синхронное получение ответа
                List<Point> routePoints = parseRoute(routeJson);

                // Сохранение точек маршрута в базу данных
                for (Point point : routePoints) {
                    GpsData gpsData = new GpsData();
                    gpsData.setVehicle(vehicle);
                    gpsData.setTimestamp(LocalDateTime.now());
                    gpsData.setCoordinates(point);
                    gpsDataRepository.save(gpsData);
                    log.info("Saved point: {}", point);
                }

                // Обновление текущей точки и оставшейся длины
                Point lastRoutePoint = routePoints.getLast();
                lastPoint[0] = lastRoutePoint.getX();
                lastPoint[1] = lastRoutePoint.getY();
                distanceRemaining[0] -= 100; // Уменьшаем оставшуюся длину маршрута
            }
        }, 0, 10000); // Интервал в 10 секунд
    }

    private List<Point> parseRoute(String routeJson) {
        // Разбираем JSON-ответ OpenRouteService и извлекаем точки маршрута
        // (реализация зависит от структуры ответа OpenRouteService)
        List<Point> routePoints = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper.readTree(routeJson);
            JsonNode features = rootNode.path("features");

            if (features.isArray() && !features.isEmpty()) {
                JsonNode coordinates = features.get(0).path("geometry").path("coordinates");

                for (JsonNode coordinateNode : coordinates) {
                    double longitude = coordinateNode.get(0).asDouble();
                    double latitude = coordinateNode.get(1).asDouble();

                    // Преобразуем координаты в объект Point
                    Coordinate coordinate = new Coordinate(longitude, latitude);
                    Point point = geometryFactory.createPoint(coordinate);
                    routePoints.add(point);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse route JSON", e);
        }

        return routePoints;
    }

}
