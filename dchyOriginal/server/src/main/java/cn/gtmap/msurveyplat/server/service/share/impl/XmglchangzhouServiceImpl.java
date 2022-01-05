package cn.gtmap.msurveyplat.server.service.share.impl;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.server.service.share.XmglchangzhouService;
import cn.gtmap.msurveyplat.server.util.PromangeFeginServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0, 2021/3/17
 * @description 项目管理常州服务
 */
@Service
public class XmglchangzhouServiceImpl implements XmglchangzhouService {

    @Autowired
    private PromangeFeginServiceUtil promangeFeginServiceUtil;


    @Override
    public Object getAllZzdjs(Map<String, Object> param) {
        return promangeFeginServiceUtil.getAllZzdjs(param);
    }

    @Override
    public ResponseMessage queryResultsManagement(Map<String, Object> param) {
        return promangeFeginServiceUtil.queryResultsManagement(param);
    }

    @Override
    public ResponseMessage getProjectConstrctInfo(Map<String, Object> param) {
        return promangeFeginServiceUtil.getProjectConstrctInfo(param);
    }


}
