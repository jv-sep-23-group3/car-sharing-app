package mate.sep23.group3.car.sharing.service.impl;

import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.service.NotificationService;
import mate.sep23.group3.car.sharing.telegram.TelegramBot;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TelegramNotificationService implements NotificationService {
    private final TelegramBot telegramBot;

    @Override
    public void sendNotification(Long userChatId, String message) {
        telegramBot.sendMessage(userChatId, message);
    }
}
