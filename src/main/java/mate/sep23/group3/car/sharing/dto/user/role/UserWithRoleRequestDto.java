package mate.sep23.group3.car.sharing.dto.user.role;

import java.util.Set;
import lombok.Data;
import mate.sep23.group3.car.sharing.model.Role;

@Data
public class UserWithRoleRequestDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<Role> roles;
}
