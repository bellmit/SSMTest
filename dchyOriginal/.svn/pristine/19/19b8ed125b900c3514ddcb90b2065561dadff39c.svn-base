package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.RecogWhitelist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/12/25 (c) Copyright gtmap Corp.
 */
public interface CameraRecogWhitelistDao extends JpaRepository<RecogWhitelist, String> {

    List<RecogWhitelist> findAllByProject(String proid);
}
