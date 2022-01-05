package cn.gtmap.msurveyplat.promanage.core.mapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClsxChdwxxGx;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DchyXmglClsxChdwxxGxMapper {

    /**
     * @param
     * @return
     * @description 2020/11/28 获取表所有信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<DchyXmglClsxChdwxxGx> getClsxChdwgxByHtxxid(Map<String, Object> param);

    /**
     * @param
     * @return
     * @description 2021/03/05 根据chxmid与fdm判断测绘单位
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     */
    List<Map<String, Object>> getChdwxxByChxmidAndFdm(Map<String, Object> param);

}
