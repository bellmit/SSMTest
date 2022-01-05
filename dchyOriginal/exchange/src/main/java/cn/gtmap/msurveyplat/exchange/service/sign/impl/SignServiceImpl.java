package cn.gtmap.msurveyplat.exchange.service.sign.impl;


import cn.gtmap.msurveyplat.common.dto.ShxxParamDTO;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;
import cn.gtmap.msurveyplat.exchange.service.sign.SignService;
import cn.gtmap.msurveyplat.exchange.util.PlatformUtil;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysSignService;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfActivityVo;
import com.gtis.plat.vo.PfSignVo;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description 签名接口
 */
@Service
public class SignServiceImpl implements SignService {
    @Autowired
    private SysSignService sysSignService;
    @Autowired
    private PlatformUtil platformUtil;
    @Autowired
    private SysUserService sysUserService;

    private static final String SIGN_IMAGE_URL = AppConfig.getPlatFormUrl() + "/tag/signtag!image.action?signVo.signId=";

    @Override
    public List<ShxxVO> getShxxVOList(ShxxParamDTO shxxParamDTO) {
        List<ShxxVO> shxxVOList = new ArrayList<>();
        if(shxxParamDTO == null||StringUtils.isBlank(shxxParamDTO.getXmid())) {
            return shxxVOList;
        }
        List<PfSignVo> pfSignVoList = null;
        if(shxxParamDTO.getSfzfhdqjdxx()) {
            pfSignVoList = sysSignService.getSignList(shxxParamDTO.getDqjdmc(), shxxParamDTO.getXmid());
        }else{
            pfSignVoList = sysSignService.getSignList("", shxxParamDTO.getXmid());
        }
        if(CollectionUtils.isEmpty(pfSignVoList)) {
            return shxxVOList;
        }
        PfActivityVo pfActivityVo = platformUtil.getPfActivityByTaskId(shxxParamDTO.getTaskid());
        for(PfSignVo pfSignVo:pfSignVoList) {
            ShxxVO shxxVO = new ShxxVO();
            if(pfActivityVo != null) {
                shxxVO.setJdmc(pfActivityVo.getActivityName());
                shxxVO.setJdid(pfActivityVo.getActivityId());
            }
            shxxVO.setQmid(pfSignVo.getSignId());
            shxxVO.setQmsj(CalendarUtil.formatDateToString(pfSignVo.getSignDate()));
            shxxVO.setQmtpdz(AppConfig.getProperty("sign.image.address") ==null ? SIGN_IMAGE_URL: AppConfig.getProperty("sign.image.address") + pfSignVo.getSignId());
            shxxVO.setShxxid(pfSignVo.getSignId());
            shxxVO.setShryxm(pfSignVo.getSignName());
            shxxVO.setShyj(pfSignVo.getSignOpinion());
            shxxVOList.add(shxxVO);
        }
        return shxxVOList;
    }

    @Override
    public ShxxVO updateShxx(ShxxParamDTO shxxParamDTO) {
        ShxxVO shxxVO = new ShxxVO();
        PfSignVo pfSignVo = new PfSignVo();
        pfSignVo.setSignDate(CalendarUtil.getCurHMSDate());
        pfSignVo.setSignId(shxxParamDTO.getShxxid());
        pfSignVo.setProId(shxxParamDTO.getXmid());
        pfSignVo.setUserId(shxxParamDTO.getUserid());
        pfSignVo.setSignOpinion(shxxParamDTO.getShyj());
        PfUserVo pfUserVo = sysUserService.getUserVo(shxxParamDTO.getUserid());
        if(pfUserVo != null) {
            pfSignVo.setSignName(pfUserVo.getUserName());
        }
        pfSignVo.setSignType("1");
        pfSignVo.setSignKey(shxxParamDTO.getSignKey());

        synchronized (this) {
            PfSignVo pfSignVoTemp = sysSignService.getSign(shxxParamDTO.getShxxid());
            if(pfSignVoTemp == null) {
                sysSignService.insertAutoSign(pfSignVo);
            }else{
                sysSignService.updateAutoSign(pfSignVo);
            }
        }

        PfActivityVo pfActivityVo = platformUtil.getPfActivityByTaskId(shxxParamDTO.getTaskid());
        if(pfActivityVo != null) {
            shxxVO.setJdmc(pfActivityVo.getActivityName());
            shxxVO.setJdid(pfActivityVo.getActivityId());
        }
        shxxVO.setQmid(pfSignVo.getSignId());
        shxxVO.setQmsj(CalendarUtil.formatDateToString(pfSignVo.getSignDate()));
        shxxVO.setQmtpdz(AppConfig.getProperty("sign.image.address") + pfSignVo.getSignId());
        shxxVO.setShxxid(pfSignVo.getSignId());
        shxxVO.setShryxm(pfSignVo.getSignName());
        shxxVO.setShyj(pfSignVo.getSignOpinion());
        return shxxVO;
    }

    @Override
    public void deleteShxxSign(String shxxid) {
        sysSignService.deleteSign(shxxid);
    }
}
