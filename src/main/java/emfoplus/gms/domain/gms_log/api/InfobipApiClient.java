package emfoplus.gms.domain.gms_log.api;

import emfoplus.gms.domain.gms_send.dto.GmsRequestDTO;
import emfoplus.gms.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "InfobipApiClient", url = "https://4mwnkn.api.infobip.com", configuration = FeignClientConfig.class)
public interface InfobipApiClient {

    @PostMapping("/sms/3/messages")
    String requestSendingMessageToInfobip(@RequestBody GmsRequestDTO.requestSendingMessageDto request);
}
