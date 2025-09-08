package ru.fisher.VehiclePark.bot;//package ru.fisher.VehiclePark.bot;
//
//import jakarta.annotation.PostConstruct;
//import lombok.*;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
//import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
//import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
//import ru.fisher.VehiclePark.config.TelegramBotConfig;
//import ru.fisher.VehiclePark.dto.MileageReportDTO;
//import ru.fisher.VehiclePark.models.*;
//import ru.fisher.VehiclePark.services.*;
//
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.*;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class TelegramBotVer2 extends TelegramLongPollingBot {
//
//    private final TelegramBotConfig botConfig;
//    private final ManagerService managerService;
//    private final ReportService reportService;
//    private final VehicleService vehicleService;
//    private final EnterpriseService enterpriseService;
//
//    private final Map<Long, Manager> authorizedUsers = new HashMap<>();
//    private final Map<Long, String> currentCommand = new HashMap<>();
//
//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasCallbackQuery()) {
//            handleCallback(update.getCallbackQuery());
//            return;
//        }
//
//        if (!update.hasMessage() || !update.getMessage().hasText()) return;
//
//        String text = update.getMessage().getText();
//        Long chatId = update.getMessage().getChatId();
//
//        try {
//            if (text.startsWith("/start")) {
//                sendMessage(chatId, "Добро пожаловать! Используйте /login для авторизации");
//            }
//            else if (text.startsWith("/login")) {
//                handleLogin(chatId, text);
//            }
//            else if (text.startsWith("/report")) {
//                handleReportCommand(chatId, text);
//            }
//            else if (currentCommand.containsKey(chatId)) {
//                handleUserInput(chatId, text);
//            }
//        } catch (Exception e) {
//            sendMessage(chatId, "Ошибка: " + e.getMessage());
//        }
//    }
//
//    private void handleLogin(Long chatId, String text) {
//        String[] parts = text.split(" ");
//        if (parts.length < 3) {
//            sendMessage(chatId, "Используйте: /login логин пароль");
//            return;
//        }
//
//        Manager manager = managerService.authenticate(parts[1], parts[2]);
//        authorizedUsers.put(chatId, manager);
//        sendMessage(chatId, "Авторизация успешна!");
//    }
//
//    private void handleReportCommand(Long chatId, String text) {
//        if (!authorizedUsers.containsKey(chatId)) {
//            sendMessage(chatId, "Сначала авторизуйтесь /login");
//            return;
//        }
//
//        String[] parts = text.split(" ");
//        if (parts.length < 2) {
//            sendMessage(chatId, "Используйте: /report vehicle или /report enterprise");
//            return;
//        }
//
//        if (parts[1].equalsIgnoreCase("vehicle")) {
//            currentCommand.put(chatId, "AWAIT_VEHICLE_NUMBER");
//            sendMessage(chatId, "Введите номер машины:");
//        }
//        else if (parts[1].equalsIgnoreCase("enterprise")) {
//            showEnterpriseList(chatId);
//        }
//    }
//
//    private void showEnterpriseList(Long chatId) {
//        Manager manager = authorizedUsers.get(chatId);
//        List<Enterprise> enterprises = enterpriseService.findAllForManager(manager.getId());
//
//        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
//
//        for (Enterprise e : enterprises) {
//            rows.add(List.of(
//                    InlineKeyboardButton.builder()
//                            .text(e.getName())
//                            .callbackData("ENT_" + e.getId())
//                            .build()
//            ));
//        }
//
//        keyboard.setKeyboard(rows);
//
//        SendMessage message = SendMessage.builder()
//                .chatId(chatId.toString())
//                .text("Выберите предприятие:")
//                .replyMarkup(keyboard)
//                .build();
//
//        try {
//            execute(message);
//        } catch (Exception e) {
//            log.error("Error sending enterprises list", e);
//        }
//    }
//
//    private void handleCallback(CallbackQuery callback) {
//        Long chatId = callback.getMessage().getChatId();
//        String data = callback.getData();
//
//        if (data.startsWith("ENT_")) {
//            Long enterpriseId = Long.parseLong(data.substring(4));
//            currentCommand.put(chatId, "AWAIT_PERIOD_" + enterpriseId);
//            sendPeriodSelection(chatId);
//        }
//        else if (data.startsWith("PERIOD_")) {
//            String period = data.substring(7);
//            Long enterpriseId = Long.parseLong(currentCommand.get(chatId).split("_")[2]);
//            generateReport(chatId, enterpriseId, period, null, null);
//        }
//    }
//
//    private void sendPeriodSelection(Long chatId) {
//        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
//        keyboard.setKeyboard(List.of(
//                List.of(
//                        InlineKeyboardButton.builder().text("День").callbackData("PERIOD_DAY").build(),
//                        InlineKeyboardButton.builder().text("Месяц").callbackData("PERIOD_MONTH").build(),
//                        InlineKeyboardButton.builder().text("Год").callbackData("PERIOD_YEAR").build()
//                )
//        ));
//
//        SendMessage message = SendMessage.builder()
//                .chatId(chatId.toString())
//                .text("Выберите период:")
//                .replyMarkup(keyboard)
//                .build();
//
//        try {
//            execute(message);
//        } catch (Exception e) {
//            log.error("Error sending period selection", e);
//        }
//    }
//
//    private void handleUserInput(Long chatId, String text) {
//        String command = currentCommand.get(chatId);
//
//        if (command.equals("AWAIT_VEHICLE_NUMBER")) {
//            handleVehicleNumberInput(chatId, text);
//        }
//    }
//
//    private void handleVehicleNumberInput(Long chatId, String number) {
//        Manager manager = authorizedUsers.get(chatId);
//        Vehicle vehicle = vehicleService.findVehicleByNumber(number)
//                .orElseThrow(() -> new RuntimeException("Машина не найдена"));
//
//        if (!vehicleService.isVehicleManagedByManager(vehicle.getId(), manager.getId())) {
//            throw new RuntimeException("Нет доступа к этой машине");
//        }
//
//        currentCommand.put(chatId, "AWAIT_PERIOD_" + vehicle.getId());
//        sendPeriodSelection(chatId);
//    }
//
//    private void generateReport(Long chatId, Long entityId, String period, LocalDateTime start, LocalDateTime end) {
//        Manager manager = authorizedUsers.get(chatId);
//        MileageReportDTO report = reportService.generateMileageReport(manager, entityId,
//                start != null ? start : LocalDateTime.now().minusDays(1),
//                end != null ? end : LocalDateTime.now(),
//                Period.valueOf(period));
//
//        sendMessage(chatId, formatReport(report));
//        currentCommand.remove(chatId);
//    }
//
//    private String formatReport(MileageReportDTO report) {
//        return String.format("Отчет:\nПробег: %.2f км\nПериод: %s",
//                report.getResults().values().stream().findFirst().orElse(0.0),
//                report.getPeriod());
//    }
//
//    private void sendMessage(Long chatId, String text) {
//        try {
//            execute(SendMessage.builder()
//                    .chatId(chatId.toString())
//                    .text(text)
//                    .build());
//        } catch (Exception e) {
//            log.error("Error sending message", e);
//        }
//    }
//
//    @Override
//    public String getBotUsername() {
//        return botConfig.getBotName();
//    }
//
//    @Override
//    public String getBotToken() {
//        return botConfig.getToken();
//    }
//}