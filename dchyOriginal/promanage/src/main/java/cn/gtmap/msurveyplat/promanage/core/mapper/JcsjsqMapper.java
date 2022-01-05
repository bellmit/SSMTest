package cn.gtmap.msurveyplat.promanage.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface JcsjsqMapper {

    //基础数据申请-随机抽查指定几数据
    List<Map<String, Object>> queryBasicDataApplicationInfoByPage(@Param(value = "ccnum") int ccnum);

    /*根据chxmid找出最早的一条抽查记录*/
    List<Map<String, Object>> queryEarliyCgccsByChxmid(@Param(value = "chxmid") String chxmid);

    //基础数据申请的基础数据范围小台账
    List<Map<String, Object>> querySjclListByBabhByPage(Map<String, Object> map);

    //通过chxmid获取备案信息
    List<Map<String, Object>> querBaxxByChxmid(Map<String, Object> map);

    //获取当前申请的操作日志
    List<Map<String, Object>> querySqxxListByPage(Map<String, Object> map);

    //根据受理编号获取项目信息
    List<Map<String, Object>> queryBaxxByBabh(Map<String, Object> map);

    //通过工程编号获取备案测量事项
    List<Map<String, Object>> queryBaClsxListByGcbh(Map<String, Object> paramMap);

    //获取抽查列表台账
    List<Map<String, Object>> queryResultsSpotCheckListByPage(Map<String, Object> paramMap);

}
