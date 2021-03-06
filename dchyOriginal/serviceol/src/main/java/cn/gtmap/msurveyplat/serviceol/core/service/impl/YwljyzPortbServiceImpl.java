package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmChdwxx;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglChdwxxService;
import cn.gtmap.msurveyplat.serviceol.core.service.YwljyzService;
import cn.gtmap.msurveyplat.serviceol.utils.ObjectToList;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-11-28
 * description
 */

@Service
public class YwljyzPortbServiceImpl implements YwljyzService {
    @Autowired
    DchyXmglChdwxxService dchyXmglChdwxxService;

    @Override
    public boolean checkInfos(Object object) {
        Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSONString(object), Map.class);
        String chgcbh = (String) map.get("chgcbh");
        String fdm = (String) map.get("fdm");
        List<String> chdwmcParam = ObjectToList.castList(map.get("chdwmc"), String.class);
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("chgcbh", chgcbh);
        paramMap.put("fdm", fdm);
        List<DchyXmglChxmChdwxx> chdwxxList = dchyXmglChdwxxService.getChdwxxList(paramMap);
        List chdwmcList = Lists.newArrayList();
        for (int i = 0; i < chdwxxList.size(); i++) {
            String chdwmc = chdwxxList.get(i).getChdwmc();
            chdwmcList.add(chdwmc);
        }
        for (Iterator itChdwxxList = chdwmcParam.iterator(); itChdwxxList.hasNext(); ) {
            if (!chdwmcList.contains(itChdwxxList.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getCode() {
        return "portb";
    }
}
