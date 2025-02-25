package emfoplus.gms.domain.gms_log.api;

import emfoplus.gms.domain.gms_send.dto.GmsRequestDTO;
import emfoplus.gms.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "InfobipApiClient", url = "https://4mwnkn.api.infobip.com", configuration = FeignClientConfig.class)
public interface InfobipApiClient {

    @PostMapping("/sms/3/messages")
    void requestSendingMessageToInfobip(@RequestBody GmsRequestDTO.requestSendingMessageDto request);

    @GetMapping("/sms/3/logs")
    String requestGetMessageLogToInfobip(@RequestParam("messageId") String messageId);
}
