package mate.sep23.group3.car.sharing.telegram;

import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.config.BotConfig;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RequiredArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final String SEND_EMAIL = "Sent an email for "
            + "authentication to receive notifications";
    private static final String  EMAIL_OK= "Now you will receive notifications from CarSharing!";
    private static final String  EMAIL_NOT_OK= "Email is invalid or not exist";
    private static final String NOT_RECOGNIZED = "Sorry, but command was not recognized!";
    private static final Logger log
            = LoggerFactory.getLogger(TelegramBot.class);
    private final BotConfig botConfig;
    private final UserRepository userRepository;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        if (messageText.equals("/start")) {
            sendMessage(chatId, SEND_EMAIL);
        } else {
            if (userRepository.findByEmail(messageText).isPresent()) {
                User user = userRepository.findByEmail(messageText).get();
                user.setChatId(chatId);
                userRepository.save(user);
                sendMessage(chatId, EMAIL_OK);
            } else {
                sendMessage(chatId, EMAIL_NOT_OK);
            }
        }
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
