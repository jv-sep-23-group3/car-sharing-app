package mate.sep23.group3.car.sharing.mapper;

import mate.sep23.group3.car.sharing.config.MapperConfig;
import mate.sep23.group3.car.sharing.dto.payment.PaymentRequestDto;
import mate.sep23.group3.car.sharing.dto.payment.PaymentResponseDto;
import mate.sep23.group3.car.sharing.model.Payment;
import mate.sep23.group3.car.sharing.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    @Mapping(target = "rentalId", source = "rental.id")
    PaymentResponseDto toDto(Payment payment);

    @Mapping(target = "rental", source = "rentalId", qualifiedByName = "rentalFromId")
    Payment toModel(PaymentRequestDto paymentRequestDto);

    @Named("rentalFromId")
    default Rental rentalFromId(Long rentalId) {
        return new Rental().setId(rentalId);
    }
}
