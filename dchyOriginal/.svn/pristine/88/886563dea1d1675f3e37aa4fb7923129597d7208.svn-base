package cn.gtmap.onemap.server.monitor.model;

import cn.gtmap.onemap.server.monitor.model.enums.ValueType;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-9
 */
public class Trend implements Serializable {
    private static final long serialVersionUID = 1641116210491263432L;
    private int itemId;
    private long clock;
    private int num;
    private ValueType type;
    private Number min;
    private Number max;
    private Number avg;

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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    public Number getMin() {
        return min;
    }

    public void setMin(Number min) {
        this.min = min;
    }

    public Number getMax() {
        return max;
    }

    public void setMax(Number max) {
        this.max = max;
    }

    public Number getAvg() {
        return avg;
    }

    public void setAvg(Number avg) {
        this.avg = avg;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
