package mate.sep23.group3.car.sharing.strategy.payment;

import mate.sep23.group3.car.sharing.model.Payment;
import mate.sep23.group3.car.sharing.model.Role;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleHandler {
    Role getRoleName();

    List<Payment> getPayments(Long userId, Pageable pageable);
}
