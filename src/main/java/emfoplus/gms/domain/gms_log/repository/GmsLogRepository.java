package emfoplus.gms.domain.gms_log.repository;

import emfoplus.gms.domain.gms_log.service.GmsLogService;
import emfoplus.gms.global.exception.handler.SettingHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class GmsLogRepository implements ApplicationRunner {
    @Value("${table.log}")
    private String type;

    private final GmsLogService gmsLogService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            if (type.equals("single")) {
                gmsLogService.checkAndCreateTable();
            }
            else if (type.equals("month")) {
                gmsLogService.checkAndCreateMonthTable();
            }
            else {
                throw new SettingHandler("SET1001", "로그 테이블 설정 입력 값이 잘못 되었습니다.");
            }
        } catch (SettingHandler e) {
            log.error(e.getMessage());
            System.exit(1);
        }
    }

    @Scheduled(cron= "0 0 0 1 * *")
    public void decideSingleOrMonth() {
        if (type.equals("month")) {
            gmsLogService.checkAndCreateMonthTable();
        }
    }
}
