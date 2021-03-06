package cn.gtmap.msurveyplat.promanage.web.main;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglMlkService;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglSjclService;
import cn.gtmap.msurveyplat.promanage.core.service.FileUploadService;
import cn.gtmap.msurveyplat.promanage.core.service.ResultsManagementService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.PlatformUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.FileDownoadUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.FileUploadUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.fileCenter.model.Node;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static cn.gtmap.msurveyplat.promanage.utils.PlatformUtil.getNodeService;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/5 16:16
 * @description
 */
@Controller
@RequestMapping("/fileoperation")
public class FileUploadController {

    @Autowired
    private DchyXmglMlkService dchyXmglMlkService;

    @Autowired
    private PlatformUtil platformUtil;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private DchyXmglSjclService dchyXmglSjclService;

    @Autowired
    private ResultsManagementService resultsManagementService;

    protected final Log logger = LogFactory.getLog(FileUploadController.class);

    private static Set<String> fileNameSet = new HashSet<>();

    private static Map<String, Integer> fileNameCount = new HashMap<>();

    /**
     * ????????????
     *
     * @param files
     */
    @PostMapping(value = "uploadfiles")
    @ResponseBody
    @SystemLog(czmkMc = ProLog.CZMC_WJSC_MC, czmkCode = ProLog.CZMC_WJSC_CODE, czlxMc = ProLog.CZLX_UPLOAD_MC, czlxCode = ProLog.CZLX_UPLOAD_CODE)
    public ResponseMessage fileUpload(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        String ssmkid = CommonUtil.ternaryOperator(request.getParameter("ssmkid"));
        String glsxid = CommonUtil.ternaryOperator(request.getParameter("glsxid"));// ?????????id
        String cllx = CommonUtil.ternaryOperator(request.getParameter("cllx"));
        String fs = CommonUtil.ternaryOperator(request.getParameter("fs"));
        String ys = CommonUtil.ternaryOperator(request.getParameter("ys"));
        String sjclid = CommonUtil.ternaryOperator(request.getParameter("sjclid"));
        String clmc = CommonUtil.ternaryOperator(request.getParameter("clmc"));
        String sjxxid = CommonUtil.ternaryOperator(request.getParameter("sjxxid"));
        String xh = CommonUtil.ternaryOperator(request.getParameter("xh"));
        String sjclpzid = CommonUtil.ternaryOperator(request.getParameter("sjclpzid"));
        String ssclsx = CommonUtil.ternaryOperator(request.getParameter("ssclsx"));
        /*????????????*/
        Map<String, Object> map = FileUploadUtil.uploadFile(files, glsxid, clmc);
        map.put("ssmkid", ssmkid);
        map.put("glsxid", glsxid);
        map.put("cllx", cllx);
        map.put("fs", fs);
        map.put("ys", ys);
        map.put("sjclid", sjclid);
        map.put("xh", xh);
        map.put("clmc", clmc);
        map.put("sjxxid", sjxxid);
        map.put("sjclpzid", sjclpzid);
        map.put("ssclsx", ssclsx);
        /*??????????????????????????????*/
        return FileUploadUtil.updateSjxxAndClxx(map, files);
    }

