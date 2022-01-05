package cn.gtmap.onemap.platform.dao.impl;

import cn.gtmap.onemap.platform.dao.ThematicMapDao;
import cn.gtmap.onemap.platform.entity.ThematicMap;
import cn.gtmap.onemap.platform.service.TemplateService;
import cn.gtmap.onemap.platform.service.impl.BaseLogger;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-11-25 下午5:28
 */
@Repository
public class ThematicMapDaoImpl extends BaseLogger implements ThematicMapDao {

    private final String CONFIG = "thematic.json";

    @Autowired
    private TemplateService templateService;

    /**
     * get config
     *
     * @return
     */
    private List<ThematicMap> getConfig() {
        try {
            return JSON.parseArray(templateService.getTemplate(CONFIG), ThematicMap.class);
        } catch (Exception e) {
            throw new RuntimeException(getMessage("thematic.config.error", e.getLocalizedMessage()));
        }
    }

    /**
     * get all thematicMaps
     *
     * @return
     */
    @Override
    public List<ThematicMap> getAll() {
        return getConfig();
    }

    /**
     * save all thematicMaps
     *
     * @param maps
     * @return
     */
    private List<ThematicMap> saveAll(List<ThematicMap> maps) {
        try {
            Collections.sort(maps);
            templateService.modify(CONFIG, JSON.toJSONString(maps, true));
            return maps;
        } catch (Exception e) {
            throw new RuntimeException(getMessage("thematic.save.error", e.getLocalizedMessage()));
        }
    }

    /**
     * get thematicMap by id
     *
     * @param id
     * @return
     */
    @Override
    public ThematicMap getOne(String id) {
        for (ThematicMap item : getConfig()) {
            if (item.getId().equals(id)) return item;
        }
        throw new RuntimeException(getMessage("thematic.not.found", id));
    }

    /**
     * save or update thematicMap
     *
     * @param entity
     * @return
     */
    @Override
    public ThematicMap saveOrUpdate(ThematicMap entity) {
        List<ThematicMap> maps = getAll();
        for (Iterator<ThematicMap> iterator = maps.iterator(); iterator.hasNext(); ) {
            ThematicMap item = iterator.next();
            if (item.getId().equals(entity.getId())) {
                iterator.remove();
                continue;
            }
        }
        maps.add(entity);
        saveAll(maps);
        return entity;
    }

    /**
     * delete thematicMap
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        List<ThematicMap> maps = getAll();
        for (Iterator<ThematicMap> iterator = maps.iterator(); iterator.hasNext(); ) {
            ThematicMap item = iterator.next();
            if (item.getId().equals(id)) {
                iterator.remove();
                continue;
            }
        }
        saveAll(maps);
    }
}
