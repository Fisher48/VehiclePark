package ru.fisher.VehiclePark.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fisher.VehiclePark.models.Enterprise;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {

    List<Enterprise> findEnterprisesByManagersId(Long id);

    boolean existsByIdAndManagersId(Long enterpriseId, Long managerId);

//    @EntityGraph(attributePaths = {"vehicles", "drivers"})
//    Optional<Enterprise> findById(Long id);
//
//    @Query("SELECT e FROM Enterprise e " +
//            "LEFT JOIN FETCH e.vehicles " +
//            "LEFT JOIN FETCH e.drivers " +
//            "WHERE e.id = :id")
//    Optional<Enterprise> findByIdWithVehiclesAndDrivers(@Param("id") Long id);
}
