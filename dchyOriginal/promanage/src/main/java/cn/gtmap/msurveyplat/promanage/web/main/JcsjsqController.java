package cn.gtmap.msurveyplat.promanage.web.main;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjxx;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.*;
import cn.gtmap.msurveyplat.promanage.service.DchyXmglJcsjsqService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.PlatformUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.FileDownoadUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.FileUploadUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipOutputStream;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/3/8
 * @description 基础数据申请
 */
@RestController
@RequestMapping("/jcsjsq")
public class JcsjsqController {

    //设置核心池大小
    int corePoolSize = 20;
    //设置线程池最大能接受多少线程
    int maximumPoolSize = 200;
    //当前线程数大于corePoolSize、小于maximumPoolSize时，超出corePoolSize的线程数的生命周期
    long keepActiveTime = 200;
    //设置时间单位，秒
    TimeUnit timeUnit = TimeUnit.SECONDS;
    //设置线程池缓存队列的排队策略为FIFO，并且指定缓存队列大小为300
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(300);
    //创建ThreadPoolExecutor线程池对象，并初始化该对象的各种参数
    ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepActiveTime, timeUnit, workQueue);


    Object o = new Object();

    protected final Log logger = LogFactory.getLog(JcsjsqController.class);

    @Autowired
    private DchyXmglJcsjsqService dchyXmglJcsjsqService;

    @Autowired
    private PlatformUtil platformUtil;

    @Autowired
    private EntityMapper entityMapper;

    @GetMapping(value = "/initSqxx")
    public ResponseMessage initSqxx() {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> map = Maps.newHashMap();
        String sqxxid = dchyXmglJcsjsqService.initSqxx();
        if (StringUtils.isNotBlank(sqxxid)) {
            map.put("sqxxid", sqxxid);
            message = ResponseUtil.wrapSuccessResponse();
            message.setData(map);
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.INIT_FAIL.getMsg(), ResponseMessage.CODE.INIT_FAIL.getCode());
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
    @SystemLog(czmkMc = ProLog.CZMC_JCSJSQ_MC, czmkCode = ProLog.CZMC_JCSJSQ_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_JCSJSQ_CODE)
    public ResponseMessage initBasicDataApplication(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = dchyXmglJcsjsqService.initBasicDataApplication(data);
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
     * @param files
     * @param request
     * @return
     * @description 2021/3/9 文件上传
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/uploadFile")
    @SystemLog(czmkMc = ProLog.CZMC_JCSJSQ_MC, czmkCode = ProLog.CZMC_JCSJSQ_CODE, czlxMc = ProLog.CZLX_UPLOAD_MC, czlxCode = ProLog.CZLX_UPLOAD_CODE, ssmkid = ProLog.SSMKID_JCSJSQ_CODE)
    public synchronized ResponseMessage fileUpload(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        Map<String, String> resultMap = Maps.newHashMap();
        String glsxid = CommonUtil.formatEmptyValue(request.getParameter("glsxid"));// 备案编号或者sqxxid
        String clmc = CommonUtil.formatEmptyValue(request.getParameter("clmc"));// 材料名称
        if (StringUtils.isNoneBlank(glsxid, clmc)) {
            synchronized (o) {
//                logger.info("child thread: holdLock: " + Thread.holdsLock(o));
                if (Thread.holdsLock(o)) {
                    Map<String, Object> map = FileUploadUtil.uploadFile(files, glsxid, clmc);
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
                    dchyXmglSjcl.setClmc(clmc);
                    dchyXmglSjcl.setClrq(CalendarUtil.getCurHMSDate());
                    dchyXmglSjcl.setFs(Constants.DCHY_XMGL_MRFS);
                    dchyXmglSjcl.setYs(Constants.DCHY_XMGL_MRYS);
                    dchyXmglSjcl.setCllx(Constants.DCHY_XMGL_SJCL_CLLX_QT);
                    dchyXmglSjcl.setXh(Constants.DCHY_XMGL_MRXH);
                    dchyXmglSjcl.setWjzxid(wjzxid);

                    int flagSjcl = entityMapper.saveOrUpdate(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
                    int flagSjxx = entityMapper.saveOrUpdate(dchyXmglSjxx, dchyXmglSjxx.getSjxxid());

                    if (flagSjcl > 0 && flagSjxx > 0) {
                        resultMap.put("code", ResponseMessage.CODE.SUCCESS.getCode());
                        resultMap.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
                    } else {
                        resultMap.put("code", ResponseMessage.CODE.EXCEPTION_MGS.getCode());
                        resultMap.put("msg", ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
                    }
                } else {
                    resultMap.put("code", ResponseMessage.CODE.PROCESSOCCUPATION_FAIL.getCode());
                    resultMap.put("msg", ResponseMessage.CODE.PROCESSOCCUPATION_FAIL.getMsg());
                }
            }
        } else {
            resultMap.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            resultMap.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return ResponseUtil.wrapResponseBodyByCodeMap(resultMap);

    }

    /**
     * @param param
     * @return
     * @description 2021/3/9 基础数据操作待办台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/queryDbSqxxList")
    @SystemLog(czmkMc = ProLog.CZMC_JCSJSQ_MC, czmkCode = ProLog.CZMC_JCSJSQ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_JCSJSQ_CODE)
    public ResponseMessage queryDbSqxxList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = dchyXmglJcsjsqService.queryDbSqxxList(data);
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
     * @description 2021/4/13 基础数据操作已办台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/queryYbSqxxList")
    @SystemLog(czmkMc = ProLog.CZMC_JCSJSQ_MC, czmkCode = ProLog.CZMC_JCSJSQ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_JCSJSQ_CODE)
    public ResponseMessage queryYbSqxxList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = dchyXmglJcsjsqService.queryYbSqxxList(data);
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
     * @description 2021/3/9 查看基础数据申请详情
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("applicationInfoView")
    @SystemLog(czmkMc = ProLog.CZMC_JCSJSQ_MC, czmkCode = ProLog.CZMC_JCSJSQ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_JCSJSQ_CODE)
    public ResponseMessage applicationInfoView(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = dchyXmglJcsjsqService.applicationInfoView(data);
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
     * @description 2021/3/23 审核
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("saveCheckOpinion")
    @SystemLog(czmkMc = ProLog.CZMC_JCSJSQ_MC, czmkCode = ProLog.CZMC_JCSJSQ_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_JCSJSQ_CODE)
    public ResponseMessage saveCheckOpinion(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                synchronized (o) {
//                    logger.info("child thread: holdLock: " + Thread.holdsLock(o));
                    if (Thread.holdsLock(o)) {
                        message = dchyXmglJcsjsqService.saveCheckOpinion(data);
                    } else {
                        message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.PROCESSOCCUPATION_FAIL.getMsg(), ResponseMessage.CODE.PROCESSOCCUPATION_FAIL.getCode());
                    }
                }
//                message = dchyXmglJcsjsqService.saveCheckOpinion(data);
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
     * @description 2021/3/23 申请时交付成果
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("resultsDelivery")
    @SystemLog(czmkMc = ProLog.CZMC_JCSJSQ_MC, czmkCode = ProLog.CZMC_JCSJSQ_CODE, czlxMc = ProLog.CZLX_DOWNLOAD_MC, czlxCode = ProLog.CZLX_DOWNLOAD_CODE, ssmkid = ProLog.SSMKID_JCSJSQ_CODE)
    public ResponseMessage resultsDelivery(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> resultMap = Maps.newHashMap();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                synchronized (o) {
//                    logger.info("child thread: holdLock: " + Thread.holdsLock(o));
                    if (Thread.holdsLock(o)) {
                        resultMap.put("wjzxid", dchyXmglJcsjsqService.resultsDelivery(data));
                        message = ResponseUtil.wrapSuccessResponse();
                        message.setData(resultMap);
                    } else {
                        message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.PROCESSOCCUPATION_FAIL.getMsg(), ResponseMessage.CODE.PROCESSOCCUPATION_FAIL.getCode());
                    }
                }
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
    @SystemLog(czmkMc = ProLog.CZMC_JCSJSQ_MC, czmkCode = ProLog.CZMC_JCSJSQ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_JCSJSQ_CODE)
    public ResponseMessage resultsDeliveryLogList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();

        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                message = dchyXmglJcsjsqService.resultsDeliveryLogList(data);
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
    @SystemLog(czmkMc = ProLog.CZMC_JCSJSQ_MC, czmkCode = ProLog.CZMC_JCSJSQ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_JCSJSQ_CODE)
    public ResponseMessage getProcessInfoBySlbh(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();

        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                message = dchyXmglJcsjsqService.getProcessInfoBySlbh(data);
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


    //*******************************成果抽查*********************************************************

    /**
     * @param param
     * @return
     * @description 2021/3/11 随机抽查
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/resultsspotrandomcheck")
    @SystemLog(czmkMc = ProLog.CZMC_CGCC_MC, czmkCode = ProLog.CZMC_CGCC_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_CGCCPJ_CODE)
    public ResponseMessage resultsSpotRandomCheck(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                List<Map<String, Object>> spotRandCheck = dchyXmglJcsjsqService.resultsSpotRandCheck(data);
                return ResponseUtil.wrapResponseBodyByList(spotRandCheck);
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
     * @description 2021/3/12 抽查列表台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/queryresultsspotchecklist")
    @SystemLog(czmkMc = ProLog.CZMC_CGPJ_MC, czmkCode = ProLog.CZMC_CGPJ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_CGCCPJ_CODE)
    public ResponseMessage queryResultsSpotCheckList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                Page<Map<String, Object>> spotCheckList = dchyXmglJcsjsqService.queryResultsSpotCheckList(data);
                return ResponseUtil.wrapResponseBodyByPage(spotCheckList);
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
     * 抽查成果包下载
     *
     * @param
     * @return
     */
    @GetMapping(value = "/achievementdownloadbybabh")
    @Transactional(rollbackFor = {Exception.class})
    @SystemLog(czmkMc = ProLog.CZMC_CGPJ_MC, czmkCode = ProLog.CZMC_CGPJ_CODE, czlxMc = ProLog.CZLX_DOWNLOAD_MC, czlxCode = ProLog.CZLX_DOWNLOAD_CODE, ssmkid = ProLog.SSMKID_CGCCPJ_CODE)
    public ResponseEntity<byte[]> achievementdownloadbybabh(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String babh = httpServletRequest.getParameter("babh");
        String cgccid = httpServletRequest.getParameter("cgccid");
        if (StringUtils.isNotBlank(babh)) {
            try {
                Integer parentId = platformUtil.creatNode(babh);
                ResponseEntity<byte[]> responseEntity = this.downFilesByFileCenterId(parentId + "", httpServletRequest, response);
                if (null != responseEntity && (responseEntity.getStatusCodeValue() != HttpStatus.EXPECTATION_FAILED.value())) {
                    /*更新抽查表*/
                    dchyXmglJcsjsqService.updateCgcc(cgccid);
                }
                return responseEntity;
            } catch (Exception e) {
                logger.error("错误原因:{}", e);
            }
        }
        return null;
    }

    /**
     * 成果评价
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/achievementevaluation")
    @SystemLog(czmkMc = ProLog.CZMC_CGPJ_MC, czmkCode = ProLog.CZMC_CGPJ_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_CGCCPJ_CODE)
    public ResponseMessage achievementEvaluation(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            int result = dchyXmglJcsjsqService.achievementEvaluation(data);
            if (result > 0) {
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.ACHIEVEMENT_EVALUATION_FAIL.getMsg(), ResponseMessage.CODE.ACHIEVEMENT_EVALUATION_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 抽查评价详情
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/spotcheckevaluationdetails")
    @SystemLog(czmkMc = ProLog.CZMC_CGPJ_MC, czmkCode = ProLog.CZMC_CGPJ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_CGCCPJ_CODE)
    public ResponseMessage spotCheckEvaluationDetails(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            List<Map<String, Object>> map = dchyXmglJcsjsqService.spotCheckEvaluationDetails(data);
            return ResponseUtil.wrapResponseBodyByList(map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 抽查评价时文件上传
     *
     * @param files
     */
    @PostMapping(value = "/cguploadfiles")
    @ResponseBody
    @SystemLog(czmkMc = ProLog.CZMC_CGPJ_MC, czmkCode = ProLog.CZMC_CGPJ_CODE, czlxMc = ProLog.CZLX_UPLOAD_MC, czlxCode = ProLog.CZLX_UPLOAD_CODE, ssmkid = ProLog.SSMKID_CGCCPJ_CODE)
    public ResponseMessage cgFileUpload(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        String ssmkid = CommonUtil.ternaryOperator(request.getParameter("ssmkid"));
        String glsxid = CommonUtil.ternaryOperator(request.getParameter("glsxid"));// ccgcid
        String cllx = CommonUtil.ternaryOperator(request.getParameter("cllx"));
        String fs = CommonUtil.ternaryOperator(request.getParameter("fs"));
        String ys = CommonUtil.ternaryOperator(request.getParameter("ys"));
        String sjclid = CommonUtil.ternaryOperator(request.getParameter("sjclid"));
        String clmc = CommonUtil.ternaryOperator(request.getParameter("clmc"));
        String sjxxid = CommonUtil.ternaryOperator(request.getParameter("sjxxid"));
        String xh = CommonUtil.ternaryOperator(request.getParameter("xh"));
        /*文件上传*/
        Map<String, Object> map = FileUploadUtil.uploadFile(files, glsxid, clmc);
        map.put("ssmkid", ssmkid);
        map.put("glsxid", glsxid);
        map.put("cllx", cllx);
        map.put("fs", fs);
        map.put("ys", ys);
        map.put("sjclid", sjclid);
        map.put("xh", xh);
        map.put("clmc", clmc + querySuffix(files));
        map.put("sjxxid", sjxxid);
        return FileUploadUtil.updateSjxxAndClxxByCgcc(map);
    }

    /**
     * 通过wjzxid下载文件
     *
     * @param wjzxid
     * @param httpServletRequest
     * @param response
     * @return
     * @throws Exception
     */
    @SystemLog(czmkMc = ProLog.CZMC_CGPJ_MC, czmkCode = ProLog.CZMC_CGPJ_CODE, czlxMc = ProLog.CZLX_DOWNLOAD_MC, czlxCode = ProLog.CZLX_DOWNLOAD_CODE, ssmkid = ProLog.SSMKID_CGCCPJ_CODE)
    public ResponseEntity<byte[]> downFilesByFileCenterId(String wjzxid, HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        // 构建响应
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

        // 二进制数据流
        bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);

        int nodeId = Integer.parseInt(wjzxid);
        String fileName = "材料.zip";
        response.reset();//清空缓存区，防止存在某些字符使得下载的文件格式错误
        response.setContentType("application/octet-stream");

        ZipOutputStream zos = null;
        HttpStatus httpState = null;
        byte[] body = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {

            int num = FileDownoadUtil.getFileNumberByNodeId(nodeId);
            Map<String, Object> wjxzMap = Maps.newHashMap();
            if (num == 0) {
                httpState = HttpStatus.NOT_FOUND;
            } else {

                if (num == 1) {
                    wjxzMap.putAll(FileDownoadUtil.downLoadFile(nodeId));
                    if (MapUtils.isNotEmpty(wjxzMap)) {
                        fileName = MapUtils.getString(wjxzMap, "wjmc");
                        body = (byte[]) wjxzMap.get("wjnr");
                    }
                } else {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    zos = new ZipOutputStream(byteArrayOutputStream);
                    fileName = FileDownoadUtil.downLoadZip(zos, nodeId, null,null);
                    zos.finish();
                    body = byteArrayOutputStream.toByteArray();
                    fileName += ".zip";
                }

                fileName = FileDownoadUtil.encodeFileName(httpServletRequest, fileName);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                headers.add("Content-Disposition", "attachment; " + fileName);
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");
                headers.add("Last-Modified", new Date().toString());
                headers.add("ETag", String.valueOf(System.currentTimeMillis()));
                return org.springframework.http.ResponseEntity
                        .ok()
                        .headers(headers)
                        .contentLength(body.length)
                        .contentType(MediaType.parseMediaType("application/octet-stream"))
                        .body(body);
            }
        } catch (Exception e) {
            httpState = HttpStatus.EXPECTATION_FAILED;
            logger.error("错误原因{}：", e);
        } finally {
            // 关闭流
            try {
                if (null != zos) {
                    zos.flush();
                    zos.close();
                }
                if (null != byteArrayOutputStream) {
                    byteArrayOutputStream.flush();
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                logger.error("错误原因{}：", e);
            }
        }
        return org.springframework.http.ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }

    //获取文件的后缀名
    public static String querySuffix(MultipartFile[] files) {
        String suffix = "";
        if (null != files && files.length > 0) {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                suffix = fileName.substring(fileName.indexOf("."), fileName.length());
            }
        }
        return suffix;
    }
}
