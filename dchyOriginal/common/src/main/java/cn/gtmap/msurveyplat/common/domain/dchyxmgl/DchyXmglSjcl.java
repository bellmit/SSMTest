package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 10:53
 * @description 多测合一材料信息
 */
@Table(name = "DCHY_XMGL_SJCL")
public class DchyXmglSjcl {
    @Id
    private String sjclid; //材料信息id
    private String sjxxid; //材料目录id
    private String clmc; //材料名称
    private Integer fs; //份数
    private String cllx; //材料类型(字典项A.18)
    private String wjzxid; //文件中心NodeId
    private Date clrq; //材料日期
    private Integer ys;//页数
    private Integer xh;//序号
    private String clsx;//测绘事项
    private String sjclpzid;

    public String getSjclpzid() {
        return sjclpzid;
    }

    public void setSjclpzid(String sjclpzid) {
        this.sjclpzid = sjclpzid;
    }

    public String getClsx() {
        return clsx;
    }

    public void setClsx(String clsx) {
        this.clsx = clsx;
    }

    public Integer getXh() {
        return xh;
    }

    public void setXh(Integer xh) {
        this.xh = xh;
    }

    public Integer getYs() {
        return ys;
    }

    public void setYs(Integer ys) {
        this.ys = ys;
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

    public String getClmc() {
        return clmc;
    }

    public void setClmc(String clmc) {
        this.clmc = clmc;
    }

    public Integer getFs() {
        return fs;
    }

    public void setFs(Integer fs) {
        this.fs = fs;
    }

    public String getCllx() {
        return cllx;
    }

    public void setCllx(String cllx) {
        this.cllx = cllx;
    }


    public String getWjzxid() {
        return wjzxid;
    }

    public void setWjzxid(String wjzxid) {
        this.wjzxid = wjzxid;
    }

    public Date getClrq() {
        return clrq;
    }

    public void setClrq(Date clrq) {
        this.clrq = clrq;
    }
}
