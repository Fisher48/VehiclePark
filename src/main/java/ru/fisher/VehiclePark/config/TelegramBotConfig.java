package ru.fisher.VehiclePark.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.fisher.VehiclePark.bot.TelegramBot;

@Configuration
@Data
@Slf4j
@PropertySource("application.properties")
public class TelegramBotConfig {

    @Value("${telegram.bot.username}")
    String botName;

    @Value("${telegram.bot.token}")
    String token;

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBot bot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
        log.info("✅ Telegram Bot зарегистрирован");
        return botsApi;
    }

}
