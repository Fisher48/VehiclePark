package ru.fisher.VehiclePark.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.fisher.VehiclePark.dto.*;
import ru.fisher.VehiclePark.exceptions.VehicleNotFoundException;
import ru.fisher.VehiclePark.models.*;
import ru.fisher.VehiclePark.repositories.GpsDataRepository;
import ru.fisher.VehiclePark.repositories.ManagerRepository;
import ru.fisher.VehiclePark.security.ManagerDetails;
import ru.fisher.VehiclePark.util.DistanceCalculator;
import ru.fisher.VehiclePark.util.TripGenerator;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;



@Service
@Slf4j
public class ImportService {

    private final ObjectMapper objectMapper;
    private final EnterpriseService enterpriseService;
    private final VehicleService vehicleService;
    private final TripService tripService;
    private final GpsDataRepository gpsDataRepository;
    private final BrandService brandService;
    private final ManagerRepository managerRepository;
    private final TripGenerator tripGenerator;

    @Autowired
    public ImportService(ObjectMapper objectMapper, EnterpriseService enterpriseService, VehicleService vehicleService,
                         TripService tripService, GpsDataRepository gpsDataRepository, BrandService brandService,
                         ManagerRepository managerRepository, TripGenerator tripGenerator) {
        this.objectMapper = objectMapper;
        this.enterpriseService = enterpriseService;
        this.vehicleService = vehicleService;
        this.tripService = tripService;
        this.gpsDataRepository = gpsDataRepository;
        this.brandService = brandService;
        this.managerRepository = managerRepository;
        this.tripGenerator = tripGenerator;
    }


    @Transactional
    public void importEnterpriseData(MultipartFile file, String format) throws Exception {
        if ("json".equalsIgnoreCase(format)) {
            // Парсинг JSON
            ImportDTO data = objectMapper.readValue(file.getInputStream(), ImportDTO.class);
            saveImportedData(data);
        } else if ("csv".equalsIgnoreCase(format)) {
            // Парсинг CSV
            ImportDTO data = parseCsvFile(file);
            saveImportedData(data);
        } else {
            throw new IllegalArgumentException("Неподдерживаемый формат файла: " + format);
        }
    }

