package emfoplus.gms.domain.gms_send.service;

import emfoplus.gms.domain.gms_log.api.InfobipApiClient;
import emfoplus.gms.domain.gms_send.dto.GmsRequestDTO;
import emfoplus.gms.domain.gms_send.entity.GmsSend;
import emfoplus.gms.domain.gms_send.repository.GmsSendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static emfoplus.gms.domain.module.Sender.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class GmsSendService {
    private final GmsSendRepository gmsSendRepository;
    private final GmsSendService gmsSendService;
    private final InfobipApiClient infobipApiClient;

    /**
     * GmsSend 데이터 추출 메서드
     * 조건1. SendAt(전송 요청 시간)이 현재 시간보다 앞
     * 조건2. status 값이 인자로 들어온 status와 일치
     * @param status = 상태값
     * @return List<GmsSend> = 조건에 일치한 GmsSend 배열
     */
    @Transactional(readOnly = true)
    public List<GmsSend> getGmsSendByStatusAndSendAt(String status) {
        return gmsSendRepository.findAllByStatusAndSendAtLessThanEqual(status, LocalDateTime.now()).orElse(new ArrayList<>());
    }

    /**
     * GmsSend Status 변경 메서드
     * @param gmsSendList = 변경할 GmsSend 배열, status = 변경 후 Status 값
     * @return List<GmsSend> = Status 변경된 GmsSend 배열
     */
    @Transactional
    public List<GmsSend> updateGmsSendStatus(List<GmsSend> gmsSendList, String status) {
        for (GmsSend gmsSend : gmsSendList) {
            gmsSend.setStatus(status);
        }
        gmsSendRepository.saveAllAndFlush(gmsSendList);
        if (status.equals(beforeRequest)) {
            log.info("Data Reading Complete. Read Data Num = {} , Change Status to {}", gmsSendList.size(), status);
        } else if (status.equals(afterRequestAndWaitLogging)) {
            log.info("Sending Global Message Api Request Complete. Read Data Num = {} , Change Status to {}", gmsSendList.size(), status);
        } else if (status.equals(someError)) {
            GmsSend gmsSend = gmsSendList.get(0);
            log.info("Sending Fail Data. Data Message Id = {} , Change Status to {}", gmsSend.getMessageId(), status);
        }

        return gmsSendList;
    }

    /**
     * GmsSend Destination 변경 메서드
     * @param gmsSendList = 변경할 GmsSend 배열
     * @return List<GmsSend> = Destination이 변경된 GmsSend 배열
     */
    @Transactional
    public List<GmsSend> updateGmsSendDestinationByReceiverAndCountryCode(List<GmsSend> gmsSendList) {
        for (GmsSend gmsSend : gmsSendList) {
            if (gmsSend.getDestination() != null) continue; // 이미 Destination이 저장되어 있다면 continue
            else {
                String receiver = gmsSend.getReceiver();
                if (!isValidReceiver(receiver)) {
                    gmsSend.setRsltCode("9999");
                    List<GmsSend> singleGmsSendData = new ArrayList<>();
                    singleGmsSendData.add(gmsSend);
                    gmsSendService.updateGmsSendStatus(singleGmsSendData, someError);
                    continue;
                }
                String countryCode = gmsSend.getCountryCode();
                StringBuilder sb = new StringBuilder();
                if (receiver.startsWith("0") && !countryCode.equals("39")) { // 39 (이탈리아) 는 제일 앞에 0 을 제외하지 않음 + 아르헨티나는 좀 특이한데 고려 안한 상태
                    sb.append("+").append(countryCode).append(receiver.substring(1));
                    // 삭제
                    System.out.println("destination = " + sb.toString());
                } else {
                    sb.append("+").append(countryCode).append(receiver);
                    //삭제
                    System.out.println("destination = " + sb.toString());
                }
                gmsSend.setDestination(sb.toString());
            }
        }
        gmsSendRepository.saveAllAndFlush(gmsSendList);

        return gmsSendList;
    }

    /**
     * Receiver의 유효성 검증 메서드
     * @param receiver = 검증할 변수
     * @return boolean = 유효하면 true / 아니면 false
     */
    private boolean isValidReceiver(String receiver) {
        String regex = "^[0-9-]$";
        return receiver != null && Pattern.matches(regex, receiver);
    }

    /**
     * GmsSend 배열 -> 요청을 위한 포맷 변경 메서드
     * @param gmsSendList 요청할 문자 데이터
     * @return
     */
    public GmsRequestDTO.requestSendingMessageDto changeDataFormatForRequest(List<GmsSend> gmsSendList) {
        return null;
    }

    /**
     * Infobip API 요청 및 GmsSend 배열 Status 변경 메서드 중개 메서드
     * @param request = 전송할 문자 데이터
     * @param gmsSendList = 전송할 문자 데이터 포맷팅 전 GmsSend 배열
     * @param changeStatus = 전송 후 GmsSend 배열의 변경 후 Status값
     */
    @Transactional
    public void requestSendingMessageAndChangeStatus(GmsRequestDTO.requestSendingMessageDto request, List<GmsSend> gmsSendList, String changeStatus) {
        gmsSendService.requestSendingMessage(request);
        gmsSendService.updateGmsSendStatus(gmsSendList, changeStatus);
    }

    /**
     * Infobip에 문자 전송 요청 메서드
     * @param request = 전송할 문자 데이터
     */
    private void requestSendingMessage(GmsRequestDTO.requestSendingMessageDto request) {
        infobipApiClient.requestSendingMessageToInfobip(request);
    }
}
