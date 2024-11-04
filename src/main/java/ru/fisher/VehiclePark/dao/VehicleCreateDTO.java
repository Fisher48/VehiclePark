package ru.fisher.VehiclePark.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleCreateDTO {

    private int id;
    private String name;
    private String number;
    private int price;
    private int mileage;
    private int yearOfCarProduction;
    private int brandId;
}
