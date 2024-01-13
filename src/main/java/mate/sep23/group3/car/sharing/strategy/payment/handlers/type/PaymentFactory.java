package mate.sep23.group3.car.sharing.strategy.payment.handlers.type;

import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.exception.PaymentTypeHandlerException;
import mate.sep23.group3.car.sharing.strategy.payment.TypeHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentFactory {
    private final Map<String, TypeHandler> typeHandlerMap;

    public TypeHandler getTypeHandler(String paymentType) {
        TypeHandler typeHandler = typeHandlerMap.get(paymentType);

        if (typeHandler == null) {
            throw new PaymentTypeHandlerException("Unsupported payment type");
        }

        return typeHandler;
    }
}
