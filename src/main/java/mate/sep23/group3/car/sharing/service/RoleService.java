package mate.sep23.group3.car.sharing.service;

import java.util.List;
import mate.sep23.group3.car.sharing.dto.role.RoleResponseDto;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    List<RoleResponseDto> getRoles(Pageable pageable);
}
