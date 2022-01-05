package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.CameraRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/12/7 (c) Copyright gtmap Corp.
 */
public interface CameraRegionDao extends JpaRepository<CameraRegion, String> {

    /**
     * find root region
     * @return
     */
    List<CameraRegion> findByParentNullOrderBySerialNumberAsc();

    /**
     * find by parent
     * @param parentId
     * @return
     */
    List<CameraRegion> findByParent(String parentId);


    /**
     * 根据行政区id查询探头总数
     * @param parentId
     * @return
     */
    @Query(value = "select count(1)\n" +
            "  from omp_camera t3\n" +
            " where t3.region_name in (select t2.name\n" +
            "                            from omp_camera_region t2\n" +
            "                           where t2.parent_id = ?1 or t2.id=?1)",nativeQuery = true)
    Integer countCameraByRegionId(String parentId);

    /**
     * 根据行政区名称获取CameraRegion
     * @param names
     * @return
     */
    List<CameraRegion> findAllByNameIn(List<String> names);

    /**
     * @param childName
     * @return
     */
    @Query(value = "select t2.name as xzq\n" +
            "  from omp_camera_region t1\n" +
            " right join omp_camera_region t2\n" +
            "    on t1.parent_id = t2.id\n" +
            " where t2.parent_id is null and t1.name=?1 ",nativeQuery = true)
    String findParentNameByName(String childName);

    CameraRegion findById(String id);

    @Query("select distinct c.name from CameraRegion c where c.parent =?1")
    List<String> findNameByParent(CameraRegion parent);
}
