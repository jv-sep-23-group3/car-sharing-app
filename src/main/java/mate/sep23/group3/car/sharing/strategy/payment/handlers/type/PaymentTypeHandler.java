package mate.sep23.group3.car.sharing.strategy.payment.handlers.type;

import mate.sep23.group3.car.sharing.model.Payment;
import mate.sep23.group3.car.sharing.model.Rental;
import mate.sep23.group3.car.sharing.strategy.payment.TypeHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Component(PaymentType.PAYMENT)
public class PaymentTypeHandler implements TypeHandler {
    @Override
    public BigDecimal calculateAmount(Rental rental) {
        long numberOfDays = ChronoUnit.DAYS.between(
                rental.getRentalDate(), rental.getReturnDate()
        );

        return rental.getCar().getDailyFee()
                .multiply(BigDecimal.valueOf(numberOfDays));
    }
}
