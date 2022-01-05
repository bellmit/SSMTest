package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYwyzxx;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.promanage.core.service.ExcuteService;
import cn.gtmap.msurveyplat.promanage.core.service.YwljyzService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 业务类型执行类
 */

@Service
public class ApiService {

    @Autowired
    private List<YwljyzService> ywljyzServiceList;


    @Autowired
    private EntityMapper entityMapper;

    /**
     * @param map
     * @return
     */
    public Map ywljyzExcutor(Map map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        List<String> yzsxid = (List<String>) (data.get("yzsxid"));
        String bdid = CommonUtil.formatEmptyValue(data.get("bdid"));
        String ssmkid = CommonUtil.formatEmptyValue(data.get("ssmkid"));
        Object obj = MapUtils.getObject(map, "data");
        Map resrultMap = Maps.newHashMap();
        ExcuteService excuteService;
        boolean checkresult = false;
        String ywyzljdmFailed = null;
        for (String ywyzljdm : yzsxid) {
            excuteService = getExcuteService(ywyzljdm);
            if (null != excuteService) {
                checkresult = excuteService.checkInfos(obj);
                if (!checkresult) {
                    ywyzljdmFailed = ywyzljdm;
                    break;
                }
            }
        }
        if (StringUtils.isNotBlank(ywyzljdmFailed)) {
            Example example = new Example(DchyXmglYwyzxx.class);
            example.createCriteria().andEqualTo("ssmkid", ssmkid).andEqualTo("bdid", bdid).andEqualTo("ywyzljdm", ywyzljdmFailed);
            List<DchyXmglYwyzxx> dchyXmglYwyzxxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(dchyXmglYwyzxxList)) {
                DchyXmglYwyzxx dchyXmglYwyzxx = dchyXmglYwyzxxList.get(0);
                resrultMap.put("yzms", dchyXmglYwyzxx.getYzms());
                resrultMap.put("yztsxx", dchyXmglYwyzxx.getYztsxx());
            }

        }
        resrultMap.put("yzjg", checkresult);

        return resrultMap;
    }

    private ExcuteService getExcuteService(String ywyzljdm) {
        for (YwljyzService ywljyzService : ywljyzServiceList) {
            if (StringUtils.equals(ywljyzService.getCode(), ywyzljdm)) {
                return ywljyzService;
            }
        }
        return null;
    }

}
