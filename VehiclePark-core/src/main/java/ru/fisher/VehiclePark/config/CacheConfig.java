package ru.fisher.VehiclePark.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000);
    }

    @Bean
    public CaffeineCacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "enterprisesByManager",
                "vehiclesByEnterprise",
                "vehicleByNumber",
                "vehiclesForManagerByPages",
                "vehiclesForManagerByEnterprise",
                "allVehiclesForManager",
                "allDriversForManager",
                "tripsForVehicle",
                "enterpriseMileageReports",
                "mileageReports",
                "tripMaps",
                "getGpsDataByTripId",
                "getGpsDataByVehicleId"
        );
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }

//    @Bean
//    public CacheManager cacheManager() {
//        return new ConcurrentMapCacheManager(
//                "enterprisesByManager",
//                "vehiclesByEnterprise",
//                "vehicleByNumber",
//                "vehiclesForManagerByPages",
//                "vehiclesForManagerByEnterprise",
//                "allVehiclesForManager",
//                "allDriversForManager",
//                "tripsForVehicle",
//                "enterpriseMileageReports",
//                "mileageReports",
//                "tripMaps",
//                "getGpsDataByTripId"
//        );
//    }
}
