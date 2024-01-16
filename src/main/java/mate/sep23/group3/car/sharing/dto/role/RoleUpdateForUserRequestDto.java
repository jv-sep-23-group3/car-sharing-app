package mate.sep23.group3.car.sharing.dto.role;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;

@Data
public class RoleUpdateForUserRequestDto {
    private static final String NOT_BE_NULL = "Can't be null";

    @NotNull(message = NOT_BE_NULL)
    private Set<Long> roles;
}
