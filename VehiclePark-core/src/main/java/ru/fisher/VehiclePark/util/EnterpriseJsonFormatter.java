package ru.fisher.VehiclePark.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.fisher.VehiclePark.dto.ExportDTO;

@Component
public class EnterpriseJsonFormatter implements ExportFormatter<ExportDTO> {

    private final ObjectMapper objectMapper;

    public EnterpriseJsonFormatter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String getFormat() {
        return "json";
    }

    @Override
    public String format(ExportDTO data) throws Exception {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
    }
}
