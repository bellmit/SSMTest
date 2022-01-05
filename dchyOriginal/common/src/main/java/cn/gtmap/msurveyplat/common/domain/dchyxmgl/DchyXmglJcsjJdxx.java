package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/4/9
 * @description TODO 基础数据 进度信息
 */
@Table(name = "DCHY_XMGL_JCSJSQXX_JDXX")
public class DchyXmglJcsjJdxx {

    @Id
    private String jdxxid;//审核记录id
    private Date bjsj;//办结时间
    private String jcsjsqxxid;//基础数据申请信息id
    private String bjry;//办结人员
    private String bjryid;//办结人员id
    private String bjjg;//办结结果
    private String bjyj;//办结意见
    private String hj;//环节

    public String getJdxxid() {
        return jdxxid;
    }

    public void setJdxxid(String jdxxid) {
        this.jdxxid = jdxxid;
    }

    public Date getBjsj() {
        return bjsj;
    }

    public void setBjsj(Date bjsj) {
        this.bjsj = bjsj;
    }

    public String getJcsjsqxxid() {
        return jcsjsqxxid;
    }

    public void setJcsjsqxxid(String jcsjsqxxid) {
        this.jcsjsqxxid = jcsjsqxxid;
    }

    public String getBjry() {
        return bjry;
    }

    public void setBjry(String bjry) {
        this.bjry = bjry;
    }

    public String getBjryid() {
        return bjryid;
    }

    public void setBjryid(String bjryid) {
        this.bjryid = bjryid;
    }

    public String getBjjg() {
        return bjjg;
    }

    public void setBjjg(String bjjg) {
        this.bjjg = bjjg;
    }

    public String getBjyj() {
        return bjyj;
    }

    public void setBjyj(String bjyj) {
        this.bjyj = bjyj;
    }

    public String getHj() {
        return hj;
    }

    public void setHj(String hj) {
        this.hj = hj;
    }
}
