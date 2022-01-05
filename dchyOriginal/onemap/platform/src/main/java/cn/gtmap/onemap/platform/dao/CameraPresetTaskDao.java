package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.CameraPresetTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author ruihui.li
 * @version V1.0
 * @Description:
 * @date 2018/8/8
 */
public interface CameraPresetTaskDao extends JpaRepository<CameraPresetTask,String>,JpaSpecificationExecutor<CameraPresetTask>{

    CameraPresetTask findById(String s);

    List<CameraPresetTask> findByIndexCode(String indexCode);

    @Override
    void delete(CameraPresetTask task);

    List<CameraPresetTask> findAllByEnable(boolean enable);

    @Override
    List<CameraPresetTask> findAll();

}
