package ru.fisher.VehiclePark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.fisher.VehiclePark.models.GpsData;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface GpsDataRepository extends JpaRepository<GpsData, Long> {

    // Найти все GPS-точки для конкретной машины
    List<GpsData> findByVehicleId(Long vehicleId);

    // Найти GPS-точки в заданном временном интервале
    @Query("SELECT g FROM GpsData g " +
            "WHERE g.vehicle.id = :vehicleId " +
            "AND g.timestamp BETWEEN :dateFrom AND :dateTo")
    List<GpsData> findByVehicleIdAndTimestampBetween(Long vehicleId, LocalDateTime dateFrom, LocalDateTime dateTo);

}
