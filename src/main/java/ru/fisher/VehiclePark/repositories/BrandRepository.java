package ru.fisher.VehiclePark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fisher.VehiclePark.models.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

}
