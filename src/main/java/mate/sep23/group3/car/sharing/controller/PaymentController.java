package mate.sep23.group3.car.sharing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @GetMapping
    public void getPayments() {

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
