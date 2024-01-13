package mate.sep23.group3.car.sharing.dto.payment;

import lombok.Data;
import mate.sep23.group3.car.sharing.model.Payment;

@Data
public class PaymentRequestDto {
    private Long rentalId;
    private Payment.Type type;
}
