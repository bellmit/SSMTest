package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjxx;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.common.util.SsmkidEnum;
import cn.gtmap.msurveyplat.serviceol.service.DchyXmglJcsjsqService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.web.util.FileUploadUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/3/8
 * @description 基础数据申请
 */
@RestController
@RequestMapping("/jcsjsq")
public class JcsjsqController {
    protected final Log logger = LogFactory.getLog(JcsjsqController.class);

    @Autowired
    private DchyXmglJcsjsqService jcsjsqService;

    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapper;

    /**
     * @param param
     * @return
     * @description 2021/3/8 基础数据申请-我的测绘项目台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "queryBasicDataApplicationInfo")
    @CheckInterfaceAuth
    public ResponseMessage queryBasicDataApplicationInfo(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = jcsjsqService.queryBasicDataApplicationInfo(data);
                logger.info(message);
            } catch (Exception e) {
                logger.error("错误原因：{}", e);
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
     * @description 2021/3/9 初始化生成基础数据申请数据
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "initBasicDataApplication")
    @CheckInterfaceAuth
    public ResponseMessage initBasicDataApplication(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = jcsjsqService.initBasicDataApplication(data);
                logger.info(message);
            } catch (Exception e) {
                logger.error("错误原因：{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * @param files
     * @param request
     * @return
     * @description 2021/3/9 文件上传
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/uploadFile")
    @CheckInterfaceAuth
    public ResponseMessage fileUpload(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        ResponseMessage message = new ResponseMessage();
        String glsxid = CommonUtil.formatEmptyValue(request.getParameter("babh"));// 备案编号或者sqxxid
        String clmc = CommonUtil.formatEmptyValue(request.getParameter("clmc"));// 材料名称
        if (StringUtils.isNoneBlank(glsxid, clmc)) {
            logger.info("***********线上基础数据申请上传数据范围的入参********" + glsxid + clmc);
            Map<String, Object> map = FileUploadUtil.uploadFile(files, glsxid, clmc);
            logger.info("***********线上基础数据申请上传数据范围的出参********" + JSON.toJSONString(map));
            String wjzxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "folderId"));
            DchyXmglSjxx dchyXmglSjxx = new DchyXmglSjxx();
            dchyXmglSjxx.setSjxxid(UUIDGenerator.generate18());
            dchyXmglSjxx.setGlsxid(glsxid);
            dchyXmglSjxx.setSsmkid(SsmkidEnum.JCSJSQ.getCode());
            dchyXmglSjxx.setSjsj(CalendarUtil.getCurHMSDate());
            dchyXmglSjxx.setTjr(CalendarUtil.getCurHMSStrDate());

            DchyXmglSjcl dchyXmglSjcl = new DchyXmglSjcl();
            dchyXmglSjcl.setSjclid(UUIDGenerator.generate18());
            dchyXmglSjcl.setSjxxid(dchyXmglSjxx.getSjxxid());
            dchyXmglSjcl.setClmc(Constants.DCHY_XMGL_JCSJSQ_CLMC);
            dchyXmglSjcl.setClrq(CalendarUtil.getCurHMSDate());
            dchyXmglSjcl.setFs(Constants.DCHY_XMGL_MRFS);
            dchyXmglSjcl.setYs(Constants.DCHY_XMGL_MRYS);
            dchyXmglSjcl.setCllx(Constants.DCHY_XMGL_SJCL_CLLX_QT);
            dchyXmglSjcl.setXh(0);
            dchyXmglSjcl.setWjzxid(wjzxid);

            int flagSjcl = entityMapper.saveOrUpdate(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
            int flagSjxx = entityMapper.saveOrUpdate(dchyXmglSjxx, dchyXmglSjxx.getSjxxid());

            if (flagSjcl > 0 && flagSjxx > 0) {
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                message = ResponseUtil.wrapExceptionResponse();
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * @param param
     * @return
     * @description 2021/3/9 查看基础数据申请详情
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("applicationInfoView")
    @CheckInterfaceAuth
    public ResponseMessage applicationInfoView(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = jcsjsqService.applicationInfoView(data);
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

    /**
     * @param param
     * @return
     * @description 2021/3/11 项目信息查看
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("myProjectInfoView")
    public ResponseMessage myProjectInfoView(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = jcsjsqService.myProjectInfoView(data);
                logger.info(message);
            } catch (Exception e) {
                logger.error("错误原因:{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * @param httpServletRequest
     * @param response
     * @return
     * @description 2021/3/9 文件下载
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/downloadFile")
    public Object download(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        ResponseMessage message = new ResponseMessage();
        String wjzxid = CommonUtil.formatEmptyValue(httpServletRequest.getParameter("wjzxid"));// 文件中心id
        if (StringUtils.isNotBlank(wjzxid)) {
            try {
                return jcsjsqService.downLoadFileByWjzxid(wjzxid, httpServletRequest, response);
            } catch (Exception e) {
                logger.error("错误原因:{}", e);
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
     * @description 2021/3/9 基础数据操作日志台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/querySqxxList")
    @CheckInterfaceAuth
    public ResponseMessage querySqxxList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = jcsjsqService.querySqxxList(data);
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

    /**
     * @param param
     * @return
     * @description 2021/3/11 成果抽查
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/resultsSpotCheck")
    public ResponseMessage resultsSpotCheck(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = jcsjsqService.querySqxxList(data);
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

    /**
     * @param param
     * @return
     * @description 2021/3/12 根据ssmkid获取需要上传的材料
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/getHtxx")
    @ResponseBody
    public ResponseMessage getHtxxForUpload(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            String ssmkId = CommonUtil.ternaryOperator(data.get("ssmkid"));
            String glsxid = CommonUtil.ternaryOperator(data.get("glsxid"));
            if (StringUtils.isNoneBlank(glsxid, ssmkId)) {
                mapList = jcsjsqService.getHtxxSjclXx(glsxid);
                if (CollectionUtils.isEmpty(mapList)) {
                    mapList = jcsjsqService.queryUploadFileBySsmkId(ssmkId);
                    jcsjsqService.initHtxxSjxxAndClxx(glsxid, ssmkId, mapList);
                    message = ResponseUtil.wrapSuccessResponse();
                    message.getData().put("dataList", mapList);
                } else {
                    message = ResponseUtil.wrapParamEmptyFailResponse();
                }
            } else {
                message = ResponseUtil.wrapParamEmptyFailResponse();
            }
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * @param param
     * @return
     * @description 2021/3/12 根据ssmkid获取需要上传的材料
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/getsjcl")
    @ResponseBody
    public ResponseMessage getSjclForUpload(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> mapListSjcl = new ArrayList<>();
        List<Map<String, Object>> mapLisHtxx = new ArrayList<>();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            String ssmkIdSjcl = SsmkidEnum.CYRY.getCode();
            String ssmkIdHtxx = SsmkidEnum.CHXMSLHTXX.getCode();
            String glsxid = CommonUtil.ternaryOperator(data.get("glsxid"));
            if (StringUtils.isNotBlank(glsxid)) {
                //获取收件材料的材料信息
                // sjcl 全部清空后 sjclid 为空
                mapListSjcl = jcsjsqService.getSjclXx(glsxid);
                /*为空的情况下初始化收件信息与材料信息*/
                if (CollectionUtils.isEmpty(mapListSjcl)) {
                    mapListSjcl = jcsjsqService.queryUploadFileBySsmkId(ssmkIdSjcl);
                    /*初始化收件信息*/
                    jcsjsqService.initSjxxAndClxx(glsxid, ssmkIdSjcl, mapListSjcl);
                } else {
                    for (Iterator<Map<String, Object>> it = mapListSjcl.iterator(); it.hasNext(); ) {
                        Map<String, Object> map = it.next();
                        String sjclid = MapUtils.getString(map, "SJCLID");
                        if (StringUtils.isBlank(sjclid)) {
                            it.remove();
                        }
                    }
                }

