package ru.fisher.VehiclePark.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.services.DriverService;
import ru.fisher.VehiclePark.services.EnterpriseService;

import java.util.List;

@Controller
@RequestMapping("/admin/enterprises")
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    private final DriverService driverService;

    @Autowired
    public EnterpriseController(EnterpriseService enterpriseService, DriverService driverService) {
        this.enterpriseService = enterpriseService;
        this.driverService = driverService;
    }

    @GetMapping()
    public String index(Model model) {
        List<Enterprise> enterprises = enterpriseService.findAll();
        List<Driver> drivers = driverService.findAll();
        if (enterprises.isEmpty()) {
            return "redirect:/admin/enterprises/new";
        }
        model.addAttribute("drivers", drivers);
        model.addAttribute("enterprises", enterprises);
        return "admin/enterprises/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("enterprise", enterpriseService.findOne(id));
        return "admin/enterprises/show";
    }

    @GetMapping("/new")
    public String newEnterprise(Model model) {
        model.addAttribute("enterprise", new Enterprise());
        model.addAttribute("drivers", driverService.findAll());
        return "admin/enterprises/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("enterprise") @Valid Enterprise enterprise,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("drivers", driverService.findAll());
            return "admin/enterprises/new";
        }
        enterpriseService.save(enterprise);
        return "redirect:/admin/enterprises";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("enterprise", enterpriseService.findOne(id));
        model.addAttribute("drivers", driverService.findAll());
        return "admin/enterprises/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("enterprise") @Valid Enterprise enterprise,
                         @PathVariable("id") Long id, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("drivers", driverService.findAll());
            return "/admin/enterprises/edit";
        }
        enterpriseService.update(id, enterprise);
        return "redirect:/admin/enterprises";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        enterpriseService.delete(id);
        return "redirect:/admin/enterprises";
    }
}
