package ru.fisher.VehiclePark.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDTO {

    private Long id;
    private String startTime;
    private String endTime;
    private String duration; // Хранится в виде строки, например: "1 day 2 hours"

}
