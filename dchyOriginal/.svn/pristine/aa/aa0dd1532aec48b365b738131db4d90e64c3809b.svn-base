package cn.gtmap.msurveyplat.server.service.shxx.impl;


import cn.gtmap.msurveyplat.common.domain.DchyCgglGcjsspxxDo;
import cn.gtmap.msurveyplat.common.domain.DchyCgglShxxDO;
import cn.gtmap.msurveyplat.common.domain.DchyXtMryjDO;
import cn.gtmap.msurveyplat.common.dto.ShxxParamDTO;
import cn.gtmap.msurveyplat.common.util.UUID;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglGcjsspxxService;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglShxxService;
import cn.gtmap.msurveyplat.server.core.service.DchyXtMryjService;
import cn.gtmap.msurveyplat.server.service.shxx.InitShxxFormService;
import cn.gtmap.msurveyplat.server.util.ExchangeFeignUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/26
 * @description 初始化审核信息表单
 */
@Service
public class InitShxxFormServiceImpl  implements InitShxxFormService {
    @Autowired
    private ExchangeFeignUtil exchangeFeignUtil;
    @Autowired
    private DchyCgglShxxService dchyCgglShxxService;
    @Autowired
    private DchyXtMryjService dchyXtMryjService;
    @Autowired
    private DchyCgglGcjsspxxService dchyCgglGcjsspxxService;


    @Override
    public List<ShxxVO> init(ShxxParamDTO shxxParamDTO) {
        List<ShxxVO> shxxVOList = null;
        if(StringUtils.equals(shxxParamDTO.getDqjdmc(),"cs")){
            shxxParamDTO.setDqjdmc("初审");
        }else if(StringUtils.equals(shxxParamDTO.getDqjdmc(),"fs")){
            shxxParamDTO.setDqjdmc("复审");
        }else{
            shxxVOList = exchangeFeignUtil.getShxxVOList(shxxParamDTO);
            if(CollectionUtils.isNotEmpty(shxxVOList)) {
                for (ShxxVO shxxVO : shxxVOList) {
                    DchyCgglShxxDO dchyCgglShxxDO = dchyCgglShxxService.getDchyCgglShxxDOByShxxid(shxxVO.getShxxid());
                    if (dchyCgglShxxDO != null) {
                        shxxVO.setJdmc(dchyCgglShxxDO.getGzljdmc());
                    }
                }
            }
            return shxxVOList;
        }

        List<DchyCgglShxxDO> dchyCgglShxxDOList = null;
        if(shxxParamDTO != null) {
            dchyCgglShxxDOList = dchyCgglShxxService.getDchyCgglShxxListByXmid(shxxParamDTO.getXmid());
        }
        if(CollectionUtils.isNotEmpty(dchyCgglShxxDOList)) {
            List<String> jdmcList = new ArrayList<>();
            for(DchyCgglShxxDO dchyCgglShxxDO:dchyCgglShxxDOList) {
                jdmcList.add(dchyCgglShxxDO.getGzljdmc());
            }
            if(!jdmcList.contains(shxxParamDTO.getDqjdmc())) {
                shxxVOList = exchangeFeignUtil.getShxxVOList(shxxParamDTO);
                if(CollectionUtils.isNotEmpty(shxxVOList)) {
                    for(ShxxVO shxxVO:shxxVOList) {
                        DchyCgglShxxDO dchyCgglShxxDO = dchyCgglShxxService.getDchyCgglShxxDOByShxxid(shxxVO.getShxxid());
                        if(dchyCgglShxxDO != null) {
                            shxxVO.setJdmc(dchyCgglShxxDO.getGzljdmc());
                        }
                    }
                    shxxVOList.add(addShxxVO(shxxParamDTO));
                }else{
                    shxxVOList = new ArrayList<>();
                    shxxVOList.add(addShxxVO(shxxParamDTO));
                }
            }else{
                shxxVOList = exchangeFeignUtil.getShxxVOList(shxxParamDTO);
                if(CollectionUtils.isNotEmpty(shxxVOList)) {
                    for(ShxxVO shxxVO:shxxVOList) {
                        DchyCgglShxxDO dchyCgglShxxDO = dchyCgglShxxService.getDchyCgglShxxDOByShxxid(shxxVO.getShxxid());
                        if(dchyCgglShxxDO != null) {
                            shxxVO.setJdmc(dchyCgglShxxDO.getGzljdmc());
                        }
                    }
                    if(dchyCgglShxxDOList.size() > shxxVOList.size()) {
                        for(DchyCgglShxxDO dchyCgglShxxDO:dchyCgglShxxDOList) {
                            if(StringUtils.equals(dchyCgglShxxDO.getGzljdmc(),shxxParamDTO.getDqjdmc())) {
                                ShxxVO shxxVO = getShxxVOByDchyCgglShxxDO(dchyCgglShxxDO);
                                shxxVOList.add(shxxVO);
                            }
                        }
                    }
                }else{
                    shxxVOList = new ArrayList<>();
                    ShxxVO shxxVO = getShxxVOByDchyCgglShxxDO(dchyCgglShxxDOList.get(0));
                    shxxVOList.add(shxxVO);
                }
            }
        }else{
            shxxVOList = new ArrayList<>();
            shxxVOList.add(addShxxVO(shxxParamDTO));
        }
        return shxxVOList;
    }

