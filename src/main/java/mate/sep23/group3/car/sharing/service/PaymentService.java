package mate.sep23.group3.car.sharing.service;

import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import mate.sep23.group3.car.sharing.dto.payment.PaymentUrlResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {
    List<PaymentResponseDto> getAll(Long userId, Pageable pageable);

    PaymentUrlResponseDto createPaymentSession(Long userId);
}
