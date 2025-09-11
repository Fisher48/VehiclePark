package ru.fisher.VehiclePark.repositories.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Enterprise;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByVehiclesIsNull();

    Optional<Driver> findByActiveVehicleId(Long activeVehicleId);

    @EntityGraph(attributePaths = {
            "activeVehicle",
            "activeVehicle.brand",
            "activeVehicle.enterprise"})
    List<Driver> findDriversByEnterpriseId(Long id);

    @EntityGraph(attributePaths = {
            "activeVehicle",
            "activeVehicle.brand",
            "activeVehicle.enterprise"
    })
    Page<Driver> findByEnterpriseIdIn(List<Long> enterpriseIds, Pageable pageable);

    List<Driver> findAllByEnterpriseIn(List<Enterprise> enterprises);

}
