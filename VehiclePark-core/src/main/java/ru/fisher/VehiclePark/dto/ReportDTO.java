package ru.fisher.VehiclePark.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class ReportDTO {
    private String reportType;
    private String period;
    private String startDate;
    private String endDate;
    private Map<String, BigDecimal> results; // Пары "время-значение"
}
