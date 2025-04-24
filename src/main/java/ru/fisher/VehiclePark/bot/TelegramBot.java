package ru.fisher.VehiclePark.bot;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.fisher.VehiclePark.config.TelegramBotConfig;
import ru.fisher.VehiclePark.dto.MileageReportDTO;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.models.Period;
import ru.fisher.VehiclePark.services.ManagerService;
import ru.fisher.VehiclePark.services.ReportService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotConfig botConfig;
    private final ManagerService managerService;
    private final ReportService reportService;

    // userId -> Manager
    private final Map<Long, Manager> authorizedUsers = new HashMap<>();


    @PostConstruct
    public void registerCommands() {
        try {
            List<BotCommand> commands = List.of(
                    new BotCommand("/start", "Запустить бота"),
                    new BotCommand("/help", "Показать команды"),
                    new BotCommand("/login", "Авторизация логин:пароль"),
                    new BotCommand("/logout", "Выход из системы"),
                    new BotCommand("/report", "Сформировать отчёт")
            );
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
            log.info("✅ Telegram команды успешно зарегистрированы");
        } catch (Exception e) {
            log.error("❌ Ошибка регистрации команд", e);
        }
    }

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("/start"));
        row1.add(new KeyboardButton("/help"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("/report"));
        row2.add(new KeyboardButton("/logout"));

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(List.of(row1, row2));
        markup.setResizeKeyboard(true); // подгоняет по размеру

        return markup;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String messageText = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        if (messageText.startsWith("/start")) {
            sendMessage(chatId, "Привет! Я бот системы VehiclePark. " +
                    "Введите /login логин:пароль для авторизации.", getMainMenuKeyboard());
        } else if (messageText.startsWith("/login")) {
            handleLogin(chatId, messageText);
        } else if (messageText.startsWith("/report")) {
            handleReport(chatId, messageText);
        } else if (messageText.startsWith("/help")) {
                sendMessage(chatId, """
            Доступные команды:
                /login логин:пароль — авторизация
                /logout — выйти
                /report vehicle <id> day|month|year [start] [end]
                /report enterprise <id> day|month|year [start] [end]
                /report total day|month|year [start] [end]
                Формат даты: yyyy-MM-dd или yyyy-MM-ddTHH:mm""", getMainMenuKeyboard());
        } else if (messageText.startsWith("/logout")) {
            authorizedUsers.remove(chatId);
            sendMessage(chatId, "Вы вышли из системы.", getMainMenuKeyboard());
        } else {
            sendMessage(chatId, "Такой команды нет: (" + messageText + ") "
                    + "\nДоступные команды: /help", getMainMenuKeyboard());
        }
    }

    private void handleLogin(Long chatId, String messageText) {
        try {
            String[] parts = messageText.split(" ", 2);
            if (parts.length < 2 || !parts[1].contains(":")) {
                sendMessage(chatId, "Формат: /login логин:пароль", getMainMenuKeyboard());
                return;
            }
            String[] credentials = parts[1].split(":");
            String login = credentials[0];
            String password = credentials[1];

            Manager manager = managerService.authenticate(login, password);
            authorizedUsers.put(chatId, manager);
            sendMessage(chatId, "Успешная авторизация. Добро пожаловать, " + login + "!", getMainMenuKeyboard());
        } catch (Exception e) {
            sendMessage(chatId, "Ошибка авторизации: " + e.getMessage(), getMainMenuKeyboard());
        }
    }

    private void handleReport(Long chatId, String messageText) {
        if (!authorizedUsers.containsKey(chatId)) {
            sendMessage(chatId, "Сначала выполните /login логин:пароль", getMainMenuKeyboard());
            return;
        }

        try {
            String[] parts = messageText.split(" ");
            if (parts.length < 4) {
                sendMessage(chatId, "Формат: /report " +
                        "\n 1. Тип отчета (vehicle/enterprise/total)" +
                        "\n 2. id" +
                        "\n 3. Формат отчета (DAY/MONTH/YEAR)" +
                        "\n 4. Начальная дата" +
                        "\n 5. Конечная дата",
                        getMainMenuKeyboard());
                return;
            }

            String type = parts[1]; // vehicle | enterprise | total
            Long id = type.equals("total") ? null : Long.parseLong(parts[2]);
            Period period = parsePeriod(parts[type.equals("total") ? 2 : 3]);

            // По умолчанию: сегодня или текущий месяц
            LocalDateTime start = LocalDateTime.now().minusDays(period == Period.DAY ? 1 : 30);
            LocalDateTime end = LocalDateTime.now();

            // Индексы сдвигаются в зависимости от типа
            int startIndex = type.equals("total") ? 3 : 4;

            if (parts.length > startIndex) {
                start = parseDate(parts[startIndex]);
            }
            if (parts.length > startIndex + 1) {
                end = parseDate(parts[startIndex + 1]);
            }

            Manager manager = authorizedUsers.get(chatId);

            log.info("REPORT: type={}, id={}, period={}, start={}, end={}",
                    type, id, period, start, end);

            MileageReportDTO report = switch (type) {
                case "vehicle" -> reportService.generateMileageReport(manager, id, start, end, period);
                case "enterprise" -> reportService.generateEnterpriseMileageReport(manager, id, start, end, period);
                case "total" -> reportService.generateTotalMileageReport(manager, start, end, period);
                default -> throw new IllegalArgumentException("Неизвестный тип: " + type);
            };

            sendMessage(chatId, formatReport(report), getMainMenuKeyboard());
        } catch (Exception e) {
            log.error("Ошибка генерации отчета", e);
            sendMessage(chatId, "Ошибка: " + e.getMessage(), getMainMenuKeyboard());
        }
    }

    private Period parsePeriod(String raw) {
        return switch (raw.toLowerCase()) {
            case "day" -> Period.DAY;
            case "month" -> Period.MONTH;
            case "year" -> Period.YEAR;
            default -> throw new IllegalArgumentException("Неизвестный период: " + raw);
        };
    }

    private LocalDateTime parseDate(String input) {
        try {
            return new DateTimeFormatterBuilder()
                    .appendPattern("yyyy[-MM[-dd['T'HH[:mm]]]]")
                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .toFormatter()
                    .parse(input, LocalDateTime::from);
        } catch (Exception e) {
            throw new IllegalArgumentException("Неверный формат даты: " + input);
        }
    }

    private String formatReport(MileageReportDTO report) {
        StringBuilder sb = new StringBuilder();
        sb.append("Тип отчета: ").append(report.getReportType()).append("\n");
        sb.append("Период: ").append(report.getPeriod()).append("\n");
        sb.append("С ").append(report.getStartDate()).append(" по ").append(report.getEndDate()).append("\n");
        sb.append("------\n");

        report.getResults().forEach((k, v) -> sb.append(k).append(" : ").append(v).append(" км\n"));

        return sb.toString();
    }

    private void sendMessage(Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(chatId.toString());
            message.setText(text);
            message.setReplyMarkup(keyboard);
            execute(message);
        } catch (Exception e) {
            log.error("Не удалось отправить сообщение", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

}
