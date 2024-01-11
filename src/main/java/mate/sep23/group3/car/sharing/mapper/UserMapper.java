package mate.sep23.group3.car.sharing.mapper;

import mate.sep23.group3.car.sharing.config.MapperConfig;
import mate.sep23.group3.car.sharing.dto.user.UserRegistrationRequestDto;
import mate.sep23.group3.car.sharing.dto.user.UserResponseDto;
import mate.sep23.group3.car.sharing.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto requestDto);

    UserResponseDto toDto(User user);
}
