package mate.sep23.group3.car.sharing.strategy.payment.handlers.roles;

import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.model.Payment;
import mate.sep23.group3.car.sharing.model.Rental;
import mate.sep23.group3.car.sharing.repository.PaymentRepository;
import mate.sep23.group3.car.sharing.repository.RentalRepository;
import mate.sep23.group3.car.sharing.strategy.payment.RoleHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(RoleType.CUSTOMER)
@RequiredArgsConstructor
public class CustomerRoleHandler implements RoleHandler {
    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Page<Payment> getPayments(Long userId, Pageable pageable) {
        List<Rental> rentals = rentalRepository.findAllByUserId(userId);

        return paymentRepository.findAllByRentalIn(rentals, pageable);
    }
}
