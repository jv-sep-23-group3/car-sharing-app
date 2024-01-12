package mate.sep23.group3.car.sharing.controller;

import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import mate.sep23.group3.car.sharing.dto.payment.PaymentUrlResponseDto;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.service.PaymentService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public List<PaymentResponseDto> getPayments(
            Authentication authentication,
            Pageable pageable
    ) {
        User user = (User) authentication.getPrincipal();
        return paymentService.getAll(user.getId(), pageable);
    }

    @PostMapping
    public PaymentUrlResponseDto createPaymentSession(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return paymentService.createPaymentSession(user.getId());
    }

    @GetMapping("/success")
    public void checkSuccessfulPayment() {

    }

    @GetMapping("/cancel")
    public void pausePayment() {

    }
}
