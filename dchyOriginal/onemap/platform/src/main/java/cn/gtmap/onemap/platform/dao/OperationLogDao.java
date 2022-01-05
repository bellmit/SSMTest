package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 *  *
 * @author <a href="mailto:zhayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 2016-08-18 10.19:00
 */
public interface OperationLogDao extends JpaRepository<OperationLog, String>, JpaSpecificationExecutor<OperationLog> {

}
