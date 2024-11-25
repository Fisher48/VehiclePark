package ru.fisher.VehiclePark.mapper;

import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.repositories.DriverRepository;
import ru.fisher.VehiclePark.repositories.VehicleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ReferenceMapper {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public <T> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    public List<Driver> mapDrivers(List<Long> driverIds) {
        return driverIds.stream()
                .map(driverRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Vehicle> mapVehicles(List<Long> vehicleIds) {
        return vehicleIds.stream()
                .map(vehicleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
