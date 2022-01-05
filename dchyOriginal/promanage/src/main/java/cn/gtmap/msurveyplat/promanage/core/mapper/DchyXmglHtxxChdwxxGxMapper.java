package cn.gtmap.msurveyplat.promanage.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DchyXmglHtxxChdwxxGxMapper {
    /**
     * @param
     * @return
     * @description 2020/11/28 获取表测绘单位信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String,Object>> getChdwxxByChxmid(@Param(value = "htxxid") String htxxid);
}
