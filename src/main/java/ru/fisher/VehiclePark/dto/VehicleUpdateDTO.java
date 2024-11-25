package ru.fisher.VehiclePark.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
import ru.fisher.VehiclePark.models.Vehicle;

@Getter
@Setter
public class VehicleUpdateDTO {

    @NotNull
    private JsonNullable<String> number;

    @NotNull
    private JsonNullable<Integer> price;

    @NotNull
    private JsonNullable<Integer> mileage;

    @NotNull
    private JsonNullable<Integer> yearOfCarProduction;

    @NotNull
    private JsonNullable<BrandDTO> brand;

    // Метод для маппинга VehicleUpdateDTO в Vehicle
    public void updateVehicle(Vehicle vehicle) {
        if (number != null && number.isPresent()) {
            vehicle.setNumber(number.get());
        }
        if (price != null && price.isPresent()) {
            vehicle.setPrice(price.get());
        }
        if (mileage != null && mileage.isPresent()) {
            vehicle.setMileage(mileage.get());
        }
        if (yearOfCarProduction != null && yearOfCarProduction.isPresent()) {
            vehicle.setYearOfCarProduction(yearOfCarProduction.get());
        }
        if (brand != null && brand.isPresent()) {
            vehicle.setBrand(brand.get().toBrand());
        }
    }
}
