package ru.fisher.VehiclePark.mapper;//package ru.fisher.VehiclePark.mapper;
//
//import io.r2dbc.spi.Row;
//import org.springframework.stereotype.Component;
//import ru.fisher.VehiclePark.models.Enterprise;
//
//@Component
//public class EnterpriseMapperReactive {
//
//    public static Enterprise fromRow(Row row) {
//        Enterprise enterprise = new Enterprise();
//        enterprise.setId(row.get("id", Long.class));
//        enterprise.setName(row.get("name", String.class));
//        enterprise.setCity(row.get("city", String.class));
//        enterprise.setTimezone(row.get("timezone", String.class));
//        return enterprise;
//    }
//
//}
