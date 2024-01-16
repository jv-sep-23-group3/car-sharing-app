package mate.sep23.group3.car.sharing.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.sep23.group3.car.sharing.config.MapperConfig;
import mate.sep23.group3.car.sharing.dto.user.UserResponseDto;
import mate.sep23.group3.car.sharing.dto.user.email.UserUpdateEmailResponseDto;
import mate.sep23.group3.car.sharing.dto.user.profile.UserWithNameAndLastNameResponseDto;
import mate.sep23.group3.car.sharing.dto.user.registration.UserRegistrationRequestDto;
import mate.sep23.group3.car.sharing.dto.user.role.UserWithRoleResponseDto;
import mate.sep23.group3.car.sharing.model.Role;
import mate.sep23.group3.car.sharing.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto requestDto);

    UserResponseDto toDto(User user);

    UserUpdateEmailResponseDto toDtoEmail(User user);

    UserWithNameAndLastNameResponseDto toWithNameAndLastNameResponse(User user);

    @Mapping(target = "roles", source = "user.roles", qualifiedByName = "mapRolesToIds")
    UserWithRoleResponseDto toUserWithRole(User user);

    @Named("mapRolesToIds")
    default Set<Long> mapRolesToIds(Set<Role> roles) {
        return roles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
    }
}
