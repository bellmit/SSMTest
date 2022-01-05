package cn.gtmap.onemap.platform.entity.video;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 识别白名单
 * 记录 不需要进行识别的 project id 和关联的 camera id
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/12/25 (c) Copyright gtmap Corp.
 */
@Entity
@Table(name = "omp_camera_recog_whitelist")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class RecogWhitelist implements Serializable {

    private static final long serialVersionUID = -3423184467253814543L;

    @Id
    @GeneratedValue(generator = "sort-uuid")
    @GenericGenerator(name = "sort-uuid", strategy = "cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator")
    @Column(length = 32)
    private String id;

    @Column(name = "camera_id", length = 32, nullable = false)
    private String camera;

    @Column(name = "pro_id", length = 32, nullable = false)
    private String project;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createAt;

    public RecogWhitelist() {
        this.setCreateAt(new Date());
    }

    public RecogWhitelist(String camera, String project) {
        this.setCreateAt(new Date());
        this.camera = camera;
        this.project = project;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
