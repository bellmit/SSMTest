package cn.gtmap.msurveyplat.server.core.service;


import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.domain.DchyZdChdwDo;
import org.springframework.ui.Model;

import java.util.List;

/**
  * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
  * @description  成果管理项目服务
  */
public interface DchyCgglXmService {
    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @description 组织受理信息
      */
    Model initSlxx(Model model, String xmid);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @description 保存成果管理项目信息
      */
    void saveDchyCgglXm(DchyCgglXmDO dchyCgglXmDO);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmid 项目ID
     * @return
     * @description 获取多测合一项目
     */
    DchyCgglXmDO getDchyCgglXmDOByXmid(String xmid);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param
     * @return
     * @description 获取测绘单位
     * */
    List<DchyZdChdwDo> getChdw();

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmid 项目ID
     * @return
     * @description 删除项目信息
     * */
    void deleteDchyCgglXmDOByXmid(String xmid);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmid 项目ID
     * @return
     * @description 删除申请人信息
     * */
    void deleteDchyCgglSqrDOByXmid(String xmid);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param chxmbh 测绘项目编号
     * @param rkzt 入库状态
     * @param xmzt 项目状态
     * @return
     * @description 获取多测合一成果管理项目信息
     */
    List<DchyCgglXmDO> getDchyCgglXmDOList(String chxmbh, String rkzt, String xmzt);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param dchybh 测绘项目编号
     * @param rkzt 入库状态
     * @param xmzt 项目状态
     * @return
     * @description 获取多测合一成果管理项目信息
     */
    List<DchyCgglXmDO> getDchyCgglXmDOListByDchybh(String dchybh, String rkzt, String xmzt);

}
