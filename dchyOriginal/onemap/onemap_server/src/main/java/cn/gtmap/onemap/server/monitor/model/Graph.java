package cn.gtmap.onemap.server.monitor.model;

import cn.gtmap.onemap.core.support.hibernate.JSONType;
import cn.gtmap.onemap.server.monitor.model.enums.GraphType;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-9
 */
@Entity
@Table(name = "m_graph")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Graph extends Base {
    private static final long serialVersionUID = -7417662584276226968L;
    private GraphType type = GraphType.LINE;
    @Type(type = JSONType.TYPE)
    @Column(length = 4000)
    private JSONObject options;
    @OneToMany(mappedBy = "graph", cascade = CascadeType.ALL)
    @OrderColumn(name = "idx", nullable = false)
    private List<GraphItem> items = Lists.newArrayList();
    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false)
    private Host host;

    public GraphType getType() {
        return type;
    }

    public void setType(GraphType type) {
        this.type = type;
    }

    public JSONObject getOptions() {
        return options;
    }

    public void setOptions(JSONObject option) {
        this.options = option;
    }

    public List<GraphItem> getItems() {
        return items;
    }

    public void setItems(List<GraphItem> items) {
        this.items = items;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
}
