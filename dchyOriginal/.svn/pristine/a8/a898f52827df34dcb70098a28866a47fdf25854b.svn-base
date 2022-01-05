package cn.gtmap.msurveyplat.portalol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglDbrw;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYbrw;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.portalol.core.service.BlsxBjService;
import cn.gtmap.msurveyplat.portalol.utils.Constants;
import cn.gtmap.msurveyplat.portalol.utils.UserUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
public class BlsxBjMlkRzServiceImpl implements BlsxBjService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Map bj(String dbrwid, Object object) {
        Map result = Maps.newHashMap();
        if (StringUtils.isNotBlank(dbrwid)) {
            Map param = (Map) object;
            boolean shth = StringUtils.equalsIgnoreCase(MapUtils.getString(param, "sfth"), "1");
            String shyj = MapUtils.getString(param, "shyj");
            DchyXmglDbrw dchyXmglDbrw = entityMapper.selectByPrimaryKey(DchyXmglDbrw.class, dbrwid);
            DchyXmglYbrw dchyXmglYbrw = new DchyXmglYbrw();
            dchyXmglYbrw.setBlryid(UserUtil.getCurrentUserId());
            dchyXmglYbrw.setBlyj(shyj);
            dchyXmglYbrw.setJssj(new Date());
            dchyXmglYbrw.setSqxxid(dchyXmglDbrw.getSqxxid());
            dchyXmglYbrw.setZrsj(dchyXmglDbrw.getZrsj());
            dchyXmglYbrw.setYbrwid(dchyXmglDbrw.getDbrwid());
            entityMapper.saveOrUpdate(dchyXmglYbrw, dchyXmglYbrw.getYbrwid());

            entityMapper.deleteByPrimaryKey(DchyXmglDbrw.class, dbrwid);

            DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, dchyXmglDbrw.getSqxxid());
            if (shth) {
                dchyXmglSqxx.setSqzt(Constants.DCHY_XMGL_SQZT_TH);
            } else {
                dchyXmglSqxx.setSqzt(Constants.DCHY_XMGL_SQZT_SHTG);
            }
            entityMapper.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());

            DchyXmglMlk dchyXmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, dchyXmglSqxx.getGlsxid());
            if (!shth) {
                dchyXmglMlk.setSfyx("1");
            }
            DataSecurityUtil.decryptSingleObject(dchyXmglMlk);
            entityMapper.saveOrUpdate(dchyXmglMlk, dchyXmglMlk.getMlkid());
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
