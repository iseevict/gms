package emfoplus.gms.domain.module;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class Sender extends AbstractCommon {

    /**
     * Sender 메인 쓰레드
     * 어플리케이션 구동 시 최초 run() 진행 후 trigger 가 true로 변경되면 쓰레드 작동 시작
     */
    @Scheduled(fixedDelay = 1000)
    public void senderThread() {
        if (!trigger) return;

        Thread.currentThread().setName("Sender");

        log.info("[ " + Thread.currentThread().getName() + " ] is starting...");

    }
}
