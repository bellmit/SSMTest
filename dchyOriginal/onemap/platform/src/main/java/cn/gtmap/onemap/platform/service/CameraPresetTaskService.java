package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.video.CameraPresetTask;
import cn.gtmap.onemap.platform.entity.video.Preset;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ruihui.li
 * @version V1.0
 * @Description:
 * @date 2018/8/8
 */
public interface CameraPresetTaskService {

    /**
     * 保存预置位拍照任务且保存新的预置位
     * @param cameraPresetTask
     */
    boolean save(CameraPresetTask cameraPresetTask);

    /**
     * find by identify
     * @param id
     * @return
     */
    CameraPresetTask findById(String id);

    List<CameraPresetTask> findByIndexCodes(String indexCode);

    void delById(String id);

    boolean modifyTask(String id, Date time);

    /**
     * 更改预设位拍照任务的状态
     * @param id
     * @param enable
     * @return
     */
    boolean changeTask(String id,boolean enable);

    List<Map<String,Object>> getPic(String id,final Date startTime, final Date endTime);
}
