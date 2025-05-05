package ru.fisher.VehiclePark.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.fisher.VehiclePark.dto.TripDTO;
import ru.fisher.VehiclePark.dto.TripMapDTO;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.models.Trip;
import ru.fisher.VehiclePark.services.GpsDataService;
import ru.fisher.VehiclePark.util.GeoCoderService;
import ru.fisher.VehiclePark.util.TimeZoneUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TripMapper {

    private final GeoCoderService geoCoderService;
    private final GpsDataService gpsDataService;

    @Autowired
    public TripMapper(GeoCoderService geoCoderService, GpsDataService gpsDataService) {
        this.geoCoderService = geoCoderService;
        this.gpsDataService = gpsDataService;
    }

    public TripMapDTO convertToTripMapDTO(Trip trip, Long vehicleId, String enterpriseTimeZone, String clientTimeZone) {
        TripDTO tripDTO = convertToTripDTOWithTimeZones(trip, enterpriseTimeZone, clientTimeZone);
        TripMapDTO tripMapDTO = new TripMapDTO();
        tripMapDTO.setTrip(tripDTO);

        // Получаем все точки маршрута
        List<GpsData> gpsPoints = gpsDataService.findByVehicleAndTimeRange(
                vehicleId,
                trip.getStartTime(),
                trip.getEndTime(),
                Sort.by(Sort.Direction.ASC, "timestamp")
        );

        log.info("=== Найдено точек: {}", gpsPoints.size());

        // Преобразуем точки в DTO
        List<double[]> coordinates = new ArrayList<>();

        // Начальная точка
        if (trip.getStartGpsData() != null) {
            double[] startCoords = toLatLon(trip.getStartGpsData());
            coordinates.add(startCoords);
            tripMapDTO.setStartPoint(startCoords);
        }

        // Промежуточные точки
        coordinates.addAll(gpsPoints.stream()
                .map(this::toLatLon)
                .toList());

        // Конечная точка
        if (trip.getEndGpsData() != null) {
            double[] endCoords = toLatLon(trip.getEndGpsData());
            coordinates.add(endCoords);
            tripMapDTO.setEndPoint(endCoords);
        }

        // Устанавливаем значения в DTO
        tripMapDTO.setTrip(tripDTO);
        tripMapDTO.setCoordinates(coordinates);

        return tripMapDTO;
    }

    private double[] toLatLon(GpsData gpsData) {
        return new double[]{
                gpsData.getCoordinates().getY(),
                gpsData.getCoordinates().getX()
        };
    }


    public TripDTO convertToTripDTO(Trip trip) {
        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(trip.getId());

        // Форматируем даты для удобства чтения
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        tripDTO.setStartTime(trip.getStartTime().format(formatter));
        tripDTO.setEndTime(trip.getEndTime().format(formatter));

        // Проверяем наличие GPS-данных для начальной точки
        if (trip.getStartGpsData() != null && trip.getStartGpsData().getCoordinates() != null) {
            tripDTO.setStartPointAddress(geoCoderService.getAddressFromOpenRouteService(
                    trip.getStartGpsData().getCoordinates().getY(),
                    trip.getStartGpsData().getCoordinates().getX()
            ));
        } else {
            tripDTO.setStartPointAddress("Адрес отсутствует");
        }

        // Проверяем наличие GPS-данных для конечной точки
        if (trip.getEndGpsData() != null && trip.getEndGpsData().getCoordinates() != null) {
            tripDTO.setEndPointAddress(geoCoderService.getAddressFromOpenRouteService(
                    trip.getEndGpsData().getCoordinates().getY(),
                    trip.getEndGpsData().getCoordinates().getX()
            ));
        } else {
            tripDTO.setEndPointAddress("Адрес отсутствует");
        }

        // Рассчитываем продолжительность
        Duration duration = Duration.between(trip.getStartTime(), trip.getEndTime());
        tripDTO.setDuration(formatDuration(duration));

        return tripDTO;
    }


    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        StringBuilder formatted = new StringBuilder();
        if (hours > 0) {
            formatted.append(hours).append(" hour").append(hours > 1 ? "s " : " ");
        }
        if (minutes > 0) {
            formatted.append(minutes).append(" minute").append(minutes > 1 ? "s " : " ");
        }
        if (seconds > 0) {
            formatted.append(seconds).append(" second").append(seconds > 1 ? "s" : "");
        }
        return formatted.toString().trim();
    }


    public TripDTO convertToTripDTOWithTimeZones(Trip trip, String enterpriseZone, String clientZone) {
        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(trip.getId());

        // Форматируем даты для удобства чтения
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

        // Перевод времени старта и окончания из UTC -> enterpriseZone -> clientZone
        LocalDateTime clientStart = TimeZoneUtil.convertTimeForClient(trip.getStartTime(), enterpriseZone, clientZone);
        LocalDateTime clientEnd = TimeZoneUtil.convertTimeForClient(trip.getEndTime(), enterpriseZone, clientZone);

        tripDTO.setStartTime(clientStart.format(formatter));
        tripDTO.setEndTime(clientEnd.format(formatter));

        // Адреса начальной и конечной точки
        tripDTO.setStartPointAddress(getAddressFromGpsData(trip.getStartGpsData()));
        tripDTO.setEndPointAddress(getAddressFromGpsData(trip.getEndGpsData()));

        // Продолжительность
        Duration duration = Duration.between(trip.getStartTime(), trip.getEndTime());
        tripDTO.setDuration(formatDuration(duration));

        // Пробег
        tripDTO.setMileage(String.valueOf(trip.getMileage()
                .setScale(2, RoundingMode.HALF_UP).doubleValue()));

        return tripDTO;
    }

    private String getAddressFromGpsData(GpsData gpsData) {
        if (gpsData != null && gpsData.getCoordinates() != null) {
            return geoCoderService.getAddressFromOpenRouteService(
                    gpsData.getCoordinates().getY(),
                    gpsData.getCoordinates().getX()
            );
        } else {
            return "Адрес отсутствует";
        }

    }
}
