package mate.sep23.group3.car.sharing.service.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import mate.sep23.group3.car.sharing.exception.EntityNotFoundException;
import mate.sep23.group3.car.sharing.exception.StripeProcessingException;
import mate.sep23.group3.car.sharing.mapper.PaymentMapper;
import mate.sep23.group3.car.sharing.model.Payment;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.repository.PaymentRepository;
import mate.sep23.group3.car.sharing.repository.RentalRepository;
import mate.sep23.group3.car.sharing.repository.UserRepository;
import mate.sep23.group3.car.sharing.service.PaymentService;
import mate.sep23.group3.car.sharing.strategy.payment.RoleHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Value("${stripe.api.key}")
    private String apiKey;
    private final List<RoleHandler> roleHandlers;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;

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
}
