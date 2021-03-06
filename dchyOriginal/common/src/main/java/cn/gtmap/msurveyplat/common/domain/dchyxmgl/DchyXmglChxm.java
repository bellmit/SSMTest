package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 10:18
 * @description 多测合一测绘项目表
 */
@Table(name = "DCHY_XMGL_CHXM")
public class DchyXmglChxm {
    @Id
    private String chxmid;//测绘委托id
    private Integer chzsx;//测绘总时限
    private String qjfs;//取件方式(字典项A.9)
    private Date yyqjsj;//预约取件时间(天)
    private String qjdd;//取件地点
    private String sjr;//收件人
    private String slr;//受理人
    private Date slsj;//受理时间
    private String xmzt;//项目状态(字典项A.20)
    private String pjzt;//评价状态(字典项A.10
    private String chgcid;//测绘工程id
    private String chgcbh;//测绘工程编号
    private String xmly;//项目来源(字典项A.14)
    private String slbh;//受理编号
    private Date fbsj;//发布时间
    private String fbr;//发布人
    private String clrzgyq;//承揽人资格要求
    private String wjzxid;//文件中心id
    private String cgfs;//采购方式
    private Date cgjfrq;//成果交付日期
    private String xqfbbh;//发布需求编号
    private String sjdd;//收件地点
    private String sjrlxdh;//收件人联系电话
    private String bjr;//办结人
    private Date bjsj;//办结时间
    private String sjdds;//收件地点省
    private String sjddss;//收件地点市
    private String sjddqx;//收件地点区县
    private String sjddxx;//收件地点详细
    private String xmlx;//项目类型
    private Integer ys;//预算
    private String wtzt;//委托状态
    private String ishy;//是否核验
    private String chjglxr;//测绘机构联系人
    private String chjglxdh;//测绘机构联系电话
    private String zdxm;//是否为重大建设项目
    private String sfsd;//是否首单
    private Integer ccnum;//抽查次数
    private String babh;//备案编号
    private String wjsftb;//文件是否同步
    private String tyjfzt;//统一交付状态
    private Date tyjfqrsj;// 统一交付确认时间
    private String tyjfqrczr; //统一交付确认操作人id
    private String tyjfqrczrmc;//统一交付确认操作人名称
    private String sfgq;//是否挂起
    private String slrmc;//受理人名称
    private String bar;//备案人
    private String barmc;//备案人名称
    private Date basj;//备案时间
    private String fwxz;//房屋性质

    public String getFwxz() {
        return fwxz;
    }

    public void setFwxz(String fwxz) {
        this.fwxz = fwxz;
    }

    public String getSlrmc() {
        return slrmc;
    }

