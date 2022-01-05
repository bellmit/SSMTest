package cn.gtmap.msurveyplat.serviceol.core.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd;

import java.util.List;
import java.util.Map;

public interface DchyXmglZdService {

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param:
     * @return void
     * @time 2020/12/1 17:04
     * @description 重置字典信息
     */
    void reSetZdxx();

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: zdlx
     * @return java.util.List<cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd>
     * @time 2020/12/1 17:06
     * @description 通过字典类型获取字典项类型，一般用于表单填报初始化
     */
    List<DchyXmglZd> getDchyXmglZdListByZdlx(String zdlx);

    /**
     * @param zdlx
     * @param dm
     * @return
     * @description 2020/12/2 通过字典类型和代码获取名称
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    DchyXmglZd getDchyXmglByZdlxAndDm(String zdlx, String dm);

    /**
     * @param zdlx
     * @param mc
     * @return
     * @description 2020/12/2 通过字典类型和名称获取代码
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    DchyXmglZd getDchyXmglByZdlxAndMc(String zdlx, String mc);

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: zdlx
     * @return java.util.List<cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd>
     * @time 2020/12/1 17:06
     * @description 通过字典类型获取字典项类型，一般用于表单填报初始化
     */
    List<Map<String,Object>> getDchyXmglZdListByZdlx(List<String> zdlxList);

    List<Map<String,Object>> getZdClsx(Map<String,Object> paramMap);

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: zdlx
     * @param: dm
     * @return java.lang.String
     * @time 2021/2/23 21:38
     * @description 返回字典项名称
     */
    String getZdmcByZdlxAndDm(String zdlx, String dm);

    /**
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     * @param: zdlx
     * @param: mc
     * @return java.lang.String
     * @time 2021/3/1 21:38
     * @description 返回字典项代码
     */
    String getZddmByZdlxAndMc(String zdlx, String mc);
}

