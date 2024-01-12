package mate.sep23.group3.car.sharing.mapper;

import mate.sep23.group3.car.sharing.config.MapperConfig;
import mate.sep23.group3.car.sharing.dto.role.RoleResponseDto;
import mate.sep23.group3.car.sharing.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RoleMapper {
    @Mapping(target = "role", source = "roleName")
    RoleResponseDto toDto(Role role);
}
