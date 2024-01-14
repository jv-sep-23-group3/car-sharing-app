package mate.sep23.group3.car.sharing.strategy.payment;

import java.math.BigDecimal;
import mate.sep23.group3.car.sharing.model.Rental;

public interface TypeHandler {
    BigDecimal calculateAmount(Rental rental);
}
