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
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public List<CarResponseDto> getAll(Pageable pageable) {
        return carRepository.findAll(pageable).stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    public CarResponseDto getCarById(Long id) {
        return carMapper.toDto(carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find car by id: " + id)
        ));
    }

    @Override
    public CarResponseDto addCar(CarRequestDto requestDto) {
        return carMapper.toDto(carRepository.save(carMapper.toEntity(requestDto)));
    }

    @Override
    public CarResponseDto updateCar(Long id, CarRequestDto requestDto) {
        Car updatedCar = carMapper.toEntity(requestDto);
        updatedCar.setId(id);
        return carMapper.toDto(carRepository.save(updatedCar));
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}
