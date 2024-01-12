package mate.sep23.group3.car.sharing.service;

import java.util.List;
import mate.sep23.group3.car.sharing.dto.car.CarRequestDto;
import mate.sep23.group3.car.sharing.dto.car.CarResponseDto;
import org.springframework.data.domain.Pageable;

public interface CarService {
    List<CarResponseDto> getAll(Pageable pageable);

    CarResponseDto getById(Long id);

    CarResponseDto add(CarRequestDto requestDto);

    CarResponseDto update(Long id, CarRequestDto requestDto);

    void delete(Long id);
}
