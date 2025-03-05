package emfoplus.gms.domain.run_checker.service;

import emfoplus.gms.domain.run_checker.entity.RunChecker;
import emfoplus.gms.domain.run_checker.repository.RunCheckerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RunCheckerService {
    private final RunCheckerRepository runCheckerRepository;

    /**
     * 모듈의 각 쓰레드가 잘 돌아가는지 DB에 저장하는 메서드
     * @param threadName = 해당 쓰레드 이름
     */
    public void runChecker(String threadName) {
        runCheckerRepository.save(
                RunChecker.builder()
                .threadName(threadName)
                .inputTime(LocalDateTime.now())
                .build()
        );
    }
}
