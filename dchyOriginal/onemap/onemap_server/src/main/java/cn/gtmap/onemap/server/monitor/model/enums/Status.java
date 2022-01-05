package cn.gtmap.onemap.server.monitor.model.enums;


import cn.gtmap.onemap.core.util.Labelable;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 12-7-1
 */
public enum Status implements Labelable {
    ENABLED("启用"),
    DISABLED("停用"),
    DELETED("删除");
    private String label;

    Status(String title) {
        this.label = title;
    }

    public String getLabel() {
        return label;
    }
}
