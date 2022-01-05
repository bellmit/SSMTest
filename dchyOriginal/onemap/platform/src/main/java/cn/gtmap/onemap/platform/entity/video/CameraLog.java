package cn.gtmap.onemap.platform.entity.video;

import cn.gtmap.onemap.security.SecHelper;
import cn.gtmap.onemap.security.User;
import org.apache.commons.collections.MapUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * . 摄像头操作日志表
 *
 * @author <a href="mailto:chayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 2016-03-23 19:47:00
 */
@Entity
@Table(name = "omp_camera_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CameraLog implements Comparable<CameraLog> {
    @Id
    @GeneratedValue(generator = "sort-uuid")
    @GenericGenerator(name = "sort-uuid", strategy = "cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator")
    @Column(length = 32)
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createAt;

    @Column
    private boolean enabled = true;

    /**
     * 用户id
     */
    @Column
    private String userId;

    /**
     * 用户名
     */
    @Column
    private String userName;

    /**
     * 记录indexCode
     */
    @Column
    private String cameraId;

    /**
     * 摄像头名称
     */
    @Column
    private String cameraName;

    /**
     * 操作内容
     */
    @Column
    private String optContent;

    /**
     * 年份
     */
    @Column
    private String year;

    /**
     * 月份
     */
    @Column
    private String month;

    /**
     * 行政区名称
     */
    @Column
    private String regionName;

    /**
     * 开始时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 摄像头打开到关闭使用的时间
     */
    @Transient
    private String totalTime;

    @Column(name = "opt_type")
    private Integer optType;

    /**
     * 备注
     */
    @Lob
    @Column(columnDefinition = "CLOB", nullable = true)
    private String remark;

    private String token;

    /**
     * USER_DEPT当前操作摄像头用户所属部门
     */
    @Column(name = "user_dept")
    private String userDept;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getOptContent() {
        return optContent;
    }

    public void setOptContent(String optContent) {
        this.optContent = optContent;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOptType() {
        return optType;
    }

    public void setOptType(Integer optType) {
        this.optType = optType;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    @Override
    public int compareTo(CameraLog cameraLog) {
        return this.getCreateAt().compareTo(cameraLog.getCreateAt());
    }

    /***
     * from map
     * @param map
     * @return
     */
    public static CameraLog fromHashMap(Map map) {
        CameraLog cameraLog = new CameraLog();

        if (map == null || Collections.emptyMap().equals(map)) return cameraLog;
        if(map.containsKey("loginUserId")&&map.containsKey("loginUserName")){
            cameraLog.setUserId(map.get("loginUserId").toString());
            cameraLog.setUserName(map.get("loginUserName").toString());
            if (map.containsKey("loginUserDept")){
                cameraLog.setUserDept(map.get("loginUserDept").toString());
            }
        }else {
            User user = SecHelper.getUser();
            cameraLog.setUserId(user.getId());
            cameraLog.setUserName(user.getViewName());
        }

        cameraLog.setCameraId(MapUtils.getString(map, "indexCode"));
        cameraLog.setCameraName(MapUtils.getString(map, "cameraName"));
        cameraLog.setEnabled(true);
        cameraLog.setCreateAt(new Date());
        cameraLog.setOptContent(MapUtils.getString(map, "content"));

        cameraLog.setMonth(String.valueOf(new Date().getMonth() + 1));
        cameraLog.setYear(DateTime.now().year().getAsShortText());
        cameraLog.setRegionName(MapUtils.getString(map, "regionName"));
        return cameraLog;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserDept() {
        return userDept;
    }

    public void setUserDept(String userDept) {
        this.userDept = userDept;
    }
}
