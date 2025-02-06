package emfoplus.gms.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class TestService {

    @Scheduled(fixedDelay = 5000)
    public void test() {
        log.error("test" + LocalDateTime.now());
    }
}
