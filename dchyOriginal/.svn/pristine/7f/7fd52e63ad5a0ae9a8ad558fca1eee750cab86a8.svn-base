package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.dao.ThematicMapDao;
import cn.gtmap.onemap.platform.entity.ThematicMap;
import cn.gtmap.onemap.platform.service.ThematicMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-11-25 下午6:22
 */
@Service
public class ThematicMapServiceImpl implements ThematicMapService<ThematicMap> {

    @Autowired
    private ThematicMapDao thematicMapDao;

    /**
     * get all
     *
     * @return
     */
    @Override
    public List<ThematicMap> getAll() {
        return thematicMapDao.getAll();
    }

    /**
     * get thematic map by id
     *
     * @param id
     * @return
     */
    @Override
    public ThematicMap getById(String id) {
        return thematicMapDao.getOne(id);
    }

    /**
     * insert entity
     *
     * @param entity
     * @return
     */
    @Override
    public ThematicMap insert(ThematicMap entity) {
        return thematicMapDao.saveOrUpdate(entity);
    }

    /**
     * update entity
     *
     * @param entity
     * @return
     */
    @Override
    public ThematicMap update(ThematicMap entity) {
        return thematicMapDao.saveOrUpdate(entity);
    }

    /**
     * delete by id
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        thematicMapDao.delete(id);
    }
}
