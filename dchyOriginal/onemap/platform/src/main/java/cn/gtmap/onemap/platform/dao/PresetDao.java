package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.Preset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/1/19 13:46
 */
public interface PresetDao extends JpaRepository<Preset,String>,JpaSpecificationExecutor<Preset> {
    List<Preset> findByProId(String proid);

    List<Preset> findByIndexCode(String indexcode);

}
