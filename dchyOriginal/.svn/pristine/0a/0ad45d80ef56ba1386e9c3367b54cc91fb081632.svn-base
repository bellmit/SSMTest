package cn.gtmap.msurveyplat.portal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/17
 * @description
 */
public class RoleDto  implements Serializable {
    private String id;
    private String name;
    private String alias;
    private int level;
    private int enabled;
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createAt;
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date updateAt;
    private List<RoleDto> parentRecords;
    private List<RoleDto> excludeRecords;
    private List<String> gradingRoleIds;


    public RoleDto() {
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public void setParentRecords(List<RoleDto> parentRecords) {
        this.parentRecords = parentRecords;
    }

    public void setExcludeRecords(List<RoleDto> excludeRecords) {
        this.excludeRecords = excludeRecords;
    }

    public void setGradingRoleIds(List<String> gradingRoleIds) {
        this.gradingRoleIds = gradingRoleIds;
    }


    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getAlias() {
        return this.alias;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnabled() {
        return this.enabled;
    }

    public Date getCreateAt() {
        return this.createAt;
    }

    public Date getUpdateAt() {
        return this.updateAt;
    }

    public List<RoleDto> getParentRecords() {
        return this.parentRecords;
    }

    public List<RoleDto> getExcludeRecords() {
        return this.excludeRecords;
    }

    public List<String> getGradingRoleIds() {
        return this.gradingRoleIds;
    }

}
