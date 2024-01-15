package mate.sep23.group3.car.sharing.service.impl;

import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.role.RoleUpdateForUserRequestDto;
import mate.sep23.group3.car.sharing.dto.user.profile.UserWithNameAndLastNameRequestDto;
import mate.sep23.group3.car.sharing.dto.user.profile.UserWithNameAndLastNameResponseDto;
import mate.sep23.group3.car.sharing.dto.user.registration.UserRegistrationRequestDto;
import mate.sep23.group3.car.sharing.dto.user.UserResponseDto;
import mate.sep23.group3.car.sharing.dto.user.email.UserUpdateEmailRequestDto;
import mate.sep23.group3.car.sharing.dto.user.email.UserUpdateEmailResponseDto;
import mate.sep23.group3.car.sharing.dto.user.password.UserUpdatePasswordRequestDto;
import mate.sep23.group3.car.sharing.dto.user.role.UserWithRoleRequestDto;
import mate.sep23.group3.car.sharing.exception.EntityNotFoundException;
import mate.sep23.group3.car.sharing.exception.RegistrationException;
import mate.sep23.group3.car.sharing.mapper.UserMapper;
import mate.sep23.group3.car.sharing.model.Role;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.repository.RoleRepository;
import mate.sep23.group3.car.sharing.repository.UserRepository;
import mate.sep23.group3.car.sharing.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String CAN_NOT_REGISTER_USER_BY_EMAIL = "Can't register a user by email:";
    private static final String FAILED_FIND_USER = "Can't find user by ID";
    private static final String FAILED_FIND_ROLE = "Can't find role by ID";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private Role.RoleName roleName;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException(CAN_NOT_REGISTER_USER_BY_EMAIL + requestDto.getEmail());
        }
        User userSave = userMapper.toModel(requestDto);
        Role role = roleRepository.findByRoleName(Role.RoleName.CUSTOMER);
        userSave.getRoles().add(role);
        userSave.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        return userMapper.toDto(userRepository.save(userSave));
    }

    @Override
    public UserWithRoleRequestDto updateRole(Long userId, RoleUpdateForUserRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FAILED_FIND_USER + userId));
        user.getRoles().clear();
        Set<Role> newRoles = requestDto.getRoles().stream()
                .map(roleIndex -> roleRepository.findById(roleIndex)
                        .orElseThrow(() -> new EntityNotFoundException(FAILED_FIND_ROLE + roleIndex)))
                .collect(Collectors.toSet());
        user.getRoles().addAll(newRoles);
        userRepository.save(user);
        return userMapper.toUserWithRole(user);
    }

    @Override
    public UserWithNameAndLastNameResponseDto updateProfile(Long userId, UserWithNameAndLastNameRequestDto requestDto) {
        User userUpdateProfile = userRepository.findRolesInUser(userId)
                .orElseThrow(() -> new EntityNotFoundException(FAILED_FIND_USER + userId));
        if (!requestDto.getFirstName().isBlank()) {
            userUpdateProfile.setFirstName(requestDto.getFirstName());
        }
        if (!requestDto.getLastName().isBlank()) {
            userUpdateProfile.setLastName(requestDto.getLastName());
        }
        return userMapper.toWithNameAndLastNameResponse(userRepository.save(userUpdateProfile));
    }

    @Override
    public UserResponseDto updatePassword(Long userId,
                                          UserUpdatePasswordRequestDto requestDto) {
        User userSave = userRepository.findRolesInUser(userId)
                .orElseThrow(() -> new EntityNotFoundException(FAILED_FIND_USER + userId));
        userSave.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        return userMapper.toDto(userRepository.save(userSave));
    }

    @Override
    public UserUpdateEmailResponseDto updateEmail(Long userId,
                                                  UserUpdateEmailRequestDto requestDto) {
        User user = userRepository.findRolesInUser(userId)
                .orElseThrow(() -> new EntityNotFoundException(FAILED_FIND_USER + userId));
        user.setEmail(requestDto.email());
        userRepository.save(user);
        return userMapper.toDtoEmail(user);
    }

    @Override
    public UserResponseDto getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(FAILED_FIND_USER + email));
        return userMapper.toDto(user);
    }
}
