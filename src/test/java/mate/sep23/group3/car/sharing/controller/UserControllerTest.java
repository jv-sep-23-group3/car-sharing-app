package mate.sep23.group3.car.sharing.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.util.Set;
import mate.sep23.group3.car.sharing.dto.role.RoleUpdateForUserRequestDto;
import mate.sep23.group3.car.sharing.dto.user.UserResponseDto;
import mate.sep23.group3.car.sharing.dto.user.email.UserUpdateEmailRequestDto;
import mate.sep23.group3.car.sharing.dto.user.email.UserUpdateEmailResponseDto;
import mate.sep23.group3.car.sharing.dto.user.password.UserUpdatePasswordRequestDto;
import mate.sep23.group3.car.sharing.dto.user.profile.UserWithNameAndLastNameRequestDto;
import mate.sep23.group3.car.sharing.dto.user.profile.UserWithNameAndLastNameResponseDto;
import mate.sep23.group3.car.sharing.model.Role;
import mate.sep23.group3.car.sharing.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = {"classpath:database/users/add-user-to-users-table.sql",
        "classpath:database/users_roles/add-users-roles-to-users-roles-table.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:database/users_roles/delete-users-roles-from-users-roles-table.sql",
        "classpath:database/users/delete-user-from-users-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    protected static MockMvc mockMvc;
    private static final Long INDEX_OF_UPDATING_USER = 3L;
    private static Role userRole;
    private static User user;
    private static UserResponseDto userResponseDto;
    private static UserUpdateEmailRequestDto updateEmailRequestDto;
    private static UserUpdateEmailRequestDto notValidUpdateEmailRequestDto;
    private static UserUpdateEmailResponseDto updateEmailResponseDto;
    private static UserUpdatePasswordRequestDto updatePasswordRequestDto;
    private static UserUpdatePasswordRequestDto notMatchRepeatPasswordUpdateRequestDto;
    private static UserUpdatePasswordRequestDto passwordIsShortUpdateRequestDto;
    private static UserWithNameAndLastNameRequestDto userWithNameAndLastNameRequestDto;
    private static UserWithNameAndLastNameResponseDto userWithNameAndLastNameResponseDto;
    private static RoleUpdateForUserRequestDto roleUpdateForUserRequestDto;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

        userRole = new Role();
        userRole.setId(1L);
        userRole.setRoleName(Role.RoleName.MANAGER);

        user = new User();
        user.setId(1L);
        user.setEmail("admin@email.com");
        user.setPassword("$2a$12$k/TROQT1JX4fZDIOH4xOA.SEDPJWz.oc9Dy.9IrPxU4ieC9u3Yn7i");
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setRoles(Set.of(userRole));

        userResponseDto = new UserResponseDto()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName());

        updateEmailRequestDto = new UserUpdateEmailRequestDto("update@email.com");

        updateEmailResponseDto = new UserUpdateEmailResponseDto(
                user.getId(), "update@email.com");

        notValidUpdateEmailRequestDto = new UserUpdateEmailRequestDto("update.com");

        updatePasswordRequestDto = new UserUpdatePasswordRequestDto();
        updatePasswordRequestDto.setPassword("12345678");
        updatePasswordRequestDto.setRepeatPassword("12345678");

        notMatchRepeatPasswordUpdateRequestDto = new UserUpdatePasswordRequestDto();
        notMatchRepeatPasswordUpdateRequestDto.setPassword("12345678");
        notMatchRepeatPasswordUpdateRequestDto.setRepeatPassword("1234567890");

        passwordIsShortUpdateRequestDto = new UserUpdatePasswordRequestDto();
        passwordIsShortUpdateRequestDto.setPassword("123456");
        passwordIsShortUpdateRequestDto.setRepeatPassword("123456");

        userWithNameAndLastNameRequestDto = new UserWithNameAndLastNameRequestDto();
        userWithNameAndLastNameRequestDto.setFirstName("Changed Name");
        userWithNameAndLastNameRequestDto.setLastName("Changed LastName");

        userWithNameAndLastNameResponseDto = new UserWithNameAndLastNameResponseDto();
        userWithNameAndLastNameResponseDto.setId(user.getId());
        userWithNameAndLastNameResponseDto.setFirstName(
                userWithNameAndLastNameRequestDto.getFirstName());
        userWithNameAndLastNameResponseDto.setLastName(
                userWithNameAndLastNameRequestDto.getLastName());

        roleUpdateForUserRequestDto = new RoleUpdateForUserRequestDto();
        roleUpdateForUserRequestDto.setRoles(Set.of(1L,2L));
    }

    @BeforeEach
    void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @WithMockUser(username = "user", roles = {"CUSTOMER", "MANAGER", "ADMIN"})
    @Test
    @DisplayName("Get my profile info")
    void getProfile_ExistingId_Success() throws Exception {
        MvcResult result = mockMvc.perform(
                            get("/users/me"))
                    .andExpect(status().isOk())
                    .andReturn();

        UserResponseDto actual = objectMapper.readValue(result.getResponse()
                    .getContentAsString(), UserResponseDto.class);

        assertNotNull(actual);
        assertEquals(userResponseDto, actual);
    }

    @WithMockUser(username = "user", roles = {"CUSTOMER", "MANAGER", "ADMIN"})
    @Test
    @DisplayName("Update user's email")
    void updateEmail_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(updateEmailRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/users/update-email")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserUpdateEmailResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), UserUpdateEmailResponseDto.class);

        assertNotNull(actual);
        assertEquals(updateEmailResponseDto, actual);
    }

    @WithMockUser(username = "user", roles = {"CUSTOMER", "MANAGER", "ADMIN"})
    @Test
    @DisplayName("Update user's email, not valid email")
    void updateEmail_NotValidRequestDto_BadRequest() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(notValidUpdateEmailRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/users/update-email")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @WithMockUser(username = "user", roles = {"CUSTOMER", "MANAGER", "ADMIN"})
    @Test
    @DisplayName("Update user's password")
    void updatePassword_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(updatePasswordRequestDto);

        mockMvc.perform(
                        put("/users/update-password")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isResetContent());
    }

    @WithMockUser(username = "user", roles = {"CUSTOMER", "MANAGER", "ADMIN"})
    @Test
    @DisplayName("Update user's password, repeat password doesn't match")
    void updatePassword_RepeatPasswordDoesNotMatch_BadRequest() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(
                notMatchRepeatPasswordUpdateRequestDto);

        mockMvc.perform(
                        put("/users/update-password")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user", roles = {"CUSTOMER", "MANAGER", "ADMIN"})
    @Test
    @DisplayName("Update user's password, password is short")
    void updatePassword_PasswordIsShort_BadRequest() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(passwordIsShortUpdateRequestDto);

        mockMvc.perform(
                        put("/users/update-password")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user", roles = {"CUSTOMER", "MANAGER", "ADMIN"})
    @Test
    @DisplayName("Update user's profile")
    void updateProfile_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(userWithNameAndLastNameRequestDto);

        MvcResult result = mockMvc.perform(
                        patch("/users/me")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isAccepted())
                .andReturn();

        UserWithNameAndLastNameResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), UserWithNameAndLastNameResponseDto.class);

        assertNotNull(actual);
        assertEquals(userWithNameAndLastNameResponseDto, actual);
    }

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test
    @DisplayName("Update user's roles")
    void updateRole_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(roleUpdateForUserRequestDto);

        //        MvcResult result = mockMvc.perform(
        //                        patch("/users/{id}/role", INDEX_OF_UPDATING_USER)
        //                                .content(jsonRequest)
        //                                .contentType(MediaType.APPLICATION_JSON)
        //                )
        //                .andExpect(status().isOk())
        //                .andReturn();

        //        UserWithNameAndLastNameResponseDto actual = objectMapper
        //        .readValue(result.getResponse()
        //                .getContentAsString(), UserWithNameAndLastNameResponseDto.class);
        //
        //        assertNotNull(actual);
        //        assertEquals(userWithNameAndLastNameResponseDto, actual);
    }
}
