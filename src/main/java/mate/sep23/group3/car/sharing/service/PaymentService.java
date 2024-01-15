package mate.sep23.group3.car.sharing.service;

import java.util.List;
import mate.sep23.group3.car.sharing.dto.payment.PaymentRequestDto;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    List<PaymentResponseDto> getAll(Long userId, Pageable pageable);

    PaymentResponseDto createPaymentSession(PaymentRequestDto paymentRequestDto);

    String setSuccessfulPayment(String sessionId);

    String setCanceledPayment(String sessionId);
}
