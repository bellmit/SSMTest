package cn.gtmap.msurveyplat.common.dto;


import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/4/15
 * @desc IntelliJ IDEA: IntelliJ IDEA
 */
public class DchyXmglSjclpzDto {

    /**
     * SJCLPZID:收件材料配置id
     */
    @Id
    private String sjclpzid;

    /**
     * CLMC:材料名称
     */
    @Column(name = "CLMC")
    private String clmc;

    /**
     * CLLX:材料类型(字典项：A18)
     */
    @Column(name = "CLLX")
    private String cllx;

    /**
     * SSMKID:办理事项(字典项：A21)
     */
    @Column(name = "SSMKID")
    private String ssmkid;

    /**
     * XH:序号
     */
    @Column(name = "XH")
    private Integer xh;

    /**
     * MRFS:份数
     */
    @Column(name = "MRFS")
    private Integer mrfs;

    /**
     * NEED：是否必须
     */
    @Column(name = "NEED")
    private Integer need;

    /**
     * CLSX；测量事项编号
     */
    @Column(name = "SSCLSX")
    private String ssclsx;

    /**
     * ssclsxList：所属测量事项编号集合
     */
    private List<String> ssclsxList;

    public List<String> getSsclsxList() {
        return ssclsxList;
    }

    public void setSsclsxList(List<String> ssclsxList) {
        this.ssclsxList = ssclsxList;
    }

    public String getSsclsx() {
        return ssclsx;
    }

    public void setSsclsx(String ssclsx) {
        this.ssclsx = ssclsx;
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

    public Integer getMrfs() {
        return mrfs;
    }

    public void setMrfs(Integer mrfs) {
        this.mrfs = mrfs;
    }

    public Integer getNeed() {
        return need;
    }

    public void setNeed(Integer need) {
        this.need = need;
    }
}
