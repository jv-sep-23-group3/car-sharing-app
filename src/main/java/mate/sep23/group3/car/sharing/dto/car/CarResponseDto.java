package mate.sep23.group3.car.sharing.dto.car;

import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;
import mate.sep23.group3.car.sharing.model.Car;

@Data
@Accessors(chain = true)
public class CarResponseDto {
    private Long id;
    private String brand;
    private String model;
    private Car.Type type;
    private int inventory;
    private BigDecimal dailyFee;
}
