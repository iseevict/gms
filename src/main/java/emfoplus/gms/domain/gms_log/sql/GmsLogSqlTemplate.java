package emfoplus.gms.domain.gms_log.sql;

import emfoplus.gms.domain.gms_send.entity.GmsSend;

public class GmsLogSqlTemplate {

    /**
     * @param tableName = 테이블명
     * @return = 로그 테이블 생성 쿼리문
     */
    public static String getCreateTableQuery(String tableName) {
        StringBuilder sb = new StringBuilder();
        return sb.append("CREATE TABLE ").append(tableName).append(" (")
                .append("msg_seq BIGINT NOT NULL PRIMARY KEY, ")
                .append("message_id VARCHAR(255) NOT NULL, ")
                .append("sender VARCHAR(20) NOT NULL, ")
                .append("receiver VARCHAR(40) NOT NULL, ")
                .append("country_code VARCHAR(10) NOT NULL, ")
                .append("text VARCHAR(70) NOT NULL, ")
                .append("destination VARCHAR(100) NOT NULL, ")
                .append("send_at DATETIME NOT NULL, ")
                .append("done_at DATETIME NOT NULL, ")
                .append("message_count INT NOT NULL, ")
                .append("price_per_message DOUBLE NOT NULL, ")
                .append("currency VARCHAR(10) NOT NULL, ")
                .append("rslt_code VARCHAR(20) NOT NULL, ")
                .append("rslt_status_memo VARCHAR(255) NOT NULL, ")
                .append("rslt_error_memo VARCHAR(255) NOT NULL, ")
                .append("total_price DOUBLE NOT NULL, ")
                .append("status VARCHAR(1) NOT NULL")
                .append(")").toString();
    }

    /**
     * @param tableName = 테이블명
     * @return = 타깃 로그 테이블이 존재하는지 확인하는 쿼리문
     */
    public static String getCheckTableQuery(String tableName) {
        StringBuilder sb = new StringBuilder();
        /* 쿼리 사용 권한 체크 필요 */
        return sb.append("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = '")
                .append(tableName).append("'").toString();
    }
/* null Test */
    public static String insertSendDataToLog(String tableName, GmsSend gmsSend) {
        StringBuilder sb = new StringBuilder();
        return sb.append("INSERT INTO ").append(tableName).append(" (")
                .append("msg_seq, message_id, sender, receiver, country_code, destination, text, ")
                .append("message_count, price_per_message, currency, total_price, status, ")
                .append("rslt_code, rslt_status_memo, rslt_error_memo, send_at, done_at")
                .append(") VALUES (")
                .append(gmsSend.getMsgSeq()).append(", ")
                .append("'").append(gmsSend.getMessageId()).append("', ")
                .append("'").append(gmsSend.getSender()).append("', ")
                .append("'").append(gmsSend.getReceiver()).append("', ")
                .append("'").append(gmsSend.getCountryCode()).append("', ")
                .append("'").append(gmsSend.getDestination()).append("', ")
                .append("'").append(gmsSend.getText()).append("', ")
                .append(gmsSend.getMessageCount()).append(", ")
                .append(gmsSend.getPricePerMessage()).append(", ")
                .append("'").append(gmsSend.getCurrency()).append("', ")
                .append(gmsSend.getTotalPrice()).append(", ")
                .append("'").append(gmsSend.getStatus()).append("', ")
                .append("'").append(gmsSend.getRsltCode()).append("', ")
                .append("'").append(gmsSend.getRsltStatusMemo()).append("', ")
                .append("'").append(gmsSend.getRsltErrorMemo()).append("', ")
                .append("'").append(gmsSend.getSendAt()).append("', ")
                .append("'").append(gmsSend.getDoneAt())
                .append("')").toString();
    }
}
