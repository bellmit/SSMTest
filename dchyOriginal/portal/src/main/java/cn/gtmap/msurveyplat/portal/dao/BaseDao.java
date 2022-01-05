package cn.gtmap.msurveyplat.portal.dao;


import cn.gtmap.msurveyplat.portal.utils.QueryCondition;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: janeshen
 * Date: 13-10-23
 * Time: 下午3:29
 * To change this template use File | Settings | File Templates.
 */

public interface BaseDao {
    public <T> T getById(Class<T> clazz, Object id);
    public <T> void delete(Class<T> clazz, Object id);
    public <T> void delete(Object entity);
    public <T> void delete(Class<T> clazz, Object[] ids);
    public <T> List<T> getAll(Class<T> clazz);
    public void save(Object entity);
    public void update(Object entity);
    public <T> List<T> get(Class<T> clazz, List<QueryCondition> queryConditions, String orderBy, int currentPage, int pageSize);
    public <T> List<T> get(Class<T> clazz, List<QueryCondition> queryConditions);
    public <T> List<T> get(Class<T> clazz, List<QueryCondition> queryConditions, String orderBy);
    public Object getSingleResult(Class clazz, List<QueryCondition> queryConditions);
    public long getRecordCount(Class clazz, List<QueryCondition> queryConditions);
    public <T> List<T> getByJpql(String jpql, Object... objects);
    public int executeJpql(String jpql, Object... objects);
    public Object getUniqueResultByJpql(String jpql, Object... objects);

}
