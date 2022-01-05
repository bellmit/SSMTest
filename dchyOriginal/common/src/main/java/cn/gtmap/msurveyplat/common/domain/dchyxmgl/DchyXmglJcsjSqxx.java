package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/3/10
 * @description TODO 基础数据申请信息
 */
@Table(name = "DCHY_XMGL_JCSJSQXX")
public class DchyXmglJcsjSqxx {

    @Id
    private String jcsjsqxxid;//申请信息id
    private Date sqsj;//申请时间
    private String babh;//备案编号
    private String gcbh;//工程编号
    private String gcmc;//工程名称
    private String jsdw;//建设单位
    private String sqfs;//申请方式
    private String dqzt;//当前状态
    private String chxmid;//测绘项目id
    private String chdwmc;//测绘单位名称
    private String sqr;//申请人
    private String sqrid;//申请人id
    private String zzr;//制作人
    private String zzrid;//制作人id
    private String shr;//审核人
    private String shrid;//审核人id
    private Date zzsj;//制作时间
    private Date shsj;//审核时间
    private String xmbabh;//项目备案编号

    public String getJcsjsqxxid() {
        return jcsjsqxxid;
    }

    public void setJcsjsqxxid(String jcsjsqxxid) {
        this.jcsjsqxxid = jcsjsqxxid;
    }

    public Date getSqsj() {
        return sqsj;
    }

    public void setSqsj(Date sqsj) {
        this.sqsj = sqsj;
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

    public String getSqfs() {
        return sqfs;
    }

    public void setSqfs(String sqfs) {
        this.sqfs = sqfs;
    }

    public String getDqzt() {
        return dqzt;
    }

    public void setDqzt(String dqzt) {
        this.dqzt = dqzt;
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

    public String getSqr() {
        return sqr;
    }

    public void setSqr(String sqr) {
        this.sqr = sqr;
    }

    public String getSqrid() {
        return sqrid;
    }

    public void setSqrid(String sqrid) {
        this.sqrid = sqrid;
    }

    public String getZzr() {
        return zzr;
    }

    public void setZzr(String zzr) {
        this.zzr = zzr;
    }

    public String getZzrid() {
        return zzrid;
    }

    public void setZzrid(String zzrid) {
        this.zzrid = zzrid;
    }

    public String getShr() {
        return shr;
    }

    public void setShr(String shr) {
        this.shr = shr;
    }

    public String getShrid() {
        return shrid;
    }

    public void setShrid(String shrid) {
        this.shrid = shrid;
    }

    public Date getZzsj() {
        return zzsj;
    }

    public void setZzsj(Date zzsj) {
        this.zzsj = zzsj;
    }

    public Date getShsj() {
        return shsj;
    }

    public void setShsj(Date shsj) {
        this.shsj = shsj;
    }

    public String getXmbabh() {
        return xmbabh;
    }

    public void setXmbabh(String xmbabh) {
        this.xmbabh = xmbabh;
    }
}
