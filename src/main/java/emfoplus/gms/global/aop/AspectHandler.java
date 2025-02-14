package emfoplus.gms.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AspectHandler {

    @Around("@annotation(emfoplus.gms.global.aop.AopAnnotation)")
    public Object handleCustomAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed(); // 원래 메서드 실행
        } catch (Exception e) {
            log.error("{}.{} - " + e.getMessage(), joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
        return null;
    }
}
