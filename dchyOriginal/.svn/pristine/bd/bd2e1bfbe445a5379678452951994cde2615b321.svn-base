package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.dao.DictDao;
import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.dict.Dict;
import cn.gtmap.onemap.platform.entity.dict.Item;
import cn.gtmap.onemap.platform.service.DictService;
import cn.gtmap.onemap.platform.service.WebMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author: <a href="mailto:yxfacw@live.com">yingxiufeng</a>
 * @date: 2013-09-27 上午10:19
 * @version: 1.0
 */
@Service
public class DictServiceImpl extends BaseLogger implements DictService {
    
    @Autowired
    private DictDao dictDao;

    private static final String ANLYSIS_PREFIX = "analysis_";

    private static final String DEFAULT_DICT_TDLYXZ= "tdlyxz";


    /***
     * get all dicts
     * @return
     */
    @Override
    public List<Dict> getAll() {
        return dictDao.findAll();
    }
    /***
     *
     * @param id
     * @return
     */
    @Override
    public Dict getDict(String id) {
        return dictDao.findOne(id);
    }

    /***
     * save dict
     * @return
     */
    @Override
    public Dict saveDict(Dict dict) {
        try {
            if (isNull(dict.getId()))
                return dictDao.saveAndFlush(dict);
            else {
                Dict existDict = dictDao.findOne(dict.getId());
                if (isNotNull(existDict)) {
                    existDict.setName(dict.getName());
                    existDict.setValue(dict.getValue());
                    existDict.setTitle(dict.getTitle());
                    existDict.setSql(dict.getSql());
                    return dictDao.saveAndFlush(existDict);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return null;
    }

    /***
     * delete dict by id
     * @param dictId
     */
    @Override
    @Transactional
    public void deleteDict(String dictId) {
        dictDao.delete(dictId);
    }

    /***
     *delete dict batch
     * @param idList
     */
    @Override
    @Transactional
    public void deleteDictBatch(List<String> idList) {
        List<Dict> dicts = new ArrayList<Dict>();
        try {
            for (String id : idList) {
                dicts.add(getDict(id));
            }
            if (dicts.size() > 0)
                dictDao.deleteInBatch(dicts);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /***
     *
     * @param name
     * @return
     */
    @Override
    public boolean validateDictName(String name) {
        for (String n : dictDao.findDictNames()) {
            if (name.equals(n)) return false;
        }
        return true;
    }

    /***
     *
     * @param name
     * @return
     */
    @Override
    public boolean validateItemName(String name) {
        for (String n : dictDao.findItemNames()) {
            if (name.equals(n)) return false;
        }
        return true;
    }

    /***
     * save dict item
     * @param dictId
     * @return
     */
    @Override
    public Item saveDictItem(String dictId, Item item) {
        Assert.notNull(dictId);
        Dict dict = null;
        try {
            dict = dictDao.findOne(dictId);
            Set<Item> items = dict.getItems();
            for (Item item1 : items) {
                if (item1.getId().equals(item.getId())) {
                    items.remove(item1);
                    break;
                }
            }
            item.setDict(dict);
            items.add(item);
            dictDao.saveAndFlush(dict);
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }


    /***
     * get dict item
     * @param dictId
     * @param itemId
     * @return
     */
    @Override
    public Item getDictItem(String dictId, String itemId) {
        Assert.notNull(dictId);
        Dict dict = dictDao.findOne(dictId);
        Set<Item> items = dict.getItems();
        while (items.iterator().hasNext()) {
            Item item = items.iterator().next();
            if (itemId.equals(item.getId()))
            {
                item.setDict(null);
                return item;
            }
        }
        return null;
    }

    /***
     * delete dict item
     * @param dictId
     * @param itemId
     */
    @Override
    @Transactional
    public void deleteDictItem(String dictId, String itemId) {
        try {
            Assert.notNull(dictId);
            Dict dict=dictDao.findOne(dictId);
            Set<Item> nItems=new HashSet<Item>();
            Set<Item> items=dict.getItems();
            for (Item item : items) {
                if(!item.getId().equals(itemId))
                    nItems.add(item);
            }
            dict.setItems(nItems);
            dictDao.save(dict);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /***
     * get dict by name
     * @param name
     * @return
     */
    @Override
    public Dict getDictByName(String name) {
        List<Dict> dicts=dictDao.findByName(name);
        return isNotNull(dicts) && dicts.size()>0 ?dicts.get(0):null;
    }

    /***
     * 获取分析时分组字段相对应的字典项配置 ,字典名称:前缀_分组字段名(如,analysis_dlbm)
     * @param groupName 分组字段名称
     * @return
     */
    @Override
    public List<Map> getAnalysisDictMap(String groupName) {
        List<Map> list = new ArrayList<Map>();
        String dictName = ANLYSIS_PREFIX + groupName.toLowerCase();
        try {
            List<Dict> dicts=dictDao.findByName(dictName);
            if(isNotNull(dicts)&&isNotNull(dicts.get(0))){
                Dict dict =dicts.get(0);
                Iterator<Item> itemIterator=dict.getItems().iterator();
                while (itemIterator.hasNext()){
                    Item item=itemIterator.next();
                    Map temp = new HashMap();
                    temp.put(item.getName(),item.getValue());
                    list.add(temp);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return list;
    }

    /***
     * 获取土地利用现状分析所需的字典配置
     * @return
     */
    @Override
    public List<Map> getTdlyxzDictList() {
        Dict dict = getDictByName(DEFAULT_DICT_TDLYXZ);
        List<Map> list = new ArrayList<Map>();
        if (isNull(dict)) {
            throw new RuntimeException(getMessage("default.dict.not.exist", DEFAULT_DICT_TDLYXZ));
        }
        Set<Item> dictItems = dict.getItems();
        for (Item item : dictItems) {
            Map temp = new HashMap();
            temp.put("dlbm", item.getName());
            temp.put("dlmc", item.getValue());
            temp.put("area", 0);
            temp.put("jtArea", 0);
            temp.put("gyArea", 0);
            list.add(temp);
        }
        return list;
    }

}
