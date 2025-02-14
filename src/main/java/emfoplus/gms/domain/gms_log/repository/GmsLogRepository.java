package emfoplus.gms.domain.gms_log.repository;

import emfoplus.gms.domain.gms_log.service.GmsLogService;
import emfoplus.gms.domain.module.Mover;
import emfoplus.gms.domain.module.Receiver;
import emfoplus.gms.domain.module.Sender;
import emfoplus.gms.global.aop.AopAnnotation;
import emfoplus.gms.global.exception.handler.SettingHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
@RequiredArgsConstructor
public class GmsLogRepository implements ApplicationRunner {
    @Value("${table.log}")
    private String type;

    private final GmsLogService gmsLogService;
    private final Sender sender;
    private final Receiver receiver;
    private final Mover mover;

    @Override
    @AopAnnotation
    public void run(ApplicationArguments args) {
        if (type.equals("single")) {
            gmsLogService.checkAndCreateTable();
        }
        else if (type.equals("month")) {
            gmsLogService.checkAndCreateMonthTable();
        }
        else {
            throw new SettingHandler("SET1001", "로그 테이블 설정 입력 값이 잘못 되었습니다.");
        }

        log.info("초기 DB 설정 완료...");
        sender.setTrigger();
        receiver.setTrigger();
        mover.setTrigger();
    }

    @Scheduled(cron= "0 0 0 1 * *")
    public void decideSingleOrMonth() {
        if (type.equals("month")) {
            gmsLogService.checkAndCreateMonthTable();
        }
    }
}
