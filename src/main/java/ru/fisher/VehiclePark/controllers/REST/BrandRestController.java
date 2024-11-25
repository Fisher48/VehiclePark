package ru.fisher.VehiclePark.controllers.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.models.Brand;
import ru.fisher.VehiclePark.services.BrandService;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandRestController {

    private final BrandService brandService;

    @Autowired
    public BrandRestController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Brand> index() {
        return brandService.findAll();
    }

    @GetMapping("/{id}")
    public Brand show(@PathVariable("id") Long id) {
        return brandService.findOne(id);
    }
}
