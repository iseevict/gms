package emfoplus.gms.global.exception.handler;

import emfoplus.gms.global.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class TestHandler extends ServiceException {
    public TestHandler(String errorCode, String message) {
        super(HttpStatus.BAD_REQUEST, errorCode, message);
    }
}
