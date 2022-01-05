package cn.gtmap.msurveyplat.server.core.mapper;

import cn.gtmap.msurveyplat.common.domain.DchyCgglGxywxxDO;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcgpz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2021/1/11
 * @description 共享业务信息
 */
@Repository
public interface DchyCgglGxywxxMapper {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param map 参数对象
     * @return
     * @description 获取项目管理系统材料成果配置信息
     */
    List<DchyXmglClcgpz> getDchyXmglClcgpzList(Map map);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param map 参数对象
     * @return
     * @description 获取成果材料名称
     */
    List<String> getCgclmcList(Map map);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param map 参数对象
     * @return
     * @description 获取成果目录名称
     */
    List<String> getCgmlmcList(Map map);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">zhaobiqiang</a>
     * @param map 参数对象
     * @return
     * @description 获取成果材料名称
     */
    List<Map<String, String>> getCgclidList(Map map);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">zhaobiqiang</a>
     * @param map 参数对象
     * @return
     * @description 获取测量事项
     */
    List<String> getCgclClsxList(Map map);


    /**
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @param map 参数对象
     * @return
     * @description 获取成果材料名称
     */
    List<DchyCgglGxywxxDO>  getGxsjList(Map map);


    Map getWjmById(@Param("clid") String clid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param map 参数对象
     * @return
     * @description 获取共享业务信息
     */
    List<DchyCgglGxywxxDO> getDchyCgglGxywxxDOList(Map map);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param map 参数对象
     * @return
     * @description 获取测量事项信息
     */
    DchyXmglZd getDchyXmglZdClsxList(Map map);

}
