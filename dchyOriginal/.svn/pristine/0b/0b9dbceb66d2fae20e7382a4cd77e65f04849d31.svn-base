package cn.gtmap.msurveyplat.portalol.core.service.impl;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.portalol.core.mapper.DchyXmglZdMapper;
import cn.gtmap.msurveyplat.portalol.core.service.DchyXmglZdService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/1 14:00
 * @description
 */
@Service
public class DchyXmglZdServiceImpl implements DchyXmglZdService {

    @Autowired
    DchyXmglZdMapper dchyXmglZdMapper;

    // 字典列表map
    private final Map<String, List<DchyXmglZd>> dchyXmglZdListMap = Maps.newHashMap();

    // 名称对照map
    private final Map<String, String> dchyXmglZdMcdzMap = Maps.newHashMap();

    @Override
    public List<Map> getDchyZdBlsxList() {
        return dchyXmglZdMapper.getDchyZdBlsxList();
    }

    /**
     * @return void
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param:
     * @time 2020/12/1 17:04
     * @description 重置字典信息
     */
    @Override
    @PostConstruct
    @SystemLog(czmkMc = ProLog.CZMK_CZZD_MC, czmkCode = ProLog.CZMK_CZZD_CODE, czlxMc = ProLog.CZLX_RESET_MC, czlxCode = ProLog.CZLX_RESET_CODE)
    public synchronized void reSetZdxx() {
        // 清空
        dchyXmglZdListMap.clear();
        dchyXmglZdMcdzMap.clear();

        // 获取列表
        List<DchyXmglZd> dchyXmglZdList = dchyXmglZdMapper.getAllDchyXmglZdList();

        // 赋值
        String zdlx;
        String dm;
        String key;
        List<DchyXmglZd> temp;
        if (CollectionUtils.isNotEmpty(dchyXmglZdList)) {
            for (DchyXmglZd dchyXmglZd : dchyXmglZdList) {
                dm = dchyXmglZd.getDm();
                zdlx = dchyXmglZd.getZdlx();
                key = zdlx + "~" + dm;
                if (!dchyXmglZdMcdzMap.containsKey(key)) {
                    dchyXmglZdMcdzMap.put(key, dchyXmglZd.getMc());
                }
                temp = dchyXmglZdListMap.get(zdlx);
                if (CollectionUtils.isEmpty(temp)) {
                    temp = Lists.newArrayList();
                }
                temp.add(dchyXmglZd);
                dchyXmglZdListMap.put(zdlx, temp);
            }
        }
    }

    /**
     * @return java.util.List<cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: zdlx
     * @time 2020/12/1 17:06
     * @description 通过字典类型获取字典项类型，一般用于表单填报初始化
     */
    @Override
    public List<DchyXmglZd> getDchyXmglZdListByZdlx(String zdlx) {
        return dchyXmglZdListMap.get(zdlx);
    }

    @Override
    public DchyXmglZd getDchyXmglByZdlxAndDm(String zdlx, String dm) {
        List<DchyXmglZd> dchyXmglZdList = dchyXmglZdListMap.get(zdlx);
        for (DchyXmglZd dchyXmglZd : dchyXmglZdList) {
            if (dm.equals(dchyXmglZd.getDm())) {
                return dchyXmglZd;
            }
        }
        return null;
    }

    /**
     * @param zdlxList
     * @return java.util.List<cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: zdlx
     * @time 2020/12/1 17:06
     * @description 通过字典类型获取字典项类型，一般用于表单填报初始化
     */
    @Override
    public List<Map<String, Object>> getDchyXmglZdListByZdlx(List<String> zdlxList) {
        List<Map<String, Object>> zdLxList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(zdlxList)) {
            List<DchyXmglZd> zdList;
            Map<String, Object> zdMap;
            for (String zdlx : zdlxList) {
                zdList = getDchyXmglZdListByZdlx(zdlx);
                if (CollectionUtils.isNotEmpty(zdList)) {
                    for (DchyXmglZd dchyXmglZd : zdList) {
                        zdMap = Maps.newHashMap();
                        zdMap.put("DM", dchyXmglZd.getDm());
                        zdMap.put("MC", dchyXmglZd.getMc());
                        zdMap.put("ZDLX", dchyXmglZd.getZdlx());
                        zdMap.put("FDM", dchyXmglZd.getFdm());
                        zdLxList.add(zdMap);
                    }
                }
            }
        }
        return zdLxList;
    }

    @Override
    public String getZdmcByZdlxAndDm(String zdlx, String dm) {
        DchyXmglZd dchyXmglZd = getDchyXmglByZdlxAndDm(zdlx, dm);
        if (null != dchyXmglZd) {
            return dchyXmglZd.getMc();
        }
        return "";
    }
}
