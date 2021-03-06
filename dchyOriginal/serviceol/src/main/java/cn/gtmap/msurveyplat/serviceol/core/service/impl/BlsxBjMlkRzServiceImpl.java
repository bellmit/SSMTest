package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglDbrw;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYbrw;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglMlkMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.BlsxBjService;
import cn.gtmap.msurveyplat.serviceol.core.service.MessagePushService;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.serviceol.utils.CommonUtil;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BlsxBjMlkRzServiceImpl implements BlsxBjService {
    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    PushDataToMqService pushDataToMqService;

    @Autowired
    MessagePushService messagePushService;
    @Autowired
    private DchyXmglMlkMapper mlkMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Map<String, Object> bj(String dbrwid, Object object) {
        Map<String, Object> result = Maps.newHashMap();
        if (StringUtils.isNotBlank(dbrwid)) {
            Map<String, Object> param = (Map<String, Object>) object;
            boolean shth = StringUtils.equalsIgnoreCase(MapUtils.getString(param, "sfth"), "1");
            String shyj = MapUtils.getString(param, "shyj");
            DchyXmglDbrw dchyXmglDbrw = entityMapper.selectByPrimaryKey(DchyXmglDbrw.class, dbrwid);
            if (null != dchyXmglDbrw) {
                /*???????????????????????????*/
                DchyXmglYbrw dchyXmglYbrw = new DchyXmglYbrw();
                dchyXmglYbrw.setBlryid(UserUtil.getCurrentUserId());
                dchyXmglYbrw.setBlyj(shyj);
                dchyXmglYbrw.setJssj(new Date());
                dchyXmglYbrw.setSqxxid(dchyXmglDbrw.getSqxxid());
                dchyXmglYbrw.setBljg(shth ? Constants.INVALID : Constants.VALID);
                dchyXmglYbrw.setDqjd(dchyXmglDbrw.getDqjd());
                dchyXmglYbrw.setZrsj(dchyXmglDbrw.getZrsj());
                dchyXmglYbrw.setYbrwid(dchyXmglDbrw.getDbrwid());
                /*??????????????????*/
                entityMapper.saveOrUpdate(dchyXmglYbrw, dchyXmglYbrw.getYbrwid());
                //??????????????????
                entityMapper.deleteByPrimaryKey(DchyXmglDbrw.class, dbrwid);
            }

            if (null != dchyXmglDbrw && StringUtils.isNotBlank(dchyXmglDbrw.getSqxxid())) {
                DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, dchyXmglDbrw.getSqxxid());
                if (shth) {
                    dchyXmglSqxx.setSqzt(Constants.DCHY_XMGL_SQZT_TH);//??????
                } else {
                    pushDataToMqService.pushMlkMsgToMq("", dchyXmglSqxx.getSqxxid());
                    dchyXmglSqxx.setSqzt(Constants.DCHY_XMGL_SQZT_SHTG);//????????????
                }
                entityMapper.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());

                DchyXmglMlk dchyXmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, dchyXmglSqxx.getGlsxid());

                if (StringUtils.isNotBlank(dchyXmglSqxx.getGlsxid())) {
                    List<Map<String, Object>> mlkXxById = mlkMapper.getMlkXxById(dchyXmglSqxx.getGlsxid());
                    if (CollectionUtils.isNotEmpty(mlkXxById) && null != mlkXxById.get(0).get("MLKTP")) {
                        //??????blob?????????????????????base64?????????
                        byte[] bytes = CommonUtil.blobToBytes((Blob) mlkXxById.get(0).get("MLKTP"));
                        if (null != bytes && bytes.length > 0) {
                            dchyXmglMlk.setMlktp(bytes);

                        }
                    }

                    if (!shth) {
                        dchyXmglMlk.setSfyx(Constants.VALID);//1:??????
                        dchyXmglMlk.setSfdj(Constants.INVALID);//0:?????????
                    }
                    entityMapper.saveOrUpdate(dchyXmglMlk, dchyXmglMlk.getMlkid());

                    //????????????
                    String yhxxpzid = null;
                    if (shth) {
                        yhxxpzid = Constants.DCHY_XMGL_ZD_XXNR_MLKRZ_BTG;
                    } else {
                        yhxxpzid = Constants.DCHY_XMGL_ZD_XXNR_MLKRZ_TG;
                    }
                    String jsyhid = dchyXmglMlk.getMlkid();
                    String glsxid = dchyXmglMlk.getMlkid();
                    Map<String, Object> paramMap = Maps.newHashMap();
                    paramMap.put("yhxxpzid", yhxxpzid);
                    paramMap.put("jsyhid", jsyhid);
                    paramMap.put("glsxid", glsxid);
                    messagePushService.updateYhxxInfo(paramMap);
                }
            }
            result.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
            result.put("code", ResponseMessage.CODE.SUCCESS.getCode());
        } else {
            result.put("msg", ResponseMessage.CODE.SAVE_FAIL.getMsg());
            result.put("code", ResponseMessage.CODE.SAVE_FAIL.getCode());
        }
        return result;
    }

    @Override
    public String getCode() {
        return Constants.BLSX_MLKRZ;
    }

}
