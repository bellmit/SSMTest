package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChgc;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxm;
import cn.gtmap.msurveyplat.promanage.core.service.LogService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public Map<String, Object> xgrz(int page, String chxmid, LinkedHashMap linkedHashMap) {
        Map resultMap = Maps.newHashMap();
        if (StringUtils.isNoneBlank(chxmid)) {
            List<Map> content = Lists.newArrayList();
            List<Map> list = (List<Map>) MapUtils.getObject(linkedHashMap, "content");
            if (CollectionUtils.isNotEmpty(list)) {
                DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
                Map chgcxx = Maps.newHashMap();
                chgcxx.put("GCBH", "");
                chgcxx.put("GCMC", "");
                chgcxx.put("XMDZ", "");

                if (null != dchyXmglChxm && StringUtils.isNoneBlank(dchyXmglChxm.getChgcid())) {
                    DchyXmglChgc dchyXmglChgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, dchyXmglChxm.getChgcid());
                    chgcxx.put("GCBH", dchyXmglChgc.getGcbh());
                    chgcxx.put("GCMC", dchyXmglChgc.getGcmc());
                    chgcxx.put("XMDZ", dchyXmglChgc.getGcdzxx());
                }
                Map contentMap;
                int size = MapUtils.getIntValue(linkedHashMap,"size");
                int number = 1;
                for (Map temp : list) {
                    contentMap = Maps.newHashMap();
                    contentMap.putAll(chgcxx);
                    contentMap.put("XGRMC", MapUtils.getString(temp, "czrmc"));
                    contentMap.put("XGSJ", MapUtils.getString(temp, "czsj"));
                    contentMap.put("RZID", MapUtils.getString(temp, "rzid"));
                    contentMap.put("ROWNUM_", (page-1)*size+number);
                    content.add(contentMap);
                    number++;
                }
            }
            resultMap.put("content",content);
            resultMap.put("totalPages",MapUtils.getObject(linkedHashMap, "totalPages"));
            resultMap.put("totalElements",MapUtils.getObject(linkedHashMap, "totalElements"));
        }
        return resultMap;
    }
}
