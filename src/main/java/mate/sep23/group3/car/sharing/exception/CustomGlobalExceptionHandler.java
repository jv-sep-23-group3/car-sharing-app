package mate.sep23.group3.car.sharing.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST);
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    private String getErrorMessage(ObjectError error) {
        if (error instanceof FieldError) {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            return field + " " + message;
        }
        return error.getDefaultMessage();
    }

    @ExceptionHandler({RegistrationException.class, RentalReturnException.class,
            StripeProcessingException.class})
    protected ResponseEntity<Object> handleExceptionRegistrationException(
            RegistrationException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler({UnauthorizedOperationException.class})
    protected ResponseEntity<Object> handleUnauthorizedOperationException(
            UnauthorizedOperationException ex) {
        return buildErrorResponse(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, ex);
    }

    @ExceptionHandler({PendingPaymentExistsException.class})
    protected ResponseEntity<Object> handleConflictException(
            PendingPaymentExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler({
            ResourceNotFoundException.class,
            EntityNotFoundException.class,
            CarInventoryException.class
    })
    protected ResponseEntity<Object> handleExceptionResourceNotFoundAndEntityNot(Exception ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex);
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, Exception exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND);
        body.put("message", exception.getMessage());
        return new ResponseEntity<>(body, status);
    }
}
