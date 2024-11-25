package ru.fisher.VehiclePark.controllers.adminControllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Controller
@RequestMapping("/admin/vehicles")
public class AdminVehicleController {

    private final VehicleService vehicleService;

    private final DriverService driverService;

    private final BrandService brandService;

    private final VehicleValidator vehicleValidator;

    @Autowired
    public AdminVehicleController(VehicleService vehicleService, DriverService driverService, BrandService brandService,
                                  VehicleValidator vehicleValidator) {
        this.vehicleService = vehicleService;
        this.driverService = driverService;
        this.brandService = brandService;
        this.vehicleValidator = vehicleValidator;
    }

//    @GetMapping()
//    public String index(Model model) {
//        List<Vehicle> vehicles = vehicleService.findAll();
//        if (vehicles.isEmpty()) {
//            return "redirect:/admin/vehicles/new";
//        }
//        model.addAttribute("vehicles", vehicles);
//        return "admin/vehicles/index";
//    }

    @GetMapping
    public String index(@RequestParam(defaultValue = "0") Integer page,
                        @RequestParam(defaultValue = "10") Integer size,
                        Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vehicle> vehiclePage = vehicleService.findAll(pageable);
        if (vehiclePage.isEmpty()) {
            return "redirect:/admin/vehicles/new";
        }
        model.addAttribute("vehicles", vehiclePage.getContent());
        model.addAttribute("currentPage", vehiclePage.getNumber());
        model.addAttribute("totalPage", vehiclePage.getTotalPages());
        return "admin/vehicles/index";
    }


    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("vehicle", vehicleService.findOne(id));
        return "admin/vehicles/show";
    }

//    @GetMapping("/new")
//    public String newVehicle(@ModelAttribute("vehicle") Vehicle vehicle) {
//        return "vehicles/new";
//    }

    @GetMapping("/new")
    public String newVehicle(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("drivers", driverService.findAvailableDrivers());
        model.addAttribute("brands", brandService.findAll());
        return "admin/vehicles/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("vehicle") @Valid Vehicle vehicle,
                         BindingResult bindingResult, Model model, @RequestParam("brandId") Long brandId) {
        vehicleValidator.validate(vehicle, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("brands", brandService.findAll());
            return "admin/vehicles/new";
        }
        vehicleService.save(vehicle, brandId);
        return "redirect:/admin/vehicles";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("vehicle", vehicleService.findOne(id));
        model.addAttribute("brands", brandService.findAll());
        return "admin/vehicles/edit";
    }

    @PatchMapping("/{id}")
    public String update(@RequestParam(value = "brandId") Long brandId,
                         @ModelAttribute("vehicle") @Valid Vehicle vehicle,
                         @PathVariable("id") Long id, BindingResult bindingResult, Model model) {
        vehicleValidator.validate(vehicle, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("brands", brandService.findAll());
            return "admin/vehicles/edit";
        }
        vehicleService.update(id, vehicle, brandId);
        return "redirect:/admin/vehicles";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        vehicleService.delete(id);
        return "redirect:/admin/vehicles";
    }
}