    /**
     * ??????ssmkid???????????????????????????
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/getsjcl")
    @ResponseBody
    @SystemLog(czmkMc = ProLog.CZMC_WJSC_MC, czmkCode = ProLog.CZMC_WJSC_CODE, czlxMc = ProLog.CZLX_UPLOAD_MC, czlxCode = ProLog.CZLX_UPLOAD_CODE)
    public ResponseMessage getSjclForUpload(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            String ssmkId = CommonUtil.ternaryOperator(data.get("ssmkid"));
            String glsxid = CommonUtil.ternaryOperator(data.get("glsxid"));
            if (StringUtils.isNotBlank(glsxid)) {
                // sjcl ??????????????? sjclid ??????
                mapList = dchyXmglMlkService.getSjclXx(glsxid, ssmkId);
                /*??????????????????????????????????????????????????????*/
                if (CollectionUtils.isEmpty(mapList)) {
                    mapList = dchyXmglMlkService.queryUploadFileBySsmkId(ssmkId);
                    /*?????????????????????*/
                    dchyXmglMlkService.initSjxxAndClxx(glsxid, ssmkId, mapList);
                } else {
                    for (Iterator<Map<String, Object>> it = mapList.iterator(); it.hasNext(); ) {
                        Map<String, Object> map = it.next();
                        String sjclid = MapUtils.getString(map, "SJCLID");
                        if (StringUtils.isBlank(sjclid)) {
                            it.remove();
                        }
                    }
                }
            }
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", mapList);
        } catch (Exception e) {
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * querySjclByClsx
     *
     * @param param Map<String, Object>
     * @return ResponseMessage
     */
    @PostMapping(value = "/querySjclByClsx")
    @ResponseBody
    @SystemLog(czmkMc = ProLog.CZMC_WJSC_MC, czmkCode = ProLog.CZMC_WJSC_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE)
    public ResponseMessage querySjclByClsx(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> mapList = Lists.newArrayList();
        try {
            mapList = dchyXmglSjclService.querySjclList(param);
            ResponseUtil.wrapResponseBodyByList(mapList);
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", mapList);
        } catch (Exception e) {
            logger.error("????????????:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * ????????????
     *
     * @return
     */
    @PostMapping(value = "deletefile")
    @ResponseBody
    @SystemLog(czmkMc = ProLog.CZMC_WJSC_MC, czmkCode = ProLog.CZMC_WJSC_CODE, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE)
    public ResponseMessage deleteFile(@RequestBody Map<String, Object> param) {
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        String sjclId = CommonUtil.ternaryOperator(data.get("sjclId"));
        String wjzxId = CommonUtil.ternaryOperator(data.get("wjzxid"));
        return dchyXmglMlkService.delFile(sjclId, wjzxId);
    }

    /**
     * ????????????????????????
     *
     * @param param Map<String, Object>
     * @return ResponseMessage
     */
    @PostMapping(value = "deletefileJl")
    @ResponseBody
    @SystemLog(czmkMc = ProLog.CZMC_WJSC_MC, czmkCode = ProLog.CZMC_WJSC_CODE, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE)
    public ResponseMessage deleteFileJl(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        try {
            fileUploadService.deleteFileJl(param);
            message = ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * ??????chxmid????????????????????????????????????
     *
     * @param
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/downloadallfiles")
    @SystemLog(czmkMc = ProLog.CZMC_WJSC_MC, czmkCode = ProLog.CZMC_WJSC_CODE, czlxMc = ProLog.CZLX_DOWNLOAD_MC, czlxCode = ProLog.CZLX_DOWNLOAD_CODE)
    public ResponseEntity<byte[]> downloadAllFiles(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String chxmids = request.getParameter("chxmids");//"53QF5222LD6JT20W-53QG04126D6JT21M";//
        if (StringUtils.isNotBlank(chxmids)) {
            //????????????????????????
            if (chxmids.indexOf("-") != -1) {
                Map<String, List<Map<String, String>>> resultMap = new HashMap();
                String[] chxmidArray = chxmids.split("-");
                for (String chxmid : chxmidArray) {
                    DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
                    String slbh = xmglChxm.getSlbh();
                    List<Map<String, String>> downloadFile = this.getDownloadFileInfoByChxmid(chxmid);
                    resultMap.put(slbh, downloadFile);
                }
                if (MapUtils.isNotEmpty(resultMap)) {
                    Iterator<Map.Entry<String, List<Map<String, String>>>> fileIterator = resultMap.entrySet().iterator();
                    Map<String, List<Map<String, byte[]>>> bytesMap = new HashMap<>();
                    while (fileIterator.hasNext()) {
                        Map.Entry<String, List<Map<String, String>>> next = fileIterator.next();
                        String slbh = next.getKey();//????????????
                        List<Map<String, String>> fileList = next.getValue();
                        List<Map<String, byte[]>> fileBytesList = new ArrayList<>();
                        if (CollectionUtils.isNotEmpty(fileList)) {
                            for (Map<String, String> fileMap : fileList) {
                                Iterator<Map.Entry<String, String>> iterator = fileMap.entrySet().iterator();
                                while (iterator.hasNext()) {
                                    Map.Entry<String, String> entry = iterator.next();
                                    String clml = entry.getKey();
                                    String wjzxid = entry.getValue();
                                    List<Map<String, byte[]>> fileBytes = this.getDowanloadFileBytes(wjzxid, clml, request, response);
                                    fileBytesList.addAll(fileBytes);
                                }
                            }
                        }
                        bytesMap.put(slbh, fileBytesList);
                    }
                    if (MapUtils.isNotEmpty(bytesMap)) {
                        return this.compressMultFileToZipByMultChxm(bytesMap, request, response, "");
                    }
                }
            } else {//????????????
                DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmids);
                List<Map<String, byte[]>> fileBytesList = new ArrayList<>();
                List<Map<String, String>> downloadFile = this.getDownloadFileInfoByChxmid(chxmids);
                /*????????????*/
                if (CollectionUtils.isNotEmpty(downloadFile)) {
                    for (Map<String, String> fileMap : downloadFile) {
                        Iterator<Map.Entry<String, String>> iterator = fileMap.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, String> entry = iterator.next();
                            String clml = entry.getKey();
                            String wjzxid = entry.getValue();
                            List<Map<String, byte[]>> fileBytes = this.getDowanloadFileBytes(wjzxid, clml, request, response);
                            fileBytesList.addAll(fileBytes);
                        }
                    }
                    return this.compressMultFileToZip(fileBytesList, request, response, xmglChxm.getSlbh());
                }
            }
        }
        return null;
    }

    /**
     * ????????????chxm????????????????????????zip???
     *
     * @param bytesMap
     * @param request
     * @param response
     * @param path
     * @return
     * @throws Exception
     */
    private ResponseEntity<byte[]> compressMultFileToZipByMultChxm(Map<String, List<Map<String, byte[]>>> bytesMap, HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
        // ????????????
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

        // ??????????????????
        bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);

        String zipName = "????????????.zip";
        response.reset();//???????????????????????????????????????????????????????????????????????????
        response.setContentType("application/octet-stream");
        HttpStatus httpState = null;
        ZipOutputStream zos = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            zos = new ZipOutputStream(byteArrayOutputStream);
            Iterator<Map.Entry<String, List<Map<String, byte[]>>>> iterator1 = bytesMap.entrySet().iterator();
            while (iterator1.hasNext()) {
                /*???????????????????????????*/
                fileNameSet.clear();
                fileNameCount.clear();
                Map.Entry<String, List<Map<String, byte[]>>> next = iterator1.next();
                String slbh = next.getKey();
                List<Map<String, byte[]>> fileBytesList = next.getValue();
                for (Map<String, byte[]> fileMap : fileBytesList) {
                    Iterator<Map.Entry<String, byte[]>> iterator = fileMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, byte[]> fileEntry = iterator.next();
                        String fileName = fileEntry.getKey();
                        byte[] fileBytes = fileEntry.getValue();
                        byte[] buf = new byte[fileBytes.length];
                        if (fileNameSet.contains(fileName)) {
                            fileNameCount.put(fileName, fileNameCount.get(fileName) + 1);
                            int i = fileName.indexOf(".");
                            String prefixName = fileName.substring(0, i);
                            String suffixName = fileName.substring(i);
                            String newFileName = prefixName + "_" + fileNameCount.get(fileName) + suffixName;
                            zos.putNextEntry(new ZipEntry(slbh + File.separator + newFileName));
                        } else {
                            fileNameSet.add(fileName);
                            fileNameCount.put(fileName, 0);
                            zos.putNextEntry(new ZipEntry(slbh + File.separator + fileName));
                        }
                        int len;
                        ByteArrayInputStream in = new ByteArrayInputStream(fileBytes);
                        while ((len = in.read(buf)) != -1) {
                            zos.write(buf, 0, len);
                        }
                        zos.closeEntry();
                        in.close();
                    }
                }
            }
            zos.finish();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            zipName = FileDownoadUtil.encodeFileName(request, zipName);
//            response.setHeader("Content-Disposition", "attachment;" + zipName);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; " + zipName);
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Last-Modified", new Date().toString());
            headers.add("ETag", String.valueOf(System.currentTimeMillis()));
            return org.springframework.http.ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(bytes.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(bytes);

        } catch (Exception e) {
            httpState = HttpStatus.EXPECTATION_FAILED;
            logger.error("????????????{}", e);
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
//                e.printStackTrace();
                logger.error("????????????{}???", e);
            }
        }
        return org.springframework.http.ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }

    /**
     * ???????????????????????????zip???
     */
    private ResponseEntity<byte[]> compressMultFileToZip(List<Map<String, byte[]>> fileBytesList, HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
        // ????????????
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

        // ??????????????????
        bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);

        String zipName = "????????????.zip";
        response.reset();//???????????????????????????????????????????????????????????????????????????
        response.setContentType("application/octet-stream");
        HttpStatus httpState = null;
        ZipOutputStream zos = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            zos = new ZipOutputStream(byteArrayOutputStream);
            /*???????????????????????????*/
            fileNameSet.clear();
            fileNameCount.clear();
            for (Map<String, byte[]> fileMap : fileBytesList) {
                Iterator<Map.Entry<String, byte[]>> iterator = fileMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, byte[]> fileEntry = iterator.next();
                    String fileName = fileEntry.getKey();
                    byte[] fileBytes = fileEntry.getValue();

                    byte[] buf = new byte[fileBytes.length];
                    if (fileNameSet.contains(fileName)) {
                        fileNameCount.put(fileName, fileNameCount.get(fileName) + 1);
                        int i = fileName.indexOf(".");
                        String prefixName = fileName.substring(0, i);
                        String suffixName = fileName.substring(i);
                        String newFileName = prefixName + "_" + fileNameCount.get(fileName) + suffixName;
                        zos.putNextEntry(new ZipEntry(path + File.separator + newFileName));
                    } else {
                        fileNameSet.add(fileName);
                        fileNameCount.put(fileName, 0);
                        zos.putNextEntry(new ZipEntry(path + File.separator + fileName));
                    }
                    int len;
                    ByteArrayInputStream in = new ByteArrayInputStream(fileBytes);
                    while ((len = in.read(buf)) != -1) {
                        zos.write(buf, 0, len);
                    }
                    zos.closeEntry();
                    in.close();
                }
            }
            zos.finish();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            zipName = FileDownoadUtil.encodeFileName(request, zipName);
