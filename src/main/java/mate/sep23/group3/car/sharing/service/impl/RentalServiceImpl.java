package mate.sep23.group3.car.sharing.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.rental.RentalRequestDto;
import mate.sep23.group3.car.sharing.dto.rental.RentalResponseDto;
import mate.sep23.group3.car.sharing.exception.EntityNotFoundException;
import mate.sep23.group3.car.sharing.mapper.RentalMapper;
import mate.sep23.group3.car.sharing.model.Car;
import mate.sep23.group3.car.sharing.model.Rental;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.repository.CarRepository;
import mate.sep23.group3.car.sharing.repository.RentalRepository;
import mate.sep23.group3.car.sharing.repository.UserRepository;
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

    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RentalResponseDto save(RentalRequestDto requestDto, Long userId) {
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
        }
        Car car = rental.getCar();
        car.setInventory(car.getInventory() + 1);
        rental.setIsActive(false);
        carRepository.save(car);
        return rentalMapper.toDto(rentalRepository.save(rental));
    }

    private Rental setUpRental(RentalRequestDto requestDto, Long userId) {
        Rental rental = new Rental();
        rental.setRentalDate(LocalDateTime.now());
        rental.setReturnDate(requestDto.getReturnDate());

        Car car = carRepository.findById(requestDto.getCarId()).orElseThrow(
                () -> new EntityNotFoundException(
                        CAN_NOT_FIND_CAR_BY_ID_MESSAGE + requestDto.getCarId()
                )
        );

        int carInventory = car.getInventory();
        if (carInventory > 0) {
            car.setInventory(carInventory - 1);
            rental.setCar(car);

            User user = userRepository.findById(userId).orElseThrow(
                    () -> new EntityNotFoundException(CAN_NOT_FIND_USER_BY_ID_MESSAGE + userId)
            );

            rental.setUser(user);
            rental.setIsActive(true);
            return rental;
        } else {
            throw new RuntimeException(CAR_INVENTORY_IS_EMPTY_MESSAGE);
        }
    }
}
