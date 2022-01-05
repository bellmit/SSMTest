package cn.gtmap.onemap.server.dao;

import cn.gtmap.onemap.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Date;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-17
 */
public interface AuditLogDAO extends JpaRepository<AuditLog, String>, QueryDslPredicateExecutor<AuditLog> {
    @Modifying
    @Query("delete AuditLog where createAt > ?1 and createAt < ?2")
    void clean(Date startTime, Date endTime);
}
