package mate.sep23.group3.car.sharing.mapper;

import mate.sep23.group3.car.sharing.config.MapperConfig;
import mate.sep23.group3.car.sharing.dto.user.profile.UserWithNameAndLastNameResponseDto;
import mate.sep23.group3.car.sharing.dto.user.registration.UserRegistrationRequestDto;
import mate.sep23.group3.car.sharing.dto.user.UserResponseDto;
import mate.sep23.group3.car.sharing.dto.user.email.UserUpdateEmailResponseDto;
import mate.sep23.group3.car.sharing.dto.user.role.UserWithRoleRequestDto;
import mate.sep23.group3.car.sharing.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto requestDto);

    UserResponseDto toDto(User user);

    UserUpdateEmailResponseDto toDtoEmail(User user);

    UserWithNameAndLastNameResponseDto toWithNameAndLastNameResponse(User user);

    UserWithRoleRequestDto toUserWithRole(User user);
}
