package ru.fisher.VehiclePark.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fisher.VehiclePark.models.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

}
