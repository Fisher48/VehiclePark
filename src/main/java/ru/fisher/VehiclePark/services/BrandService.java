package ru.fisher.VehiclePark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.models.Brand;
import ru.fisher.VehiclePark.repositories.BrandRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public Brand findOne(Long id) {
        Optional<Brand> foundBrand = brandRepository.findById(id);
        return foundBrand.orElse(null);
    }

    @Transactional
    public void save(Brand brand) {
        brandRepository.save(brand);
    }

    @Transactional
    public void update(Brand brand, Long id) {
        brand.setId(id);
        brandRepository.save(brand);
    }

    @Transactional
    public void delete(Long id) {
        brandRepository.deleteById(id);
    }


}
