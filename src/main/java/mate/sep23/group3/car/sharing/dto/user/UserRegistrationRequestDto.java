package mate.sep23.group3.car.sharing.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import mate.sep23.group3.car.sharing.validation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch
public class UserRegistrationRequestDto {
    private static final String NOT_BE_NULL = "Can't be null";
    private static final String FROM_8_TO_24_CHARACTERS = "Must contain 8 to 24 characters long";
    private static final String INCORRECT_EMAIL_FORMAT = "Incorrect email format";

    @Email(message = INCORRECT_EMAIL_FORMAT)
    @NotBlank(message = NOT_BE_NULL)
    private String email;

    @NotBlank(message = NOT_BE_NULL)
    @Length(min = 4, max = 24, message = FROM_8_TO_24_CHARACTERS)
    private String password;

    @NotBlank(message = NOT_BE_NULL)
    @Length(min = 4, max = 24, message = FROM_8_TO_24_CHARACTERS)
    private String repeatPassword;

    @NotBlank(message = NOT_BE_NULL)
    private String firstName;

    @NotBlank(message = NOT_BE_NULL)
    private String lastName;
}
