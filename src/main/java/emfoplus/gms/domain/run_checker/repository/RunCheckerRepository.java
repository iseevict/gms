package emfoplus.gms.domain.run_checker.repository;

import emfoplus.gms.domain.run_checker.entity.RunChecker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunCheckerRepository extends JpaRepository<RunChecker, Long> {
}
