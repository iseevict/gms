package emfoplus.gms.global.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public void handleAllExceptions(Exception e) {
        StackTraceElement st = e.getStackTrace()[0];
        String location = st.getMethodName() + "( Line : " + st.getLineNumber() + " )";

        log.error("Service error location: {}", location);
        log.error("Error Name: {}", e.getClass().getSimpleName());
        log.error("Error Message: {}", e.getMessage());
    }
}


//    @ExceptionHandler(ServiceException.class)
//    public void handleServiceException(ServiceException e) {
//        StackTraceElement st = e.getStackTrace()[0];
//        String location = st.getMethodName() + "( Line : " + st.getLineNumber() + " )";
//
//        log.error("Service error location: {}", location);
//        log.error("Error Name: {}", e.getClass().getSimpleName());
//        log.error("Error Message: {}", e.getMessage());
//    }