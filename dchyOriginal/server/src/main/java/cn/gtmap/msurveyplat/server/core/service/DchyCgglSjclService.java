package cn.gtmap.msurveyplat.server.core.service;


import cn.gtmap.msurveyplat.common.domain.DchyCgglSjclDO;

import java.util.List;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/16
 * @description 成果管理收件材料信息接口
 */
public interface DchyCgglSjclService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param  xmid 项目ID
     * @return
     * @description 根据项目ID获取多测合一成果管理收件材料信息
     */
    List<DchyCgglSjclDO> getDchyCgglSjclDOListByXmid(String xmid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param  xmid 项目ID
     * @return
     * @description 根据登记小类获取多测合一成果管理默认收件材料信息
     */
    List<DchyCgglSjclDO> getDefaultDchyCgglSjclDOListByXmid(String xmid);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param  gzldyid 工作流定义id
     * @return
     * @description 根据工作流定义id获取字典表中的收件材料
     * */
    List<DchyCgglSjclDO> getDchyCgglSjclDOListByGzldyid(String gzldyid);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param dchyCgglSjclDO
     * @return
     * @description 保存或更新收件材料信息
     * */
    void saveOrUpdateDchyCgglSjcl(DchyCgglSjclDO dchyCgglSjclDO);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param slclidList 收件材料id
     * @return
     * @description 删除收件材料信息
     * */
    void deleteSjcl(List<String> slclidList);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param sjclid 收件材料id
     * @return
     * @description 根据收件材料id 获取收件材料
     * */
    DchyCgglSjclDO getDchyCgglSjclDOById(String sjclid);
}
