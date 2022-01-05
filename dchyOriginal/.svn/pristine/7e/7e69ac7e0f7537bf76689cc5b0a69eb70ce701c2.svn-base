package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.dict.Dict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/5/26 11:23
 */
public interface DictDao extends JpaRepository<Dict,String> {

    /***
     *
     * @param name
     * @return
     */
    List<Dict> findByName(String name);

    @Query("select t.name from Dict t")
    List<String> findDictNames();

    @Query("select t.name from Item t")
    List<String> findItemNames();
}
