package emfoplus.gms.domain.gms_send.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class GmsResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseSendMessage {
        private List<ResponseMessage> messages;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ResponseMessage {
            private String messageId;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseGetLog {
        private List<Results> results;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Results {
            private String doneAt;
            private Double messageCount;
            private Price price;
            private Status status;
            private Error error;

            @Getter
            @NoArgsConstructor
            @AllArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Price {
                private Double pricePerMessage;
                private String currency;
            }

            @Getter
            @NoArgsConstructor
            @AllArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Status {
                private String groupId;
                private String groupName;
                private String id;
                private String name;
                private String description;
            }

            @Getter
            @NoArgsConstructor
            @AllArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Error {
                private String groupId;
                private String groupName;
                private String id;
                private String name;
                private String description;
            }
        }
    }

}
