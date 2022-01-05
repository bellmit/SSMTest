package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/17 9:43
 */
public interface ProjectDao {

    /**
     * 查询
     * @param where
     * @param pageable
     * @return
     */
    Page<Project> search(String where, Pageable pageable);

    /**
     * 查询符合条件的所有项目信息
     * @param where
     * @return
     */
    List<Project> search(String where, String orders);

    /***删除
     * delete project
     * @param proid
     * @return
     */
    boolean delete(String proid);



}
