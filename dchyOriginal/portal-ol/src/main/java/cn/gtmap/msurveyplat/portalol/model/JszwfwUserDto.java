package cn.gtmap.msurveyplat.portalol.model;

public class JszwfwUserDto {

    private String name;//单位名称

    private String creditcode;//统一社会信用代码

    private String jsdwm;//建设单位码

    private String contactperson;//联系人

    private String contactnumber;//联系电话

    private String cardid;//证件号码

    private String pagerstype;//证件类型

    private String uuid;//uuid

    private boolean login;

    private boolean corpUser;

    private boolean avaliable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreditcode() {
        return creditcode;
    }

    public void setCreditcode(String creditcode) {
        this.creditcode = creditcode;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isCorpUser() {
        return corpUser;
    }

    public void setCorpUser(boolean corpUser) {
        this.corpUser = corpUser;
    }

    public boolean isAvaliable() {
        return avaliable;
    }

    public void setAvaliable(boolean avaliable) {
        this.avaliable = avaliable;
    }

    public String getJsdwm() {
        return jsdwm;
    }

    public void setJsdwm(String jsdwm) {
        this.jsdwm = jsdwm;
    }

    public String getContactperson() {
        return contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getPagerstype() {
        return pagerstype;
    }

    public void setPagerstype(String pagerstype) {
        this.pagerstype = pagerstype;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
