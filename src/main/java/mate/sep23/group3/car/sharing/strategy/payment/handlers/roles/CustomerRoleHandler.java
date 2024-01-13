package mate.sep23.group3.car.sharing.strategy.payment.handlers.roles;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.model.Payment;
import mate.sep23.group3.car.sharing.model.Rental;
import mate.sep23.group3.car.sharing.model.Role;
import mate.sep23.group3.car.sharing.repository.PaymentRepository;
import mate.sep23.group3.car.sharing.repository.RentalRepository;
import mate.sep23.group3.car.sharing.strategy.payment.RoleHandler;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerRoleHandler implements RoleHandler {
    private static final Role CUSTOMER = new Role()
            .setRoleName(Role.RoleName.CUSTOMER);
    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;

    @Override
    public Role getRoleName() {
        return CUSTOMER;
    }

    @Override
    public List<Payment> getPayments(Long userId, Pageable pageable) {
        List<Rental> rentals = rentalRepository.findAllByUserId(userId);
        return paymentRepository.findAllByRentalIn(rentals, pageable);
    }
}
