package ru.fisher.VehiclePark.dto;

import lombok.Getter;
import lombok.Setter;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Vehicle;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VehicleDetailsDTO {
    Enterprise enterprise;
    Vehicle vehicle;
    List<TripDTO> trips;
    LocalDateTime clientPurchaseTime;
}
