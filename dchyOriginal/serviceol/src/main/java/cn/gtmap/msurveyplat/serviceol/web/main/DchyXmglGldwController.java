package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglKp;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglGldwService;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglMlkService;
import cn.gtmap.msurveyplat.serviceol.web.util.FileDownoadUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipOutputStream;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/10 21:18
 * @description 管理单位
 */
@RestController
@RequestMapping(value = "gldw")
public class DchyXmglGldwController {

    protected final Log logger = LogFactory.getLog(DchyXmglGldwController.class);

    @Autowired
    private DchyXmglGldwService gldService;

    @Autowired
    private DchyXmglMlkService mlkService;

    @Autowired
    private EntityMapper entityMapper;

    /**
     * 获取项目考评列表
     *
     * @param param
     * @return
     */
    @PostMapping(value = "getchdwkplist")
    @CheckInterfaceAuth
    public ResponseMessage getProjectEvalList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Page<Map<String, Object>> objects = gldService.queryChdwKpStatusByPage(data);
            DataSecurityUtil.decryptMapList(objects.getContent());
            return ResponseUtil.wrapResponseBodyByPage(objects);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }


    /**
     * 根据kpid新增对应考评信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "savechdwevalbyid")
    @CheckInterfaceAuth
    public ResponseMessage getChdwEvalByChxmid(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            DchyXmglKp dchyXmglKp = gldService.saveChdwKpxx(data);

            //考评完成后更新mlk的评价等级字段
            String mlkid = CommonUtil.formatEmptyValue(MapUtils.getString(data, "mlkid"));
            if (StringUtils.isNotBlank(mlkid)) {
                int pjfs = mlkService.getKpResultByMlkId(mlkid);
                DchyXmglMlk dchyXmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
                if (null != dchyXmglMlk) {
                    dchyXmglMlk.setPjdj(pjfs + "");
                    entityMapper.saveOrUpdate(dchyXmglMlk, mlkid);
                }
            }

            if (null != dchyXmglKp) {
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.SAVE_FAIL.getMsg(), ResponseMessage.CODE.SAVE_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 保存测绘单位（名录库）诚信记录
     *
     * @param param
     * @return
     */
    @PostMapping(value = "savechdwcxjl")
    @CheckInterfaceAuth
    public ResponseMessage savechdwcxjl(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Map<String, String> resultMap = gldService.saveChdwCxjl(data);
            message = ResponseUtil.wrapResponseBodyByCodeMap(resultMap);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    @GetMapping("/getndkp/{mlkid}")
    public ResponseMessage getKpNdkpByMlkid(@PathVariable(name = "mlkid") String mlkid) {
        ResponseMessage message = new ResponseMessage();
        try {
            Set<String> kpNdkpByMlkid = gldService.getKpNdkpByMlkid(mlkid);
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("ndkp", kpNdkpByMlkid);
        } catch (Exception e) {
            logger.error("获取名录库年度考评错误：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 获取建设单位评价记录
     *
     * @param param
     * @return
     */
    @PostMapping(value = "getjsdwevalbyid")
    @CheckInterfaceAuth
    public ResponseMessage getChdwEvalByid(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Page<Map<String, Object>> chdwEvalByid = gldService.getChdwEvalByid(data);
            return ResponseUtil.wrapResponseBodyByPage(chdwEvalByid);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 获取管理单位对应的考评结果
     *
     * @param param
     * @return
     */
    @PostMapping(value = "getgldwkpbymlkid")
    @CheckInterfaceAuth
    public ResponseMessage getGldwKpInfoByMlkid(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Page<Map<String, Object>> gldwKpxxByMlkId = gldService.getGldwKpxxByMlkId(data);
            return ResponseUtil.wrapResponseBodyByPage(gldwKpxxByMlkId);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    @GetMapping("getNdkp")
    public ResponseMessage getNdkp() {
        ResponseMessage message = new ResponseMessage();
        try {
            List<Integer> ndkp = cn.gtmap.msurveyplat.serviceol.utils.CommonUtil.getNdkp();
            message.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getMsg());
            message.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
            message.getData().put("datalist", ndkp);
        } catch (Exception e) {
            logger.error("获取年度错误：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 根据mlkid移出名录库
     *
     * @param param
     * @return
     */
    @PostMapping(value = "removemlkbyid")
    @CheckInterfaceAuth
    public ResponseMessage remveMlkById(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            gldService.removeMlkById(data);
            message = ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 名录库移出前判断是否有在建工程
     *
     * @return
     */
    @PostMapping(value = "isconstructproject")
    @SystemLog(czmkMc = ProLog.CZMC_MLKDJ_CODE, czmkCode = ProLog.CZMC_MLKDJ_MC, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE)
    public ResponseMessage isConstructProject(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        boolean constructProject = false;
        try {
            constructProject = gldService.isConstructProject(data);
            /*true:已办结*/
            if (constructProject) {
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.LOGOUT_FAIL.getMsg(), ResponseMessage.CODE.LOGOUT_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
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
                message = gldService.evaluationCheckResults(data);
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

    @RequestMapping("/download")
    @ResponseBody
    @SystemLog(czmkMc = ProLog.SSMKID_CGCCPJ_CODE, czmkCode = ProLog.SSMKID_CGCCPJ_MC, czlxMc = ProLog.CZLX_DOWNLOAD_MC, czlxCode = ProLog.CZLX_DOWNLOAD_CODE)
    public ResponseEntity<byte[]> download(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String glsxid = httpServletRequest.getParameter("cgccid");
        if (StringUtils.isNotBlank(glsxid)) {
            List<String> wjzxidList = gldService.queryWjzxidListByGlsxid(glsxid);
            if (CollectionUtils.isNotEmpty(wjzxidList)) {
                // 构建响应
                ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

                // 二进制数据流
                bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);


                String fileName = "材料.zip";
                response.reset();//清空缓存区，防止存在某些字符使得下载的文件格式错误
                response.setContentType("application/octet-stream");

                ZipOutputStream zos = null;
                HttpStatus httpState = null;
                byte[] body = null;
                ByteArrayOutputStream byteArrayOutputStream = null;
                try {
                    int num = 0;
                    for (String wjzxid : wjzxidList) {
                        int nodeId = Integer.parseInt(wjzxid);
                        num += FileDownoadUtil.getFileNumberByNodeId(nodeId);
                    }
                    Map<String, Object> wjxzMap = Maps.newHashMap();
                    if (num == 1) {
                        for (String wjzxid : wjzxidList) {
                            int nodeId = Integer.parseInt(wjzxid);
                            int numTemp = FileDownoadUtil.getFileNumberByNodeId(nodeId);
                            if (numTemp == 1) {
                                wjxzMap.putAll(FileDownoadUtil.downLoadFile(nodeId));
                                if (MapUtils.isNotEmpty(wjxzMap)) {
                                    fileName = MapUtils.getString(wjxzMap, "wjmc");
                                    body = (byte[]) wjxzMap.get("wjnr");
                                }
                            } else {
                                continue;
                            }
                            fileName = FileDownoadUtil.encodeFileName(httpServletRequest, fileName);
                            HttpHeaders headers = new HttpHeaders();
                            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                            headers.add("Content-Disposition", "attachment; " + fileName);
                            headers.add("Pragma", "no-cache");
                            headers.add("Expires", "0");
                            headers.add("Last-Modified", new Date().toString());
                            headers.add("ETag", String.valueOf(System.currentTimeMillis()));
                            return ResponseEntity
                                    .ok()
                                    .headers(headers)
                                    .contentLength(body.length)
                                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                                    .body(body);

                        }
                    } else {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        zos = new ZipOutputStream(byteArrayOutputStream);
                        for (String wjzxid : wjzxidList) {
                            int nodeId = Integer.parseInt(wjzxid);
                            FileDownoadUtil.downLoadZip(zos, nodeId, null);
                        }
                        zos.finish();
                        body = byteArrayOutputStream.toByteArray();
                        fileName = "材料.zip";
                        fileName = FileDownoadUtil.encodeFileName(httpServletRequest, fileName);
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                        headers.add("Content-Disposition", "attachment; " + fileName);
                        headers.add("Pragma", "no-cache");
                        headers.add("Expires", "0");
                        headers.add("Last-Modified", new Date().toString());
                        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
                        return ResponseEntity
                                .ok()
                                .headers(headers)
                                .contentLength(body.length)
                                .contentType(MediaType.parseMediaType("application/octet-stream"))
                                .body(body);

                    }
                } catch (Exception e) {
                    httpState = HttpStatus.EXPECTATION_FAILED;
                    logger.error("错误信息:{}", e);
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
                        logger.error("错误信息:{}", e);
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }


}