    // Парсинг CSV-файла
    public ImportDTO parseCsvFile(MultipartFile file) throws Exception {
        ImportDTO importData = new ImportDTO();
        List<VehicleDTO> vehicles = new ArrayList<>();
        List<TripImportData> trips = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            boolean isVehicleSection = false;
            boolean isTripSection = false;

            while ((line = reader.readNext()) != null) {
                if (line[0].equalsIgnoreCase("Enterprise ID")) {
                    // Считываем данные предприятия
                    importData.setEnterprise(new EnterpriseImportData
                            (Long.parseLong(line[1]), line[2], line[3], line[4]));
                } else if (line[0].equalsIgnoreCase("Vehicle ID")) {
                    isVehicleSection = true;
                    isTripSection = false;
                } else if (line[0].equalsIgnoreCase("Trip ID")) {
                    isVehicleSection = false;
                    isTripSection = true;
                } else if (isVehicleSection) {
                    VehicleDTO vehicle = new VehicleDTO();
                    vehicle.setId(Long.parseLong(line[0]));

                    // Обрабатываем бренд
                    BrandDTO brandDTO = new BrandDTO();
                    brandDTO.setId(Long.parseLong(line[1])); // ID бренда
                    vehicle.setBrand(brandDTO); // Устанавливаем бренд в DTO

                    vehicle.setNumber(line[2]);
                    vehicle.setPrice(Integer.parseInt((line[3])));
                    vehicle.setYearOfCarProduction(Integer.parseInt(line[4]));
                    vehicle.setMileage(Integer.parseInt((line[5])));
                    vehicles.add(vehicle);
                } else if (isTripSection) {
                    TripImportData trip = new TripImportData();
                    trip.setId(Long.parseLong(line[0]));
                    trip.setVehicleNumber(line[1]);
                    trip.setStartTime(line[2]);
                    trip.setEndTime(line[3]);
                    trip.setStartLatitude(Double.valueOf(line[4]));
                    trip.setStartLongitude(Double.valueOf(line[5]));
                    trip.setEndLatitude(Double.valueOf(line[6]));
                    trip.setEndLongitude(Double.valueOf(line[7]));
                    trip.setMileage(BigDecimal.valueOf(Long.parseLong(line[8])));
                    // trip.setDuration(line[8]);
                    trips.add(trip);
                }
            }
        }
        importData.setVehicles(vehicles);
        importData.setTrips(trips);
        return importData;
    }

    private Vehicle convertToVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = new Vehicle();
        vehicle.setPrice(vehicleDTO.getPrice());
        vehicle.setNumber(vehicleDTO.getNumber());
        vehicle.setYearOfCarProduction(vehicleDTO.getYearOfCarProduction());
        vehicle.setMileage(vehicleDTO.getMileage());
        LocalDateTime localDateTime = LocalDateTime.now();
        vehicle.setPurchaseTime(localDateTime);
        Brand brand = brandService.findOne(vehicleDTO.getBrand().getId());
        if (brand == null) {
//            log.info("Бренд с ID {} не найден. Создаем новый бренд.", vehicleDTO.getBrand().getId());
//            brand = new Brand();
//            brand.setId(vehicleDTO.getBrand().getId());
//            brand.setBrandName("BMW"); // Новый бренд
//            brandService.save(brand);
            Random rand = new Random();
            brand = brandService.findOne(rand.nextLong(brandService.findAll().size()) + 1);
            vehicle.setBrand(brand);
        }
        vehicle.setBrand(brand);
        // vehicle.setBrand(brandService.findOne(vehicleDTO.getBrand().getId()));
        // vehicle.setEnterprise(enterpriseService.findOne(vehicleDTO.).get());
        return vehicle;
    }

    public Enterprise convertToEnterprise(EnterpriseImportData enterpriseImportData) {
        Enterprise enterprise = new Enterprise();
        enterprise.setCity(enterpriseImportData.getCity());
        enterprise.setName(enterpriseImportData.getName());
        enterprise.setTimezone(enterpriseImportData.getTimezone());
        return enterprise;
    }

    public Manager getCurrentManager() {
        ManagerDetails managerDetails = (ManagerDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return managerRepository.findByUsername(managerDetails.getManager().getUsername())
                .orElseThrow(() -> new RuntimeException("Менеджер не найден"));
    }

    // Сохранение импортированных данных
    @Transactional
    private void saveImportedData(ImportDTO data) {
        log.info("Начинаем импорт данных...");

        // Получаем текущего менеджера
        Manager currentManager = getCurrentManager();

        // Сохранение предприятия
        EnterpriseImportData enterpriseData = data.getEnterprise();
        log.info("Обрабатываем предприятие: {}", enterpriseData.getName());
        Enterprise enterprise = enterpriseService.findById(enterpriseData.getId());

        if (enterprise != null) {
            // Обновляем существующее предприятие
            enterprise.setName(enterpriseData.getName());
            enterprise.setCity(enterpriseData.getCity());
            enterprise.setTimezone(enterpriseData.getTimezone());

            // Инициализация списка менеджеров, если он null
            if (enterprise.getManagers() == null) {
                enterprise.setManagers(new ArrayList<>());
            }

            // Привязываем текущего менеджера
            if (!enterprise.getManagers().contains(currentManager)) {
                enterprise.getManagers().add(currentManager);
            }

            enterpriseService.save(enterprise);
            log.info("Предприятие обновлено: {}", enterprise.getName());
        } else {
            // Создаем новое предприятие
            enterprise = convertToEnterprise(enterpriseData);

            // Инициализация списка менеджеров, если он null
            if (enterprise.getManagers() == null) {
                enterprise.setManagers(new ArrayList<>());
            }

            // Добавляем текущего менеджера
            enterprise.getManagers().add(currentManager);

            enterpriseService.save(enterprise);
            log.info("Предприятие создано: {}", enterprise.getName());
        }

        // Сохраняем автомобили
        Enterprise finalEnterprise = enterprise;
        if (data.getVehicles() != null && !data.getVehicles().isEmpty()) {
            data.getVehicles().forEach(vehicleDTO -> {
                Vehicle existingVehicle = vehicleService.findVehicleByNumber(vehicleDTO.getNumber()).orElse(null);
                if (existingVehicle != null) {
                    log.info("Автомобиль {} уже существует. Обновляем данные.", vehicleDTO.getNumber());
                    existingVehicle.setPrice(vehicleDTO.getPrice());
                    existingVehicle.setYearOfCarProduction(vehicleDTO.getYearOfCarProduction());
                    existingVehicle.setMileage(vehicleDTO.getMileage());
                    existingVehicle.setBrand(brandService.findOne(vehicleDTO.getBrand().getId()));
                    // existingVehicle.setEnterprise(enterpriseService.findOne(enterpriseData.getId()));
                    vehicleService.save(existingVehicle);
                } else {
                    Vehicle newVehicle = convertToVehicle(vehicleDTO);
                    newVehicle.setEnterprise(finalEnterprise);
                    vehicleService.save(newVehicle);
                    log.info("Автомобиль сохранен: {}, {}", newVehicle.getId(), newVehicle.getNumber());
                }
            });
        } else {
            log.info("Данные об автомобилях не переданы.");
        }

        // Сохранение поездок
        data.getTrips().forEach(tripImportData -> {
            Trip trip = new Trip();
            trip.setStartTime(LocalDateTime.parse(tripImportData.getStartTime(),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
            trip.setEndTime(LocalDateTime.parse(tripImportData.getEndTime(),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));

            // Привязываем машину
            Vehicle vehicle = vehicleService.findVehicleByNumber(tripImportData.getVehicleNumber())
                    .orElseThrow(() -> new VehicleNotFoundException
                            ("Машина с номером " + tripImportData.getVehicleNumber() + " не найдена"));
            trip.setVehicle(vehicle);

            // Генерация и сохранение точек маршрута между начальной и конечной точками
            LinkedList<GpsData> routePoints = generateRoutePoints(
                    tripImportData.getStartLatitude(),
                    tripImportData.getStartLongitude(),
                    tripImportData.getEndLatitude(),
                    tripImportData.getEndLongitude(),
                    trip, trip.getStartTime()
            );
            gpsDataRepository.saveAll(routePoints);

            BigDecimal distanceKm = DistanceCalculator.calculateDistance
                    (tripImportData.getStartLatitude(), tripImportData.getStartLongitude(),
                    tripImportData.getEndLatitude(), tripImportData.getEndLongitude());

            trip.setMileage(distanceKm);
            trip.setStartGpsData(routePoints.getFirst());
            trip.setEndGpsData(routePoints.getLast());

            // Сохраняем поездку
            tripService.save(trip);
            log.info("Поездка сохранена. ID: {}", trip.getId());
        });

    }


    private LinkedList<GpsData> generateRoutePoints(Double startLat, Double startLon,
                                              Double endLat, Double endLon,
                                              Trip trip, LocalDateTime startTime) {
        LinkedList<GpsData> routePoints = new LinkedList<>();
        String routeJson = tripGenerator.callOpenRouteService(startLat, startLon, endLat, endLon);

        try {
            JsonNode rootNode = objectMapper.readTree(routeJson);
            JsonNode coordinates = rootNode.path("features").get(0).path("geometry").path("coordinates");

            if (coordinates.isArray()) {
                GeometryFactory geometryFactory = new GeometryFactory();
                LocalDateTime timestamp = startTime;

                for (JsonNode coordinateNode : coordinates) {
                    double longitude = coordinateNode.get(0).asDouble();
                    double latitude = coordinateNode.get(1).asDouble();

                    Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
                    GpsData gpsData = new GpsData();
                    gpsData.setCoordinates(point);
                    gpsData.setVehicle(trip.getVehicle());
                    gpsData.setTimestamp(timestamp);
                    gpsData.setTrip(trip);

                    routePoints.add(gpsData);
                    timestamp = timestamp.plusSeconds(10); // Шаг 10 секунд между точками
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при генерации маршрута: {}", e.getMessage());
        }

        return routePoints;
    }


}
