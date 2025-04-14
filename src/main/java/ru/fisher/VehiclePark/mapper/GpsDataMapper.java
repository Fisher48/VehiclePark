package ru.fisher.VehiclePark.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.fisher.VehiclePark.dto.GpsDataDTO;
import ru.fisher.VehiclePark.models.GeoJSONResponse;
import ru.fisher.VehiclePark.models.GpsData;
import ru.fisher.VehiclePark.util.TimeZoneUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class GpsDataMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public GpsDataMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GpsDataDTO convertToPointGpsDTO(GpsData gpsData) {
        return modelMapper.map(gpsData, GpsDataDTO.class);
    }

    public GeoJSONResponse convertToGeoJSON(List<GpsDataDTO> gpsDataDTOList) {
        List<GeoJSONResponse.Feature> features = gpsDataDTOList.stream()
                .map(gpsDataDTO -> new GeoJSONResponse.Feature(
                        new GeoJSONResponse.Geometry(
                                List.of(gpsDataDTO.getLongitude(),
                                        gpsDataDTO.getLatitude()) // Каждая точка [longitude, latitude]
                        ),
                        new GeoJSONResponse.Properties(gpsDataDTO.getTimestamp()) // Временная метка
                ))
                .toList();

        return new GeoJSONResponse(features);
    }

    public GpsDataDTO convertToPointGpsDTO_forAPI(GpsData gpsData, String clientTimeZone, String enterpriseTimeZone) {
        GpsDataDTO gpsDataDTO = modelMapper.map(gpsData, GpsDataDTO.class);

        // Преобразование времени
        LocalDateTime time = TimeZoneUtil.
                convertTimeForClient(gpsData.getTimestamp(), enterpriseTimeZone, clientTimeZone);

        // Устанавливаем преобразованное время в DTO
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        gpsDataDTO.setTimestamp(time.format(formatter));
        return gpsDataDTO;
    }
}
