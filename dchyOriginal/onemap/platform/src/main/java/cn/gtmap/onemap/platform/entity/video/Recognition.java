package cn.gtmap.onemap.platform.entity.video;

import cn.gtmap.onemap.core.entity.AbstractEntity;
import cn.gtmap.onemap.platform.entity.FileStore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 摄像头智能识别结果记录表
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/11/14 (c) Copyright gtmap Corp.
 */
@Entity
@Table(name = "omp_camera_recog")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Recognition extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -5710815570741177720L;

    /**
     * 关联的 camera
     */
    @Column(nullable = false)
    private String cameraId;
    /**
     * 识别结果文件 id
     */
    @Column(nullable = false)
    private String resultFile;

    /**
     * 原始照片文件 id
     */
    @Column(nullable = false)
    private String originFile;
    /**
     * 接口返回的结果
     */
    @Column(length = 2000)
    private String callResult;

    public Recognition() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /***
     * 0 预警
     * 1 违法用地
     * 2建设用地
     */

    @Column(nullable = true,columnDefinition = "INT default 0")
    private int status;

    public Recognition(String name, String cameraId, String resultFile, String originFile, String callResult) {
        this.setName(name);
        this.setCreateAt(new Date());
        this.cameraId = cameraId;
        this.resultFile = resultFile;
        this.originFile = originFile;
        this.callResult = callResult;
    }
    public Recognition(String name, String cameraId, String resultFile, String originFile) {
        this.setName(name);
        this.setCreateAt(new Date());
        this.cameraId = cameraId;
        this.resultFile = resultFile;
        this.originFile = originFile;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getResultFile() {
        return resultFile;
    }

    public void setResultFile(String resultFile) {
        this.resultFile = resultFile;
    }

    public String getOriginFile() {
        return originFile;
    }

    public void setOriginFile(String originFile) {
        this.originFile = originFile;
    }

    public String getCallResult() {
        return callResult;
    }

    public void setCallResult(String callResult) {
        this.callResult = callResult;
    }
}
