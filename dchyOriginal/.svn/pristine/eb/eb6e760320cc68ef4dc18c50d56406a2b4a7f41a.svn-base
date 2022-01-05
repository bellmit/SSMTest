package cn.gtmap.onemap.platform.entity.video;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author ruihui.li
 * @version V1.0
 * @Description: 摄像头的多方位拍照任务实体类
 * @date 2018/4/17
 */
@Data
@Entity
@Table(name = "omp_camera_dfw_task")
public class CameraDfwTask {

    @Id
    @GeneratedValue(generator = "sort-uuid")
    @GenericGenerator(name = "sort-uuid", strategy = "cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator")
    @Column(length = 32)
    private String id;

    /**
     * 是否开启多方位拍照
     */
    @Column
    private boolean enable;

    @Column(name = "index_code")
    private String indexCode;

    @Column
    private Date time;

}
