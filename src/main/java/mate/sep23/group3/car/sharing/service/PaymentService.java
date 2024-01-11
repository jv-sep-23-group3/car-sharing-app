package mate.sep23.group3.car.sharing.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

public interface PaymentService {
    void createCustomer(String email, String firstName, String lastName);
}
