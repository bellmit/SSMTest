package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglBdbtyz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYwyzxx;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglBdbtyzService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-11-28
 * description
 */

@Service
public class DchyXmglBdbtyzServiceImpl implements DchyXmglBdbtyzService {
    @Autowired
    EntityMapper entityMapper;

    @Override
    public Map<String, Object> getBdbtyzList(Map<String, Object> paramMap) {
        Map<String, Object> resultList = Maps.newHashMap();
        String bdid = CommonUtil.formatEmptyValue(paramMap.get("bdid"));
        String ssmkid = CommonUtil.formatEmptyValue(paramMap.get("ssmkid"));

        List btxyzList = new ArrayList();
        List ywljyzList = new ArrayList();
        if (StringUtils.isNoneBlank(bdid, ssmkid)) {
            Example btyzExample = new Example(DchyXmglBdbtyz.class);
            btyzExample.createCriteria().andEqualTo("bdid", bdid).andEqualTo("ssmkid", ssmkid);
            List<DchyXmglBdbtyz> bdbtxyzList = entityMapper.selectByExample(btyzExample);

            if (CollectionUtils.isNotEmpty(bdbtxyzList)) {
                for (int i = 0; i < bdbtxyzList.size(); i++) {
                    String bdzdid = bdbtxyzList.get(i).getBdzdid();
                    btxyzList.add(bdzdid);
                }

            }

            Example ywyzExample = new Example(DchyXmglYwyzxx.class);
            ywyzExample.createCriteria().andEqualTo("bdid", bdid).andEqualTo("ssmkid", ssmkid);
            List<DchyXmglYwyzxx> ywyzxxList = entityMapper.selectByExample(ywyzExample);
            if (CollectionUtils.isNotEmpty(ywyzxxList)) {
                for (int i = 0; i < ywyzxxList.size(); i++) {
                    String ywyzljdm = ywyzxxList.get(i).getYwyzljdm();
                    ywljyzList.add(ywyzljdm);
                }
            }
        }
        resultList.put("btxyzList", btxyzList);
        resultList.put("ywljyzList", ywljyzList);
        return resultList;
    }
}
