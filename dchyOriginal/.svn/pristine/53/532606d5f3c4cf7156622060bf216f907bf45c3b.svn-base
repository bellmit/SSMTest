package cn.gtmap.msurveyplat.server.core.mapper;

import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DchyCgglXmMapper {
    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @description 根据xmid查询cgglxm
      */
    DchyCgglXmDO queryDchyCgglXmByXmid(@Param("xmid") String xmid);

    /**
     * 获得所有项目信息
     * @param paramMap
     * @return
     */
      List<Map<String,String>> ObtainXmBanSjByPage(Map<String,String> paramMap);


    /**
     * 通过类型 1,2分别获取电话和联系人
     * @param paramMap
     * @return
     */
    Map<String,String> queryNamePhone(Map<String,String> paramMap);


    /**
     * 查找所有的测绘名称和代码
     * @return
     */
    List<Map<String,String>>  queryChdwMc();

    /**
     *     查询项目地址 查出来的结果是dm并非显示在页面的值
     * @return
     */
    List<String> queryXmdz (Map<String,String> paramMap);



    /**
     *查询项目地址  从配置文件中读取并非代码
     * @return
     */
    List<String> queryXmdzmc (List<String> dm);

}
