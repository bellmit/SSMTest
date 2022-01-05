package cn.gtmap.msurveyplat.server.service.sjcl.impl;

import cn.gtmap.msurveyplat.common.domain.DchyCgglSjclDO;
import cn.gtmap.msurveyplat.common.domain.DchyCgglSjxxDO;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.dto.InitDataResultDTO;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglSjclService;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglSjxxService;
import cn.gtmap.msurveyplat.server.service.sjcl.InitDchyCgglSjclAbstractService;
import cn.gtmap.msurveyplat.server.util.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/25
 * @description 收件材料信息
 */
@Service
public class InitDchyCgglSjclAbstractServiceImpl extends InitDchyCgglSjclAbstractService {

    @Autowired
    DchyCgglSjclService dchyCgglSjclService;

    @Override
    public String getVal() {
        return Constants.DEFAULT_UPPERCASE;
    }

    @Override
    public InitDataResultDTO init(InitDataParamDTO initDataParamDTO, InitDataResultDTO initDataResultDTO) throws Exception {
        if(initDataResultDTO == null){
            initDataResultDTO = new InitDataResultDTO();
        }
        List<DchyCgglSjclDO> dchyCgglSjclDOList = initDchyCgglSjcl(initDataParamDTO,initDataResultDTO);
        initDataResultDTO.setDchyCgglSjclDOList(dchyCgglSjclDOList);
        return initDataResultDTO;
    }


    @Override
    public List<DchyCgglSjclDO> initDchyCgglSjcl(InitDataParamDTO initDataParamDTO, InitDataResultDTO initDataResultDTO) {
        // 根据流程定义id  获取收件材料
        List<DchyCgglSjclDO> dchyCgglSjclDOList = dchyCgglSjclService.getDchyCgglSjclDOListByGzldyid(initDataParamDTO.getGzldyid());
        DchyCgglSjxxDO dchyCgglSjxxDO = initDataResultDTO.getDchyCgglSjxxDO();
        if (CollectionUtils.isNotEmpty(dchyCgglSjclDOList))  {
            for (DchyCgglSjclDO dchyCgglSjclDO : dchyCgglSjclDOList) {
                dchyCgglSjclDO.setSjclid(UUIDGenerator.generate18());
                dchyCgglSjclDO.setSjxxid(dchyCgglSjxxDO.getSjxxid());
            }
        }
        return dchyCgglSjclDOList;
    }
}
