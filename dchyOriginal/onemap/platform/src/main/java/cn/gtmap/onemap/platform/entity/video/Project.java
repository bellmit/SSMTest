package cn.gtmap.onemap.platform.entity.video;

import cn.gtmap.onemap.core.entity.AbstractEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 项目实体
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/11/24 11:26
 */
@Entity
@Table(name = "omp_project")
public class Project extends AbstractEntity {

    /***
     * 项目id
     */
    @Column(length = 32,nullable = false)
    private String proId;
    /**
     * 项目名称
     */
    @Column(length = 200, nullable = true)
    private String proName;

    /***
     * 项目类型
     */
    @Column(length = 50,nullable = false)
    private String proType;

    /***
     * 项目状态
     * 0--无状态 新建
     * 1--待办
     * 2--已办
     */
    @Column(nullable = false)
    private int status=0;

    /***
     * 项目位置 geojson
     */
    @Column
    private String location;

    /**
     * 最近更新时间
     */
    @Column
    private Date updatedTime;
    
    /**
     * 标记时间
     */
    @Column
    private Date markedTime;

    /***
     * 关联摄像头的indexCode
     */
    @Column(length = 50)
    private String cameraId;

    /***
     * 异常类型
     */
    @Column(length = 50)
    private String exType;
    /***
     * 异常描述
     */
    @Column(length = 500)
    private String exDesc;

    /***
     * 处理结果/意见
     */
    @Column(length = 500)
    private String handleResult;

    /**
     * 所有者id
     */
    @Column(length = 50)
    private String ownerId;

    /**
     * 所有者行政区代码
     */
    @Column(length=50)
    private String xzqdm;

    @Column(length=20)
    private String year;

    @Column(length = 100)
    private String associateBlockName;

    /**
     * 关联预设位记录
     */
    @OneToMany(mappedBy = "proId",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @OrderBy("createAt DESC")
    private Set<Preset> presets=new HashSet<Preset>();


    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getExType() {
        return exType;
    }

    public void setExType(String exType) {
        this.exType = exType;
    }

    public String getExDesc() {
        return exDesc;
    }

    public void setExDesc(String exDesc) {
        this.exDesc = exDesc;
    }


    public Set<Preset> getPresets() {
        return presets;
    }

    public void setPresets(Set<Preset> presets) {
        this.presets = presets;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getXzqdm() {
        return xzqdm;
    }

    public void setXzqdm(String xzqdm) {
        this.xzqdm = xzqdm;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAssociateBlockName() {
        return associateBlockName;
    }

    public void setAssociateBlockName(String associateBlockName) {
        this.associateBlockName = associateBlockName;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Project && getProId().equals(((Project) o).getProId());
    }

	public Date getMarkedTime() {
		return markedTime;
	}

	public void setMarkedTime(Date markedTime) {
		this.markedTime = markedTime;
	}
}
