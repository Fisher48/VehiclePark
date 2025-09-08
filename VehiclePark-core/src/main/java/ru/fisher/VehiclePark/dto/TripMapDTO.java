package ru.fisher.VehiclePark.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TripMapDTO {
    TripDTO trip;
    List<double[]> coordinates;
    double[] startPoint;
    double[] endPoint;
}


