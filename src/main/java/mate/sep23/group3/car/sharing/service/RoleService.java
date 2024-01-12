package mate.sep23.group3.car.sharing.service;

import mate.sep23.group3.car.sharing.dto.role.RoleResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    List<RoleResponseDto> getRoles(Pageable pageable);
}
