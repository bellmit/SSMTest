package cn.gtmap.msurveyplat.serviceol.web;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.service.ChdwXmcxService;
import cn.gtmap.msurveyplat.serviceol.service.JsdwFbxqglService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-01
 * description
 */
@Controller
@RequestMapping("/chdwxmcx")
public class ChdwXmcxController {

    @Autowired
    ChdwXmcxService chdwXmcxService;

    @Autowired
    JsdwFbxqglService jsdwFbxqglService;

    private Logger logger = LoggerFactory.getLogger(ChdwXmcxController.class);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 测绘单位查看项目
     */
    @RequestMapping(value = "/getChdwXmcxList")
    @ResponseBody
    @CheckInterfaceAuth
    public Object getChdwXmcxList(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            Page<Map<String, Object>> dchyXmglXmcx = chdwXmcxService.getChdwXmcxList(map);
            DataSecurityUtil.decryptMapList(dchyXmglXmcx.getContent());
            responseMessage = ResponseUtil.wrapResponseBodyByPage(dchyXmglXmcx);
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 点击查看，根据测绘工程ID查看详情
     */
    @RequestMapping(value = "/getXmcxxqList")
    @ResponseBody
    @CheckInterfaceAuth
    public Object getXmcxxqList(@RequestBody Map map) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            List<Map<String, Object>> dchyXmgl = chdwXmcxService.getXmcxxqList(map);
            responseMessage = ResponseUtil.wrapResponseBodyByList(dchyXmgl);
            responseMessage.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getMsg());
            responseMessage.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getCode());
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            responseMessage.getHead().setMsg(ResponseMessage.CODE.QUERY_FAIL.getMsg());
            responseMessage.getHead().setCode(ResponseMessage.CODE.QUERY_FAIL.getCode());
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 我的测绘项目
     */
    @RequestMapping(value = "/chdwCkxm")
    @ResponseBody
    @CheckInterfaceAuth
    public Object getChdwCkxmList(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            Page<Map<String, Object>> chxmList = chdwXmcxService.getChdwCkxmList(map);
            if (chxmList != null) {
                DataSecurityUtil.decryptMapList(chxmList.getContent());
                responseMessage = ResponseUtil.wrapResponseBodyByPage(chxmList);
            }
            responseMessage.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getMsg());
            responseMessage.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            responseMessage.getHead().setMsg(ResponseMessage.CODE.QUERY_FAIL.getMsg());
            responseMessage.getHead().setCode(ResponseMessage.CODE.QUERY_FAIL.getCode());
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 正在进行中我的测绘项目
     */
    @RequestMapping(value = "/chdwCkxmCount")
    @ResponseBody
    @CheckInterfaceAuth
    public Object chdwCkxmCount() {
        ResponseMessage responseMessage = new ResponseMessage();
        String chxmsl = chdwXmcxService.queryChxmCount();
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("chxmsl", chxmsl);
        if (StringUtils.isNotBlank(chxmsl)) {
            responseMessage.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getMsg());
            responseMessage.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
            responseMessage.setData(resultMap);
        } else {
            responseMessage.getHead().setCode(ResponseMessage.CODE.FILENOTFOUND_FAIL.getCode());
            responseMessage.getHead().setMsg(ResponseMessage.CODE.FILENOTFOUND_FAIL.getMsg());
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 委托双方信息
     */
    @RequestMapping(value = "/queryWtxxByChdwxxid")
    @ResponseBody
    @CheckInterfaceAuth
    public Object queryWtxxByChdwxxid(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            logger.info("***********查看项目入参***********" + JSON.toJSONString(map));
            List<Map<String, Object>> dataList = chdwXmcxService.queryWtxxByChdwxxid(map);
            logger.info("***********查看项目出参***********" + JSON.toJSONString(dataList));
            responseMessage = ResponseUtil.wrapResponseBodyByList(dataList);
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 测绘单位办理信息
     */
    @RequestMapping(value = "/queryChdwxxByChdwxxid")
    @ResponseBody
    @CheckInterfaceAuth
    public Object queryChdwxxByChdwxxid(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            List<Map<String, Object>> dataList = chdwXmcxService.queryChdwxxByChdwxxid(map);
            responseMessage = ResponseUtil.wrapResponseBodyByList(dataList);
            responseMessage.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getMsg());
            responseMessage.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getCode());
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            responseMessage.getHead().setMsg(ResponseMessage.CODE.QUERY_FAIL.getMsg());
            responseMessage.getHead().setCode(ResponseMessage.CODE.QUERY_FAIL.getCode());
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 委托单位办理信息
     */
    @RequestMapping(value = "/queryQtblxxByChxmid")
    @ResponseBody
    @CheckInterfaceAuth
    public Object queryQtblxxByChxmid(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            List<Map<String, Object>> dataList = chdwXmcxService.queryQtblxxByChxmid(map);
            responseMessage = ResponseUtil.wrapResponseBodyByList(dataList);
            responseMessage.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getMsg());
            responseMessage.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getCode());
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            responseMessage.getHead().setMsg(ResponseMessage.CODE.QUERY_FAIL.getMsg());
            responseMessage.getHead().setCode(ResponseMessage.CODE.QUERY_FAIL.getCode());
        }
        return responseMessage;
    }

    /**
     * @return
     * @parammap
     * @description 2020/12/10 建设单位  我的项目   合同下载
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @RequestMapping(value = "/chdwHtDownload")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage chdwHtDownload(@RequestBody Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();

        Map<String, Object> data = (Map<String, Object>) map.get("data");

        String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));
        String mlkid = CommonUtil.formatEmptyValue(data.get("mlkid"));

        List<String> wjzxidList = chdwXmcxService.queryHtxxByChxmidAndHtxxid(chxmid, mlkid);
        if (CollectionUtils.isNotEmpty(wjzxidList)) {
            Map<String, Object> result = Maps.newHashMap();
            for (String wjzxidLists : wjzxidList) {
                result.put("WJZXID", wjzxidLists);
            }
            message = ResponseUtil.wrapSuccessResponse();
            message.setData(result);
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.FILENOTFOUND_FAIL.getMsg(), ResponseMessage.CODE.FILENOTFOUND_FAIL.getCode());
        }
        return message;
    }

}
