package cn.gtmap.msurveyplat.exchange.core.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/7
 * @description
 */
public class ProcessDefData implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String name;
    protected String description;
    protected String key;
    protected int version;
    protected String category;
    protected Map<String, Object> variables;
    protected int suspensionState;
    private String processDefKey;
    private Integer commonUse;

    public ProcessDefData() {
    }

    public Integer getCommonUse() {
        return this.commonUse;
    }

    public void setCommonUse(Integer commonUse) {
        this.commonUse = commonUse;
    }

    public static long getSerialVersionUID() {
        return 1L;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, Object> getVariables() {
        return this.variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public int getSuspensionState() {
        return this.suspensionState;
    }

    public void setSuspensionState(int suspensionState) {
        this.suspensionState = suspensionState;
    }

    public String getProcessDefKey() {
        return this.processDefKey;
    }

    public void setProcessDefKey(String processDefKey) {
        this.processDefKey = processDefKey;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ProcessDefData that = (ProcessDefData)o;
            if (this.id != null) {
                if (this.id.equals(that.id)) {
                    return this.id != null ? this.id.equals(that.id) : that.id == null;
                }
            } else if (that.id == null) {
                return this.id != null ? this.id.equals(that.id) : that.id == null;
            }

            return false;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.id != null ? this.id.hashCode() : 0;
        result = 31 * result + (this.id != null ? this.id.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "ProcessDefData{id='" + this.id + '\'' + ", name='" + this.name + '\'' + ", description='" + this.description + '\'' + ", key='" + this.key + '\'' + ", version=" + this.version + ", category='" + this.category + '\'' + ", variables=" + this.variables + ", suspensionState=" + this.suspensionState + ", processDefKey='" + this.processDefKey + '\'' + '}';
    }
}
