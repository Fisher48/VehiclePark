package ru.fisher.VehiclePark.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import ru.fisher.VehiclePark.models.Brand;

@Getter
@Setter
public class BrandDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Brand toBrand() {
        Brand brand = new Brand();
        brand.setId(this.id);
        return brand;
    }

}
