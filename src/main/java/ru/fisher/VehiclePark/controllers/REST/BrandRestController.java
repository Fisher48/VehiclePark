package ru.fisher.VehiclePark.controllers.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.fisher.VehiclePark.models.Brand;
import ru.fisher.VehiclePark.repositories.BrandRepository;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandRestController {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandRestController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Brand> index() {
        return brandRepository.findAll();
    }
}
