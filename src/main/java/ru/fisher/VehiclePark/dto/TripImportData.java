package ru.fisher.VehiclePark.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TripImportData {

    private Long id;
    private String vehicleNumber; // Используем номер машины вместо ID
    private String startTime;
    private String endTime;
    private Double startLatitude;
    private Double startLongitude;
    private Double endLatitude;
    private Double endLongitude;
    private BigDecimal mileage;

}
