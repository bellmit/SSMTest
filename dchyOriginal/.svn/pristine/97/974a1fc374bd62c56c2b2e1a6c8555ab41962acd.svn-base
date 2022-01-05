package cn.gtmap.onemap.platform.entity.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * .镜头离线持续时间
 *
 * @author <a href="mailto:zhaozhuyi@gtmap.cn">zhaozhuyi</a>
 * @version v1.0, 2018/1/29 (c) Copyright gtmap Corp.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "omp_camera_offline_duration")
public class CameraOfflineDuration  implements Serializable {

    private static final long serialVersionUID = 5974457380316212886L;

    @Id
    @GeneratedValue(generator = "sort-uuid")
    @GenericGenerator(name = "sort-uuid", strategy = "cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator")
    @Column(length = 32)
    private String id;

    @Column(length = 256, nullable = false)
    private String name;

    @Column(name = "device_id", length = 256, nullable = false)
    private String indexCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createAt;

    /**
     * 持续时间(单位: 秒)
     */
    @Column(nullable = false)
    private long duration;

    /**
     * camera
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="device_id", referencedColumnName="device_id",insertable = false,updatable = false)
    private Camera camera;

}
