package mate.sep23.group3.car.sharing.strategy.payment;

import mate.sep23.group3.car.sharing.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleHandler {
    Page<Payment> getPayments(Long userId, Pageable pageable);
}
