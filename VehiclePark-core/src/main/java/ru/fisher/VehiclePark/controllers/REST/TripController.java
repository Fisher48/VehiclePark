package ru.fisher.VehiclePark.controllers.REST;//package ru.fisher.VehiclePark.controllers.REST;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.locationtech.jts.geom.Point;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.Sort;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import ru.fisher.VehiclePark.dto.GpsDataDTO;
//import ru.fisher.VehiclePark.dto.TripDTO;
//import ru.fisher.VehiclePark.models.*;
//import ru.fisher.VehiclePark.services.EnterpriseService;
//import ru.fisher.VehiclePark.services.GpsDataService;
//import ru.fisher.VehiclePark.services.TripService;
//import ru.fisher.VehiclePark.services.VehicleService;
//import ru.fisher.VehiclePark.util.GeoCoderService;
//import ru.fisher.VehiclePark.util.TimeZoneUtil;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@RestController
//@RequestMapping("/api/trips")
//@RequiredArgsConstructor
//@Slf4j
//public class TripController {
//
//    private final TripService tripService;
//    private final GpsDataService gpsDataService;
//    private final VehicleService vehicleService;
//    private final EnterpriseService enterpriseService;
//    private final ModelMapper modelMapper;
//    private final GeoCoderService geoCoderService;
//
//    @Value("${openrouteservice.api.key}")
//    private String apiKey;
//
//    /**
//     * Получить все точки трека за указанный период и для заданного автомобиля.
//     */
//    @GetMapping("/vehicle/{vehicleId}")
//    public GeoJSONResponse getTripPointsByVehicle(
//            @PathVariable Long vehicleId,
//            @RequestParam String startTime,
//            @RequestParam String endTime,
//            @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone) {
//
//        // Получаем информацию о машине и предприятии
//        Vehicle vehicle = vehicleService.findOne(vehicleId);
//        Enterprise enterprise = enterpriseService.findOne(vehicle.getEnterprise().getId());
//
//        LocalDateTime start = LocalDateTime.parse(startTime);
//        LocalDateTime end = LocalDateTime.parse(endTime);
//
//        // Таймзона предприятия
//        String enterpriseTimeZone = enterprise.getTimezone() != null ? enterprise.getTimezone() : "UTC";
//
//        // Конвертация времени из таймзоны клиента в UTC
//        start = TimeZoneUtil.convertToUtc(start, enterpriseTimeZone);
//        end = TimeZoneUtil.convertToUtc(end, enterpriseTimeZone);
//
//        // Получение поездок в указанном временном диапазоне
//        List<Trip> trips = tripService.findTripsForVehicleInTimeRange(vehicleId, start, end);
//
//        // Получение точек GPS для всех поездок
//        List<GpsData> gpsPoints = new ArrayList<>();
//        for (Trip trip : trips) {
//            gpsPoints.addAll(gpsDataService.findByVehicleAndTimeRange(vehicleId, trip.getStartTime(), trip.getEndTime(),
//                   Sort.by(Sort.Direction.ASC, "timestamp")));
//        }
//
//        // Конвертация в формат GeoJSON
//        List<GpsDataDTO> gpsDataDTOList = gpsPoints.stream()
//                .map(gpsData -> convertToPointGpsDTO_forAPI(gpsData, clientTimeZone, enterpriseTimeZone))
//                .toList();
//
//        return convertToGeoJSON(gpsDataDTOList);
//    }
//
//    private TripDTO convertToTripDTO_forAPI(Trip trip, String clientTimeZone, String enterpriseTimeZone) {
//
//        TripDTO tripDTO = modelMapper.map(trip, TripDTO.class);
//
//        // Преобразование времени
//        LocalDateTime utcTimestamp = trip.getStartTime();
//        ZoneId clientZoneId = ZoneId.of(clientTimeZone);
//        ZoneId enterpriseZoneId = ZoneId.of(enterpriseTimeZone);
//
//        // Преобразуем время из UTC в таймзону предприятия
//        LocalDateTime enterpriseTimestamp = utcTimestamp.atZone(ZoneId.of("UTC"))
//                .withZoneSameInstant(enterpriseZoneId)
//                .toLocalDateTime();
//
//        // Преобразуем время из таймзоны предприятия в таймзону клиента
//        LocalDateTime clientTimestamp = enterpriseTimestamp.atZone(enterpriseZoneId)
//                .withZoneSameInstant(clientZoneId)
//                .toLocalDateTime();
//
//        // Форматируем дату для удобства чтения
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
//        // Устанавливаем преобразованное время в DTO
//        tripDTO.setStartTime(clientTimestamp.format(formatter));
//
//        return tripDTO;
//    }
//
//    /**
//     * Получить список поездок для заданного автомобиля и временного диапазона.
//     */
//    @GetMapping("/{vehicleId}")
//    public List<TripDTO> getTripsForVehicle(
//            @PathVariable Long vehicleId,
//            @RequestParam String startTime,
//            @RequestParam String endTime) {
//
//        LocalDateTime start = LocalDateTime.parse(startTime);
//        LocalDateTime end = LocalDateTime.parse(endTime);
//
//        return tripService.findTripsForVehicleInTimeRange(vehicleId, start, end)
//                .stream()
//                .map(this::convertToTripDTO)
//                .toList();
//    }
//
//    private TripDTO convertToTripDTO(Trip trip) {
//        TripDTO tripDTO = new TripDTO();
//        tripDTO.setId(trip.getId());
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
//        tripDTO.setStartTime(trip.getStartTime().format(formatter));
//        tripDTO.setEndTime(trip.getEndTime().format(formatter));
//
//        tripDTO.setDuration(formatDuration(Duration.between(trip.getStartTime(), trip.getEndTime())));
//
//        // Проверяем наличие GPS-данных для начальной точки
//        if (trip.getStartGpsData() != null && trip.getStartGpsData().getCoordinates() != null) {
//            tripDTO.setStartPointAddress(geoCoderService.getAddressFromOpenRouteService(
//                    trip.getStartGpsData().getCoordinates().getY(),
//                    trip.getStartGpsData().getCoordinates().getX()
//            ));
//        } else {
//            tripDTO.setStartPointAddress("Адрес отсутствует");
//        }
//
//        // Проверяем наличие GPS-данных для конечной точки
//        if (trip.getEndGpsData() != null && trip.getEndGpsData().getCoordinates() != null) {
//            tripDTO.setEndPointAddress(geoCoderService.getAddressFromOpenRouteService(
//                    trip.getEndGpsData().getCoordinates().getY(),
//                    trip.getEndGpsData().getCoordinates().getX()
//            ));
//        } else {
//            tripDTO.setEndPointAddress("Адрес отсутствует");
//        }
//
//        return tripDTO;
//    }
//
//    public static String formatDuration(Duration duration) {
//        long hours = duration.toHours();
//        long minutes = duration.toMinutesPart();
//        long seconds = duration.toSecondsPart();
//        StringBuilder formatted = new StringBuilder();
//        if (hours > 0) {
//            formatted.append(hours).append(" hour").append(hours > 1 ? "s " : " ");
//        }
//        if (minutes > 0) {
//            formatted.append(minutes).append(" minute").append(minutes > 1 ? "s " : " ");
//        }
//        if (seconds > 0) {
//            formatted.append(seconds).append(" second").append(seconds > 1 ? "s" : "");
//        }
//        return formatted.toString().trim();
//    }
//
//    private GeoJSONResponse convertToGeoJSON(List<GpsDataDTO> gpsDataDTOList) {
//        List<GeoJSONResponse.Feature> features = gpsDataDTOList.stream()
//                .map(gpsDataDTO -> new GeoJSONResponse.Feature(
//                        new GeoJSONResponse.Geometry(
//                                List.of(gpsDataDTO.getLongitude(),
//                                        gpsDataDTO.getLatitude()) // Каждая точка [longitude, latitude]
//                        ),
//                        new GeoJSONResponse.Properties(gpsDataDTO.getTimestamp()) // Временная метка
//                ))
//                .toList();
//
//        return new GeoJSONResponse(features);
//    }
//
//    private GpsDataDTO convertToPointGpsDTO_forAPI(GpsData gpsData, String clientTimeZone, String enterpriseTimeZone) {
//
//        GpsDataDTO gpsDataDTO = modelMapper.map(gpsData, GpsDataDTO.class);
//
//        // Преобразование времени
//        LocalDateTime utcTimestamp = gpsData.getTimestamp();
//        ZoneId clientZoneId = ZoneId.of(clientTimeZone);
//        ZoneId enterpriseZoneId = ZoneId.of(enterpriseTimeZone);
//
//        // Преобразуем время из UTC в таймзону предприятия
//        LocalDateTime enterpriseTimestamp = utcTimestamp.atZone(ZoneId.of("UTC"))
//                .withZoneSameInstant(enterpriseZoneId)
//                .toLocalDateTime();
//
//        // Преобразуем время из таймзоны предприятия в таймзону клиента
//        LocalDateTime clientTimestamp = enterpriseTimestamp.atZone(enterpriseZoneId)
//                .withZoneSameInstant(clientZoneId)
//                .toLocalDateTime();
//
//        // Форматируем дату для удобства чтения
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
//        // Устанавливаем преобразованное время в DTO
//        gpsDataDTO.setTimestamp(clientTimestamp.format(formatter));
//
//        return gpsDataDTO;
//    }
//
//    @GetMapping("/vehicle/{vehicleId}/trips")
//    public List<TripDTO> getTripsForVehicle(
//            @PathVariable Long vehicleId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
//
//        List<Trip> trips = tripService.findTripsForVehicleInTimeRange(vehicleId, startTime, endTime);
//
//        return trips.stream()
//                .map(this::convertToTripDTO)
//                .toList();
//    }
//
//    @Value("${yandex.api.key}")
//    private String yandexApiKey;
//
//    @GetMapping("/{managerId}/vehicle/{vehicleId}/map")
//    public ResponseEntity<String> getMapUrl(
//            @PathVariable Long managerId,
//            @PathVariable Long vehicleId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
//
//        // Получаем поездки
//        List<Trip> trips = tripService.findTripsForVehicleInTimeRange(vehicleId, startTime, endTime);
//
//        // Формируем параметры для полилиний
//        StringBuilder polyline = new StringBuilder();
//        for (Trip trip : trips) {
//            List<GpsData> gpsDataList = trip.getVehicle().getGpsData();
//            for (GpsData data : gpsDataList) {
//                Point coordinates = data.getCoordinates();
//                polyline.append(coordinates.getX()).append(",").append(coordinates.getY()).append("~");
//            }
//        }
//
//        // Удаляем последний символ
//        if (!polyline.isEmpty()) {
//            polyline.setLength(polyline.length() - 1);
//        }
//
//        // Формируем URL для карты
//        String mapUrl = "https://static-maps.yandex.ru/1.x/?l=map"
//                + "&pl=" + polyline
//                + "&size=650,450"
//                + "&key=" + yandexApiKey;
//
//        return ResponseEntity.ok(mapUrl);
//    }
//}
