package mate.sep23.group3.car.sharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.car.CarRequestDto;
import mate.sep23.group3.car.sharing.dto.car.CarResponseDto;
import mate.sep23.group3.car.sharing.service.CarService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Car management", description = "Endpoints for managing cars")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cars")
public class CarController {
    private final CarService carService;

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    @GetMapping
    @Operation(summary = "Get all cars", description = "Get a list of all available cars")
    public List<CarResponseDto> getAll(Pageable pageable) {
        return carService.getAll(pageable);
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get a car by id", description = "Get a car by id")
    public CarResponseDto getById(@PathVariable Long id) {
        return carService.getById(id);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new car", description = "Add a new car")
    public CarResponseDto add(@RequestBody @Valid CarRequestDto requestDto) {
        return carService.add(requestDto);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a car", description = "Update a car")
    public CarResponseDto update(
            @PathVariable Long id,
            @RequestBody @Valid CarRequestDto requestDto
    ) {
        return carService.update(id, requestDto);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a car", description = "Delete a car")
    public void delete(@PathVariable Long id) {
        carService.delete(id);
    }
}
