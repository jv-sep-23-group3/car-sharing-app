package mate.sep23.group3.car.sharing.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {
    List<PaymentResponseDto> getAll(Long userId, Pageable pageable);
}
