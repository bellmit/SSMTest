package cn.gtmap.msurveyplat.serviceol.core.service.mq.service.Impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglKp;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.dto.DchyXmglMlkDto;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.KpxxService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/14
 * @description 管理单位考评消息
 */
@Service
public class KpxxServiceImpl implements KpxxService {

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public DchyXmglMlkDto getAllData(String mlkid) {
        DchyXmglMlkDto kpxxModel = new DchyXmglMlkDto();

        if (StringUtils.isNoneBlank(mlkid)) {
            Example exampleKp = new Example(DchyXmglKp.class);
            exampleKp.createCriteria().andEqualTo("mlkid", mlkid);
            List<DchyXmglKp> dchyXmglKpList = entityMapper.selectByExample(exampleKp);
            kpxxModel.setDchyXmglKpList(dchyXmglKpList);

            List<DchyXmglMlk> dchyXmglMlkList = new ArrayList<>();
            DchyXmglMlk dchyXmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
            dchyXmglMlkList.add(dchyXmglMlk);
            kpxxModel.setDchyXmglMlkList(dchyXmglMlkList);
        }
        return kpxxModel;
    }
}
