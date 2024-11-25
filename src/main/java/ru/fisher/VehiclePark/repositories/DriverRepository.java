package ru.fisher.VehiclePark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Manager;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByVehiclesIsNull();

    List<Driver> findDriversByEnterpriseId(Long id);

    List<Driver> findAllByEnterpriseIn(List<Enterprise> enterprises);

}
