package emfoplus.gms.domain.mover.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class moverService {

    @Scheduled(fixedDelay = 1000)
    public void moverThread() {
        Thread.currentThread().setName("Mover-Thread");

        log.error("moverThread");
    }
}
