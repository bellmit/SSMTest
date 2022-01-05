package cn.gtmap.msurveyplat.server.service.ywxx.impl;

import cn.gtmap.msurveyplat.common.domain.DchyCgglSjclDO;
import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.dto.InitDataResultDTO;
import cn.gtmap.msurveyplat.server.config.InitBeanConfig;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglShxxService;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglSjclService;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglSjxxService;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglXmService;
import cn.gtmap.msurveyplat.server.service.InitDataService;
import cn.gtmap.msurveyplat.server.service.ywxx.InitDataDealService;
import cn.gtmap.msurveyplat.server.service.ywxx.QualityCheckService;
import cn.gtmap.msurveyplat.server.util.Constants;
import cn.gtmap.msurveyplat.server.util.ExchangeFeignUtil;
import cn.gtmap.msurveyplat.server.util.SpringContextUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/4
 * @description
 */
@Service
public class InitDataDealServiceImpl implements InitDataDealService {
    @Autowired
    private InitBeanConfig initBeanConfig;
    @Autowired
    private DchyCgglXmService dchyCgglXmService;
    @Autowired
    private DchyCgglSjxxService dchyCgglSjxxService;
    @Autowired
    private DchyCgglShxxService dchyCgglShxxService;
    @Autowired
    private ExchangeFeignUtil exchangeFeignUtil;
    @Autowired
    private QualityCheckService qualityCheckService;
    @Autowired
    private DchyCgglSjclService dchyCgglSjclService;


    @Override
    public InitDataResultDTO init(InitDataParamDTO initDataParamDTO, boolean sfrk) throws Exception {
        InitDataResultDTO initResultDTO = new InitDataResultDTO();
        //初始化服务类
        List<Class> initServiceClassList = initBeanConfig.getInitServices();
        //初始化加载类循环
        for (Class initServiceClass : initServiceClassList) {
            //对应实现循环处理
            InitDataService initDataService = getTrafficMode(Constants.DEFAULT_UPPERCASE, initServiceClass);
            initDataService.init(initDataParamDTO,initResultDTO);
        }
        if(sfrk){
            //插入数据
            dealResultDTO(initResultDTO);
        }
       return initResultDTO;
    }

    @Override
    @Transactional
    public Boolean dealResultDTO(InitDataResultDTO initDataResultDTO) throws Exception {
        String slbh = "";
        if(initDataResultDTO.getDchyCgglXmDO() != null) {
            slbh = initDataResultDTO.getDchyCgglXmDO().getSlbh();
            dchyCgglXmService.saveDchyCgglXm(initDataResultDTO.getDchyCgglXmDO());
        }
        if(initDataResultDTO.getDchyCgglSjxxDO() != null) {
            dchyCgglSjxxService.saveDchyCgglSjxx(initDataResultDTO.getDchyCgglSjxxDO());
        }
        if(initDataResultDTO.getDchyCgglShxxDO() != null) {
            dchyCgglShxxService.saveDchyCgglShxx(initDataResultDTO.getDchyCgglShxxDO());
        }
        List<DchyCgglSjclDO> dchyCgglSjclDList = initDataResultDTO.getDchyCgglSjclDOList();
        if (CollectionUtils.isNotEmpty(dchyCgglSjclDList)) {
            for (DchyCgglSjclDO dchyCgglSjclDO: dchyCgglSjclDList) {
                dchyCgglSjclService.saveOrUpdateDchyCgglSjcl(dchyCgglSjclDO);
            }
        }
        //创建文件中心节点
        //exchangeFeignUtil.creatXmNode(slbh);
        return true;
    }

    private InitDataService getTrafficMode(String val, Class clazz) {
       Map<String, InitDataService> map = SpringContextUtil.getApplicationContext().getBeansOfType(clazz);
       if(MapUtils.isNotEmpty(map)){
           for(InitDataService initDataService:map.values()){
               if(StringUtils.isNotBlank(val)&&StringUtils.equals(val,initDataService.getVal())){
                   return initDataService;
               }
           }
       }
       return null;
   }

    @Override
    @Transactional
    public void delYwxx(String xmid) throws Exception {
        if (StringUtils.isNotBlank(xmid)) {
            DchyCgglXmDO dchyCgglXmDO = dchyCgglXmService.getDchyCgglXmDOByXmid(xmid);
            dchyCgglXmService.deleteDchyCgglSqrDOByXmid(xmid);
            dchyCgglXmService.deleteDchyCgglXmDOByXmid(xmid);
            dchyCgglSjxxService.deleteDchyCgglSjxx(xmid);
            dchyCgglShxxService.deleteDchyCgglShxxByXmid(xmid);
            qualityCheckService.deletDchyCgglZjqtcwDOByXmid(xmid);
            exchangeFeignUtil.deleteXmNode(dchyCgglXmDO.getSlbh());
        }
    }
}
