package emfoplus.gms.domain.module;

import emfoplus.gms.domain.gms_send.entity.GmsSend;
import emfoplus.gms.domain.gms_send.service.GmsSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class Sender extends AbstractCommon {
    // Status 값
    private final static String inDb = "0";
    private final static String beforeRequest = "1";

    private final GmsSendService gmsSendService;

    /**
     * Sender 메인 쓰레드
     * 어플리케이션 구동 시 최초 run() 진행 후 trigger 가 true로 변경되면 쓰레드 작동 시작
     */
    @Scheduled(fixedDelay = 1000)
    public void senderThread() {
        if (!trigger) return;

        Thread.currentThread().setName("Sender");

        log.info("[ " + Thread.currentThread().getName() + " ] is starting...");

        List<GmsSend> gmsSendListInDb = gmsSendService.getGmsSendByStatusAndSendAt(inDb);

        // 트랜잭션 시작

        for (GmsSend gmsSend : gmsSendListInDb) {
            gmsSend.setStatus(beforeRequest);
        }



        // 트랜잭션 종료

        List<GmsSend> gmsSendListBeforeRequest = gmsSendListInDb;

        // 유효성 검사

        // 트랜잭션 시작

        // 전송 요청

        // Status 변경 1 TO 2

        // 트랜잭션 종료

        // Status 값이 1이고 SendAt이 현재 시간보다 앞인 데이터 추출

        // 트랜잭션 시작

        // 전송 요청

        // Status 변경 1 To 2

        // 트랜잭션 종료

    }

}
