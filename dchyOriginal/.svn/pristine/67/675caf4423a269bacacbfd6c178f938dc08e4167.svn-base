package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * project jpa dao
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/11/24 14:55
 */
public interface ProjectJpaDao extends JpaRepository<Project,String>,JpaSpecificationExecutor<Project>{

    List<Project> findByCameraId(String id);

    List<Project> findByProId(String proid);

    @Query(value = "select p.xzqdm from Project p group by p.xzqdm")
    List<String> findXzqdms();

    @Query("select t.proId,t.proType,t.location from Project t where t.ownerId=? and t.location is not null")
    List<Object[]> getLocationInfo(String ownerId);

    @Query(value = "SELECT A.* FROM OMP_CAMERA A JOIN OMP_PROJECT_CAMERA_REF B on A.DEVICE_ID=B.CAMERA_ID JOIN OMP_PROJECT C ON B.PROID=C.PROID AND C.PROTYPE=?1",nativeQuery = true)
    List<Camera> getCamerasBySpecialProtype(String type);

}
