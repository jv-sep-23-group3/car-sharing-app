package mate.sep23.group3.car.sharing.dto.user.login;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(
        @NotBlank
        @Length(min = 8, max = 24)
        String email,
        @NotBlank
        @Length(min = 8, max = 24)
        String password
) {
}
