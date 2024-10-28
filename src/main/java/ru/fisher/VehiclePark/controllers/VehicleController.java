package ru.fisher.VehiclePark.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.services.VehicleService;
import ru.fisher.VehiclePark.util.VehicleValidator;

import java.util.List;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    private final VehicleValidator vehicleValidator;

    @Autowired
    public VehicleController(VehicleService vehicleService, VehicleValidator vehicleValidator) {
        this.vehicleService = vehicleService;
        this.vehicleValidator = vehicleValidator;
    }

    @GetMapping()
    public String index(Model model) {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        if (vehicles.isEmpty()) {
            return "redirect:/vehicles/new";
        }

        model.addAttribute("vehicles", vehicles);
        return "vehicles/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model model) {
        model.addAttribute("vehicle", vehicleService.getVehicleById(id));
        return "vehicles/show";
    }

    @GetMapping("/new")
    public String newVehicle(@ModelAttribute("vehicle") Vehicle vehicle) {
        return "vehicles/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("vehicle") @Valid Vehicle vehicle,
                         BindingResult bindingResult) {
        vehicleValidator.validate(vehicle, bindingResult);

        if (bindingResult.hasErrors()) {
            return "vehicles/new";
        }
        vehicleService.save(vehicle);
        return "redirect:/vehicles";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("vehicle", vehicleService.getVehicleById(id));
        return "vehicles/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("vehicle") @Valid Vehicle vehicle,
                         @PathVariable("id") int id, BindingResult bindingResult) {
        vehicleValidator.validate(vehicle, bindingResult);

        if (bindingResult.hasErrors()) {
            return "vehicles/edit";
        }
        vehicleService.update(id, vehicle);
        return "redirect:/vehicles";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        vehicleService.delete(id);
        return "redirect:/vehicles";
    }

}
