package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.dict.Dict;
import cn.gtmap.onemap.platform.entity.dict.Item;

import java.util.List;
import java.util.Map;

/**
 * @author: <a href="mailto:yxfacw@live.com">yingxiufeng</a>
 * @date: 2013-09-27 上午10:18
 * @version: 1.0
 */
public interface DictService {

    /***
     * get all dicts
     * @return
     */
    List<Dict> getAll();
    /***
     * get dict by id
     * @param id
     * @return
     */
    Dict getDict(String id);

    /***
     * save dict
     * @return
     */
    Dict saveDict(Dict dict);

    /***
     * delete dict by id
     * @param dictId
     */
    void deleteDict(String dictId);

    /***
     *
     * @param idList
     */
    void deleteDictBatch(List<String> idList);

    /***
     * 验证name是否已经存在
     * @param name
     * @return
     */
    boolean validateDictName(String name);

    /***
     * 验证name是否已经存在
     * @param name
     * @return
     */
    boolean validateItemName(String name);

    /***
     * save dict item
     * @param dictId
     * @param item
     * @return
     */
    Item saveDictItem(String dictId, Item item);

    /***
     *
     * @param dictId
     * @param itemId
     * @return
     */
    Item getDictItem(String dictId,String itemId);

    /***
     * delete dict item
     * @param dictId
     * @param itemId
     */
    void deleteDictItem(String dictId,String itemId);

    /**
     * get dict by name
     * @param name
     * @return
     */
    Dict getDictByName(String name);
    /**
     *  获取分析时分组字段相对应的字典项配置 ,字典名称:前缀_分组字段名(如,analysis_dlbm)
     * @param groupName 分组字段名称
     * @return
     */
    List<Map> getAnalysisDictMap(String groupName);

    /***
     * 获取土地利用现状分析所需的字典配置
     * @return
     */
    List<Map> getTdlyxzDictList();

}
