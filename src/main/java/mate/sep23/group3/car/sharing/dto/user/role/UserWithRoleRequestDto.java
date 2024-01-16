package mate.sep23.group3.car.sharing.dto.user.role;

import java.util.Set;
import lombok.Data;

@Data
public class UserWithRoleRequestDto {
    private Long id;
    private Set<Long> roles;
}
