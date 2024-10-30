package ru.fisher.VehiclePark.controllers.adminControllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.services.BrandService;
import ru.fisher.VehiclePark.services.VehicleService;
import ru.fisher.VehiclePark.util.VehicleValidator;

import java.util.List;

@Controller
@RequestMapping("/admin/vehicles")
public class AdminVehicleController {


    private final VehicleService vehicleService;

    private final BrandService brandService;

    private final VehicleValidator vehicleValidator;

    @Autowired
    public AdminVehicleController(VehicleService vehicleService, BrandService brandService,
                                  VehicleValidator vehicleValidator) {
        this.vehicleService = vehicleService;
        this.brandService = brandService;
        this.vehicleValidator = vehicleValidator;
    }

    @GetMapping()
    public String index(Model model) {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        if (vehicles.isEmpty()) {
            return "redirect:/admin/vehicles/new";
        }
        model.addAttribute("vehicles", vehicles);
        return "admin/vehicles/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model model) {
        model.addAttribute("vehicle", vehicleService.getVehicleById(id));
        return "admin/vehicles/show";
    }

//    @GetMapping("/new")
//    public String newVehicle(@ModelAttribute("vehicle") Vehicle vehicle) {
//        return "vehicles/new";
//    }

    @GetMapping("/new")
    public String newVehicle(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("brands", brandService.getAllBrands());
        return "admin/vehicles/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("vehicle") @Valid Vehicle vehicle,
                         BindingResult bindingResult, Model model) {
        vehicleValidator.validate(vehicle, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("brands", brandService.getAllBrands());
            return "admin/vehicles/new";
        }
        vehicleService.save(vehicle);
        return "redirect:/admin/vehicles";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("vehicle", vehicleService.getVehicleById(id));
        model.addAttribute("brands", brandService.getAllBrands());
        return "admin/vehicles/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("vehicle") @Valid Vehicle vehicle,
                         @PathVariable("id") int id, BindingResult bindingResult, Model model) {
        vehicleValidator.validate(vehicle, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("brands", brandService.getAllBrands());
            return "admin/vehicles/edit";
        }
        vehicleService.update(id, vehicle);
        return "redirect:/admin/vehicles";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        vehicleService.delete(id);
        return "redirect:/admin/vehicles";
    }
}
