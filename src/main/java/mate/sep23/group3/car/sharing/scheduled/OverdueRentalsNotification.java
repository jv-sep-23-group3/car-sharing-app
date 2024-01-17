package mate.sep23.group3.car.sharing.scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.model.Rental;
import mate.sep23.group3.car.sharing.repository.RentalRepository;
import mate.sep23.group3.car.sharing.telegram.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class OverdueRentalsNotification {
    private static final int THREE_HOURS = 3;
    private static final String INDENTATION = " ";
    private static final String COMA = ",";
    private static final String DAILY_FEE = "daily fee: ";
    private static final String RETURNED_DATE = "Returned date :";
    private static final String MONEY = "💵";
    private static final String TIME = "🕖";
    @Value("${admin.chat.id}")
    private Long adminChatId;
    private final TelegramBot telegramBot;
    private final RentalRepository rentalRepository;

    @Scheduled(cron = "0 00 15 * * *")// notification will be sent every day at 15:00
    public void sendMessageToUsersAndManagerGroup() {
        List<Rental> rentals = rentalRepository.findAllByIsActive(true);
        StringBuilder messageForManagers = new StringBuilder();
        for (Rental rental : rentals) {
            if (ChronoUnit.HOURS.between(LocalDateTime.now(),
                    rental.getReturnDate()) < THREE_HOURS) {
                String message = rental.getCar().getBrand() + INDENTATION
                        + rental.getCar().getModel() + COMA + INDENTATION
                        + DAILY_FEE + rental.getCar().getDailyFee() + MONEY
                        + System.lineSeparator()
                        + RETURNED_DATE + rental.getReturnDate()
                        .format(DateTimeFormatter.ofPattern("d MMMM yyyy h:mm a")) + TIME;

                messageForManagers.append(rental.getUser().getEmail())
                        .append(System.lineSeparator()).append(message)
                        .append(System.lineSeparator()).append(System.lineSeparator());
                telegramBot.sendMessage(rental.getUser().getChatId(), message);
            } else {
                telegramBot.sendMessage(adminChatId, "There are no overdue "
                        + "rentals on the moment!");
                return;
            }
        }

        telegramBot.sendMessage(adminChatId, messageForManagers.toString());
    }
}
