package cn.gtmap.msurveyplat.server.service.shxx.impl;

import cn.gtmap.msurveyplat.common.domain.DchyCgglShxxDO;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.dto.InitDataResultDTO;
import cn.gtmap.msurveyplat.server.service.shxx.InitDchyCgglShxxAbstractService;
import cn.gtmap.msurveyplat.server.util.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/10
 * @description
 */
@Service
public class InitDchyCgglShxxServiceImpl extends InitDchyCgglShxxAbstractService {

    @Override
    public String getVal() {
        return Constants.DEFAULT_UPPERCASE;
    }

    @Override
    public InitDataResultDTO init(InitDataParamDTO initDataParamDTO, InitDataResultDTO initDataResultDTO) {
        if(initDataResultDTO == null){
            initDataResultDTO = new InitDataResultDTO();
        }
        DchyCgglShxxDO dchyCgglShxxDO = initDchyCgglShxx(initDataParamDTO,initDataResultDTO);
        initDataResultDTO.setDchyCgglShxxDO(dchyCgglShxxDO);
        return initDataResultDTO;
    }


    @Override
    public DchyCgglShxxDO initDchyCgglShxx(InitDataParamDTO initDataParamDTO, InitDataResultDTO initDataResultDTO) {
        if(initDataResultDTO.getDchyCgglShxxDO() != null) {
            return initDataResultDTO.getDchyCgglShxxDO();
        }
        DchyCgglShxxDO dchyCgglShxxDO = new DchyCgglShxxDO();
        dchyCgglShxxDO.setXmid(initDataResultDTO.getDchyCgglXmDO().getXmid());
        if(StringUtils.isBlank(dchyCgglShxxDO.getXmid())) {
            dchyCgglShxxDO.setXmid(UUIDGenerator.generate18());
        }
        if(StringUtils.isBlank(dchyCgglShxxDO.getShxxid())) {
            dchyCgglShxxDO.setShxxid(UUIDGenerator.generate18());
        }
        return dchyCgglShxxDO;
    }
}

