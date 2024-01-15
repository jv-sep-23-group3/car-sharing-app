package mate.sep23.group3.car.sharing.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.rental.RentalRequestDto;
import mate.sep23.group3.car.sharing.dto.rental.RentalResponseDto;
import mate.sep23.group3.car.sharing.exception.CarInventoryException;
import mate.sep23.group3.car.sharing.exception.EntityNotFoundException;
import mate.sep23.group3.car.sharing.exception.PendingPaymentExistsException;
import mate.sep23.group3.car.sharing.exception.RentalReturnException;
import mate.sep23.group3.car.sharing.mapper.RentalMapper;
import mate.sep23.group3.car.sharing.model.Car;
import mate.sep23.group3.car.sharing.model.Payment;
import mate.sep23.group3.car.sharing.model.Rental;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.repository.CarRepository;
import mate.sep23.group3.car.sharing.repository.PaymentRepository;
import mate.sep23.group3.car.sharing.repository.RentalRepository;
import mate.sep23.group3.car.sharing.repository.UserRepository;
import mate.sep23.group3.car.sharing.service.NotificationService;
import mate.sep23.group3.car.sharing.service.RentalService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private static final String CAN_NOT_FIND_RENTAL_BY_ID_MESSAGE =
            "Can't find rental by ID: ";
    private static final String CAN_NOT_FIND_CAR_BY_ID_MESSAGE =
            "Can't find car by ID: ";
    private static final String CAN_NOT_FIND_USER_BY_ID_MESSAGE =
            "Can't find user by ID: ";
    private static final String CAR_INVENTORY_IS_EMPTY_MESSAGE =
            "Sorry, this car model isn't currently available!";
    private static final String PENDING_PAYMENT_EXISTS_EXCEPTION =
            "Unfortunately, you are unable to rent a new car "
                    + "if you have at least one pending payment!";
    private static final String CAN_NOT_RETURN_CAR_MESSAGE =
            "We cannot return a car that has actually already returned.";

    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public RentalResponseDto save(RentalRequestDto requestDto, Long userId) {
        Boolean isPendingExists =
                paymentRepository.existsPaymentByIdAndType(userId, Payment.Status.PENDING);
        if (isPendingExists) {
            throw new PendingPaymentExistsException(PENDING_PAYMENT_EXISTS_EXCEPTION);
        }
        return rentalMapper.toDto(
                rentalRepository.save(setUpRental(requestDto, userId))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalResponseDto> getListByUserIdAndIsActiveStatus(
            Long userId,
            boolean isActive,
            Pageable pageable
    ) {
        return rentalRepository.findAllByUserIdAndIsActive(userId, isActive, pageable)
                .stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    @Override
    public RentalResponseDto findByIdAndUserId(Long id, Long userId) {
        Rental rental = rentalRepository.findByIdAndUserId(id, userId).orElseThrow(
                () -> new EntityNotFoundException(
                        CAN_NOT_FIND_RENTAL_BY_ID_MESSAGE + id
                )
        );
        return rentalMapper.toDto(rental);
    }

    @Override
    @Transactional
    public RentalResponseDto update(Long id) {
        Rental rental = rentalRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        CAN_NOT_FIND_RENTAL_BY_ID_MESSAGE + id
                )
        );
        if (rental.getActualReturnDate() == null) {
            rental.setActualReturnDate(LocalDateTime.now());
            Car car = rental.getCar();
            car.setInventory(car.getInventory() + 1);
            rental.setIsActive(false);
            carRepository.save(car);
        } else {
            throw new RentalReturnException(CAN_NOT_RETURN_CAR_MESSAGE);
        }
        return rentalMapper.toDto(rentalRepository.save(rental));
    }

    private Rental setUpRental(RentalRequestDto requestDto, Long userId) {
        Car car = carRepository.findById(requestDto.getCarId()).orElseThrow(
                () -> new EntityNotFoundException(
                        CAN_NOT_FIND_CAR_BY_ID_MESSAGE + requestDto.getCarId()
                )
        );

        int carInventory = car.getInventory();
        if (carInventory > 0) {
            Rental rental = new Rental();
            rental.setRentalDate(LocalDateTime.now());
            rental.setReturnDate(requestDto.getReturnDate());
            car.setInventory(carInventory - 1);
            rental.setCar(car);

            User user = userRepository.findById(userId).orElseThrow(
                    () -> new EntityNotFoundException(CAN_NOT_FIND_USER_BY_ID_MESSAGE + userId)
            );

            rental.setUser(user);
            rental.setIsActive(true);

            notificationService.sendNotification(user.getChatId(), formatMessage(rental));

            return rental;
        } else {
            throw new CarInventoryException(CAR_INVENTORY_IS_EMPTY_MESSAGE);
        }
    }

    private String formatMessage(Rental rental) {
        return String.format(
                "✅Rental succeed! You have successfully submitted your application to rent a "
                        + rental.getCar().getBrand() + " "
                        + rental.getCar().getModel() + "🚗"
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + "You'll need to get car back on "
                        + rental.getReturnDate()
                        .format(DateTimeFormatter.ofPattern("d MMMM yyyy h:mm a")) + "⏳"
        );
    }
}
