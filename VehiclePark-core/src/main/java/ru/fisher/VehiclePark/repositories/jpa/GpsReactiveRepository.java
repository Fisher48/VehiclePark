package ru.fisher.VehiclePark.repositories.jpa;//package ru.fisher.VehiclePark.repositories.jpa;
//
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.reactive.ReactiveCrudRepository;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;
//import ru.fisher.VehiclePark.models.GpsData;
//
//@Repository
//public interface GpsReactiveRepository extends ReactiveCrudRepository<GpsData, Long> {
//
//    @Query("SELECT * FROM gps_data WHERE vehicle_id = :vehicleId")
//    Flux<GpsData> findByVehicleId(Long vehicleId);
//}
