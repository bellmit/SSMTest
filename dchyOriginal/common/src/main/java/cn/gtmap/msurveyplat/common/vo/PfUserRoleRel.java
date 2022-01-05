package cn.gtmap.msurveyplat.common.vo;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/2
 * @description TODO
 */
@Table(name = "PF_USER_ROLE_REL")
public class PfUserRoleRel implements Serializable {
    private String user_id;

    private String role_id;

    private String urr_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getUrr_id() {
        return urr_id;
    }

    public void setUrr_id(String urr_id) {
        this.urr_id = urr_id;
    }
}
