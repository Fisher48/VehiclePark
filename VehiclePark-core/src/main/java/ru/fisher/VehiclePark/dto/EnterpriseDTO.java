package ru.fisher.VehiclePark.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Vehicle;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class EnterpriseDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;
    private String city;
    private List<Long> driversId;
    private List<Long> vehiclesId;

}
