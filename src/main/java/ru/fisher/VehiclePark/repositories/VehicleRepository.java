package ru.fisher.VehiclePark.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Vehicle;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

   List<Vehicle> findVehiclesByEnterpriseId(Long id);

   Optional<Vehicle> findByNumber(String vehicleName);

   List<Vehicle> findAllByEnterpriseIn(List<Enterprise> enterprises);

   Page<Vehicle> findAllByEnterprise(Enterprise enterprise, Pageable pageable);

}
