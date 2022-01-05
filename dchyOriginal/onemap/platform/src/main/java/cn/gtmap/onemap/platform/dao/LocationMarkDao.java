package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.LocationMark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-29 上午9:42
 */
public interface LocationMarkDao extends JpaRepository<LocationMark, String> {

    /**
     * 根据所有者获取LocationMark
     * @param owner
     * @return
     */
    List<LocationMark> findByOwner(String owner);

    /**
     * 根据公开性获取LocationMark
     * @param publicity
     * @return
     */
    List<LocationMark> findByPublicity(String publicity);

    /**
     * 根据所有者以及公开性获取LocationMark
     * @param owner
     * @param publicity
     * @return
     */
    List<LocationMark> findByOwnerOrPublicity(String owner,String publicity);
}
