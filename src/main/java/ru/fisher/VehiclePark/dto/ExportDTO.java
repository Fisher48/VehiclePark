package ru.fisher.VehiclePark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fisher.VehiclePark.models.Enterprise;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportDTO {
    private Enterprise enterprise;
    private List<VehicleDTO> vehicles;
    private List<TripDTO> trips;
}
