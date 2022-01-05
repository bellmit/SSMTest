package cn.gtmap.msurveyplat.server.core.mapper;


import cn.gtmap.msurveyplat.common.domain.DchyXtMryjDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/10
 * @description 多测合一默认意见接口
 */
public interface DchyXtMryjMapper {


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmid 项目ID
     * @param gzljdmc 工作流节点名称
     * @return
     * @description 获取收件材料字典表信息
     */
    List<DchyXtMryjDO> getDchyXtMryjDOListByXmidAndGzljdmc(@Param("xmid") String xmid, @Param("gzljdmc") String gzljdmc);

    List<DchyXtMryjDO> getYjpz(Map<String,String> map);

    String getYjid(Map<String,String> map);

}
