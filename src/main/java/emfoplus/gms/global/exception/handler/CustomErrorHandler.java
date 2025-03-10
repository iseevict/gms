package emfoplus.gms.global.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ErrorHandler;

@Slf4j
public class CustomErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {
        for (StackTraceElement st : t.getStackTrace()) {
            if (st.getClassName().startsWith("emfoplus")) {
                log.error("{} - Line: {}", st.getClassName() + "." + st.getMethodName(), st.getLineNumber());
            }
        }

        log.error("Error Name: {}", t.getClass().getSimpleName());
        log.error("Error Message: {}", t.getMessage());
    }
}
