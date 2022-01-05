package cn.gtmap.msurveyplat.server.service.ywxx.impl;

import cn.gtmap.msurveyplat.common.domain.*;
import cn.gtmap.msurveyplat.common.dto.InitDataResultDTO;
import cn.gtmap.msurveyplat.common.dto.UploadParamDTO;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.server.config.DjxlGxConfig;
import cn.gtmap.msurveyplat.server.core.service.*;
import cn.gtmap.msurveyplat.server.service.ywxx.AlterClsxztService;
import cn.gtmap.msurveyplat.server.service.ywxx.SldxxService;
import cn.gtmap.msurveyplat.server.util.Constants;
import cn.gtmap.msurveyplat.server.util.ExchangeFeignUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SldxxServiceImpl implements SldxxService {

    @Autowired
    private DchyCgglXmService dchyCgglXmService;
    @Autowired
    private DchyCgglSqrService dchyCgglSqrService;
    @Autowired
    private DchyCgglSjclService dchyCgglSjclService;
    @Autowired
    private ExchangeFeignUtil exchangeFeignUtil;
    @Autowired
    private DchyZdService dchyZdService;
    @Autowired
    private DchyCgglSjxxService dchyCgglSjxxService;
    @Autowired
    private DjxlGxConfig djxlGxConfig;

    @Autowired
    private AlterClsxztService alterClsxztService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SldxxServiceImpl.class);

    private  static  final  String alertclxszt =  AppConfig.getProperty("alertclxszt.url");

    @Override
    public InitDataResultDTO getSldxx(String xmid) {
        InitDataResultDTO initDataResultDTO =  new InitDataResultDTO();
        DchyCgglXmDO dchyCgglXmDO = dchyCgglXmService.getDchyCgglXmDOByXmid(xmid);
        if(dchyCgglXmDO != null) {
            if(StringUtils.isNotBlank(dchyCgglXmDO.getYbbdcdydm())) {
                String newYbdcdydm = dchyCgglXmDO.getYbbdcdydm().substring(0, 6) + " " + dchyCgglXmDO.getYbbdcdydm().substring(6, 12) + " " + dchyCgglXmDO.getYbbdcdydm().substring(12, 19) + " " + dchyCgglXmDO.getYbbdcdydm().substring(19);
                dchyCgglXmDO.setYbbdcdydm(newYbdcdydm);
            }
            initDataResultDTO.setDchyCgglXmDO(dchyCgglXmDO);
        }
        List<DchyCgglSqrDO> dchyCgglSqrDOList = dchyCgglSqrService.getDchyCgglSqrDOListByXmid(xmid);
        if(CollectionUtils.isNotEmpty(dchyCgglSqrDOList)) {
            initDataResultDTO.setDchyCgglSqrDOList(dchyCgglSqrDOList);
        }
        List<DchyCgglSjclDO> dchyCgglSjclDOList = dchyCgglSjclService.getDchyCgglSjclDOListByXmid(xmid);
        initDataResultDTO.setDchyCgglSjclDOList(dchyCgglSjclDOList);
        List<DchyZdSjlxDO> dchyZdSjlxDOList = dchyZdService.getDchyZdSjlxDOList();
        initDataResultDTO.setDchyZdSjlxDOList(dchyZdSjlxDOList);
        List<DchyZdChdwDo> dchyZdChdwDoList =  dchyCgglXmService.getChdw();
        initDataResultDTO.setDchyZdChdwDo(dchyZdChdwDoList);
        return initDataResultDTO;
    }

    @Override
    public void saveSldxx(InitDataResultDTO initDataResultDTO) {
        if(initDataResultDTO != null) {
            String gzlslid = "";
            String remark = "";
            if(initDataResultDTO.getDchyCgglXmDO() != null) {
                if (StringUtils.isNotBlank(initDataResultDTO.getDchyCgglXmDO().getYbbdcdydm())) {
                    String newYbdcdydm = StringUtils.deleteWhitespace(initDataResultDTO.getDchyCgglXmDO().getYbbdcdydm());
                    initDataResultDTO.getDchyCgglXmDO().setYbbdcdydm(newYbdcdydm);
                }
                dchyCgglXmService.saveDchyCgglXm(initDataResultDTO.getDchyCgglXmDO());
                List<DchyZdChdwDo> dchyZdChdwDoList = initDataResultDTO.getDchyZdChdwDo();
                String chdw = initDataResultDTO.getDchyCgglXmDO().getChdw();
                for (DchyZdChdwDo dchyZdChdwDo : dchyZdChdwDoList) {
                    if (StringUtils.equals(dchyZdChdwDo.getDm(),initDataResultDTO.getDchyCgglXmDO().getChdw())) {
                        chdw = dchyZdChdwDo.getMc();
                        break;
                    }
                }
                gzlslid = initDataResultDTO.getDchyCgglXmDO().getGzlslid();
                remark += initDataResultDTO.getDchyCgglXmDO().getSlbh() + Constants.SPLIT_STR;
                remark += initDataResultDTO.getDchyCgglXmDO().getJsdw() + Constants.SPLIT_STR;
                remark += chdw + Constants.SPLIT_STR;
                remark += initDataResultDTO.getDchyCgglXmDO().getXmdz() + Constants.SPLIT_STR;
                remark += initDataResultDTO.getDchyCgglXmDO().getSlr();
                ActStProRelDo actStProRelDo = new ActStProRelDo();
                actStProRelDo.setProcInsId(gzlslid);
                actStProRelDo.setText1(initDataResultDTO.getDchyCgglXmDO().getSlbh());
                actStProRelDo.setText2(initDataResultDTO.getDchyCgglXmDO().getJsdw());
                actStProRelDo.setText3(chdw);
                actStProRelDo.setText4(initDataResultDTO.getDchyCgglXmDO().getXmdz());
                actStProRelDo.setText5(initDataResultDTO.getDchyCgglXmDO().getSlr());
                exchangeFeignUtil.saveOrUpdateTaskExtendDto(actStProRelDo);
            }
            if(CollectionUtils.isNotEmpty(initDataResultDTO.getDchyCgglSqrDOList())) {
               for(DchyCgglSqrDO dchyCgglSqrDO:initDataResultDTO.getDchyCgglSqrDOList()) {
                   dchyCgglSqrService.saveDchyCgglSqr(dchyCgglSqrDO);
               }
            }

            List<DchyCgglSjclDO> dchyCgglSjclDOList = initDataResultDTO.getDchyCgglSjclDOList();
            if (CollectionUtils.isNotEmpty(dchyCgglSjclDOList)) {
                for (DchyCgglSjclDO dchyCgglSjclDO : dchyCgglSjclDOList) {
                    dchyCgglSjclService.saveOrUpdateDchyCgglSjcl(dchyCgglSjclDO);
                }
            }
            //更新rmark字段
            exchangeFeignUtil.updateRemark(gzlslid,remark);
            //调用接口
            excuteAlertClxszt(initDataResultDTO);
        }
    }

    /**
     * @author: <a href="mailto:wangyang@gtmap.cn">wangyang</a>
     * @description 接口调用
     */
    public  void   excuteAlertClxszt( InitDataResultDTO initDataResultDTO){
         Map head = new HashMap();
         head.put("code","");
         head.put("msg","");
         Map data = new HashMap();
         data.put("chgcbh",initDataResultDTO.getDchyCgglXmDO().getChxmbh());
         data.put("clsx",initDataResultDTO.getGzldyid());
         data.put("cgtjzt","1");
         LOGGER.info("调用多测合一网上办事大厅修改测量事项状态接口开始");
         alterClsxztService.alterClsxzt(head,data,alertclxszt);
         LOGGER.info("调用多测合一网上办事大厅修改测量事项状态接口结束");
     }

    @Override
    public String saveSjcl(DchyCgglSjclDO dchyCgglSjclDO, String xmid) {
        String sjxxid = null;
        if (dchyCgglSjclDO != null) {
            if (StringUtils.isBlank(dchyCgglSjclDO.getSjclid())) {
                sjxxid = UUIDGenerator.generate18();
                dchyCgglSjclDO.setSjclid(sjxxid);
                List<DchyCgglSjxxDO> dchyCgglSjxxDOList = dchyCgglSjxxService.getDchyCgglSjxxDOListByXmid(xmid);
                if (CollectionUtils.isNotEmpty(dchyCgglSjxxDOList)) {
                    dchyCgglSjclDO.setSjxxid(dchyCgglSjxxDOList.get(0).getSjxxid());
                }
            }
            dchyCgglSjclService.saveOrUpdateDchyCgglSjcl(dchyCgglSjclDO);
        }
        return sjxxid;
    }

    @Override
    public void deleteSjcl(List<String> slclidList) {
        dchyCgglSjclService.deleteSjcl(slclidList);
    }

    @Override
    public UploadParamDTO getUploadParam(String slbh, String xmid, String sjclid, Boolean sfpl) {
        UploadParamDTO uploadParamDTO = null;
        DchyCgglXmDO dchyCgglXmDO = dchyCgglXmService.getDchyCgglXmDOByXmid(xmid);
        if (sfpl) {
            // 批量上传
            List<DchyCgglSjclDO> dchyCgglSjclDOList = dchyCgglSjclService.getDchyCgglSjclDOListByXmid(xmid);
            if (CollectionUtils.isNotEmpty(dchyCgglSjclDOList)) {
                for (DchyCgglSjclDO dchyCgglSjclDO : dchyCgglSjclDOList) {
                    uploadParamDTO = exchangeFeignUtil.getUploadParamDTO(slbh,dchyCgglSjclDO.getClmc());
                    dchyCgglSjclDO.setWjzxjdid(uploadParamDTO.getNodeId());
                    dchyCgglSjclService.saveOrUpdateDchyCgglSjcl(dchyCgglSjclDO);
                }
            }
            uploadParamDTO = exchangeFeignUtil.getUploadParamDTO(slbh,"undefined");
            if(uploadParamDTO != null) {
                uploadParamDTO.setSfjcwjwzx(Constants.FJSC_SFJCWJWZX_YES);
            }
        } else {
            // 单个上传
            if (StringUtils.isNotBlank(sjclid)) {
                DchyCgglSjclDO dchyCgglSjclDO = dchyCgglSjclService.getDchyCgglSjclDOById(sjclid);
                uploadParamDTO = exchangeFeignUtil.getUploadParamDTO(slbh,dchyCgglSjclDO.getClmc());
                dchyCgglSjclDO.setWjzxjdid(uploadParamDTO.getNodeId());
                dchyCgglSjclService.saveOrUpdateDchyCgglSjcl(dchyCgglSjclDO);
            }
            if(uploadParamDTO != null) {
                uploadParamDTO.setSfjcwjwzx(Constants.FJSC_SFJCWJWZX_NO);
            }
        }

        List<Map> djxlCgsjlxMapList = djxlGxConfig.getDjxlCgsjlxMapList();
        if(dchyCgglXmDO != null&&CollectionUtils.isNotEmpty(djxlCgsjlxMapList)) {
            String cgsjlx = Constants.ZJ_CGSJLX_GHCH;
            for(Map djxlCgsjlxMap:djxlCgsjlxMapList) {
                if(StringUtils.equals(dchyCgglXmDO.getDjxl(),String.valueOf(djxlCgsjlxMap.get("djxl")))){
                    cgsjlx = CommonUtil.formatEmptyValue(djxlCgsjlxMap.get("cgsjlx"));
                    break;
                }
            }
            uploadParamDTO.setChlx(cgsjlx);
        }
        return uploadParamDTO;
    }
}
