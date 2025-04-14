package ru.fisher.VehiclePark.controllers.adminControllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.exceptions.AccessDeniedException;
import ru.fisher.VehiclePark.models.Brand;
import ru.fisher.VehiclePark.services.BrandService;

import java.util.List;

@Controller
@RequestMapping("/admin/brands")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBrandController {

    private final BrandService brandService;

    @Autowired
    public AdminBrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public String index(Model model) {
        List<Brand> brands =  brandService.findAll();
        if (brands.isEmpty()) {
            return "redirect:/admin/brands/new";
        }
        model.addAttribute("brands", brands);
        return "admin/brands/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("brand", brandService.findOne(id));
        return "admin/brands/show";
    }

    @GetMapping("/new")
    public String newBrand(@ModelAttribute("brand") Brand brand) {
        return "admin/brands/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("brand") @Valid Brand brand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/brands/new";
        }
        brandService.save(brand);
        return "redirect:/admin/brands";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("brand", brandService.findOne(id));
        return "admin/brands/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("brand") @Valid Brand brand,
                         @PathVariable("id") Long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/brands/edit";
        }
        brandService.update(brand, id);
        return "redirect:/admin/brands";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        brandService.delete(id);
        return "redirect:/admin/brands";
    }

}
