package mate.sep23.group3.car.sharing.service.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import jakarta.annotation.PostConstruct;
import mate.sep23.group3.car.sharing.exception.StripeProcessingException;
import mate.sep23.group3.car.sharing.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;

public class PaymentServiceImpl implements PaymentService {
    @Value("${stripe.api.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    @Override
    public void createCustomer(String email, String firstName, String lastName) {
        CustomerCreateParams createParams = CustomerCreateParams.builder()
                .setEmail(email)
                .setName(String.format("%s %s", firstName, lastName))
                .build();

        try {
            Customer.create(createParams);
        } catch (StripeException e) {
            throw new StripeProcessingException(
                    String.format("Can't create customer with email: %s", email), e
            );
        }
    }
}
