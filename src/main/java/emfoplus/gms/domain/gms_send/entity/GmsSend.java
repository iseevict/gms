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
}
