package ru.fisher.VehiclePark.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import ru.fisher.VehiclePark.models.Driver;

@Getter
@Setter
public class VehicleDTO {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotEmpty(message = "Обязательное поле")
        private String number;

        @Min(value = 0)
        private int price;

        private int mileage;

        @Min(value = 1950, message = "Допускается добавление ТС старше 1950 г.")
        private int yearOfCarProduction;

        private BrandDTO brand;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm")
        private String purchaseTime; // Время покупки в виде строки, форматированное с учётом таймзон

        // private Long activeDriverId;

}
