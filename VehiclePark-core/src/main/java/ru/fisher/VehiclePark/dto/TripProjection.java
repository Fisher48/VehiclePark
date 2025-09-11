package ru.fisher.VehiclePark.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TripProjection {
    Long getId();
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
    BigDecimal getDistance();
}
