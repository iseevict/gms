package emfoplus.gms.domain.gms_log.sql;

public class GmsLogSqlTemplate {

    /**
     * @param tableName = 테이블명
     * @return = 로그 테이블 생성 쿼리문
     */
    public static String getCreateTableQuery(String tableName) {
        return "CREATE TABLE " + tableName + " (" +
                "msgSeq BIGINT NOT NULL PRIMARY KEY, " +
                "messageId VARCHAR(255) NOT NULL, " +
                "networkId VARCHAR(255), " +
                "sender VARCHAR(20) NOT NULL, " +
                "receiver VARCHAR(40) NOT NULL, " +
                "countryCode VARCHAR(10) NOT NULL, " +
                "text VARCHAR(70) NOT NULL, " +
                "destination VARCHAR(100) NOT NULL, " +
                "sendAt DATETIME NOT NULL, " +
                "doneAt DATETIME NOT NULL, " +
                "messageCount INT NOT NULL, " +
                "pricePerMessage DOUBLE NOT NULL, " +
                "currency VARCHAR(10) NOT NULL, " +
                "rsltCode VARCHAR(20) NOT NULL, " +
                "rsltStatusMemo VARCHAR(255) NOT NULL, " +
                "rsltErrorMemo VARCHAR(255) NOT NULL, " +
                "emfoPrice DOUBLE NOT NULL, " +
                "totalPrice DOUBLE NOT NULL, " +
                "status VARCHAR(1) NOT NULL" +
                ")";
    }

    /**
     * @param tableName = 테이블명
     * @return = 타깃 로그 테이블이 존재하는지 확인하는 쿼리문
     */
    public static String getCheckTableQuery(String tableName) {
        return "SELECT COUNT(*) FROM information_schema.tables " +
                "WHERE table_schema = DATABASE() AND table_name = '" + tableName + "'";
    }
}
