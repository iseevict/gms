package emfoplus.gms.domain.gms_log.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GmsLog {

    @NotNull
    private Long msgSeq;

    @NotNull
    private String messageId;

    private String networkId;

    @NotNull
    @Size(max = 20)
    private String sender;

    @NotNull
    @Size(max = 40)
    private String receiver;

    @NotNull
    @Size(max = 10)
    private String countryCode;

    @NotNull
    @Size(max = 70)
    private String text;

    @NotNull
    @Size(max = 100)
    private String destination;

    @NotNull
    private LocalDateTime sendAt;

    @NotNull
    private LocalDateTime doneAt;

    @NotNull
    private Integer messageCount;

    @NotNull
    private Double pricePerMessage;

    @NotNull
    @Size(max = 10)
    private String currency;

    @NotNull
    @Size(max = 20)
    private String rsltCode;

    @NotNull
    private String rsltStatusMemo;

    @NotNull
    private String rsltErrorMemo;

    @NotNull
    private Double emfoPrice;

    @NotNull
    private Double totalPrice;

    @NotNull
    @Size(max = 1)
    private String status;
}
