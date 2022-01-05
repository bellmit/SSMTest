package cn.gtmap.msurveyplat.serviceol.web.log;

import cn.gtmap.bdc.log.domain.dto.QueryLogCondition;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.*;
import cn.gtmap.msurveyplat.serviceol.core.service.LogService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;


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
    @Autowired
    private LogService logService;

    private Logger logger = LoggerFactory.getLogger(LogController.class);

    @ApiOperation(value = "自定义条件日志事件分页查询")
    @PostMapping("/xgrzcx")
    public ResponseMessage rzcx(@RequestBody Map map) {
        ResponseMessage responseMessage;
        try {
            String uri = url + "/api/v2/logs/condition/list";
            Map data = MapUtils.getMap(map, "data");
            Map paraMap = Maps.newHashMap();
            String page = Constants.DCHY_XMGL_PAGINATION_PAGE;
            String size = Constants.DCHY_XMGL_PAGINATION_SIZE;
            String glsxid = null;
            paraMap.put("glsxid", "");
            List<QueryLogCondition> logConditionList = Lists.newArrayList();
            if (MapUtils.isNotEmpty(data)) {
                glsxid = MapUtils.getString(data, "glsxid");
                if (StringUtils.isNoneBlank(glsxid)) {
                    QueryLogCondition queryLogCondition = new QueryLogCondition();
                    queryLogCondition.setKey("glsxid");
                    queryLogCondition.setValue(glsxid);
                    queryLogCondition.setType(Constants.EQUAL_QUERY);
                    logConditionList.add(queryLogCondition);
                }
                if (StringUtils.isNotBlank(MapUtils.getString(data, "kssj"))) {
                    paraMap.put("beginTime", CommonUtil.formateDateToStr(MapUtils.getString(data, "kssj")));
                }
                if (StringUtils.isNotBlank(MapUtils.getString(data, "jssj"))) {
                    Date jssj = CalendarUtil.formatDate(MapUtils.getString(data, "jssj"));
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(jssj);
                    calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
                    paraMap.put("endTime", calendar.getTime());
                }
                if (StringUtils.isNotBlank(MapUtils.getString(data, "xgr"))) {
                    QueryLogCondition queryLogCondition = new QueryLogCondition();
                    queryLogCondition.setKey("czrid");
                    queryLogCondition.setValue(SM4Util.decryptData_ECB(MapUtils.getString(data, "xgr")));
                    queryLogCondition.setType(Constants.EQUAL_QUERY);
                    logConditionList.add(queryLogCondition);
                }
                if (StringUtils.isNotBlank(MapUtils.getString(data, "page"))) {
                    page = MapUtils.getString(data, "page");
                }
                if (StringUtils.isNotBlank(MapUtils.getString(data, "size"))) {
                    size = MapUtils.getString(data, "size");
                }
            }
            /*名录库*/
            paraMap.put("datas", logConditionList);
            paraMap.put("czmk", ProLog.CZMC_MLK_ALTER_CODE);
            paraMap.put("page", page);
            paraMap.put("size", size);
            LinkedHashMap resultMap = sendPostRequest(uri, paraMap);
            /*从业人员*/
            paraMap.put("datas", logConditionList);
            paraMap.put("czmk", ProLog.CZMC_CYRY_ALTER_CODE);
            paraMap.put("page", page);
            paraMap.put("size", size);
            LinkedHashMap resultMap2 = sendPostRequest(uri, paraMap);
            List<Object> content2 = (List<Object>) resultMap2.get("content");

            List<Object> content1 = (List<Object>) resultMap.get("content");
            content1.addAll(content2);

            /*按操作时间降序排序*/
            Collections.sort(content1, new Comparator<Object>() {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                @Override
                public int compare(Object o1, Object o2) {
                    LinkedHashMap<String, String> tempO1 = (LinkedHashMap) o1;
                    LinkedHashMap<String, String> tempO2 = (LinkedHashMap) o2;
                    try {
                        Date date1 = format.parse(tempO1.get("czsj"));
                        Date date2 = format.parse(tempO2.get("czsj"));
                        if (date1.getTime() > date2.getTime()) {
                            return -1;
                        } else if (date1.getTime() < date2.getTime()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    } catch (Exception e) {
                        logger.error("错误原因:{}", e);
                    }
                    return 0;
                }
            });

            List<HashMap<String,HashMap>> list = (List<HashMap<String, HashMap>>) resultMap.get("content");
            try{
                if(CollectionUtils.isNotEmpty(list)){
                    Iterator<HashMap<String, HashMap>> iterator = list.iterator();
                    while(iterator.hasNext()){
                        HashMap<String, HashMap> hashMap = iterator.next();
                        HashMap cznr = hashMap.get("cznr");
                        String xgnr = (String) cznr.get("xgnr");
                        if(StringUtils.isBlank(xgnr)){
                            iterator.remove();
                        }
                    }
                }
            }
            catch (Exception e){
                logger.error("错误原因:{}", e);
            }

            Map logMap = logService.xgrz(Integer.parseInt(page), glsxid, resultMap);
            responseMessage = ResponseUtil.wrapResponseLogPage(logMap);
        } catch (Exception e) {
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    @ApiOperation(value = "查询单条日志")
    @GetMapping("/rzck/{rzid}")
    public ResponseMessage getAuditLogDetail(@PathVariable(name = "rzid") String rzid) {
        ResponseMessage responseMessage;
        try {
            responseMessage = ResponseUtil.wrapSuccessResponse();
            String uri = url + "/api/v2/logs/" + rzid;
            LinkedHashMap auditLogDto = sendGetRequest(uri);
            if (MapUtils.isNotEmpty(auditLogDto) && auditLogDto.containsKey("cznr")) {
                Map cznr = MapUtils.getMap(auditLogDto, "cznr");
                if (MapUtils.isNotEmpty(cznr) && cznr.containsKey("xgnr")) {
                    responseMessage.getData().put("czxx", JSONArray.parseArray(MapUtils.getString(cznr, "xgnr")));
                }
            }
        } catch (Exception e) {
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * 调用第三方get接口
     *
     * @param url
     */
    public <T> T sendGetRequest(String url) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.GET;

        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 执行HTTP请求，将返回的结构类格式化（
        ResponseEntity<T> response = client.exchange(url, method, null, new ParameterizedTypeReference<T>() {
        });
        Integer status = response.getStatusCodeValue();
        if (200 == status) {
            T body = response.getBody();
            logger.info("日志信息查询:日志查询成功" + body);
            return body;
        } else {
            logger.error("日志信息查询:日志查询失败" + status);
            return null;
        }
    }

    public <T> T sendPostRequest(String url, Map obj) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;

        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 将请求头部和参数合成一个请求
        HttpEntity<Map> requestEntity = new HttpEntity<>(obj, headers);

        // 执行HTTP请求，将返回的结构类格式化（
        ResponseEntity<T> response = client.exchange(url, method, requestEntity, new ParameterizedTypeReference<T>() {
        });
        Integer status = response.getStatusCodeValue();
        if (200 == status) {
            T body = response.getBody();
            logger.info("日志信息查询:日志查询成功" + body);
            return body;
        } else {
            logger.error("日志信息查询:日志查询失败" + status);
            return null;
        }

    }
}
