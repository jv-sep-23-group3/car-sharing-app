package mate.sep23.group3.car.sharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.user.UserLoginRequestDto;
import mate.sep23.group3.car.sharing.dto.user.UserLoginResponseDto;
import mate.sep23.group3.car.sharing.dto.user.UserRegistrationRequestDto;
import mate.sep23.group3.car.sharing.dto.user.UserResponseDto;
import mate.sep23.group3.car.sharing.exception.RegistrationException;
import mate.sep23.group3.car.sharing.security.AuthenticationService;
import mate.sep23.group3.car.sharing.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication management",
        description = "Endpoints for authentication and authorization")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Login to a user account",
            description = "Get a JWT token to a certain account")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/registration")
    @Operation(summary = "Register a new user",
            description = "Add a new user with USER roleName to database")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
