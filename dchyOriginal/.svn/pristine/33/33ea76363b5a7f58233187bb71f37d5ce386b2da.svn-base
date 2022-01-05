package cn.gtmap.msurveyplat.portalol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.portalol.core.service.DchyXmglMbService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/30 14:42
 * @description
 */
@Service
public class DchyXmglMbServiceImpl implements DchyXmglMbService {

    @Autowired
    Repository repository;

    @Override
    public Page<Map<String, Object>> queryMbQyztListByPage(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        int page = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("page")));
        int pageSize = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("pageSize")));
        return repository.selectPaging("queryMbQyztListByPage", data, page - 1, pageSize);
    }

}
