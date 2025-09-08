package ru.fisher.VehiclePark.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.repositories.jpa.GpsDataRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class GpsDataService {

    private final GpsDataRepository gpsDataRepository;
//    private final GpsReactiveRepository gpsReactiveRepository;

    @Autowired
    public GpsDataService(GpsDataRepository gpsDataRepository) {
        this.gpsDataRepository = gpsDataRepository;
    }

    public List<GpsData> findAll() {
        return gpsDataRepository.findAll();
    }

    @Cacheable(value = "getGpsDataByVehicleId", key = "#vehicleId")
    public List<GpsData> findByVehicleId(Long vehicleId) {
        return gpsDataRepository.findByVehicleId(vehicleId);
    }

//    public Flux<GpsData> findByVehicleIdReactive(Long vehicleId) {
//        return gpsReactiveRepository.findByVehicleId(vehicleId);
//    }


    @Cacheable(value = "getGpsDataByTripId", key = "#tripId")
    public List<GpsData> getGpsDataByTripId(Long tripId) {
        log.info("Запрос точек по поездке tripId={}", tripId);
        List<GpsData> gpsDataList = gpsDataRepository.findByTripIdOrderByTimestampAsc(tripId);
        log.debug("Найдено {} точек GPS для tripId={}", gpsDataList.size(), tripId);
        return gpsDataList;
    }

    @Transactional(readOnly = true)
    public List<GpsData> findByVehicleAndTimeRange(Long vehicleId, LocalDateTime dateFrom, LocalDateTime dateTo, Sort sort) {
        log.info("Запрос точек для vehicleId={}, от {} до {}", vehicleId, dateFrom, dateTo);

        List<GpsData> list = gpsDataRepository.findByVehicleIdAndTimestampBetween(vehicleId, dateFrom, dateTo,
                Sort.by(Sort.Direction.ASC, "timestamp"));

        for (GpsData gps : list) {
            log.info("Точка: {}, {}, {}", gps.getId(), gps.getCoordinates().getX(), gps.getCoordinates().getY());
        }

        log.debug("Найдено {} точек GPS для vehicleId={}", list.size(), vehicleId);
        return list;
    }

    @Transactional
    public void save(GpsData gpsData) {
        gpsDataRepository.save(gpsData);
    }

    @Transactional
    public void saveAll(List<GpsData> gpsDataList) {
        gpsDataRepository.saveAll(gpsDataList);
    }



}
