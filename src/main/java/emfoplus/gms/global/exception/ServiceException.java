package emfoplus.gms.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;

    public ServiceException(HttpStatus httpStatus, String errorCode, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this. errorCode = errorCode;
    }
}
