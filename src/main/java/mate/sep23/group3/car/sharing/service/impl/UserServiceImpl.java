package mate.sep23.group3.car.sharing.service.impl;

import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.user.UserRegistrationRequestDto;
import mate.sep23.group3.car.sharing.dto.user.UserResponseDto;
import mate.sep23.group3.car.sharing.exception.RegistrationException;
import mate.sep23.group3.car.sharing.mapper.UserMapper;
import mate.sep23.group3.car.sharing.model.Role;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.repository.RoleRepository;
import mate.sep23.group3.car.sharing.repository.UserRepository;
import mate.sep23.group3.car.sharing.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String CAN_NOT_REGISTER_USER_BY_EMAIL = "Can't register a user by email:";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
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
}
