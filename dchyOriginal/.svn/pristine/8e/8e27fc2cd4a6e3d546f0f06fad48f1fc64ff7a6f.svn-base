package cn.gtmap.msurveyplat.serviceol.core.service.mq.service.Impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxm;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmChdwxx;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.JsdwPjService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/14
 * @description 建设单位评价
 */
@Service
public class JsdwPjServiceImpl implements JsdwPjService {

    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapperXSBF;

    @Override
    public DchyXmglChxmDto getAllData(String chxmid) {
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        if (StringUtils.isNoneBlank(chxmid)) {
            DchyXmglChxm dchyXmglChxm = entityMapperXSBF.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            dchyXmglChxmDto.setDchyXmglChxm(dchyXmglChxm);

            Example exampleChxmxx = new Example(DchyXmglChxmChdwxx.class);
            exampleChxmxx.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList = entityMapperXSBF.selectByExample(exampleChxmxx);
            dchyXmglChxmDto.setDchyXmglChxmChdwxxList(dchyXmglChxmChdwxxList);
        }
        return dchyXmglChxmDto;
    }
}
