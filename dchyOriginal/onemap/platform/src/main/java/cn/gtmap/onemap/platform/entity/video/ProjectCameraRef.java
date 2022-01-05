package cn.gtmap.onemap.platform.entity.video;

import cn.gtmap.onemap.core.entity.AbstractEntity;
import cn.gtmap.onemap.platform.entity.video.Project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 *  项目 - 摄像头 - 预设位 关联关系表
 *
 * @author <a href="mailto:zhayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 2016-08-05 09:43:00
 */
@Entity
@Table(name = "omp_project_camera_ref")
public class ProjectCameraRef extends AbstractEntity {

    public ProjectCameraRef() {

    }

    public ProjectCameraRef(Project project, String cameraId, String presetId) {
        this.setName(project.getName());
        this.setCreateAt(new Date());
        this.proId = project.getId();
        this.cameraId = cameraId;
        this.presetId = presetId;
    }

    /**
     * 关联项目的proId
     */
    @Column(name = "proid",nullable = false)
    private String proId;

    /**
     * 关联摄像头indexCode
     */
    @Column(name = "camera_id", nullable = false)
    private String cameraId;

    /**
     * 关联预设位的主键id
     */
    @Column(name = "preset_id")
    private String presetId;

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getPresetId() {
        return presetId;
    }

    public void setPresetId(String presetId) {
        this.presetId = presetId;
    }
}
