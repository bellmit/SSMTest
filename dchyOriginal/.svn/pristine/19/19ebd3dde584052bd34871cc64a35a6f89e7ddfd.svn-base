package cn.gtmap.msurveyplat.server.service.feign;

import cn.gtmap.msurveyplat.common.domain.ProcessDefData;
import cn.gtmap.msurveyplat.common.dto.UserTaskDto;
import com.gtis.plat.vo.PfSignVo;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description
 */
public interface ExchangeSignService {

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @description 获取签名
     */
    @RequestLine("POST /v1.0/platForm/getSign/{signId}")
    PfSignVo getSign(@Param("signId") String signId);

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @description 获取签名图片
     */
    @RequestLine("POST /v1.0/platForm/getSignImage/{signId}")
    PfSignVo getSignImage(@Param("signId") String signId);


    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @description 删除签名
     */
    @RequestLine("POST /v1.0/platForm/deleteSign/{signId}")
    void deleteSign(@Param("signId") String signId);


    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @description 更新签名
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @RequestLine("POST /v1.0/platForm/updateAutoSign")
    void updateAutoSign(@RequestBody PfSignVo pfSignVo);

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @description 保存签名
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @RequestLine("POST /v1.0/platForm/insertAutoSign")
    void insertAutoSign(@RequestBody PfSignVo pfSignVo);

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @description 获取签名list
     */
    @RequestLine("POST /v1.0/platForm/getSignList/{signKey}/{proid}")
    List<PfSignVo> getSignList(@Param("signKey")String signKey, @Param("proid")String proid);

    /**
     * @author <a href="mailto:liuqiang@gtmap.cn">dingweiwei</a>
     * @description 获取工作流信息列表
     */
    @RequestLine("GET /rest/v1.0/platform/process/list")
     List<ProcessDefData> getWorkflowDefinitionList();

    /**
     * @author <a href="mailto:liuqiang@gtmap.cn">dingweiwei</a>
     * @description 获取节点信息列表
     */
    @RequestLine("POST /rest/v1.0/platform/process/{gzldyid}")
     List<UserTaskDto> getWorkflowDefinitionListByid(@Param("gzldyid") String gzldyid);


}
