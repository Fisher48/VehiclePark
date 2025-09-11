package ru.fisher.VehiclePark.controllers.adminControllers;//package ru.fisher.VehiclePark.controllers.adminControllers;
//
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import ru.fisher.VehiclePark.models.Driver;
//import ru.fisher.VehiclePark.models.Enterprise;
//import ru.fisher.VehiclePark.services.DriverService;
//import ru.fisher.VehiclePark.services.EnterpriseService;
//import ru.fisher.VehiclePark.services.VehicleService;
//
//import java.util.List;
//import java.util.TimeZone;
//
//@Controller
//@RequestMapping("/admin/enterprises")
//@PreAuthorize("hasRole('ADMIN')")
//public class AdminEnterpriseController {
//
//    private final EnterpriseService enterpriseService;
//    private final VehicleService vehicleService;
//    private final DriverService driverService;
//
//    @Autowired
//    public AdminEnterpriseController(EnterpriseService enterpriseService,
//                                     VehicleService vehicleService,
//                                     DriverService driverService) {
//        this.enterpriseService = enterpriseService;
//        this.vehicleService = vehicleService;
//        this.driverService = driverService;
//    }
//
//    @GetMapping()
//    public String index(Model model) {
//        List<Enterprise> enterprises = enterpriseService.findAll();
//        List<Driver> drivers = driverService.findAll();
//        if (enterprises.isEmpty()) {
//            return "redirect:/admin/enterprises/new";
//        }
//        model.addAttribute("drivers", drivers);
//        model.addAttribute("enterprises", enterprises);
//        return "admin/enterprises/index";
//    }
//
//    @GetMapping("/{enterpriseId}")
//    public String show(@PathVariable("enterpriseId") Long enterpriseId, Model model) {
//        model.addAttribute("enterprise", enterpriseService.findOne(enterpriseId));
//        model.addAttribute("enterpriseId", enterpriseId);
//        model.addAttribute("vehicles", vehicleService.findAllByEnterpriseId(enterpriseId));
//        // Список всех временных зон
//        model.addAttribute("timezones", TimeZone.getAvailableIDs());
//        return "admin/enterprises/show";
//    }
//
//    @GetMapping("/new")
//    public String newEnterprise(Model model) {
//        model.addAttribute("enterprise", new Enterprise());
//        // Список всех временных зон
//        model.addAttribute("timezones", TimeZone.getAvailableIDs());
//        return "admin/enterprises/new";
//    }
//
//    @PostMapping()
//    public String create(@ModelAttribute("enterprise") @Valid Enterprise enterprise,
//                         BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("drivers", driverService.findAll());
//            return "admin/enterprises/new";
//        }
//        enterpriseService.save(enterprise);
//        return "redirect:/admin/enterprises";
//    }
//
//    @GetMapping("/{enterpriseId}/edit")
//    public String edit(Model model, @PathVariable("enterpriseId") Long enterpriseId) {
//        model.addAttribute("enterprise", enterpriseService.findOne(enterpriseId));
//        // Список всех временных зон
//        model.addAttribute("timezones", TimeZone.getAvailableIDs());
//        return "admin/enterprises/edit";
//    }
//
//    @PatchMapping("/{id}")
//    public String update(@ModelAttribute("enterprise") @Valid Enterprise enterprise,
//                         @PathVariable("id") Long id, BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "/admin/enterprises/edit";
//        }
//        enterpriseService.update(id, enterprise);
//        return "redirect:/admin/enterprises";
//    }
//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") Long id) {
//        enterpriseService.delete(id);
//        return "redirect:/admin/enterprises";
//    }
//}
