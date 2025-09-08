package ru.fisher.VehiclePark;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class VehicleParkApplication {

	public static void main(String[] args) {
        // Создаем Spring приложение
        SpringApplication app = new SpringApplication(VehicleParkApplication.class);

        // Устанавливаем активные профили
        //app.setAdditionalProfiles("test"); // или "dev", "prod"

        // Запускаем
        app.run(args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}

}
