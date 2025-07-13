package ru.fisher.VehiclePark.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Manager;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByVehiclesIsNull();

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
