package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglSqxxService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DchyXmglSqxxServiceImpl implements DchyXmglSqxxService {

    @Autowired
    private EntityMapper entityMapper;

    /**
     * @return cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: initDataParamDTO
     * @time 2021/1/19 11:27
     * @description
     */
    @Override
    public DchyXmglSqxx initSqxx(InitDataParamDTO initDataParamDTO) {
        DchyXmglSqxx sqxx = new DchyXmglSqxx();
        sqxx.setSqxxid(initDataParamDTO.getXmid());
        sqxx.setSqbh(initDataParamDTO.getSlbh());
        sqxx.setBlsx("");
        sqxx.setSqr(initDataParamDTO.getUserId());
        sqxx.setSqrmc(initDataParamDTO.getUserName());
        sqxx.setSqsj(new Date());
        sqxx.setSqzt(Constants.DCHY_XMGL_SQZT_DSH);
        entityMapper.saveOrUpdate(sqxx, sqxx.getSqxxid());
        return sqxx;
    }
}
