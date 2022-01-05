package cn.gtmap.onemap.server.monitor.model;

import cn.gtmap.onemap.server.monitor.collector.impl.ArcgisRestServiceMonitor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-9
 */
@Entity
@Table(name = "m_host")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Host extends Base {
    private static final long serialVersionUID = -13249846244462454L;
    @OneToMany(mappedBy = "host")
    private Set<Interface> interfaces;
    @OneToMany(mappedBy = "host")
    private Set<Item> items;
    @OneToMany(mappedBy = "host")
    private Set<Graph> graphs;

    public Set<Interface> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Set<Interface> interfaces) {
        this.interfaces = interfaces;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public Set<Graph> getGraphs() {
        return graphs;
    }

    public void setGraphs(Set<Graph> graphs) {
        this.graphs = graphs;
    }

    public boolean isFixed() {
        return ArcgisRestServiceMonitor.HOST_NAME.equals(getName());
    }
}
