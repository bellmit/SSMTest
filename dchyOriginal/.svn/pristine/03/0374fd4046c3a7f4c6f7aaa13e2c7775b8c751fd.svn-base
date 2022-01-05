package cn.gtmap.onemap.platform.entity.video;

import cn.gtmap.onemap.core.entity.AbstractEntity;

import javax.persistence.*;

/**
 * 视频预设位实体
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/1/19 8:57
 */
@Entity
@Table(name = "omp_preset")
public class Preset extends AbstractEntity{

    /***
     * 预设位序号
     */
    @Column(length=10,nullable = false)
    private int presetNo;
    /***
     * 关联的监控探头indexCode
     */
    @Column(nullable = false)
    private String indexCode;
    /**
     * 关联的项目id
     */
    @Column(length = 32,nullable = false)
    private String proId;

    @Column
    private Double x;

    @Column
    private Double y;

    @Column
    private Double z;

    @Transient
    private String proName;


    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public int getPresetNo() {
        return presetNo;
    }

    public void setPresetNo(int presetNo) {
        this.presetNo = presetNo;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Preset && getIndexCode().equals(((Preset) o).getIndexCode());
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }
}
