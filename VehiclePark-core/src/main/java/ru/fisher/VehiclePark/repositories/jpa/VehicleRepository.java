package ru.fisher.VehiclePark.repositories.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Vehicle;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

   // @Query("Select v FROM Vehicle v LEFT JOIN FETCH v.trip")
   @EntityGraph(attributePaths = {"brand", "enterprise", "activeDriver"})
   List<Vehicle> findVehiclesByEnterpriseId(Long id);

   @EntityGraph(attributePaths = {"brand", "enterprise", "activeDriver", "trip"})
   List<Vehicle> findVehiclesWithTripsByEnterpriseId(@Param("enterpriseId") Long id);

   Optional<Vehicle> findByNumber(String vehicleName);

   List<Vehicle> findAllByEnterpriseIn(List<Enterprise> enterprises);

   @EntityGraph(attributePaths = {"brand", "enterprise", "activeDriver"})
   Page<Vehicle> findAllByEnterprise(Enterprise enterprise, Pageable pageable);

   @EntityGraph(attributePaths = {"brand", "enterprise", "activeDriver"})
   Page<Vehicle> findVehiclesByEnterpriseId(Long id, Pageable pageable);

   @EntityGraph(attributePaths = {"brand", "enterprise", "activeDriver"})
   Page<Vehicle> findByEnterpriseIdIn(List<Long> enterpriseIds, Pageable pageable);

}
