package mate.sep23.group3.car.sharing.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.payment.PaymentRequestDto;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.service.PaymentService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public PaymentResponseDto createPaymentSession(
            @RequestBody PaymentRequestDto paymentRequestDto
            ) {
        return paymentService.createPaymentSession(paymentRequestDto);
    }

    @GetMapping("/success")
    public void checkSuccessfulPayment(
            @RequestParam("sessionId") String sessionId
    ) {

    }

    @GetMapping("/cancel")
    public void pausePayment() {

    }
}
