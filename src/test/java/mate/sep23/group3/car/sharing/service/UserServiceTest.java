package mate.sep23.group3.car.sharing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.sep23.group3.car.sharing.dto.role.RoleUpdateForUserRequestDto;
import mate.sep23.group3.car.sharing.dto.user.UserResponseDto;
import mate.sep23.group3.car.sharing.dto.user.email.UserUpdateEmailRequestDto;
import mate.sep23.group3.car.sharing.dto.user.email.UserUpdateEmailResponseDto;
import mate.sep23.group3.car.sharing.dto.user.password.UserUpdatePasswordRequestDto;
import mate.sep23.group3.car.sharing.dto.user.profile.UserWithNameAndLastNameRequestDto;
import mate.sep23.group3.car.sharing.dto.user.profile.UserWithNameAndLastNameResponseDto;
import mate.sep23.group3.car.sharing.dto.user.registration.UserRegistrationRequestDto;
import mate.sep23.group3.car.sharing.dto.user.role.UserWithRoleResponseDto;
import mate.sep23.group3.car.sharing.exception.EntityNotFoundException;
import mate.sep23.group3.car.sharing.mapper.UserMapper;
import mate.sep23.group3.car.sharing.model.Role;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.repository.RoleRepository;
import mate.sep23.group3.car.sharing.repository.UserRepository;
import mate.sep23.group3.car.sharing.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 100L;
    private static final Long NEW_ROLE_INDEX = 1L;
    private static UserRegistrationRequestDto registrationRequestDto;
    private static UserResponseDto responseDto;
    private static UserWithRoleResponseDto userWithRoleResponseDto;
    private static RoleUpdateForUserRequestDto roleUpdateForUserRequestDto;
    private static Role userRole;
    private static Role newUserRole;
    private static User user;
    private static UserWithNameAndLastNameRequestDto userWithNameAndLastNameRequestDto;
    private static UserWithNameAndLastNameResponseDto userWithNameAndLastNameResponseDto;
    private static UserUpdatePasswordRequestDto updatePasswordRequestDto;
    private static UserUpdateEmailRequestDto updateEmailRequestDto;
    private static UserUpdateEmailResponseDto updateEmailResponseDto;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl useServiceImpl;

    @BeforeAll
    static void beforeAll() {
        userRole = new Role();
        userRole.setId(3L);
        userRole.setRoleName(Role.RoleName.ADMIN);

        newUserRole = new Role();
        newUserRole.setId(NEW_ROLE_INDEX);
        newUserRole.setRoleName(Role.RoleName.MANAGER);

        roleUpdateForUserRequestDto = new RoleUpdateForUserRequestDto();
        roleUpdateForUserRequestDto.setRoles(Set.of(newUserRole.getId()));

        user = new User();
        user.setId(1L);
        user.setEmail("admin@email.com");
        user.setPassword("$2a$12$k/TROQT1JX4fZDIOH4xOA.SEDPJWz.oc9Dy.9IrPxU4ieC9u3Yn7i");
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.getRoles().add(userRole);

        registrationRequestDto = new UserRegistrationRequestDto()
                .setEmail("user@email.com")
                .setPassword("12345678")
                .setRepeatPassword("12345678")
                .setFirstName("User")
                .setLastName("User");

        responseDto = new UserResponseDto()
                .setId(1L)
                .setEmail(registrationRequestDto.getEmail())
                .setFirstName(registrationRequestDto.getFirstName())
                .setLastName(registrationRequestDto.getLastName());

        userWithRoleResponseDto = new UserWithRoleResponseDto();
        userWithRoleResponseDto.setId(user.getId());
        userWithRoleResponseDto.setRoles(Set.of(newUserRole.getId()));

        userWithNameAndLastNameRequestDto = new UserWithNameAndLastNameRequestDto();
        userWithNameAndLastNameRequestDto.setFirstName("Change name");
        userWithNameAndLastNameRequestDto.setLastName("Change LastName");

        userWithNameAndLastNameResponseDto = new UserWithNameAndLastNameResponseDto();
        userWithNameAndLastNameResponseDto.setId(user.getId());
        userWithNameAndLastNameResponseDto.setFirstName("Change name");
        userWithNameAndLastNameResponseDto.setLastName("Change LastName");

        updatePasswordRequestDto = new UserUpdatePasswordRequestDto();
        updatePasswordRequestDto.setPassword("12121212");
        updatePasswordRequestDto.setRepeatPassword("12121212");

        updateEmailRequestDto = new UserUpdateEmailRequestDto(
                "changed_email@email.com"
        );

        updateEmailResponseDto = new UserUpdateEmailResponseDto(
                user.getId(), updateEmailRequestDto.email()
        );

    }

    @Test
    @DisplayName("Update role")
    public void updateRole_ExistingUserId_ReturnUserWithRoleResponseDto() {
        when(userRepository.findById(EXISTING_ID))
                .thenReturn(Optional.of(user));
        when(roleRepository.findById(NEW_ROLE_INDEX)).thenReturn(Optional.of(newUserRole));
        when(userMapper.toUserWithRole(user)).thenReturn(userWithRoleResponseDto);

        UserWithRoleResponseDto actual = useServiceImpl.updateRole(user.getId(),
                roleUpdateForUserRequestDto);
        assertEquals(userWithRoleResponseDto, actual);
    }

    @Test
    @DisplayName("Update role, not existing user id")
    public void updateRole_NotExistingUserId_EntityNotFoundExceptionExpected() {
        when(userRepository.findById(NOT_EXISTING_ID))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> useServiceImpl.updateRole(NOT_EXISTING_ID,
                        roleUpdateForUserRequestDto)
        );

        String expectedMessage = "Can't find user by ID " + NOT_EXISTING_ID;
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Update profile")
    public void updateProfile_ReturnUserWithNameAndLastNameResponseDto() {
        when(userRepository.findRolesInUser(user.getId()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toWithNameAndLastNameResponse(user))
                .thenReturn(userWithNameAndLastNameResponseDto);

        UserWithNameAndLastNameResponseDto actual = useServiceImpl.updateProfile(user.getId(),
                userWithNameAndLastNameRequestDto);
        assertEquals(userWithNameAndLastNameResponseDto, actual);
    }

    @Test
    @DisplayName("Update password")
    public void updatePassword_ValidUpdateDto_ReturnUserResponseDto() {
        when(userRepository.findRolesInUser(user.getId()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.encode(updatePasswordRequestDto.getPassword()))
                .thenReturn("Encoded_password");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(responseDto);

        UserResponseDto actual = useServiceImpl.updatePassword(user.getId(),
                updatePasswordRequestDto);
        assertEquals(responseDto, actual);
    }

    @Test
    @DisplayName("Update email")
    public void updateEmail_ValidUpdateDto_ReturnUserUpdateEmailResponseDto() {
        when(userRepository.findRolesInUser(user.getId()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDtoEmail(user)).thenReturn(updateEmailResponseDto);

        UserUpdateEmailResponseDto actual = useServiceImpl.updateEmail(user.getId(),
                updateEmailRequestDto);
        assertEquals(updateEmailResponseDto, actual);
    }

    @Test
    @DisplayName("Get profile info")
    public void getProfile_EmailExist_ReturnUserResponseDto() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(responseDto);

        UserResponseDto actual = useServiceImpl.getProfile(user.getEmail());
        assertEquals(responseDto, actual);
    }

    @Test
    @DisplayName("Get profile info, email doesn't exist")
    public void getProfile_EmailNotExist_EntityNotFoundExceptionExpected() {
        String notExistingEmail = "not_existing@email.com";

        when(userRepository.findByEmail(notExistingEmail))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> useServiceImpl.getProfile(notExistingEmail)
        );

        String expectedMessage = "Can't find user by email " + notExistingEmail;
        assertEquals(expectedMessage, exception.getMessage());
    }
}
