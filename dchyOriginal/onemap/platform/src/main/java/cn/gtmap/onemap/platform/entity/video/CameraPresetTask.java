package cn.gtmap.onemap.platform.entity.video;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author ruihui.li
 * @version V1.0
 * @Description: 摄像头预设位拍照任务
 * @date 2018/8/8
 */
@Data
@Entity
@Table(name = "omp_camera_preset_task")
public class CameraPresetTask {

    @Id
    @GeneratedValue(generator = "sort-uuid")
    @GenericGenerator(name = "sort-uuid", strategy = "cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator")
    @Column(length = 32)
    private String id;

    /**
     * 设备编码(即 镜头ID)
     */
    @Column(name = "device_id", unique = false, length = 48, nullable = false)
    private String indexCode;

//    @Column(length=32,nullable = true)
//    private String presetId;

    @Column
    private String presetName;

    /***
     * 预设位序号
     */
    @Column(name = "preset_no", length=10,nullable = false)
    private int pNo;

    @Column
    private boolean enable;

    @Column
    private Date time;
}
