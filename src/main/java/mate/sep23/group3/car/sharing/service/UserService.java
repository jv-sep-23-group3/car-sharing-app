package mate.sep23.group3.car.sharing.service;

import mate.sep23.group3.car.sharing.dto.role.RoleUpdateForUserRequestDto;
import mate.sep23.group3.car.sharing.dto.user.profile.UserWithNameAndLastNameRequestDto;
import mate.sep23.group3.car.sharing.dto.user.profile.UserWithNameAndLastNameResponseDto;
import mate.sep23.group3.car.sharing.dto.user.registration.UserRegistrationRequestDto;
import mate.sep23.group3.car.sharing.dto.user.UserResponseDto;
import mate.sep23.group3.car.sharing.dto.user.email.UserUpdateEmailRequestDto;
import mate.sep23.group3.car.sharing.dto.user.email.UserUpdateEmailResponseDto;
import mate.sep23.group3.car.sharing.dto.user.password.UserUpdatePasswordRequestDto;
import mate.sep23.group3.car.sharing.dto.user.role.UserWithRoleRequestDto;
import mate.sep23.group3.car.sharing.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    UserWithRoleRequestDto updateRole(Long userId, RoleUpdateForUserRequestDto requestDto);

    UserUpdateEmailResponseDto updateEmail(Long userId, UserUpdateEmailRequestDto requestDto);

    UserResponseDto getProfile(String email);
    UserResponseDto updatePassword(Long userId, UserUpdatePasswordRequestDto requestDto);

    UserWithNameAndLastNameResponseDto updateProfile(Long id, UserWithNameAndLastNameRequestDto requestDto);
}