                //获取合同信息
                mapLisHtxx = jcsjsqService.getHtxxSjclXx(glsxid);
                if (CollectionUtils.isEmpty(mapLisHtxx)) {
                    mapLisHtxx = jcsjsqService.queryUploadFileBySsmkId(ssmkIdHtxx);
                    jcsjsqService.initHtxxSjxxAndClxx(glsxid, ssmkIdHtxx, mapLisHtxx);
                }
            }

            mapListSjcl.addAll(mapLisHtxx);

            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", mapListSjcl);
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 获取测量事项与测绘项目树形表
     */
    @PostMapping(value = "/getZdClsx")
    @ResponseBody
    @CheckInterfaceAuth
    public Object getZdClsx(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> resultMap = null;
        try {
            resultMap = jcsjsqService.getZdClsx(param);
            message = ResponseUtil.wrapResponseBodyByList(resultMap);
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * @param param
     * @return
     * @description 2021/3/15 管理单位抽查台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/evaluationCheckResults")
    @CheckInterfaceAuth
    public ResponseMessage evaluationCheckResults(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = jcsjsqService.evaluationCheckResults(data);
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

    /**
     * @param param
     * @return
     * @description 2021/3/15 管理单位抽查台账查看
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/evaluationCheckResultsView")
    @CheckInterfaceAuth
    public ResponseMessage evaluationCheckResultsView(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = jcsjsqService.evaluationCheckResultsView(data);
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

    @PostMapping(value = "/getPjSjcl")
    @CheckInterfaceAuth
    public ResponseMessage getsjcl(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = jcsjsqService.getsjcl(data);
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

    /**
     * @param param
     * @return
     * @description 2021/3/23 基础数据交付日志
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("resultsDeliveryLogList")
    public ResponseMessage resultsDeliveryLogList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();

        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                message = jcsjsqService.resultsDeliveryLogList(data);
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

    /**
     * @param param 受理编号
     * @return
     * @description 2021/3/30 通过受理编号获取流程信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("getProcessInfoBySlbh")
    public ResponseMessage getProcessInfoBySlbh(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();

        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                message = jcsjsqService.getProcessInfoBySlbh(data);
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
