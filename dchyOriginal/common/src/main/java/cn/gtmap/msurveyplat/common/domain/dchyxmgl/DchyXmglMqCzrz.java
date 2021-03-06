package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/18
 * @description TODO
 */
@Table(name = "DCHY_XMGL_MQ_CZRZ")
public class DchyXmglMqCzrz {
    @Id
    private String czrzid; //操作日志id
    private Date czsj; //操作时间
    private byte[] msg; //发送数据
    private String sbyy; //失败原因
    private String dldm; //队列代码
    private String dlmc; //队列名称

    public String getCzrzid() {
        return czrzid;
    }

    public void setCzrzid(String czrzid) {
        this.czrzid = czrzid;
    }

    public Date getCzsj() {
        return czsj;
    }

    public void setCzsj(Date czsj) {
        this.czsj = czsj;
    }

    public byte[] getMsg() {
        return msg;
    }

    public void setMsg(byte[] msg) {
        this.msg = msg;
    }

    public String getSbyy() {
        return sbyy;
    }

    public void setSbyy(String sbyy) {
        this.sbyy = sbyy;
    }

    public String getDldm() {
        return dldm;
    }

    public void setDldm(String dldm) {
        this.dldm = dldm;
    }

    public String getDlmc() {
        return dlmc;
    }

    public void setDlmc(String dlmc) {
        this.dlmc = dlmc;
    }
}
