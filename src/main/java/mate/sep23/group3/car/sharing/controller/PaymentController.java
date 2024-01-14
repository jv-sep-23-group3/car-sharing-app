package mate.sep23.group3.car.sharing.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.payment.PaymentRequestDto;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/success")
    public String checkSuccessfulPayment(
            @RequestParam("sessionId") String sessionId
    ) {
        return paymentService.setSuccessfulPayment(sessionId);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/cancel")
    public String pausePayment(
            @RequestParam("sessionId") String sessionId
    ) {
        return paymentService.setCanceledPayment(sessionId);
    }
}
