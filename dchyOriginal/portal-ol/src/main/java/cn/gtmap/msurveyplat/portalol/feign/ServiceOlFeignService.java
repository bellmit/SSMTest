package cn.gtmap.msurveyplat.portalol.feign;

import cn.gtmap.msurveyplat.common.dto.DchyXmglJsdwlrDto;
import cn.gtmap.msurveyplat.common.dto.ParamDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.TokenCheckUtil;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description Exchange服务
 */
public interface ServiceOlFeignService {

    /**
     * @param tzggid
     * @return
     * @description 2020/12/11 查看具体的通知公告
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /feign/queryGldwTzgggl/{tzggid}")
    Map<String, Object> queryGldwTzgggl(@Param("tzggid") String tzggid);

    /**
     * @param paramDto
     * @return
     * @description 2020/12/11 产看办事指南的通知公告
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /feign/getTzggByBszn")
    ResponseMessage getTzggByBszn(@RequestBody ParamDto paramDto);

    /**
     * @param paramDto
     * @return
     * @description 2020/12/11 查看除了办事指南外的通知公告
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /feign/getOtherTzgg")
    ResponseMessage getOtherTzgg(@RequestBody ParamDto paramDto);

    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /feign/getsjcl/{ssmkId}/{glsxid}")
    ResponseMessage getsjcl(@Param("ssmkId") String ssmkId, @Param("glsxid") String glsxid);


    @Headers({"Content-Type: application/json", "Accept: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /feign/getzdxx")
    ResponseMessage getzdxx(@RequestBody Map map);

    @Headers({"Content-Type: application/json", "Accept: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /feign/pushJsdwlrDataMsg")
    ResponseMessage pushJsdwlrDataMsg(@RequestBody DchyXmglJsdwlrDto dchyXmglJsdwlrDto);

    @Headers({"Content-Type: application/json", "Accept: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /feign/excuteYwljyz")
    ResponseMessage excuteYwljyz(@RequestBody Map map);

    @Headers({"Content-Type: application/json", "Accept: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /feign/getBdbtxyzList")
    ResponseMessage getBdbtxyzList(@RequestBody Map map);
}
