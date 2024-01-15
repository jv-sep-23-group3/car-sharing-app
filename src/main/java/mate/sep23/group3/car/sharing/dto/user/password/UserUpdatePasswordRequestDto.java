package mate.sep23.group3.car.sharing.dto.user.password;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.sep23.group3.car.sharing.validation.FieldMatchChangePassword;
import org.hibernate.validator.constraints.Length;

@FieldMatchChangePassword
@Data
public class UserUpdatePasswordRequestDto {
    private static final String NOT_BE_NULL = "Can't be null";
    private static final String FROM_8_TO_24_CHARACTERS = "Must contain 8 to 24 characters long";

    @NotNull(message = NOT_BE_NULL)
    @Length(min = 8, max = 24, message = FROM_8_TO_24_CHARACTERS)
    private String password;

    @NotNull(message = NOT_BE_NULL)
    @Length(min = 8, max = 24, message = FROM_8_TO_24_CHARACTERS)
    private String repeatPassword;
}
