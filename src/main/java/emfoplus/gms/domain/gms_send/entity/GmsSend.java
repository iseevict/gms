package emfoplus.gms.domain.gms_send.entity;

import jakarta.persistence.*;
import lombok.*;

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

    /**
     * -- SETTER --
     *  messageId 값 변경 메서드
     *
     * @param messageId = 변경 후 messageId 값
     */
    @Setter
    private String messageId;

    @Column(nullable = false, length = 20)
    private String sender;

    @Column(nullable = false, length = 20)
    private String receiver;

    @Column(nullable = false, length = 10)
    private String countryCode;

    @Column(nullable = false, length = 70)
    private String text;

    /**
     * -- SETTER --
     *  Destination 값 변경 메서드
     *
     * @param afterDestination = 변경 후 Destination 값
     */
    @Setter
    @Column(length = 100)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime sendAt;

    @Setter
    private LocalDateTime doneAt;

    @Setter
    private LocalDateTime requestAt;

    @Setter
    private Double messageCount;

    @Setter
    private Double pricePerMessage;

    @Setter
    @Column(length = 10)
    private String currency;

    @Setter
    @Column(length = 20)
    private String rsltCode;

    @Setter
    private String rsltStatusMemo;

    @Setter
    private String rsltErrorMemo;

    /*@Setter
    private Double emfoPrice;*/ // 아직 몰라서 주석처리

    @Setter
    private Double totalPrice;

    @Setter
    @Column(length = 1)
    private String status;

}
