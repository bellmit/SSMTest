package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.video.Recognition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-25 下午3:26
 */
public interface FileStoreDao extends JpaRepository<FileStore, String>,JpaSpecificationExecutor<FileStore>{

    /**
     * 根据父ID获取FileStore
     *
     * @param parentId
     * @return
     */
    List<FileStore> findByParentId(String parentId);

    /***
     * 根据父ID获取FileStore并排序
     * @param parentId
     * @return
     */
    List<FileStore> findByParentIdOrderByCreateAtDesc(String parentId);

    /***
     *
     * @param parentId
     * @return
     */
    List<FileStore> findByParentIdOrderByCreateTimeAsc(String parentId);

    /**
     * 获取文件记录里所有的年份
     * @param parentId
     * @return
     */
    @Query(value = "select distinct to_char(t.create_time, 'yyyy') years from OMP_FILE_STORE t where t.parent_id = ?1 order by years asc ", nativeQuery = true)
    List<Object> findDistinctYear(String parentId);

    @Query(value = "select * from OMP_FILE_STORE t where t.parent_id = ?1 and to_char(t.create_time,'yyyy-MM-dd')=?2 order by t.create_time asc ", nativeQuery = true)
    List<Object> findByParentIdAndDate(String parentid,String dateStr);

    @Query(value = "select * from OMP_FILE_STORE t where t.parent_id = ?1 and to_char(t.create_time,'yyyy-MM-dd')=?2 order by t.create_time asc ", nativeQuery = true)
    List<FileStore> findByParentIdAndCreateDate(String parentid,String dateStr);

    @Query(value = "select MAX(create_time) from OMP_FILE_STORE  where parent_id = ?1 ", nativeQuery = true)
    Object getUpdatedDate(String parentid);

    /**
     * 选择项目关联的指定的照片记录
     * @param parentId
     * @param ids
     * @return
     */
    @Query(value = "from FileStore f where f.parentId=?1 and f.id in ?2")
    List<FileStore> findByParentIdAndId(String parentId, List<String> ids);

    @Query(value = "from FileStore f where f.parentId=?1 and f.createTime >=?2 and f.createTime <?3")
    List<FileStore> findByParentIdAndTime(String parentId, Date start, Date end);

//    @Query(
//            nativeQuery = true,
//            value = "select B.*  from OMP_CAMERA_RECOG A join OMP_FILE_STORE B ON A.ORIGINFILE=B.ID AND A.STATUS =?2 AND A.PARENT_ID=?1 ",
//            countQuery = "select COUNT(B.*)  from OMP_CAMERA_RECOG A join OMP_FILE_STORE B ON A.ORIGINFILE=B.ID AND A.STATUS =?2 AND A.PARENT_ID=?1"
//            )
//    Page<FileStore> findWarnRec(String proID, int status, Pageable pageable);


}
