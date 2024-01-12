package mate.sep23.group3.car.sharing.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class OverdueRentalsNotification {
    private final TelegramLongPollingBot telegramBot;
    //private final RentalRepository rentalRepository;

    @Scheduled(cron = "0 00 15 * * *")// notification will be sent every day at 15:00
    public void sendMessageToManagerGroup() {
        //List<Rental> rentals = >rentalRepository.findAllAvaliableRental();
        //StringBuilder messageForManagers = new StringBuilder();
    }

}
