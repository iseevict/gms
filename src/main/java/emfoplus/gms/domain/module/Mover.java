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

import static emfoplus.gms.domain.module.Sender.*;

@Service
@Slf4j
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
    public void moverThread() throws JsonProcessingException, IndexOutOfBoundsException {
        // run() 종료 대기
        if (!trigger) return;

        // 쓰레드 이름 설정
        Thread.currentThread().setName("Mover");

        // runChecker
        if (runCheckerCount == 60) {
            runCheckerService.runChecker(Thread.currentThread().getName());
        } else runCheckerCount++;

        // Select Where Status = 3
        List<GmsSend> gmsSendListBeforeReflectResult = gmsSendService.getTop1000GmsSendByStatusAndSendAt(completeLogCheckAndWaitMove);
        if (!gmsSendListBeforeReflectResult.isEmpty()) {
            // 로그 요청 및 결과 반영, GmsLog 이동, Status 값 변경
            for (GmsSend gmsSend : gmsSendListBeforeReflectResult) {
                // Tr on
                gmsSendService.reflectResultByLogAndMoveDataToGmsLog(gmsSend);
                // Tr off
            }
        }

        List<GmsSend> gmsSendListAlreadyError = gmsSendService.getTop1000GmsSendByStatusAndSendAt(someError);
        if (!gmsSendListAlreadyError.isEmpty()) {
            for (GmsSend gmsSend : gmsSendListAlreadyError) {
                gmsSendService.moveErrorDataToGmsLog(gmsSend);
            }
        }

        // 데이터 삭제 메서드
        List<GmsSend> gmsSendListWaitingDelete = gmsSendService.getTop1000GmsSendByStatusAndSendAt(completeMoveAndWaitDelete);
        if (!gmsSendListWaitingDelete.isEmpty()) {
            // Tr on
            gmsSendService.deleteGmsSendData(gmsSendListWaitingDelete);
            // Tr off
        }
    }

    public void setTrigger() {
        this.trigger = true;
        log.info("Mover is starting...");
    }
}
