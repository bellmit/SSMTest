package cn.gtmap.msurveyplat.portalol.model;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/2
 * @description 用户和部门关系实体类
 */
@Table(name = "PF_USER_ORGAN_REL")
public class PfUserOrganRel implements Serializable {
    private String organ_id;
    private String user_id;
    private String udr_id;

    public String getOrgan_id() {
        return organ_id;
    }

    public void setOrgan_id(String organ_id) {
        this.organ_id = organ_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUdr_id() {
        return udr_id;
    }

    public void setUdr_id(String udr_id) {
        this.udr_id = udr_id;
    }

}
