package cn.gtmap.msurveyplat.serviceol.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhdw;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface DchyXmglYhdwService {

    DchyXmglYhdw getDchyXmglYhdwByUserId(String userid);

    Page<Map<String, Object>> queryYhdwUserByPage(Map<String, Object> paramdata);

    Map<String, String> changeUserState(String userid, String state);
}