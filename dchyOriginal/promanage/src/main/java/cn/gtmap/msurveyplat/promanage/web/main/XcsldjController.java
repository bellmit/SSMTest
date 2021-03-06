package cn.gtmap.msurveyplat.promanage.web.main;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.service.XcsldjDtoService;
import cn.gtmap.msurveyplat.promanage.core.service.XcsldjService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-01
 * description
 */
@RestController
@RequestMapping("/xcsldj")
public class XcsldjController {

    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    XcsldjService xcsldjService;
    @Autowired
    XcsldjDtoService xcsldjDtoService;

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 初始化现场受理登记测绘项目ID
     */
    @GetMapping(value = "/initXcsldj")
    @SystemLog(czmkMc = ProLog.CZMK_DBAXX_MC, czmkCode = ProLog.CZMK_DBAXX_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    public Object initXcsldj() {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> resultMap = xcsldjService.initXcsldj();
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().putAll(resultMap);
        } catch (Exception e) {
            logger.error("错误原因{}：", e);
            responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 初始化加载测绘单位信息
     */
    @SystemLog(czmkMc = ProLog.CZMK_DBAXX_MC, czmkCode = ProLog.CZMK_DBAXX_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    @PostMapping(value = "/queryChdwList")
    public Object queryChdwList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> resultList = Lists.newArrayList();
        if (null != param && param.containsKey("data")) {
            Map data = (Map) param.get("data");
            List<String> clsxList = (List<String>) data.get("clsxList");
            try {
                List<Map<String, Object>> dataList = xcsldjService.queryChdwList();
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

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 手动录入测绘单位
     */
    @SystemLog(czmkMc = ProLog.CZMK_DBAXX_MC, czmkCode = ProLog.CZMK_DBAXX_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    @PostMapping(value = "/saveChdw")
    public Object saveChdw(@RequestBody Map map) {
        ResponseMessage responseMessage;
        try {
            Map result = xcsldjService.saveChdw(map);
            responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(MapUtils.getString(result, "msg"), MapUtils.getString(result, "code"));
        } catch (Exception e) {
            logger.error("错误原因{}：", e);
            responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 保存待办备案信息
     */
    @PostMapping(value = "/saveJsdwFbxq")
    @SystemLog(czmkMc = ProLog.CZMK_DBAXX_MC, czmkCode = ProLog.CZMK_DBAXX_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    public Object saveJsdwFbxq(@RequestBody Map map) {
        ResponseMessage responseMessage;
        try {
            boolean resultList = xcsldjDtoService.saveXcsldjCs(map);
            int myInt = resultList ? 1 : 0;
            responseMessage = ResponseUtil.wrapResponseBody(myInt);
        } catch (Exception e) {
            logger.error("错误原因{}：", e);
            responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 查询备案登记信息
     */
    @RequestMapping(value = "/queryBaXmxx")
    @SystemLog(czmkMc = ProLog.CZMK_DBAXX_MC, czmkCode = ProLog.CZMK_DBAXX_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    public Object queryBaXmXxByArgs(@RequestBody Map map) {
        ResponseMessage responseMessage;
        try {
            List<Map<String, Object>> dataList = xcsldjService.queryXcsldj(map);
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().put("dataLsit", dataList);
        } catch (Exception e) {
            logger.error("错误原因{}：", e);
            responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.QUERY_FAIL.getCode());
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 删除现场受理登记
     */
    @PostMapping(value = "/deleteXcsldj")
    @SystemLog(czmkMc = ProLog.CZMK_DBAXX_MC, czmkCode = ProLog.CZMK_DBAXX_CODE, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    public Object deleteXcsldj(@RequestBody Map map) {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> resultMap = xcsldjService.deleteXcsldjByChxmid(map);
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getHead().setMsg(MapUtils.getString(resultMap, "msg"));
            responseMessage.getHead().setCode(MapUtils.getString(resultMap, "code"));
        } catch (Exception e) {
            logger.error("错误原因{}：", e);
            responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 根据工程编号查询相关测绘信息
     */
    @PostMapping(value = "/queryChxxByChgcbh")
    @SystemLog(czmkMc = ProLog.CZMK_DBAXX_MC, czmkCode = ProLog.CZMK_DBAXX_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    public Object queryChxxByChgcbh(@RequestBody Map map) {
        ResponseMessage message = new ResponseMessage();
        try {
            Map<String, Object> dataList = xcsldjService.queryChxxByChgcbh(map);
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().putAll(dataList);
        } catch (Exception e) {
            logger.error("错误原因{}：", e);
            message = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.QUERY_FAIL.getCode());

        }
        return message;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 根据需求发布编号查询测绘项目信息
     */
    @PostMapping(value = "/queryChxmByXqfbbh")
    @SystemLog(czmkMc = ProLog.CZMK_DBAXX_MC, czmkCode = ProLog.CZMK_DBAXX_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    public Object queryChxmByXqfbbh(@RequestBody Map map) {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> dataList = xcsldjService.queryChxmByXqfbbh(map);
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().putAll(dataList);
        } catch (Exception e) {
            logger.error("错误原因{}：", e);
            responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.EXCEPTION_MGS.getMsg(), ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 根据工程编号查询备案信息
     */
    @PostMapping(value = "/queryBaxxByChxmid")
    @SystemLog(czmkMc = ProLog.CZMK_DBAXX_MC, czmkCode = ProLog.CZMK_DBAXX_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    public Object queryBaxxByChxmid(@RequestBody Map map) {
        ResponseMessage message = new ResponseMessage();
        try {
            Map<String, Object> dataList = xcsldjService.queryBaxxByChxmid(map);
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().putAll(dataList);
        } catch (Exception e) {
            logger.error("错误原因{}：", e);
            message = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.QUERY_FAIL.getCode());
        }
        return message;
    }


    @PostMapping(value = "/saveJsdw")
    @SystemLog(czmkMc = ProLog.CZMK_DBAXX_MC, czmkCode = ProLog.CZMK_DBAXX_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    public ResponseMessage saveJsdw(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = xcsldjService.saveJsdw(data);
                logger.info(message);
            } catch (Exception e) {
                logger.error("错误原因{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }


}
