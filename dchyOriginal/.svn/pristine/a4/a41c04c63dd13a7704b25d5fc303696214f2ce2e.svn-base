package cn.gtmap.onemap.server.monitor.model;

import cn.gtmap.onemap.server.monitor.model.enums.DataType;
import cn.gtmap.onemap.server.monitor.model.enums.ValueStoreType;
import cn.gtmap.onemap.server.monitor.model.enums.ValueType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-9
 */
@Entity
@Table(name = "m_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Item extends Base {
    private static final long serialVersionUID = 5493427708594594583L;
    @Column(length = 1024, nullable = false)
    private String key;
    private DataType dataType = DataType.DECIMAL;
    private ValueType valueType = ValueType.LONG;
    private ValueStoreType storeType = ValueStoreType.ORIGINAL;
    @Column(length = 128)
    private String format;
    private int interval = 30;
    private int history = 30;
    private int trend = 365;
    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false)
    private Host host;
    @ManyToOne
    @JoinColumn(name = "interface_id", nullable = false)
    private Interface inf;

    public Interface getInf() {
        return inf;
    }

    public void setInf(Interface inf) {
        this.inf = inf;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public ValueStoreType getStoreType() {
        return storeType;
    }

    public void setStoreType(ValueStoreType storeType) {
        this.storeType = storeType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getHistory() {
        return history;
    }

    public void setHistory(int history) {
        this.history = history;
    }

    public int getTrend() {
        return trend;
    }

    public void setTrend(int trend) {
        this.trend = trend;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public boolean isFixed() {
        return host != null && host.isFixed();
    }
}
