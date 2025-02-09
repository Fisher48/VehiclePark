package ru.fisher.VehiclePark.controllers.REST;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.dto.DriverDTO;
import ru.fisher.VehiclePark.dto.EnterpriseDTO;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.services.DriverService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/drivers")
public class DriverRestController {

    private final DriverService driverService;
    private final ModelMapper modelMapper;

    @Autowired
    public DriverRestController(DriverService driverService, ModelMapper modelMapper) {
        this.driverService = driverService;
        this.modelMapper = modelMapper;
    }

    private DriverDTO convertToDriverDTO(Driver driver) {
        return modelMapper.map(driver, DriverDTO.class);
    }

//    public DriverDTO convertToDto(Driver driver) {
//        DriverDTO dto = new DriverDTO();
//        dto.setId(driver.getId());
//        dto.setName(driver.getName());
//        dto.setSalary(driver.getSalary());
//        dto.setEnterprise(dto.getEnterprise());
//        return dto;
//    }


    @GetMapping
    public List<DriverDTO> index() {
        return driverService.findAll().stream()
                .map(this::convertToDriverDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public DriverDTO show(@PathVariable("id") Long id) {
        return convertToDriverDTO(driverService.findOne(id));
    }

}
