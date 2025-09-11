package ru.fisher.VehiclePark.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Trip;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("SELECT t FROM Trip t " +
            "LEFT JOIN FETCH t.startGpsData " +
            "LEFT JOIN FETCH t.endGpsData " +
            "WHERE t.vehicle.id = :vehicleId " +
            "AND t.startTime >= :start " +
            "AND t.endTime <= :end")
    List<Trip> findTripsForVehicleInTimeRange(
            @Param("vehicleId") Long vehicleId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT t " +
            "FROM Trip t " +
            "WHERE t.startTime >= :startDate AND t.endTime <= :endDate")
    List<Trip> findTripsInTimeRange(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t " +
            "FROM Trip t " +
            "LEFT JOIN FETCH t.startGpsData " +
            "LEFT JOIN FETCH t.endGpsData " +
            "WHERE t.vehicle.id = :vehicleId")
    List<Trip> findByVehicleId(Long vehicleId);

    @Query("SELECT t FROM Trip t " +
            "WHERE t.vehicle.enterprise.id = :enterpriseId " +
            "AND t.startTime >= :dateFrom AND t.endTime <= :dateTo")
    List<Trip> findTripsByEnterpriseAndTimeRange(@Param("enterpriseId") Long enterpriseId,
                                                 @Param("dateFrom") LocalDateTime dateFrom,
                                                 @Param("dateTo") LocalDateTime dateTo);

    @Query("SELECT t FROM Trip t " +
            "WHERE t.vehicle.enterprise IN :enterprises " +
            "AND t.startTime >= :startDate AND t.endTime <= :endDate")
    List<Trip> findTripsByEnterpriseAndTimeRange(@Param("enterprises") List<Enterprise> enterprises,
                                                 @Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);

    @Query("""
        SELECT t FROM Trip t
        WHERE t.vehicle.id = :vehicleId
          AND (
            (:startTime BETWEEN t.startTime AND t.endTime)
            OR (:endTime BETWEEN t.startTime AND t.endTime)
            OR (t.startTime BETWEEN :startTime AND :endTime)
            OR (t.endTime BETWEEN :startTime AND :endTime)
          )
        """)
    List<Trip> findByVehicleIdAndOverlapTimeRange(@Param("vehicleId") Long vehicleId,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);


}
