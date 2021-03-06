package cn.gtmap.msurveyplat.portal.dao.impl;

import cn.gtmap.msurveyplat.portal.dao.BaseDao;
import cn.gtmap.msurveyplat.portal.utils.QueryCondition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;

@Repository
public  class BaseDaoImpl implements BaseDao {

    @PersistenceContext(unitName = "entityManagerFactory")
    EntityManager em;

    public <T> T getById(Class<T> clazz, Object id) {
        return em.find(clazz, id);
    }
    @Transactional
    public <T> void delete(Class<T> clazz, Object id) {
    	if (id != null) {
    		T entity = getById(clazz, id);
            if(entity!=null){
                delete(entity);
            }
		}
    }
    @Transactional
    public <T> void delete(Object entity){
    	em.remove(entity);
    }
    @Transactional
    public <T> void delete(Class<T> clazz, Object[] ids) {
    	if (ids != null) {
    		for(Object id : ids) {
            	delete(clazz,id);
            }
		}
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAll(Class<T> clazz) {
        String className = clazz.getSimpleName();
        StringBuffer jpql = new StringBuffer("select o from ");
        jpql.append(className).append(" o ");
        return em.createQuery(jpql.toString()).getResultList();
    }
    @Transactional
    public void save(Object entity) {
        em.persist(entity);
    }
    @Transactional
    public void update(Object entity) {
        em.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> get(Class<T> clazz, List<QueryCondition> queryConditions, String orderBy, int currentPage, int pageSize) {
        Query query = getQuery(clazz, queryConditions, orderBy, false);
        if(currentPage == 0 && pageSize == 0) {
            return query.getResultList();
        }else {
            return query.setFirstResult((currentPage-1)*pageSize).setMaxResults(pageSize).getResultList();
        }

    }

/**
     * ????????????????????????Query
     * @param clazz
     * @param queryConditions
     * @param orderBy
     * @param isQueryTotal  ????????????????????????, true ?????????????????????
     * @return
     */

    @SuppressWarnings("rawtypes")
    private  Query getQuery(Class clazz, List<QueryCondition> queryConditions, String orderBy, boolean isQueryTotal) {
        String className = clazz.getSimpleName();
        String preJPQL = isQueryTotal?"select count(*) from ":"select o from ";
        StringBuffer jpql = new StringBuffer(preJPQL);
        jpql.append(className).append(" o where 1=1 ");
        Query query = null;
        if(queryConditions != null && queryConditions.size() > 0) {
            //??????jpql??????
            Iterator<QueryCondition> iterator = queryConditions.iterator();
            while(iterator.hasNext()) {
                QueryCondition queryCondition = iterator.next();
                if(queryCondition!=null) {
                    if(queryCondition.getOperator().equals(QueryCondition.CUSTOM)) {
                        jpql.append(" and (").append(queryCondition.getCustomJPQL()).append(")");
                    }
                    if(queryCondition.getValue() != null && !"".equals(queryCondition.getValue())) {
                        //????????????????????????*.*??????????????????*_*?????????????????????????????????????????????
                        String placeholder = queryCondition.getField().indexOf(".")!=-1 ? queryCondition.getField().replace(".", "_"):queryCondition.getField();
                        jpql.append(" and o.").append(queryCondition.getField().trim())
                                .append(" ").append(queryCondition.getOperator()).append(":").append(placeholder.trim());
                    }
                }

            }
        }
        if(orderBy != null && !"".equals(orderBy)) {
            jpql.append(" ").append(orderBy);
        }

        query = em.createQuery(jpql.toString());

        if(queryConditions != null && queryConditions.size() > 0) {
            //???????????????
            Iterator<QueryCondition> iterator2 = queryConditions.iterator();
            while(iterator2.hasNext()) {
                QueryCondition queryCondition = iterator2.next();
                if(queryCondition!=null) {
                    if(queryCondition.getValue() != null && !"".equals(queryCondition.getValue())) {
                        //??????????????????.?????????_
                        queryCondition.setField(queryCondition.getField().indexOf(".") != -1 ? queryCondition.getField().replace(".", "_"):queryCondition.getField());
                        if(queryCondition.getOperator().equals(QueryCondition.LK)) {
                            query.setParameter(queryCondition.getField(), "%"+queryCondition.getValue()+"%");
                        }else {
                            query.setParameter(queryCondition.getField(), queryCondition.getValue());
                        }
                    }
                }

            }
        }
        return query;
    }



    public <T> List<T> get(Class<T> clazz, List<QueryCondition> queryConditions) {
        return get(clazz, queryConditions, null, 0, 0);
    }


    public <T> List<T> get(Class<T> clazz, List<QueryCondition> queryConditions, String orderBy) {
        return get(clazz, queryConditions, orderBy, 0, 0);
    }

    @SuppressWarnings("rawtypes")
    public Object getSingleResult(Class clazz, List<QueryCondition> queryConditions) {
        Query query = getQuery(clazz, queryConditions, null, false);
        return query.getSingleResult();
    }

    @SuppressWarnings("rawtypes")
    public long getRecordCount(Class clazz, List<QueryCondition> queryConditions) {
        Query query = getQuery(clazz, queryConditions, null, true);
        Object result = query.getSingleResult();
        long recordCount = 0L;
        if(result != null) {
            recordCount = ((Long)result).longValue();
        }
        return recordCount;
    }



    @SuppressWarnings("unchecked")
    public <T> List<T> getByJpql(String jpql, Object... objects) {
        Query query = em.createQuery(jpql);
        if(objects != null) {
            for(int i = 0 ; i < objects.length ; i ++){
                query.setParameter(i, objects[i]);
            }
        }
        return query.getResultList();
    }

    public int executeJpql(String jpql, Object...objects) {
        Query query = em.createQuery(jpql);
        if (objects != null) {
            for(int i = 0 ; i < objects.length ; i ++){
                query.setParameter(i, objects[i]);
            }
        }
        return query.executeUpdate();
    }

    public Object getUniqueResultByJpql(String jpql, Object... objects) {
        Query query = em.createQuery(jpql);
        if (objects != null) {
            for(int i = 0 ; i < objects.length ; i ++){
                query.setParameter(i, objects[i]);
            }
        }
    	try {
    		return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
        
    }
}
