package mate.sep23.group3.car.sharing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.sep23.group3.car.sharing.dto.rental.RentalRequestDto;
import mate.sep23.group3.car.sharing.dto.rental.RentalResponseDto;
import mate.sep23.group3.car.sharing.exception.EntityNotFoundException;
import mate.sep23.group3.car.sharing.exception.PendingPaymentExistsException;
import mate.sep23.group3.car.sharing.mapper.RentalMapper;
import mate.sep23.group3.car.sharing.model.Car;
import mate.sep23.group3.car.sharing.model.Payment;
import mate.sep23.group3.car.sharing.model.Rental;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.repository.CarRepository;
import mate.sep23.group3.car.sharing.repository.PaymentRepository;
import mate.sep23.group3.car.sharing.repository.RentalRepository;
import mate.sep23.group3.car.sharing.repository.UserRepository;
import mate.sep23.group3.car.sharing.service.impl.RentalServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTest {
    private static final LocalDateTime RENTAL_DATE_BY_USER
            = LocalDateTime.of(2024, 1, 13, 15, 0);
    private static final LocalDateTime RETURN_DATE_BY_USER
            = LocalDateTime.of(2024, 1, 20, 15, 0);
    private static final LocalDateTime ACTUAL_RETURN_DATE_BY_USER
            = LocalDateTime.of(2024, 1, 20, 12, 0);
    private static final Long CAR_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Long RENTAL_ID = 1L;
    private static final Long SECOND_RENTAL_ID = 2L;
    private static Rental rental;
    private static Rental secondRental;
    private static User user;
    private static Car car;
    private static RentalRequestDto requestDto;
    private static RentalResponseDto firstResponseDto;
    private static RentalResponseDto secondResponseDto;
    private static Page<Rental> rentalPages;

    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private RentalMapper rentalMapper;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private RentalServiceImpl rentalService;

    @BeforeAll
    static void beforeAll() {
        car = new Car()
                .setId(CAR_ID)
                .setBrand("BMW")
                .setModel("X5")
                .setType(Car.Type.SUV)
                .setInventory(15)
                .setDailyFee(BigDecimal.valueOf(75));

        user = new User()
                .setId(USER_ID)
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("johndoe@mail.com")
                .setPassword("12345678")
                .setRoles(Set.of());

        rental = new Rental()
                .setId(RENTAL_ID)
                .setUser(user)
                .setCar(car)
                .setRentalDate(RENTAL_DATE_BY_USER)
                .setReturnDate(RETURN_DATE_BY_USER)
                .setIsActive(true);

        secondRental = new Rental()
                .setId(SECOND_RENTAL_ID)
                .setUser(user)
                .setCar(car)
                .setRentalDate(RENTAL_DATE_BY_USER)
                .setReturnDate(RETURN_DATE_BY_USER)
                .setIsActive(true);

        requestDto = new RentalRequestDto()
                .setCarId(CAR_ID)
                .setReturnDate(RETURN_DATE_BY_USER);

        firstResponseDto = new RentalResponseDto()
                .setId(RENTAL_ID)
                .setUserId(USER_ID)
                .setCarId(CAR_ID)
                .setRentalDate(RENTAL_DATE_BY_USER)
                .setReturnDate(RETURN_DATE_BY_USER);

        secondResponseDto = new RentalResponseDto()
                .setId(SECOND_RENTAL_ID)
                .setUserId(USER_ID)
                .setCarId(CAR_ID)
                .setRentalDate(RENTAL_DATE_BY_USER)
                .setReturnDate(RETURN_DATE_BY_USER)
                .setActualReturnDate(ACTUAL_RETURN_DATE_BY_USER);

        rentalPages = new PageImpl<>(List.of(rental, secondRental));
    }

    @Test
    @DisplayName("Save rental, user has pending status rental")
    void save_HasPendingRental_ReturnPendingPaymentExistException() {
        when(paymentRepository.existsPaymentByIdAndType(user.getId(), Payment.Status.PENDING))
                .thenReturn(true);

        Exception exception = assertThrows(
                PendingPaymentExistsException.class,
                () -> rentalService.save(requestDto, user.getId())
        );

        String expectedMessage = "Unfortunately, you are unable to rent a new car "
                + "if you have at least one pending payment!";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Get a list of existing rentals that are specified by parameters")
    void getListByUserIdAndIsActiveStatus_WithValidParams_ReturnValidList() {
        Pageable pageable = PageRequest.of(0, 5);
        when(rentalRepository.findAllByUserIdAndIsActive(USER_ID, true, pageable))
                .thenReturn(rentalPages);
        when(rentalMapper.toDto(rental)).thenReturn(firstResponseDto);

        List<RentalResponseDto> actual = rentalService.getListByUserIdAndIsActiveStatus(USER_ID,
                true, pageable);

        List<RentalResponseDto> expected = rentalPages.stream().map(rentalMapper::toDto).toList();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Check the usual situation when we have our rental in DB")
    void findByIdAndUserId_WithExistingRental_ReturnCorrectDto() {
        when(rentalRepository.findByIdAndUserId(RENTAL_ID, USER_ID))
                .thenReturn(Optional.ofNullable(rental));
        when(rentalMapper.toDto(rental)).thenReturn(firstResponseDto);

        RentalResponseDto actual = rentalService.findByIdAndUserId(RENTAL_ID, USER_ID);

        RentalResponseDto expected = firstResponseDto;
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("There are no rentals available in DB with invalid parameters")
    void findByIdAndUserId_WithNotExistingRental_ReturnEntityNotFoundException() {
        when(rentalRepository.findByIdAndUserId(anyLong(), anyLong()))
                .thenThrow(new EntityNotFoundException("Can't find rental by ID: "));

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> rentalService.findByIdAndUserId(USER_ID, RENTAL_ID)
        );

        String expectedMessage = "Can't find rental by ID: ";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Check the normal case when the user returns the car")
    void update_WithRentalThatNotHaveActualReturnDate_ReturnCorrectDto() {
        when(rentalRepository.findById(SECOND_RENTAL_ID)).thenReturn(
                Optional.ofNullable(secondRental));
        when(carRepository.save(car)).thenReturn(car);
        when(rentalMapper.toDto(secondRental)).thenReturn(secondResponseDto);
        when(rentalRepository.save(secondRental)).thenReturn(secondRental);

        RentalResponseDto actual = rentalService.update(SECOND_RENTAL_ID);

        RentalResponseDto expected = secondResponseDto;
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("We can't return our rental with the car that already returned.")
    void update_WithRentalThatNotExists_ReturnRentalReturnException() {
        Long invalidId = 100_000_000L;
        when(rentalRepository.findById(invalidId))
                .thenThrow(new EntityNotFoundException("Can't find rental by ID: "));

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> rentalService.update(invalidId)
        );

        assertEquals("Can't find rental by ID: ",
                exception.getMessage());
    }
}
