package emfoplus.gms.domain.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import emfoplus.gms.domain.gms_send.dto.GmsRequestDTO;
import emfoplus.gms.domain.gms_send.entity.GmsSend;
import emfoplus.gms.domain.gms_send.service.GmsSendService;
import emfoplus.gms.domain.run_checker.service.RunCheckerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class Sender {
    // Status 값
    public static final String inDb = "0";
    public static final String beforeRequest = "1";
    public static final String afterRequestAndWaitLogCheck = "2";
    public static final String completeLogCheckAndWaitMove = "3";
    public static final String completeMoveAndWaitDelete = "4";
    public static final String someError = "5";
    public static final String complete = "6";

    private final GmsSendService gmsSendService;
    private final RunCheckerService runCheckerService;
    private boolean trigger = false;
    private int runCheckerCount = 0;

    /**
     * Sender 메인 쓰레드
     * 어플리케이션 구동 시 최초 run() 진행 후 trigger 가 true로 변경되면 쓰레드 작동 시작
     */
    @Scheduled(fixedDelay = 1000)
    public void senderThread() throws JsonProcessingException {
        // Trigger
        if (!trigger) return;

        // Thread Name Change
        Thread.currentThread().setName("Sender");

        // runChecker
        if (runCheckerCount == 60) {
            runCheckerService.runChecker(Thread.currentThread().getName());
            runCheckerCount = 0;
        } else runCheckerCount++;

        // Select Data Where Status = 0 And SendAt < LocalDateTime.now()
        List<GmsSend> gmsSendListInDb = gmsSendService.getTop1000GmsSendByStatusAndSendAt(inDb);
        if (!gmsSendListInDb.isEmpty()) {
            log.info("Select Count: {} ", gmsSendListInDb.size());

            List<GmsSend> gmsSendListBeforeRequest = new ArrayList<>();
            for (GmsSend gmsSend : gmsSendListInDb) {
                // Tr on
                if (gmsSendService.isValidReceiverAndUpdateStatus(gmsSend)) {
                    gmsSendListBeforeRequest.add(gmsSend);
                }
                // Tr off
            }

            // 전송 요청 포맷 (정상 요청 건) + Gms Request Api 요청 + GmsSend MessageId 수정
            if (!gmsSendListBeforeRequest.isEmpty()) {
                for (GmsSend gmsSend : gmsSendListBeforeRequest) {
                    GmsRequestDTO.RequestSendingMessageDto dataForRequest = gmsSendService.changeDataFormatForRequestSendMessage(gmsSend);
                    // Tr on
                    gmsSendService.requestSendingMessageAndChangeStatus(dataForRequest, gmsSend);
                    // Tr off
                }
            }
        }

        // Status 값이 1이고 SendAt이 현재 시간보다 앞인 데이터 추출
        List<GmsSend> unsentGmsSendList = gmsSendService.getTop1000GmsSendByStatusAndSendAt(beforeRequest);
        if (!unsentGmsSendList.isEmpty()) {
            // 전송 요청 포맷 (비정상 요청 건 - DB에서 읽어왔지만 무슨 이유로 전송되지 않은 데이터들)
            for (GmsSend gmsSend : unsentGmsSendList) {
                GmsRequestDTO.RequestSendingMessageDto dataForRequest = gmsSendService.changeDataFormatForRequestSendMessage(gmsSend);
                // Tr on
                gmsSendService.requestSendingMessageAndChangeStatus(dataForRequest, gmsSend);
                // Tr off
            }
        }
    }

    public void setTrigger() {
        this.trigger = true;
        log.info("Sender is starting...");
    }
}
