package mate.sep23.group3.car.sharing.dto.role;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RoleResponseDto {
    private Long id;
    private String role;
}
