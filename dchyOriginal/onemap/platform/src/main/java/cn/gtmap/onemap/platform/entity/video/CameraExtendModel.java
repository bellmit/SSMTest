package cn.gtmap.onemap.platform.entity.video;

import com.alibaba.fastjson.JSON;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public class CameraExtendModel implements Serializable, Cloneable {
    private String id;

    /**
     * 监控点名称
     */
    @Column(length = 256, nullable = false)
    private String name;

    /**
     * 设备编码(即 镜头ID)
     * 预览视频必须字段 值具有唯一性
     */
    @Column(name = "device_id", unique = true, length = 48, nullable = false)
    private String indexCode;



    /**
     * 控制单元id
     * 预览视频必须字段
     */
    @Column(name = "vcu_id", length = 48, nullable = false)
    private String vcuId;

    @Column(name = "file_id", length = 48, nullable = true)
    private String fileId;

    /**
     * 平台名称
     */
    @Column(name = "device_plat", nullable = false)
    private String platform;

    /**
     * x坐标
     */
    @Column(scale = 6, nullable = false)
    private double x;

    /**
     * y坐标
     */
    @Column(scale = 6, nullable = false)
    private double y;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createAt;

    /**
     * 设备类型 normal/mobile
     */
    @Column(name = "device_type")
    private String type = "normal";

    /**
     * 设备状态
     * 0：不在线
     * 1：在线
     */
    @Column(name = "device_status")
    private int status = 1;

    /**
     * 设备ip
     */
    @Column(name = "device_ip")
    private String ip;

    /**
     * 设备访问端口
     */
    @Column(name = "device_port")
    private String port;

    /**
     * 高度
     */
    @Column(name = "device_height")
    private double height;

    public String getPROTYPE() {
        return PROTYPE;
    }

    public void setPROTYPE(String PROTYPE) {
        this.PROTYPE = PROTYPE;
    }

    @Column(name = "PROTYPE")
    private String PROTYPE;



    /**
     * 所属监控区域名称 关联到 camera_region
     */
    @Column(name = "region_name", length = 256, nullable = false)
    private String regionName;


    public double getHeight() {
        return height;
    }

    public cn.gtmap.onemap.platform.entity.video.CameraExtendModel setHeight(double height) {
        this.height = height;
        return this;
    }

    public String getId() {
        return id;
    }

    public cn.gtmap.onemap.platform.entity.video.CameraExtendModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getVcuId() {
        return vcuId;
    }

    public cn.gtmap.onemap.platform.entity.video.CameraExtendModel setVcuId(String vcuId) {
        this.vcuId = vcuId;
        return this;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public cn.gtmap.onemap.platform.entity.video.CameraExtendModel setIndexCode(String indexCode) {
        this.indexCode = indexCode;
        return this;
    }

    public String getRegionName() {
        return regionName;
    }

    public cn.gtmap.onemap.platform.entity.video.CameraExtendModel setRegionName(String regionName) {
        this.regionName = regionName;
        return this;
    }

    public String getName() {
        return name;
    }

    public cn.gtmap.onemap.platform.entity.video.CameraExtendModel setName(String name) {
        this.name = name;
        return this;
    }

    public double getX() {
        return x;
    }

    public cn.gtmap.onemap.platform.entity.video.CameraExtendModel setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public cn.gtmap.onemap.platform.entity.video.CameraExtendModel setY(double y) {
        this.y = y;
        return this;
    }

    public String getPort() {
        return port;
    }

    public cn.gtmap.onemap.platform.entity.video.CameraExtendModel setPort(String port) {
        this.port = port;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public cn.gtmap.onemap.platform.entity.video.CameraExtendModel setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public cn.gtmap.onemap.platform.entity.video.CameraExtendModel setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getPlatform() {
        return platform;
    }

    public cn.gtmap.onemap.platform.entity.video.CameraExtendModel setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public cn.gtmap.onemap.platform.entity.video.CameraExtendModel setCreateAt(Date createAt) {
        this.createAt = createAt;
        return this;
    }

    public String getType() {
        return type;
    }

    public cn.gtmap.onemap.platform.entity.video.CameraExtendModel setType(String type) {
        this.type = type;
        return this;
    }

}
