package mate.sep23.group3.car.sharing.dto.payment;

import java.math.BigDecimal;
import lombok.Data;
import mate.sep23.group3.car.sharing.model.Payment;

@Data
public class PaymentResponseDto {
    private Long id;
    private Payment.Status status;
    private Payment.Type type;
    private Long rentalId;
    private String session;
    private String sessionId;
    private BigDecimal amount;
}
