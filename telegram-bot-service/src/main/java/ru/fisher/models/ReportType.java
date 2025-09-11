package ru.fisher.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportType {

    VEHICLE_MILEAGE("Пробег автомобиля за период", 0),

    ENTERPRISE_MILEAGE("Пробег всех авто для предприятия", 1),

    TOTAL_MILEAGE("Общий пробег предприятий менеджера", 2);

    private final String title;

    private final int value;
}
