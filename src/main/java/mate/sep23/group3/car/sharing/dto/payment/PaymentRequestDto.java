package mate.sep23.group3.car.sharing.dto.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;
import mate.sep23.group3.car.sharing.model.Payment;

@Data
@Accessors(chain = true)
public class PaymentRequestDto {
    @NotNull(message = "can't be null")
    @Positive(message = "must be greater than 0")
    private Long rentalId;
    @NotNull(message = "can't be null")
    private Payment.Type type;
}
