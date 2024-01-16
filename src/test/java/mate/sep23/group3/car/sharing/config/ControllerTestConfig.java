package mate.sep23.group3.car.sharing.config;

import mate.sep23.group3.car.sharing.telegram.BotInitializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
@MockBean(BotInitializer.class)
public class ControllerTestConfig {
}
