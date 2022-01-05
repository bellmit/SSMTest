package cn.gtmap.onemap.platform.entity.video;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 摄像头区域实体
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/12/6 (c) Copyright gtmap Corp.
 */
@Entity
@Table(name = "omp_camera_region")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CameraRegion implements Serializable {
    private static final long serialVersionUID = 1990851425316224573L;

    @Id
    @GeneratedValue(generator = "sort-uuid")
    @GenericGenerator(name = "sort-uuid", strategy = "cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator")
    @Column(length = 32)
    private String id;
    /**
     * 区域名称
     */
    @Column(length = 256, nullable = false)
    private String name;

    /**
     * 排序字段
     */
    @Column(name ="serial_number", unique = true)
    private Integer serialNumber;



    /**
     * 父级区域
     */
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private CameraRegion parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @OrderBy("id")
    private List<CameraRegion> children = Lists.newArrayList();

    @Transient
    private String iconSkin;

    @Transient
    private String indexCode;

    @Transient
    private BigDecimal x;
    @Transient
    private BigDecimal y;

    @Transient
    private CameraRegion parent_obj;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JSONField(serialize = false)
    public CameraRegion getParent() {
        return parent;
    }

    public void setParent(CameraRegion parent) {
        this.parent = parent;
    }

    public List<CameraRegion> getChildren() {
        return children;
    }

    public void setChildren(List<CameraRegion> children) {
        this.children = children;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIconSkin() {
        if(CollectionUtils.isEmpty(this.children)){
            this.iconSkin = "leafRegionCamera";
        }
        return iconSkin;
    }

    public BigDecimal getX() {
        return x;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public CameraRegion getParent_obj() {
        return parent_obj;
    }

    public void setParent_obj(CameraRegion region){
        this.parent_obj =region;
    }

}
