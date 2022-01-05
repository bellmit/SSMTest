package cn.gtmap.msurveyplat.serviceol.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2020/11/27 16:08
 * @description 模板管理
 */
@Repository
public interface DchyXmglHtxxMapper {

    List<Map<String,Object>> queryMbxxByMbmcOrMblxByPage(Map<String,Object> paraMap);

    void updateMbztByMbid(Map<String,Object> paramMap);

    void updateTmbztByMbid(Map<String,Object> paramMap);



}
