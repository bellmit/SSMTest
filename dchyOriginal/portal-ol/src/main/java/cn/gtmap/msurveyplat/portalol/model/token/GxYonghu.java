package cn.gtmap.msurveyplat.portalol.model.token;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/5
 * @description TODO
 */
@Table(name = "gx_yonghu")
public class GxYonghu {
    @Id
    private String yhId;//主键id
    private String username;//账号
    private String password;//密码
    private String xm;//姓名
    private String dh;//电话
    private String zt;//状态 1：正常 2：限制

    public String getYhId() {
        return yhId;
    }

    public void setYhId(String yhId) {
        this.yhId = yhId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }
}
