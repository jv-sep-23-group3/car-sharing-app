package mate.sep23.group3.car.sharing.exception;

public class StripeProcessingException extends RuntimeException {
    public StripeProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
