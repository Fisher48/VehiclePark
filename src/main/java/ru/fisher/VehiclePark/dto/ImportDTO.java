package ru.fisher.VehiclePark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportDTO {

    private EnterpriseImportData enterprise;
    private List<VehicleDTO> vehicles;
    private List<TripImportData> trips;
}
