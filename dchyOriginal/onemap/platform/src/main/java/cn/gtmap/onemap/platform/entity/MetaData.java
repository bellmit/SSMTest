package cn.gtmap.onemap.platform.entity;

import cn.gtmap.onemap.core.entity.AbstractEntity;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * @author: <a href="mailto:yxfacw@live.com">yingxiufeng</a>
 * @date: 2013-07-08 下午5:13
 * @version: 1.0
 */
@Entity
@Table(name = "s_xx_jjfa")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MetaData extends AbstractEntity implements Comparable<MetaData> {

    @Column
    private String nd;
    @Column(name = "username")
    private String userName;
    @Column(name = "userpwd")
    private String userPwd;
    @Column
    private String ipbm;
    @Column
    private String sjkslbm;
    @Column
    private String bkjmc;
    @Column
    private String bkjwjlj;
    @Column
    private String ywlx;
    @Column
    private String bkjzdx;
    @Column
    private String bkjyydx;
    @Column
    private String bkjsydx;
    @Column
    private String zt;

    public MetaData() {
    }

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getIpbm() {
        return ipbm;
    }

    public void setIpbm(String ipbm) {
        this.ipbm = ipbm;
    }

    public String getSjkslbm() {
        return sjkslbm;
    }

    public void setSjkslbm(String sjkslbm) {
        this.sjkslbm = sjkslbm;
    }

    public String getBkjmc() {
        return bkjmc;
    }

    public void setBkjmc(String bkjmc) {
        this.bkjmc = bkjmc;
    }

    public String getBkjwjlj() {
        return bkjwjlj;
    }

    public void setBkjwjlj(String bkjwjlj) {
        this.bkjwjlj = bkjwjlj;
    }

    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }

    public String getBkjzdx() {
        return bkjzdx;
    }

    public void setBkjzdx(String bkjzdx) {
        this.bkjzdx = bkjzdx;
    }

    public String getBkjyydx() {
        return bkjyydx;
    }

    public void setBkjyydx(String bkjyydx) {
        this.bkjyydx = bkjyydx;
    }

    public String getBkjsydx() {
        return bkjsydx;
    }

    public void setBkjsydx(String bkjsydx) {
        this.bkjsydx = bkjsydx;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    @Override
    public int compareTo(MetaData o) {
        return Integer.valueOf(this.getNd()) - Integer.valueOf(o.getNd());
    }
}
