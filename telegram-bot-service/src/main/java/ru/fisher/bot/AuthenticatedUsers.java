package ru.fisher.bot;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthenticatedUsers {
    private final Map<String, Long> usernameToChatId = new ConcurrentHashMap<>();

    public void login(String username, Long chatId) {
        usernameToChatId.put(username, chatId);
    }

    public void logout(String username) {
        usernameToChatId.remove(username);
    }

    public Optional<Long> getChatId(String username) {
        return Optional.ofNullable(usernameToChatId.get(username));
    }
}
