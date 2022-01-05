package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/12
 * @description TODO
 */
@Table(name = "DCHY_XMGL_CLSX_CZRZ")
public class DchyXmglClsxCzrz {

    @Id
    private String czrzid; //操作日志id
    private String czr; //操作人
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date czsj; //操作时间
    private String clsxid; //测量事项id
    private String czzt; //操作状态
    private String czyy; //操作原因

    public String getCzrzid() {
        return czrzid;
    }

    public void setCzrzid(String czrzid) {
        this.czrzid = czrzid;
    }

    public String getCzr() {
        return czr;
    }

    public void setCzr(String czr) {
        this.czr = czr;
    }

    public Date getCzsj() {
        return czsj;
    }

    public void setCzsj(Date czsj) {
        this.czsj = czsj;
    }

    public String getClsxid() {
        return clsxid;
    }

    public void setClsxid(String clsxid) {
        this.clsxid = clsxid;
    }

    public String getCzzt() {
        return czzt;
    }

    public void setCzzt(String czzt) {
        this.czzt = czzt;
    }

    public String getCzyy() {
        return czyy;
    }

    public void setCzyy(String czyy) {
        this.czyy = czyy;
    }
}
