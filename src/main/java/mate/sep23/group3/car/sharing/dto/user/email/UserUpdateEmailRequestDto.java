package mate.sep23.group3.car.sharing.dto.user.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateEmailRequestDto(
        @Email
        @NotBlank
        String email
) {
}
