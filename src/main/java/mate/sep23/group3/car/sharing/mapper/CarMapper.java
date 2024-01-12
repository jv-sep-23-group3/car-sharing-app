package mate.sep23.group3.car.sharing.mapper;

import mate.sep23.group3.car.sharing.config.MapperConfig;
import mate.sep23.group3.car.sharing.dto.car.CarRequestDto;
import mate.sep23.group3.car.sharing.dto.car.CarResponseDto;
import mate.sep23.group3.car.sharing.model.Car;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CarMapper {
    CarResponseDto toDto(Car car);

    Car toEntity(CarRequestDto requestDto);
}
