package ru.fisher.VehiclePark.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.models.Trip;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.services.GpsDataService;
import ru.fisher.VehiclePark.services.TripService;
import ru.fisher.VehiclePark.services.VehicleService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@ShellComponent
@RequiredArgsConstructor
public class BulkTripGeneratorReactive {

    private final GpsDataService gpsDataService;
    private final VehicleService vehicleService;
    private final TripService tripService;
    private final WebClient webClient;
    private final Random rand = new Random();

    @Value("${openrouteservice.api.key}")
    public String key;

    @Value("${openrouteservice.url}")
    public String openRouteUrl;

    @Value("${openrouteservice.url.snap}")
    public String openRouteSnap;

    private static final double EARTH_RADIUS = 6371;

    // Область для генерации точек (Липецк)
    private static final double LIPETSK_MIN_LAT = 51.0;
    private static final double LIPETSK_MAX_LAT = 53.0;
    private static final double LIPETSK_MIN_LON = 38.0;
    private static final double LIPETSK_MAX_LON = 40.5;

    private static final double MOSCOW_MIN_LAT = 55.334;
    private static final double MOSCOW_MAX_LAT = 56.025;
    private static final double MOSCOW_MIN_LON = 37.321;
    private static final double MOSCOW_MAX_LON = 38.100;

//    @ShellMethod(key = "generate-trips-reactive", value = "Generate trips for vehicles in bulk (Reactive)")
//    public Mono<Void> generateTripsReactive(@ShellOption(defaultValue = "5") int vehicleCounts,
//                                            @ShellOption(defaultValue = "5") int tripsPerVehicle,
//                                            @ShellOption String startDate,
//                                            @ShellOption String endDate) {
//
//        return Flux.range(0, vehicleCounts)
//                .flatMap(i -> Mono.fromCallable(() -> vehicleService.findOne(rand.nextLong(15000) + 1))
//                        .subscribeOn(Schedulers.boundedElastic()))
//                .flatMap(vehicle -> Flux.range(0, tripsPerVehicle)
//                        .flatMap(i -> generateReactiveTripForVehicle(vehicle, startDate, endDate)))
//                .doOnComplete(() -> log.info("Реактивная генерация поездок завершена"))
//                .then();
//    }

//    @ShellMethod(key = "generate-trips-reactive", value = "Generate trips for vehicles in bulk (Reactive)")
//    public Flux<String> generateTripsReactiveWithOutput(@ShellOption(defaultValue = "5") int vehicleCounts,
//                                                        @ShellOption(defaultValue = "5") int tripsPerVehicle,
//                                                        @ShellOption String startDate,
//                                                        @ShellOption String endDate) {
//        return Flux.range(0, vehicleCounts)
//                .flatMap(i -> Mono.fromCallable(() -> vehicleService.findOne(rand.nextLong(15000) + 1))
//                        .subscribeOn(Schedulers.boundedElastic()))
//                .flatMap(vehicle -> Flux.range(0, tripsPerVehicle)
//                        .flatMap(i -> generateReactiveTripForVehicle(vehicle, startDate, endDate)
//                                .thenReturn("Создана поездка для машины " + vehicle.getNumber())))
//                .onErrorResume(e -> Flux.just("Ошибка генерации: " + e.getMessage()))
//                .concatWith(Mono.just("Генерация завершена"));
//    }

    @ShellMethod(key = "generate-trips-reactive", value = "Generate trips for vehicles in bulk (Reactive with logging)")
    public void generateTripsReactiveWithLogging(@ShellOption(defaultValue = "5") int vehicleCounts,
                                                 @ShellOption(defaultValue = "5") int tripsPerVehicle,
                                                 @ShellOption String startDate,
                                                 @ShellOption String endDate) {

        generateTripsReactive(vehicleCounts, tripsPerVehicle, startDate, endDate)
                .doOnSubscribe(subscription -> log.info("🔧 Начало генерации поездок..."))
                .doOnNext(message -> log.info("➡ {}", message))
                .doOnError(error -> log.error("❗ Ошибка генерации поездок: {}", error.getMessage()))
                .doOnComplete(() -> log.info("✅ Генерация завершена"))
                .subscribe(); // Подписываемся, чтобы поток запустился
    }

