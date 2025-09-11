package ru.fisher.VehiclePark.dto;

import java.math.BigDecimal;

public record MileageAggregateDTO(String periodKey, BigDecimal totalMileage) {}
