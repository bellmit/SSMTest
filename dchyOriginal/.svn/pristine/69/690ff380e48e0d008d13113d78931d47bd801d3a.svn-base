package cn.gtmap.msurveyplat.server.web.rest.log;

import cn.gtmap.bdc.log.domain.dto.AuditLogDto;
import cn.gtmap.msurveyplat.server.aop.LogAspect;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;


/**
 * 日志信息查询
 *
 * @author Liuhongwei
 */

@RestController
@RequestMapping("/log")
public class LogController {

    /**
     * 日志服务地址
     */
    @Value("${log.url}")
    private String url;

    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @ApiOperation(value = "自定义条件日志事件分页查询")
    @GetMapping("/v1.0/list")
    public LinkedHashMap listLogs(@RequestParam(name = "event", required = false) String event,
                                  @RequestParam(name = "principal", required = false) String principal,
                                  @RequestParam(name = "begin", required = false) Long begin,
                                  @RequestParam(name = "end", required = false) Long end) {

        String uri = url + "/rest/v1/logs/list?event=" + event + "&principal=" + principal + "&begin=" + begin + "&end=" + end;
        LinkedHashMap map = sendGetRequest(uri);
        return map;
    }

    @ApiOperation(value = "查询单条日志")
    @GetMapping("/v1.0/logInfo/{id}")
    public AuditLogDto getAuditLogDetail(@PathVariable(name = "id") String id) {
        String uri = url + "/rest/v1/logs/" + id;
        AuditLogDto auditLogDto = sendGetRequest(uri);
        return auditLogDto;
    }

    /**
     * 调用第三方get接口
     *
     * @param url
     */
    public <T> T sendGetRequest(String url){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.GET;

        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 执行HTTP请求，将返回的结构类格式化（
        ResponseEntity<T> response = client.exchange(url, method, null, new ParameterizedTypeReference<T>() {
        });
        Integer status = response.getStatusCodeValue();
        if(200 == status){
            T body = response.getBody();
            logger.info("日志信息查询:日志查询成功" + body);
            return body;
        }else {
            logger.error("日志信息查询:日志查询失败" + status);
            return null;
        }
    }
}
