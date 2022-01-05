package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 10:31
 * @description 多测合一合同信息表
 */
@Table(name = "DCHY_XMGL_HTXX")
public class DchyXmglHtxx {
    @Id
    private String htxxid; //合同信息id
    private String chxmid; //测绘项目id
    private String bazt; //备案状态(字典表A.10)
    private Date basj; //备案时间
    private Date qysj; //签约时间
    private String wjzxid; //文件中心id
    private String htlx; //合同类型(字典表A.6)
    private String htbmid; //合同模板id

    public String getHtxxid() {
        return htxxid;
    }

    public void setHtxxid(String htxxid) {
        this.htxxid = htxxid;
    }

    public String getChxmid() {
        return chxmid;
    }

    public void setChxmid(String chxmid) {
        this.chxmid = chxmid;
    }

    public String getBazt() {
        return bazt;
    }

    public void setBazt(String bazt) {
        this.bazt = bazt;
    }

    public Date getBasj() {
        return basj;
    }

    public void setBasj(Date basj) {
        this.basj = basj;
    }

    public Date getQysj() {
        return qysj;
    }

    public void setQysj(Date qysj) {
        this.qysj = qysj;
    }

    public String getWjzxid() {
        return wjzxid;
    }

    public void setWjzxid(String wjzxid) {
        this.wjzxid = wjzxid;
    }

    public String getHtlx() {
        return htlx;
    }

    public void setHtlx(String htlx) {
        this.htlx = htlx;
    }

    public String getHtbmid() {
        return htbmid;
    }

    public void setHtbmid(String htbmid) {
        this.htbmid = htbmid;
    }
}
