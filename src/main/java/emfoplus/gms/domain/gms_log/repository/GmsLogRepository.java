package emfoplus.gms.domain.gms_log.repository;

import emfoplus.gms.domain.gms_log.entity.GmsLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GmsLogRepository extends JpaRepository<GmsLog, Long> {
}
