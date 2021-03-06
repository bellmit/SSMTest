package cn.gtmap.msurveyplat.portalol.web.main;

import cn.gtmap.msurveyplat.common.dto.ParamDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.portalol.utils.ServiceOlFeignUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/9
 * @description 通知公告
 */
@RestController
@RequestMapping("/tzgg")
public class DchyXmglTzggglController {

    private static final Log logger = LogFactory.getLog(DchyXmglTzggglController.class);

    @Autowired
    private ServiceOlFeignUtil serviceOlFeignUtil;

    /**
     * @param param
     * @return
     * @description 2020/12/9 portalol  首页通过tzggid获取通知公告信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/getTzggByTzggid")
    public Map<String, Object> getTzggByTzggid(@RequestBody Map<String, Object> param) {
        Map mapResult = Maps.newHashMap();
        if (null != param && param.containsKey("data")) {
            Map data = MapUtils.getMap(param, "data");
            String tzggid = CommonUtil.formatEmptyValue(data.get("tzggid"));
            if (StringUtils.isNotBlank(tzggid)) {
                mapResult = serviceOlFeignUtil.queryGldwTzgggl(tzggid);
            } else {
                mapResult.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
                mapResult.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
            }
        } else {
            mapResult.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            mapResult.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return mapResult;
    }

    /**
     * @param param
     * @return
     * @description 2020/12/9 portalol  首页获取办事指南通知公告信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/getTzggByBszn")
    public ResponseMessage getTzggByBszn(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        ParamDto paramDto = new ParamDto();
        if (null != param && param.containsKey("data")) {
            Map data = MapUtils.getMap(param, "data");
            String page = MapUtils.getString(data, "page");
            String size = MapUtils.getString(data, "size");
            String title = MapUtils.getString(data, "title");
            paramDto.setPage(page);
            paramDto.setSize(size);
            paramDto.setTitle(title);

            try {
                message = serviceOlFeignUtil.getTzggByBszn(paramDto);
            } catch (Exception e) {
                logger.error("错误原因:{调用serviceOl接口异常getTzggByBszn}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }

        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * @param param
     * @return
     * @description 2020/12/9 portalol  首页获取除办事指南通知公告信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/getOtherTzgg")
    public ResponseMessage getOtherTzgg(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        ParamDto paramDto = new ParamDto();
        if (null != param && param.containsKey("data")) {
            Map data = MapUtils.getMap(param, "data");
            String page = CommonUtil.formatEmptyValue(MapUtils.getString(data, "page"));
            String size = CommonUtil.formatEmptyValue(MapUtils.getString(data, "size"));
            String title = CommonUtil.formatEmptyValue(MapUtils.getString(data, "title"));
            //是否正序
            String sfzx = CommonUtil.formatEmptyValue(MapUtils.getString(data, "sfzx"));
            paramDto.setTitle(title);
            paramDto.setPage(page);
            paramDto.setSize(size);
            paramDto.setSfzx(sfzx);
            try {
                message = serviceOlFeignUtil.getOtherTzgg(paramDto);
            } catch (Exception e) {
                logger.error("错误原因:{调用serviceOl接口异常getOtherTzgg}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }

        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }
}
