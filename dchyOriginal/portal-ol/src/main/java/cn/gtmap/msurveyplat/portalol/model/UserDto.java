package cn.gtmap.msurveyplat.portalol.model;

import java.io.Serializable;

public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String username;
    private String alias;
    private String signId;
    private String dwmc;
    private String dwbh;
    private String yhlx;

    public UserDto() {
    }

    @Override
    public String toString() {
        return "UserDto{id='" + this.id + '\'' + ", username='" + this.username + '\'' + ", alias='" + this.alias + '\'' + '}';
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getAlias() {
        return this.alias;
    }

    public String getSignId() {
        return this.signId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getDwmc() {
        return dwmc;
    }

    public void setDwmc(String dwmc) {
        this.dwmc = dwmc;
    }

    public String getDwbh() {
        return dwbh;
    }

    public void setDwbh(String dwbh) {
        this.dwbh = dwbh;
    }

    public String getYhlx() {
        return yhlx;
    }

    public void setYhlx(String yhlx) {
        this.yhlx = yhlx;
    }
}
