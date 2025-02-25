package emfoplus.gms.domain.module;

import emfoplus.gms.domain.gms_send.dto.GmsRequestDTO;
import emfoplus.gms.domain.gms_send.entity.GmsSend;
import emfoplus.gms.domain.gms_send.service.GmsSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class Sender extends AbstractCommon {
    // Status 값
    public static final String inDb = "0";
    public static final String beforeRequest = "1";
    public static final String afterRequestAndWaitLogCheck = "2";
    public static final String completeLogCheckAndWaitMove = "3";
    public static final String completeMoveAndWaitDelete = "4";
    public static final String someError = "5";

    private final GmsSendService gmsSendService;

    /**
     * Sender 메인 쓰레드
     * 어플리케이션 구동 시 최초 run() 진행 후 trigger 가 true로 변경되면 쓰레드 작동 시작
     */
    @Scheduled(fixedDelay = 1000)
    public void senderThread() throws InterruptedException {
        if (!trigger) return;

        Thread.currentThread().setName("Sender");

        log.info("[ " + Thread.currentThread().getName() + " ] is starting...");

        // runChecker 등 Sender Main logic 실행 전 필요한 메서드 추가 예정 //

        List<GmsSend> gmsSendListInDb = gmsSendService.getGmsSendByStatusAndSendAt(inDb);

        System.out.println("num : " + gmsSendListInDb.size());

        // Tr on
        // 전화번호 + 국가코드 -> destination 저장
        List<GmsSend> gmsSendListAfterSetDestination = gmsSendService.updateGmsSendDestinationByReceiverAndCountryCode(gmsSendListInDb);
        // Tr off

        // Tr on
        List<GmsSend> gmsSendListBeforeRequest = gmsSendService.updateGmsSendStatus(gmsSendListAfterSetDestination, beforeRequest);
        // Tr off -> 예외 발생이 없다면 DB에 Status 값 0 -> 1 변경

        // 전송 요청 포맷 (정상 요청 건)
        GmsRequestDTO.requestSendingMessageDto dataForRequest = gmsSendService.changeDataFormatForRequestSendMessage(gmsSendListBeforeRequest);

        // Tr on
        gmsSendService.requestSendingMessageAndChangeStatus(dataForRequest, gmsSendListBeforeRequest);
        // Tr off

        // Status 값이 1이고 SendAt이 현재 시간보다 앞인 데이터 추출
        List<GmsSend> unsentGmsSendList = gmsSendService.getGmsSendByStatusAndSendAt(beforeRequest);

        // 전송 요청 포맷 (비정상 요청 건 - DB에서 읽어왔지만 무슨 이유로 전송되지 않은 데이터들)
        dataForRequest = gmsSendService.changeDataFormatForRequestSendMessage(gmsSendListBeforeRequest);

        // Tr on
        gmsSendService.requestSendingMessageAndChangeStatus(dataForRequest, unsentGmsSendList);
        // Tr off
    }

}
