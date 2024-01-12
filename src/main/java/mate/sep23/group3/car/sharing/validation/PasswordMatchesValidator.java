package mate.sep23.group3.car.sharing.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import mate.sep23.group3.car.sharing.dto.user.UserRegistrationRequestDto;

public class PasswordMatchesValidator implements ConstraintValidator<FieldMatch,
        UserRegistrationRequestDto> {
    @Override
    public boolean isValid(UserRegistrationRequestDto requestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        return requestDto.getPassword() != null
                && Objects.equals(requestDto.getPassword(), requestDto.getRepeatPassword());
    }
}
