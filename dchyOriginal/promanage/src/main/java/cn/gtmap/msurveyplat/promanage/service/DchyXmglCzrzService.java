package cn.gtmap.msurveyplat.promanage.service;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;

import java.util.Map;

public interface DchyXmglCzrzService {

    /**
     * @param param
     * @return
     * @description 2021/4/30 线下操作日志管理台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage queryCzrzList(Map<String, Object> param);
}
