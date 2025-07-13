package ru.fisher.VehiclePark.util;

public interface ExportFormatter<T> {
    String getFormat(); // "json", "csv", ...
    String format(T data) throws Exception;
}
