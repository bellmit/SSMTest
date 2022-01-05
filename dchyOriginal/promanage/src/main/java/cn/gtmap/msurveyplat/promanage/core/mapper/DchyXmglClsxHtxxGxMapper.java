package cn.gtmap.msurveyplat.promanage.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DchyXmglClsxHtxxGxMapper {

    /**
     * @param
     * @return
     * @description 2020/11/28 获取表测量事项信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String,Object>> getClsxByChxmid(@Param(value = "htxxid") String htxxid);

    /**
     * @param
     * @description  项目查看台账获取测量事项
     */
    List<Map<String,Object>> getClsxByChxmids(@Param(value = "chxmid") String chxmid);

    /**
     * @param
     * @description  项目查看中点击查询按钮获取测量事项台账
     */
    List<Map<String,Object>> getClsxByChxmidlist(Map<String,Object> paramMap);

    /**
     * @param
     * @description  根据CLSXID获取对应的测绘单位信息
     */
    List<Map<String,Object>> getChdwxxByCLsx(Map<String,Object> paramMap);


}
