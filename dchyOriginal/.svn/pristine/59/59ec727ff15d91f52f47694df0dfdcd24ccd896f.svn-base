package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmChdwxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglKp;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglKpService;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/8 15:30
 * @description
 */
@Service
public class DchyXmglKpServiceImpl implements DchyXmglKpService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    @Override
    public int getKpResultByMlkId(String mlkid) {
        HashMap<String, Object> map = Maps.newHashMap();
        /*获取最新的考评结果*/
        Example kpExample = new Example(DchyXmglKp.class);
        kpExample.createCriteria().andEqualTo("mlkid", mlkid).andEqualTo("sfyx", Constants.VALID);
        List<DchyXmglKp> kpList = entityMapper.selectByExample(kpExample);
        if (CollectionUtils.isNotEmpty(kpList)) {
            DchyXmglKp xmglKp = kpList.get(0);
            /*获取考评结果*/
            map.put("kpjg", dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_KPJG, xmglKp.getKpjg()).getMc());
        }
        //通过mlkid获取对应测绘单位信息
        Example chdwExample = new Example(DchyXmglChxmChdwxx.class);
        chdwExample.createCriteria().andEqualTo("mlkid", mlkid);
        List<DchyXmglChxmChdwxx> chdwList = entityMapper.selectByExample(chdwExample);
        if (CollectionUtils.isNotEmpty(chdwList)) {
            DchyXmglChxmChdwxx chdwxx = chdwList.get(0);
            /*获取测绘单位对应的服务评价分数*/
            map.put("fwpj", dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_FWPJ, Math.round(chdwxx.getFwpj()) + "").getMc());
        }
        return this.calEvaluate(map);
    }


    /**
     * 计算评价
     *
     * @param param
     * @return
     */
    public int calEvaluate(Map<String, Object> param) {
        /*考评最后得分+本年度的最后服务评价得分*/
        Integer kpjg = MapUtils.getInteger(param, "kpjg");
        Integer fwpj = MapUtils.getInteger(param, "fwpj");
        return (kpjg + fwpj) / 2;
    }
}
