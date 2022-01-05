package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * DCHY_XMGL_MLK_CXJL
 * @author 
 */
@Table(name = "DCHY_XMGL_MLK_CXJL")
public class DchyXmglMlkCxjl {
    /**
     * 诚信记录id
     */
    @Id
    private String cxjlid;

    /**
     * 记录时间
     */
    private Date jlsj;

    /**
     * 记录人id
     */
    private String jlrid;

    /**
     * 记录人名称
     */
    private String jlrmc;

    /**
     * 诚信评价
     */
    private String cxpj;

    /**
     * 名录库id
     */
    private String mlkid;

    public String getCxjlid() {
        return cxjlid;
    }

    public void setCxjlid(String cxjlid) {
        this.cxjlid = cxjlid;
    }

    public Date getJlsj() {
        return jlsj;
    }

    public void setJlsj(Date jlsj) {
        this.jlsj = jlsj;
    }

    public String getJlrid() {
        return jlrid;
    }

    public void setJlrid(String jlrid) {
        this.jlrid = jlrid;
    }

    public String getJlrmc() {
        return jlrmc;
    }

    public void setJlrmc(String jlrmc) {
        this.jlrmc = jlrmc;
    }

    public String getCxpj() {
        return cxpj;
    }

    public void setCxpj(String cxpj) {
        this.cxpj = cxpj;
    }

    public String getMlkid() {
        return mlkid;
    }

    public void setMlkid(String mlkid) {
        this.mlkid = mlkid;
    }
}