package emfoplus.gms.domain.gms_send.service;

import emfoplus.gms.domain.gms_send.repository.GmsSendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GmsSendService {
    private final GmsSendRepository gmsSendRepository;

}
