package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/3/11
 * @description TODO
 */
@Table(name = "DCHY_XMGL_CGCC")
public class DchyXmglCgcc {
    @Id
    private String cgccid;//成果抽查id
    private Date ccsj;//抽查时间
    private String babh;//备案编号
    private String gcbh;//工程编号
    private String gcmc;//工程名称
    private String jsdw;//建设单位
    private String chxmid;//测绘项目id
    private String chdwmc;//测绘单位名称
    private String cgpj;//成果评价
    private String pjyj;//评价意见
    private String sfsd;//是否首单
    private String pjzt;//评价状态
    private Date cjsj;//创建时间
    private Date pjsj;//评价时间
    private String cjr;//创建人
    private String cjrid;//创建人id
    private String ccr;//抽查人
    private String ccrid;//抽查人id
    private String pjr;//评价人
    private String pjrid;//评价人id

    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }

    public Date getPjsj() {
        return pjsj;
    }

    public void setPjsj(Date pjsj) {
        this.pjsj = pjsj;
    }

    public String getCjr() {
        return cjr;
    }

    public void setCjr(String cjr) {
        this.cjr = cjr;
    }

    public String getCjrid() {
        return cjrid;
    }

    public void setCjrid(String cjrid) {
        this.cjrid = cjrid;
    }

    public String getCcr() {
        return ccr;
    }

    public void setCcr(String ccr) {
        this.ccr = ccr;
    }

    public String getCcrid() {
        return ccrid;
    }

    public void setCcrid(String ccrid) {
        this.ccrid = ccrid;
    }

    public String getPjr() {
        return pjr;
    }

    public void setPjr(String pjr) {
        this.pjr = pjr;
    }

    public String getPjrid() {
        return pjrid;
    }

    public void setPjrid(String pjrid) {
        this.pjrid = pjrid;
    }

    public String getCgccid() {
        return cgccid;
    }

    public void setCgccid(String cgccid) {
        this.cgccid = cgccid;
    }

    public Date getCcsj() {
        return ccsj;
    }

    public void setCcsj(Date ccsj) {
        this.ccsj = ccsj;
    }

    public String getBabh() {
        return babh;
    }

    public void setBabh(String babh) {
        this.babh = babh;
    }

    public String getGcbh() {
        return gcbh;
    }

    public void setGcbh(String gcbh) {
        this.gcbh = gcbh;
    }

    public String getGcmc() {
        return gcmc;
    }

    public void setGcmc(String gcmc) {
        this.gcmc = gcmc;
    }

    public String getJsdw() {
        return jsdw;
    }

    public void setJsdw(String jsdw) {
        this.jsdw = jsdw;
    }

    public String getChxmid() {
        return chxmid;
    }

    public void setChxmid(String chxmid) {
        this.chxmid = chxmid;
    }

    public String getChdwmc() {
        return chdwmc;
    }

    public void setChdwmc(String chdwmc) {
        this.chdwmc = chdwmc;
    }

    public String getCgpj() {
        return cgpj;
    }

    public void setCgpj(String cgpj) {
        this.cgpj = cgpj;
    }

    public String getPjyj() {
        return pjyj;
    }

    public void setPjyj(String pjyj) {
        this.pjyj = pjyj;
    }

    public String getSfsd() {
        return sfsd;
    }

    public void setSfsd(String sfsd) {
        this.sfsd = sfsd;
    }

    public String getPjzt() {
        return pjzt;
    }

    public void setPjzt(String pjzt) {
        this.pjzt = pjzt;
    }
}
