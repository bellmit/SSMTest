package cn.gtmap.msurveyplat.server.core.service;

import cn.gtmap.msurveyplat.common.domain.DchyXtMryjDO;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/26
 * @description 多测合一系统默认意见
 */
public interface DchyXtMryjService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param gzldyid 工作流定义ID
     * @param gzljdmc 工作流节点名称
     * @return
     * @description 获取多测合一系统默认意见
     */
    List<DchyXtMryjDO> getDchyXtMryjDOListByGzldyidAndGzljdmc(String gzldyid, String gzljdmc);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmid 项目ID
     * @param gzljdmc 工作流节点名称
     * @return
     * @description 获取多测合一系统默认意见
     */
    List<DchyXtMryjDO> getDchyXtMryjDOListByXmidAndGzljdmc(String xmid, String gzljdmc);


    /**
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     * @param map
     * @return
     * @description 获取默认意见集合
     */
    List<DchyXtMryjDO> getYjpz(Map<String,String> map);

    /**
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     * @param dchyXtMryjDO
     * @return
     * @description 新增默认意见
     */
    int addMryj(DchyXtMryjDO dchyXtMryjDO);

    /**
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     * @param yjid 意见ID
     * @return
     * @description 根据yjid删除数据
     */
    int delMryjByYjid(String yjid);

    /**
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     * @param yjidList
     * @return
     * @description 批量删除
     */
    int delMryj(List<String> yjidList);

    /**
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     * @param dchyXtMryjDO
     * @return
     * @description 根据yjid更新数据
     */
    int updMryjByYjid(DchyXtMryjDO dchyXtMryjDO);

    /**
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     * @param map
     * @return
     * @description 根据节点名称和工作流名称获取当条记录的yjid
     */
    String getYjid(Map<String,String> map);

}
