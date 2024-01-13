package mate.sep23.group3.car.sharing.strategy.payment;

import mate.sep23.group3.car.sharing.model.Rental;

import java.math.BigDecimal;

public interface TypeHandler {
    BigDecimal calculateAmount(Rental rental);
}
