package ru.fisher.VehiclePark.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.fisher.VehiclePark.dto.TripDTO;
import ru.fisher.VehiclePark.dto.TripMapDTO;
import ru.fisher.VehiclePark.exceptions.ResourceNotFoundException;
import ru.fisher.VehiclePark.mapper.TripMapper;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.models.Trip;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.repositories.TripRepository;
import ru.fisher.VehiclePark.util.DistanceCalculator;
import ru.fisher.VehiclePark.util.TimeZoneUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TripService {

    private final TripRepository tripRepository;
    private final EnterpriseService enterpriseService;
    private final GpsDataService gpsDataService;
    private final TripMapper tripMapper;
    private final GpxParserService gpxParserService;

    @Autowired
    public TripService(TripRepository tripRepository, EnterpriseService enterpriseService,
                       GpsDataService gpsDataService, TripMapper tripMapper, GpxParserService gpxParserService) {
        this.tripRepository = tripRepository;
        this.enterpriseService = enterpriseService;
        this.gpsDataService = gpsDataService;
        this.tripMapper = tripMapper;
        this.gpxParserService = gpxParserService;
    }

    public Trip findOne(Long id) {
        Optional<Trip> foundTrip = tripRepository.findById(id);
        return foundTrip.orElseThrow(
                () -> new ResourceNotFoundException("Trip with " + id + " id, not exists"));
    }

    @Cacheable(value = "tripsForVehicle", key = "{#vehicleId, #startTime?.toString(), #endTime?.toString()}")
    public List<Trip> findTripsForVehicleInTimeRange(Long vehicleId, LocalDateTime start, LocalDateTime end) {
        return tripRepository.findTripsForVehicleInTimeRange(vehicleId, start, end);
    }

//    public List<Trip> findByEnterpriseId(Long enterpriseId) {
//        return tripRepository.findByEnterpriseId(enterpriseId);
//    }

    public List<Trip> findTripsForEnterpriseInRange(Long enterpriseId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        List<Trip> trips = tripRepository.findTripsByEnterpriseAndTimeRange(enterpriseId, dateFrom, dateTo);
        return trips;
    }

    public List<Trip> findTripsByVehicle(Long vehicleId) {
        // Получить все поездки для автомобиля
        return tripRepository.findByVehicleId(vehicleId);
    }

    @Transactional
    public void save(Trip trip) {
        tripRepository.save(trip);
    }

    @Transactional
    public void delete(Long id) {
        tripRepository.deleteById(id);
    }

    @Transactional
    public void saveAll(List<Trip> trips) {
        tripRepository.saveAll(trips);
    }

    public boolean isTimeRangeOverlapping(Long vehicleId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Trip> existingTrips = tripRepository.findByVehicleIdAndOverlapTimeRange(vehicleId, startTime, endTime);
        return !existingTrips.isEmpty();
    }

    @Cacheable(value = "tripMaps", key = "{#enterpriseId, #vehicleId, #startTime?.toString(), #endTime?.toString()}")
    public List<TripMapDTO> getTripMapData(Long enterpriseId, Long vehicleId,
                                           LocalDateTime startTime, LocalDateTime endTime,
                                           String clientTimeZone) {
        // 1. Получаем необходимые сущности
        Enterprise enterprise = enterpriseService.findOne(enterpriseId);

        // 2. Обрабатываем временной диапазон
        String timezone = Optional.ofNullable(enterprise.getTimezone()).orElse("UTC");
        LocalDateTime[] range = processTimeRange(startTime, endTime, timezone);

        log.info("=== Vehicle ID: {}", vehicleId);
        log.info("=== Временной диапазон: {} - {}", startTime, endTime);

        // 3. Получаем и сортируем поездки по времени начала
        List<Trip> trips = tripRepository.findTripsForVehicleInTimeRange(vehicleId, range[0], range[1]);
        trips.sort(Comparator.comparing(Trip::getStartTime));

        // 4. Собираем результат
        return trips.stream()
                .map(trip ->  tripMapper.convertToTripMapDTO(trip, vehicleId, timezone, clientTimeZone))
                .toList();
    }

    // Конвертируем время из таймзоны предприятия в UTC
    private LocalDateTime[] processTimeRange(LocalDateTime start, LocalDateTime end, String timezone) {
        LocalDateTime defaultStart = LocalDateTime.now().minusDays(1);
        LocalDateTime defaultEnd = LocalDateTime.now();

        return new LocalDateTime[] {
                start != null ? TimeZoneUtil.convertToUtc(start, timezone) : defaultStart,
                end != null ? TimeZoneUtil.convertToUtc(end, timezone) : defaultEnd
        };
    }


    @Transactional
    public Trip uploadTripFromGpx(Vehicle vehicle, LocalDateTime startTime,
                                  LocalDateTime endTime, MultipartFile gpxFile) {

        // Проверяем, что временной диапазон не пересекается с существующими поездками
        if (isTimeRangeOverlapping(vehicle.getId(), startTime, endTime)) {
            throw new IllegalArgumentException("Наложение с существующей поездкой");
        }

        // 1. Парсим точки из GPX
        List<GpsData> gpsDataList = gpxParserService.parseGpxFile(vehicle, gpxFile, startTime, endTime);

        // 2. Создаем Trip (сначала без связки с GPS)
        Trip trip = new Trip();
        trip.setVehicle(vehicle);
        trip.setStartTime(startTime);
        trip.setEndTime(endTime);
        trip.setStartGpsData(gpsDataList.getFirst());
        trip.setEndGpsData(gpsDataList.getLast());
        trip.setMileage(DistanceCalculator.calculateMileageFromGpx(gpsDataList));

        gpsDataService.saveAll(gpsDataList);

        // 3. Сохраняем поездку
        tripRepository.save(trip);

        // 4. Связываем все GPS точки с этой поездкой
        for (GpsData gps : gpsDataList) {
            gps.setTrip(trip);
        }

        // 5. Сохраняем GPS данные
        gpsDataService.saveAll(gpsDataList);

        return trip;
    }

    public List<TripDTO> getTripsForVehicleWithTimezone(Vehicle vehicle,
                                                        String clientTimeZone,
                                                        LocalDateTime startTime,
                                                        LocalDateTime endTime) {
        // Получаем информацию о машине и предприятии
        Long vehicleId = vehicle.getId();
        Enterprise enterprise = vehicle.getEnterprise();

        String enterpriseTimeZone = Optional.ofNullable(enterprise.getTimezone()).orElse("UTC");

        // Конвертируем время из таймзоны предприятия в UTC
//        LocalDateTime startUtc = TimeZoneUtil.convertToUtc(startTime, enterpriseTimeZone);
//        LocalDateTime endUtc = TimeZoneUtil.convertToUtc(endTime, enterpriseTimeZone);

        LocalDateTime[] processedRange = processTimeRange(startTime, endTime, enterpriseTimeZone);

        // Получаем поездки
        List<Trip> trips = tripRepository.findTripsForVehicleInTimeRange
                (vehicleId, processedRange[0], processedRange[1]);

        // Конвертируем поездки в DTO
        return trips.stream()
                .map(trip -> tripMapper.convertToTripDTOWithTimeZones(trip, enterpriseTimeZone, clientTimeZone))
                .toList();
    }

}
