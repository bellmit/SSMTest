package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChgc;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglChgcMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.core.xsbfmapper.XsbfDchyXmglChgcMapper;
import cn.gtmap.msurveyplat.serviceol.service.ChdwXmcxService;
import cn.gtmap.msurveyplat.serviceol.service.JsdwFbxqglService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import cn.gtmap.msurveyplat.serviceol.web.util.FileDownoadUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-01
 * description
 */
@Controller
@RequestMapping("/jsdwFbxqgl")
public class JsdwFbxqglController {

    private Logger logger = LoggerFactory.getLogger(JsdwFbxqglController.class);

    @Autowired
    DchyXmglChgcMapper dchyXmglChgcMapper;
    @Autowired
    JsdwFbxqglService jsdwFbxqglService;
    @Autowired
    ChdwXmcxService chdwXmcxService;
    @Autowired
    DchyXmglZdService dchyXmglZdService;
    @Autowired
    XsbfDchyXmglChgcMapper xsbfDchyXmglChgcMapper;

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description ?????????????????????????????????
     */
    @PostMapping(value = "/jsdwCkxq")
    @ResponseBody
    @CheckInterfaceAuth
    public Object getJsdwCkxqList(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            map.put("userid", UserUtil.getCurrentUserId());
            Page<Map<String, Object>> dataList = jsdwFbxqglService.getJsdwCkxqList(map);
            DataSecurityUtil.decryptMapList(dataList.getContent());
            responseMessage = ResponseUtil.wrapResponseBodyByPage(dataList);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    @GetMapping(value = "/initJsdwFbxq")
    @ResponseBody
    @CheckInterfaceAuth
    public Object initJsdwFbxq() {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> resultMap = jsdwFbxqglService.initJsdwFbxq();
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().putAll(resultMap);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    @PostMapping(value = "/deleteJsdwFbxq")
    @ResponseBody
    @CheckInterfaceAuth
    public Object deleteJsdwFbxq(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> resultMap = jsdwFbxqglService.deleteJsdwFbxqByChxmid(map);
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getHead().setMsg(MapUtils.getString(resultMap, "msg"));
            responseMessage.getHead().setCode(MapUtils.getString(resultMap, "code"));
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description ????????????????????????
     */
    @PostMapping(value = "/saveJsdwFbxq")
    @ResponseBody
    @CheckInterfaceAuth
    public Object saveJsdwFbxq(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            jsdwFbxqglService.saveChgcxx(map);
            responseMessage = ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description ????????????????????????, ??????????????????????????????
     */
    @PostMapping(value = "/queryGcmcByGcbh")
    @ResponseBody
    @CheckInterfaceAuth
    public Object queryGcmcByGcbh(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            DchyXmglChgc dchyXmglChgc = jsdwFbxqglService.queryGcmcByGcbh(map);
            responseMessage = ResponseUtil.wrapSuccessResponse();
            if (null != dchyXmglChgc) {
                responseMessage.getData().put("gcbh", dchyXmglChgc.getGcbh());
                responseMessage.getData().put("gcmc", dchyXmglChgc.getGcmc());
                responseMessage.getData().put("gcdzs", dchyXmglChgc.getGcdzs());
                responseMessage.getData().put("gcdzss", dchyXmglChgc.getGcdzss());
                responseMessage.getData().put("gcdzxx", dchyXmglChgc.getGcdzxx());
                responseMessage.getData().put("gcdzqx", dchyXmglChgc.getGcdzqx());
            }
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description ??????????????????????????????
     */
    @RequestMapping(value = "/jsdwCkxm")
    @ResponseBody
    @CheckInterfaceAuth
    public Object getJsdwCkxmList(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            Page<Map<String, Object>> dataList = jsdwFbxqglService.getJsdwCkxmList(map);
            if (dataList != null) {
                DataSecurityUtil.decryptMapList(dataList.getContent());
            }
            responseMessage = ResponseUtil.wrapResponseBodyByPage(dataList);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description ????????????????????????
     */
    @RequestMapping(value = "/queryWtxxByChdwxxid")
    @ResponseBody
    @CheckInterfaceAuth
    public Object queryWtxxByChxmid(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            List<Map<String, Object>> dataList = jsdwFbxqglService.queryWtxxByChdwxxid(map);
            responseMessage = ResponseUtil.wrapResponseBodyByList(dataList);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description ????????????????????????
     */
    @RequestMapping(value = "/queryChdwxxByChdwxxid")
    @ResponseBody
    @CheckInterfaceAuth
    public Object queryChdwxxByChxmid(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            List<Map<String, Object>> dataList = jsdwFbxqglService.queryChdwxxByChdwxxid(map);
            responseMessage = ResponseUtil.wrapResponseBodyByList(dataList);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description ????????????????????????
     */
    @RequestMapping(value = "/queryQtblxxByChxmid")
    @ResponseBody
    @CheckInterfaceAuth
    public Object queryQtblxxByChxmid(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            List<Map<String, Object>> dataList = jsdwFbxqglService.queryQtblxxByChxmid(map);
            responseMessage = ResponseUtil.wrapResponseBodyByList(dataList);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description ??????????????????
     */
    @RequestMapping(value = "/queryChxmxxByChxmid")
    @ResponseBody
    @CheckInterfaceAuth
    public Object queryChxmxxByChxmid(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            List<Map<String, Object>> dataList = jsdwFbxqglService.queryChxmxxByChxmid(map);
            responseMessage = ResponseUtil.wrapResponseBodyByList(dataList);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description ??????????????????, ??????clsx
     */
    @RequestMapping(value = "/queryClsxByChxmid")
    @ResponseBody
    @CheckInterfaceAuth
    public Object queryChxmxxByChbh(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            List<Map<String, Object>> dataList = jsdwFbxqglService.queryClsxByChxmid(map);
            responseMessage = ResponseUtil.wrapResponseBodyByList(dataList);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @return
     * @parammap
     * @description 2020/12/10 ????????????  ????????????   ????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @RequestMapping(value = "/jsdwHtDownload")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage jsdwHtDownload(@RequestBody Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();

        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));
        String chdwxxid = CommonUtil.formatEmptyValue(data.get("chdwxxid"));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("chxmid", chxmid);
        paramMap.put("chdwxxid", chdwxxid);

        List<String> wjzxidList = jsdwFbxqglService.queryHtxxByChxmid(paramMap);
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

    @RequestMapping("/download")
    @ResponseBody
    public ResponseEntity<byte[]> download(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        List<String> wjzxidList = Lists.newArrayList();
        String dwlx = httpServletRequest.getParameter("dwlx");
        // ????????????1 ????????????2
        if (StringUtils.equals(dwlx, Constants.DWLX_JSDW)) {
            String chxmid = httpServletRequest.getParameter("chxmid");
            String chdwxxid = httpServletRequest.getParameter("chdwxxid");
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("chxmid", chxmid);
            paramMap.put("chdwxxid", chdwxxid);
            wjzxidList = jsdwFbxqglService.queryHtxxByChxmid(paramMap);
        } else if (StringUtils.equals(dwlx, Constants.DWLX_CHDW)) {
            String chxmid = httpServletRequest.getParameter("chxmid");
            String mlkid = httpServletRequest.getParameter("mlkid");
            wjzxidList = chdwXmcxService.queryHtxxByChxmidAndHtxxid(chxmid, mlkid);
        }

        if (CollectionUtils.isNotEmpty(wjzxidList)) {
            // ????????????
            ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

            // ??????????????????
            bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);


            String fileName = "??????.zip";
            response.reset();//???????????????????????????????????????????????????????????????????????????
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
                    fileName = "??????.zip";
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
                logger.error("????????????:{}", e);
            } finally {
                // ?????????
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
                    logger.error("????????????:{}", e);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }

    @GetMapping("/ssjdList")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage ssjdList() {
        ResponseMessage message;
        try {
            List<DchyXmglZd> dchyXmglZdList = dchyXmglZdService.getDchyXmglZdListByZdlx(Constants.DCHY_XMGL_CHXM_CLSX);
            List<DchyXmglZd> dchyXmglZdSsjdList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(dchyXmglZdList)) {
                for (DchyXmglZd dchyXmglZd : dchyXmglZdList) {
                    if (StringUtils.isBlank(dchyXmglZd.getFdm())) {
                        dchyXmglZdSsjdList.add(dchyXmglZd);
                    }
                }
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("dataList", dchyXmglZdSsjdList);
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.CONFIGTABLE_NULL.getMsg(), ResponseMessage.CODE.CONFIGTABLE_NULL.getCode());
            }
        } catch (Exception e) {
            logger.error("????????????:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;

    }
}
