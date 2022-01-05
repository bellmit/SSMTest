package cn.gtmap.msurveyplat.serviceol.core.service;

import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;

import java.util.Map;

/**
 * 生成合同信息DTO对象
 */
public interface DchyXmglHtService {

    DchyXmglHtxxDto generateHtxxDto(Map<String,Object> map);
}
