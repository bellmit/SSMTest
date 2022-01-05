package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxm;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.promanage.core.service.CgtjService;
import cn.gtmap.msurveyplat.promanage.model.UserInfo;
import cn.gtmap.msurveyplat.promanage.service.CgtjCxService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class CgtjServiceImpl implements CgtjService {
    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private CgtjCxService cgtjCxService;

    @Override
    public Map cgtyjfqr(Map param) {
        Map qrjgResrultMap = Maps.newHashMap();
        String msg;
        String code;
        String chxmid = MapUtils.getString(param, "chxmid");
        if (StringUtils.isNotBlank(chxmid)) {
            Map cgtyjfResultMap = cgtjCxService.cgtyjfqr(param);
            if (MapUtils.getBoolean(cgtyjfResultMap, "chxmcz", false)) {
                if (MapUtils.getBoolean(cgtyjfResultMap, "cgtyjf", false)) {
                    msg = "测绘项目成果已确认交付，无法再次确认";
                    code = ResponseMessage.CODE.OPERATION_REPET.getCode();
                } else {
                    DchyXmglChxm dchyXmglChxm = new DchyXmglChxm();
                    UserInfo userInfo = UserUtil.getCurrentUser();
                    dchyXmglChxm.setChxmid(chxmid);
                    dchyXmglChxm.setTyjfqrczr(userInfo.getId());
                    dchyXmglChxm.setTyjfqrczrmc(userInfo.getUsername());
                    dchyXmglChxm.setTyjfzt(Constants.VALID);
                    dchyXmglChxm.setTyjfqrsj(new Date());
                    entityMapper.updateByPrimaryKeySelective(dchyXmglChxm);
                    msg = "确认成功";
                    code = ResponseMessage.CODE.SUCCESS.getCode();
                }
            } else {
                msg = "测绘项目不存在，无法确认成果已交付";
                code = ResponseMessage.CODE.QUERY_NULL.getCode();
            }
        } else {
            msg = "测绘项目不存在，无法确认成果已交付";
            code = ResponseMessage.CODE.PARAMEMPTY_FAIL.getCode();
        }
        qrjgResrultMap.put("msg", msg);
        qrjgResrultMap.put("code", code);
        return qrjgResrultMap;
    }
}
