package mate.sep23.group3.car.sharing.strategy.payment.handlers.type;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import mate.sep23.group3.car.sharing.model.Rental;
import mate.sep23.group3.car.sharing.strategy.payment.TypeHandler;
import org.springframework.stereotype.Component;

@Component(PaymentType.FINE)
public class FineTypeHandler implements TypeHandler {
    private static final Double FINE_MULTIPLIER = 1.5;

    @Override
    public BigDecimal calculateAmount(Rental rental) {
        long numberOfDays = ChronoUnit.DAYS.between(
                rental.getReturnDate(), rental.getActualReturnDate()
        );

        return rental.getCar().getDailyFee()
                .multiply(BigDecimal.valueOf(numberOfDays))
                .multiply(BigDecimal.valueOf(FINE_MULTIPLIER));
    }
}
