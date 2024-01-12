package mate.sep23.group3.car.sharing.mapper;

import mate.sep23.group3.car.sharing.config.MapperConfig;
import mate.sep23.group3.car.sharing.dto.rental.RentalResponseDto;
import mate.sep23.group3.car.sharing.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {
    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "userId", source = "user.id")
    RentalResponseDto toDto(Rental rental);
}
