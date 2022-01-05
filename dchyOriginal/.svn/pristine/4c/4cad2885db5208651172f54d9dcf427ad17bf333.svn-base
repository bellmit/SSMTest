package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxm;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChxmChdwxxMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglClsxChdwxxGxMapper;
import cn.gtmap.msurveyplat.promanage.core.service.YwljyzService;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
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
public class YwljyzCommissionServiceImpl implements YwljyzService {
    @Autowired
    DchyXmglChxmChdwxxMapper dchyXmglChxmChdwxxMapper;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    DchyXmglClsxChdwxxGxMapper dchyXmglClsxChdwxxGxMapper;

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @version 1.o, 2021-03-05
     * description  同一个工程下面，2、3、4阶段测绘事项只能被同一家单位选择
     */
    @Override
    public boolean checkInfos(Object object) {
        Map map = JSONObject.parseObject(JSONObject.toJSONString(object), Map.class);
        String gcbh = MapUtils.getString(map, "gcbh");
        List<Map<String, Object>> clsxList = (List<Map<String, Object>>) map.get("clsx");
        List<String> chdwidList = (List<String>) map.get("chdwid");

        //根据工程编号获取工程下所有chxmid
        Example example = new Example(DchyXmglChxm.class);
        example.createCriteria().andEqualTo("chgcbh", gcbh);
        List<DchyXmglChxm> chxmList = entityMapper.selectByExampleNotNull(example);
        List<String> chxmidList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(chxmList)) {
            for (DchyXmglChxm chxmLists : chxmList) {
                chxmidList.add(chxmLists.getChxmid());
            }
        }

        //根据测绘事项进行判断
        for (Map<String, Object> clsxLists : clsxList) {
            String fdm = MapUtils.getString(clsxLists, "fdm");
            //判断是否不为第一阶段
            if (!StringUtils.equals(fdm, "1")) {
                Map<String, Object> parammMap = Maps.newHashMap();
                parammMap.put("chxmidList", chxmidList);
                parammMap.put("fdm", fdm);
                List<Map<String, Object>> chdwxxMap = dchyXmglClsxChdwxxGxMapper.getChdwxxByChxmidAndFdm(parammMap);
                List<String> oldChdwidList = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(chdwxxMap)) {
                    for (Map<String, Object> chdwxxMaps : chdwxxMap) {
                        oldChdwidList.add(MapUtils.getString(chdwxxMaps, "MLKID"));
                    }

                    for (Iterator itChdwxxList = chdwidList.iterator(); itChdwxxList.hasNext(); ) {
                        if (!oldChdwidList.contains(itChdwxxList.next())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String getCode() {
        return "commission";
    }
}
