package emfoplus.gms.domain.gms_log.service;

import emfoplus.gms.domain.gms_log.repository.GmsLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GmsLogService {
    private final GmsLogRepository gmsLogRepository;

    @Scheduled(fixedDelay = 1000)
    public void logThread() {
        Thread.currentThread().setName("Log-Thread");

        log.error("logThread");
    }
}
