package ru.fisher.VehiclePark.controllers.REST;


import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.dao.VehicleDTO;
import ru.fisher.VehiclePark.exceptions.ResourceNotFoundException;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.repositories.VehicleRepository;
import ru.fisher.VehiclePark.services.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleRestController {

    private final VehicleRepository vehicleRepository;

    private final VehicleService vehicleService;

    private final ModelMapper modelMapper;


    @Autowired
    public VehicleRestController(VehicleRepository vehicleRepository, VehicleService vehicleService, ModelMapper modelMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleService = vehicleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleDTO> index(VehicleDTO vehicleDTO) {
        return vehicleService.getAllVehicles().stream()
                .map(this::convertToVehicleDTO) // Преобразование в DTO
                .toList();
    }

    private Vehicle convertToVehicle(VehicleDTO vehicleDTO) {
        return modelMapper.map(vehicleDTO, Vehicle.class);
    }

    private VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<Vehicle> index() {
//        return vehicleRepository.findAll();
//    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VehicleDTO show(@PathVariable int id) {
        return convertToVehicleDTO(vehicleService.getVehicleById(id));
    }

//    @GetMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Vehicle show(@PathVariable int id) {
//        return vehicleRepository.findById(id).orElseThrow(
//                () -> new ResourceNotFoundException("Vehicle with " + id + " id, not exists"));
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicle create(@Valid @RequestBody Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Vehicle update(@PathVariable int id, @Valid @RequestBody Vehicle vehicle) {
        vehicleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Vehicle with " + id + " id, not exists"));
        return vehicleRepository.save(vehicle);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        vehicleRepository.deleteById(id);
    }
}
