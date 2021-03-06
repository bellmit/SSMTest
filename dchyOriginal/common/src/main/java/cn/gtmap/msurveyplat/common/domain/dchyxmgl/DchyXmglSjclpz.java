package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 11:13
 * @description 多测合一收件材料配置
 */
@Table(name = "DCHY_XMGL_SJCLPZ")
public class DchyXmglSjclpz {
    @Id
    private String  sjclpzid; //收件材料配置id
    private String  clmc; //材料名称
    private String  cllx; //材料类型(字典项A.18)
    private String  ssmkid; //材料所属模块(字典项A.21)
    private Integer xh; //序号
    private Integer mrfs;//默认份数
    private Integer need;//是否必须

    public Integer getNeed() {
        return need;
    }

    public void setNeed(Integer need) {
        this.need = need;
    }

    public Integer getMrfs() {
        return mrfs;
    }

    public void setMrfs(Integer mrfs) {
        this.mrfs = mrfs;
    }

    public String getSjclpzid() {
        return sjclpzid;
    }

    public void setSjclpzid(String sjclpzid) {
        this.sjclpzid = sjclpzid;
    }

    public String getClmc() {
        return clmc;
    }

    public void setClmc(String clmc) {
        this.clmc = clmc;
    }

    public String getCllx() {
        return cllx;
    }

    public void setCllx(String cllx) {
        this.cllx = cllx;
    }

    public String getSsmkid() {
        return ssmkid;
    }

    public void setSsmkid(String ssmkid) {
        this.ssmkid = ssmkid;
    }

    public Integer getXh() {
        return xh;
    }

    public void setXh(Integer xh) {
        this.xh = xh;
    }
}
