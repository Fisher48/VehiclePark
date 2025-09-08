package ru.fisher.VehiclePark.controllers.adminControllers;//package ru.fisher.VehiclePark.controllers.adminControllers;
//
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import ru.fisher.VehiclePark.models.Driver;
//import ru.fisher.VehiclePark.services.DriverService;
//import ru.fisher.VehiclePark.services.EnterpriseService;
//import ru.fisher.VehiclePark.services.VehicleService;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/admin/drivers")
//@PreAuthorize("hasRole('ADMIN')")
//public class AdminDriverController {
//
//    private final DriverService driverService;
//    private final VehicleService vehicleService;
//    private final EnterpriseService enterpriseService;
//
//    @Autowired
//    public AdminDriverController(DriverService driverService,
//                                 VehicleService vehicleService,
//                                 EnterpriseService enterpriseService) {
//        this.driverService = driverService;
//        this.vehicleService = vehicleService;
//        this.enterpriseService = enterpriseService;
//    }
//
//    @GetMapping()
//    public String index(Model model,
//                        @RequestParam(defaultValue = "1") int page,
//                        @RequestParam(defaultValue = "10") int size) {
//        Page<Driver> drivers = driverService.findAll(PageRequest.of(page, size));
//        if (drivers.isEmpty()) {
//            return "redirect:/admin/drivers/new";
//        }
//        model.addAttribute("drivers", drivers);
//        model.addAttribute("enterprises", enterpriseService.findAll());
//        return "admin/drivers/index";
//    }
//
//    @GetMapping("/{id}")
//    public String show(@PathVariable Long id, Model model) {
//        model.addAttribute("driver", driverService.findOne(id));
//        model.addAttribute("enterprises", enterpriseService.findAll());
//        return "admin/drivers/show";
//    }
//
//    @GetMapping("/new")
//    public String newDriver(Model model) {
//        model.addAttribute("driver", new Driver());
//        model.addAttribute("enterprises", enterpriseService.findAll());
//        model.addAttribute("vehicles", vehicleService.findAll());
//        return "admin/drivers/new";
//    }
//
//    @PostMapping()
//    public String create(@ModelAttribute("driver") @Valid Driver driver,
//                         BindingResult bindingResult, Model model) {
//        model.addAttribute("enterprises", enterpriseService.findAll());
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("vehicles", vehicleService.findAll());
//            return "admin/drivers/new";
//        }
//        driverService.save(driver);
//        return "redirect:/admin/drivers";
//    }
//
//    @GetMapping("/{id}/edit")
//    public String edit(Model model, @PathVariable("id") Long id) {
//        model.addAttribute("driver", driverService.findOne(id));
//        model.addAttribute("vehicles", vehicleService.findAll());
//        model.addAttribute("enterprises", enterpriseService.findAll());
//        return "admin/drivers/edit";
//    }
//
//    @PatchMapping("/{id}")
//    public String update(@ModelAttribute("driver") @Valid Driver driver,
//                         @PathVariable("id") Long id, BindingResult bindingResult, Model model) {
//
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("vehicles", vehicleService.findAll());
//            model.addAttribute("enterprises", enterpriseService.findAll());
//            return "admin/drivers/edit";
//        }
//        driverService.update(id, driver);
//        return "redirect:/admin/drivers";
//    }
//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") Long id) {
//        driverService.delete(id);
//        return "redirect:/admin/drivers";
//    }
//}
