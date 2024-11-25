package ru.fisher.VehiclePark.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.models.Brand;
import ru.fisher.VehiclePark.services.BrandService;

import java.util.List;

@Controller
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public String index(Model model) {
        List<Brand> brands =  brandService.findAll();
        if (brands.isEmpty()) {
            return "redirect:/brands/new";
        }
        model.addAttribute("brands", brands);
        return "brands/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model,
                       @ModelAttribute("vehicle") Brand brand) {
        model.addAttribute("brand", brandService.findOne(id));
        return "brands/show";
    }

    @GetMapping("/new")
    public String newBrand(@ModelAttribute("brand") Brand brand) {
        return "brands/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("brand") @Valid Brand brand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "brands/new";
        }
        brandService.save(brand);
        return "redirect:/brands";
    }


    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("brand", brandService.findOne(id));
        return "brands/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("brand") @Valid Brand brand,
                       @PathVariable("id") Long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "brands/edit";
        }
        brandService.update(brand, id);
        return "redirect:/brands";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        brandService.delete(id);
        return "redirect:/brands";
    }
}