    public void setSlrmc(String slrmc) {
        this.slrmc = slrmc;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    public String getBarmc() {
        return barmc;
    }

    public void setBarmc(String barmc) {
        this.barmc = barmc;
    }

    public Date getBasj() {
        return basj;
    }

    public void setBasj(Date basj) {
        this.basj = basj;
    }

    public String getSfgq() {
        return sfgq;
    }

    public void setSfgq(String sfgq) {
        this.sfgq = sfgq;
    }

    public String getBabh() {
        return babh;
    }

    public void setBabh(String babh) {
        this.babh = babh;
    }

    public String getWjsftb() {
        return wjsftb;
    }

    public void setWjsftb(String wjsftb) {
        this.wjsftb = wjsftb;
    }

    public String getSfsd() {
        return sfsd;
    }

    public void setSfsd(String sfsd) {
        this.sfsd = sfsd;
    }

    public Integer getCcnum() {
        return ccnum;
    }

    public void setCcnum(Integer ccnum) {
        this.ccnum = ccnum;
    }

    public String getZdxm() {
        return zdxm;
    }

    public void setZdxm(String zdxm) {
        this.zdxm = zdxm;
    }

    public String getChjglxr() {
        return chjglxr;
    }

    public void setChjglxr(String chjglxr) {
        this.chjglxr = chjglxr;
    }

    public String getChjglxdh() {
        return chjglxdh;
    }

    public void setChjglxdh(String chjglxdh) {
        this.chjglxdh = chjglxdh;
    }

    public String getIshy() {
        return ishy;
    }

    public void setIshy(String ishy) {
        this.ishy = ishy;
    }


    public String getWtzt() {
        return wtzt;
    }

    public void setWtzt(String wtzt) {
        this.wtzt = wtzt;
    }

    public String getXmlx() {
        return xmlx;
    }

    public void setXmlx(String xmlx) {
        this.xmlx = xmlx;
    }

    public Integer getYs() {
        return ys;
    }

    public void setYs(Integer ys) {
        this.ys = ys;
    }

    public String getSjdds() {
        return sjdds;
    }

    public void setSjdds(String sjdds) {
        this.sjdds = sjdds;
    }

    public String getSjddss() {
        return sjddss;
    }

    public void setSjddss(String sjddss) {
        this.sjddss = sjddss;
    }

    public String getSjddqx() {
        return sjddqx;
    }

    public void setSjddqx(String sjddqx) {
        this.sjddqx = sjddqx;
    }

    public String getSjddxx() {
        return sjddxx;
    }

    public void setSjddxx(String sjddxx) {
        this.sjddxx = sjddxx;
    }

    public String getBjr() {
        return bjr;
    }

    public void setBjr(String bjr) {
        this.bjr = bjr;
    }

    public Date getBjsj() {
        return bjsj;
    }

    public void setBjsj(Date bjsj) {
        this.bjsj = bjsj;
    }

    public String getSjdd() {
        return sjdd;
    }

    public void setSjdd(String sjdd) {
        this.sjdd = sjdd;
    }

    public String getSjrlxdh() {
        return sjrlxdh;
    }

    public void setSjrlxdh(String sjrlxdh) {
        this.sjrlxdh = sjrlxdh;
    }

    public String getXqfbbh() {
        return xqfbbh;
    }

    public void setXqfbbh(String xqfbbh) {
        this.xqfbbh = xqfbbh;
    }

    public Date getFbsj() {
        return fbsj;
    }

    public void setFbsj(Date fbsj) {
        this.fbsj = fbsj;
    }

    public Date getCgjfrq() {
        return cgjfrq;
    }

    public void setCgjfrq(Date cgjfrq) {
        this.cgjfrq = cgjfrq;
    }

    public String getWjzxid() {
        return wjzxid;
    }

    public void setWjzxid(String wjzxid) {
        this.wjzxid = wjzxid;
    }

    public String getClrzgyq() {
        return clrzgyq;
    }

    public void setClrzgyq(String clrzgyq) {
        this.clrzgyq = clrzgyq;
    }

    public String getChxmid() {
        return chxmid;
    }

    public void setChxmid(String chxmid) {
        this.chxmid = chxmid;
    }


    public Integer getChzsx() {
        return chzsx;
    }

    public void setChzsx(Integer chzsx) {
        this.chzsx = chzsx;
    }

    public String getQjfs() {
        return qjfs;
    }

    public void setQjfs(String qjfs) {
        this.qjfs = qjfs;
    }

    public Date getYyqjsj() {
        return yyqjsj;
    }

    public void setYyqjsj(Date yyqjsj) {
        this.yyqjsj = yyqjsj;
    }

    public String getQjdd() {
        return qjdd;
    }

    public void setQjdd(String qjdd) {
        this.qjdd = qjdd;
    }

    public String getSjr() {
        return sjr;
    }

    public void setSjr(String sjr) {
        this.sjr = sjr;
    }

    public Date getSlsj() {
        return slsj;
    }

    public void setSlsj(Date slsj) {
        this.slsj = slsj;
    }

    public String getXmzt() {
        return xmzt;
    }

    public void setXmzt(String xmzt) {
        this.xmzt = xmzt;
    }

    public String getPjzt() {
        return pjzt;
    }

    public void setPjzt(String pjzt) {
        this.pjzt = pjzt;
    }

    public String getChgcid() {
        return chgcid;
    }

    public void setChgcid(String chgcid) {
        this.chgcid = chgcid;
    }

    public String getChgcbh() {
        return chgcbh;
    }

    public void setChgcbh(String chgcbh) {
        this.chgcbh = chgcbh;
    }

    public String getXmly() {
        return xmly;
    }

    public void setXmly(String xmly) {
        this.xmly = xmly;
    }

    public String getSlbh() {
        return slbh;
    }

    public void setSlbh(String slbh) {
        this.slbh = slbh;
    }

    public String getFbr() {
        return fbr;
    }

    public void setFbr(String fbr) {
        this.fbr = fbr;
    }

    public String getCgfs() {
        return cgfs;
    }

    public void setCgfs(String cgfs) {
        this.cgfs = cgfs;
    }

    public String getSlr() {
        return slr;
    }

    public void setSlr(String slr) {
        this.slr = slr;
    }

    public String getTyjfzt() {
        return tyjfzt;
    }

    public void setTyjfzt(String tyjfzt) {
        this.tyjfzt = tyjfzt;
    }

    public Date getTyjfqrsj() {
        return tyjfqrsj;
    }

    public void setTyjfqrsj(Date tyjfqrsj) {
        this.tyjfqrsj = tyjfqrsj;
    }

    public String getTyjfqrczr() {
        return tyjfqrczr;
    }

    public void setTyjfqrczr(String tyjfqrczr) {
        this.tyjfqrczr = tyjfqrczr;
    }

    public String getTyjfqrczrmc() {
        return tyjfqrczrmc;
    }

    public void setTyjfqrczrmc(String tyjfqrczrmc) {
        this.tyjfqrczrmc = tyjfqrczrmc;
    }
}
