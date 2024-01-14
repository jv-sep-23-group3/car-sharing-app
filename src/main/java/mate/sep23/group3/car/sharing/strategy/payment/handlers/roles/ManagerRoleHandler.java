package mate.sep23.group3.car.sharing.strategy.payment.handlers.roles;

import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.model.Payment;
import mate.sep23.group3.car.sharing.repository.PaymentRepository;
import mate.sep23.group3.car.sharing.strategy.payment.RoleHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component(RoleType.MANAGER)
@RequiredArgsConstructor
public class ManagerRoleHandler implements RoleHandler {
    private final PaymentRepository paymentRepository;

    @Override
    public Page<Payment> getPayments(Long userId, Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }
}
