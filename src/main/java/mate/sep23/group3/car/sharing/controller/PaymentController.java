package mate.sep23.group3.car.sharing.controller;

import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import mate.sep23.group3.car.sharing.model.Role;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.service.PaymentService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
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
    public void createPaymentSession() {

    }

    @GetMapping("/success")
    public void checkSuccessfulPayment() {

    }

    @GetMapping("/cancel")
    public void pausePayment() {

    }
}
