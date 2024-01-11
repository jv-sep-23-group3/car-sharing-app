package mate.sep23.group3.car.sharing.dto.rental;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RentalRequestDto {
    @NotNull
    @Positive
    private Long carId;
}
