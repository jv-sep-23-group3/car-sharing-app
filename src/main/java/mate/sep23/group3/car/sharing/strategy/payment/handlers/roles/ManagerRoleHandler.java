package mate.sep23.group3.car.sharing.strategy.payment.handlers.roles;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.model.Payment;
import mate.sep23.group3.car.sharing.model.Role;
import mate.sep23.group3.car.sharing.repository.PaymentRepository;
import mate.sep23.group3.car.sharing.strategy.payment.RoleHandler;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagerRoleHandler implements RoleHandler {
    private static final Role MANAGER = new Role()
            .setRoleName(Role.RoleName.MANAGER);
    private final PaymentRepository paymentRepository;

    @Override
    public Role getRoleName() {
        return MANAGER;
    }

    @Override
    public List<Payment> getPayments(Long userId, Pageable pageable) {
        return paymentRepository.findAll();
    }
}
