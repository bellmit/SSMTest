package cn.gtmap.msurveyplat.server.service.rest;

import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/4
 * @description 登记子系统对外初始化接口
 */
public interface InitDataRestService {
   /**
    * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
    * @param initDataParamDTO 初始化参数对象
    * @return 项目信息
    * @description 初始化业务系统项目相关信息
    */
    @PostMapping(value = "/init/rest/v1.0/csh")
    DchyCgglXmDO csh(InitDataParamDTO initDataParamDTO) throws Exception;

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmid 项目id
     * @return
     * @description 根据xmid删除业务信息
     * */
    @PostMapping(value = "/init/rest/v1.0/scywxx/{xmid}")
    void scywxx(String xmid) throws Exception;

}
