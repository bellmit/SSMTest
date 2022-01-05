package cn.gtmap.onemap.platform.dao.impl;

import cn.gtmap.onemap.platform.dao.ProjectDao;
import cn.gtmap.onemap.platform.dao.ProjectJpaDao;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.Project;
import cn.gtmap.onemap.platform.service.InspectRecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/17 9:58
 */
@Repository
@Transactional(readOnly = true)
public class ProjectDaoImpl implements ProjectDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private InspectRecordService inspectRecordService;

    @Autowired
    private ProjectJpaDao projectJpaDao;


    /***
     * 分页查询
     * @param where
     * @param pageable
     * @return
     */
    @Override
    public Page<Project> search(String where, Pageable pageable) {
        Query query=null;
        if(pageable==null){
            query = createQuery(where);
        }else {
            query = createQuery(where,pageable.getSort().toString().replace(":",""));
        }
        return pageable == null ? new PageImpl(query.getResultList()) : readPage(query, pageable, where);
    }

    /**
     * 获取符合条件的项目信息
     * @param where
     * @param orders
     * @return
     */
    @Override
    public List<Project> search(String where, String orders) {
        Query query = createQuery(where, orders);
        return query.getResultList();
    }

    /**
     * 删除项目
     * @param proid
     * @return
     */
    @Transactional
    @Override
    public boolean delete(String proid) {
        inspectRecordService.deleteByProid(proid);
        Project project = projectJpaDao.findByProId(proid).get(0);
        projectJpaDao.delete(project);
        return false;
    }


    /***
     *
     * @param where
     * @return
     */
    public Query createQuery(String where) {
        String sql = "select * from omp_project  " + (StringUtils.isBlank(where) ? "" : " where ") + where;
        return em.createNativeQuery(sql, Project.class);
    }

    /**
     *
     * @param where
     * @param orders
     * @return
     */
    public Query createQuery(String where, String orders) {
        String sql = "select * from omp_project  " + (StringUtils.isBlank(where) ? "" : " where ") + where + (StringUtils.isBlank(orders) ? "" : " ORDER BY ") + orders;
        return em.createNativeQuery(sql, Project.class);
    }

    /***
     *
     * @param query
     * @param pageable
     * @param where
     * @return
     */
    private Page readPage(Query query, Pageable pageable,String where) {
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        Long total = executeCountQuery(where);
        List content = total > pageable.getOffset() ? query.getResultList() : Collections.emptyList();
        return new PageImpl(content, pageable, total);
    }

    /***
     *
     * @param where
     * @return
     */
    private Long executeCountQuery(String where) {
        Query query = createQuery(where);
        Assert.notNull(query);
        return Long.valueOf(query.getResultList().size());
    }

    public List<Camera> getCamerasBySpecialProtype(String type){
        return projectJpaDao.getCamerasBySpecialProtype(type);
    }
}
