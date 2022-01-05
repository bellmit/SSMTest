package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.service.DchyGldwDblbService;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/1
 * @description 管理单位 管理人员登陆首页面-待办列表和已办列表
 */
@Controller
@RequestMapping("/gldwDblb")
public class DchyGldwDblbController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DchyGldwDblbService dchyGldwDblbService;

    /**
     * @param param
     * @return
     * @description 2020/12/1 管理单位待办列表台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @ResponseBody
    @PostMapping("/queryDblbList")
    public ResponseMessage getDblbByDwmcAndSqsj(@RequestBody Map<String, Object> param) {

        Map<String, Object> map = Maps.newHashMap();
        Map<String, Object> data = (Map<String, Object>) param.get("data");

        map.put("page", data.get("page"));
        map.put("size", data.get("size"));
        String dwmc = CommonUtil.formatEmptyValue(data.get("dwmc"));
        String kssj = CommonUtil.formatEmptyValue(data.get("kssj"));
        String jssj = CommonUtil.formatEmptyValue(data.get("jssj"));
        if (StringUtils.isNotBlank(dwmc)) {
            map.put("dwmc", dwmc);
        }

        if (StringUtils.isNotBlank(kssj)) {
            map.put("kssj", kssj);
        }

        if (StringUtils.isNotBlank(jssj)) {
            map.put("jssj", jssj);
        }
        map.put("userid", UserUtil.getCurrentUserId());
        Page<Map<String, Object>> dataPaging = dchyGldwDblbService.getDblbByDwmcAndSqsj(map);
        DataSecurityUtil.decryptMapList(dataPaging.getContent());
        return ResponseUtil.wrapResponseBodyByPage(dataPaging);
    }

    /**
     * @param param
     * @return
     * @description 2020/12/1 管理单位待办列表审核按钮,通过名录库id获取名录库信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/getMlkxxByMlkid")
    @ResponseBody
    public ResponseMessage getMlkxxByMlkid(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        String mlkid = CommonUtil.formatEmptyValue(data.get("mlkid"));
        if (StringUtils.isNotBlank(mlkid)) {
            try {
                mapList = dchyGldwDblbService.queryMlkxxByMlkid(mlkid);
                DataSecurityUtil.decryptMapList(mapList);
                message = ResponseUtil.wrapResponseBodyByList(mapList);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
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
     * @description 2020/12/1 管理单位待办列表审核按钮,判断能否进行审核
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/isEnterPage")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage isEnterPage(@RequestBody Map<String, Object> param) {
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        return dchyGldwDblbService.isEnterPage(data);
    }

    /**
     * @param param
     * @return
     * @description 2020/12/1 管理单位待办列表 取回
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/moveDblbxxByzrryid")
    @ResponseBody
    public ResponseMessage moveDblbxxByzrryid(@RequestBody Map<String, Object> param) {
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        String glsxid = CommonUtil.formatEmptyValue(data.get("glsxid"));
        String sqxxid = CommonUtil.formatEmptyValue(data.get("sqxxid"));
        String msg = "";
        String code = "";
        try {
            Map<String, Object> map = dchyGldwDblbService.moveDblbxxByzrryid(UserUtil.getCurrentUserId(), sqxxid, glsxid);
            msg = MapUtils.getString(map, "msg");
            code = MapUtils.getString(map, "code");
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            code = ResponseMessage.CODE.DELETE_FAIL.getCode();
            msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
        }
        return ResponseUtil.wrapResponseBodyByMsgCode(msg, code);
    }

    /**
     * @param param
     * @return
     * @description 2020/12/1 管理单位待办列表 取回
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/tjsq")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage tjsq(@RequestBody Map<String, Object> param) {
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        String sqxxid = CommonUtil.formatEmptyValue(data.get("sqxxid"));
        String sqzt = CommonUtil.formatEmptyValue(data.get("sqzt"));
        String msg = "";
        String code = "";
        try {
            Map<String, Object> map = dchyGldwDblbService.insertDbrw(UserUtil.getCurrentUserId(), sqxxid, sqzt);
            msg = MapUtils.getString(map, "msg");
            code = MapUtils.getString(map, "code");

        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            code = ResponseMessage.CODE.DELETE_FAIL.getCode();
            msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
            logger.error("错误原因:{}", e);
        }
        return ResponseUtil.wrapResponseBodyByMsgCode(msg, code);
    }
}
