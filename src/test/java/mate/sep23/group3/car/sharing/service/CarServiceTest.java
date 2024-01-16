package mate.sep23.group3.car.sharing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import mate.sep23.group3.car.sharing.dto.car.CarRequestDto;
import mate.sep23.group3.car.sharing.dto.car.CarResponseDto;
import mate.sep23.group3.car.sharing.exception.EntityNotFoundException;
import mate.sep23.group3.car.sharing.mapper.CarMapper;
import mate.sep23.group3.car.sharing.model.Car;
import mate.sep23.group3.car.sharing.repository.CarRepository;
import mate.sep23.group3.car.sharing.service.impl.CarServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {
    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 10L;
    private static Car car;
    private static Car savedCar;
    private static CarRequestDto requestDto;
    private static CarRequestDto updateRequestDto;
    private static CarResponseDto responseDto;
    private static CarResponseDto updatedResponseDto;
    private static Car carBeforeUpdating;
    private static Car updatedCar;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carServiceImpl;

    @BeforeAll
    static void beforeAll() {
        car = new Car()
                .setBrand("BMW")
                .setModel("X5")
                .setType(Car.Type.SUV)
                .setInventory(3)
                .setDailyFee(BigDecimal.valueOf(100));

        carBeforeUpdating = new Car()
                .setBrand("BMW")
                .setModel("E39")
                .setType(Car.Type.SEDAN)
                .setInventory(5)
                .setDailyFee(BigDecimal.valueOf(60));

        savedCar = new Car()
                .setId(1L)
                .setBrand("BMW")
                .setModel("X5")
                .setType(Car.Type.SUV)
                .setInventory(3)
                .setDailyFee(BigDecimal.valueOf(100));

        updatedCar = new Car()
                .setId(1L)
                .setBrand("BMW")
                .setModel("E39")
                .setType(Car.Type.SEDAN)
                .setInventory(5)
                .setDailyFee(BigDecimal.valueOf(60));

        requestDto = new CarRequestDto()
                .setBrand("BMW")
                .setModel("X5")
                .setType(Car.Type.SUV)
                .setInventory(3)
                .setDailyFee(BigDecimal.valueOf(100));

        updateRequestDto = new CarRequestDto()
                .setBrand("BMW")
                .setModel("E39")
                .setType(Car.Type.SEDAN)
                .setInventory(5)
                .setDailyFee(BigDecimal.valueOf(60));

        responseDto = new CarResponseDto()
                .setId(1L)
                .setBrand("BMW")
                .setModel("X5")
                .setType(Car.Type.SUV)
                .setInventory(3)
                .setDailyFee(BigDecimal.valueOf(100));

        updatedResponseDto = new CarResponseDto()
                .setId(1L)
                .setBrand("BMW")
                .setModel("E39")
                .setType(Car.Type.SEDAN)
                .setInventory(5)
                .setDailyFee(BigDecimal.valueOf(60));
    }

    @Test
    @DisplayName("Get all cars")
    public void getAll_OneCar_ReturnListOfCarResponseDto() {
        PageRequest pageRequest = PageRequest.of(0,20);
        when(carRepository.findAll(pageRequest))
                .thenReturn(new PageImpl<>(List.of(savedCar)));
        when(carMapper.toDto(savedCar)).thenReturn(responseDto);

        List<CarResponseDto> expected = List.of(responseDto);
        List<CarResponseDto> actual = carServiceImpl.getAll(pageRequest);
        assertTrue(CollectionUtils.isEqualCollection(expected, actual));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    @DisplayName("Get a car by existing id")
    public void getById_ExistingId_ReturnCarResponseDto() {
        when(carRepository.findById(EXISTING_ID))
                .thenReturn(Optional.of(savedCar));
        when(carMapper.toDto(savedCar)).thenReturn(responseDto);

        CarResponseDto actual = carServiceImpl.getById(EXISTING_ID);
        assertEquals(responseDto, actual);
    }

    @Test
    @DisplayName("Get a car by not existing id")
    public void getById_NotExistingId_EntityNotFoundExceptionExpected() {
        when(carRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> carServiceImpl.getById(NOT_EXISTING_ID)
        );

        String expectedMessage = "Can't find car by id: " + NOT_EXISTING_ID;
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Add a new car")
    void add_ValidCarRequestDto_ReturnValidCarResponseDto() {
        when(carRepository.save(car)).thenReturn(savedCar);
        when(carMapper.toModel(requestDto)).thenReturn(car);
        when(carMapper.toDto(savedCar)).thenReturn(responseDto);

        CarResponseDto actual = carServiceImpl.add(requestDto);

        assertEquals(responseDto, actual);
    }

    @Test
    @DisplayName("Update a car by existing id")
    public void update_ExistingId_ReturnCarResponseDto() {
        when(carRepository.findById(EXISTING_ID))
                .thenReturn(Optional.of(savedCar));
        when(carRepository.save(carBeforeUpdating)).thenReturn(updatedCar);
        when(carMapper.toModel(updateRequestDto)).thenReturn(carBeforeUpdating);
        when(carMapper.toDto(updatedCar)).thenReturn(updatedResponseDto);

        CarResponseDto actual = carServiceImpl.update(EXISTING_ID, updateRequestDto);
        assertEquals(updatedResponseDto, actual);
    }

    @Test
    @DisplayName("Update a car by not existing id")
    public void update_NotExistingId_EntityNotFoundExceptionExpected() {
        when(carRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> carServiceImpl.update(NOT_EXISTING_ID, updateRequestDto)
        );

        String expectedMessage = "Can't find and update car by id: " + NOT_EXISTING_ID;
        assertEquals(expectedMessage, exception.getMessage());
    }
}
