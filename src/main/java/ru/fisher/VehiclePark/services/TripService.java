package ru.fisher.VehiclePark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.exceptions.ResourceNotFoundException;
import ru.fisher.VehiclePark.models.Trip;
import ru.fisher.VehiclePark.repositories.TripRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    private final TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip findOne(Long id) {
        Optional<Trip> foundTrip = tripRepository.findById(id);
        return foundTrip.orElseThrow(
                () -> new ResourceNotFoundException("Trip with " + id + " id, not exists"));
    }

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
    public void saveAll(List<Trip> trips) {
        tripRepository.saveAll(trips);
    }
}