    public Flux<String> generateTripsReactive(int vehicleCounts, int tripsPerVehicle, String startDate, String endDate) {
        return Flux.range(0, vehicleCounts)
                .flatMap(i -> Mono.fromCallable(() -> vehicleService.findOne(rand.nextLong(15000) + 1))
                        .subscribeOn(Schedulers.boundedElastic()))
                .flatMap(vehicle -> Flux.range(0, tripsPerVehicle)
                        .flatMap(i -> generateReactiveTripForVehicle(vehicle, startDate, endDate)
                                .thenReturn("Создана поездка для машины " + vehicle.getNumber())))
                .onErrorResume(e -> Flux.just("Ошибка генерации: " + e.getMessage()))
                .concatWith(Mono.just("Генерация завершена"));
    }

    private Mono<Void> generateReactiveTripForVehicle(Vehicle vehicle, String startDate, String endDate) {
        return tryGenerateTrip(vehicle, startDate, endDate, 50);
    }

    private Mono<Void> tryGenerateTrip(Vehicle vehicle, String startDate, String endDate,
                                       int maxRetries) {
        return Mono.defer(() -> {
                    double[] startCoordinates = generateRandomPointInArea
                            (MOSCOW_MIN_LAT, MOSCOW_MAX_LAT, MOSCOW_MIN_LON, MOSCOW_MAX_LON);
                    double randomDistance = 25 + rand.nextDouble() * 75; // Расстояние от 50 до 100 км
                    LocalDateTime tripStartTime = generateRandomDateBetween(startDate, endDate);
                    double[] endCoordinates = generateCoordinatesByDistance
                            (startCoordinates[0], startCoordinates[1], randomDistance);

                    return generateRouteReactive(startCoordinates, randomDistance)
                            .flatMap(route -> saveTripWithTrackReactive
                                    (route, vehicle, tripStartTime, startCoordinates, endCoordinates))
                            .onErrorResume(TooManyRequestsException.class, e -> {
                                log.warn("Превышен лимит запросов (429). Ждем 1 минуту и пробуем заново...");
                                return Mono.delay(Duration.ofMinutes(1))
                                        .then(tryGenerateTrip(vehicle, startDate, endDate, maxRetries));
                            })
                            .onErrorResume(NotFoundException.class, e -> {
                                double newDistance = randomDistance - 1;
                                if (newDistance < 1 || maxRetries <= 0) {
                                    return Mono.error
                                            (new RuntimeException
                                                    ("Не удалось сгенерировать поездку: " +
                                                            "минимальное расстояние достигнуто"));
                                }
                                log.warn("Маршрут не найден. Уменьшаем расстояние до {} км и пробуем снова...",
                                        newDistance);
                                return tryGenerateTrip(vehicle, startDate, endDate, maxRetries - 1);
                            });
                })
                .retryWhen(Retry.fixedDelay(maxRetries, Duration.ofMillis(500))
                        .filter(e ->
                                !(e instanceof TooManyRequestsException || e instanceof NotFoundException)))
                .onErrorResume(e -> {
                    log.error("❗ Генерация поездки не удалась окончательно: {}", e.getMessage());
                    return Mono.empty();
                });
    }

