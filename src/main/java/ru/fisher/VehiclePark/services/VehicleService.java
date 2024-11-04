package ru.fisher.VehiclePark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.exceptions.ResourceNotFoundException;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.repositories.VehicleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleByNumber(String number) {
        return vehicleRepository.findByNumber(number);
    }

    public Vehicle getVehicleById(int id) {
        Optional<Vehicle> foundVehicle = vehicleRepository.findById(id);
        return foundVehicle.orElseThrow(
                () -> new ResourceNotFoundException("Vehicle with " + id + " id, not exists"));
    }

    @Transactional
    public void save(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void update(int id, Vehicle vehicle) {
        vehicle.setId(id);
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void delete(int id) {
        vehicleRepository.deleteById(id);
    }

}
