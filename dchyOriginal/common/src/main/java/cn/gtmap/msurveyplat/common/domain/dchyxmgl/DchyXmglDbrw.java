package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 11:08
 * @description 多测合一待办列表
 */
@Table(name = "DCHY_XMGL_DBRW")
public class DchyXmglDbrw {
    @Id
    private String dbrwid; //待办任务id
    private String sqxxid; //申请信息id
    private Date zrsj; //转入时间
    private String blryid; //办理人员id
    private String bljsid; //办理角色id
    private String zrryid; //转入人员id
    private String dqjd; // 当前节点

    public String getDbrwid() {
        return dbrwid;
    }

    public void setDbrwid(String dbrwid) {
        this.dbrwid = dbrwid;
    }

    public String getSqxxid() {
        return sqxxid;
    }

    public void setSqxxid(String sqxxid) {
        this.sqxxid = sqxxid;
    }

    public Date getZrsj() {
        return zrsj;
    }

    public void setZrsj(Date zrsj) {
        this.zrsj = zrsj;
    }

    public String getBlryid() {
        return blryid;
    }

    public void setBlryid(String blryid) {
        this.blryid = blryid;
    }

    public String getBljsid() {
        return bljsid;
    }

    public void setBljsid(String bljsid) {
        this.bljsid = bljsid;
    }

    public String getZrryid() {
        return zrryid;
    }

    public void setZrryid(String zrryid) {
        this.zrryid = zrryid;
    }

    public String getDqjd() {
        return dqjd;
    }

    public void setDqjd(String dqjd) {
        this.dqjd = dqjd;
    }

    @Override
    public String toString() {
        return "DchyXmglDbrw{" +
                "dbrwid='" + dbrwid + '\'' +
                ", sqxxid='" + sqxxid + '\'' +
                ", zrsj=" + zrsj +
                ", blryid='" + blryid + '\'' +
                ", bljsid='" + bljsid + '\'' +
                ", zrryid='" + zrryid + '\'' +
                ", dqjd='" + dqjd + '\'' +
                '}';
    }
}
