package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.ProjectCameraRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhayuwen on 2016/8/5.
 */
public interface ProjectCameraRefDao extends JpaRepository<ProjectCameraRef,String> {
    /**
     * 删除项目关联的摄像头关系
     * @param proId
     */
    @Transactional
    @Modifying
    @Query("delete from ProjectCameraRef projectCameraRef where projectCameraRef.proId=?1")
    void deleteByProId(String proId);

    /**
     * find by proid
     * @param proId
     * @return
     */
    List<ProjectCameraRef> findByProId(String proId);


    /**
     * 查询项目关联摄像头以及预设位关系
     * @param proId
     * @param cameraId
     * @param PresetId
     * @return
     */
    List<ProjectCameraRef> findByProIdAndCameraIdAndPresetId(String proId, String cameraId, String PresetId);

    /**
     * 删除项目关联摄像头以及预设位关系
     * @param proId
     * @param cameraId
     * @param PresetId
     */
    @Transactional
    @Modifying
    void deleteByProIdAndCameraIdAndPresetId(String proId, String cameraId, String PresetId);

    /**
     * 删除预设位的关联关系
     * @param proId
     * @param cameraId
     * @param PresetId
     */
    @Transactional
    @Modifying
    @Query("update ProjectCameraRef projectCameraRef set  projectCameraRef.presetId=null where projectCameraRef.proId=?1 and " +
            "projectCameraRef.cameraId=?2 and projectCameraRef.presetId = ?3")
    void deletePresetRef(String proId, String cameraId, String PresetId);

    /**
     * 删除一个项目的关联的一个摄像头信息
     * @param proId
     * @param cameraId
     */
    @Transactional
    @Modifying
    void deleteByProIdAndCameraId(String proId, String cameraId);

    /**
     * 根据cameraId查询关系
     * @param cameraId
     * @return
     */
    List<ProjectCameraRef> findByCameraId(String cameraId);

    /**
     *  根据cameraId的集合查询所有项目
     * @param cameraIds
     * @return
     */
    @Query("from ProjectCameraRef pr where pr.cameraId in?1")
    List<ProjectCameraRef> findByCameraIdList(List<String> cameraIds);

}
