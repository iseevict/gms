package emfoplus.gms.domain.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import emfoplus.gms.domain.gms_send.entity.GmsSend;
import emfoplus.gms.domain.gms_send.service.GmsSendService;
import emfoplus.gms.domain.run_checker.service.RunCheckerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static emfoplus.gms.domain.module.Sender.afterRequestAndWaitLogCheck;

@Service
@Slf4j
@RequiredArgsConstructor
public class Receiver {
    private final GmsSendService gmsSendService;
    private final RunCheckerService runCheckerService;

    private boolean trigger = false;
    private int runCheckerCount = 0;

    /**
     * Receiver 메인 쓰레드
     * 어플리케이션 구동 시 최초 run() 진행 후 trigger 가 true로 변경되면 쓰레드 작동 시작
     */
    @Scheduled(fixedDelay = 1000)
    public void receiverThread() throws JsonProcessingException {
        if (!trigger) return;

        Thread.currentThread().setName("Receiver");

        // runChecker
        if (runCheckerCount == 60) {
            runCheckerService.runChecker(Thread.currentThread().getName());
        } else runCheckerCount++;

        // 데이터 추출 status = 2
        List<GmsSend> gmsSendListWaitChecking = gmsSendService.getTop1000GmsSendByStatusAndRequestAt(afterRequestAndWaitLogCheck);

        for (GmsSend gmsSend : gmsSendListWaitChecking) {
            log.info("CHECK");
            gmsSendService.requestGetMessageLogAndCheckSent(gmsSend);
        }
    }

    public void setTrigger() {
        this.trigger = true;
        log.info("Receiver is starting...");
    }
}
