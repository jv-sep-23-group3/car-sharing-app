package mate.sep23.group3.car.sharing.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.role.RoleResponseDto;
import mate.sep23.group3.car.sharing.service.RoleService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users management", description = "Endpoints for managing ssers")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public List<RoleResponseDto> getRoles(Pageable pageable) {
        return roleService.getRoles(pageable);
    }
}