    @Override
    public ShxxVO update(ShxxParamDTO shxxParamDTO) {
        if (StringUtils.isNotBlank(shxxParamDTO.getShxxid())) {
            DchyCgglShxxDO dchyCgglShxxDO = dchyCgglShxxService.getDchyCgglShxxDOByShxxid(shxxParamDTO.getShxxid());
            if (dchyCgglShxxDO == null) {
                dchyCgglShxxDO = new DchyCgglShxxDO();
                dchyCgglShxxDO.setXmid(shxxParamDTO.getXmid());
                dchyCgglShxxDO.setShxxid(shxxParamDTO.getShxxid());
                if (StringUtils.equals("cs",shxxParamDTO.getDqjdmc())) {
                    dchyCgglShxxDO.setGzljdmc("初审");
                } else {
                    dchyCgglShxxDO.setGzljdmc("复审");
                }
                dchyCgglShxxService.saveDchyCgglShxx(dchyCgglShxxDO);
            }
        }
        //exchangeFeignUtil.updateShxxVO(shxxParamDTO);
        return exchangeFeignUtil.updateShxxVO(shxxParamDTO);
    }

    @Override
    public ShxxVO deleteSign(String shxxid) {
        dchyCgglShxxService.deleteDchyCgglShxxByShxxid(shxxid);
        return exchangeFeignUtil.deleteSign(shxxid);
    }

    @Override
    public DchyCgglGcjsspxxDo getDchyCgglGcjsspxxDoByXmid(String xmid) {
        DchyCgglGcjsspxxDo  dchyCgglGcjsspxxDo = null;
        List<DchyCgglGcjsspxxDo> dchyCgglGcjsspxxDoList = dchyCgglGcjsspxxService.getDchyCgglGcjsspxxDoListByXmid(xmid);
        if(CollectionUtils.isNotEmpty(dchyCgglGcjsspxxDoList)) {
            dchyCgglGcjsspxxDo = dchyCgglGcjsspxxDoList.get(0);
        }
        if(dchyCgglGcjsspxxDo == null) {
            dchyCgglGcjsspxxDo = new DchyCgglGcjsspxxDo();
        }
        return dchyCgglGcjsspxxDo;
    }

    @Override
    public DchyCgglGcjsspxxDo saveOrUpdateDchyCgglGcjsspxxDo(ShxxParamDTO shxxParamDTO) {
        DchyCgglGcjsspxxDo  dchyCgglGcjsspxxDo = null;
        List<DchyCgglGcjsspxxDo> dchyCgglGcjsspxxDoList = dchyCgglGcjsspxxService.getDchyCgglGcjsspxxDoListByXmid(shxxParamDTO.getXmid());
        if(CollectionUtils.isNotEmpty(dchyCgglGcjsspxxDoList)) {
            dchyCgglGcjsspxxDo = dchyCgglGcjsspxxDoList.get(0);
        }
        if(dchyCgglGcjsspxxDo == null) {
            dchyCgglGcjsspxxDo = new DchyCgglGcjsspxxDo();
            dchyCgglGcjsspxxDo.setSpxxid(UUIDGenerator.generate18());
            dchyCgglGcjsspxxDo.setXmid(shxxParamDTO.getXmid());
        }
        dchyCgglGcjsspxxDo.setSpbm(shxxParamDTO.getShr());
        dchyCgglGcjsspxxDo.setSpr(shxxParamDTO.getShr());
        dchyCgglGcjsspxxDo.setSpsj(shxxParamDTO.getShsj());
        dchyCgglGcjsspxxDo.setSpyj(shxxParamDTO.getShyj());
        dchyCgglGcjsspxxService.saveDchyCgglGcjsspxx(dchyCgglGcjsspxxDo);
        return dchyCgglGcjsspxxDo;
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param shxxParamDTO 初始化审核信息参数对象
     * @return
     * @description 新增一条审核信息
     */
    private ShxxVO addShxxVO(ShxxParamDTO shxxParamDTO){
        ShxxVO shxxVO = new ShxxVO();
        //获取默认的签名意见
        String mryj = "";
        List<DchyXtMryjDO> dchyXtMryjDOList = dchyXtMryjService.getDchyXtMryjDOListByXmidAndGzljdmc(shxxParamDTO.getXmid(),shxxParamDTO.getDqjdmc());
        if(CollectionUtils.isNotEmpty(dchyXtMryjDOList)) {
            mryj = dchyXtMryjDOList.get(0).getMryj();
        }
        DchyCgglShxxDO dchyCgglShxxDO = new DchyCgglShxxDO();
        dchyCgglShxxDO.setXmid(shxxParamDTO.getXmid());
        dchyCgglShxxDO.setShxxid(UUID.hex32());
        dchyCgglShxxDO.setGzljdmc(shxxParamDTO.getDqjdmc());
        dchyCgglShxxDO.setShrid(shxxParamDTO.getUserid());
        //dchyCgglShxxDO.setShr();
        dchyCgglShxxDO.setShyj(mryj);
        dchyCgglShxxService.saveDchyCgglShxx(dchyCgglShxxDO);
        shxxVO.setShyj(mryj);
        shxxVO.setShxxid(dchyCgglShxxDO.getShxxid());
        shxxVO.setJdmc(shxxParamDTO.getDqjdmc());
        return shxxVO;
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param dchyCgglShxxDO 多测合一成果管理审核信息对象
     * @return
     * @description 获取审核信息
     */
    private ShxxVO getShxxVOByDchyCgglShxxDO(DchyCgglShxxDO dchyCgglShxxDO){
        ShxxVO shxxVO = new ShxxVO();
        if(dchyCgglShxxDO != null) {
            //获取默认的签名意见
            shxxVO.setShyj(dchyCgglShxxDO.getShyj());
            shxxVO.setShxxid(dchyCgglShxxDO.getShxxid());
            shxxVO.setJdmc(dchyCgglShxxDO.getGzljdmc());
        }
        return shxxVO;
    }
}
