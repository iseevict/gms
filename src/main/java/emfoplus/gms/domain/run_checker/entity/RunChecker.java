package emfoplus.gms.domain.run_checker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RunChecker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "run_checker_id")
    private Long id;

    @Column(nullable = false)
    private String threadName;

    @Column(nullable = false)
    private LocalDateTime inputTime;
}
