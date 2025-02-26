package emfoplus.gms.domain.gms_log.api;

import emfoplus.gms.domain.gms_send.dto.GmsRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InfobipService {
    private final InfobipApiClient infobipApiClient;

    /**
     * Infobip에게 로그 메시지 받아오는 메서드
     * @param messageId = 확인할 GmsSend의 messageId 값
     * @return 로그메시지
     */
    public String requestGetMessageLog(String messageId) {
        return infobipApiClient.requestGetMessageLogToInfobip(messageId);
    }

    /**
     * Infobip에 문자 전송 요청 메서드
     * @param request = 전송할 문자 데이터
     */
    public String requestSendingMessage(GmsRequestDTO.RequestSendingMessageDto request) {
        return infobipApiClient.requestSendingMessageToInfobip(request);
    }
}
