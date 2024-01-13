package mate.sep23.group3.car.sharing.dto.car;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import mate.sep23.group3.car.sharing.model.Car;

@Data
public class CarRequestDto {
    @NotNull(message = "name cannot be null")
    @Size(max = 255, message = "name cannot be greater than 255 characters")
    private String model;

    @NotNull(message = "name cannot be null")
    @Size(max = 255, message = "name cannot be greater than 255 characters")
    private String brand;

    @NotNull
    private Car.Type type;

    @Min(0)
    private int inventory;

    @NotNull(message = "cannot be null")
    @Positive
    private BigDecimal dailyFee;
}
