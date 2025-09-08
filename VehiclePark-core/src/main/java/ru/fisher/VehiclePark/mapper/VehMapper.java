package ru.fisher.VehiclePark.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.models.Vehicle;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class VehMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public VehMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public VehicleDTO convertToVehicleDTO(Vehicle vehicle, String clientTimeZone) {
        VehicleDTO vehicleDTO = modelMapper.map(vehicle, VehicleDTO.class);

        // Преобразуем время покупки из UTC в таймзону клиента
        LocalDateTime utcPurchaseTime = vehicle.getPurchaseTime();
        ZoneId clientZoneId = ZoneId.of(clientTimeZone);

        LocalDateTime clientPurchaseTime = utcPurchaseTime
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(clientZoneId)
                .toLocalDateTime();

        // Форматируем время для отображения
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        vehicleDTO.setPurchaseTime(clientPurchaseTime.format(formatter));

        return vehicleDTO;
    }
}
