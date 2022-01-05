package cn.gtmap.msurveyplat.portal.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 项目基本信息
 * @author szh
 */
@Entity
@Table(name = "DCHY_CGGL_XM")
public class DchyCgglXm {
    @Id
    private String xmid;
    private String slbh;
    private String xmmc;
    private Date slsj;
    private String slrid;
    private String slr;
    private String jsdw;
    private String chdw;
    private String xmdz;
    private String xmzt;
    private String bjsj;
    private String gzlslid;
    private String dchybh;
    private String chxmbh;
    private String djlx;
    private String djxl;
    private String ghshzt;
    private String fcshzt;
    private String dzshzt;
    private String rkzt;
    private String bz;

    public String getXmid() {
        return xmid;
    }

    public void setXmid(String xmid) {
        this.xmid = xmid;
    }

    public String getSlbh() {
        return slbh;
    }

    public void setSlbh(String slbh) {
        this.slbh = slbh;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public Date getSlsj() {
        return slsj;
    }

    public void setSlsj(Date slsj) {
        this.slsj = slsj;
    }

    public String getSlrid() {
        return slrid;
    }

    public void setSlrid(String slrid) {
        this.slrid = slrid;
    }

    public String getSlr() {
        return slr;
    }

    public void setSlr(String slr) {
        this.slr = slr;
    }

    public String getJsdw() {
        return jsdw;
    }

    public void setJsdw(String jsdw) {
        this.jsdw = jsdw;
    }

    public String getChdw() {
        return chdw;
    }

    public void setChdw(String chdw) {
        this.chdw = chdw;
    }

    public String getXmdz() {
        return xmdz;
    }

    public void setXmdz(String xmdz) {
        this.xmdz = xmdz;
    }

    public String getXmzt() {
        return xmzt;
    }

    public void setXmzt(String xmzt) {
        this.xmzt = xmzt;
    }

    public String getBjsj() {
        return bjsj;
    }

    public void setBjsj(String bjsj) {
        this.bjsj = bjsj;
    }

    public String getGzlslid() {
        return gzlslid;
    }

    public void setGzlslid(String gzlslid) {
        this.gzlslid = gzlslid;
    }

    public String getDchybh() {
        return dchybh;
    }

    public void setDchybh(String dchybh) {
        this.dchybh = dchybh;
    }

    public String getChxmbh() {
        return chxmbh;
    }

    public void setChxmbh(String chxmbh) {
        this.chxmbh = chxmbh;
    }

    public String getDjlx() {
        return djlx;
    }

    public void setDjlx(String djlx) {
        this.djlx = djlx;
    }

    public String getDjxl() {
        return djxl;
    }

    public void setDjxl(String djxl) {
        this.djxl = djxl;
    }

    public String getGhshzt() {
        return ghshzt;
    }

    public void setGhshzt(String ghshzt) {
        this.ghshzt = ghshzt;
    }

    public String getFcshzt() {
        return fcshzt;
    }

    public void setFcshzt(String fcshzt) {
        this.fcshzt = fcshzt;
    }

    public String getDzshzt() {
        return dzshzt;
    }

    public void setDzshzt(String dzshzt) {
        this.dzshzt = dzshzt;
    }

    public String getRkzt() {
        return rkzt;
    }

    public void setRkzt(String rkzt) {
        this.rkzt = rkzt;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
}
