package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.SM4Util;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglJsdwMapper;
import cn.gtmap.msurveyplat.promanage.core.service.ConstructionService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-09
 * description
 */
@Service
public class ConstructionServiceImpl implements ConstructionService {
    @Autowired
    private Repository repository;

    @Autowired
    private DchyXmglJsdwMapper dchyXmglJsdwMapper;

    private final String DELETED = "delete_";

    private Logger logger = LoggerFactory.getLogger(ConstructionServiceImpl.class);


    @Override
    public List<Map<String, Object>> queryConstructionList() {
        List<Map<String, Object>> jsdwList = null;
        try {
            Map<String, Object> paramMap = Maps.newHashMap();
            jsdwList = dchyXmglJsdwMapper.queryJsdwList(paramMap);
        } catch (Exception e) {
            logger.error("错误原因{}：", e);
        }
        return jsdwList;
    }

    @Override
    public Page<Map<String, Object>> queryConstructionInfo(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String jsdwmc = CommonUtil.formatEmptyValue(data.get("jsdwmc"));
        String tyshxydm = CommonUtil.formatEmptyValue(data.get("tyshxydm"));
        String jsdwm = CommonUtil.formatEmptyValue(data.get("jsdwm"));
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("jsdwmc", jsdwmc);
        paramMap.put("tyshxydm", SM4Util.encryptData_ECB(tyshxydm));
        paramMap.put("jsdwm", jsdwm);
        int page = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("page")));
        int pageSize = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("pageSize")));
        Page<Map<String, Object>> resultList = null;
        try {
            resultList = repository.selectPaging("queryJsdwmByPage", paramMap, page - 1, pageSize);
        } catch (Exception e) {
            logger.error("错误原因{}：", e);
        }
        return resultList;
    }
}
