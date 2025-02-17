package emfoplus.gms.domain.gms_send.service;

import emfoplus.gms.domain.gms_send.entity.GmsSend;
import emfoplus.gms.domain.gms_send.repository.GmsSendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GmsSendService {
    private final GmsSendRepository gmsSendRepository;

    /**
     * GmsSend 데이터 추출 메서드
     * 조건1. SendAt(전송 요청 시간)이 현재 시간보다 앞
     * 조건2. status 값이 인자로 들어온 status와 일치
     * @param status = 상태값
     * @return List<GmsSend>
     */
    public List<GmsSend> getGmsSendByStatusAndSendAt(String status) {
        return gmsSendRepository.findAllByStatusAndSendAtLessThanEqual(status, LocalDateTime.now()).orElse(new ArrayList<>());
    }
}