//            response.setHeader("Content-Disposition", "attachment;" + zipName);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; " + zipName);
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Last-Modified", new Date().toString());
            headers.add("ETag", String.valueOf(System.currentTimeMillis()));
            return org.springframework.http.ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(bytes.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(bytes);

        } catch (Exception e) {
            httpState = HttpStatus.EXPECTATION_FAILED;
            logger.error("????????????{}", e.getCause());
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
//                e.printStackTrace();
                logger.error("????????????{}???", e);
            }
        }
        return org.springframework.http.ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }

    /**
     * ??????wjzxid????????????????????????????????????
     *
     * @param wjzxid
     * @param clml
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    private List<Map<String, byte[]>> getDowanloadFileBytes(String wjzxid, String clml, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int nodeId = Integer.parseInt(wjzxid);
        List<Map<String, byte[]>> fileList = new ArrayList<>();
        int num = FileDownoadUtil.getFileNumberByNodeId(nodeId);
        Map<String, Object> wjxzMap = Maps.newHashMap();//????????????????????????
        if (num == 1) {//????????????
            wjxzMap.putAll(FileDownoadUtil.downLoadFile(nodeId));
            if (MapUtils.isNotEmpty(wjxzMap)) {
                String fileName = MapUtils.getString(wjxzMap, "wjmc");
                byte[] body = (byte[]) wjxzMap.get("wjnr");
                Map<String, byte[]> fileMap = new HashMap<>();
                fileMap.put(fileName, body);
                fileList.add(fileMap);
            }
        } else {
            List<Node> nodeList = getNodeService().getChildNodes(nodeId);
            if (CollectionUtils.isNotEmpty(nodeList)) {
                for (Node nodeTemp : nodeList) {
                    if (nodeTemp.getType() == 1) {//????????????
                        String fileName = nodeTemp.getName();
                        byte[] downloadWj = FileDownoadUtil.downloadWj(nodeTemp.getId());
                        Map<String, byte[]> fileMap2 = new HashMap<>();
                        fileMap2.put(fileName, downloadWj);
                        fileList.add(fileMap2);
                    }
                }
            }
        }
        return fileList;
    }

    /**
     * ??????chxmid????????????????????????????????????????????????????????????
     *
     * @param chxmids String
     * @return List<Map < String, String>>
     */
    private List<Map<String, String>> getDownloadFileInfoByChxmid(String chxmids) {
        List<Map<String, String>> downloadClList = new ArrayList<>();
        DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmids);
        if (null != xmglChxm) {
            String chxmid = xmglChxm.getChxmid();
            /*?????????(???????????????)?????????wjzxid*/
            Example sjxxExample = new Example(DchyXmglSjxx.class);
            sjxxExample.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
            if (CollectionUtils.isNotEmpty(sjxxList)) {
                Map<String, String> sjclMap = new HashMap<>();
                /*??????sjxxid?????????sjcl*/
                for (DchyXmglSjxx sjxx : sjxxList) {
                    Example sjclExample = new Example(DchyXmglSjcl.class);
                    sjclExample.createCriteria().andEqualTo("sjxxid", sjxx.getSjxxid()).andIsNotNull("wjzxid");
                    List<DchyXmglSjcl> sjclList = entityMapper.selectByExample(sjclExample);
                    if (CollectionUtils.isNotEmpty(sjclList)) {
                        for (DchyXmglSjcl xmglSjcl : sjclList) {
                            sjclMap.put(xmglSjcl.getClmc(), xmglSjcl.getWjzxid());
                        }
                    }
                }
                /*?????????????????????*/
                downloadClList.add(sjclMap);
                /*????????????*/
                Example htxxExample = new Example(DchyXmglHtxx.class);
                htxxExample.createCriteria().andEqualTo("chxmid", chxmid).andIsNotNull("wjzxid");
                List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
                if (CollectionUtils.isNotEmpty(htxxList)) {
                    for (DchyXmglHtxx dchyXmglHtxx : htxxList) {
                        String htxxid = dchyXmglHtxx.getHtxxid();//????????????id
                        if (StringUtils.isNotBlank(htxxid)) {
                            /*??????htxxid??????sjxx*/
                            Example htSjxxExample = new Example(DchyXmglSjxx.class);
                            htSjxxExample.createCriteria().andEqualTo("glsxid", htxxid);
                            List<DchyXmglSjxx> htSjxxList = entityMapper.selectByExample(htSjxxExample);
                            if (CollectionUtils.isNotEmpty(htSjxxList)) {
                                Map<String, String> htSjclMap = new HashMap<>();
                                for (DchyXmglSjxx htSjxx : htSjxxList) {
                                    Example sjclExample = new Example(DchyXmglSjcl.class);
                                    sjclExample.createCriteria().andEqualTo("sjxxid", htSjxx.getSjxxid()).andIsNotNull("wjzxid");
                                    List<DchyXmglSjcl> htSjclList = entityMapper.selectByExample(sjclExample);
                                    if (CollectionUtils.isNotEmpty(htSjclList)) {
                                        for (DchyXmglSjcl htSjcl : htSjclList) {
                                            htSjclMap.put(htSjcl.getClmc(), htSjcl.getWjzxid());
                                        }
                                    }
                                }
                                /*????????????*/
                                downloadClList.add(htSjclMap);
                            }
                        }
                    }
                }
            }
        }
        return downloadClList;
    }

    @RequestMapping("/download")
    @ResponseBody
    public ResponseEntity<byte[]> download(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        String wjzxid = httpServletRequest.getParameter("wjzxid");
        String xzlx = httpServletRequest.getParameter("xzlx");
        if (wjzxid.indexOf("-") != -1) {//??????wjzxid
            String[] wjzxids = wjzxid.split("-");
            ByteArrayOutputStream byteArrayOutputStream = null;
            ZipOutputStream zos = null;
            byteArrayOutputStream = new ByteArrayOutputStream();
            zos = new ZipOutputStream(byteArrayOutputStream);
            String fileName = "????????????";

            byte[] body = null;
            /*??????wjzxid*/
            for (int i = 0; i < wjzxids.length; i++) {
                /**/
                String path = "";
                if (StringUtils.equals(xzlx, Constants.XZLX_CGXZ)) {
                    Node node = PlatformUtil.getNodeService().getNode(Integer.parseInt(wjzxids[i]));
                    if (null != node && node.getType() != 1) {
                        path = node.getName();
                    }
                } else {
                    Example cyryExample = new Example(DchyXmglCyry.class);
                    cyryExample.createCriteria().andEqualTo("wjzxid", wjzxids[i]);
                    List<DchyXmglCyry> cyryList = entityMapper.selectByExample(cyryExample);
                    if (CollectionUtils.isNotEmpty(cyryList)) {
                        path = cyryList.get(0).getRyxm();//
                    }
                }
                /*????????????*/
                FileDownoadUtil.downLoadZip(zos, Integer.parseInt(wjzxids[i]), path, xzlx);
            }
            FileDownoadUtil.getFileName().clear();
            FileDownoadUtil.getFileNameCount().clear();
            zos.finish();
            body = byteArrayOutputStream.toByteArray();
            fileName += ".zip";

            fileName = FileDownoadUtil.encodeFileName(httpServletRequest, fileName);
//            response.setHeader("Content-Disposition", "attachment;" + fileName);
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
        } else {
            return this.downFilesByFileCenterId(wjzxid, xzlx, httpServletRequest, response);
        }
    }

    /**
     * ???????????????????????????,?????????wjzxid?????????????????????????????????
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/getuploadfilenums")
    @ResponseBody
    public ResponseMessage getUploadFileNums(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        String wjzxid = CommonUtil.ternaryOperator(data.get("wjzxid"));
        String babh = CommonUtil.ternaryOperator(data.get("babh"));
        String gcbh = CommonUtil.ternaryOperator(data.get("gcbh"));
        try {
            if (StringUtils.isNotBlank(wjzxid)) {
                if (wjzxid.indexOf("-") != -1) {//??????wjzxid
                    message = ResponseUtil.wrapSuccessResponse();
                } else {
                    int nodeId = Integer.parseInt(wjzxid);
                    int fileNum = FileDownoadUtil.getFileNumberByNodeId(nodeId);
                    if (fileNum > 0) {
                        message = ResponseUtil.wrapSuccessResponse();
                        message.getData().put("wjzxid", wjzxid);
                    } else {
                        message.getHead().setMsg("????????????????????????");
                        message.getHead().setCode(ResponseMessage.CODE.QUERY_NULL.getCode());
                    }
                }
            } else if (StringUtils.isNotBlank(babh)) {
                String wjzxs = resultsManagementService.getChxmClcgWjzxidByBabh(babh);
                if (StringUtils.isNotBlank(wjzxs)) {
                    message = ResponseUtil.wrapSuccessResponse();
                    message.getData().put("wjzxid", wjzxs);
                } else {
                    message.getHead().setMsg("????????????????????????");
                    message.getHead().setCode(ResponseMessage.CODE.QUERY_NULL.getCode());
                }
            } else if (StringUtils.isNotBlank(gcbh)) {
                String wjzxs = resultsManagementService.getChxmClcgWjzxidByGcbh(gcbh);
                if (StringUtils.isNotBlank(wjzxs)) {
                    message = ResponseUtil.wrapSuccessResponse();
                    message.getData().put("wjzxid", wjzxs);
                } else {
                    message.getHead().setMsg("????????????????????????");
                    message.getHead().setCode(ResponseMessage.CODE.QUERY_NULL.getCode());
                }
            } else {
                message.getHead().setMsg("????????????????????????");
                message.getHead().setCode(ResponseMessage.CODE.QUERY_NULL.getCode());
            }
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * ??????wjzxid???????????????????????????
     *
     * @param wjzxid
     * @return
     */
    public Map<String, byte[]> downFileByteArrayByWjzxid(String wjzxid) {
        if (wjzxid.indexOf("sx") != -1) {//??????
            int index = wjzxid.indexOf("sx");
            String fileCenterId = wjzxid.substring(0, index);
            int nodeId = Integer.parseInt(fileCenterId);
            ZipOutputStream zos = null;
            HttpStatus httpState = null;
            byte[] body = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            Map<String, Object> wjxzMap = Maps.newHashMap();
            String fileName = "????????????.zip";
            try {
                int num = FileDownoadUtil.getFileNumberByNodeId(nodeId);
                Map<String, byte[]> fileMap = new HashMap<>();
                if (num == 1) {
                    wjxzMap.putAll(FileDownoadUtil.downLoadFile(nodeId));
                    if (MapUtils.isNotEmpty(wjxzMap)) {
                        fileName = MapUtils.getString(wjxzMap, "wjmc");
                        body = (byte[]) wjxzMap.get("wjnr");
                        fileMap.put(fileName, body);
                    }
                } else {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    zos = new ZipOutputStream(byteArrayOutputStream);
                    fileName = FileDownoadUtil.downLoadZip4Xs(zos, nodeId, null);
                    zos.finish();
                    body = byteArrayOutputStream.toByteArray();
                    fileName += ".zip";
                    fileMap.put(fileName, body);
                }
                return fileMap;
            } catch (Exception e) {
                logger.error("????????????{}", e);
            }
        } else {
            int nodeId = Integer.parseInt(wjzxid);
            ZipOutputStream zos = null;
            HttpStatus httpState = null;
            byte[] body = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            Map<String, Object> wjxzMap = Maps.newHashMap();
            String fileName = "????????????.zip";
            try {
                int num = FileDownoadUtil.getFileNumberByNodeId(nodeId);
                Map<String, byte[]> fileMap = new HashMap<>();
                if (num == 1) {
                    wjxzMap.putAll(FileDownoadUtil.downLoadFile(nodeId));
                    if (MapUtils.isNotEmpty(wjxzMap)) {
                        fileName = MapUtils.getString(wjxzMap, "wjmc");
                        body = (byte[]) wjxzMap.get("wjnr");
                        fileMap.put(fileName, body);
                    }
                } else {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    zos = new ZipOutputStream(byteArrayOutputStream);
                    fileName = FileDownoadUtil.downLoadZip(zos, nodeId, null, null);
                    zos.finish();
                    body = byteArrayOutputStream.toByteArray();
                    fileName += ".zip";
                    fileMap.put(fileName, body);
                }
                return fileMap;
            } catch (Exception e) {
                logger.error("????????????{}", e);
            }
        }
        return null;
    }

    /**
     * ??????wjzxid????????????
     *
     * @param wjzxid
     * @param httpServletRequest
     * @param response
     * @return
     * @throws Exception
     */
    public ResponseEntity<byte[]> downFilesByFileCenterId(String wjzxid, String xzlx, HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        // ????????????
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

        // ??????????????????
        bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);

        int nodeId = Integer.parseInt(wjzxid);
        String fileName = "??????.zip";
        response.reset();//???????????????????????????????????????????????????????????????????????????
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
                    fileName = FileDownoadUtil.downLoadZip(zos, nodeId, null, xzlx);
                    zos.finish();
                    body = byteArrayOutputStream.toByteArray();
                    fileName += ".zip";
                }

                fileName = FileDownoadUtil.encodeFileName(httpServletRequest, fileName);
