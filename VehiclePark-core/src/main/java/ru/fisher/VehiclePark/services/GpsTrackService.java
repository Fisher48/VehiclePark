package ru.fisher.VehiclePark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.fisher.VehiclePark.dto.GpsDataDTO;
import ru.fisher.VehiclePark.mapper.GpsDataMapper;
import ru.fisher.VehiclePark.models.Enterprise;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.models.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GpsTrackService {

    private final VehicleService vehicleService;
    private final EnterpriseService enterpriseService;
    private final GpsDataService gpsDataService;
    private final GpsDataMapper gpsDataMapper;

    @Autowired
    public GpsTrackService(VehicleService vehicleService,
                           EnterpriseService enterpriseService,
                           GpsDataService gpsDataService,
                           GpsDataMapper gpsDataMapper) {
        this.vehicleService = vehicleService;
        this.enterpriseService = enterpriseService;
        this.gpsDataService = gpsDataService;
        this.gpsDataMapper = gpsDataMapper;
    }

    public Object getTrackForVehicle(Long vehicleId,
                                     LocalDateTime dateFrom, LocalDateTime dateTo,
                                     String clientTimeZone, String format) {

        Vehicle vehicle = vehicleService.findOne(vehicleId);

        // Получаем предприятие, к которому принадлежит автомобиль
        Enterprise enterprise = enterpriseService.findOne(vehicle.getEnterprise().getId());

        // Получаем таймзону предприятия
        String enterpriseTimeZone = Optional.ofNullable(enterprise.getTimezone()).orElse("UTC");

        // Получаем данные GPS в UTC
        List<GpsData> gpsDataList = gpsDataService.findByVehicleAndTimeRange(
                vehicleId, dateFrom, dateTo, Sort.by(Sort.Direction.ASC, "timestamp"));

        // Преобразуем данные GPS в DTO с учетом таймзон
        List<GpsDataDTO> gpsDataDTOList = gpsDataList.stream()
                .map(gpsData -> gpsDataMapper.convertToPointGpsDTO_forAPI
                                        (gpsData, clientTimeZone, enterpriseTimeZone))
                .toList();

        // Возвращаем данные в зависимости от формата
        if ("geojson".equalsIgnoreCase(format)) {
            return gpsDataMapper.convertToGeoJSON(gpsDataDTOList);
        }

        return gpsDataDTOList;
    }

}

