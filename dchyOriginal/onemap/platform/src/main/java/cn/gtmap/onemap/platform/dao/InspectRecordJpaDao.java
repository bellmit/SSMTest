package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.InspectRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 2016/3/18
 */
public interface InspectRecordJpaDao extends JpaRepository<InspectRecord, String>,
        JpaSpecificationExecutor<InspectRecord> {

    @Query("from InspectRecord c where c.project.proId=?1")
    List<InspectRecord> getByproId(String proid);
}
