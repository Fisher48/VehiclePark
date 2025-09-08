package ru.fisher.VehiclePark.util;//package ru.fisher.VehiclePark.util;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import net.datafaker.Faker;
//import org.springframework.shell.standard.ShellComponent;
//import org.springframework.shell.standard.ShellMethod;
//import org.springframework.shell.standard.ShellOption;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.core.scheduler.Schedulers;
//import ru.fisher.VehiclePark.models.Brand;
//import ru.fisher.VehiclePark.models.Driver;
//import ru.fisher.VehiclePark.models.Enterprise;
//import ru.fisher.VehiclePark.models.Vehicle;
//import ru.fisher.VehiclePark.services.BrandService;
//import ru.fisher.VehiclePark.services.DriverService;
//import ru.fisher.VehiclePark.services.EnterpriseService;
//import ru.fisher.VehiclePark.services.VehicleService;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Locale;
//import java.util.Random;
//import java.util.TimeZone;
//
//@Slf4j
//@ShellComponent
//@RequiredArgsConstructor
//public class ReactiveDataGeneration {
//
//    private final EnterpriseService enterpriseService;
//    private final VehicleService vehicleService;
//    private final DriverService driverService;
//    private final BrandService brandService;
//    private Random random = new Random();
//    private final Faker faker = new Faker(new Locale("ru", "Russia"));
//
//    @ShellMethod(key = "generate-data-reactive", value = "Создание предприятий с машинами и водителями (реактивно)")
//    public void generateDataReactiveShell(@ShellOption(defaultValue = "1") int enterpriseCount,
//                                          @ShellOption(defaultValue = "2") int vehiclesPerEnterprise,
//                                          @ShellOption(defaultValue = "3") int driversPerEnterprise) {
//        long start = System.currentTimeMillis();
//
//        generateDataReactive(enterpriseCount, vehiclesPerEnterprise, driversPerEnterprise)
//                .doOnSubscribe(s -> log.info("🔧 Начинаем генерацию..."))
//                .doOnNext(msg -> log.info("➡ {}", msg))
//                .doOnComplete(() -> {
//                    long time = System.currentTimeMillis() - start;
//                    log.info("✅ Генерация завершена за {} мс", time);
//                })
//                .subscribe();
//    }
//
//    public Flux<String> generateDataReactiveFlux(int enterpriseCount, int vehiclesPerEnterprise, int driversPerEnterprise) {
//        return Flux.range(0, enterpriseCount)
//                .flatMap(i -> generateEnterpriseReactive(vehiclesPerEnterprise, driversPerEnterprise))
//                .doOnSubscribe(s -> log.info("🔧 Начинаем генерацию..."))
//                .doOnNext(msg -> log.info("➡ {}", msg))
//                .doOnComplete(() -> log.info("✅ Генерация завершена"));
//    }
//
//    public Flux<String> generateDataReactive(int enterpriseCount,
//                                             int vehiclesPerEnterprise,
//                                             int driversPerEnterprise) {
//        return Flux.range(0, enterpriseCount)
//                .flatMap(i -> generateEnterpriseReactive(vehiclesPerEnterprise, driversPerEnterprise));
//    }
//
//    private Mono<String> generateEnterpriseReactive(int vehiclesPerEnterprise, int driversPerEnterprise) {
//        return Mono.fromCallable(this::generateEnterprise)
//                .subscribeOn(Schedulers.boundedElastic())
//                .flatMap(enterprise -> Mono.fromRunnable(() -> enterpriseService.save(enterprise))
//                        .subscribeOn(Schedulers.boundedElastic())
//                        .flatMap(savedEnterprise -> Flux.concat(
//                                generateVehiclesReactive((Enterprise) savedEnterprise, vehiclesPerEnterprise),
//                                generateDriversReactive((Enterprise) savedEnterprise, driversPerEnterprise)
//                        ).then(Mono.just("Создано предприятие: " + ((Enterprise) savedEnterprise).getName())))
//                );
//    }
//
//    private Flux<Void> generateVehiclesReactive(Enterprise enterprise, int count) {
//        return Flux.range(0, count)
//                .map(i -> generateVehicle(enterprise))
//                .flatMap(vehicle -> Mono.fromRunnable(() -> vehicleService.save(vehicle))
//                        .subscribeOn(Schedulers.boundedElastic()))
//                .thenMany(Flux.empty());
//    }
//
//    private Flux<Void> generateDriversReactive(Enterprise enterprise, int count) {
//        return Flux.range(0, count)
//                .map(i -> generateDriver(enterprise))
//                .flatMap(driver -> Mono.fromRunnable(() -> driverService.save(driver))
//                        .subscribeOn(Schedulers.boundedElastic()))
//                .thenMany(Flux.empty());
//    }
//
//    private Enterprise generateEnterprise() {
//        Enterprise enterprise = new Enterprise();
//        enterprise.setName(faker.company().name());
//        enterprise.setCity(faker.address().city());
//        enterprise.setTimezone(TimeZone.getDefault().getID());
//        return enterprise;
//    }
//
//    private Vehicle generateVehicle(Enterprise enterprise) {
//        Vehicle vehicle = new Vehicle();
//        vehicle.setEnterprise(enterprise);
//        vehicle.setBrand(getRandomBrandOrGenerate());
//        vehicle.setNumber(generateUniqueRegistrationNumber());
//        vehicle.setPrice(faker.number().numberBetween(250000, 3500000));
//        vehicle.setMileage(random.nextInt(200000));
//        vehicle.setPurchaseTime(LocalDateTime.now().minusYears(random.nextInt(20)+1));
//        vehicle.setYearOfCarProduction(faker.number().numberBetween(1950, 2024));
//        return vehicle;
//    }
//
//    private Driver generateDriver(Enterprise enterprise) {
//        Driver driver = new Driver();
//        driver.setEnterprise(enterprise);
//        driver.setName(faker.name().fullName());
//        driver.setSalary(50000.0 + random.nextInt(90000));
//        return driver;
//    }
//
//    private Brand getRandomBrandOrGenerate() {
//        List<Brand> brands = brandService.findAll();
//        if (brands.isEmpty()) {
//            Brand brand = new Brand();
//            brand.setNumberOfSeats(faker.random().nextInt(4, 12));
//            brand.setType(faker.vehicle().carType());
//            brand.setBrandName(faker.vehicle().model());
//            brand.setLoadCapacity(faker.random().nextInt(500, 5000));
//            brand.setFuelTank(faker.random().nextInt(45, 200));
//            brandService.save(brand);
//            return brand;
//        } else {
//            return brands.get(random.nextInt(brands.size()));
//        }
//    }
//
//    private String generateUniqueRegistrationNumber() {
//        // Реализация аналогична вашей оригинальной
//        final String LETTERS = "АВЕКМНОРСТУХ";
//        final int MAX_REGION = 190;
//        char firstChar = LETTERS.charAt(random.nextInt(LETTERS.length()));
//        char secondChar = LETTERS.charAt(random.nextInt(LETTERS.length()));
//        char thirdChar = LETTERS.charAt(random.nextInt(LETTERS.length()));
//        int number = random.nextInt(1000);
//        int region = random.nextInt(MAX_REGION) + 1;
//        return String.format("%c%03d%c%c%02d", firstChar, number, secondChar, thirdChar, region);
//    }
//
//}
