package ru.fisher.VehiclePark.mapper;

import org.mapstruct.*;
import ru.fisher.VehiclePark.dto.BrandDTO;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.dto.VehicleUpdateDTO;
import ru.fisher.VehiclePark.models.Brand;
import ru.fisher.VehiclePark.models.Vehicle;


@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class VehicleMapper {

    public abstract Vehicle map(VehicleDTO dto);

    public abstract VehicleDTO map(Vehicle model);

    public abstract void update(VehicleUpdateDTO dto, @MappingTarget Vehicle vehicle);

    // Этот метод не нужен, если ты уже используешь JsonNullableMapper
    // @AfterMapping можно оставить, если тебе нужна дополнительная логика по Nullable
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
        // Brand теперь надо устанавливать в сервисе!
    }

}
