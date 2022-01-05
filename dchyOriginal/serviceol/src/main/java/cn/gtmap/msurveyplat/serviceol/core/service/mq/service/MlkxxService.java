package cn.gtmap.msurveyplat.serviceol.core.service.mq.service;

import cn.gtmap.msurveyplat.common.dto.DchyXmglMlkDto;

import java.util.Map;

public interface MlkxxService {

    /**
     * @param param
     * @return
     * @description 2020/12/1 通过mlkid或者sqxxid(以mlkid为住)获取名录库信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    DchyXmglMlkDto getAllData(Map<String, String> param);

    DchyXmglMlkDto getSingleData(String mlkid);
}
