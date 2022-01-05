package cn.gtmap.onemap.platform.entity.video;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 摄像头自动拍照
 *
 * @author monarchCheng
 * @create 2018-01-05 17:10
 **/
@Entity
@Table(name = "omp_camera_auto_shot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CameraAutoShot {
    @Id
    @GeneratedValue(generator = "sort-uuid")
    @GenericGenerator(name = "sort-uuid", strategy = "cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator")
    @Column(length = 32)
    private String id;

    /**
     * 设备编号
     */
    @Column(length = 255, nullable = false,name = "device_id")
    private String indexCode;

    /**
     * 预设拍照序号 1-8
     */
    @Column(nullable = false,name = "preset_num")
    private int presetNum;

    /**
     * 拍摄日期
     */
    @Column(nullable = false,name = "shot_at")
    @Temporal(TemporalType.DATE)
    private Date shotAt;

    /**
     * 当天第几次拍摄
     */
    @Column(nullable = false,name = "shot_num")
    private int shotNum;

    /**
     * 关联摄像头
     */
    @Transient
    private Camera camera;

    @Column(nullable = true,name = "count")
    private int count;

    /**
     * 拍摄所得照片关联的filestore 的 id
     */
    @Column(name = "img_id")
    private String imgId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public int getPresetNum() {
        return presetNum;
    }

    public void setPresetNum(int presetNum) {
        this.presetNum = presetNum;
    }

    public Date getShotAt() {
        return shotAt;
    }

    public void setShotAt(Date shotAt) {
        this.shotAt = shotAt;
    }

    public int getShotNum() {
        return shotNum;
    }

    public void setShotNum(int shotNum) {
        this.shotNum = shotNum;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
