package emfoplus.gms.test;

import emfoplus.gms.global.exception.handler.TestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class TestService {

    @Scheduled(fixedDelay = 5000)
    public void test() {
        throw new TestHandler("TEST001", "★ Exception Test ★");
    }
}
