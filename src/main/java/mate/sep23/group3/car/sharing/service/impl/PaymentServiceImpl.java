package mate.sep23.group3.car.sharing.service.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.payment.PaymentRequestDto;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
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
import mate.sep23.group3.car.sharing.strategy.payment.TypeHandler;
import mate.sep23.group3.car.sharing.strategy.payment.handlers.type.PaymentFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final String DEFAULT_CURRENCY = "USD";
    private static final String CAR_RENT_TAX_CODE = "txcd_20030000";
    private static final String SUCCESS_URL_TEMPLATE
            = "http://localhost:8080/api/payments/success?sessionId={CHECKOUT_SESSION_ID}";
    private static final String CANCEL_URL_TEMPLATE
            = "http://localhost:8080/api/payments/cancel?sessionId={CHECKOUT_SESSION_ID}";
    private static final String SUCCESSFUL_PAYMENT = "Payment was successful";
    private static final String CANCELED_PAYMENT = "You can pay in 24 hours";
    private static final Long DEFAULT_QUANTITY = 1L;

    private final PaymentFactory paymentFactory;
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
    public PaymentResponseDto createPaymentSession(PaymentRequestDto paymentRequestDto) {
        Rental rental = rentalRepository.findById(paymentRequestDto.getRentalId()).orElseThrow(
                () -> new EntityNotFoundException("Can't get rental with id: "
                        + paymentRequestDto.getRentalId())
        );

        TypeHandler typeHandler = paymentFactory.getTypeHandler(paymentRequestDto.getType().name());
        BigDecimal amount = typeHandler.calculateAmount(rental);

        Session session = createSession(amount, rental.getCar());

        Payment payment = paymentMapper.toModel(paymentRequestDto)
                .setStatus(Payment.Status.PENDING)
                .setType(paymentRequestDto.getType())
                .setRental(rental)
                .setSession(session.getUrl())
                .setSessionId(session.getId())
                .setAmount(amount);

        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    public String setSuccessfulPayment(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId).orElseThrow(
                () -> new EntityNotFoundException("Can't get payment with session id: " + sessionId)
        );

        payment.setStatus(Payment.Status.PAID);
        paymentRepository.save(payment);

        return SUCCESSFUL_PAYMENT;
    }

    @Override
    public String setCanceledPayment(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId).orElseThrow(
                () -> new EntityNotFoundException("Can't get payment with session id: " + sessionId)
        );

        payment.setStatus(Payment.Status.CANCELED);
        paymentRepository.save(payment);

        return CANCELED_PAYMENT;
    }

    private Session createSession(
            BigDecimal amount, Car car
    ) {
        SessionCreateParams createParams = SessionCreateParams.builder()
                .setSuccessUrl(SUCCESS_URL_TEMPLATE)
                .setCancelUrl(CANCEL_URL_TEMPLATE)
                .addLineItem(createLineItem(amount, car))
                .setMode(SessionCreateParams.Mode.PAYMENT)
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
        String name = String.format("%s %s", car.getBrand(), car.getModel());
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
