package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 11:15
 * @description 多测合一表单必填验证
 */
@Table(name = "DCHY_XMGL_BDBTYZ")
public class DchyXmglBdbtyz {
    @Id
    private String bdbtyzid; //表单必填验证id
    private String bdid; //表单id
    private String ssmkid; //所属模块id(字典表A.21)
    private String bdzdid; //表单字段id

    public String getBdbtyzid() {
        return bdbtyzid;
    }

    public void setBdbtyzid(String bdbtyzid) {
        this.bdbtyzid = bdbtyzid;
    }

    public String getBdid() {
        return bdid;
    }

    public void setBdid(String bdid) {
        this.bdid = bdid;
    }

    public String getSsmkid() {
        return ssmkid;
    }

    public void setSsmkid(String ssmkid) {
        this.ssmkid = ssmkid;
    }

    public String getBdzdid() {
        return bdzdid;
    }

    public void setBdzdid(String bdzdid) {
        this.bdzdid = bdzdid;
    }
}
