package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
 * @description 多测合一测量成果提交记录
 * @time 2021/1/14 18:07
 */
@Table(name = "DCHY_XMGL_CLCG_TJJL")
public class DchyXmglClcgTjjl {
    @Id
    private String tjjlid; //提交记录id
    private String chgcid; //测绘工程id
    private String chgcbh; //测绘工程编号
    private String chxmid; //测绘项目id
    private String sqxxid; //申请信息id
    private Date tjsj; //提交时间
    private byte[] cwxx; //错误信息
    private String sftj; //是否提交 0:未提交 1:已提交

    public String getTjjlid() {
        return tjjlid;
    }

    public void setTjjlid(String tjjlid) {
        this.tjjlid = tjjlid;
    }

    public String getChgcid() {
        return chgcid;
    }

    public void setChgcid(String chgcid) {
        this.chgcid = chgcid;
    }

    public String getChgcbh() {
        return chgcbh;
    }

    public void setChgcbh(String chgcbh) {
        this.chgcbh = chgcbh;
    }

    public String getChxmid() {
        return chxmid;
    }

    public void setChxmid(String chxmid) {
        this.chxmid = chxmid;
    }

    public String getSqxxid() {
        return sqxxid;
    }

    public void setSqxxid(String sqxxid) {
        this.sqxxid = sqxxid;
    }

    public Date getTjsj() {
        return tjsj;
    }

    public void setTjsj(Date tjsj) {
        this.tjsj = tjsj;
    }

    public byte[] getCwxx() {
        return cwxx;
    }

    public void setCwxx(byte[] cwxx) {
        this.cwxx = cwxx;
    }

    public String getSftj() {
        return sftj;
    }

    public void setSftj(String sftj) {
        this.sftj = sftj;
    }
}
