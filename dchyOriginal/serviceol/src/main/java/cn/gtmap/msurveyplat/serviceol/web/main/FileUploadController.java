package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmClsx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClsxHtxxGx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglCyry;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglHtxx;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.common.util.SsmkidEnum;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglMlkService;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglSjclService;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.core.service.FileUploadService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil;
import cn.gtmap.msurveyplat.serviceol.web.util.FileDownoadUtil;
import cn.gtmap.msurveyplat.serviceol.web.util.FileUploadUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipOutputStream;

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
    private FileUploadService fileUploadService;

    @Autowired
    PlatformUtil platformUtil;

    @Autowired
    EntityMapper entityMapper;

    @Autowired
    DchyXmglSjclService dchyXmglSjclService;

    @Autowired
    DchyXmglZdService dchyXmglZdService;

    protected final Log logger = LogFactory.getLog(FileUploadController.class);

    /**
     * querySjclByClsx
     *
     * @param param Map<String, Object>
     * @return ResponseMessage
     */
    @PostMapping(value = "/querySjclByClsx")
    @ResponseBody
    public ResponseMessage querySjclByClsx(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> mapList;
        try {
            mapList = dchyXmglSjclService.querySjclList(param);
            message = ResponseUtil.wrapResponseBodyByList(mapList);
        } catch (Exception e) {
            message = ResponseUtil.wrapExceptionResponse(e);
            logger.error("????????????:{}", e);
        }
        return message;
    }

    /**
     * ????????????
     *
     * @param files MultipartFile[]
     */
    @PostMapping(value = "uploadfiles")
    @ResponseBody
    @CheckInterfaceAuth
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
     * ???????????????????????????
     *
     * @param files MultipartFile[]
     */
    @PostMapping(value = "uploadfilestosx")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage fileUploadToSx(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        String ssmkid = CommonUtil.ternaryOperator(request.getParameter("ssmkid"));
        String glsxid = CommonUtil.ternaryOperator(request.getParameter("glsxid"));// ?????????id
        String cllx = CommonUtil.ternaryOperator(request.getParameter("cllx"));
        String fs = CommonUtil.ternaryOperator(request.getParameter("fs"));
        String ys = CommonUtil.ternaryOperator(request.getParameter("ys"));
        String sjclid = CommonUtil.ternaryOperator(request.getParameter("sjclid"));
        String clmc = CommonUtil.ternaryOperator(request.getParameter("clmc"));
        String sjxxid = CommonUtil.ternaryOperator(request.getParameter("sjxxid"));
        String xh = CommonUtil.ternaryOperator(request.getParameter("xh"));
        String htxxid = CommonUtil.ternaryOperator(request.getParameter("htxxid"));
        String sjclpzid = CommonUtil.ternaryOperator(request.getParameter("sjclpzid"));
        String ssclsx = CommonUtil.ternaryOperator(request.getParameter("ssclsx"));
        /*????????????*/
        Map<String, Object> map;

        if (StringUtils.equals(SsmkidEnum.ZXBA.getCode(), ssmkid)) {
            /*????????????????????????*/
            map = FileUploadUtil.uploadFile(files, htxxid, clmc);
        } else {
            /*??????????????????*/
            map = FileUploadUtil.uploadFile(files, glsxid, clmc);
        }
        map.put("ssmkid", ssmkid);
        map.put("glsxid", glsxid);//chxmid
        map.put("cllx", cllx);
        map.put("fs", fs);
        map.put("ys", ys);
        map.put("sjclid", sjclid);
        map.put("xh", xh);
        map.put("clmc", clmc);
        map.put("sjxxid", sjxxid);
        map.put("htxxid", htxxid);
        map.put("sjclpzid", sjclpzid);
        map.put("ssclsx", ssclsx);

        /*??????????????????????????????*/
        return FileUploadUtil.updateSjxxAndClxx(map, files);
    }


    /**
     * ????????????,??????????????????
     *
     * @param files
     * @param request
     * @return
     */
    @PostMapping(value = "uploadfilestoxsbf")
    @ResponseBody
    public ResponseMessage fileUploadToXsbf(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        ResponseMessage message = new ResponseMessage();
        String clmc = CommonUtil.ternaryOperator(request.getParameter("clmc"));
        String htxxid = CommonUtil.ternaryOperator(request.getParameter("htxxid"));
        String ssmkid = CommonUtil.ternaryOperator(request.getParameter("ssmkid"));
        String glsxid = CommonUtil.ternaryOperator(request.getParameter("glsxid"));
        Map<String, Object> map = new HashMap<>();
        try {
            /*??????????????????*/
            map = FileUploadUtil.uploadFile(files, htxxid, clmc);
            if (MapUtils.isNotEmpty(map)) {
                String folderId = MapUtils.getString(map, "folderId");//wjzxid
                if (StringUtils.isNotBlank(folderId) && StringUtils.isNotBlank(folderId)) {
                    /*??????????????????sjxx???sjcl*/
                    return dchyXmglSjclService.updateSjxxAndSjcl4Xsbf(htxxid, folderId, ssmkid, glsxid);
                }
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.UPLOAD_FAIL.getMsg(), ResponseMessage.CODE.UPLOAD_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("?????????????????????????????????{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param param Map<String, Object>
     * @return ResponseMessage
     */
    @PostMapping(value = "/getsjclforht")
    @ResponseBody
    public ResponseMessage getSjclForHt(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> mapList;
        List<List<Map<String, Object>>> resultList = new ArrayList<>();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            String ssmkId = CommonUtil.ternaryOperator(data.get("ssmkid"));
            String glsxid = CommonUtil.ternaryOperator(data.get("glsxid"));//chxmid
            if (StringUtils.isNoneBlank(glsxid, ssmkId)) {
                /*??????????????????*/
                if (StringUtils.equals(SsmkidEnum.ZXBA.getCode(), ssmkId)) {
                    /*???????????????????????????htxxid*/
                    String[] clsxNum = this.getHtxxIdListByGlsxid(glsxid);
                    if (clsxNum.length > 0) {
                        List<String> htxxidList = dchyXmglMlkService.getHtxxIdByChxmid2(glsxid);
                        if (CollectionUtils.isEmpty(htxxidList)) {//??????htxx????????????
                            for (int i = 0; i < clsxNum.length; i++) {
                                /*??????????????????*/
                                String htxxid = this.generateHtxx(glsxid);
                                if (StringUtils.isNotBlank(htxxid)) {
                                    /*?????????????????????????????????*/
                                    this.generateHtxxClsxGx(glsxid, htxxid, clsxNum[i]);
                                    mapList = dchyXmglMlkService.getSjclXx2(htxxid, ssmkId);
                                    if (CollectionUtils.isEmpty(mapList)) {
                                        mapList = dchyXmglMlkService.queryUploadFileBySsmkId(ssmkId);
                                        mapList.get(0).put("HTXXID", htxxid);
                                        List<String> clsxList = this.generateClsxByHtxxid(htxxid);
                                        mapList.get(0).put("CLSXS", clsxList);
                                        /*?????????sjxx???sjcl*/
                                        dchyXmglMlkService.initSjxxAndClxx(htxxid, ssmkId, mapList);
                                        resultList.add(mapList);
                                    }
                                }
                            }
                        } else {//????????????htxxid
                            for (String htxxid : htxxidList) {
                                /*??????htxxid??????sjxx???sjcl*/
                                mapList = dchyXmglMlkService.getSjclXx2(htxxid, ssmkId);
                                if (CollectionUtils.isNotEmpty(mapList)) {
                                    String tempHtxxid = (String) mapList.get(0).get("HTXXID");
                                    List<String> clsxList = this.generateClsxByHtxxid(tempHtxxid);
                                    mapList.get(0).put("CLSXS", clsxList);
                                }
                                resultList.add(mapList);
                            }
                        }
                    }
                }
            }
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", resultList);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * ??????ssmkid???????????????????????????
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/getsjcl")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage getSjclForUpload(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            String ssmkId = CommonUtil.ternaryOperator(data.get("ssmkid"));
            String glsxid = CommonUtil.ternaryOperator(data.get("glsxid"));//chxmid
            if (StringUtils.isNoneBlank(glsxid, ssmkId)) {
                if (StringUtils.equals(SsmkidEnum.ZXBA.getCode(), ssmkId)) {
                    /*??????glsxid???chxmid*/
                    String htxxid = dchyXmglMlkService.getHtxxIdByChxmid(glsxid);
                    if (StringUtils.isNotBlank(htxxid)) {
                        glsxid = htxxid;
                    }
                    // sjcl ??????????????? sjclid ??????
                    mapList = dchyXmglMlkService.getSjclXx2(glsxid, ssmkId);
                    if (CollectionUtils.isEmpty(mapList)) {
                        mapList = dchyXmglMlkService.queryUploadFileBySsmkId(ssmkId);
                        String htxxid2 = dchyXmglMlkService.initHtxx(glsxid);
                        mapList.get(0).put("HTXXID", htxxid2);
                        /*?????????????????????*/
                        dchyXmglMlkService.initSjxxAndClxx(htxxid2, ssmkId, mapList);
                    }
                } else {
                    // sjcl ??????????????? sjclid ??????
                    mapList = dchyXmglMlkService.getSjclXx(glsxid, ssmkId);
                    if (CollectionUtils.isEmpty(mapList)) {
                        mapList = dchyXmglMlkService.queryUploadFileBySsmkId(ssmkId);
                        /*?????????????????????*/
                        dchyXmglMlkService.initSjxxAndClxx(glsxid, ssmkId, mapList);
                    }
                }
            }
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", mapList);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    private List<String> generateClsxByHtxxid(String htxxid) {
        List<String> clsxList = new ArrayList<>();
        Example htxxGxExample = new Example(DchyXmglClsxHtxxGx.class);
        htxxGxExample.createCriteria().andEqualTo("htxxid", htxxid);
        List<DchyXmglClsxHtxxGx> htxxGxListList = entityMapper.selectByExample(htxxGxExample);
        if (CollectionUtils.isNotEmpty(htxxGxListList)) {
            List<Object> clsxids = new ArrayList<>();
            for (DchyXmglClsxHtxxGx htxxGx : htxxGxListList) {
                clsxids.add(htxxGx.getClsxid());
            }
            if (CollectionUtils.isNotEmpty(clsxids)) {
                Example chxmClsxExample = new Example(DchyXmglChxmClsx.class);
                chxmClsxExample.createCriteria().andIn("clsxid", clsxids);
                List<DchyXmglChxmClsx> chxmClsxList = entityMapper.selectByExample(chxmClsxExample);
                if (CollectionUtils.isNotEmpty(chxmClsxList)) {
                    for (DchyXmglChxmClsx chxmClsx : chxmClsxList) {
                        clsxList.add(chxmClsx.getClsx());
                    }
                }
            }
        }
        return clsxList;
    }

    /**
     * ??????????????????????????????
     *
     * @param chxmid
     * @param htxxid
     * @param parentClsx
     */
    private void generateHtxxClsxGx(String chxmid, String htxxid, String parentClsx) {
        /*?????????????????????????????????*/
        /*??????chxmid?????????????????????*/
        Example clsxExample = new Example(DchyXmglChxmClsx.class);
        clsxExample.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglChxmClsx> clsxList = entityMapper.selectByExample(clsxExample);
        /*???????????????????????????????????????*/
        if (CollectionUtils.isNotEmpty(clsxList)) {
            for (DchyXmglChxmClsx chxmClsx : clsxList) {
                String clsxid = chxmClsx.getClsxid();
                String clsx = chxmClsx.getClsx();
                if (StringUtils.equals(parentClsx, clsx.substring(0, 1))) {
                    DchyXmglClsxHtxxGx htxxGx = new DchyXmglClsxHtxxGx();
                    htxxGx.setGxid(UUIDGenerator.generate18());
                    htxxGx.setHtxxid(htxxid);
                    htxxGx.setClsxid(clsxid);
                    htxxGx.setChxmid(chxmid);
                    entityMapper.saveOrUpdate(htxxGx, htxxGx.getGxid());
                }
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param chxmid
     * @return
     */
    private String generateHtxx(String chxmid) {
        /*?????????????????????*/
        DchyXmglHtxx xmglHtxx = new DchyXmglHtxx();
        xmglHtxx.setHtxxid(UUIDGenerator.generate18());
        xmglHtxx.setChxmid(chxmid);
        xmglHtxx.setBazt(Constants.WTZT_DBA);//????????????
        xmglHtxx.setBasj(new Date());//????????????
        xmglHtxx.setQysj(null);//????????????
        xmglHtxx.setWjzxid("");
        xmglHtxx.setHtlx("0");//????????????
        xmglHtxx.setHtbmid("");//????????????id
        logger.info("htxx: " + xmglHtxx);
        int i = entityMapper.saveOrUpdate(xmglHtxx, xmglHtxx.getHtxxid());
        if (i > 0) {
            return xmglHtxx.getHtxxid();
        }
        return "";
    }

    private String[] getHtxxIdListByGlsxid(String chxmid) {
        Set<String> clsxSet = new HashSet<>();
        if (StringUtils.isNotBlank(chxmid)) {
            Example clsxExample = new Example(DchyXmglChxmClsx.class);
            clsxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglChxmClsx> clsxList = entityMapper.selectByExample(clsxExample);
            if (CollectionUtils.isNotEmpty(clsxList)) {
                for (DchyXmglChxmClsx chxmClsx : clsxList) {
                    String clsx = chxmClsx.getClsx();
                    if (StringUtils.isNotBlank(clsx)) {
                        clsxSet.add(clsx.substring(0, 1));
                    }
                }
            }
        }
        return clsxSet.toArray(new String[clsxSet.size()]);
    }

    /**
     * ??????clsx?????????????????????????????????
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/getSjclByClsx")
    @ResponseBody
    public ResponseMessage getSjclByClsx(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            mapList = dchyXmglSjclService.getSjclList(param);
            ResponseUtil.wrapResponseBodyByList(mapList);
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", mapList);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * ????????????????????????glsxid???????????????????????????
     *
     * @param param Map<String, Object>
     * @return ResponseMessage
     */
    @PostMapping(value = "/getsjclByGlsxid")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage getSjclByGlsxidForUpload(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            //chxmid
            String glsxid = CommonUtil.ternaryOperator(data.get("glsxid"));
            if (StringUtils.isNoneBlank(glsxid)) {
                /*??????glsxid???chxmid*/
                //????????????
                //mapList = dchyXmglMlkService.getSjclXxByGlsxid(glsxid);
                mapList = dchyXmglMlkService.getSjclByGlsxId(glsxid);
            }
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", mapList);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * ????????????????????????glsxid?????????????????????????????????
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/getsjclHtxxByGlsxid")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage getsjclHtxxByGlsxid(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            String glsxid = CommonUtil.ternaryOperator(data.get("glsxid"));//chxmid
            if (StringUtils.isNoneBlank(glsxid)) {
                /*??????glsxid???chxmid*/
                List<String> htxxidList = dchyXmglMlkService.getXsbfHtxxIdByChxmid(glsxid);
                if (CollectionUtils.isNotEmpty(htxxidList)) {
                    for (String htxxid : htxxidList) {
                        glsxid = htxxid;
                        /*??????*/
                        mapList = dchyXmglMlkService.getSjclXxByGlsxid(glsxid);
                        resultList.add(mapList.get(0));
                    }
                }
            }
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", resultList);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * ???????????????chxm?????????????????????????????????sjcl
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/afterbaxxforsjcl")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage afterBaxxForSjcl(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            List<Map<String, Object>> maps = dchyXmglMlkService.afterBaxxForSjcl(data);
            message = ResponseUtil.wrapResponseBodyByList(maps);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }


    /**
     * ??????ssmkid???????????????????????????
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/getsjclxsbf")
    @ResponseBody
    public ResponseMessage getsjclxsbf(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            String ssmkId = CommonUtil.ternaryOperator(data.get("ssmkid"));
            String glsxid = CommonUtil.ternaryOperator(data.get("glsxid"));
            if (StringUtils.isNotBlank(glsxid)) {
                // sjcl ??????????????? sjclid ??????
                mapList = dchyXmglMlkService.getXsbfSjclXx(glsxid);
            }
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", mapList);
        } catch (Exception e) {
            logger.error("???????????????{}", e);
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
    @CheckInterfaceAuth
    public ResponseMessage deleteFile(@RequestBody Map<String, Object> param) {
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        String sjclId = CommonUtil.ternaryOperator(data.get("sjclId"));
        String wjzxId = CommonUtil.ternaryOperator(data.get("wjzxid"));
        return dchyXmglMlkService.delFile(sjclId, wjzxId);
    }

    /**
     * ??????????????????
     */
    @GetMapping(value = "/onlinepreview")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseEntity<byte[]> onlinePreview(@RequestParam(value = "wjzxid") String wjzxid, @RequestParam(value = "fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String agent = request.getHeader("User-Agent");
        if (StringUtils.isNotBlank(wjzxid) && StringUtils.isNotBlank(fileName)) {
            int nodeId = Integer.parseInt(wjzxid);
            //???????????????????????????
            String ext = fileName.substring(fileName.indexOf("."));
            //??????????????????,?????????????????????????????????
            if (ext.equals(".jpg")) {
                response.setContentType("text/html; charset=UTF-8");
                response.setContentType("image/jpeg");
            } else if (ext.equals(".JPG")) {
                response.setContentType("text/html; charset=UTF-8");
                response.setContentType("image/jpeg");
            } else if (ext.equals(".png")) {
                response.setContentType("text/html; charset=UTF-8");
                response.setContentType("image/png");
            } else if (ext.equals(".PNG")) {
                response.setContentType("text/html; charset=UTF-8");
                response.setContentType("image/png");
            } else if (ext.equals(".pdf")) {//????????????pdf??????
                if (-1 != agent.indexOf("Trident")) {//IE?????????
                    return this.downFilesByFileCenterId(wjzxid, request, response);
                } else {/*???ie?????????*/
                    response.setContentType("application/pdf;charset=utf-8");
                }
            } else if (ext.equals(".txt")) {
                response.setContentType("text/plain;charset=utf-8");
            } else if (ext.equals(".xls")) {
                response.setContentType("application/msexcel;charset=utf-8");
            } else if (ext.equals(".doc") || ext.equals(".docx")) {//??????word???????????????????????????
                return this.downFilesByFileCenterId(wjzxid, request, response);
            }
            response.setHeader("Content-Disposition", "inline;filename="
                    + fileName);
            try {
                byte[] fileDatas = FileDownoadUtil.downloadWj(nodeId);
                response.getOutputStream().write(fileDatas);
                response.getOutputStream().flush();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * ???????????????????????????,?????????wjzxid?????????????????????????????????
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/getuploadfilenums")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage getUploadFileNums(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        String wjzxid = CommonUtil.ternaryOperator(data.get("wjzxid"));
        if (StringUtils.isNotBlank(wjzxid)) {
            if (wjzxid.indexOf("-") != -1) {//??????wjzxid
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                int nodeId = Integer.parseInt(wjzxid);
                int fileNum = FileDownoadUtil.getFileNumberByNodeId(nodeId);
                if (fileNum > 0) {
                    message = ResponseUtil.wrapSuccessResponse();
                } else {
                    message.getHead().setMsg("????????????????????????");
                    message.getHead().setCode(ResponseMessage.CODE.QUERY_NULL.getCode());
                }
            }
        } else {
            message.getHead().setMsg("????????????????????????");
            message.getHead().setCode(ResponseMessage.CODE.QUERY_NULL.getCode());
        }
        return message;
    }

    @RequestMapping("/download")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseEntity<byte[]> download(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        String wjzxid = httpServletRequest.getParameter("wjzxid");
        String ssmkid = httpServletRequest.getParameter("ssmkid");
        String glsxid = httpServletRequest.getParameter("glsxid");

        if (wjzxid.indexOf("-") != -1) {//??????wjzxid
            String[] wjzxids = wjzxid.split("-");
            ByteArrayOutputStream byteArrayOutputStream = null;
            ZipOutputStream zos = null;
            byteArrayOutputStream = new ByteArrayOutputStream();
            zos = new ZipOutputStream(byteArrayOutputStream);
            String fileName = "????????????";
            if (StringUtils.isNotBlank(ssmkid)) {
                if (StringUtils.equals("1", ssmkid)) {
                    fileName = "????????????";
                } else if (StringUtils.equals("2", ssmkid)) {
                    fileName = "??????????????????";
                }
            }

            byte[] body = null;
            /*??????wjzxid*/
            for (int i = 0; i < wjzxids.length; i++) {
                /**/
                String path = "";
                Example cyryExample = new Example(DchyXmglCyry.class);
                cyryExample.createCriteria().andEqualTo("wjzxid", wjzxids[i]);
                List<DchyXmglCyry> cyryList = entityMapper.selectByExample(cyryExample);
                if (CollectionUtils.isNotEmpty(cyryList)) {
                    path = cyryList.get(0).getRyxm();//
                }
                /*????????????*/
                FileDownoadUtil.downLoadZip(zos, Integer.parseInt(wjzxids[i]), path);
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
            return this.downFilesByFileCenterId(wjzxid, httpServletRequest, response);
        }
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
    public ResponseEntity<byte[]> downFilesByFileCenterId(String wjzxid, HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
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
                    fileName = FileDownoadUtil.downLoadZip(zos, nodeId, null);
                    String cyryid = CommonUtil.ternaryOperator(httpServletRequest.getParameter("cyryid"));
                    if (StringUtils.isNotBlank(cyryid)) {
                        DchyXmglCyry cyry = entityMapper.selectByPrimaryKey(DchyXmglCyry.class, cyryid);
                        if (null != cyry) {
                            fileName = cyry.getRyxm();
                        }
                    }
                    String ssmkid = CommonUtil.ternaryOperator(httpServletRequest.getParameter("ssmkid"));
                    if (StringUtils.isNotBlank(ssmkid)) {
                        if (StringUtils.equals("1", ssmkid)) {
                            fileName = "????????????";
                        } else if (StringUtils.equals("2", ssmkid)) {
                            fileName = "??????????????????";
                        }
                    }
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
        return org.springframework.http.ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    @PostMapping(value = "deletefileJl")
    @ResponseBody
    public ResponseMessage deletefileJl(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        try {
            fileUploadService.deleteFileJl(param);
            message = ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description ????????????????????????
     */
    @PostMapping(value = "/saveSjclpz")
    @ResponseBody
    public Object saveSjclpz(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> result = dchyXmglSjclService.saveSjclpz(map);
            responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(MapUtils.getString(result, "msg"), MapUtils.getString(result, "code"));
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
            logger.error("???????????????{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

}
