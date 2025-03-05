package emfoplus.gms.global.exception.handler;

import emfoplus.gms.global.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ErrorHandler;

@Slf4j
public class CustomErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {
//        StackTraceElement st = t.getStackTrace()[0];
//        String location = st.getClassName() + "." + st.getMethodName() + "( Line : " + st.getLineNumber() + " )";

        for (StackTraceElement st : t.getStackTrace()) {
            log.error("{} - Line: {}", st.getClassName() + "." + st.getMethodName(), st.getLineNumber());
        }

//        log.error("Service error location: {}", location);
        log.error("Error Name: {}", t.getClass().getSimpleName());
        log.error("Error Message: {}", t.getMessage());
    }
}
