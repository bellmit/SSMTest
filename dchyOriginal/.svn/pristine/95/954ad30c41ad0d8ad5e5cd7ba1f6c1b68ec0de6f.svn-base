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
 * @version V1.0, 13-3-25 下午3:39
 */
@Entity
@Table(name="omp_location_mark")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LocationMark extends AbstractEntity {
    private static final long serialVersionUID = 1576070035956091676L;
    @Column(name = "owner",length = 100)
    private String owner;
    @Temporal(TemporalType.DATE)
    @Column(name="create_time")
    private Date createTime;
    @Column(name = "title")
    private String title;
    @Column
    private double x;
    @Column
    private double y;
    @Column(name="publicity",nullable = false)
    private String publicity;
    /**
     * 地图标注的显示样式
     */
    @Column(name = "symbol",length = 1024)
    private String symbol;

    public LocationMark() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getPublicity() {
        return publicity;
    }

    public void setPublicity(String publicity) {
        this.publicity = publicity;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
