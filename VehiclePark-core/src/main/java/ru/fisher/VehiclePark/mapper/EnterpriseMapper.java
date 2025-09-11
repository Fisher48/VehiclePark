package ru.fisher.VehiclePark.mapper;

import org.mapstruct.*;
import ru.fisher.VehiclePark.dto.EnterpriseDTO;
import ru.fisher.VehiclePark.dto.EnterpriseUpdateDTO;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Vehicle;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class EnterpriseMapper {

//    @Mapping(target = "name", source = "name")
//    @Mapping(target = "city", source = "city")
//    @Mapping(target = "drivers", source = "driversId")
//    @Mapping(target = "vehicles", source = "vehiclesId")
    public abstract void update(EnterpriseUpdateDTO dto, @MappingTarget Enterprise enterprise);

    //@InheritInverseConfiguration
    public abstract Enterprise map(EnterpriseDTO dto);

//    @Mapping(target = "driversId", source = "drivers")
//    @Mapping(target = "vehiclesId", source = "vehicles")
    public abstract EnterpriseDTO map(Enterprise model);

    // Методы для маппинга списков
    protected List<Long> mapDriversToIds(List<Driver> drivers) {
        return drivers.stream()
                .map(Driver::getId)
                .collect(Collectors.toList());
    }

    protected List<Long> mapVehiclesToIds(List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(Vehicle::getId)
                .collect(Collectors.toList());
    }

}
