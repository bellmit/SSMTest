package cn.gtmap.onemap.platform.entity.network;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 摄像头区域实体
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/12/6 (c) Copyright gtmap Corp.
 */
@Entity
@Table(name = "omp_network_region")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NetworkRegion implements Serializable {
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
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private NetworkRegion parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @OrderBy("id")
    private List<NetworkRegion> children = Lists.newArrayList();

    @Transient
    private String iconSkin;


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
    public NetworkRegion getParent() {
        return parent;
    }

    public void setParent(NetworkRegion parent) {
        this.parent = parent;
    }

    public List<NetworkRegion> getChildren() {
        return children;
    }

    public void setChildren(List<NetworkRegion> children) {
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
            this.iconSkin = "leafRegion";
        }
        return iconSkin;
    }
}
