package ru.fisher.VehiclePark.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Getter
@Setter
public class EnterpriseUpdateDTO {

    @NotNull
    private JsonNullable<String> name;

    @NotNull
    private JsonNullable<String> city;

    @NotNull
    private JsonNullable<List<Long>> driversId;

    @NotNull
    private JsonNullable<List<Long>> vehiclesId;

}
