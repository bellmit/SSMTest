package cn.gtmap.msurveyplat.promanage.core.mapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChgc;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/11/28
 * @description 测绘工程mapper
 */
@Repository
public interface DchyXmglChgcMapper {

    List<Map<String, Object>> queryChgcxxByPage(Map<String, Object> map);

    /**
     * 成果一棵树台账查询
     * @param map
     * @return
     */
    List<Map<String,Object>> queryChgcForTreeByPage(Map<String,Object> map);

    List<Map<String,Object>> queryChxmByGcid(Map<String,Object> map);

    List<Map<String,Object>> getClsxByChxmid(@Param("chxmid") String chxmid);

    /**
     * @param map
     * @return
     * @description 2021/6/8 成果提交待办列表和已办列表中用过xmid关联数据
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> queryChgcLcxx(Map<String, Object> map);

    /**
     * @param
     * @return
     * @description 2021/1/13 获取工程编号
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<DchyXmglChgc> queryChgcxx(Map<String, Object> map);


    /**
     * @param map
     * @return
     * @description 2021/1/18 获取最新的chxmid,clsxid,clsx
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> queryClcgByGcbh(Map<String, Object> map);

    /**
     * @return java.util.List<cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChgc>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: paramMap
     * @time 2021/2/23 10:51
     * @description 测量成果查找对应的测绘工程信息
     */
    List<DchyXmglChgc> queryChgcxxByClcgxx(Map<String, Object> paramMap);

    /**
     * @param map
     * @return
     * @description 2021/3/1 通过测绘工程编号和测量事项判断该测量事项是否已经备案,
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> queryHtxxByChgcbhAndClsx(Map<String, Object> map);
}
