package mate.sep23.group3.car.sharing.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.car.CarRequestDto;
import mate.sep23.group3.car.sharing.dto.car.CarResponseDto;
import mate.sep23.group3.car.sharing.exception.EntityNotFoundException;
import mate.sep23.group3.car.sharing.mapper.CarMapper;
import mate.sep23.group3.car.sharing.model.Car;
import mate.sep23.group3.car.sharing.repository.CarRepository;
import mate.sep23.group3.car.sharing.service.CarService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private static final String CAN_NOT_FIND_CAR_BY_ID_MESSAGE = "Can't find car by id: ";
    private static final String CAN_NOT_FIND_AND_UPDATE_CAR_BY_ID_MESSAGE
            = "Can't find and update car by id: ";
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public List<CarResponseDto> getAll(Pageable pageable) {
        return carRepository.findAll(pageable).stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    public CarResponseDto getById(Long id) {
        return carMapper.toDto(carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(CAN_NOT_FIND_CAR_BY_ID_MESSAGE + id)
        ));
    }

    @Override
    public CarResponseDto add(CarRequestDto requestDto) {
        return carMapper.toDto(carRepository.save(carMapper.toModel(requestDto)));
    }

    @Override
    public CarResponseDto update(Long id, CarRequestDto requestDto) {
        carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(CAN_NOT_FIND_AND_UPDATE_CAR_BY_ID_MESSAGE + id)
        );
        Car updatedCar = carMapper.toModel(requestDto);
        updatedCar.setId(id);
        return carMapper.toDto(carRepository.save(updatedCar));
    }

    @Override
    public void delete(Long id) {
        carRepository.deleteById(id);
    }
}
