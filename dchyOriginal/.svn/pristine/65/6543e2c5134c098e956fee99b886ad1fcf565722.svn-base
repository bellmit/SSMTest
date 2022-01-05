package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.LocationMark;
import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-29 上午9:53
 */
public interface LocationMarkService {

    /**
     * save
     *
     * @param entity
     * @return
     */
    LocationMark save(LocationMark entity);

    /**
     * delete
     *
     * @param id
     * @return
     */
    boolean delete(String id);

    /**
     * upate
     *
     * @param entity
     * @return
     */
    LocationMark update(LocationMark entity);

    /**
     *  get one
     * @param id
     * @return
     */
    LocationMark get(String id);

    /**
     * get all
     * @return
     */
    List<LocationMark> findAll();

    /**
     * 根据owner获取marks
     * @param owner
     * @return
     */
    List<LocationMark> getLocationMarksByOwner(String owner);

    /**
     *根据所有者和公开性获取marks
     * @param owner
     * @param publicity
     * @return
     */
    List<LocationMark> getLocationMarksByOwnerOrPublicity(String owner,String publicity);


}
