package emfoplus.gms.domain.gms_send.repository;

import emfoplus.gms.domain.gms_send.entity.GmsSend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GmsSendRepository extends JpaRepository<GmsSend, Long> {
    Optional<List<GmsSend>> findAllByStatusAndSendAtLessThanEqual(String status, LocalDateTime dateTime);
}
