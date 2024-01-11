package mate.sep23.group3.car.sharing;

import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.service.NotificationService;
import mate.sep23.group3.car.sharing.service.impl.TelegramNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class CarSharingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarSharingAppApplication.class, args);
    }

}
