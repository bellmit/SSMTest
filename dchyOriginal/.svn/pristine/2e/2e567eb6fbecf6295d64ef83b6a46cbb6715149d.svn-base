package cn.gtmap.msurveyplat.portal.feign;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.util.TokenCheckUtil;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
  * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
  * @description 项目管理feign接口
  * @time 2021/1/18 11:19
  */
public interface PromanageFeignService {
    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param  initDataParamDTO
     * @return
     * @description  创建流程 初始化数据
     * */
    @Headers({"Content-Type: application/json","token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/event/lccsh")
    DchyXmglSqxx initData(InitDataParamDTO initDataParamDTO);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param  xmid 项目id
     * @return
     * @description 删除业务数据
     * */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/event/scywxx/{gzldyid}/{xmid}")
    void deleteYwxx(@Param("gzldyid") String gzldyid,@Param("xmid") String xmid);
}
