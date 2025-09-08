package ru.fisher.VehiclePark.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleCreateDTO {
    @NotNull
    private String number;

    @NotNull
    private Long brandId;

    @NotNull
    private Long enterpriseId;

    private LocalDateTime purchaseTime;
}
