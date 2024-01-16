package mate.sep23.group3.car.sharing.config;

import mate.sep23.group3.car.sharing.telegram.BotInitializer;
import mate.sep23.group3.car.sharing.telegram.TelegramBot;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
@MockBean(BotInitializer.class)
@MockBean(TelegramBot.class)
public class ControllerTestConfig {
}
