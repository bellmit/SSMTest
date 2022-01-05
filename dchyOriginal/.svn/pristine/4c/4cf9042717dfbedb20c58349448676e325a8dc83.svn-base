package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhdw;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.promanage.core.service.UserService;
import cn.gtmap.msurveyplat.promanage.model.UserDto;
import cn.gtmap.msurveyplat.promanage.model.UserInfo;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public UserDto getCurrentUserDto() {
        UserInfo userInfo = UserUtil.getCurrentUser();
        UserDto userDto = new UserDto();
        userDto.setAlias(userInfo.getUsername());
        userDto.setId(userInfo.getId());
        userDto.setUsername(userInfo.getUsername());
        DchyXmglYhdw dchyXmglYhdw = getDchyXmglYhdwByUserId(userInfo.getId());
        if (dchyXmglYhdw != null) {
            userDto.setDwmc(dchyXmglYhdw.getDwmc());
            userDto.setDwbh(dchyXmglYhdw.getDwbh());
            userDto.setYhlx(dchyXmglYhdw.getYhlx());
        }
        return userDto;
    }

    @Override
    public DchyXmglYhdw getDchyXmglYhdwByUserId(String userid) {
        if (StringUtils.isNotBlank(userid)) {
            Example example = new Example(DchyXmglYhdw.class);
            example.createCriteria().andEqualTo("yhid", userid);
            List<DchyXmglYhdw> dchyXmglYhdwList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglYhdwList)) {
                DchyXmglYhdw dchyXmglYhdw = dchyXmglYhdwList.get(0);
                DataSecurityUtil.decryptSingleObject(dchyXmglYhdw);
                return dchyXmglYhdw;
            }
        }
        return null;
    }
}
