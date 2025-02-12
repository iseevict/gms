package emfoplus.gms.domain.gms_log.service;

import emfoplus.gms.domain.gms_log.sql.GmsLogSqlTemplate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GmsLogService {
    @PersistenceContext
    private EntityManager em;

    /**
     * 로그 테이블 존재하는지 확인 후 없으면 테이블 생성하는 함수
     */
    @Transactional
    public void checkAndCreateTable() {
        Thread.currentThread().setName("Single-Log-Table-Checker");

        String tableName = "GMS_LOG";
        Boolean isTable = isTable(tableName);

        if (!isTable) {
            String createQuery = GmsLogSqlTemplate.getCreateTableQuery(tableName);
            em.createNativeQuery(createQuery).executeUpdate();
            log.info("{} Table is created", tableName);
        } else {
            log.info("{} Table already exists", tableName);
        }
    }

    /**
     * 현재 날짜에 해당하는 월 로그 테이블과 다음 달 로그 테이블이 존재하는지 확인 후 없으면 테이블 생성하는 함수
     */
    @Transactional
    public void checkAndCreateMonthTable() {
        Thread.currentThread().setName("Month-Log-Table-Checker");

        String nowMonthTableName = "GMS_LOG_" + getMonth(0);
        String nextMonthTableName = "GMS_LOG_" + getMonth(1);

        Boolean isNowMonthTable = isTable(nowMonthTableName);
        Boolean isNextMonthTable = isTable(nextMonthTableName);

        if (!isNowMonthTable) {
            String createQuery = GmsLogSqlTemplate.getCreateTableQuery(nowMonthTableName);
            em.createNativeQuery(createQuery).executeUpdate();
            log.info("{} Table is created", nowMonthTableName);
        } else {
            log.info("{} Table already exists", nowMonthTableName);
        }

        if (!isNextMonthTable) {
            String createQuery = GmsLogSqlTemplate.getCreateTableQuery(nextMonthTableName);
            em.createNativeQuery(createQuery).executeUpdate();
            log.info("{} Table is created", nextMonthTableName);
        } else {
            log.info("{} Table already exists", nextMonthTableName);
        }
    }

    /**
     * 로타깃 그 테이블이 존재하는지 판단하는 함수
     * @param tableName = 테이블명
     * @return = True or False
     */
    private Boolean isTable(String tableName) {
        String checkQuery = GmsLogSqlTemplate.getCheckTableQuery(tableName);

        Number count = (Number) em
                .createNativeQuery(checkQuery)
                .getSingleResult();

        return count.intValue() != 0;
    }

    /**
     * 원하는 날짜의 "년도_달"을 가져오는 함수
     * @param plus = 현재 달에 추가하고 싶은 개월 수
     * @return = "yyyy_MM"
     */
    private String getMonth(int plus) {
        LocalDateTime now = LocalDateTime.now().plusMonths(plus);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM");

        return now.format(formatter);
    }
}
