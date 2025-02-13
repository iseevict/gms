package emfoplus.gms.domain.module;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class Mover extends AbstractCommon {

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
    }
}
