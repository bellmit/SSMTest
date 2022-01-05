package cn.gtmap.onemap.platform.service;

import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-11-25 下午6:21
 */
public interface ThematicMapService<T> {

    /**
     * get all thematic maps
     *
     * @return
     */
    List<T> getAll();

    /**
     * get thematic map by id
     *
     * @param id
     * @return
     */
    T getById(String id);

    /**
     * insert entity
     *
     * @param entity
     * @return
     */
    T insert(T entity);

    /**
     * update entity
     *
     * @param entity
     * @return
     */
    T update(T entity);

    /**
     * delete by id
     *
     * @param id
     */
    void delete(String id);


}
