package ru.fisher.VehiclePark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fisher.VehiclePark.models.Trip;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("SELECT t FROM Trip t WHERE t.vehicle.id = :vehicleId " +
            "AND t.startTime >= :start " +
            "AND t.endTime <= :end")
    List<Trip> findTripsForVehicleInTimeRange(
            @Param("vehicleId") Long vehicleId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT t FROM Trip t WHERE t.startTime >= :startDate AND t.endTime <= :endDate")
    List<Trip> findTripsInTimeRange(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);

    List<Trip> findByVehicleId(Long vehicleId);

    @Query("SELECT t FROM Trip t " +
            "WHERE t.vehicle.enterprise.id = :enterpriseId " +
            "AND t.startTime >= :dateFrom AND t.endTime <= :dateTo")
    List<Trip> findTripsByEnterpriseAndTimeRange(@Param("enterpriseId") Long enterpriseId,
                                                 @Param("dateFrom") LocalDateTime dateFrom,
                                                 @Param("dateTo") LocalDateTime dateTo);

}
