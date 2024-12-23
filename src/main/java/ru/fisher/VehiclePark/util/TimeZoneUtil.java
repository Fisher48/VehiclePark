package ru.fisher.VehiclePark.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeZoneUtil {

//    public static LocalDateTime convertToEnterpriseTime(LocalDateTime utcTime, String enterpriseTimeZone) {
//        ZoneId enterpriseZoneId = ZoneId.of(enterpriseTimeZone != null ? enterpriseTimeZone : "UTC");
//        ZonedDateTime utcZonedTime = utcTime.atZone(ZoneId.of("UTC"));
//        return utcZonedTime.withZoneSameInstant(enterpriseZoneId).toLocalDateTime();
//    }
//
//    public static LocalDateTime convertToClientTime(LocalDateTime enterpriseTime, String clientTimeZone) {
//        ZoneId clientZoneId = ZoneId.of(clientTimeZone != null ? clientTimeZone : "UTC");
//        ZonedDateTime enterpriseZonedTime = enterpriseTime.atZone(ZoneId.systemDefault());
//        return enterpriseZonedTime.withZoneSameInstant(clientZoneId).toLocalDateTime();
//    }

    public static LocalDateTime convertTimeForClient(LocalDateTime utcTime, String enterpriseTimeZone, String clientTimeZone) {
        ZoneId enterpriseZoneId = ZoneId.of(enterpriseTimeZone != null ? enterpriseTimeZone : "UTC");
        ZoneId clientZoneId = ZoneId.of(clientTimeZone != null ? clientTimeZone : "UTC");

        // Преобразуем UTC -> Таймзона предприятия
        LocalDateTime enterpriseTime = utcTime.atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(enterpriseZoneId)
                .toLocalDateTime();

        // Если клиентская таймзона равна UTC, возвращаем время предприятия
        if (clientTimeZone.equals("UTC")) {
            return enterpriseTime;
        }

        // Преобразуем из таймзоны предприятия в таймзону клиента
        return enterpriseTime.atZone(enterpriseZoneId)
                .withZoneSameInstant(clientZoneId)
                .toLocalDateTime();
    }

    public static LocalDateTime convertToUtc(LocalDateTime localDateTime, String timeZone) {
        ZoneId zoneId = ZoneId.of(timeZone != null ? timeZone : "UTC");
        return localDateTime.atZone(zoneId).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
    }

    public static LocalDateTime convertFromUtc(LocalDateTime utcDateTime, String timeZone) {
        ZoneId zoneId = ZoneId.of(timeZone != null ? timeZone : "UTC");
        return utcDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneId).toLocalDateTime();
    }

}
