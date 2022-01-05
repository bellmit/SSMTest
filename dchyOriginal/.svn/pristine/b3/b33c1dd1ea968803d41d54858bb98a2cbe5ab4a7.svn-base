package cn.gtmap.onemap.platform.entity.video;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "omp_camera_patrol_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CameraPatrolLog implements Serializable, Cloneable {
    @Id
    @GeneratedValue(generator = "sort-uuid")
    @GenericGenerator(name = "sort-uuid", strategy = "cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator")
    @Column(length = 32)
    private String id;

    private String camera_id;

    private String userId;

    private String userName;

    private Date patrol_date;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCamera_id() {
        return camera_id;
    }

    public void setCamera_id(String camera_id) {
        this.camera_id = camera_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getPatrol_date() {
        return patrol_date;
    }

    public void setPatrol_date(Date patrol_date) {
        this.patrol_date = patrol_date;
    }
}
