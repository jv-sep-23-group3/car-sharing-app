package mate.sep23.group3.car.sharing.dto.rental;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class RentalRequestDto {
    @NotNull
    @Positive
    private Long id;
}
