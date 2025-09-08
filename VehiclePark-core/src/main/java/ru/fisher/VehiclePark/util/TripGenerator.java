package ru.fisher.VehiclePark.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.models.Trip;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.services.GpsDataService;
import ru.fisher.VehiclePark.services.TripService;
import ru.fisher.VehiclePark.services.VehicleService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@ShellComponent
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TripGenerator {

    private final GpsDataService gpsDataService;
    private final VehicleService vehicleService;
    private final TripService tripService;
    private final WebClient webClient;

    @Value("${openrouteservice.api.key}")
    public String key;

    @Value("${openrouteservice.url}")
    public String openRouteUrl;

    private static final double EARTH_RADIUS = 6371;

    @ShellMethod(key = "generate-trip")
    public void generateTripForVehicle(@ShellOption long vehicleId,
                                       double latitude, double longitude,
                                       double trackLengthKm) {
        log.info("Генерация поездки для машины с id {} длиной {} км", vehicleId, trackLengthKm);

        // Генерация конечной точки маршрута
        double[] endCoordinates = generateCoordinatesByDistance(latitude, longitude, trackLengthKm);

        // Получение маршрута через OpenRouteService
        String route = callOpenRouteService(latitude, longitude, endCoordinates[0], endCoordinates[1]);

        // Создание и сохранение поездки
        Vehicle vehicle = vehicleService.findOne(vehicleId);
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = saveTripWithTrack(route, vehicle, startTime,
                latitude, longitude, endCoordinates[0], endCoordinates[1]);

        log.info("Поездка для машины с id {} завершена. Время: {} - {}", vehicleId, startTime, endTime);
    }

    private LocalDateTime saveTripWithTrack(String routeJson, Vehicle vehicle, LocalDateTime startTime,
                                            double startLat, double startLon, double endLat, double endLon) {
        LocalDateTime localDateTime = startTime;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(routeJson);
            JsonNode features = rootNode.path("features");

            if (!features.isArray() || features.isEmpty()) {
                log.warn("Маршрут пуст или не содержит данных");
                return localDateTime;
            }

            // Получение координат маршрута
            JsonNode coordinates = features.get(0).path("geometry").path("coordinates");
            log.info("Количество точек в маршруте: {}", coordinates.size());

            List<GpsData> gpsDataList = new ArrayList<>();
            GeometryFactory geometryFactory = new GeometryFactory();

            for (JsonNode coordinateNode : coordinates) {
                double longitude = coordinateNode.get(0).asDouble();
                double latitude = coordinateNode.get(1).asDouble();

                Coordinate coordinate = new Coordinate(longitude, latitude);
                Point point = geometryFactory.createPoint(coordinate);

                GpsData gpsData = new GpsData();
                gpsData.setVehicle(vehicle);
                gpsData.setCoordinates(point);
                gpsData.setTimestamp(localDateTime);

                gpsDataList.add(gpsData);
                localDateTime = localDateTime.plusSeconds(10); // Шаг в 10 секунд
            }

            // Сохранение точек GPS
            gpsDataService.saveAll(gpsDataList);

            // Расчёт расстояния
            BigDecimal distanceKm = DistanceCalculator.calculateDistance(startLat, startLon, endLat, endLon);

            // Сохранение поездки с начальной и конечной точками
            Trip trip = new Trip();
            trip.setVehicle(vehicle);
            trip.setStartTime(startTime);
            trip.setEndTime(localDateTime);
            trip.setStartGpsData(gpsDataList.getFirst()); // Первая точка
            trip.setEndGpsData(gpsDataList.getLast()); // Последняя точка
            trip.setMileage(distanceKm); // Устанавливаем расстояние

            // 3. Сохраняем поездку
            tripService.save(trip);

            // 4. Связываем все GPS точки с этой поездкой
            for (GpsData gps : gpsDataList) {
                gps.setTrip(trip);
            }

            // 5. Сохраняем GPS данные
            gpsDataService.saveAll(gpsDataList);

            log.info("Поездка сохранена. ID: {}, расстояние: {} км", trip.getId(), distanceKm);
        } catch (Exception e) {
            log.error("Ошибка при обработке маршрута: {}", e.getMessage());
        }

        return localDateTime;
    }

    public String callOpenRouteService(double sourceLatitude, double sourceLongitude,
                                       double targetLatitude, double targetLongitude) {
        String body = "{\"coordinates\":[[" + sourceLongitude + "," + sourceLatitude + "]," +
                "[" + targetLongitude + "," + targetLatitude + "]]}";
        log.info("Запрос к OpenRouteService: {}", body);

        try {
            return webClient.post()
                    .uri(openRouteUrl)
                    .header("Authorization", "Bearer " + key)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> {
                        log.error("Ошибка API: {}", clientResponse.statusCode());
                        return Mono.error(new RuntimeException("Ошибка API: " + clientResponse.statusCode()));
                    })
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Ошибка при вызове OpenRouteService: {}", e.getMessage());
            throw e;
        }
    }

    private double[] generateCoordinatesByDistance(double startLat, double startLon, double distanceKm) {
        Random random = new Random();
        log.info("Создание конечных координат для машины");

        double angle = 2 * Math.PI * random.nextDouble();
        double deltaLat = Math.toDegrees(distanceKm / EARTH_RADIUS);
        double deltaLon = Math.toDegrees(distanceKm / (EARTH_RADIUS * Math.cos(Math.toRadians(startLat))));

        double endLat = startLat + deltaLat * Math.cos(angle);
        double endLon = startLon + deltaLon * Math.sin(angle);

        log.info("Сгенерированы конечные координаты: [latitude={}, longitude={}]", endLat, endLon);
        return new double[]{endLat, endLon};
    }

}
