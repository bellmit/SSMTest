package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "DCHY_XMGL_AUTHORIZE")
public class DchyXmglAuthorize {
    @Id
    private String id;//授权id
    private String sqlx;//授权类型(字典项A.39)
    private String zylx;//资源类型(字典项A.40)
    private String zyuri;//资源uri标识
    private String roleid;//角色id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSqlx() {
        return sqlx;
    }

    public void setSqlx(String sqlx) {
        this.sqlx = sqlx;
    }

    public String getZylx() {
        return zylx;
    }

    public void setZylx(String zylx) {
        this.zylx = zylx;
    }

    public String getZyuri() {
        return zyuri;
    }

    public void setZyuri(String zyuri) {
        this.zyuri = zyuri;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }
}
