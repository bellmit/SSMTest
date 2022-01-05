package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhdw;
import cn.gtmap.msurveyplat.serviceol.core.service.UserService;
import cn.gtmap.msurveyplat.serviceol.model.UserDto;
import cn.gtmap.msurveyplat.serviceol.model.UserInfo;
import cn.gtmap.msurveyplat.serviceol.service.DchyXmglYhdwService;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private DchyXmglYhdwService dchyXmglYhdwService;

    @Override
    public UserDto getCurrentUserDto() {
        UserInfo userInfo = UserUtil.getCurrentUser();
        UserDto userDto = new UserDto();
        userDto.setAlias(userInfo.getUsername());
        userDto.setId(userInfo.getId());
        userDto.setUsername(userInfo.getLoginName());
        DchyXmglYhdw dchyXmglYhdw = dchyXmglYhdwService.getDchyXmglYhdwByUserId(userInfo.getId());
        if (dchyXmglYhdw != null) {
            userDto.setDwmc(dchyXmglYhdw.getDwmc());
            userDto.setDwbh(dchyXmglYhdw.getDwbh());
            userDto.setYhlx(dchyXmglYhdw.getYhlx());
        }
        return userDto;
    }
}
