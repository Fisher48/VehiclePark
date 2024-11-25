package ru.fisher.VehiclePark.mapper;

import org.mapstruct.*;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.dto.VehicleUpdateDTO;
import ru.fisher.VehiclePark.models.Vehicle;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class VehicleMapper {

//    @Mapping(target = "number", source = "number")
//    @Mapping(target = "price", source = "price")
//    @Mapping(target = "mileage", source = "mileage")
//    @Mapping(target = "yearOfCarProduction", source = "yearOfCarProduction")
    public abstract void update(VehicleUpdateDTO dto, @MappingTarget Vehicle vehicle);

    public abstract Vehicle map(VehicleDTO dto);

    public abstract VehicleDTO map(Vehicle model);

    @AfterMapping
    protected void updateVehicleFromDto(@MappingTarget Vehicle vehicle, VehicleUpdateDTO dto) {
        if (dto.getNumber() != null && dto.getNumber().isPresent()) {
            vehicle.setNumber(dto.getNumber().get());
        }
        if (dto.getPrice() != null && dto.getPrice().isPresent()) {
            vehicle.setPrice(dto.getPrice().get());
        }
        if (dto.getMileage() != null && dto.getMileage().isPresent()) {
            vehicle.setMileage(dto.getMileage().get());
        }
        if (dto.getYearOfCarProduction() != null && dto.getYearOfCarProduction().isPresent()) {
            vehicle.setYearOfCarProduction(dto.getYearOfCarProduction().get());
        }
        if (dto.getBrand() != null && dto.getBrand().isPresent()) {
            vehicle.setBrand(dto.getBrand().get().toBrand());
        }
    }

}
