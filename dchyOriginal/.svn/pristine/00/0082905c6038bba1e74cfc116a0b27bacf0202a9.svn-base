package cn.gtmap.onemap.dchy.dao;

import cn.gtmap.onemap.dchy.model.DchyCgglXmDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/4/21
 * @description 多测合一
 */
public interface DchyDao extends JpaRepository<DchyCgglXmDO, String> {
    @Query("from DchyCgglXmDO  where dchybh=?1")
    List<DchyCgglXmDO> getDchyXm(String dchybh);

    @Query("from DchyCgglXmDO as a left join a.dchyZdRkztDO b where a.chxmbh=?1 and b.dm='1'")
    List<DchyCgglXmDO> getDchyXmByXmbh(String chxmbh);

}
