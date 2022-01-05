package cn.gtmap.msurveyplat.server.service.sjxx.impl;

import cn.gtmap.msurveyplat.common.domain.DchyCgglSjxxDO;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.dto.InitDataResultDTO;
import cn.gtmap.msurveyplat.server.service.sjxx.InitDchyCgglSjxxAbstractService;
import cn.gtmap.msurveyplat.server.util.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/3
 * @description
 */
@Service
public class InitDchyCgglSjxxServiceImpl extends InitDchyCgglSjxxAbstractService {

    @Override
    public String getVal() {
        return Constants.DEFAULT_UPPERCASE;
    }

    @Override
    public InitDataResultDTO init(InitDataParamDTO initDataParamDTO, InitDataResultDTO initDataResultDTO) {
        if(initDataResultDTO == null){
            initDataResultDTO = new InitDataResultDTO();
        }
        DchyCgglSjxxDO dchyCgglSjxxDO = initDchyCgglSjxx(initDataParamDTO,initDataResultDTO);
        initDataResultDTO.setDchyCgglSjxxDO(dchyCgglSjxxDO);
        return initDataResultDTO;
    }


    @Override
    public DchyCgglSjxxDO initDchyCgglSjxx(InitDataParamDTO initDataParamDTO, InitDataResultDTO initDataResultDTO) {
        if(initDataResultDTO.getDchyCgglSjxxDO() != null) {
            return initDataResultDTO.getDchyCgglSjxxDO();
        }
        DchyCgglSjxxDO dchyCgglSjxxDO = new DchyCgglSjxxDO();
        dchyCgglSjxxDO.setXmid(initDataResultDTO.getDchyCgglXmDO().getXmid());
        if(StringUtils.isBlank(dchyCgglSjxxDO.getXmid())) {
            dchyCgglSjxxDO.setXmid(UUIDGenerator.generate18());
        }
        if(StringUtils.isBlank(dchyCgglSjxxDO.getSjxxid())) {
            dchyCgglSjxxDO.setSjxxid(UUIDGenerator.generate18());
        }
        return dchyCgglSjxxDO;
    }
}

