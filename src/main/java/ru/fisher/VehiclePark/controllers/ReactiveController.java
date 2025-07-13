package ru.fisher.VehiclePark.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.fisher.VehiclePark.dto.GpsDataDTO;
import ru.fisher.VehiclePark.dto.MileageReportDTO;
import ru.fisher.VehiclePark.dto.TripDTO;
import ru.fisher.VehiclePark.dto.VehicleDTO;
import ru.fisher.VehiclePark.mapper.GpsDataMapper;
import ru.fisher.VehiclePark.mapper.TripMapper;
import ru.fisher.VehiclePark.models.*;
import ru.fisher.VehiclePark.services.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/reactive/stream")
@RequiredArgsConstructor
public class ReactiveController {

    private final VehicleService vehicleService;
    private final GpsDataService gpsDataService;
    private final TripService tripService;
    private final ReportService reportService;
    private final AuthContextService authContextService;
    private final TripMapper tripMapper;
    private final GpsDataMapper gpsDataMapper;

    // Выдача машин в потоке (демонстрация потока)
    @GetMapping(value = "/vehicles", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<VehicleDTO> streamVehicles() {
        return Flux.defer(() -> Flux.fromIterable(vehicleService.findAll()))
                .map(vehicleService::convertToVehicleDTO)
                .delayElements(Duration.ofMillis(100)) // имитация задержки (демонстрация потока)
                .subscribeOn(Schedulers.boundedElastic());
    }

    // Выдача отчета о пробеге авто, асинхронно
    @GetMapping("/reports/vehicles/{vehicleId}")
    public Mono<MileageReportDTO> generateVehicleReport(@PathVariable String vehicleNumber) {
        Manager manager = authContextService.getCurrentManager();
        return Mono.fromCallable(() ->
                reportService.generateMileageReport(manager, vehicleNumber,
                        LocalDateTime.now().minusYears(3), LocalDateTime.now(), Period.DAY));
    }

    // Выдача точек Gps в потоке (демонстрация потока)
    @GetMapping(value = "/points/vehicles/{vehicleId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<GpsDataDTO> streamGps(@PathVariable Long vehicleId) {
        List<GpsData> gpsDataList = gpsDataService.findByVehicleId(vehicleId);
        return Flux.fromIterable(gpsDataList)
                .map(gpsDataMapper::convertToPointGpsDTO)
                .delayElements(Duration.ofMillis(500)); // имитация задержки (демонстрация потока)
    }

    // Выдача поездок в потоке (реактивно) параллельно
    @GetMapping(value = "/vehicles/{vehicleId}/trips", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TripDTO> streamTrips(@PathVariable("vehicleId") Long vehicleId) {
        return Flux.defer(() -> Flux.fromIterable(tripService.findTripsByVehicle(vehicleId)))
                .flatMap(tripMapper::convertToTripDTOReactive)
                .delayElements(Duration.ofMillis(300)) // имитация задержки (демонстрация потока)
                .subscribeOn(Schedulers.boundedElastic()); // Имитируем асинхронный стрим
    }

}
