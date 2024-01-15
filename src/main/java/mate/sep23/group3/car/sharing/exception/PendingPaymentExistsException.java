package mate.sep23.group3.car.sharing.exception;

public class PendingPaymentExistsException extends RuntimeException {
    public PendingPaymentExistsException(String message) {
        super(message);
    }
}
