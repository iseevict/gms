package emfoplus.gms.global.exception.handler;

import emfoplus.gms.global.exception.ErrorResponse;
import emfoplus.gms.global.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(ServiceException e) {
        log.error("Service error occurred: {}", e.getClass().getName());
        log.error("Error Message: {}", e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                e.getHttpStatus().value(),
                e.getErrorCode(),
                e.getMessage()
        );

        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception e) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred"
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
