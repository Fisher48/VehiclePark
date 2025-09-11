package ru.fisher.VehiclePark.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.repositories.jpa.VehicleRepository;

import java.util.List;


@Component
@Slf4j
public class NPlusOne {

    @Autowired
    private final VehicleRepository vehicleRepository;

    public NPlusOne(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void demonstrateNPlusOneProblem(Long enterpriseId) {
        List<Vehicle> vehicles = vehicleRepository.findVehiclesByEnterpriseId(enterpriseId);
        for (Vehicle v : vehicles) {
            v.getTrip();
        }
    }

    public void demonstrateEntityGraphSolution(Long enterpriseId) {
        List<Vehicle> vehicles = vehicleRepository.findVehiclesWithTripsByEnterpriseId(enterpriseId);
        for (Vehicle v : vehicles) {
            v.getTrip();
        }
    }

}
