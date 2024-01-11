package mate.sep23.group3.car.sharing.dto.rental;

import lombok.Data;
import java.time.LocalDateTime;
import mate.sep23.group3.car.sharing.model.Car;

@Data
public class RentalResponseDto {
    private Long id;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;
    private LocalDateTime actualReturnDate;
    private Car car;
}
