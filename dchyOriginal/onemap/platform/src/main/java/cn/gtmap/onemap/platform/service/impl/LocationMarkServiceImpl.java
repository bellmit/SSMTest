package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.dao.LocationMarkDao;
import cn.gtmap.onemap.platform.entity.LocationMark;
import cn.gtmap.onemap.platform.service.LocationMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-29 上午9:56
 */
@Service
public class LocationMarkServiceImpl extends BaseLogger implements LocationMarkService {

    @Autowired
    private LocationMarkDao locationMarkDao;

    /**
     * save
     *
     * @param entity
     * @return
     */
    @Transactional
    @Override
    public LocationMark save(LocationMark entity) {
        return locationMarkDao.save(entity);
    }

    /**
     * delete
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public boolean delete(String id) {
        locationMarkDao.delete(id);
        return true;
    }

    /**
     * upate
     *
     * @param entity
     * @return
     */
    @Transactional
    @Override
    public LocationMark update(LocationMark entity) {
        return locationMarkDao.save(entity);
    }

    /**
     * get one
     * @param id
     * @return
     */
    @Override
    public LocationMark get(String id) {
        return locationMarkDao.findOne(id);
    }

    /**
     *
     * @return
     */
    @Override
    public List<LocationMark> findAll() {
        return locationMarkDao.findAll();
    }

    /**
     * get by owner
     * @param owner
     * @return
     */
    @Override
    public List<LocationMark> getLocationMarksByOwner(String owner) {
        List<LocationMark> locationMarkList = locationMarkDao.findByOwner(owner);
        return locationMarkList;
    }

    /**
     *根据所有者和公开性获取marks
     * @param owner
     * @param publicity
     * @return
     */
    @Override
    public List<LocationMark> getLocationMarksByOwnerOrPublicity(String owner, String publicity) {
        List<LocationMark> locationMarkList = locationMarkDao.findByOwnerOrPublicity(owner,publicity);
        return locationMarkList;
    }
}
