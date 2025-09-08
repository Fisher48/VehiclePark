package ru.fisher.VehiclePark.util;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.fisher.VehiclePark.models.Brand;
import ru.fisher.VehiclePark.models.Driver;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.Vehicle;
import ru.fisher.VehiclePark.services.BrandService;
import ru.fisher.VehiclePark.services.DriverService;
import ru.fisher.VehiclePark.services.EnterpriseService;
import ru.fisher.VehiclePark.services.VehicleService;

import java.util.*;

@Slf4j
@ShellComponent
public class DataGeneration {

    private final EnterpriseService enterpriseService;
    private final VehicleService vehicleService;
    private final DriverService driverService;
    private final BrandService brandService;
    private final Faker faker = new Faker(new Locale("ru", "Russia"));
    private final Random random = new Random();

    @Autowired
    public DataGeneration(EnterpriseService enterpriseService, VehicleService vehicleService, DriverService driverService, BrandService brandService) {
        this.enterpriseService = enterpriseService;
        this.vehicleService = vehicleService;
        this.driverService = driverService;
        this.brandService = brandService;
    }

    @ShellMethod(key = "generate-data", value = "Создание предприятий с машинами и водителями")
    public String generateData(int enterpriseCount, int vehiclesPerEnterprise, int driversPerEnterprise) {
        log.info("Создание данных предприятия с машинами и водителями");

        for (int i = 0; i < enterpriseCount; i++) {
            Enterprise enterprise = generateEnterprise();
            enterpriseService.save(enterprise);

            List<Vehicle> vehicles = generateVehicles(enterprise, vehiclesPerEnterprise);
            vehicleService.saveAll(vehicles);

            List<Driver> drivers = generateDrivers(enterprise, driversPerEnterprise, vehicles);
            driverService.saveAll(drivers);
        }

        return String.format("Создано %d предприятий, в каждом - %d машин и %d водителей",
                enterpriseCount, vehiclesPerEnterprise, driversPerEnterprise);
    }

    private Enterprise generateEnterprise() {
        Enterprise enterprise = new Enterprise();
        enterprise.setName(faker.company().name());
        enterprise.setCity(faker.address().city());
        log.info("Создание предприятия: {}", enterprise.getName());
        return enterprise;
    }

    private List<Vehicle> generateVehicles(Enterprise enterprise, int count) {
        List<Vehicle> vehicles = new ArrayList<>();
        List<Brand> brands = brandService.findAll();

        // Используем Set для хранения уникальных номеров
        Set<String> registrationNumbers = new HashSet<>();

        for (int i = 0; i < count; i++) {
            Vehicle vehicle = new Vehicle();
            if (brands.isEmpty()) {
                vehicle.setBrand(generateBrand());
            } else {
                vehicle.setBrand(brands.get(random.nextInt(brands.size())));
            }

            // Генерируем уникальный номер регистрации
            String registrationNumber;
            do {
                registrationNumber = generateRegistrationNumber();
            } while (registrationNumbers.contains(registrationNumber));

            // Добавляем номер в Set
            registrationNumbers.add(registrationNumber);
            vehicle.setNumber(registrationNumber);

            vehicle.setPrice(faker.number().numberBetween(250000, 3500000));
            vehicle.setMileage(random.nextInt(200000));
            vehicle.setYearOfCarProduction(faker.number().numberBetween(1950, 2024));
            vehicle.setEnterprise(enterprise);
            vehicles.add(vehicle);
            log.info("Создана машина: {}", vehicle.getNumber());
        }
        return vehicles;
    }

    private Brand generateBrand() {
        Brand brand = new Brand();
        brand.setNumberOfSeats(faker.random().nextInt(4, 12));
        brand.setType(faker.vehicle().carType());
        brand.setBrandName(faker.vehicle().model());
        brand.setLoadCapacity(faker.random().nextInt(500, 5000));
        brand.setFuelTank(faker.random().nextInt(45, 200));
        brandService.save(brand);
        log.info("Создан бренд: {}", brand.getBrandName());
        return brand;
    }

    private String generateRegistrationNumber() {
        final String LETTERS = "АВЕКМНОРСТУХ";
        final int MAX_REGION = 190;
        char firstChar = LETTERS.charAt(random.nextInt(LETTERS.length()));
        char secondChar = LETTERS.charAt(random.nextInt(LETTERS.length()));
        char thirdChar = LETTERS.charAt(random.nextInt(LETTERS.length()));
        int number = random.nextInt(1000);
        int region = random.nextInt(MAX_REGION) + 1;
        return String.format("%c%03d%c%c%02d", firstChar, number, secondChar, thirdChar, region);
    }

    private List<Driver> generateDrivers(Enterprise enterprise, int count, List<Vehicle> vehicles) {
        List<Driver> drivers = new ArrayList<>();
        Set<Long> assignedVehicles = new HashSet<>();
        for (int i = 0; i < count; i++) {
            Driver driver = new Driver();
            driver.setName(faker.name().fullName());
            driver.setSalary(50000.0 + random.nextInt(90000));
            driver.setEnterprise(enterprise);

            log.info("Создан водитель: {}", driver.getName());

            // Привязываем активного водителя примерно к каждой 10-й машинке
//            if (random.nextInt(10) == 0 && !vehicles.isEmpty()) { // Придумать как по-другому
//                Vehicle activeVehicle = vehicles.get(random.nextInt(vehicles.size()));
//
//                driver.setActiveVehicle(activeVehicle);
//                driver.setIsActive(true);
//                activeVehicle.setActiveDriver(driver);
//                log.info("Водитель {} закреплен за машиной {}", driver.getName(), activeVehicle.getNumber());
//            }

            // Привязываем активного водителя примерно к каждой 10-й машинке
            if (random.nextInt(10) == 0 && !vehicles.isEmpty()) {
                Vehicle activeVehicle = vehicles.get(random.nextInt(vehicles.size()));
                while (assignedVehicles.contains(activeVehicle.getId())) {
                    activeVehicle = vehicles.get(random.nextInt(vehicles.size()));
                }
                driver.setActiveVehicle(activeVehicle);
                driver.setIsActive(true);
                activeVehicle.setActiveDriver(driver);
                assignedVehicles.add(activeVehicle.getId());
                log.info("Водитель {} закреплен за машиной {}", driver.getName(), activeVehicle.getNumber());
            }
            drivers.add(driver);
        }
        return drivers;
    }

}
