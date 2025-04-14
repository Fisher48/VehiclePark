package ru.fisher.VehiclePark.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.dto.TripDTO;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.dto.VehicleDetailsDTO;
import ru.fisher.VehiclePark.exceptions.VehicleNotFoundException;
import ru.fisher.VehiclePark.models.*;
import ru.fisher.VehiclePark.repositories.VehicleRepository;
import ru.fisher.VehiclePark.util.TimeZoneUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final BrandService brandService;
    private final EnterpriseService enterpriseService;
    private final ModelMapper modelMapper;
    private final TripService tripService;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, BrandService brandService,
                          EnterpriseService enterpriseService, ModelMapper modelMapper, TripService tripService) {
        this.vehicleRepository = vehicleRepository;
        this.brandService = brandService;
        this.enterpriseService = enterpriseService;
        this.modelMapper = modelMapper;
        this.tripService = tripService;
    }

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Page<Vehicle> findAll(Pageable pageable) {
        return vehicleRepository.findAll(pageable);
    }

    public List<Vehicle> findAllByEnterpriseId(Long enterpriseId) {
        return vehicleRepository.findVehiclesByEnterpriseId(enterpriseId);
    }

    public Page<Vehicle> findAllByEnterprise(Enterprise enterprise, Pageable paging) {
        return vehicleRepository.findAllByEnterprise(enterprise, paging);
    }

    public Page<Vehicle> findAllForManagerByEnterpriseId(Long managerId, Long enterpriseId,
                                                         Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        return vehicleRepository.findVehiclesByEnterpriseId(enterpriseId, pageable);
    }


    public Page<Vehicle> findAllForManager(Long managerId, Integer page, Integer size) {
        List<Enterprise> enterprises = enterpriseService.findAllForManager(managerId);
        List<Vehicle> vehicles = new ArrayList<>();
        for (Enterprise enterprise : enterprises) {
            vehicles.addAll(vehicleRepository.findVehiclesByEnterpriseId(enterprise.getId()));
        }
        int pageNumber = (page != null) ? Math.max(page - 1, 0) : 0;
        Pageable pageable = PageRequest.of(pageNumber, size);
       // int pageSize = (size != null) ? size : vehicles.size(); // Установим размер страницы равным количеству результатов, если size = null
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), vehicles.size());
        return new PageImpl<>(vehicles.subList(start, end), pageable, vehicles.size());
    }

    public Optional<Vehicle> findVehicleByNumber(String number) {
        return vehicleRepository.findByNumber(number);
    }

    public Vehicle findOne(Long id) {
        Optional<Vehicle> foundVehicle = vehicleRepository.findById(id);
        return foundVehicle.orElseThrow(
                () -> new VehicleNotFoundException("Vehicle with " + id + " id, not exists"));
    }

    @Transactional
    public void save(Vehicle vehicle, Long brandId) {
        vehicle.setBrand(brandService.findOne(brandId));
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void save(Vehicle vehicle, Long brandId, Long enterpriseId) {
        vehicle.setBrand(brandService.findOne(brandId));
        vehicle.setEnterprise(enterpriseService.findOne(enterpriseId));
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void save(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void saveAll(List<Vehicle> vehicles) {
        vehicleRepository.saveAll(vehicles);
    }

    @Transactional
    public void update(Long id, Vehicle vehicle, Long updatedBrandId) {
        vehicleRepository.findById(id).orElseThrow(
                () -> new VehicleNotFoundException("Vehicle with " + id + " id, not exists"));

        vehicle.setId(id);
        vehicle.setBrand(brandService.findOne(updatedBrandId));
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void update(Long id, Vehicle updatedVehicle, Long brandId, Long enterpriseId) {
        updatedVehicle.setId(id);
        updatedVehicle.setBrand(brandService.findOne(brandId));
        updatedVehicle.setEnterprise(enterpriseService.findOne(enterpriseId));
        vehicleRepository.save(updatedVehicle);
    }

    @Transactional
    public void update(Long id, Vehicle vehicle) {
        vehicle.setId(id);
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void updateVehicle(Long vehicleId, VehicleDTO vehicleDTO, Long brandId, Long enterpriseId) {
        Vehicle existingVehicle = findOne(vehicleId);

        Vehicle updatedVehicle = modelMapper.map(vehicleDTO, Vehicle.class);
        updatedVehicle.setId(vehicleId);

        // Если дата не указана — сохраняем старую
        if (updatedVehicle.getPurchaseTime() == null) {
            updatedVehicle.setPurchaseTime(existingVehicle.getPurchaseTime());
        }

        updatedVehicle.setBrand(brandService.findOne(brandId));
        updatedVehicle.setEnterprise(enterpriseService.findOne(enterpriseId));

        vehicleRepository.save(updatedVehicle);
    }

    @Transactional
    public void delete(Long vehicleId) {
        // Удаляем все поездки, связанные с автомобилем
//        List<Trip> trips = tripService.findTripsByVehicle(vehicleId);
//        for (Trip trip : trips) {
//            tripService.delete(trip.getId());
//        }
        vehicleRepository.deleteById(vehicleId);
    }

    public List<Vehicle> findAllForManager(Long managerId) {
        List<Enterprise> enterprises = enterpriseService.findAllForManager(managerId);
        List<Vehicle> vehicles = new ArrayList<>();

        for (Enterprise enterprise : enterprises) {
            vehicles.addAll(vehicleRepository.findVehiclesByEnterpriseId(enterprise.getId()));
        }

        return vehicles;
    }

    public VehicleDTO convertToVehicleDTO(Vehicle vehicle, String clientTimeZone) {
        VehicleDTO vehicleDTO = modelMapper.map(vehicle, VehicleDTO.class);

        // Преобразуем время покупки из UTC в таймзону клиента
        // Форматируем время для отображения
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        vehicleDTO.setPurchaseTime(TimeZoneUtil.convertFromUtc
                (vehicle.getPurchaseTime(), clientTimeZone).format(formatter));

        return vehicleDTO;
    }

    public Vehicle convertToVehicle(VehicleDTO vehicleDTO) {
        return modelMapper.map(vehicleDTO, Vehicle.class);
    }

    public VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    public VehicleDetailsDTO getVehicleDetailsForDisplay(Long enterpriseId, Long vehicleId,
                                                         LocalDateTime start, LocalDateTime end,
                                                         String clientTimeZone) {
        Enterprise enterprise = enterpriseService.findOne(enterpriseId);
        Vehicle vehicle = findOne(vehicleId);

        List<Trip> trips = (start != null && end != null)
                ? tripService.findTripsForVehicleInTimeRange(vehicleId, start, end)
                : tripService.findTripsByVehicle(vehicleId);

        List<TripDTO> tripDTOs = trips.stream()
                .map(trip -> modelMapper.map(trip, TripDTO.class))
                .toList();

        LocalDateTime clientPurchaseTime = TimeZoneUtil.convertTimeForClient(
                vehicle.getPurchaseTime(),
                enterprise.getTimezone(),
                clientTimeZone
        );

        VehicleDetailsDTO vehicleDetailsDTO = new VehicleDetailsDTO();
        vehicleDetailsDTO.setVehicle(findOne(vehicleId));
        vehicleDetailsDTO.setTrips(tripDTOs);
        vehicleDetailsDTO.setEnterprise(enterprise);
        vehicleDetailsDTO.setClientPurchaseTime(clientPurchaseTime);

        return vehicleDetailsDTO;
    }

}
