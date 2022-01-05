package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.Camera;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * camera dao
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/12/6 (c) Copyright gtmap Corp.
 */
public interface CameraDao extends JpaRepository<Camera, String>, JpaSpecificationExecutor<Camera> {

    /**
     * find by camera id
     *
     * @param id
     * @return
     */
    Camera findById(String id);
    /**
     * find by name
     *
     * @param name
     * @return
     */
    List<Camera> findByName(String name);

    /**
     * find by indexcode
     * @param indexCode
     * @return
     */
    Camera findByIndexCode(String indexCode);

    /**
     * find by vcuId
     * @param vcuId
     * @return
     */
    List<Camera> findByVcuId(String vcuId);


    @Query(value = "select t1.device_id" +
            "  from omp_camera t1" +
            "  left join omp_camera_dfw_task t2" +
            "    on t1.device_id = t2.index_code" +
            " where t2.index_code is  null", nativeQuery = true)
    List<String> findCameraIdsNotInCameraDfwTask();


    /**find by region name
     *
     * @param regionName
     * @return
     */
    List<Camera> findByRegionName(String regionName);

    /**find by region name
     *
     * @param regionName
     * @return
     */
    Page<Camera> findByRegionName(String regionName,Pageable pageable);

    Page<Camera> findByRegionNameIn(Set<String> regionNames,Pageable pageable);



    /**find by region name and IndexCodes in
     *
     * @param regionName
     * @return
     */
    List<Camera> findByRegionNameAndIndexCodeIn(String regionName,Set<String> indexCodes);


    /**find by region name and IndexCodes in
     *
     * @param regionName
     * @return
     */
    Page<Camera> findByRegionNameAndIndexCodeIn(String regionName,Set<String> indexCodes,Pageable pageable);
    /**find by region name and IndexCodes in
     *
     * @param regionNames
     * @return
     */
    Page<Camera> findByRegionNameInAndIndexCodeIn(Set<String> regionNames,Set<String> indexCodes,Pageable pageable);

    /**
     * find by name or indexcode
     * @param name
     * @param indexCode
     * @param pageable
     * @return
     */
    Page<Camera> findByNameContainingOrIndexCode(String name, String indexCode,Pageable pageable);

    /**
     * find by name and indexCodes in
     * @param name
     * @param indexCodes
     * @param pageable
     * @return
     */
    Page<Camera> findByNameContainingAndIndexCodeIn(String name,Set<String> indexCodes,Pageable pageable);

    /**
     * find all with indexCodes
     * @param indexCodes
     * @return
     */
    List<Camera> findByIndexCodeIn(Set<String> indexCodes);

    /**
     * find all with indexCodes
     * @param indexCodes
     * @param pageable
     * @return
     */
    Page<Camera> findByIndexCodeIn(Set<String> indexCodes,Pageable pageable);

    /**
     * find IndexCodes By RegionName
     * @param regionName
     * @return
     */
    @Query("select indexCode from Camera where regionName = ?1")
    List<String> findIndexCodesByRegionName(String regionName);

    @Query("select indexCode from Camera where regionName in ?1")
    List<String> findIndexCodeByRegionNameIn(Set<String> regionNames);

    List<Camera> findByRegionNameIn(Set<String> regionNames);

    List<Camera> findCamerasByDepartmentIn(List<String> deList);

    List<Camera> findCamerasByRegionName(String regionName);

    @Query(value="select a.CAMERA_ID  from OMP_PROJECT_CAMERA_REF a join OMP_CAMERA b on a.CAMERA_ID = b.DEVICE_ID and b.regionName in regionName in ?1 ",nativeQuery = true)
    List<String> findProIndexCodeByRegionNameIn(Set<String> regionNames);

    /**
     * find RegionNames
     * @return
     */
    @Query("select distinct regionName from Camera")
    List<String> findRegionNames();

    int countCamerasByStatus(int status);

    int countCamerasByStatusAndRegionName(int status,String regionName);

    List<Camera> findCamerasByPlatform(String xzqdm);

}
