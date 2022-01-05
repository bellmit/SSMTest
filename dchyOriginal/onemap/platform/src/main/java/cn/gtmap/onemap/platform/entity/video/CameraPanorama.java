package cn.gtmap.onemap.platform.entity.video;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/4/13 13:41
 */
@Entity
@Table(name = "omp_camera_panorama")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CameraPanorama implements Comparable<CameraPanorama> {

    @Id
    @GeneratedValue(generator = "sort-uuid")
    @GenericGenerator(name = "sort-uuid", strategy = "cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator")
    @Column(length = 32)
    private String id;

    /***
     * 全景照片对应的日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createAt;
    /***
     * 关联的设备编码
     */
    @Column(nullable = false)
    private String indexCode;

    /***
     * 全景照片存放路径
     */
    @Column(nullable = false, length = 512)
    private String path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int compareTo(CameraPanorama cameraPanorama) {
        return this.getCreateAt().compareTo(cameraPanorama.getCreateAt());
    }
}
