package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.CameraOfflineDuration;
import cn.gtmap.onemap.platform.entity.video.CameraOfflineTrend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * . camera offline dao
 *
 * @author <a href="mailto:zhaozhuyi@gtmap.cn">zhaozhuyi</a>
 * @version v1.0, 2018/1/29 (c) Copyright gtmap Corp.
 */
public interface CameraOfflineDao  extends JpaRepository<CameraOfflineDuration, String>, JpaSpecificationExecutor<CameraOfflineDuration> {

    @Query("select new cn.gtmap.onemap.platform.entity.video.CameraOfflineTrend(t.name, t1.indexCode, " +
            "sum(t.duration) as offlineTime, t1.regionName as region,t1.regionName as xzq) from CameraOfflineDuration t left join t.camera t1 " +
            " group by t.name, t1.indexCode, t1.regionName order by offlineTime desc")
    List<CameraOfflineTrend> aggregateTrend();


    /**
     * 根据时间生成范围内离线报告
     * @param beginDate 开始日期 格式 yyyy-MM-dd
     * @param endDate  截止日期 格式 yyyy-MM-dd
     * @return
     */
    @Query(value = "select t2.*, t5.name as xzq\n" +
            "  from (select sum(t.duration), t.name, t1.region_name, t1.device_id from\n" +
            "        omp_camera_offline_duration t left join omp_camera t1 on\n" +
            "        t.device_id = t1.device_id where\n" +
            "        t.createat between to_date(?1, 'yyyy-mm-dd') and\n" +
            "        to_date(?2, 'yyyy-mm-dd') group by t.name, t1.device_id,\n" +
            "        t1.region_name) t2\n" +
            "  left join (select t4.name, t3.name as region_name\n" +
            "               from omp_camera_region t3\n" +
            "               join omp_camera_region t4\n" +
            "                 on t3.parent_id = t4.id\n" +
            "                and t4.parent_id is null) t5\n" +
            "    on t2.region_name = t5.region_name",nativeQuery = true)
    List<Object> findOffLineReports(String beginDate,String endDate);
}
