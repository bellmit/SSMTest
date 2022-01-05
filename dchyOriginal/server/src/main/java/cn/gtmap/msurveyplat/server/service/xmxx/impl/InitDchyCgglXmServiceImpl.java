package cn.gtmap.msurveyplat.server.service.xmxx.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.domain.DchyGzlDjxlGxDo;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.dto.InitDataResultDTO;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.server.service.xmxx.InitDchyCgglXmAbstractService;
import cn.gtmap.msurveyplat.server.util.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/3
 * @description
 */
@Service
public class InitDchyCgglXmServiceImpl extends InitDchyCgglXmAbstractService {

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public String getVal() {
        return Constants.DEFAULT_UPPERCASE;
    }

    @Override
    public InitDataResultDTO init(InitDataParamDTO initDataParamDTO, InitDataResultDTO initDataResultDTO) throws Exception {
        if(initDataResultDTO == null) {
            initDataResultDTO = new InitDataResultDTO();
        }
        DchyCgglXmDO dchyCgglXmDO = initDchyCgglXm(initDataParamDTO,initDataResultDTO);
        initDataResultDTO.setDchyCgglXmDO(dchyCgglXmDO);
        return initDataResultDTO;
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param initDataParamDTO 初始化数据参数对象
     * @param initDataResultDTO 初始化数据返回数据对象
     * @return 成果管理系统项目信息
     * @description 初始化成果管理系统项目信息
     */
    @Override
    public DchyCgglXmDO initDchyCgglXm(InitDataParamDTO initDataParamDTO, InitDataResultDTO initDataResultDTO) {
        if(initDataResultDTO.getDchyCgglXmDO() != null) {
            return initDataResultDTO.getDchyCgglXmDO();
        }
        DchyCgglXmDO dchyCgglXmDO = new DchyCgglXmDO();
        dchyCgglXmDO.setGzlslid(initDataParamDTO.getGzlslid());
        if(StringUtils.isBlank(dchyCgglXmDO.getXmid())) {
            dchyCgglXmDO.setXmid(UUIDGenerator.generate18());
        }
        if(StringUtils.isBlank(dchyCgglXmDO.getSlbh())) {
            dchyCgglXmDO.setSlbh(CalendarUtil.getTimeMs());
        }
        if(dchyCgglXmDO.getSlsj() == null) {
            dchyCgglXmDO.setSlsj(CalendarUtil.getCurHMSDate());
        }
        if(StringUtils.isBlank(dchyCgglXmDO.getSlr())) {
            dchyCgglXmDO.setSlr(initDataParamDTO.getUserName());
        }
        if(StringUtils.isBlank(dchyCgglXmDO.getSlrid())) {
            dchyCgglXmDO.setSlrid(initDataParamDTO.getUserId());
        }

        // 根据工作流id获取登记小类
        if (StringUtils.isNotBlank(initDataParamDTO.getGzldyid())) {
            Example example = new Example(DchyGzlDjxlGxDo.class);
            example.createCriteria().andEqualTo("gzldyid",initDataParamDTO.getGzldyid());
            List<DchyGzlDjxlGxDo> dchyGzlDjxlGxDoList =  entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(dchyGzlDjxlGxDoList)) {
                dchyCgglXmDO.setDjxl(dchyGzlDjxlGxDoList.get(0).getDjxl());
            }
        }


        return dchyCgglXmDO;
    }
}

