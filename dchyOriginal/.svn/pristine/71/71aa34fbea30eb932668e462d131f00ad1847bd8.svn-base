package cn.gtmap.msurveyplat.serviceol.mapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglTzgg;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/1
 * @description 管理单位  通知公告管理
 */
@Repository
public interface DchyGldwTzggglMapper {

    List<Map<String,String>> getAllTzggListByPage(Map<String,String> param);

    List<Map<String,String>> getBsznTzggListByPage(Map<String,String> param);

    List<Map<String,String>> getOtherTzggListByPage(Map<String,String> param);

    DchyXmglTzgg getDchyXmglTzggByTzggid(String tzggid);

}
