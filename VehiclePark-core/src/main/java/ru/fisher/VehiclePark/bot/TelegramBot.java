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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.fisher.VehiclePark.config.TelegramBotConfig;
import ru.fisher.VehiclePark.dto.MileageReportDTO;
import ru.fisher.VehiclePark.exceptions.VehicleNotFoundException;
import ru.fisher.VehiclePark.models.*;
import ru.fisher.VehiclePark.services.EnterpriseService;
import ru.fisher.VehiclePark.services.ManagerService;
import ru.fisher.VehiclePark.services.ReportService;
import ru.fisher.VehiclePark.services.VehicleService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.fisher.VehiclePark.models.ReportType.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotConfig botConfig;
    private final ManagerService managerService;
    private final ReportService reportService;
    private final EnterpriseService enterpriseService;
    private final VehicleService vehicleService;

    // userId -> Manager
    private final Map<Long, Manager> authorizedUsers = new HashMap<>();

    // Чат -> контекст запроса
    private final Map<Long, ReportRequestContext> sessionContext = new HashMap<>();

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
        } catch (Exception e) {
            log.error("Ошибка регистрации команд", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update.getMessage().getChatId(), update.getMessage().getText());
        } else if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery().getMessage().getChatId(),
                    update.getCallbackQuery().getData());
        }
    }

    private void handleMessage(Long chatId, String text) {
        if (text.startsWith("/start")) {
            handleStart(chatId);
        } else if (text.startsWith("/login")) {
            handleLogin(chatId, text);
        } else if (text.equals("/logout")) {
            authorizedUsers.remove(chatId);
            sessionContext.remove(chatId);
            sendMessage(chatId, "Вы вышли из системы.");
        } else if (text.equals("/help")) {
            sendHelp(chatId);
        } else if (text.equals("/report")) {
            if (managerService.getManagerByChatId(chatId).isEmpty()) {
                sendMessage(chatId, "Сначала авторизуйтесь через /login логин:пароль");
                return;
            }
            sessionContext.put(chatId, new ReportRequestContext());
            sendReportTypeSelection(chatId);
        } else {
            handleStep(chatId, text);
        }
    }

    private void handleStart(Long chatId) {
        sendMessage(chatId, "🚗 Добро пожаловать в VehiclePark Bot!\n\n" +
                "Для работы с системой используйте команды:\n" +
                "/login - для авторизация\n" +
                "/help - список команд");
    }

    private void sendHelp(Long chatId) {
        String helpText = """
            📋 Доступные команды:

            /login логин:пароль - авторизация
            /logout - выход из системы
            /report - сформировать отчет

            📊 Формирование отчетов:
            - По машине (введите гос. номер авто)
            - По предприятию (выберите из списка)
            - Общий отчет
            """;
        sendMessage(chatId, helpText);
    }

    private void sendReportTypeSelection(Long chatId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(
                List.of(
                        InlineKeyboardButton.builder().text("🚗 По машине").callbackData("report_vehicle").build(),
                        InlineKeyboardButton.builder().text("\uD83C\uDFE2 По предприятию").callbackData("report_enterprise").build(),
                        InlineKeyboardButton.builder().text("\uD83D\uDCCA Общий").callbackData("report_total").build()
                )
        ));
        sendMessage(chatId, "Выберите тип отчета:", markup);
    }

    private void handleLogin(Long chatId, String messageText) {
        try {
            String[] parts = messageText.split(" ", 2);
            if (parts.length < 2) {
                sendMessage(chatId, "Используйте формат: /login логин:пароль");
                return;
            }
            String[] creds = parts[1].split(":");
            if (creds.length != 2) {
                sendMessage(chatId, "Используйте формат: логин:пароль");
                return;
            }
            String username = creds[0];
            String password = creds[1];

            // Аутентификация
            Manager manager = managerService.authenticate(username, password);

            // ✅ Обновляем chatId для менеджера
            managerService.updateManagerChatId(manager.getId(), chatId);

            // ✅ Обновляем объект менеджера
            manager.setChatId(chatId);

            // ✅ Добавляем в сессию
            authorizedUsers.put(chatId, manager);

            sendMessage(chatId, manager.getUsername() + ", вы успешно авторизовались! ✅");
            log.info("Менеджер {} авторизован, chatId сохранён: {}", username, chatId);

        } catch (Exception e) {
            sendMessage(chatId, "❌ Ошибка авторизации: " + e.getMessage());
            log.error("Ошибка авторизации", e);
        }
    }

    private void handleStep(Long chatId, String text) {
        ReportRequestContext ctx = sessionContext.get(chatId);

        if (managerService.getManagerByChatId(chatId).isEmpty()) {
            sendMessage(chatId, "Сначала авторизуйтесь /login");
            return;
        }

        if (ctx == null && managerService.getManagerByChatId(chatId).isPresent()) {
            sendMessage(chatId, "Команды в боте /help");
            return;
        }

        if (ctx == null) {
            sendMessage(chatId, "Сначала выполните /report");
            return;
        }

        try {
            switch (ctx.getState()) {
                case VEHICLE_WAITING_NUMBER -> {
                    ctx.setVehicleNumber(text);
                    ctx.setState(BotState.PERIOD_SELECTION);
                    sendPeriodSelection(chatId);
                }
                case ENTERPRISE_WAITING_NAME -> {
                    ctx.setEnterpriseName(text);
                    ctx.setState(BotState.PERIOD_SELECTION);
                    sendPeriodSelection(chatId);
                }
                case PERIOD_SELECTION -> {
                    ctx.setPeriod(parsePeriod(text));
                    ctx.setState(BotState.WAITING_START_DATE);
                    sendMessage(chatId, "Введите начальную дату (формат yyyy-MM-dd или yyyy-MM-ddTHH:mm):");
                }
                case WAITING_START_DATE -> {
                    ctx.setStartDate(parseDate(text));
                    ctx.setState(BotState.WAITING_END_DATE);
                    sendMessage(chatId, "Введите конечную дату:");
                }
                case WAITING_END_DATE -> {
                    ctx.setEndDate(parseDate(text));
                    generateAndSendReport(chatId, ctx);
                    sessionContext.remove(chatId);
                }
                default -> sendMessage(chatId, "⚠ Неожиданное состояние. Введите /report заново.");
            }
        } catch (Exception e) {
            sendMessage(chatId, "❌ Ошибка: " + e.getMessage());
        }
    }

    private void sendPeriodSelection(Long chatId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(
                List.of(
                        InlineKeyboardButton.builder().text("День").callbackData("period_day").build(),
                        InlineKeyboardButton.builder().text("Месяц").callbackData("period_month").build(),
                        InlineKeyboardButton.builder().text("Год").callbackData("period_year").build()
                )
        ));
        sendMessage(chatId, "Выберите период отчета:", markup);
    }

    private void handleCallback(Long chatId, String data) {
        if (managerService.getManagerByChatId(chatId).isEmpty()) {
            sendMessage(chatId, "Сначала авторизуйтесь через /login логин:пароль");
            return;
        }

        ReportRequestContext ctx = sessionContext.computeIfAbsent(chatId, k -> new ReportRequestContext());

        if (data.startsWith("enterprise_")) {
            try {
                Long enterpriseId = Long.parseLong(data.substring("enterprise_".length()));
                Enterprise enterprise = enterpriseService.findById(enterpriseId);
                ctx.setEnterpriseName(enterprise.getName());
                ctx.setState(BotState.PERIOD_SELECTION);
                sendPeriodSelection(chatId);
            } catch (Exception e) {
                sendMessage(chatId, "❌ Ошибка выбора предприятия: " + e.getMessage());
            }
        } else if (data.startsWith("period_")) {
            try {
                String period = data.substring("period_".length());
                ctx.setPeriod(parsePeriod(period));
                ctx.setState(BotState.WAITING_START_DATE);
                sendMessage(chatId, "Введите начальную дату (формат yyyy-MM-dd или yyyy-MM-ddTHH:mm):");
            } catch (Exception e) {
                sendMessage(chatId, "❌ Ошибка выбора периода: " + e.getMessage());
            }
        }
        else {
            switch (data) {
                case "report_vehicle" -> {
                    ctx.setType(VEHICLE_MILEAGE);
                    ctx.setState(BotState.VEHICLE_WAITING_NUMBER);
                    sendMessage(chatId, "Введите номер машины:");
                }
                case "report_enterprise" -> {
                    ctx.setType(ENTERPRISE_MILEAGE);
                    ctx.setState(BotState.ENTERPRISE_WAITING_NAME);
                    sendEnterpriseSelection(chatId);
                }
                case "report_total" -> {
                    ctx.setType(TOTAL_MILEAGE);
                    ctx.setState(BotState.PERIOD_SELECTION);
                    sendPeriodSelection(chatId);
                }
                default -> sendMessage(chatId, "Неизвестный выбор: " + data);
            }
        }
    }

    private void sendEnterpriseSelection(Long chatId) {
        Manager manager = authorizedUsers.get(chatId);
        List<Enterprise> enterprises = enterpriseService.findAllForManager(manager.getId());

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = enterprises.stream()
                .map(e -> List.of(
                        InlineKeyboardButton.builder().text(e.getName())
                                .callbackData("enterprise_" + e.getId()).build()))
                .toList();
        markup.setKeyboard(rows);
        sendMessage(chatId, "Выберите предприятие:", markup);
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

    private void generateAndSendReport(Long chatId, ReportRequestContext ctx) {
        try {
            Manager manager = authorizedUsers.get(chatId);
            MileageReportDTO reportDTO;

            switch (ctx.getType()) {
                case VEHICLE_MILEAGE -> {
                    Vehicle vehicle = vehicleService.findVehicleByNumber(ctx.getVehicleNumber())
                            .orElseThrow(() -> new VehicleNotFoundException(
                                    "Машина с " + ctx.getVehicleNumber() + " номером, не существует"));
                    reportDTO = reportService.generateMileageReport(
                            manager, vehicle.getNumber(), ctx.getStartDate(), ctx.getEndDate(), ctx.getPeriod());
                }
                case ENTERPRISE_MILEAGE -> {
                    Enterprise enterprise = enterpriseService.findByName(ctx.getEnterpriseName())
                            .orElseThrow(() -> new IllegalArgumentException("Предприятие не найдено"));
                    reportDTO = reportService.generateEnterpriseMileageReport(
                            manager, enterprise.getId(), ctx.getStartDate(), ctx.getEndDate(), ctx.getPeriod());
                }
                case TOTAL_MILEAGE -> {
                    reportDTO = reportService.generateTotalMileageReport(
                            manager, ctx.getStartDate(), ctx.getEndDate(), ctx.getPeriod());
                }
                default -> throw new IllegalArgumentException("Неизвестный тип отчета");
            }
            sendMessage(chatId, formatReport(reportDTO));
        } catch (Exception e) {
            sendMessage(chatId, "❌ Ошибка при генерации отчета: " + e.getMessage());
            log.error("Ошибка генерации отчета", e);
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

    private String formatReport(MileageReportDTO report) {
        StringBuilder sb = new StringBuilder();
        sb.append("📊 ").append(report.getReportType()).append("\n");
        sb.append("⏱ Период: ").append(report.getPeriod()).append("\n");
        sb.append("🔄 С ").append(report.getStartDate()).append(" по ")
                .append(report.getEndDate()).append("\n\n");

        report.getResults().forEach((key, value) ->
                        sb.append(key).append(": ").append(value).append(" км\n"));
        return sb.toString();
    }

    private void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, null);
    }

    private void sendMessage(Long chatId, String text, InlineKeyboardMarkup markup) {
        try {
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId.toString());
            msg.setText(text);
            if (markup != null) msg.setReplyMarkup(markup);
            execute(msg);
        } catch (Exception e) {
            log.error("Ошибка отправки сообщения", e);
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
