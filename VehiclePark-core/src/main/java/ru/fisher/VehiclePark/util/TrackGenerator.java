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
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.reactive.function.client.WebClient;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.models.Trip;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.services.GpsDataService;
import ru.fisher.VehiclePark.services.TripService;
import ru.fisher.VehiclePark.services.VehicleService;

import java.time.LocalDateTime;
import java.util.Random;

@ShellComponent
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TrackGenerator {

    private final GpsDataService gpsDataService;
    private final VehicleService vehicleService;
    private final TripService tripService;
    private final WebClient webClient;
    private static final double EARTH_RADIUS = 6371;

    @Value("${openrouteservice.api.key}")
    private String key;

//    @ShellMethod(key = "tracks")
//    public void generateTracks(@ShellOption(defaultValue = "spring") long vehicleId,
//                               double latitude, double longitude, double trackLengthKm) {
//        log.info("Генерация маршрута для машины с id {} длиной {} км", vehicleId, trackLengthKm);
//
//        // Генерация конечной точки на заданном расстоянии
//        double[] coordinates = generateCoordinatesByDistance(latitude, longitude, trackLengthKm);
//
//        // Получаем маршрут через OpenRouteService
//         String route = callOpenRouteService(latitude, longitude, coordinates[0], coordinates[1]);
//
//        // Сохраняем маршрут
//        fillAndSaveTrack(route, vehicleId);
//    }

    @ShellMethod(key = "tracks")
    public void generateTracks(@ShellOption(defaultValue = "spring") long vehicleId,
                               double latitude, double longitude, double trackLengthKm) {
        log.info("Генерация маршрута для машины с id {} длиной {} км", vehicleId, trackLengthKm);

        // Генерация конечной точки на заданном расстоянии
        double[] coordinates = generateCoordinatesByDistance(latitude, longitude, trackLengthKm);

        // Получаем маршрут через OpenRouteService
        String route = callOpenRouteService(latitude, longitude, coordinates[0], coordinates[1]);

        // Создаем новую поездку
        Vehicle vehicle = vehicleService.findOne(vehicleId);
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = fillAndSaveTrack(route, vehicle); // Сохраняем маршрут и получаем время окончания

        // Сохраняем поездку
        saveTrip(vehicle, startTime, endTime);
    }

    private void saveTrip(Vehicle vehicle, LocalDateTime startTime, LocalDateTime endTime) {
        Trip trip = new Trip();
        trip.setVehicle(vehicle);
        trip.setStartTime(startTime);
        trip.setEndTime(endTime);

        tripService.save(trip); // Используем сервис для сохранения поездки
        log.info("Сохранена поездка для машины с id {}: начало {}, конец {}", vehicle.getId(), startTime, endTime);
    }

    private LocalDateTime fillAndSaveTrack(String routeJson, Vehicle vehicle) {
        LocalDateTime localDateTime = LocalDateTime.now();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(routeJson);
            JsonNode features = rootNode.path("features");

            if (features.isArray() && !features.isEmpty()) {
                JsonNode coordinates = features.get(0).path("geometry").path("coordinates");
                log.info("Количество точек в маршруте: {}", coordinates.size());

                for (JsonNode coordinateNode : coordinates) {
                    double longitude = coordinateNode.get(0).asDouble();
                    double latitude = coordinateNode.get(1).asDouble();

                    GeometryFactory geometryFactory = new GeometryFactory();
                    Coordinate coordinate = new Coordinate(longitude, latitude);
                    Point point = geometryFactory.createPoint(coordinate);

                    GpsData gpsData = new GpsData();
                    gpsData.setVehicle(vehicle);
                    gpsData.setCoordinates(point);
                    gpsData.setTimestamp(localDateTime);
                    gpsDataService.save(gpsData);

                    // Увеличиваем время для следующей точки
                    localDateTime = localDateTime.plusSeconds(10);
                    log.info("Сохранена точка: {}, с временем {}", point, localDateTime);
                }
            } else {
                log.warn("Маршрут пуст или не содержит данных");
            }
        } catch (Exception e) {
            log.error("Ошибка при обработке JSON-ответа", e);
        }

        return localDateTime; // Возвращаем время окончания трека
    }

    private String callOpenRouteService(double sourceLatitude, double sourceLongitude,
                                        double targetLatitude, double targetLongitude) {
        String body = "{\"coordinates\":[[" + sourceLongitude + "," + sourceLatitude + "]," +
                "[" + targetLongitude + "," + targetLatitude + "]]}";
        log.info("Запрос к OpenRouteService: {}", body);

        try {
            String response = webClient.post()
                    .uri("https://api.openrouteservice.org/v2/directions/driving-car/geojson")
                    .header("Authorization", "Bearer " + key)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class) // Получаем ответ как строку
                    .block();

            log.info("Ответ OpenRouteService получен: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Ошибка при вызове OpenRouteService: {}", e.getMessage());
            throw e;
        }
    }

