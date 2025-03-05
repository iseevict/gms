package emfoplus.gms.domain.gms_send.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import emfoplus.gms.domain.gms_log.api.InfobipService;
import emfoplus.gms.domain.gms_log.service.GmsLogService;
import emfoplus.gms.domain.gms_send.dto.GmsRequestDTO;
import emfoplus.gms.domain.gms_send.dto.GmsResponseDTO;
import emfoplus.gms.domain.gms_send.entity.GmsSend;
import emfoplus.gms.domain.gms_send.repository.GmsSendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static emfoplus.gms.domain.module.Sender.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class GmsSendService {
    private final GmsSendRepository gmsSendRepository;
    private final GmsLogService gmsLogService;
    private final InfobipService infobipService;

    /**
     * GmsSend 데이터 추출 메서드
     * 조건1. SendAt(전송 요청 시간)이 현재 시간보다 앞
     * 조건2. status 값이 인자로 들어온 status와 일치
     * @param status = 상태값
     * @return List<GmsSend> = 조건에 일치한 GmsSend 배열
     */
    @Transactional(readOnly = true)
    public List<GmsSend> getTop1000GmsSendByStatusAndSendAt(String status) {
        return gmsSendRepository.findTop1000ByStatusAndSendAtLessThanEqual(status, LocalDateTime.now()).orElse(new ArrayList<>());
    }

    /**
     * Receiver의 유효성 검증 후 유효 여부에 따라 Status 변경 메서드
     * @param gmsSend = 변경할 GmsSend
     * @return boolean = Receiver가 유효하면 True, 유효하지 않다면 False
     */
    @Transactional
    public boolean isValidReceiverAndUpdateStatus(GmsSend gmsSend) {
        String receiver = gmsSend.getReceiver();
        if (!isValidReceiver(receiver)) {
            gmsSend.setRsltCode("9-9_9-9");
            gmsSend.setStatus(someError);
            gmsSendRepository.saveAndFlush(gmsSend);
            return false;
        }

        String countryCode = gmsSend.getCountryCode();
        StringBuilder sb = new StringBuilder();
        if (receiver.startsWith("0") && !countryCode.equals("39")) { // 39 (이탈리아) 는 제일 앞에 0 을 제외하지 않음 + 아르헨티나는 좀 특이한데 고려 안한 상태
            sb.append("+").append(countryCode).append(receiver.substring(1));
        } else {
            sb.append("+").append(countryCode).append(receiver);
        }
        gmsSend.setDestination(sb.toString());
        gmsSend.setStatus(beforeRequest);

        gmsSendRepository.saveAndFlush(gmsSend);
        return true;
    }

    /**
     * Receiver의 유효성 검증 메서드
     * @param receiver = 검증할 변수
     * @return boolean = 유효하면 true / 아니면 false
     */
    private boolean isValidReceiver(String receiver) {
        String regex = "^[0-9-]{1,20}$";
        return receiver != null && Pattern.matches(regex, receiver);
    }

    /**
     * GmsSend  -> 요청을 위한 포맷 변경 메서드
     * @param gmsSend 요청할 문자 데이터
     * @return GmsRequestDTO.RequestSendingMessageDto = 요청용 포맷 데이터
     */
    public GmsRequestDTO.RequestSendingMessageDto changeDataFormatForRequestSendMessage(GmsSend gmsSend) {
        List<GmsRequestDTO.Messages> messageList = new ArrayList<>();
        List<GmsRequestDTO.Destinations> destinationsList = new ArrayList<>();
        destinationsList.add(
                GmsRequestDTO.Destinations.builder()
                        .to(gmsSend.getDestination())
                        .build());
        messageList.add(
                GmsRequestDTO.Messages.builder()
                        .sender(gmsSend.getSender())
                        .destinations(destinationsList)
                        .content(GmsRequestDTO.Contents.builder()
                                .text(gmsSend.getText())
                                .build())
                        .build());
        return GmsRequestDTO.RequestSendingMessageDto.builder()
                .messages(messageList)
                .build();
    }

    /**
     * Infobip API 요청 및 GmsSend 배열 Status 변경 메서드 중개 메서드
     * @param request = 전송할 문자 데이터
     * @param gmsSend = 전송할 문자 데이터 포맷팅 전 GmsSend
     */
    @Transactional
    public void requestSendingMessageAndChangeStatus(GmsRequestDTO.RequestSendingMessageDto request, GmsSend gmsSend) throws JsonProcessingException {
        // Request
        String response = infobipService.requestSendingMessage(request);
        // Get
        String messageId = getMessageIdFromResponse(response);

        gmsSend.setStatus(afterRequestAndWaitLogCheck);
        gmsSend.setMessageId(messageId);

        gmsSendRepository.saveAndFlush(gmsSend);
    }

    private String getMessageIdFromResponse(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        GmsResponseDTO.ResponseSendMessage responseSendMessage = mapper.readValue(response, GmsResponseDTO.ResponseSendMessage.class);
        return responseSendMessage.getMessages().get(0).getMessageId();
    }

    /**
     * Infobip API 요청 및 GmsSend Status 변경 메서드 중개 메서드
     * @param gmsSend = 체크할 GmsSend 데이터
     * @return 전송 완료됐다면 True, 아니라면 False
     */
    @Transactional
    public boolean requestGetMessageLogAndCheckSent(GmsSend gmsSend) throws JsonProcessingException {
        // Log Get Api 호출
        String response = infobipService.requestGetMessageLog(gmsSend.getMessageId());
        // Result 나왔다면 If 문 실행
        if (!getResponseFromLog(response).getResults().isEmpty()) {
            gmsSend.setStatus(completeLogCheckAndWaitMove);
            gmsSendRepository.saveAndFlush(gmsSend);
            return true;
        }
        return false;
    }

    /**
     * infobip API 요청에 대한 응답값(String)에서 데이터 추출을 할 수 있도록 변환해주는 메서드
     * @param response : api 요청에 대한 응답값
     * @return GmsResponseDTO.ResponseGetLog
     * @throws JsonProcessingException
     */
    private GmsResponseDTO.ResponseGetLog getResponseFromLog(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, GmsResponseDTO.ResponseGetLog.class);
    }

    /**
     * infobip에게 받은 로그 데이터를 바탕으로 GmsSend를 갱신, GmsLog 테이블로 데이터 복사, Status 변경 메서드
     * @param gmsSend
     * @throws JsonProcessingException
     */
    @Transactional
    public void reflectResultByLogAndMoveDataToGmsLog(GmsSend gmsSend) throws JsonProcessingException {
        GmsResponseDTO.ResponseGetLog responseGetLog = getResponseFromLog(infobipService.requestGetMessageLog(gmsSend.getMessageId()));

        if (!responseGetLog.getResults().isEmpty()) {
            // GmsSend 반영 메서드
            GmsSend reflectedGmsSend = reflectResultToGmsSend(gmsSend, responseGetLog);

            // GmsSend -> GmsLog
            gmsLogService.insertSendDataToLogTable(reflectedGmsSend);

            // Status 변경
            gmsSend.setStatus(completeMoveAndWaitDelete);

            gmsSendRepository.saveAndFlush(gmsSend);
        }
    }

    /**
     * 로그 데이터를 바탕으로 GmsSend 데이터 갱신하는 메서드
     * @param gmsSend
     * @param response
     * @return GmsSend
     */
    private GmsSend reflectResultToGmsSend(GmsSend gmsSend, GmsResponseDTO.ResponseGetLog response) {
        GmsResponseDTO.ResponseGetLog.Results results = response.getResults().get(0);

        gmsSend.setCurrency(results.getPrice().getCurrency());
        gmsSend.setPricePerMessage(results.getPrice().getPricePerMessage());
        gmsSend.setMessageCount(results.getMessageCount());
        gmsSend.setTotalPrice(results.getPrice().getPricePerMessage() * results.getMessageCount());
        gmsSend.setDoneAt(changeTimeFormat(results.getDoneAt()));
        gmsSend.setRsltCode(results.getStatus().getGroupId() + "-" + results.getStatus().getId() + "_" +
                results.getError().getGroupId() + "-" + results.getError().getId());
        gmsSend.setRsltStatusMemo(results.getStatus().getDescription());
        gmsSend.setRsltErrorMemo(results.getError().getDescription());
        gmsSend.setStatus(complete);

        return gmsSend;
    }

    /**
     * 전송 전 유효성 검사 등을 통해 에러처리가 된 데이터를 GmsLog 테이블로 옮기기 위한 메서드ㅡ
     * @param gmsSend
     */
    @Transactional
    public void moveErrorDataToGmsLog(GmsSend gmsSend) {
        GmsSend errorGmsSend = errorResultToGmsSend(gmsSend);

        // GmsSend -> GmsLog
        gmsLogService.insertSendDataToLogTable(errorGmsSend);

        // Status 변경
        gmsSend.setStatus(completeMoveAndWaitDelete);

        gmsSendRepository.saveAndFlush(gmsSend);
    }

    /**
     * 에러 데이터를 바탕으로 GmsSend 데이터 업데이트 | 현재는 전화번호 포맷만 고려 -> 나중에 리팩토링 필요함
     * @param gmsSend
     * @return
     */
    private GmsSend errorResultToGmsSend(GmsSend gmsSend) {
        gmsSend.setCurrency("ERR");
        gmsSend.setPricePerMessage(0.0);
        gmsSend.setMessageCount(0.0);
        gmsSend.setTotalPrice(0.0);
        gmsSend.setDoneAt(gmsSend.getSendAt());
        gmsSend.setRsltStatusMemo("Receiver Format Error");
        gmsSend.setRsltErrorMemo("Receiver Format Error");
        gmsSend.setStatus(complete);

        return gmsSend;
    }

    /**
     * 로그로 넘어온 시간값의 포맷을 LocalDateTime으로 변경해주는 메서드
     * @param beforeTime
     * @return
     */
    private LocalDateTime changeTimeFormat(String beforeTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        return OffsetDateTime.parse(beforeTime, formatter).toLocalDateTime().plusHours(9); // infobip은 UTC 기준, 한국은 UTC + 9
    }

    /**
     * Move 완료된 GmsSend 데이터 배열 삭제 메서드
     * @param gmsSendList = Move 완료된 상태 값을 가진 GmsSend 배열
     */
    @Transactional
    public void deleteGmsSendData(List<GmsSend> gmsSendList) {
        gmsSendRepository.deleteAll(gmsSendList);
    }
}
