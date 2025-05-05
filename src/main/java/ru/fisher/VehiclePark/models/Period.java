package ru.fisher.VehiclePark.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Period {

    DAY("День", 0), MONTH("Месяц", 1), YEAR("Год", 2);

    private final String title;

    private final int value;
}
