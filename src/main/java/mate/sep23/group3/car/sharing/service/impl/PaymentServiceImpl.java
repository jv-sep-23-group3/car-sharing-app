package mate.sep23.group3.car.sharing.service.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import mate.sep23.group3.car.sharing.dto.payment.PaymentUrlResponseDto;
import mate.sep23.group3.car.sharing.exception.EntityNotFoundException;
import mate.sep23.group3.car.sharing.exception.StripeProcessingException;
import mate.sep23.group3.car.sharing.mapper.PaymentMapper;
import mate.sep23.group3.car.sharing.model.Car;
import mate.sep23.group3.car.sharing.model.Payment;
import mate.sep23.group3.car.sharing.model.Rental;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.repository.PaymentRepository;
import mate.sep23.group3.car.sharing.repository.RentalRepository;
import mate.sep23.group3.car.sharing.repository.UserRepository;
import mate.sep23.group3.car.sharing.service.PaymentService;
import mate.sep23.group3.car.sharing.strategy.payment.RoleHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final String DEFAULT_CURRENCY = "USD";
    private static final String CAR_RENT_TAX_CODE = "txcd_20030000";
    private static final Long DEFAULT_QUANTITY = 1L;

    private final List<RoleHandler> roleHandlers;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final PaymentMapper paymentMapper;

    @Value("${stripe.api.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    @Override
    public List<PaymentResponseDto> getAll(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't get user with id: " + userId)
        );

        List<Payment> payments = roleHandlers.stream()
                .filter(rh -> user.getRoles().contains(rh.getRoleName()))
                .map(rh -> rh.getPayments(userId, pageable))
                .flatMap(Collection::stream)
                .toList();

        return payments.stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public PaymentUrlResponseDto createPaymentSession(Long userId) {
        Rental usersActiveRental = rentalRepository.findByUserIdAndIsActiveTrue(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't get rental with user id: " + userId)
        );

        Payment payment = createPayment(usersActiveRental);
        Session session = createPaymentSession(payment.getAmount(), usersActiveRental.getCar());

        session.setSuccessUrl(String.format(
                "http://localhost:8080/api/payments/success?session_id=%s",
                session.getId())
        );

        session.setCancelUrl(String.format(
                "http://localhost:8080/api/payments/cancel?session_id=%s",
                session.getId())
        );

        payment.setSession(session.getUrl());
        payment.setSessionId(session.getId());

        paymentRepository.save(payment);

        return paymentMapper.toDto(session);
    }

    private Payment createPayment(Rental rental) {
        Payment payment = new Payment()
                .setStatus(Payment.Status.PENDING)
                .setRental(rental);

        BigDecimal amount = calculateBasicPrice(rental);

        if (rental.getActualReturnDate().isAfter(rental.getReturnDate())) {
            payment.setType(Payment.Type.FINE);
            amount = amount.multiply(BigDecimal.valueOf(1.5));
        } else {
            payment.setType(Payment.Type.PAYMENT);
        }

        return payment.setAmount(amount);
    }

    private BigDecimal calculateBasicPrice(Rental rental) {
        return rental.getCar().getDailyFee().multiply(
                BigDecimal.valueOf(ChronoUnit.DAYS.between(
                        rental.getRentalDate(),
                        rental.getActualReturnDate())
                )
        );
    }

    private Session createPaymentSession(
            BigDecimal amount, Car car
    ) {
        SessionCreateParams createParams = SessionCreateParams.builder()
                .addLineItem(createLineItem(amount, car))
                .build();

        try {
            return Session.create(createParams);
        } catch (StripeException e) {
            throw new StripeProcessingException("Can't create session", e);
        }
    }

    private SessionCreateParams.LineItem createLineItem(
            BigDecimal amount, Car car
    ) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(amount, car))
                .setQuantity(DEFAULT_QUANTITY)
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(
            BigDecimal amount, Car car
    ) {
        String name = String.format("%s %s", car.getModel(), car.getBrand());
        String description = String.format("%s rental", name);

        return SessionCreateParams.LineItem.PriceData.builder()
                .setProductData(createProductData(name, description))
                .setCurrency(DEFAULT_CURRENCY)
                .setUnitAmount(
                        amount.multiply(BigDecimal.valueOf(100)).longValue()
                )
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(
            String name, String description
    ) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(name)
                .setDescription(description)
                .setTaxCode(CAR_RENT_TAX_CODE)
                .build();
    }
}
