package ru.fisher.VehiclePark.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MileageReportDTO {
    private String reportType;
    private String period;
    private String startDate;
    private String endDate;
    private Map<String, Double> results; // Пары "время-значение"
}
