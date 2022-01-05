package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.CameraAutoShot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * dao
 *
 * @author monarchCheng
 * @create 2018-01-05 18:36
 **/
public interface CameraAutoShotDao extends JpaRepository<CameraAutoShot, String>, JpaSpecificationExecutor<CameraAutoShot> {

    /**
     *
     * @param indexCode
     * @param shotAt
     * @param presetNum
     * @param shotNum
     * @return
     */
  List<CameraAutoShot> findByIndexCodeAndShotAtAndPresetNumAndShotNum(String indexCode, Date shotAt, int presetNum, int shotNum);

    /**
     * 查询当日已拍数目
     * @param shotAt
     * @return
     */
  int countByShotAtAndIndexCode(Date shotAt, String indexCode);

  @Query(value="select shot from CameraAutoShot shot where shot.shotAt  > ?1 and shotAt< ?2 and (shot.indexCode in (?3) ) and shot.imgId is not null order by shot.shotAt desc")
  Page<CameraAutoShot> findByIndexCodeInAndShotAtBetweenAndImgIdNotNull(Date begin, Date end, Set<String> indexCodes, Pageable pageable);

  Page<CameraAutoShot> findByShotAtBetweenAndImgIdNotNullOrderByShotAtDesc(Date begin, Date end, Pageable pageable);

  @Query(value="select shot from CameraAutoShot shot where shot.shotAt  > ?1 and shotAt< ?2 and (shot.indexCode in (?3) or shot.indexCode in (?4)) and shot.imgId is not null order by shot.shotAt desc ")
  Page<CameraAutoShot> findByIndexCodeInAndShotAtBetweenAndImgIdNotNull(Date begin, Date end,Set<String> indexCodes,Set<String> indexCodes1,  Pageable pageable);
}
