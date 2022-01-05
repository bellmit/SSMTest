package cn.gtmap.msurveyplat.serviceol.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DchyXmglOnlineDelegationMapper {

    /**
     * 多条件查询项目委托信息
     * @param param
     * @return
     */
    List<Map<String,Object>> queryProjectEntrustMultipleConditionsByPage(Map<String,Object> param);

    List<Map<String,Object>> getHtbgRecord(Map<String,Object> param);

    /**
     * 获取待接受委托的数量
     * @param mlkid
     * @return
     */
    int getToBeAcceptedNum(@Param(value = "mlkid")String mlkid);


    List<Map<String, Object>> getLatelyCgTjjlByChxmid(@Param(value = "chxmid") String chxmid);


    /**
     * 更新sjcl中的wjzxid
     * @param sjclid
     * @return
     */
    int updateSjclById(@Param(value = "sjclid")String sjclid);

    int updateHtxxById(@Param(value = "htxxid") String htxxid);
}
