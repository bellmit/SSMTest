package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.util.SM4Util;
import cn.gtmap.msurveyplat.serviceol.core.service.YwljyzService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.web.util.DateUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/7 12:39
 * @description
 */
@Service
public class YwljzyMlkServiceImpl implements YwljyzService {

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public boolean checkInfos(Object object) {
        Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSONString(object), Map.class);
        Map<String, Object> data = MapUtils.getMap(map, "data");
        if (null != data) {
            String mlkId = MapUtils.getString(data, "mlkid");
            String tyshxydm = MapUtils.getString(data, "tyshxydm");
            Example example = new Example(DchyXmglMlk.class);
            example.createCriteria().andEqualTo("tyshxydm", SM4Util.encryptData_ECB(tyshxydm)).andIsNotNull("ycsj").andEqualTo("sfyx", Constants.INVALID).andEqualTo("sfdj", Constants.INVALID);
            /*判断mlkid对应的名录是否存在*/
            List<DchyXmglMlk> tempMlkList = entityMapper.selectByExampleNotNull(example);
            /*存在，则对有效和移出时间进行判定*/
            if (CollectionUtils.isNotEmpty(tempMlkList)) {
                for (DchyXmglMlk tempMlk : tempMlkList) {
                    /*无效状态*/
                    int result = DateUtil.daysBetween(tempMlk.getYcsj(), new Date());
                    /*相差日期在两年之内，不可再申请*/
                    if (result < 731) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String getCode() {
        return "mlk";
    }
}
