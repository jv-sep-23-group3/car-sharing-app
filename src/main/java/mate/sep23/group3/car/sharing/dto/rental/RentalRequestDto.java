package mate.sep23.group3.car.sharing.dto.rental;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RentalRequestDto {
    @NotNull(message = "can't be null")
    @Positive(message = "must be greater than 0")
    private Long carId;
    @NotNull(message = "can't be null")
    private LocalDateTime returnDate;
}
