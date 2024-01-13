package mate.sep23.group3.car.sharing.service.impl;

import java.math.BigDecimal;
import java.util.Optional;
import mate.sep23.group3.car.sharing.dto.car.CarResponseDto;
import mate.sep23.group3.car.sharing.model.Car;
import mate.sep23.group3.car.sharing.repository.CarRepository;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carServiceImpl;

    @Test
    void getAll() {
    }

    @Test
    @DisplayName("""
            Verify the correct car is returned by id
            """)
    void getById_ValidCarId_ReturnValidCar() {
        // Given
        Long carId = 1L;
        Car car = new Car();
        car.setId(carId);
        car.setModel("Civic");
        car.setBrand("Honda");
        car.setType(Car.Type.HATCHBACK);
        car.setInventory(15);
        car.setDailyFee(BigDecimal.valueOf(100));

        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // When
        CarResponseDto actual = carServiceImpl.getById(carId);

        // Then
        CarResponseDto expected = new CarResponseDto();
        expected.setId(carId);
        expected.setModel(car.getModel());
        expected.setBrand(car.getBrand());
        expected.setType(car.getType());
        expected.setInventory(car.getInventory());
        expected.setDailyFee(car.getDailyFee());

        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}