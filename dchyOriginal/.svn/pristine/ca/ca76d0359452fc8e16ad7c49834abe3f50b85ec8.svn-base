package cn.gtmap.msurveyplat.server.core.mapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmClsx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcgShjl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2021/1/11
 * @description 项目管理系统测绘工程Mapper接口
 */
@Repository
public interface DchyXmglChgcMapper {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param chgcid 测绘工程ID
     * @return
     * @description 根据测绘工程ID获取测绘工程测量事项信息
     */
    List<DchyXmglChxmClsx> getDchyXmglChgcClsxListByChgcid(@Param("chgcid") String chgcid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param map 参数对象
     * @return
     * @description 根据测量事项获取测绘单位名称
     */
    String getChdmcByClsx(Map map);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param map 参数对象
     * @return
     * @description 根据测绘项目获取测绘单位名称
     */
    String getChdmcByChxm(Map map);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param map 参数对象
     * @return
     * @description 获取最新审核通过的审核数据
     */
    DchyXmglClcgShjl getNewDchyXmglClcgShjl(Map map);

}
