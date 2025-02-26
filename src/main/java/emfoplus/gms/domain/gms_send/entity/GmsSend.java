package emfoplus.gms.domain.gms_send.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Entity
@Table(name = "GMS_SEND")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GmsSend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long msgSeq;

    private String messageId;

    private String networkId;

    @Column(nullable = false, length = 20)
    private String sender;

    @Column(nullable = false, length = 20)
    private String receiver;

    @Column(nullable = false, length = 10)
    private String countryCode;

    @Column(nullable = false, length = 70)
    private String text;

    @Column(length = 100)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime sendAt;

    private LocalDateTime doneAt;

    private Integer messageCount;

    private Double pricePerMessage;

    @Column(length = 10)
    private String currency;

    @Column(length = 20)
    private String rsltCode;

    private String rsltStatusMemo;

    private String rsltErrorMemo;

    private Double emfoPrice;

    private Double totalPrice;

    @Column(length = 1)
    private String status;

    /**
     * Status 값 변경 메서드
     * @param afterStatus = 변경 후 Status 값
     */
    public void setStatus(String afterStatus) {
        this.status = afterStatus;
    }

    /**
     * Destination 값 변경 메서드
     * @param afterDestination = 변경 후 Destination 값
     */
    public void setDestination(String afterDestination) { this.destination = afterDestination; }

    /**
     * RsltCode 값 변경 메서드
     * @param afterRsltCode = 변경 후 RsltCode 값
     */
    public void setRsltCode(String afterRsltCode) { this.rsltCode = afterRsltCode; }
}
