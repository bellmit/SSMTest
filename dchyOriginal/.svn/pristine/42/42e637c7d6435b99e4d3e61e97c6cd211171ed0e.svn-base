package cn.gtmap.onemap.platform.entity;

import cn.gtmap.onemap.core.entity.AbstractEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-25 下午3:50
 */
@Entity
@Table(name = "omp_file_store")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FileStore extends AbstractEntity {

    @Column(name = "file_size", nullable = true, precision = 10)
    private double fileSize;

    @Column(name = "parent_id", length = 32)
    private String parentId;

    @Column(nullable = true, length = 512)
    private String path;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    public FileStore() {
        this.setCreateAt(new Date());
        this.setCreateTime(new Date());
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
