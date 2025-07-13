package ru.fisher.VehiclePark.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.fisher.VehiclePark.dto.MileageReportDTO;
import ru.fisher.VehiclePark.exceptions.AccessDeniedException;
import ru.fisher.VehiclePark.exceptions.VehicleNotFoundException;
import ru.fisher.VehiclePark.models.*;
import ru.fisher.VehiclePark.repositories.TripRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ru.fisher.VehiclePark.models.ReportType.*;

@Service
@Slf4j
public class ReportService {

    private final TripRepository tripRepository;
    private final EnterpriseService enterpriseService;
    private final VehicleService vehicleService;

    @Autowired
    public ReportService(TripRepository tripRepository,
                         EnterpriseService enterpriseService, VehicleService vehicleService) {
        this.tripRepository = tripRepository;
        this.enterpriseService = enterpriseService;
        this.vehicleService = vehicleService;
    }

    @Cacheable(value = "mileageReports",
            key = "{#vehicleId, #startDate?.hashCode(), #endDate?.hashCode(), #period}")
    public MileageReportDTO generateMileageReport(Manager manager,
                                                  String vehicleNumber,
                                                  LocalDateTime startDate,
                                                  LocalDateTime endDate, Period period) {
        Optional<Vehicle> vehicle = Optional.ofNullable(vehicleService.findVehicleByNumber(vehicleNumber)
                .orElseThrow(() -> new VehicleNotFoundException("Машина не найдена: " + vehicleNumber)));

        Long vehicleId = vehicle.get().getId();

        if (!vehicleService.isVehicleManagedByManager(vehicleId, manager.getId())) {
            throw new AccessDeniedException("Нет доступа к этому автомобилю.");
        }
        log.info("Формирование отчета по машине id={}, период {}, с {} по {}", vehicleId, period, startDate, endDate);

        List<Trip> trips = tripRepository.findTripsForVehicleInTimeRange(vehicleId, startDate, endDate);
        log.debug("Найдено {} поездок", trips.size());

        Map<String, BigDecimal> mileageData = calculateMileage(trips, startDate, endDate, period);
        return buildReport(VEHICLE_MILEAGE, period, startDate, endDate, mileageData);
    }

    @Cacheable(value = "enterpriseMileageReports",
            key = "{#enterpriseId, #startDate?.hashCode(), #endDate?.hashCode(), #period}")
    public MileageReportDTO generateEnterpriseMileageReport(Manager manager,
                                                            Long enterpriseId,
                                                            LocalDateTime startDate,
                                                            LocalDateTime endDate, Period period) {
        if (!enterpriseService.isEnterpriseManagedByManager(enterpriseId, manager.getId())) {
            throw new AccessDeniedException("Нет доступа к этому предприятию.");
        }

        log.info("Формирование отчета по предприятию id={}, период {}, с {} по {}", enterpriseId, period, startDate, endDate);

        List<Trip> allTrips = tripRepository.findTripsByEnterpriseAndTimeRange(enterpriseId, startDate, endDate);
        Map<String, BigDecimal> mileageData = calculateMileage(allTrips, startDate, endDate, period);

        return buildReport(ENTERPRISE_MILEAGE, period, startDate, endDate, mileageData);
    }

    public MileageReportDTO generateTotalMileageReport(Manager manager,
                                                       LocalDateTime startDate,
                                                       LocalDateTime endDate, Period period) {
        List<Enterprise> enterprises = enterpriseService.findAllForManager(manager.getId());
        List<Trip> trips = tripRepository.findTripsByEnterpriseAndTimeRange(enterprises, startDate, endDate);

        Map<String, BigDecimal> mileageData = calculateMileage(trips, startDate, endDate, period);

        return buildReport(TOTAL_MILEAGE, period, startDate, endDate, mileageData);
    }

    private MileageReportDTO buildReport(ReportType title, Period period, LocalDateTime startDate,
                                  LocalDateTime endDate, Map<String, BigDecimal> results) {
        MileageReportDTO report = new MileageReportDTO();
        report.setReportType(title.getTitle());
        report.setPeriod(period.getTitle());
        report.setStartDate(startDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
        report.setEndDate(endDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
        report.setResults(results);
        return report;
    }

    private Map<String, BigDecimal> calculateMileage(List<Trip> trips, LocalDateTime startDate,
                                                 LocalDateTime endDate, Period period) {
        Map<String, BigDecimal> mileageMap = new HashMap<>();

        for (Trip trip : trips) {
            if (trip.getStartTime().isAfter(startDate) && trip.getEndTime().isBefore(endDate)) {
                String key = switch (period) {
                    case DAY -> trip.getStartTime().toLocalDate().toString();
                    case MONTH -> trip.getStartTime().getYear() + "-" +
                            String.format("%02d", trip.getStartTime().getMonthValue());
                    case YEAR -> String.valueOf(trip.getStartTime().getYear());
                    default -> throw new IllegalArgumentException("Неподдерживаемый период: " + period);
                };

                BigDecimal tripMileage = trip.getMileage() != null ? trip.getMileage() : BigDecimal.ZERO;

                mileageMap.merge(key, tripMileage, BigDecimal::add);
            }
        }

        // Округление и преобразование в Map<String, BigDecimal>
        return mileageMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, // Оставляем в BigDecimal
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

}
