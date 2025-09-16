package ru.fisher.VehiclePark.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Сущность автомобиля")
public class VehicleDTO {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotEmpty(message = "Обязательное поле")
        @Schema(description = "Гос. номер авто", example = "О727НЕ48", accessMode = Schema.AccessMode.READ_ONLY)
        private String number;

        @Min(value = 0)
        @Schema(description = "Стоимость автомобиля", example = "1500300", accessMode = Schema.AccessMode.READ_ONLY)
        private int price;

        @Schema(description = "Пробег автомобиля", example = "75000", accessMode = Schema.AccessMode.READ_ONLY)
        private int mileage;

        @Min(value = 1950, message = "Допускается добавление ТС старше 1950 г.")
        @Schema(description = "Год выпуска автомобиля", example = "2005", accessMode = Schema.AccessMode.READ_ONLY)
        private int yearOfCarProduction;

        @Schema(description = "ID бренда")
        private BrandDTO brand;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm")
        @Schema(description = "Дата покупки авто", example = "2020/01/01 12:30", accessMode = Schema.AccessMode.READ_ONLY)
        private String purchaseTime; // Время покупки в виде строки, форматированное с учётом таймзон

        // private Long activeDriverId;

}
