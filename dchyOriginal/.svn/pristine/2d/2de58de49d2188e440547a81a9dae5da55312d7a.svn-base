package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.CameraPanorama;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * camera panorama dao
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/4/13 14:08
 */
public interface CameraPanoramaDao  extends JpaRepository<CameraPanorama, String> {

    List<CameraPanorama> findByIndexCode(String indexCode);

    List<CameraPanorama> findByIndexCodeAndCreateAt(String indexCode,Date createAt);
}
