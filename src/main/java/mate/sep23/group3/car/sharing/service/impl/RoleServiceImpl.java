package mate.sep23.group3.car.sharing.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.role.RoleResponseDto;
import mate.sep23.group3.car.sharing.mapper.RoleMapper;
import mate.sep23.group3.car.sharing.repository.RoleRepository;
import mate.sep23.group3.car.sharing.service.RoleService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleResponseDto> getRoles(Pageable pageable) {
        return roleRepository.findAll(pageable).stream()
                .map(roleMapper::toDto)
                .toList();
    }
}
