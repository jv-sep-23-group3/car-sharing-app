package mate.sep23.group3.car.sharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.payment.PaymentRequestDto;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.service.PaymentService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payment management", description = "Endpoints for managing payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    @GetMapping
    @Operation(summary = "Get all payments",
            description = "Managers can see all payments, customers only theirs")
    public List<PaymentResponseDto> getPayments(
            Authentication authentication,
            Pageable pageable
    ) {
        User user = (User) authentication.getPrincipal();
        return paymentService.getAll(user.getId(), pageable);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    @Operation(summary = "Create payment session",
            description = "A car rental payment session is created")
    public PaymentResponseDto createPaymentSession(
            @RequestBody @Valid PaymentRequestDto paymentRequestDto
    ) {
        return paymentService.createPaymentSession(paymentRequestDto);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/success")
    @Operation(summary = "Success payment",
            description = "You will be automatically redirected"
                    + " to this endpoint after successful payment")
    public String checkSuccessfulPayment(
            @RequestParam("sessionId") String sessionId
    ) {
        return paymentService.setSuccessfulPayment(sessionId);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/cancel")
    @Operation(summary = "Canceled payment",
            description = "You will be automatically redirected"
                    + " to this endpoint after canceled payment")
    public String pausePayment(
            @RequestParam("sessionId") String sessionId
    ) {
        return paymentService.setCanceledPayment(sessionId);
    }
}
