package ru.fisher.VehiclePark.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DriverDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Обязательное поле")
    private String name;

    @NotNull(message = "Обязательное поле")
    private Double salary;

    private Boolean isActive;

    private VehicleIdDTO activeVehicle;

//  private Long enterpriseId;

//   private EnterpriseDTO enterprise;
//   private List<Long> vehicleIds;
//   private Long activeVehicleId;

}
