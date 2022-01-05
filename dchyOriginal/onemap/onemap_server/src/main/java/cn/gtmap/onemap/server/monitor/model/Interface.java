package cn.gtmap.onemap.server.monitor.model;

import cn.gtmap.onemap.core.support.hibernate.JSONType;
import com.alibaba.fastjson.JSONObject;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-9
 */
@Entity
@Table(name = "m_interface")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Interface extends Base {
    private static final long serialVersionUID = 3112087699733467131L;
    @Type(type = JSONType.TYPE)
    @Column(length = 4000)
    private JSONObject attrs = new JSONObject();
    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false)
    private Host host;
    @OneToMany(mappedBy = "inf")
    private Set<Item> items;

    public JSONObject getAttrs() {
        return attrs;
    }

    public void setAttrs(JSONObject attrs) {
        this.attrs = attrs;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public boolean isFixed() {
        return host.isFixed();
    }
}