//    private void fillAndSaveTrack(String routeJson, long vehicleId) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode rootNode = objectMapper.readTree(routeJson);
//            JsonNode features = rootNode.path("features");
//
//            if (features.isArray() && !features.isEmpty()) {
//                JsonNode coordinates = features.get(0).path("geometry").path("coordinates");
//                log.info("Количество точек в маршруте: {}", coordinates.size());
//
//                Vehicle vehicle = vehicleService.findOne(vehicleId);
//                LocalDateTime localDateTime = LocalDateTime.now();
//
//                for (JsonNode coordinateNode : coordinates) {
//                    double longitude = coordinateNode.get(0).asDouble();
//                    double latitude = coordinateNode.get(1).asDouble();
//
//                    GeometryFactory geometryFactory = new GeometryFactory();
//                    Coordinate coordinate = new Coordinate(longitude, latitude);
//                    Point point = geometryFactory.createPoint(coordinate);
//
//                    GpsData gpsData = new GpsData();
//                    gpsData.setVehicle(vehicle);
//                    gpsData.setCoordinates(point);
//                    gpsData.setTimestamp(localDateTime);
//                    gpsDataService.save(gpsData);
//                    localDateTime = localDateTime.plusSeconds(10);
//                    log.info("Сохранена точка: {}, c временем {}", point, localDateTime);
//                }
//            } else {
//                log.warn("Маршрут пуст или не содержит данных");
//            }
//        } catch (Exception e) {
//            log.error("Ошибка при обработке JSON-ответа", e);
//        }
//    }


    public double[] generateCoordinatesByDistance(double startLat, double startLon, double distanceKm) {
        Random random = new Random();
        log.info("Создание точек для машины");

        // Генерация случайного угла направления (в радианах)
        double angle = 2 * Math.PI * random.nextDouble();

        // Преобразование расстояния из километров в градусы
        double deltaLat = Math.toDegrees(distanceKm / EARTH_RADIUS);
        double deltaLon = Math.toDegrees(distanceKm / (EARTH_RADIUS * Math.cos(Math.toRadians(startLat))));

        // Вычисление конечных координат
        double endLat = startLat + deltaLat * Math.cos(angle);
        double endLon = startLon + deltaLon * Math.sin(angle);

        log.info("Сгенерированы конечные координаты: [latitude={}, longitude={}]", endLat, endLon);

        return new double[]{endLat, endLon};
    }

    /* Бэк от первого варианта */

//    public double[] generateRandomCoordinates(double centerLat, double centerLon, double radiusInKm) {
//        Random random = new Random();
//        log.info("Создание точек для машины");
//
//        // Генерация случайного угла для определения направления
//        double angle = 2 * Math.PI * random.nextDouble();
//        // Генерация случайного радиуса в пределах указанного радиуса
//        double distance = Math.sqrt(random.nextDouble()) * radiusInKm;
//
//        if (distance < 0.1) { // Если расстояние меньше 100 метров
//            log.warn("Начальная и конечная точки находятся слишком близко. Расстояние: {} км", distance);
//        }
//
//        // Преобразование расстояния из километров в градусы
//        double deltaLat = Math.toDegrees(distance / EARTH_RADIUS);
//        double deltaLon = Math.toDegrees(distance / (EARTH_RADIUS * Math.cos(Math.toRadians(centerLat))));
//
//        // Вычисление новой широты и долготы
//        double newLat = centerLat + deltaLat * Math.cos(angle);
//        double newLon = centerLon + deltaLon * Math.sin(angle);
//
//        return new double[]{newLat, newLon};
//    }


//    private GeoJSONResponse callOpenRouteService(double sourceLatitude, double sourceLongitude,
//                                                 double targetLatitude, double targetLongitude) {
//        String body = "{\"coordinates\":[[" + sourceLongitude + "," + sourceLatitude + "]," +
//                "[" + targetLongitude + "," + targetLatitude + "]]}";
//        log.info("Запрос к OpenRouteService: {}", body);
//
//        try {
//            GeoJSONResponse response = webClient.post()
//                    .uri("https://api.openrouteservice.org/v2/directions/driving-car/geojson")
//                    .header("Authorization",
//                            "Bearer " + key)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .bodyValue(body)
//                    .retrieve()
//                    .bodyToMono(GeoJSONResponse.class)
//                    .block();
//
//            log.info("Ответ OpenRouteService получен");
//            return response;
//        } catch (Exception e) {
//            log.error("Ошибка при вызове OpenRouteService: {}", e.getMessage());
//            throw e;
//        }
//    }


//    private void fillAndSaveTrack(GeoJSONResponse route, long vehicleId) {
//        if (route == null || route.getFeatures() == null || route.getFeatures().isEmpty()) {
//            log.warn("Маршрут пуст или не содержит данных");
//            return;
//        }
//
//        // Обрабатываем точки маршрута
//        List<List<Double>> routeCoordinates =
//                route.getFeatures()
//                .stream()
//                .flatMap(feature -> feature.getGeometry().getCoordinates().stream())
//                .toList();

        // Получаем список координат из маршрута
//        List<List<Double>> routeCoordinates = new ArrayList<>();
//        route.getFeatures()
//                .stream()
//               .findFirst()
//                 .ifPresent(feature -> routeCoordinates.add(feature.getGeometry().getCoordinates()));


        // Логирование количества точек
//        log.info("Количество точек в маршруте: {}", routeCoordinates.size());
//
//        Vehicle vehicle = vehicleService.findOne(vehicleId);
//        LocalDateTime localDateTime = LocalDateTime.now();
//        for (List<Double> coor : routeCoordinates) {
//            if (coor == null || coor.size() < 2) {
//                log.warn("Пропуск некорректной точки маршрута: {}", coor);
//                continue;
//            }
//
//            GeometryFactory geometryFactory = new GeometryFactory();
//            Coordinate coordinate = new Coordinate(coor.get(0), coor.get(1));
//            Point point = geometryFactory.createPoint(coordinate);
//
//            GpsData gpsData = new GpsData();
//            gpsData.setVehicle(vehicle);
//            gpsData.setCoordinates(point);
//            gpsData.setTimestamp(localDateTime);
//            gpsDataService.save(gpsData);
//            localDateTime = localDateTime.plusSeconds(10);
//            log.info("Сохранена точка: {}, c временем {}", point, localDateTime);
//        }
//    }

}
