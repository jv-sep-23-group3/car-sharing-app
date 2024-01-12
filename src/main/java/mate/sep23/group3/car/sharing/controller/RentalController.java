package mate.sep23.group3.car.sharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.awt.print.Pageable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.dto.rental.RentalRequestDto;
import mate.sep23.group3.car.sharing.dto.rental.RentalResponseDto;
import mate.sep23.group3.car.sharing.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Rental management", description = "Endpoints for managing rentals")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rentals")
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new rental")
    public RentalResponseDto create(@RequestBody @Valid RentalRequestDto requestDto) {
        return rentalService.save(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get list of rentals",
            description = "Get rentals by user ID and whether the rental is still active or not")
    public List<RentalResponseDto> getList(
            @RequestParam Long userId, @RequestParam boolean isActive, Pageable pageable
    ) {
        return rentalService.getRentalListByUserIdAndIsActiveStatus(userId, isActive, pageable);
    }

    @GetMapping("/id")
    @Operation(summary = "Get a specific rental by rental ID")
    public RentalResponseDto getSpecificRental(@PathVariable Long id) {
        return rentalService.findById(id);
    }

    @PostMapping("/id/return")
    @Operation(summary = "Set actual return date to our rental")
    public RentalResponseDto homecoming(@PathVariable Long id) {
        return rentalService.update(id);
    }

}
