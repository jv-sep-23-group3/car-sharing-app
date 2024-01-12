package mate.sep23.group3.car.sharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.rental.RentalRequestDto;
import mate.sep23.group3.car.sharing.dto.rental.RentalResponseDto;
import mate.sep23.group3.car.sharing.model.User;
import mate.sep23.group3.car.sharing.service.RentalService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Rental management", description = "Endpoints for managing rentals")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rentals")
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new rental")
    public RentalResponseDto create(
            @RequestBody @Valid RentalRequestDto requestDto,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return rentalService.save(requestDto, user.getId());
    }

    @GetMapping
    @Operation(summary = "Get list of rentals",
            description = "Get rentals by user ID and whether the rental is still active or not")
    public List<RentalResponseDto> getList(
            @RequestParam Long userId, @RequestParam boolean isActive, Pageable pageable
    ) {
        return rentalService.getListByUserIdAndIsActiveStatus(userId, isActive, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific rental by rental ID")
    public RentalResponseDto getSpecificRental(
            @PathVariable Long id,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return rentalService.findByIdAndUserId(id, user.getId());
    }

    @PostMapping("/{id}/return")
    @Operation(summary = "Set actual return date to our rental")
    public RentalResponseDto homecoming(@PathVariable Long id) {
        return rentalService.update(id);
    }

}
