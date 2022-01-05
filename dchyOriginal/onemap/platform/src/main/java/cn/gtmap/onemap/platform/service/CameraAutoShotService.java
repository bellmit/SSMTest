package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.video.CameraAutoShot;

import java.util.Date;

/**
 * auto shot
 *
 * @author monarchCheng
 * @create 2018-01-05 19:11
 **/
public interface CameraAutoShotService {

    /**
     * 查询
     * @param id
     * @return
     */
    CameraAutoShot findById(String id);

    /**
     * save
     * @param cameraAutoShot
     * @return
     */
    CameraAutoShot save(CameraAutoShot cameraAutoShot);

    /**
     * 根据多个条件联合查询
     * @param indexCode
     * @param shotAt
     * @param shotNum
     * @param presetNum
     * @return
     */
    CameraAutoShot findByDetails(String indexCode, Date shotAt, int shotNum, int presetNum);

}
