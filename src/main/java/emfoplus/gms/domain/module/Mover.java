package emfoplus.gms.domain.module;

import emfoplus.gms.domain.gms_log.api.InfobipService;
import emfoplus.gms.domain.gms_send.entity.GmsSend;
import emfoplus.gms.domain.gms_send.service.GmsSendService;
import emfoplus.gms.domain.run_checker.service.RunCheckerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static emfoplus.gms.domain.module.Sender.*;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class Mover {
    private final GmsSendService gmsSendService;
    private final RunCheckerService runCheckerService;

    private boolean trigger = false;
    private int runCheckerCount = 0;

    /**
     * Mover 메인 쓰레드
     * 어플리케이션 구동 시 최초 run() 진행 후 trigger 가 true로 변경되면 쓰레드 작동 시작
     */
    @Scheduled(fixedDelay = 1000)
    public void moverThread() {
        // run() 종료 대기
        if (!trigger) return;

        // 쓰레드 이름 설정
        Thread.currentThread().setName("Mover");

        log.info("[ " + Thread.currentThread().getName() + " ] is starting...");

        // runChecker
        if (runCheckerCount == 10) {
            runCheckerService.runChecker(Thread.currentThread().getName());
        } else runCheckerCount++;

        // Select Where Status = 3
        List<GmsSend> gmsSendListBeforeReflectResult = gmsSendService.getGmsSendByStatusAndSendAt(completeLogCheckAndWaitMove);

        // 로그 요청 및 결과 반영, GmsLog 이동, Status 값 변경
        for (GmsSend gmsSend : gmsSendListBeforeReflectResult) {
            // Tr on
            gmsSendService.reflectResultByLogAndMoveDataToGmsLog(gmsSend);
            // Tr off
        }

        // 데이터 삭제 메서드
        List<GmsSend> gmsSendListWaitingDelete = gmsSendService.getGmsSendByStatusAndSendAt(completeMoveAndWaitDelete);

        // Tr on
        gmsSendService.deleteGmsSendData(gmsSendListWaitingDelete);
        // Tr off
    }

    public void setTrigger() {
        this.trigger = true;
    }
}
