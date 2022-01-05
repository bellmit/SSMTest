package cn.gtmap.onemap.server.monitor.model;

import cn.gtmap.onemap.server.monitor.model.enums.ValueType;

import java.io.Serializable;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-9
 */
public class History implements Serializable {
    private static final long serialVersionUID = -5151059369956520632L;
    private int itemId;
    private long clock;
    private ValueType type;
    private Object value;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public long getClock() {
        return clock;
    }

    public void setClock(long clock) {
        this.clock = clock;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
