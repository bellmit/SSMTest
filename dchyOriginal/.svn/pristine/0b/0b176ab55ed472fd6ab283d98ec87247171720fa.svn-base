package cn.gtmap.msurveyplat.serviceol.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglDbrw;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYbrw;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.service.ShxxcxService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShxxcxServiceImpl implements ShxxcxService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    /**
     * @param sqxxid
     * @return java.util.List<java.util.Map>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param:
     * @time 2020/12/8 16:04
     * @description 通过sqxxid获取审核信息列表
     */
    @Override
    public List<Map<String, Object>> getShxxlbListBySqxxid(String sqxxid) {
        List<Map<String, Object>> bljlList = Lists.newArrayList();
        if (StringUtils.isNotBlank(sqxxid)) {
            Map<String, Object> rwlbMap = null;

            DchyXmglZd dchyXmglZd;
            // 获取已办任务
            Example dchyXmglYbrwExample = new Example(DchyXmglYbrw.class);
            dchyXmglYbrwExample.createCriteria().andEqualTo("sqxxid", sqxxid);
            dchyXmglYbrwExample.setOrderByClause("zrsj");
            List<DchyXmglYbrw> ychyXmglYbrwList = entityMapper.selectByExample(dchyXmglYbrwExample);
            if (CollectionUtils.isNotEmpty(ychyXmglYbrwList)) {
                for (DchyXmglYbrw dchyXmglYbrw : ychyXmglYbrwList) {
                    rwlbMap = Maps.newHashMap();
                    dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_ZDLX_JDMC, dchyXmglYbrw.getDqjd());
                    if (null != dchyXmglZd) {
                        rwlbMap.put("DQJD", dchyXmglZd.getMc());
                    } else {
                        rwlbMap.put("DQJD", StringUtils.EMPTY);
                    }
                    rwlbMap.put("BLRYMC", UserUtil.getUserNameById(dchyXmglYbrw.getBlryid()));
                    rwlbMap.put("SPSJ", dchyXmglYbrw.getJssj());
                    rwlbMap.put("SPZT", "已处理");
                    rwlbMap.put("BLJG", StringUtils.equals(dchyXmglYbrw.getBljg(), Constants.VALID) ? Constants.TG_MC : Constants.TH_MC);
                    rwlbMap.put("BLYJ", dchyXmglYbrw.getBlyj());
                    bljlList.add(rwlbMap);
                }
            }

            // 获取待办任务
            Example dchyXmglDbrwExample = new Example(DchyXmglDbrw.class);
            dchyXmglDbrwExample.createCriteria().andEqualTo("sqxxid", sqxxid);
            dchyXmglDbrwExample.setOrderByClause("zrsj");
            List<DchyXmglDbrw> dchyXmglDbrwList = entityMapper.selectByExampleNotNull(dchyXmglDbrwExample);
            if (CollectionUtils.isNotEmpty(dchyXmglDbrwList)) {
                for (DchyXmglDbrw dchyXmglDbrw : dchyXmglDbrwList) {
                    rwlbMap = Maps.newHashMap();
                    dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_ZDLX_JDMC, dchyXmglDbrw.getDqjd());
                    if (null != dchyXmglZd) {
                        rwlbMap.put("DQJD", dchyXmglZd.getMc());
                    } else {
                        rwlbMap.put("DQJD", StringUtils.EMPTY);
                    }
                    rwlbMap.put("BLRYMC", UserUtil.getUserNameById(dchyXmglDbrw.getBlryid()));
                    rwlbMap.put("SPSJ", "");
                    rwlbMap.put("SPZT", StringUtils.isNotBlank(dchyXmglDbrw.getBlryid()) ? "处理中" : "待处理");
                    rwlbMap.put("BLJG", "");
                    rwlbMap.put("BLYJ", "");
                    bljlList.add(rwlbMap);
                }
            }

        }
        return bljlList;
    }
}
