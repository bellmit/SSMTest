package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 10:34
 * @description 多测合一测量事项与测绘单位关系表
 */
@Table(name = "DCHY_XMGL_CLSX_CHDWXX_GX")
public class DchyXmglClsxChdwxxGx {
    @Id
    private String gxid; //关系id
    private String chdwxxid; //测绘单位信息id
    private String clsxid; //测量事项id
    private String chxmid;

    public String getChxmid() {
        return chxmid;
    }

    public void setChxmid(String chxmid) {
        this.chxmid = chxmid;
    }

    public String getGxid() {
        return gxid;
    }

    public void setGxid(String gxid) {
        this.gxid = gxid;
    }

    public String getChdwxxid() {
        return chdwxxid;
    }

    public void setChdwxxid(String chdwxxid) {
        this.chdwxxid = chdwxxid;
    }

    public String getClsxid() {
        return clsxid;
    }

    public void setClsxid(String clsxid) {
        this.clsxid = clsxid;
    }
}
