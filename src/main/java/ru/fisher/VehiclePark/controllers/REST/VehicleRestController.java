package ru.fisher.VehiclePark.controllers.REST;


import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fisher.VehiclePark.dto.EnterpriseDTO;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.exceptions.ResourceNotFoundException;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.repositories.VehicleRepository;
import ru.fisher.VehiclePark.services.VehicleService;
import ru.fisher.VehiclePark.util.TimeZoneUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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



//    // Получить все машины
//    @GetMapping
//    public List<VehicleDTO> getAllVehicles() {
//        List<Vehicle> vehicles = vehicleService.findAll();
//        return vehicles.stream()
//                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
//                .collect(Collectors.toList());
//    }
//
//    // Получить машину по ID
//    @GetMapping("/{id}")
//    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
//        Vehicle vehicle = vehicleService.findOne(id);
//        VehicleDTO vehicleDTO = modelMapper.map(vehicle, VehicleDTO.class);
//        return ResponseEntity.ok(vehicleDTO);
//    }
//
//    // Получить активного водителя машины
//    @GetMapping("/{id}/active-driver")
//    public ResponseEntity<Long> getActiveDriverByVehicle(@PathVariable Long id) {
//        Vehicle vehicle = vehicleService.findOne(id);
//        return ResponseEntity.ok(vehicle.getActiveDriver() != null ? vehicle.getActiveDriver().getId() : null);
//    }

    //    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<VehicleDTO> index(VehicleDTO vehicleDTO) {
//        return vehicleService.findAll().stream()
//                .map(this::convertToVehicleDTO) // Преобразование в DTO
//                .toList();
//    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<VehicleDTO>> getAllVehicles(Pageable pageable,
                                                           @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone) {
        Page<Vehicle> vehicles = vehicleService.findAll(pageable);
        Page<VehicleDTO> vehicleDTOs = vehicles.map(vehicle -> convertToVehicleDTO(vehicle, clientTimeZone));
        return ResponseEntity.ok().body(vehicleDTOs);
    }

    private Vehicle convertToVehicle(VehicleDTO vehicleDTO) {
        return modelMapper.map(vehicleDTO, Vehicle.class);
    }

//    private VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
//        return modelMapper.map(vehicle, VehicleDTO.class);
//    }

    private VehicleDTO convertToVehicleDTO(Vehicle vehicle, String clientTimeZone) {
        VehicleDTO vehicleDTO = modelMapper.map(vehicle, VehicleDTO.class);

        // Преобразование времени
        LocalDateTime utcPurchaseTime = vehicle.getPurchaseTime();
        ZoneId clientZoneId = ZoneId.of(clientTimeZone);

        // Преобразуем время из UTC в таймзону клиента
        LocalDateTime clientPurchaseTime = utcPurchaseTime
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(clientZoneId)
                .toLocalDateTime();

        // Форматируем дату для удобства чтения
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        vehicleDTO.setPurchaseTime(clientPurchaseTime.format(formatter));

        return vehicleDTO;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VehicleDTO show(@PathVariable Long id,
                           @RequestParam(required = false, defaultValue = "UTC") String clientTimeZone) {
        return convertToVehicleDTO(vehicleService.findOne(id), clientTimeZone);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicle create(@Valid @RequestBody Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Vehicle update(@PathVariable Long id, @Valid @RequestBody Vehicle vehicle) {
        vehicleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Vehicle with " + id + " id, not exists"));
        return vehicleRepository.save(vehicle);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vehicleRepository.deleteById(id);
    }
}
