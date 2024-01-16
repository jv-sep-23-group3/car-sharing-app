package mate.sep23.group3.car.sharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.role.RoleUpdateForUserRequestDto;
import mate.sep23.group3.car.sharing.dto.user.UserResponseDto;
import mate.sep23.group3.car.sharing.dto.user.email.UserUpdateEmailRequestDto;
import mate.sep23.group3.car.sharing.dto.user.email.UserUpdateEmailResponseDto;
import mate.sep23.group3.car.sharing.dto.user.password.UserUpdatePasswordRequestDto;
import mate.sep23.group3.car.sharing.dto.user.profile.UserWithNameAndLastNameRequestDto;
import mate.sep23.group3.car.sharing.dto.user.profile.UserWithNameAndLastNameResponseDto;
import mate.sep23.group3.car.sharing.dto.user.role.UserWithRoleResponseDto;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users management", description = "Endpoints for managing users")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}/role")
    @Operation(summary = "Update the role of a user by ID",
            description = "Only the administrator can update roles for a user")
    public UserWithRoleResponseDto updateRole(
            @PathVariable("id") Long userId,
            @RequestBody @Valid RoleUpdateForUserRequestDto requestDto) {
        return userService.updateRole(userId, requestDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CUSTOMER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Update profile",
            description = "You can update your first and last name")
    @PatchMapping("/me")
    UserWithNameAndLastNameResponseDto updateProfile(
            Authentication authentication,
            @RequestBody @Valid UserWithNameAndLastNameRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return userService.updateProfile(user.getId(), requestDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CUSTOMER')")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    @Operation(summary = "Update your password",
            description = "You need to enter a password and confirm it")
    @PutMapping("/update-password")
     void updatePassword(
             Authentication authentication,
             @RequestBody @Valid UserUpdatePasswordRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        userService.updatePassword(user.getId(), requestDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CUSTOMER')")
    @Operation(summary = "Update email", description = "Update email for your account")
    @PutMapping("/update-email")
    UserUpdateEmailResponseDto updateEmail(
            Authentication authentication,
            @RequestBody @Valid UserUpdateEmailRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return userService.updateEmail(user.getId(), requestDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CUSTOMER')")
    @Operation(summary = "Get profile",
            description = "you receive your profile with an ID, email, first and last name")
    @GetMapping("/me")
    UserResponseDto getProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.getProfile(user.getEmail());
    }
}
