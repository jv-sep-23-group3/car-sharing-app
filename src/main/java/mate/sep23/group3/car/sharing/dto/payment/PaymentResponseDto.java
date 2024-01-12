package mate.sep23.group3.car.sharing.dto.payment;

import lombok.Data;
import mate.sep23.group3.car.sharing.model.Payment;

import java.math.BigDecimal;

@Data
public class PaymentResponseDto {
    private Long id;
    private Payment.Status status;
    private Payment.Type type;
    private String session;
    private String sessionId;
    private BigDecimal amount;
}