//                response.setHeader("Content-Disposition", "attachment;" + fileName);
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
//            e.printStackTrace();
            logger.error("????????????{}???", e);
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
//                e.printStackTrace();
                logger.error("????????????{}???", e);
            }
        }
        return org.springframework.http.ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }

    /**
     * ??????????????????
     */
    @GetMapping(value = "/onlinepreview")
    public ResponseEntity<byte[]> onlinePreview(@RequestParam(value = "wjzxid") String wjzxid, @RequestParam(value = "xzlx",required = false) String xzlx, @RequestParam(value = "fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String agent = request.getHeader("User-Agent");
        if (StringUtils.isNotBlank(wjzxid) && StringUtils.isNotBlank(fileName)) {
            int nodeId = Integer.parseInt(wjzxid);
            //???????????????????????????
            String ext = fileName.substring(fileName.indexOf("."));
            //??????????????????,?????????????????????????????????
            if (".jpg".equals(ext)) {
                response.setContentType("text/html; charset=UTF-8");
                response.setContentType("image/jpeg");
            } else if (".JPG".equals(ext)) {
                response.setContentType("text/html; charset=UTF-8");
                response.setContentType("image/jpeg");
            } else if (".png".equals(ext)) {
                response.setContentType("text/html; charset=UTF-8");
                response.setContentType("image/png");
            } else if (".PNG".equals(ext)) {
                response.setContentType("text/html; charset=UTF-8");
                response.setContentType("image/png");
            } else if (".pdf".equals(ext)) {//????????????pdf??????
                if (-1 != agent.indexOf("Trident")) {//IE?????????
                    return this.downFilesByFileCenterId(wjzxid, xzlx, request, response);
                } else {/*???ie?????????*/
                    response.setContentType("application/pdf;charset=utf-8");
                }
            } else if (".txt".equals(ext)) {
                response.setContentType("text/plain;charset=utf-8");
            } else if (".xls".equals(ext)) {
                response.setContentType("application/msexcel;charset=utf-8");
            } else if (".doc".equals(ext) || ".docx".equals(ext)) {//??????word???????????????????????????
                return this.downFilesByFileCenterId(wjzxid, xzlx, request, response);
            }
            response.setHeader("Content-Disposition", "inline;filename="
                    + fileName);
            try {
                byte[] fileDatas = FileDownoadUtil.downloadWj(nodeId);
                response.getOutputStream().write(fileDatas);
                response.getOutputStream().flush();
            } catch (IOException e) {
                logger.error("????????????:{}", e);
            }
        }
        return null;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description ????????????????????????
     */
    @PostMapping(value = "/saveSjclpz")
    @ResponseBody
    public Object saveSjclpz(@RequestBody Map map) {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> result = dchyXmglSjclService.saveSjclpz(map);
            responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(MapUtils.getString(result, "msg"), MapUtils.getString(result, "code"));
        } catch (Exception e) {
            logger.error("????????????{}", e);
            responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description ????????????????????????
     */
    @PostMapping(value = "/deleteSjclpz")
    @ResponseBody
    public Object deleteSjclpz(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> resultMap = dchyXmglSjclService.deleteSjclpz(map);
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getHead().setMsg(MapUtils.getString(resultMap, "msg"));
            responseMessage.getHead().setCode(MapUtils.getString(resultMap, "code"));
        } catch (Exception e) {
            logger.error("????????????{}", e);
            responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return responseMessage;
    }

    public static Set<String> getFileNameSet() {
        return fileNameSet;
    }

    public static void setFileNameSet(Set<String> fileNameSet) {
        FileUploadController.fileNameSet = fileNameSet;
    }

    public static Map<String, Integer> getFileNameCount() {
        return fileNameCount;
    }

    public static void setFileNameCount(Map<String, Integer> fileNameCount) {
        FileUploadController.fileNameCount = fileNameCount;
    }

}
