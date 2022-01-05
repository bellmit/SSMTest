package cn.gtmap.msurveyplat.promanage.core.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhdw;
import cn.gtmap.msurveyplat.promanage.model.UserDto;

public interface UserService {

    UserDto getCurrentUserDto();

    /**
     * @param userid
     * @return
     * @description 2020/12/30 通过userid返货当前用户信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    DchyXmglYhdw getDchyXmglYhdwByUserId(String userid);
}
