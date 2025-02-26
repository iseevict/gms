package emfoplus.gms.domain.module;

import emfoplus.gms.domain.gms_send.dto.GmsRequestDTO;
import emfoplus.gms.domain.gms_send.entity.GmsSend;
import emfoplus.gms.domain.gms_send.service.GmsSendService;
import emfoplus.gms.domain.run_checker.service.RunCheckerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static emfoplus.gms.domain.module.Sender.afterRequestAndWaitLogCheck;

@Service
@Slf4j
@Transactional(readOnly = true)
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
    public void receiverThread() {
        if (!trigger) return;

        Thread.currentThread().setName("Receiver");

        log.info("[ " + Thread.currentThread().getName() + " ] is starting...");

        // runChecker
        if (runCheckerCount == 10) {
            runCheckerService.runChecker(Thread.currentThread().getName());
        } else runCheckerCount++;

        // 데이터 추출 status = 2
        List<GmsSend> gmsSendListWaitChecking = gmsSendService.getGmsSendByStatusAndSendAt(afterRequestAndWaitLogCheck);
        log.info("{} 개 ", gmsSendListWaitChecking.size());

        // 추출한 데이터 타입 변환 List -> Set (빠른 삭제 위함)
        Set<GmsSend> gmsSendSetWaitChecking = new HashSet<>(gmsSendListWaitChecking);
        while(!gmsSendSetWaitChecking.isEmpty()) {
            // Tr on
            gmsSendSetWaitChecking.removeIf(gmsSendService::requestGetMessageLogAndCheckSent);
            // Tr off
        }
    }

    public void setTrigger() {
        this.trigger = true;
    }
}
