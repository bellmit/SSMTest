package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.CameraLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * .
 *
 * @author <a href="mailto:chayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 2016-03-24 09:26:00
 */
public interface CameraLogDao extends JpaRepository<CameraLog, String>, JpaSpecificationExecutor<CameraLog>{
    /**
     * 查询每种设备探头使用次数及人数
     * @return
     */
    @Query("select  c.userId,c.userName, count(c.id), count(c.userId) from CameraLog c " +
            "where c.createAt>=?1 and c.createAt<?2 and c.cameraId=?3  group by c.userId,c.userName")
    List<Object[]> queryGroupByCameraId(Date startDate, Date endDate, String cameraId);


    /**
     * 查询人员关联的摄像头
     * @param startDate
     * @param endDate
     * @param userId
     * @return
     */
    @Query("select  c.cameraId,c.cameraName, count(c.cameraId) from CameraLog c " +
            "where c.createAt>=?1 and c.createAt<?2  and c.userId=?3 group by c.cameraId, c.cameraName ")
    List<Object[]> queryGroupByUserId(Date startDate, Date endDate, String userId);


    @Query(value = "select * from omp_camera_log c where c.cameraid = :cameraid and c.userid = :userid and (createat+2/60/24)>:nowdate",nativeQuery = true)
    List<Object> queryRecent(@Param("cameraid") String cameraId,@Param("userid") String userId,@Param("nowdate") Date nowDate);


    /**
     * 查询全部人员关联的摄像头
     * @param startDate
     * @param endDate
     * @return
     */
    @Query("select c.userName, c.cameraName,c.userId, count(c.id)" +
            " from CameraLog c where c.createAt>=?1 and c.createAt<=?2" +
            " group by c.userId, c.userName, c.cameraName" +
            " order by c.userName,c.cameraName")
    List<Object[]> queryGroup(Date startDate, Date endDate);



    @Query("select c.userName,count(c.id) from CameraLog c where c.createAt>=?1 and c.createAt<=?2 group by c.userName")
    List<Object[]> countTimesGroupByUserName(Date startDate, Date endDate);

    /**
     * 查询日志中不重复的监控点信息
     * @return
     */
    @Query("select distinct c.cameraId,c.cameraName from CameraLog c group by c.cameraId,c.cameraName")
    List<Object[]>  queryDistinctCamera();

    /**
     * 查询日志中不重复的监控点信息
     * @return
     */
    @Query("select distinct c.cameraId,c.cameraName from CameraLog c where c.createAt between to_date(?1,'yyyyMMdd hh:mm:ss') and to_date(?2,'yyyyMMdd hh:mm:ss') group by c.cameraId,c.cameraName")
    List<Object[]>  queryDistinctCameraWithDate(String start,String end);

    /**
     * 查询日志用不同的人员信息
     * @return
     */
    @Query("select distinct c.userId,c.userName from CameraLog c group by c.userId,c.userName")
    List<Object[]> queryDistinctUser();

    /**
     * 查询日志用不同的人员信息
     * @return
     */
    @Query("select distinct c.userId,c.userName from CameraLog c where c.createAt between to_date(?1,'yyyyMMdd hh:mm:ss') and to_date(?2,'yyyyMMdd hh:mm:ss') group by c.userId,c.userName")
    List<Object[]> queryDistinctUserWithDate(String start,String end);

    /**
     *  查找最新的用户使用的摄像头的未关闭数据
     * @param userId
     * @param cameraId
     * @return
     */
    @Query("from CameraLog c where c.userId=?1 and c.cameraId=?2 and c.optType=?3 and c.endTime is null order by c.createAt desc")
    List<CameraLog> findLatestCameraLog(String userId, String cameraId, Integer optType);

    /**
     * 根据cameraId查询
     * @param cameraId
     * @return
     */
    @Query("from CameraLog c where c.cameraId=?1 order by c.createAt desc")
    List<CameraLog> findMaxCreateByCameraId(String cameraId);

    CameraLog findFirstByToken(String token);
}
