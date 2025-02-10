package emfoplus.gms.domain.gms_log.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "GMS_LOG")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GmsLog {

    @Id
    @Column(nullable = false)
    private Long msgSeq;

    @Column(nullable = false)
    private String messageId;

    private String networkId;

    @Column(nullable = false, length = 20)
    private String sender;

    @Column(nullable = false, length = 40)
    private String receiver;

    @Column(nullable = false, length = 10)
    private String countryCode;

    @Column(nullable = false, length = 70)
    private String text;

    @Column(nullable = false, length = 100)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime sendAt;

    @Column(nullable = false)
    private LocalDateTime doneAt;

    @Column(nullable = false)
    private Integer messageCount;

    @Column(nullable = false)
    private Double pricePerMessage;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(nullable = false, length = 20)
    private String rsltCode;

    @Column(nullable = false)
    private String rsltStatusMemo;

    @Column(nullable = false)
    private String rsltErrorMemo;

    @Column(nullable = false)
    private Double emfoPrice;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false, length = 1)
    private String status;
}
