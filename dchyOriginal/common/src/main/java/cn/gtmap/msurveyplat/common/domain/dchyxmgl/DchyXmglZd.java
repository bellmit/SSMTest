package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 11:20
 * @description
 */
@Table(name = "DCHY_XMGL_ZD")
public class DchyXmglZd {
    @Id
    private String id; //id
    private String dm; //代码
    private String mc; //名称
    private String fdm; //父代码
    private String zdlx; //字典类型
    private String dzzd; //字段对照
    private String qtsx; //其它事项

    public String getDzzd() {
        return dzzd;
    }

    public void setDzzd(String dzzd) {
        this.dzzd = dzzd;
    }

    public String getQtsx() {
        return qtsx;
    }

    public void setQtsx(String qtsx) {
        this.qtsx = qtsx;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getFdm() {
        return fdm;
    }

    public void setFdm(String fdm) {
        this.fdm = fdm;
    }

    public String getZdlx() {
        return zdlx;
    }

    public void setZdlx(String zdlx) {
        this.zdlx = zdlx;
    }
}
