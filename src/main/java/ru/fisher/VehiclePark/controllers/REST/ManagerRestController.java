package ru.fisher.VehiclePark.controllers.REST;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.fisher.VehiclePark.dto.*;
import ru.fisher.VehiclePark.mapper.EnterpriseMapper;
import ru.fisher.VehiclePark.mapper.VehicleMapper;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.security.ManagerDetails;
import ru.fisher.VehiclePark.services.DriverService;
import ru.fisher.VehiclePark.services.EnterpriseService;
import ru.fisher.VehiclePark.services.ManagerService;
import ru.fisher.VehiclePark.services.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerRestController {

    private final EnterpriseService enterpriseService;
    private final DriverService driverService;
    private final VehicleService vehicleService;
    private final ModelMapper modelMapper;
    private final VehicleMapper vehicleMapper;
    private final EnterpriseMapper enterpriseMapper;
    private final ManagerService managerService;

    @Autowired
    public ManagerRestController(EnterpriseService enterpriseService, DriverService driverService,
                                 VehicleService vehicleService, ModelMapper modelMapper, VehicleMapper vehicleMapper, EnterpriseMapper enterpriseMapper, ManagerService managerService) {
        this.enterpriseService = enterpriseService;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
        this.modelMapper = modelMapper;
        this.vehicleMapper = vehicleMapper;
        this.enterpriseMapper = enterpriseMapper;
        this.managerService = managerService;
    }

    @GetMapping
    public ModelAndView start(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ManagerDetails managerDetails = (ManagerDetails) authentication.getPrincipal();
        String username = managerDetails.getManager().getUsername();

        model.addAttribute("manager", managerService.findByUsername(username));
        return new ModelAndView("start");
    }

    public void checkManager(Long id) {
        // Получаем текущего менеджера
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ManagerDetails managerDetails = (ManagerDetails) authentication.getPrincipal();

        // Проверяем, соответствует ли id менеджера
        if (!managerDetails.getManager().getId().equals(id)) {
            throw new AccessDeniedException("Доступ запрещен");
        }
    }

    @GetMapping("/{id}/enterprises")
    @PreAuthorize("#id == authentication.principal.person.id")
    @ResponseStatus(HttpStatus.OK)
    public List<Enterprise> indexEnterprise(@PathVariable("id") Long id) {
        checkManager(id);
        return enterpriseService.findAllForManager(id);
    }

    @GetMapping("/{id}/enterprises/{enterpriseId}")
    @PreAuthorize("#id == authentication.principal.person.id")
    @ResponseStatus(HttpStatus.OK)
    public Enterprise indexOneEnterprise(@PathVariable("id") Long id,
                                         @PathVariable("enterpriseId") Long enterpriseId) {
        checkManager(id);
        return enterpriseService.findOne(enterpriseId);
    }


    @GetMapping("/{id}/vehicles")
    @PreAuthorize("#id == authentication.principal.person.id")
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleDTO> indexVehicles(@PathVariable("id") Long id) {
        checkManager(id);
        return vehicleService.findAllForManager(id).stream()
                .map(this::convertToVehicleDTO)
                .toList();
    }

    @GetMapping("/{id}/vehicles/{vehicleId}")
    @PreAuthorize("#id == authentication.principal.person.id")
    @ResponseStatus(HttpStatus.OK)
    public VehicleDTO indexOneVehicle(@PathVariable("id") Long id,
                                      @PathVariable("vehicleId") Long vehicleId) {
        checkManager(id);
        return convertToVehicleDTO(vehicleService.findOne(vehicleId));
    }

    @GetMapping("/{id}/drivers")
    @PreAuthorize("#id == authentication.principal.person.id")
    @ResponseStatus(HttpStatus.OK)
    public List<DriverDTO> indexDrivers(@PathVariable("id") Long id) {
        checkManager(id);
        return driverService.findAllForManager(id).stream()
                .map(this::convertToDriverDTO)
                .toList();
    }

    public VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    public Vehicle convertToVehicle(VehicleDTO vehicleDTO) {
        return modelMapper.map(vehicleDTO, Vehicle.class);
    }

    public DriverDTO convertToDriverDTO(Driver driver) {
        return modelMapper.map(driver, DriverDTO.class);
    }

    public Enterprise convertToEnterprise(EnterpriseDTO enterpriseDTO) {
        return modelMapper.map(enterpriseDTO, Enterprise.class);
    }

}
