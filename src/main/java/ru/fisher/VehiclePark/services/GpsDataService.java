package ru.fisher.VehiclePark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.repositories.GpsDataRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GpsDataService {

    private final GpsDataRepository gpsDataRepository;

    @Autowired
    public GpsDataService(GpsDataRepository gpsDataRepository) {
        this.gpsDataRepository = gpsDataRepository;
    }

    public List<GpsData> findAll() {
        return gpsDataRepository.findAll();
    }

    public List<GpsData> findByVehicleId(Long vehicleId) {
        return gpsDataRepository.findByVehicleId(vehicleId);
    }

    public List<GpsData> findByVehicleAndTimeRange(Long vehicleId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return gpsDataRepository.findByVehicleIdAndTimestampBetween(vehicleId, dateFrom, dateTo);
    }

//    public List<GpsData> findAllByVehicleAndTrip(Long vehicleId, List<Trip> trips) {
//        LocalDateTime dateFrom_upd = trips.get(0).getStartTime();
//        LocalDateTime dateTo_upd = trips.get(trips.size() - 1).getEndTime();
//        return gpsDataRepository.findByVehicleIdAndTimestampBetween(vehicleId, dateFrom_upd, dateTo_upd);
//    }

    @Transactional
    public void save(GpsData gpsData) {
        gpsDataRepository.save(gpsData);
    }

    @Transactional
    public void saveAll(List<GpsData> gpsDataList) {
        gpsDataRepository.saveAll(gpsDataList);
    }

}
