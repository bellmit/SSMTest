package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 10:48
 * @description 多测合一测绘单位
 */
@Table(name = "DCHY_XMGL_JSDW")
public class DchyXmglJsdw {
    @Id
    private String jsdwid; //建设单位id
    private String dwmc; //建设单位名称
    private String tyshxydm; //统一社会信用代码
    private String dwbh; //建设单位编号
    private String lxr; //联系人
    private String lxdh; //联系电话
    private String jsdwm; //建设单位码
    private String lrr; //录入人
    private Date lrsj; //录入时间

    public String getJsdwid() {
        return jsdwid;
    }

    public void setJsdwid(String jsdwid) {
        this.jsdwid = jsdwid;
    }

    public String getDwmc() {
        return dwmc;
    }

    public void setDwmc(String dwmc) {
        this.dwmc = dwmc;
    }

    public String getTyshxydm() {
        return tyshxydm;
    }

    public void setTyshxydm(String tyshxydm) {
        this.tyshxydm = tyshxydm;
    }

    public String getDwbh() {
        return dwbh;
    }

    public void setDwbh(String dwbh) {
        this.dwbh = dwbh;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getJsdwm() {
        return jsdwm;
    }

    public void setJsdwm(String jsdwm) {
        this.jsdwm = jsdwm;
    }

    public String getLrr() {
        return lrr;
    }

    public void setLrr(String lrr) {
        this.lrr = lrr;
    }

    public Date getLrsj() {
        return lrsj;
    }

    public void setLrsj(Date lrsj) {
        this.lrsj = lrsj;
    }
}
