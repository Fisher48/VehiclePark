package ru.fisher.VehiclePark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fisher.VehiclePark.dto.MileageReportDTO;
import ru.fisher.VehiclePark.models.*;
import ru.fisher.VehiclePark.repositories.TripRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.fisher.VehiclePark.models.ReportType.*;

@Service
public class ReportService {

    private final TripRepository tripRepository;
    private final VehicleService vehicleService;

    @Autowired
    public ReportService(TripRepository tripRepository, VehicleService vehicleService) {
        this.tripRepository = tripRepository;
        this.vehicleService = vehicleService;
    }

    public MileageReportDTO generateMileageReport(Long vehicleId, LocalDateTime startDate,
                                           LocalDateTime endDate, Period period) {
        List<Trip> trips = tripRepository.findTripsForVehicleInTimeRange(vehicleId, startDate, endDate);
        Map<String, Double> mileageData = calculateMileage(trips, startDate, endDate, period);

        return buildReport(VEHICLE_MILEAGE, period, startDate, endDate, mileageData);
    }

    public MileageReportDTO generateEnterpriseMileageReport(Long enterpriseId, LocalDateTime startDate,
                                                     LocalDateTime endDate, Period period) {
        List<Long> vehicleIds = vehicleService.findAllByEnterpriseId(enterpriseId)
                .stream()
                .map(Vehicle::getId)
                .toList();

        Map<String, Double> mileageData = new HashMap<>();
        for (Long vehicleId : vehicleIds) {
            List<Trip> trips = tripRepository.findTripsForVehicleInTimeRange(vehicleId, startDate, endDate);
            Map<String, Double> vehicleMileage = calculateMileage(trips, startDate, endDate, period);

            vehicleMileage.forEach((key, value) -> mileageData.merge(key, value, Double::sum));
        }

        return buildReport(ENTERPRISE_MILEAGE, period, startDate, endDate, mileageData);
    }

    public MileageReportDTO generateTotalMileageReport(LocalDateTime startDate, LocalDateTime endDate, Period period) {
        List<Trip> trips = tripRepository.findTripsInTimeRange(startDate, endDate);
        Map<String, Double> mileageData = calculateMileage(trips, startDate, endDate, period);

        return buildReport(TOTAL_MILEAGE, period, startDate, endDate, mileageData);
    }

    private MileageReportDTO buildReport(ReportType title, Period period, LocalDateTime startDate,
                                  LocalDateTime endDate, Map<String, Double> results) {
        MileageReportDTO report = new MileageReportDTO();
        report.setReportType(title.getTitle());
        report.setPeriod(period.getTitle());
        report.setStartDate(startDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
        report.setEndDate(endDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
        report.setResults(results);
        return report;
    }

    private Map<String, Double> calculateMileage(List<Trip> trips, LocalDateTime startDate,
                                                 LocalDateTime endDate, Period period) {
        Map<String, Double> mileageMap = new HashMap<>();

        for (Trip trip : trips) {
            // Проверяем, что поездка попадает в указанный диапазон
            if (trip.getStartTime().isAfter(startDate) && trip.getEndTime().isBefore(endDate)) {
                // Формируем ключ для группировки
                String key = switch (period) {
                    case DAY -> trip.getStartTime().toLocalDate().toString(); // Группировка по дням
                    case MONTH -> trip.getStartTime().getYear() + "-"
                            + trip.getStartTime().getMonthValue(); // Группировка по месяцам
                    case YEAR -> String.valueOf(trip.getStartTime().getYear()); // Группировка по годам
                    default -> throw new IllegalArgumentException("Неподдерживаемый период: " + period);
                };

                // Суммируем пробеги и округляем до 2 знаков после запятой
                double currentMileage = mileageMap.getOrDefault(key, 0.0) + trip.getMileage();
                mileageMap.put(key, BigDecimal.valueOf(currentMileage).setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
        }

        // Сортируем Map по ключу
        return mileageMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey()) // Сортировка ключей
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new) // Используем LinkedHashMap для сохранения порядка
                );
    }

}
