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
public class Mover {

    @Scheduled(fixedDelay = 1000)
    public void moverThread() {
        Thread.currentThread().setName("Mover");
        log.info("[ " + Thread.currentThread().getName() + " ] is starting...");
    }
}
