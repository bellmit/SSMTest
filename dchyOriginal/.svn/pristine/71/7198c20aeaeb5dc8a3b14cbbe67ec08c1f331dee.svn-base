package cn.gtmap.msurveyplat.server.core.mapper;

import cn.gtmap.msurveyplat.common.domain.DchyCgglZjqtcwDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/10
 * @description 质检信息
 */
public interface QualityCheckMapper {
    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param  cwList 其他错误
     * @return
     * @description 新增其他错误
     * */
    void xzqtcw(@Param("cwList") List<DchyCgglZjqtcwDO> cwList);


    /**
     * @author <a href="mailto:wangyang@gtmap.cn">wangyang</a>
     * @param  cwid 其他错误
     * @return
     * @description 新增其他错误
     * */
    Map<String,Object> getqtcwxx( Map<String,String> cwid);



    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param cwidList 错误id
     * @return
     * @description
     * */
    void delQtcw(@Param("cwidList") List<String> cwidList);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param  param 项目id
     * @return
     * @description 获取其他错误
     * */
    List<DchyCgglZjqtcwDO> getQtcwListByPage(Map<String, String> param);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @return 检查类型
     * @description 获取检查类型
     * */
    List<Map<String, String>> getJclx();

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param map
     * @return 检查结果
     * @description 获取检查结果
     * */
    List<Map<String, Object>> getJcjgByPage(Map<String, String> map);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param
     * @return
     * @description 统计结果数量
     * */
    Map<String, Object> countJgsl(@Param("xmbn") String xmbh, @Param("dm") String dm);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param
     * @return
     * @description 获取字典表  错误级别
     * */
    List<Map<String, String>> getCwjb();

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmbh  项目编号
     * @return 检查内容
     * @description 获取检查内容
     * */
    Map<String, String> getJcnr(@Param("xmbh") String xmbh);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmbh  项目编号
     * @return
     * @description 统计结果数量
     * */
    Map<String, Object> countJcjgTotal(@Param("xmbn") String xmbh);

    /**
     * @author <a href="mailto:wangyang@gtmap.cn">wangyang</a>
     * @param
     * @return
     * @description 查看是否全部删除
     * */
    Integer  countcwxx(Map<String,String>xmidMap);

}
