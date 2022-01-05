package cn.gtmap.onemap.platform.entity.video;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.MapUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 视频监控点实体
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/9 18:56
 */
@Entity
@Table(name = "omp_camera")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Camera implements Serializable, Cloneable {

    private static final long serialVersionUID = -2147458548543182942L;

    @Id
    @GeneratedValue(generator = "sort-uuid")
    @GenericGenerator(name = "sort-uuid", strategy = "cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator")
    @Column(length = 32)
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



    /**
     * 联系人
     */
    @Column(name = "linkman",length = 16)
    private String linkman;

    /**
     * 联系人
     */
    @Column(name = "department")
    private String department;

    /**
     * 联系电话
     */
    @Column(name = "phone",length = 16)
    private String phone;
//    /**
//     * 所属行政区域id
//     */
//    @Column(name = "region_id", length = 32, nullable = false)
//    private String regionId;

    /**
     * 所属监控区域名称 关联到 camera_region
     */
    @Column(name = "region_name", length = 256, nullable = false)
    private String regionName;


    public double getHeight() {
        return height;
    }

    public Camera setHeight(double height) {
        this.height = height;
        return this;
    }

    public String getId() {
        return id;
    }

    public Camera setId(String id) {
        this.id = id;
        return this;
    }

    public String getVcuId() {
        return vcuId;
    }

    public Camera setVcuId(String vcuId) {
        this.vcuId = vcuId;
        return this;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public Camera setIndexCode(String indexCode) {
        this.indexCode = indexCode;
        return this;
    }

    public String getRegionName() {
        return regionName;
    }

    public Camera setRegionName(String regionName) {
        this.regionName = regionName;
        return this;
    }

    public String getName() {
        return name;
    }

    public Camera setName(String name) {
        this.name = name;
        return this;
    }

    public double getX() {
        return x;
    }

    public Camera setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public Camera setY(double y) {
        this.y = y;
        return this;
    }

    public String getPort() {
        return port;
    }

    public Camera setPort(String port) {
        this.port = port;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public Camera setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Camera setStatus(int status) {
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

    public Camera setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Camera setCreateAt(Date createAt) {
        this.createAt = createAt;
        return this;
    }

    public String getType() {
        return type;
    }

    public Camera setType(String type) {
        this.type = type;
        return this;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * to hash map use reflect
     *
     * @return
     */
    public Map toMap() {
        Map map = new HashMap();
        List<Field> fields = Arrays.asList(this.getClass().getDeclaredFields());
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
        return map;
    }

    /**
     * x y 坐标的 geojson 表示
     *
     * @return
     */
    public String pointJson() {
        Map map = new LinkedHashMap();
        map.put("type", "Point");
        map.put("coordinates", Arrays.asList(new Double[]{this.getX(), this.getY()}));
        return JSON.toJSONString(map);
    }

    /**
     * equals
     *
     * @param input
     * @return
     */
    @Override
    public boolean equals(Object input) {
        if(input!=null){
            return this.getIndexCode().equalsIgnoreCase(((Camera) input).getIndexCode());
        }else {
            return false;
        }

    }
}
