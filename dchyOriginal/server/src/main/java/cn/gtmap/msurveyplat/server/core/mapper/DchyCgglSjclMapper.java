package cn.gtmap.msurveyplat.server.core.mapper;

import cn.gtmap.msurveyplat.common.domain.DchyCgglSjclDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/16
 * @description 成果管理收件材料信息接口
 */
@Repository
public interface DchyCgglSjclMapper {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmid 项目ID
     * @return
     * @description 根据项目ID查询成果管理收件材料信息
     */
    List<DchyCgglSjclDO> getDchyCgglSjclDOListByXmid(@Param("xmid") String xmid);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param  xmid 项目ID
     * @return
     * @description 根据登记小类获取多测合一成果管理默认收件材料信息
     */
    List<DchyCgglSjclDO> getDefaultDchyCgglSjclDOListByXmid(@Param("xmid") String xmid);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param  gzldyid 工作流定义id
     * @return
     * @description 根据工作流定义id获取字典表中的收件材料
     * */
    List<DchyCgglSjclDO> getDchyCgglSjclDOListByGzldyid(@Param("gzldyid") String gzldyid);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param slclidList 收件材料id
     * @return
     * @description 删除收件材料信息
     * */
    void deleteSjcl(@Param("slclidList") List<String> slclidList);
}
