package mate.sep23.group3.car.sharing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import mate.sep23.group3.car.sharing.dto.role.RoleResponseDto;
import mate.sep23.group3.car.sharing.mapper.RoleMapper;
import mate.sep23.group3.car.sharing.model.Role;
import mate.sep23.group3.car.sharing.repository.RoleRepository;
import mate.sep23.group3.car.sharing.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {
    private static Role adminRole;
    private static Role managerRole;
    private static Role customerRole;
    private static RoleResponseDto adminDto;
    private static RoleResponseDto managerDto;
    private static RoleResponseDto customerDto;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleServiceImpl roleServiceImpl;

    @BeforeAll
    static void beforeAll() {
        adminRole = new Role()
                .setId(1L)
                .setRoleName(Role.RoleName.ADMIN);

        managerRole = new Role()
                .setId(2L)
                .setRoleName(Role.RoleName.MANAGER);

        customerRole = new Role()
                .setId(3L)
                .setRoleName(Role.RoleName.CUSTOMER);

        adminDto = new RoleResponseDto()
                .setId(1L)
                .setRole("ADMIN");

        managerDto = new RoleResponseDto()
                .setId(2L)
                .setRole("MANAGER");

        customerDto = new RoleResponseDto()
                .setId(3L)
                .setRole("CUSTOMER");
    }

    @Test
    @DisplayName("Get all roles")
    public void getRoles_ThreeRoles_ReturnListOfRoleResponseDto() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        when(roleRepository.findAll(pageRequest))
                .thenReturn(new PageImpl<>(List.of(adminRole, managerRole, customerRole)));
        when(roleMapper.toDto(adminRole)).thenReturn(adminDto);
        when(roleMapper.toDto(managerRole)).thenReturn(managerDto);
        when(roleMapper.toDto(customerRole)).thenReturn(customerDto);

        List<RoleResponseDto> expected = List.of(adminDto, managerDto, customerDto);
        List<RoleResponseDto> actual = roleServiceImpl.getRoles(pageRequest);
        assertEquals(expected, actual);
        assertEquals(expected.size(), actual.size());
    }
}
