package mate.sep23.group3.car.sharing.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mate.sep23.group3.car.sharing.dto.user.UserRegistrationRequestDto;
import java.util.Objects;

public class PasswordMatchesValidator implements ConstraintValidator<FieldMatch,
        UserRegistrationRequestDto> {
    @Override
    public boolean isValid(UserRegistrationRequestDto user,
                           ConstraintValidatorContext constraintValidatorContext) {
        return user.getPassword() != null
                && Objects.equals(user.getPassword(), user.getRepeatPassword());
    }
}
