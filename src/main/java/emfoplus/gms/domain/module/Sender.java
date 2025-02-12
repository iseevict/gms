package emfoplus.gms.domain.module;

import emfoplus.gms.domain.gms_log.entity.GmsLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class Sender {

    @Scheduled(fixedDelay = 1000)
    public void senderThread() {
        Thread.currentThread().setName("Sender");
        log.info("[ " + Thread.currentThread().getName() + " ] is starting...");

    }
}
