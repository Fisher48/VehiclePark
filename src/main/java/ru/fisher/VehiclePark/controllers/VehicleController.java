package ru.fisher.VehiclePark.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.services.BrandService;
import ru.fisher.VehiclePark.services.DriverService;
import ru.fisher.VehiclePark.services.VehicleService;
import ru.fisher.VehiclePark.util.VehicleValidator;

import java.util.List;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    private final BrandService brandService;

    @Autowired
    public VehicleController(VehicleService vehicleService, BrandService brandService) {
        this.vehicleService = vehicleService;
        this.brandService = brandService;
    }

//    @GetMapping()
//    public String index(Model model) {
//        List<Vehicle> vehicles = vehicleService.findAll();
//        if (vehicles.isEmpty()) {
//            return "redirect:/vehicles/new";
//        }
//        model.addAttribute("vehicles", vehicles);
//        return "vehicles/index";
//    }

    @GetMapping
    public String getVehicles(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vehicle> vehiclesPage = vehicleService.findAll(pageable);
        model.addAttribute("vehicles", vehiclesPage.getContent());
        model.addAttribute("currentPage", vehiclesPage.getNumber());
        model.addAttribute("totalPages", vehiclesPage.getTotalPages());
        return "vehicles/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("vehicle", vehicleService.findOne(id));
        return "vehicles/show";
    }

    @GetMapping("/new")
    public String newVehicle(@ModelAttribute("vehicle") Vehicle vehicle,
                             Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "vehicles/new";
    }

    @PostMapping
    public String create(@RequestParam("brandId") Long brandId, Model model,
                         @ModelAttribute("vehicle") @Valid Vehicle vehicle, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("brands", brandService.findAll());
            return "vehicles/new";
        }
        vehicleService.save(vehicle, brandId);
        return "redirect:/vehicles";
    }


    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("vehicle", vehicleService.findOne(id));
        model.addAttribute("brands", brandService.findAll());
        return "vehicles/edit";
    }

    @PatchMapping("/{id}")
    public String update(@RequestParam("brandId") Long brandId, Model model,
                         @ModelAttribute("vehicle") @Valid Vehicle vehicle,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("brands", brandService.findAll());
            return "vehicles/edit";
        }
        vehicleService.update(id, vehicle, brandId);
        return "redirect:/vehicles";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        vehicleService.delete(id);
        return "redirect:/vehicles";
    }

}
