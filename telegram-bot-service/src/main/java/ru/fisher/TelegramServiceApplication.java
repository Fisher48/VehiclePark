package ru.fisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class TelegramServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelegramServiceApplication.class, args);
    }
}