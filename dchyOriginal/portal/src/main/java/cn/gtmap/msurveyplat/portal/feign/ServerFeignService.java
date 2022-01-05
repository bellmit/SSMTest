package cn.gtmap.msurveyplat.portal.feign;

import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.util.TokenCheckUtil;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/10
 * @description
 */
public interface ServerFeignService {
    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param  initDataParamDTO
     * @return
     * @description  创建流程 初始化数据
     * */
    @Headers({"Content-Type: application/json","token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /init/rest/v1.0/csh")
    DchyCgglXmDO initData(InitDataParamDTO initDataParamDTO);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param  xmid 项目id
     * @return
     * @description 删除业务数据
     * */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /init/rest/v1.0/scywxx/{xmid}")
    void deleteYwxx(@Param("xmid") String xmid);
}
