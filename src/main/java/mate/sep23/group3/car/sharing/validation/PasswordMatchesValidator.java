package mate.sep23.group3.car.sharing.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import mate.sep23.group3.car.sharing.dto.user.password.UserUpdatePasswordRequestDto;

public class PasswordMatchesValidator implements ConstraintValidator<FieldMatchChangePassword,
        UserUpdatePasswordRequestDto> {
    @Override
    public boolean isValid(UserUpdatePasswordRequestDto requestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        return requestDto.getPassword() != null
                && Objects.equals(requestDto.getPassword(), requestDto.getRepeatPassword());
    }
}
