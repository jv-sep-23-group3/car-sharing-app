package mate.sep23.group3.car.sharing.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import mate.sep23.group3.car.sharing.dto.payment.PaymentRequestDto;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import mate.sep23.group3.car.sharing.exception.EntityNotFoundException;
import mate.sep23.group3.car.sharing.mapper.PaymentMapper;
import mate.sep23.group3.car.sharing.model.*;
import mate.sep23.group3.car.sharing.repository.PaymentRepository;
import mate.sep23.group3.car.sharing.repository.RentalRepository;
import mate.sep23.group3.car.sharing.repository.UserRepository;
import mate.sep23.group3.car.sharing.service.impl.PaymentServiceImpl;
import mate.sep23.group3.car.sharing.strategy.payment.RoleHandler;
import mate.sep23.group3.car.sharing.strategy.payment.TypeHandler;
import mate.sep23.group3.car.sharing.strategy.payment.handlers.roles.CustomerRoleHandler;
import mate.sep23.group3.car.sharing.strategy.payment.handlers.roles.RoleFactory;
import mate.sep23.group3.car.sharing.strategy.payment.handlers.type.PaymentFactory;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.aspectj.weaver.TypeFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    private static User customer;
    private static User anotherCustomer;
    private static User manager;
    private static Rental firstRental;
    private static Payment first;
    private static Payment second;
    private static Payment third;
    private static PaymentRequestDto createPaymentSessionDto;
    private static Payment createdPayment;
    private static PaymentResponseDto createdPaymentDto;
    private static PaymentResponseDto firstResponse;
    private static PaymentResponseDto secondResponse;
    private static PaymentResponseDto thirdResponse;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private RoleFactory roleFactory;
    @Mock
    private RoleHandler roleHandler;
    @Mock
    private PaymentMapper paymentMapper;
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeAll
    static void beforeAll() {


        Role customerRole = new Role()
                .setId(1L)
                .setRoleName(Role.RoleName.CUSTOMER);

        Role managerRole = new Role()
                .setId(2L)
                .setRoleName(Role.RoleName.MANAGER);

        customer = new User()
                .setId(1L)
                .setEmail("customer@email.com")
                .setRoles(Set.of(customerRole));

        anotherCustomer = new User()
                .setId(2L)
                .setEmail("anotherCustomer@email.com")
                .setRoles(Set.of(customerRole));

        manager = new User()
                .setId(3L)
                .setEmail("manager@email.com")
                .setRoles(Set.of(managerRole));

        Car firstCar = new Car()
                .setId(1L)
                .setBrand("BMW")
                .setModel("X5")
                .setType(Car.Type.SUV)
                .setInventory(5)
                .setDailyFee(BigDecimal.valueOf(100));

        Car secondCar = new Car()
                .setId(2L)
                .setBrand("Audi")
                .setModel("A6")
                .setType(Car.Type.SEDAN)
                .setInventory(10)
                .setDailyFee(BigDecimal.valueOf(50));

        firstRental = new Rental()
                .setId(1L)
                .setCar(firstCar)
                .setUser(customer)
                .setRentalDate(LocalDateTime.now())
                .setReturnDate(LocalDateTime.now().plusDays(10))
                .setActualReturnDate(LocalDateTime.now().plusDays(10));

        Rental secondRental = new Rental()
                .setId(2L)
                .setCar(secondCar)
                .setUser(anotherCustomer)
                .setRentalDate(LocalDateTime.now())
                .setReturnDate(LocalDateTime.now().plusDays(5))
                .setActualReturnDate(LocalDateTime.now().plusDays(7));

        createPaymentSessionDto = new PaymentRequestDto()
                .setRentalId(1L)
                .setType(Payment.Type.PAYMENT);

        createdPayment = new Payment()
                .setId(1L)
                .setStatus(Payment.Status.PENDING)
                .setType(Payment.Type.PAYMENT)
                .setRental(firstRental)
                .setSession("Session_url")
                .setSessionId("Session_id")
                .setAmount(BigDecimal.valueOf(1000));

        createdPaymentDto = new PaymentResponseDto()
                .setId(1L)
                .setStatus(Payment.Status.PENDING)
                .setType(Payment.Type.PAYMENT)
                .setRentalId(1L)
                .setSession("Session_url")
                .setSessionId("Session_id")
                .setAmount(BigDecimal.valueOf(1000));

        first = new Payment()
                .setId(1L)
                .setStatus(Payment.Status.PENDING)
                .setType(Payment.Type.PAYMENT)
                .setRental(firstRental)
                .setSession("Session_url")
                .setSessionId("Session_id")
                .setAmount(BigDecimal.valueOf(1000));

        second = new Payment()
                .setId(2L)
                .setStatus(Payment.Status.PAID)
                .setType(Payment.Type.PAYMENT)
                .setRental(secondRental)
                .setSession("Session_url")
                .setSessionId("Session_id")
                .setAmount(BigDecimal.valueOf(250));

        third = new Payment()
                .setId(3L)
                .setStatus(Payment.Status.PAID)
                .setType(Payment.Type.FINE)
                .setRental(secondRental)
                .setSession("Session_url")
                .setSessionId("Session_id")
                .setAmount(BigDecimal.valueOf(150));

        firstResponse = new PaymentResponseDto()
                .setId(1L)
                .setStatus(Payment.Status.PAID)
                .setType(Payment.Type.PAYMENT)
                .setRentalId(1L)
                .setSession("Session_url")
                .setSessionId("Session_id")
                .setAmount(BigDecimal.valueOf(1000));

        secondResponse = new PaymentResponseDto()
                .setId(2L)
                .setStatus(Payment.Status.PAID)
                .setType(Payment.Type.PAYMENT)
                .setRentalId(2L)
                .setSession("Session_url")
                .setSessionId("Session_id")
                .setAmount(BigDecimal.valueOf(250));

        thirdResponse = new PaymentResponseDto()
                .setId(3L)
                .setStatus(Payment.Status.PAID)
                .setType(Payment.Type.FINE)
                .setRentalId(3L)
                .setSession("Session_url")
                .setSessionId("Session_id")
                .setAmount(BigDecimal.valueOf(150));
    }

    @Test
    void getAllForCustomer_OneSavedPayment_GetListWithOnePaymentDto() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(customer));
        Mockito.when(roleFactory.getRoleHandler("ROLE_CUSTOMER")).thenReturn(roleHandler);
        Mockito.doReturn(new PageImpl<>(List.of(first)))
                .when(roleHandler).getPayments(userId, PageRequest.of(0, 20));

        List<PaymentResponseDto> expected = List.of(firstResponse);
        List<PaymentResponseDto> actual = paymentService.getAll(userId, PageRequest.of(0, 20));

        Assertions.assertEquals(expected.size(), actual.size());
        EqualsBuilder.reflectionEquals(expected.get(0), actual.get(0), "id");
    }

    @Test
    void getAllForManager_ThreeSavedPayments_ReturnListWithThreePaymentDtos() {
        Long userId = 3L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(manager));
        Mockito.when(roleFactory.getRoleHandler("ROLE_MANAGER")).thenReturn(roleHandler);
        Mockito.doReturn(new PageImpl<>(List.of(first, second, third)))
                .when(roleHandler).getPayments(userId, PageRequest.of(0, 20));

        List<PaymentResponseDto> expected = List.of(firstResponse, secondResponse, thirdResponse);
        List<PaymentResponseDto> actual = paymentService.getAll(userId, PageRequest.of(0, 20));

        Assertions.assertEquals(expected.size(), actual.size());
        EqualsBuilder.reflectionEquals(expected.get(0), actual.get(0), "id");
        EqualsBuilder.reflectionEquals(expected.get(1), actual.get(1), "id");
        EqualsBuilder.reflectionEquals(expected.get(2), actual.get(2), "id");
    }

    @Test
    void setSuccessfulPayment_ValidSessionId_ReturnMessage() {
        String sessionId = "valid session id";

        Mockito.when(paymentRepository.findBySessionId(sessionId)).thenReturn(Optional.of(first));
        Mockito.when(paymentRepository.save(first)).thenReturn(first);

        String expected = "Payment was successful";
        String actual = paymentService.setSuccessfulPayment(sessionId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void setSuccessfulPayment_InvalidSessionId_ThrowException() {
        String sessionId = "invalid session id";

        Mockito.when(paymentRepository.findBySessionId(sessionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> paymentService.setSuccessfulPayment(sessionId)
        );
    }

    @Test
    void setCanceledPayment_ValidSessionId_ReturnMessage() {
        String sessionId = "valid session id";

        Mockito.when(paymentRepository.findBySessionId(sessionId)).thenReturn(Optional.of(first));
        Mockito.when(paymentRepository.save(first)).thenReturn(first);

        String expected = "You can pay in 24 hours";
        String actual = paymentService.setCanceledPayment(sessionId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void setCanceledPayment_InvalidSessionId_ThrowException() {
        String sessionId = "invalid session id";

        Mockito.when(paymentRepository.findBySessionId(sessionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> paymentService.setCanceledPayment(sessionId)
        );
    }
}
