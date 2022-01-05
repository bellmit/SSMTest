package cn.gtmap.msurveyplat.portal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String username;
    private String alias;
    private int admin;
    private int enabled;
    private int locked;
    private String signId;
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createAt;
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date updateAt;
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date expired;
    private String password;
    private String mobile;
    private String email;
    private String tel;
    @DateTimeFormat(
            pattern = "yyyy-MM-dd"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd"
    )
    private Date birthday;
    private String gender;
    private String title;
    private String resume;
    private String address;
    private String sequenceNumber;
    private String idCard;
    private String jobNumber;
    private String partyMember = "N";
    private String pictureId;
    private String roleName;
    private String departmentName;

/*    private List<RoleDto> roleRecordList;
    private List<OrganizationDto> orgRecordList;*/

    public UserDto() {
    }

    public String toString() {
        return "UserDto{id='" + this.id + '\'' + ", username='" + this.username + '\'' + ", alias='" + this.alias + '\'' + ", enabled=" + this.enabled + ", locked=" + this.locked + ", createAt=" + this.createAt + ", updateAt=" + this.updateAt + '}';
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getAlias() {
        return this.alias;
    }

    public int getAdmin() {
        return this.admin;
    }

    public int getEnabled() {
        return this.enabled;
    }

    public int getLocked() {
        return this.locked;
    }

    public String getSignId() {
        return this.signId;
    }

    public Date getCreateAt() {
        return this.createAt;
    }

    public Date getUpdateAt() {
        return this.updateAt;
    }

    public Date getExpired() {
        return this.expired;
    }

    public String getPassword() {
        return this.password;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getEmail() {
        return this.email;
    }

    public String getTel() {
        return this.tel;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public String getGender() {
        return this.gender;
    }

    public String getTitle() {
        return this.title;
    }

    public String getResume() {
        return this.resume;
    }

    public String getAddress() {
        return this.address;
    }

    public String getSequenceNumber() {
        return this.sequenceNumber;
    }

    public String getIdCard() {
        return this.idCard;
    }

    public String getJobNumber() {
        return this.jobNumber;
    }

    public String getPartyMember() {
        return this.partyMember;
    }

    public String getPictureId() {
        return this.pictureId;
    }

/*    public List<RoleDto> getRoleRecordList() {
        return this.roleRecordList;
    }

    public List<OrganizationDto> getOrgRecordList() {
        return this.orgRecordList;
    }*/

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public void setPartyMember(String partyMember) {
        this.partyMember = partyMember;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /*   public void setRoleRecordList(List<RoleDto> roleRecordList) {
        this.roleRecordList = roleRecordList;
    }

    public void setOrgRecordList(List<OrganizationDto> orgRecordList) {
        this.orgRecordList = orgRecordList;
    }*/
}
