package cn.gtmap.msurveyplat.server.core.service;

import cn.gtmap.msurveyplat.common.domain.DchyCgglShxxDO;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/10
 * @description 成果管理审核信息接口
 */
public interface DchyCgglShxxService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param dchyCgglShxxDO 审核信息
     * @return
     * @description 保存审核信息
     */
    void saveDchyCgglShxx(DchyCgglShxxDO dchyCgglShxxDO);


    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @description 根据xmid查询成果管理审核信息
      */
    List<DchyCgglShxxDO> getDchyCgglShxxListByXmid(String xmid);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param shxxid 审核信息ID
     * @return
     * @description 根据审核信息ID获取审核信息
     */
    DchyCgglShxxDO getDchyCgglShxxDOByShxxid(String shxxid);


    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmid 项目id
     * @return
     * @description 根据xmid删除审核签名
     * */
    void deleteDchyCgglShxxByXmid(String xmid);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param shxxid 审核信息ID
     * @return
     * @description 根据审核信息ID删除审核信息
     * */
    void deleteDchyCgglShxxByShxxid(String shxxid);

}
