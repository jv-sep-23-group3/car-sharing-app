package mate.sep23.group3.car.sharing.mapper;

import com.stripe.model.checkout.Session;
import mate.sep23.group3.car.sharing.config.MapperConfig;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import mate.sep23.group3.car.sharing.dto.payment.PaymentUrlResponseDto;
import mate.sep23.group3.car.sharing.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    PaymentResponseDto toDto(Payment payment);

    PaymentUrlResponseDto toDto(Session session);
}