    private Mono<String> generateRouteReactive(double[] startCoordinates, double distanceKm) {
        double[] endCoordinates = generateCoordinatesByDistance(startCoordinates[0], startCoordinates[1], distanceKm);
        String body = "{\"coordinates\":[[" + startCoordinates[1] + "," + startCoordinates[0] + "],[" + endCoordinates[1] + "," + endCoordinates[0] + "]]}";
        return webClient.post()
                .uri(openRouteUrl)
                .header("Authorization", "Bearer " + key)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.value() == 429,
                        r -> Mono.error(new TooManyRequestsException("429 Too Many Requests")))
                .bodyToMono(String.class);
    }

    private Mono<Void> saveTripWithTrackReactive(String routeJson, Vehicle vehicle,
                                                 LocalDateTime startTime,
                                                 double[] startCoordinates, double[] endCoordinates) {
        return Mono.fromCallable(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode features = objectMapper.readTree(routeJson).path("features");
            if (!features.isArray() || features.isEmpty()) throw new RuntimeException("Маршрут пуст");

            List<GpsData> gpsDataList = new ArrayList<>();
            GeometryFactory geometryFactory = new GeometryFactory();
            LocalDateTime localDateTime = startTime;

            for (JsonNode coordinateNode : features.get(0).path("geometry").path("coordinates")) {
                double longitude = coordinateNode.get(0).asDouble();
                double latitude = coordinateNode.get(1).asDouble();

                GpsData gpsData = new GpsData();
                gpsData.setVehicle(vehicle);
                Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
                gpsData.setCoordinates(point);
                gpsData.setTimestamp(localDateTime);
                gpsDataList.add(gpsData);
                localDateTime = localDateTime.plusSeconds(10);
            }

            // 1. Сохраняем GPS-данные без trip
            gpsDataService.saveAll(gpsDataList);

            BigDecimal distanceKm = DistanceCalculator.calculateDistance(startCoordinates[0], startCoordinates[1],
                    endCoordinates[0], endCoordinates[1]);

            // 2. Создаём поездку с уже сохранёнными точками
            Trip trip = new Trip();
            trip.setVehicle(vehicle);
            trip.setStartTime(startTime);
            trip.setEndTime(localDateTime);
            trip.setStartGpsData(gpsDataList.getFirst());
            trip.setEndGpsData(gpsDataList.getLast());
            trip.setMileage(distanceKm);

            // Сохраняем поездку
            tripService.save(trip);

            // 3. Привязываем trip ко всем GPS-данным и обновляем
            for (GpsData gps : gpsDataList) {
                gps.setTrip(trip);
            }

            // Сохраняем GPS-данные с поездкой
            gpsDataService.saveAll(gpsDataList); // второй вызов обновляет

            log.info("Реактивно сохранена поездка ID={} для машины ID={}", trip.getId(), vehicle.getId());
            return true;
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    private double[] generateRandomPointInArea(double minLat, double maxLat, double minLon, double maxLon) {
        double latitude = minLat + (maxLat - minLat) * rand.nextDouble();
        double longitude = minLon + (maxLon - minLon) * rand.nextDouble();
        log.info("Сгенерирована начальная точка: [latitude={}, longitude={}]", latitude, longitude);
        return new double[]{latitude, longitude};
    }

    private double[] generateCoordinatesByDistance(double startLat, double startLon, double distanceKm) {
        double angle = 2 * Math.PI * rand.nextDouble();
        double deltaLat = Math.toDegrees(distanceKm / EARTH_RADIUS);
        double deltaLon = Math.toDegrees(distanceKm / (EARTH_RADIUS * Math.cos(Math.toRadians(startLat))));

        double endLat = startLat + deltaLat * Math.cos(angle);
        double endLon = startLon + deltaLon * Math.sin(angle);

        log.info("Сгенерирована конечная точка: [latitude={}, longitude={}]", endLat, endLon);
        return new double[]{endLat, endLon};
    }

    private LocalDateTime generateRandomDateBetween(String startDate, String endDate) {
        // Преобразуем строки в LocalDate
        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);

        // Преобразуем LocalDate в LocalDateTime (начало дня)
        long startEpoch = startLocalDate.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        long endEpoch = endLocalDate.atStartOfDay(ZoneOffset.UTC).toEpochSecond();

        // Генерация случайного времени между двумя датами
        long randomEpoch = startEpoch + (long) (Math.random() * (endEpoch - startEpoch));
        return LocalDateTime.ofEpochSecond(randomEpoch, 0, ZoneOffset.UTC);
    }

    private static class TooManyRequestsException extends RuntimeException {
        public TooManyRequestsException(String message) {
            super(message);
        }
    }

    private static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }
}
