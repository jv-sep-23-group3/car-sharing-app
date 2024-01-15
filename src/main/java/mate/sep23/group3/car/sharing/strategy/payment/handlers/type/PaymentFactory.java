package mate.sep23.group3.car.sharing.strategy.payment.handlers.type;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.exception.PickHandlerException;
import mate.sep23.group3.car.sharing.strategy.payment.TypeHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFactory {
    private static final String EXCEPTION_MESSAGE = "Unsupported payment type";
    private final Map<String, TypeHandler> typeHandlerMap;

    public TypeHandler getTypeHandler(String paymentType) {
        TypeHandler typeHandler = typeHandlerMap.get(paymentType);

        if (typeHandler == null) {
            throw new PickHandlerException(EXCEPTION_MESSAGE);
        }

        return typeHandler;
    }
}
