package ru.fisher.VehiclePark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.models.Trip;
import ru.fisher.VehiclePark.repositories.TripRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public List<Trip> findTripsForVehicleInTimeRange(Long vehicleId, LocalDateTime start, LocalDateTime end) {
        return tripRepository.findTripsForVehicleInTimeRange(vehicleId, start, end);
    }

    @Transactional
    public void save(Trip trip) {
        tripRepository.save(trip);
    }
}
