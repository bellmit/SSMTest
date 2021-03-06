package cn.gtmap.msurveyplat.portalol.utils;

import cn.gtmap.msurveyplat.common.dto.DchyXmglJsdwlrDto;
import cn.gtmap.msurveyplat.common.dto.ParamDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.portalol.feign.ServiceOlFeignService;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liujie</a>
 * @version 1.0, 2020/12/9
 * @description Exchange接口工具类
 */
@Component
public class ServiceOlFeignUtil {

    private static final Log logger = LogFactory.getLog(ServiceOlFeignUtil.class);

    /**
     * @param
     * @return
     * @author <a href="mailto:liuqiang@gtmap.cn">liujie</a>
     * @description 初始化ExchangeFeignService接口
     */
    private ServiceOlFeignService init() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(35000, 35000))
                .retryer(new Retryer.Default(35000, 35000, 3))
                .target(ServiceOlFeignService.class, AppConfig.getProperty("serviceol.url"));
//                .target(ServiceOlFeignService.class, AppConfig.getProperty("192.168.50.60:8087/msurveyplat-serviceol"));
    }

    /**
     * @param tzggid
     * @return
     * @description 2020/12/9 通过通知公告id获取通知公告信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public Map<String, Object> queryGldwTzgggl(String tzggid) {
        Map resultMap = Maps.newHashMap();
        ServiceOlFeignService serviceOlFeignService = init();
        try {
            resultMap = serviceOlFeignService.queryGldwTzgggl(tzggid);
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            resultMap.put("code", ResponseMessage.CODE.EXCEPTION_MGS.getCode());
            resultMap.put("msg", ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
        }
        return resultMap;
    }

    /**
     * @param paramDto
     * @return
     * @description 2020/12/9 通过通知公告id获取通知公告信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public ResponseMessage getTzggByBszn(ParamDto paramDto) {
        ResponseMessage message = new ResponseMessage();
        Map resultMap = Maps.newHashMap();
        ServiceOlFeignService serviceOlFeignService = init();
        try {
            message = serviceOlFeignService.getTzggByBszn(paramDto);
        } catch (Exception e) {
            logger.error("错误原因{}" + e.getMessage(), e);
            resultMap.put("code", ResponseMessage.CODE.EXCEPTION_MGS.getCode());
            resultMap.put("msg", ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
            message = ResponseUtil.wrapResponseBodyByCodeMap(resultMap);
        }
        return message;
    }

    /**
     * @param paramDto
     * @return
     * @description 2020/12/9 通过通知公告id获取通知公告信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public ResponseMessage getOtherTzgg(ParamDto paramDto) {
        ResponseMessage message = new ResponseMessage();
        Map resultMap = Maps.newHashMap();
        ServiceOlFeignService serviceOlFeignService = init();
        try {
            message = serviceOlFeignService.getOtherTzgg(paramDto);
        } catch (Exception e) {
            logger.error("错误原因{}" + e.getMessage(), e);
            resultMap.put("code", ResponseMessage.CODE.EXCEPTION_MGS.getCode());
            resultMap.put("msg", ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
            message = ResponseUtil.wrapResponseBodyByCodeMap(resultMap);
        }
        return message;
    }

    /**
     * @param ssmkId
     * @param glsxid
     * @return
     * @description 2020/12/12 通过所属模块id获取收件材料
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public ResponseMessage getsjcl(String ssmkId, String glsxid) {
        ResponseMessage message = new ResponseMessage();
        ServiceOlFeignService serviceOlFeignService = init();
        try {
            message = serviceOlFeignService.getsjcl(ssmkId, glsxid);
        } catch (Exception e) {
            logger.error("错误原因{}" + e.getMessage(), e);
            message.getHead().setMsg(ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
            message.getHead().setCode(ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return message;
    }

    /**
     * @param dchyXmglJsdwlrDto
     * @return
     * @description 2020/12/9 通过通知公告id获取通知公告信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public ResponseMessage pushJsdwlrDataMsg(DchyXmglJsdwlrDto dchyXmglJsdwlrDto) {
        ResponseMessage message = new ResponseMessage();
        Map resultMap = Maps.newHashMap();
        ServiceOlFeignService serviceOlFeignService = init();
        try {
            message = serviceOlFeignService.pushJsdwlrDataMsg(dchyXmglJsdwlrDto);
        } catch (Exception e) {
            logger.error("错误原因{}" + e.getMessage(), e);
            resultMap.put("code", ResponseMessage.CODE.EXCEPTION_MGS.getCode());
            resultMap.put("msg", ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
            message = ResponseUtil.wrapResponseBodyByCodeMap(resultMap);
        }
        return message;
    }


    /**
     * @return cn.gtmap.msurveyplat.common.dto.ResponseMessage
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: map
     * @time 2021/5/14 17:07
     * @description 业务逻辑验证
     */
    public ResponseMessage excuteYwljyz(Map map) {
        ResponseMessage message;
        ServiceOlFeignService serviceOlFeignService = init();
        try {
            message = serviceOlFeignService.excuteYwljyz(map);
        } catch (Exception e) {
            logger.error("错误原因{serviceOl接口调用异常,接口名称excuteYwljyz}" + e.getMessage(), e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * @return cn.gtmap.msurveyplat.common.dto.ResponseMessage
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: map
     * @time 2021/5/14 17:07
     * @description 表单必填验证
     */
    public ResponseMessage getBdbtxyzList(Map map) {
        ResponseMessage message;
        ServiceOlFeignService serviceOlFeignService = init();
        try {
            message = serviceOlFeignService.getBdbtxyzList(map);
        } catch (Exception e) {
            logger.error("错误原因{serviceOl接口调用异常,接口名称getBdbtxyzList}" + e.getMessage(), e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }
}
