package ru.fisher.VehiclePark.controllers.REST;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fisher.VehiclePark.dto.EnterpriseDTO;
import ru.fisher.VehiclePark.mapper.EnterpriseMapper;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.services.EnterpriseService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enterprises")
public class EnterpriseRestController {

    private final EnterpriseService enterpriseService;

    private final ModelMapper modelMapper;

    private final EnterpriseMapper enterpriseMapper;

    @Autowired
    public EnterpriseRestController(EnterpriseService enterpriseService,
                                    ModelMapper modelMapper,
                                    EnterpriseMapper enterpriseMapper) {
        this.enterpriseService = enterpriseService;
        this.modelMapper = modelMapper;
        this.enterpriseMapper = enterpriseMapper;
    }

    private EnterpriseDTO convertToEnterpriseDTO(Enterprise enterprise) {
        return modelMapper.map(enterprise, EnterpriseDTO.class);
    }

//    @GetMapping
//    public List<Enterprise> getAllEnterprises() {
//        return enterpriseService.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public Enterprise show(@PathVariable("id") Long id) {
//        return enterpriseService.findOne(id);
//    }

    @GetMapping
    public List<EnterpriseDTO> getAllEnterprises() {
        return enterpriseService.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public EnterpriseDTO getEnterpriseById(@PathVariable Long id) {
        Enterprise enterprise = enterpriseService.findOne(id);
        return convertToDto(enterprise);
    }


    public EnterpriseDTO convertToDto(Enterprise enterprise) {
        EnterpriseDTO dto = new EnterpriseDTO();
        dto.setId(enterprise.getId());
        dto.setName(enterprise.getName());
        dto.setCity(enterprise.getCity());
        dto.setVehiclesId(enterprise.getVehicles().stream()
                .map(Vehicle::getId)
                .collect(Collectors.toList()));
        dto.setDriversId(enterprise.getDrivers().stream()
                .map(Driver::getId)
                .collect(Collectors.toList()));
        return dto;
    }

//    private EnterpriseDTO convertToDTO(Enterprise enterprise) {
//        EnterpriseDTO convertedEnterprise = modelMapper.map(enterprise, EnterpriseDTO.class);
//        List<Long> drivers = enterprise.getDrivers().stream()
//                .map(Driver::getId)
//                .collect(Collectors.toList());
//        convertedEnterprise.setDrivers(drivers);
//        List<Long> vehicles = enterprise.getVehicles().stream()
//                .map(Vehicle::getId)
//                .collect(Collectors.toList());
//        convertedEnterprise.setVehicles(vehicles);
//        return convertedEnterprise;
//    }

}
