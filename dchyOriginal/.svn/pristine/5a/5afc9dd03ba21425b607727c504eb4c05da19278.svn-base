package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglMlkMapper;
import cn.gtmap.msurveyplat.serviceol.service.DchyXmglReleaseEntrustService;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2021/02/22 15:51
 * @description 建设单位发布委托
 */
@RestController
@RequestMapping(value = "entrust")
public class DchyXmglReleaseEntrustController {

    private Logger logger = LoggerFactory.getLogger(DchyXmglReleaseEntrustController.class);

    @Autowired
    private DchyXmglReleaseEntrustService dchyXmglReleaseEntrustService;
    @Autowired
    private DchyXmglMlkMapper dchyXmglMlkMapper;

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 建设单位查看发布的委托
     */
    @PostMapping(value = "/getEntrustByPage")
    @ResponseBody
    @CheckInterfaceAuth
    public Object getEntrustByPage(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = null;
        try {
            map.put("userid", UserUtil.getCurrentUserId());
            Page<Map<String, Object>> dataList = dchyXmglReleaseEntrustService.getEntrustByPage(map);
            if (dataList != null) {
                DataSecurityUtil.decryptMapList(dataList.getContent());
                responseMessage = ResponseUtil.wrapResponseBodyByPage(dataList);
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 点击查看详情
     */
    @RequestMapping(value = "/queryEntrustByChxmid")
    @ResponseBody
    @CheckInterfaceAuth
    public Object queryEntrustByChxmid(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            List<Map<String, Object>> dataList = dchyXmglReleaseEntrustService.queryEntrustByChxmid(map);
            responseMessage = ResponseUtil.wrapResponseBodyByList(dataList);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    @GetMapping(value = "/initEntrust")
    @ResponseBody
    @CheckInterfaceAuth
    public Object initJsdwFbxq() {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> resultMap = dchyXmglReleaseEntrustService.initEntrust();
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().putAll(resultMap);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    @PostMapping(value = "/deleteEntrust")
    @ResponseBody
    @CheckInterfaceAuth
    public Object deleteJsdwFbxq(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> resultMap = dchyXmglReleaseEntrustService.deleteEntrust(map);
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getHead().setMsg(MapUtils.getString(resultMap, "msg"));
            responseMessage.getHead().setCode(MapUtils.getString(resultMap, "code"));
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 建设单位发布委托
     */
    @PostMapping(value = "/saveEntrust")
    @ResponseBody
    @CheckInterfaceAuth
    public Object saveEntrust(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            dchyXmglReleaseEntrustService.saveEntrust(map);
            responseMessage = ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 建设单位发布委托取回
     */
    @PostMapping(value = "/retrieveEntrust")
    @ResponseBody
    @CheckInterfaceAuth
    public Object retrieveEntrust(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            boolean ishy = dchyXmglReleaseEntrustService.retrieveEntrust(map);
            Map<String, Object> resultMap = Maps.newHashMap();
            resultMap.put("ISHY", ishy);
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.setData(resultMap);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 初始化加载测绘单位信息
     */
    @PostMapping(value = "/queryChdwList")
    @ResponseBody
    @CheckInterfaceAuth
    public Object queryChdwList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");

            List<Map<String, Object>> resultList = Lists.newArrayList();
            try {
                List<String> clsxList = (List<String>) data.get("clsxList");

                List<Map<String, Object>> dataList = dchyXmglMlkMapper.queryChdwList();
                //过滤测绘单位
                if (CollectionUtils.isNotEmpty(dataList)) {
                    for (Map<String, Object> map : dataList) {
                        boolean flag = true;
                        String clsx = CommonUtil.formatEmptyValue(MapUtils.getString(map, "CLSX"));
                        //组织clsx
                        String[] clsxs = clsx.split(",");
                        List<String> clsxListByChdw = Lists.newArrayList();
                        if (clsxs.length > 0) {
                            for (String str : clsxs) {
                                if (!clsxListByChdw.contains(str)) {
                                    clsxListByChdw.add(str);
                                }
                            }
                        }

                        if (CollectionUtils.isNotEmpty(clsxList) && CollectionUtils.isNotEmpty(clsxListByChdw)) {
                            for (String c : clsxList) {
                                if (!clsxListByChdw.contains(c)) {
                                    flag = false;
                                    break;
                                }
                            }
                        }
                        if (flag) {
                            resultList.add(map);
                        }
                    }
                }
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("dataList", resultList);

            } catch (Exception e) {
                logger.error("错误信息:{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;

    }

}
