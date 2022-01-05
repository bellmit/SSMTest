package cn.gtmap.msurveyplat.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/4/10
 * @description 表单必填字段字典表
 */
@Table(name = "DCHY_ZD_SJKB")
@ApiModel(value = "DchyZdSjkbDO", description = "表单必填字段字典表")
public class DchyZdSjkbDo {
    @Id
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "数据库表id")
    private String sjkbid;
    @ApiModelProperty(value = "数据库表名称")
    private String sjkbmc;
    @ApiModelProperty(value = "sql")
    private String sql;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSjkbid() {
        return sjkbid;
    }

    public void setSjkbid(String sjkbid) {
        this.sjkbid = sjkbid;
    }

    public String getSjkbmc() {
        return sjkbmc;
    }

    public void setSjkbmc(String sjkbmc) {
        this.sjkbmc = sjkbmc;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
