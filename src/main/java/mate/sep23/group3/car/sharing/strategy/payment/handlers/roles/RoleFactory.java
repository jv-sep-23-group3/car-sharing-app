package mate.sep23.group3.car.sharing.strategy.payment.handlers.roles;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import mate.sep23.group3.car.sharing.exception.PickHandlerException;
import mate.sep23.group3.car.sharing.strategy.payment.RoleHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleFactory {
    private static final String EXCEPTION_MESSAGE = "Unsupported role type";
    private final Map<String, RoleHandler> roleHandlerMap;

    public RoleHandler getRoleHandler(String roleType) {
        RoleHandler roleHandler = roleHandlerMap.get(roleType);

        if (roleHandler == null) {
            throw new PickHandlerException(EXCEPTION_MESSAGE);
        }

        return roleHandler;
    }
}
