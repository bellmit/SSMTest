package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/1/8
 * @description TODO
 */
@Table(name = "DCHY_XMGL_CLCG")
public class DchyXmglClcg {
    @Id
    private String clcgid; //测量成果id
    private String chxmid; //测绘项目id
    private String clsxid; //测量事项id
    private String clsx; //测量事项(字典表A.3)
    private String sqxxid; //申请信息id
    private String shzt; //审核状态(字典表A.3)
    private String chgcbh; //测绘工程编号
    private String chgcid; //测绘工程id
    private String clcgmc; //测量成果名称
    private String sjclid; //收件材料id
    private String sjxxid; //收件信息id
    private String wjzxid; //文件中心id
    private Date rksj;//入库时间
    private Date tjsj;// 提交时间
    private String tjr;//提交人id
    private String tjrmc;//提交人名称

    public String getClcgid() {
        return clcgid;
    }

    public void setClcgid(String clcgid) {
        this.clcgid = clcgid;
    }

    public String getChxmid() {
        return chxmid;
    }

    public void setChxmid(String chxmid) {
        this.chxmid = chxmid;
    }

    public String getClsxid() {
        return clsxid;
    }

    public void setClsxid(String clsxid) {
        this.clsxid = clsxid;
    }

    public String getClsx() {
        return clsx;
    }

    public void setClsx(String clsx) {
        this.clsx = clsx;
    }

    public String getSqxxid() {
        return sqxxid;
    }

    public void setSqxxid(String sqxxid) {
        this.sqxxid = sqxxid;
    }

    public String getShzt() {
        return shzt;
    }

    public void setShzt(String shzt) {
        this.shzt = shzt;
    }

    public String getChgcbh() {
        return chgcbh;
    }

    public void setChgcbh(String chgcbh) {
        this.chgcbh = chgcbh;
    }

    public String getChgcid() {
        return chgcid;
    }

    public void setChgcid(String chgcid) {
        this.chgcid = chgcid;
    }

    public String getClcgmc() {
        return clcgmc;
    }

    public void setClcgmc(String clcgmc) {
        this.clcgmc = clcgmc;
    }

    public String getSjclid() {
        return sjclid;
    }

    public void setSjclid(String sjclid) {
        this.sjclid = sjclid;
    }

    public String getSjxxid() {
        return sjxxid;
    }

    public void setSjxxid(String sjxxid) {
        this.sjxxid = sjxxid;
    }

    public String getWjzxid() {
        return wjzxid;
    }

    public void setWjzxid(String wjzxid) {
        this.wjzxid = wjzxid;
    }

    public Date getRksj() {
        return rksj;
    }

    public void setRksj(Date rksj) {
        this.rksj = rksj;
    }

    public Date getTjsj() {
        return tjsj;
    }

    public void setTjsj(Date tjsj) {
        this.tjsj = tjsj;
    }

    public String getTjr() {
        return tjr;
    }

    public void setTjr(String tjr) {
        this.tjr = tjr;
    }

    public String getTjrmc() {
        return tjrmc;
    }

    public void setTjrmc(String tjrmc) {
        this.tjrmc = tjrmc;
    }

    @Override
    public String toString() {
        return "DchyXmglClcg{" +
                "clcgid='" + clcgid + '\'' +
                ", chxmid='" + chxmid + '\'' +
                ", clsxid='" + clsxid + '\'' +
                ", clsx='" + clsx + '\'' +
                ", sqxxid='" + sqxxid + '\'' +
                ", shzt='" + shzt + '\'' +
                ", chgcbh='" + chgcbh + '\'' +
                ", chgcid='" + chgcid + '\'' +
                ", clcgmc='" + clcgmc + '\'' +
                ", sjclid='" + sjclid + '\'' +
                ", sjxxid='" + sjxxid + '\'' +
                ", wjzxid='" + wjzxid + '\'' +
                ", rksj=" + rksj +
                ", tjsj=" + tjsj +
                ", tjr='" + tjr + '\'' +
                ", tjrmc='" + tjrmc + '\'' +
                '}';
    }
}
