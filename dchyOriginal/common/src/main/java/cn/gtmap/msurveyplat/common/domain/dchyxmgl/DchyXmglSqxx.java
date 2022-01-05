package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 9:47
 * @description 多测合一申请信息表
 */
@Table(name = "DCHY_XMGL_SQXX")
public class DchyXmglSqxx {
    /*申请信息id*/
    @Id
    private String sqxxid;
    /*申请机构名称*/
    private String sqjgmc;
    /*办理事项*/
    private String blsx;
    /*申请人*/
    private String sqr;
    /*申请人名称*/
    private String sqrmc;
    /*申请时间*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sqsj;
    /*申请状态*/
    private String sqzt;
    /*关联事项id*/
    private String glsxid;
    /*申请编号*/
    private String sqbh;
    /*线上申请信息id*/
    private String xssqxxid;
    /*线上申请编号*/
    private String xssqbh;


    public String getSqxxid() {
        return sqxxid;
    }

    public void setSqxxid(String sqxxid) {
        this.sqxxid = sqxxid;
    }

    public String getSqjgmc() {
        return sqjgmc;
    }

    public void setSqjgmc(String sqjgmc) {
        this.sqjgmc = sqjgmc;
    }

    public String getBlsx() {
        return blsx;
    }

    public void setBlsx(String blsx) {
        this.blsx = blsx;
    }

    public String getSqr() {
        return sqr;
    }

    public void setSqr(String sqr) {
        this.sqr = sqr;
    }

    public Date getSqsj() {
        return sqsj;
    }

    public void setSqsj(Date sqsj) {
        this.sqsj = sqsj;
    }

    public String getSqzt() {
        return sqzt;
    }

    public void setSqzt(String sqzt) {
        this.sqzt = sqzt;
    }

    public String getGlsxid() {
        return glsxid;
    }

    public void setGlsxid(String glsxid) {
        this.glsxid = glsxid;
    }

    public String getSqbh() {
        return sqbh;
    }

    public void setSqbh(String sqbh) {
        this.sqbh = sqbh;
    }

    public String getSqrmc() {
        return sqrmc;
    }

    public void setSqrmc(String sqrmc) {
        this.sqrmc = sqrmc;
    }

    public String getXssqbh() {
        return xssqbh;
    }

    public void setXssqbh(String xssqbh) {
        this.xssqbh = xssqbh;
    }

    public String getXssqxxid() {
        return xssqxxid;
    }

    public void setXssqxxid(String xssqxxid) {
        this.xssqxxid = xssqxxid;
    }

}
