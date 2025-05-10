package ru.fisher.VehiclePark.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.models.Trip;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GpsDataRepository extends JpaRepository<GpsData, Long> {

    // Найти все GPS-точки для конкретной машины
    List<GpsData> findByVehicleId(Long vehicleId);

    // Найти GPS-точки в заданном временном интервале
    @Query("SELECT g FROM GpsData g " +
            "WHERE g.vehicle.id = :vehicleId " +
            "AND g.timestamp BETWEEN :dateFrom AND :dateTo")
    List<GpsData> findByVehicleIdAndTimestampBetween(Long vehicleId,
                                                     LocalDateTime dateFrom,
                                                     LocalDateTime dateTo, Sort sort);

    // Получение GPS-точек трека по ID поездки
    List<GpsData> findByTripIdOrderByTimestampAsc(Long tripId);

}
