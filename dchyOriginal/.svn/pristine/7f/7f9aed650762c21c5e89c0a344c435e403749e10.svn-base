package cn.gtmap.onemap.platform.entity.video;

import cn.gtmap.onemap.core.entity.AbstractEntity;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * 巡查记录 实体
 *
 * @author <a href="zhayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 16-3-18 下午16:26
 */
@Entity
@Table(name = "omp_inspect_record")
public class InspectRecord  extends AbstractEntity {

    /**
     * 巡报号
     */
    @Column(nullable = true)
    private String recordNO;

    /**
     * 巡查人
     */
    @Column(nullable = false)
    private String inspector;

    /**
     * 其他巡查人
     */
    @Column
    private String otherInspector;

    /**
     * 基本事实
     */
    @Column
    private String fact;

    /**
     * 更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    /**
     * 是否疑似违法
     */
    @Column
    private boolean sfyswf = false;

    /**
     * 项目属性
     */
    @Lob
    @Column(name="property", columnDefinition="CLOB", nullable=true)
    private String property = "{}";

    /**
     * 每个记录关联的项目
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proId",nullable = false)
    private Project project;

    /**
     * 备注
     */
    @Lob
    @Column(columnDefinition = "CLOB", nullable = true)
    private String remark;

    /**
     * 巡查记录
     */
    @Column
    private String userId;

    /**
     * 关联的执法监察项目id
     */
    @Column
    private String leasProId;

    /**
     * 巡查区域
     */
    @Column
    private String inspectArea;

    /**
     * 被查单位（个人）
     */
    @Column
    private String inspectedUnit;

    /**
     * 部门id
     */
    private String organId;

    /**
     * 部门名称
     */
    private String organName;

    public String getRecordNO() {
        return recordNO;
    }

    public void setRecordNO(String recordNO) {
        this.recordNO = recordNO;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getOtherInspector() {
        return otherInspector;
    }

    public void setOtherInspector(String otherInspector) {
        this.otherInspector = otherInspector;
    }

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isSfyswf() {
        return sfyswf;
    }

    public void setSfyswf(boolean sfyswf) {
        this.sfyswf = sfyswf;
    }

    public Map getProperty() {
        if(this.property == null)
            return new HashedMap();
        return JSONObject.parseObject(property, Map.class);
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLeasProId() {
        return leasProId;
    }

    public void setLeasProId(String leasProId) {
        this.leasProId = leasProId;
    }

    public String getInspectArea() {
        return inspectArea;
    }

    public void setInspectArea(String inspectArea) {
        this.inspectArea = inspectArea;
    }

    public String getInspectedUnit() {
        return inspectedUnit;
    }

    public void setInspectedUnit(String inspectedUnit) {
        this.inspectedUnit = inspectedUnit;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }
}
