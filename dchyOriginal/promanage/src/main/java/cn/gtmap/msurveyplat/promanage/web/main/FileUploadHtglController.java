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
import cn.gtmap.msurveyplat.promanage.core.service.FileUploadHtxxService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.ListUtil;
import cn.gtmap.msurveyplat.promanage.utils.PlatformUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.FileDownoadUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.FileUploadHtglUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.FileUploadUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/fileoperationhtxx")
public class FileUploadHtglController {

    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private DchyXmglMlkService dchyXmglMlkService;
    @Autowired
    PlatformUtil platformUtil;
    @Autowired
    FileUploadHtxxService fileUploadHtxxService;
    @Autowired
    DchyXmglSjclService dchyXmglSjclService;
    @Autowired
    private EntityMapper entityMapper;

    /**
     * ????????????
     *
     * @param files
     */
    @PostMapping(value = "uploadfiles")
    @ResponseBody
    @SystemLog(czmkMc = ProLog.CZMC_HTWJSC_MC, czmkCode = ProLog.CZMC_HTWJSC_CODE, czlxMc = ProLog.CZLX_UPLOAD_MC, czlxCode = ProLog.CZLX_UPLOAD_CODE, ssmkid = ProLog.SSMKID_XXBA_CODE)
    public ResponseMessage fileUpload(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        String ssmkid = CommonUtil.ternaryOperator(request.getParameter("ssmkid"));
        String sjclid = CommonUtil.ternaryOperator(request.getParameter("sjclid"));
        String sjxxid = CommonUtil.ternaryOperator(request.getParameter("sjxxid"));
        String xh = CommonUtil.ternaryOperator(request.getParameter("xh"));
        String chxmid = CommonUtil.ternaryOperator(request.getParameter("chxmid"));
        String clmc = CommonUtil.ternaryOperator(request.getParameter("clmc"));
        String chdwid = CommonUtil.ternaryOperator(request.getParameter("chdwid"));
        String clsx = CommonUtil.ternaryOperator(request.getParameter("clsx"));
        String fs = CommonUtil.ternaryOperator(request.getParameter("fs"));
        String ys = CommonUtil.ternaryOperator(request.getParameter("ys"));
        String cllx = CommonUtil.ternaryOperator(request.getParameter("cllx"));
        String htxxid = CommonUtil.ternaryOperator(request.getParameter("htxxid"));
        /*????????????*/
        Map<String, Object> map = FileUploadUtil.uploadFile(files, htxxid, clmc);
        map.put("ssmkid", ssmkid);
        map.put("glsxid", htxxid);
        map.put("sjxxid", sjxxid);
        map.put("sjclid", sjclid);
        map.put("xh", xh);
        map.put("chxmid", chxmid);
        map.put("clmc", clmc);
        map.put("chdwid", chdwid);
        map.put("clsx", clsx);
        map.put("fs", fs);
        map.put("ys", ys);
        map.put("cllx", cllx);
        map.put("htxxid", htxxid);

        /*???????????????????????????????????????*/
        return FileUploadHtglUtil.updateSjxxAndClxx(map, files);
    }

    /**
     * ??????ssmkid???????????????????????????
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/getHtxx")
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @SystemLog(czmkMc = ProLog.CZMC_HTWJSC_MC, czmkCode = ProLog.CZMC_HTWJSC_CODE, czlxMc = ProLog.CZLX_UPLOAD_MC, czlxCode = ProLog.CZLX_UPLOAD_CODE, ssmkid = ProLog.SSMKID_XXBA_CODE)
    public ResponseMessage getHtxxForUpload(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<List<Map<String, Object>>> resultList = new ArrayList<>();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            String ssmkId = CommonUtil.ternaryOperator(data.get("ssmkid"));
            String glsxid = CommonUtil.ternaryOperator(data.get("glsxid"));//chxmid
            List<String> tempClsxList = (List<String>) data.get("clsxList");//????????????
            String[] htClsxNums = this.getHtClsxNums(tempClsxList);
            if (htClsxNums.length > 0) {
                List<String> htxxidList = dchyXmglMlkService.getHtxxIdByChxmid2(glsxid);
                if (CollectionUtils.isEmpty(htxxidList)) {//??????htxx????????????
                    resultList = this.generateMultHtxxByClsxs(htClsxNums, glsxid, ssmkId, tempClsxList);
                } else {//????????????htxxid

                    /*??????clxm????????????*/
                    boolean flag = false;
                    Example clsxExample = new Example(DchyXmglChxmClsx.class);
                    clsxExample.createCriteria().andEqualTo("chxmid", glsxid);
                    List<DchyXmglChxmClsx> clsxList2 = entityMapper.selectByExample(clsxExample);
                    if (CollectionUtils.isNotEmpty(clsxList2)) {
                        List<String> list = new ArrayList<>();
                        for (DchyXmglChxmClsx clsx : clsxList2) {
                            list.add(clsx.getClsx());
                        }
                        /*????????????????????????????????????????????????*/
                        flag = ListUtil.isDiffrent(tempClsxList, list);
                        /*clsx???????????????????????????*/
                        if (flag) {
                            /*???????????????????????????????????????????????????????????????*/
                            for (String htxxid : htxxidList) {
                                /*??????htxxid??????sjxx???sjcl*/
                                mapList = dchyXmglMlkService.getSjclXx2(htxxid, ssmkId);
                                if (CollectionUtils.isNotEmpty(mapList)) {
                                    String tempHtxxid = (String) mapList.get(0).get("HTXXID");
                                    Set<String> clsxList = this.generateClsxByHtxxid(tempHtxxid);
                                    mapList.get(0).put("CLSXS", clsxList);
                                }
                                resultList.add(mapList);
                            }
                        } else {
                            /*
                            1.???????????????????????????
                            2.?????????????????????????????????
                            * */
                            /*????????????????????????????????????????????????,??????????????????????????????????????????????????????????????????*/
                            Example htExample = new Example(DchyXmglHtxx.class);
                            List<Object> htIds = new ArrayList<>();
                            htIds.addAll(htxxidList);
                            htExample.createCriteria().andIn("htxxid", htIds).andIsNotNull("wjzxid");
                            List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htExample);
                            if (null != htxxList) {
                                List<String> diffrent = ListUtil.getDiffrent(tempClsxList, list);
                                if (CollectionUtils.isNotEmpty(diffrent)) {
                                    for (String dif : diffrent) {
                                        if (list.contains(dif)) {
                                            /*?????????????????????????????????????????????*/
                                            Example clsxExample2 = new Example(DchyXmglChxmClsx.class);
                                            clsxExample2.createCriteria().andEqualTo("clsx", dif).andEqualTo("chxmid", glsxid);
                                            List<DchyXmglChxmClsx> clsxs = entityMapper.selectByExample(clsxExample2);
                                            if (CollectionUtils.isNotEmpty(clsxs)) {
                                                String clsxid = clsxs.get(0).getClsxid();
                                                if (StringUtils.isNotBlank(clsxid)) {
                                                    Example htGxExample = new Example(DchyXmglClsxHtxxGx.class);
                                                    htGxExample.createCriteria().andEqualTo("clsxid", clsxid).andEqualTo("chxmid", glsxid);
                                                    List<DchyXmglClsxHtxxGx> htxxGxList = entityMapper.selectByExample(htGxExample);
                                                    if (CollectionUtils.isNotEmpty(htxxGxList)) {
                                                        /*htxxid*/
                                                        String htxxid = htxxGxList.get(0).getHtxxid();
                                                        if (StringUtils.isNotBlank(htxxid)) {
                                                            /*???????????????????????????????????????????????????????????????*/
                                                            int count = dchyXmglMlkService.getClsxCountByHtid(htxxid);
                                                            if (count > 1) {
                                                                /*???????????????????????????????????????*/
                                                                /*chxm_clsx*/
                                                                String delClsxid = dchyXmglMlkService.getClsxidByChxmidAndClsx(glsxid, dif);
                                                                if (StringUtils.isNotBlank(delClsxid)) {
                                                                    entityMapper.deleteByPrimaryKey(DchyXmglChxmClsx.class, delClsxid);
                                                                    /*clsx_htxx_gx*/
                                                                    DchyXmglClsxHtxxGx gx = dchyXmglMlkService.getClsxHtGxByChxmidAndClsxid(glsxid, clsxid);
                                                                    if (null != gx) {
                                                                        entityMapper.deleteByPrimaryKey(DchyXmglClsxHtxxGx.class, gx.getGxid());
                                                                    }

                                                                }
                                                            } else {
                                                                /*??????*/
                                                                this.delHtSjclAndSjxx(htxxid, glsxid, dif);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            /*??????*/
                                            DchyXmglChxmClsx clsx = dchyXmglMlkService.getChxmClsxByXmidAndClsx(glsxid, dif.substring(0, 1));
                                            if (null != clsx) {
                                                /**/
                                                String clsxid = clsx.getClsxid();
                                                if (StringUtils.isNotBlank(clsxid)) {
                                                    String htxxid = dchyXmglMlkService.getHtxxIdByClsxId(clsxid);
                                                    if (StringUtils.isNotBlank(htxxid)) {
                                                        /*?????????????????????????????????*/
                                                        this.generateHtxxClsxGx2(glsxid, htxxid, dif);
                                                        mapList = dchyXmglMlkService.queryUploadFileBySsmkId(ssmkId);
                                                        /*?????????sjxx???sjcl*/
                                                        dchyXmglMlkService.initSjxxAndClxx(htxxid, ssmkId, mapList);
                                                    }
                                                }

                                            } else {
                                                /*????????????????????????????????????clsx*/
                                                String htxxid = this.generateHtxx(glsxid);
                                                if (StringUtils.isNotBlank(htxxid)) {
                                                    if (StringUtils.isNotBlank(htxxid)) {
                                                        /*?????????????????????????????????*/
                                                        this.generateHtxxClsxGx2(glsxid, htxxid, dif);
                                                        mapList = dchyXmglMlkService.queryUploadFileBySsmkId(ssmkId);
                                                        /*?????????sjxx???sjcl*/
                                                        dchyXmglMlkService.initSjxxAndClxx(htxxid, ssmkId, mapList);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    /*??????*/
                                    List<String> htids = new ArrayList<>();
                                    Example ht = new Example(DchyXmglHtxx.class);
                                    ht.createCriteria().andEqualTo("chxmid", glsxid);
                                    List<DchyXmglHtxx> htxxes = entityMapper.selectByExample(ht);
                                    if (CollectionUtils.isNotEmpty(htxxes)) {
                                        htxxes.forEach(htid -> {
                                            htids.add(htid.getHtxxid());
                                        });
                                    }
                                    if (CollectionUtils.isNotEmpty(htids)) {
                                        for (String htxxid : htids) {
                                            /*??????htxxid??????sjxx???sjcl*/
                                            mapList = dchyXmglMlkService.getSjclXx2(htxxid, ssmkId);
                                            if (CollectionUtils.isNotEmpty(mapList)) {
                                                String tempHtxxid = (String) mapList.get(0).get("HTXXID");
                                                Set<String> clsxList = this.generateClsxByHtxxid(tempHtxxid);
                                                mapList.get(0).put("CLSXS", clsxList);
                                            }
                                            resultList.add(mapList);
                                        }
                                    }
                                }
                            } else {
                                /*????????????htxx????????????*/
                                for (String htxxid : htxxidList) {
                                    this.delHtSjclAndSjxx(htxxid, glsxid, "");
                                }
                                /*????????????*/
                                resultList = this.generateMultHtxxByClsxs(htClsxNums, glsxid, ssmkId, tempClsxList);
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

    @Transactional
    public void delHtSjclAndSjxx(String htxxid, String glsxid, String clsx) {
        /*??????htxx*/
        entityMapper.deleteByPrimaryKey(DchyXmglHtxx.class, htxxid);
        /*??????clsx_chxm*/
        Example delClsxExample = new Example(DchyXmglChxmClsx.class);
        if (StringUtils.isNotBlank(clsx)) {
            delClsxExample.createCriteria().andEqualTo("chxmid", glsxid).andEqualTo("clsx", clsx);
        } else {
            delClsxExample.createCriteria().andEqualTo("chxmid", glsxid);
        }
        entityMapper.deleteByExample(delClsxExample);
        /*??????clsxhtxx?????????*/
        Example clsxHtxxGxExample = new Example(DchyXmglClsxHtxxGx.class);
        clsxHtxxGxExample.createCriteria().andEqualTo("htxxid", htxxid);
        entityMapper.deleteByExample(clsxHtxxGxExample);
        /*??????sjxx*/
        Example sjxxExample = new Example(DchyXmglSjxx.class);
        sjxxExample.createCriteria().andEqualTo("glsxid", htxxid);
        List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
        if (CollectionUtils.isNotEmpty(sjxxList)) {
            for (DchyXmglSjxx sjxx : sjxxList) {
                entityMapper.deleteByPrimaryKey(DchyXmglSjxx.class, sjxx.getSjxxid());
                /*??????sjcl*/
                Example sjclExample = new Example(DchyXmglSjcl.class);
                sjclExample.createCriteria().andEqualTo("sjxxid", sjxx.getSjxxid());
                entityMapper.deleteByExample(sjclExample);
            }
        }
    }


    private List<List<Map<String, Object>>> generateMultHtxxByClsxs(String[] htClsxNums, String glsxid, String ssmkId, List<String> tempClsxList) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<List<Map<String, Object>>> resultList = new ArrayList<>();
        for (int i = 0; i < htClsxNums.length; i++) {
            /*??????????????????*/
            String htxxid = this.generateHtxx(glsxid);
            if (StringUtils.isNotBlank(htxxid)) {
                /*?????????????????????????????????*/
                this.generateHtxxClsxGx(glsxid, htxxid, htClsxNums[i], tempClsxList);
                mapList = dchyXmglMlkService.getSjclXx2(htxxid, ssmkId);
                if (CollectionUtils.isEmpty(mapList)) {
                    mapList = dchyXmglMlkService.queryUploadFileBySsmkId(ssmkId);
                    if (CollectionUtils.isNotEmpty(mapList)) {
                        mapList.get(0).put("HTXXID", htxxid);
                        Set<String> clsxList = this.generateClsxByHtxxid(htxxid);
                        mapList.get(0).put("CLSXS", clsxList);
                        /*?????????sjxx???sjcl*/
                        dchyXmglMlkService.initSjxxAndClxx(htxxid, ssmkId, mapList);
                        resultList.add(mapList);
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * ??????????????????????????????
     *
     * @param clsxList
     * @return
     */
    private String[] getHtClsxNums(List<String> clsxList) {
        Set<String> clsxSet = new HashSet<>();
        if (clsxList.size() > 0) {
            for (String clsx : clsxList) {
                clsxSet.add(clsx.substring(0, 1));
            }
        }
        return clsxSet.toArray(new String[clsxSet.size()]);
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

    /**
     * ??????????????????????????????
     *
     * @param chxmid
     * @param htxxid
     * @param parentClsx
     */
    private void generateHtxxClsxGx(String chxmid, String htxxid, String parentClsx, List<String> tempClsxList) {

        if (CollectionUtils.isNotEmpty(tempClsxList)) {
            for (String tempClsx : tempClsxList) {
                if (StringUtils.equals(parentClsx, tempClsx.substring(0, 1))) {
                    /*??????clsx_chxm*/
                    Example clsxExample = new Example(DchyXmglChxmClsx.class);
                    clsxExample.createCriteria().andEqualTo("chxmid", chxmid).andEqualTo("clsx", tempClsx);
                    List<DchyXmglChxmClsx> clsxList = entityMapper.selectByExample(clsxExample);
                    if (CollectionUtils.isEmpty(clsxList)) {
                        DchyXmglChxmClsx chxmClsx = new DchyXmglChxmClsx();
                        chxmClsx.setClsxid(UUIDGenerator.generate18());
                        chxmClsx.setChxmid(chxmid);
                        chxmClsx.setClzt("1");
                        chxmClsx.setClsx(tempClsx);
                        entityMapper.saveOrUpdate(chxmClsx, chxmClsx.getClsxid());
                        /*?????????clsx?????????*/
                        DchyXmglClsxHtxxGx htxxGx = new DchyXmglClsxHtxxGx();
                        htxxGx.setGxid(UUIDGenerator.generate18());
                        htxxGx.setHtxxid(htxxid);
                        htxxGx.setClsxid(chxmClsx.getClsxid());
                        htxxGx.setChxmid(chxmid);
                        entityMapper.saveOrUpdate(htxxGx, htxxGx.getGxid());
                    }
                }
            }
        }
    }

    private void generateHtxxClsxGx2(String chxmid, String htxxid, String tempClsx) {

        if (StringUtils.isNotBlank(tempClsx)) {
            /*??????clsx_chxm*/
            Example clsxExample = new Example(DchyXmglChxmClsx.class);
            clsxExample.createCriteria().andEqualTo("chxmid", chxmid).andEqualTo("clsx", tempClsx);
            List<DchyXmglChxmClsx> clsxList = entityMapper.selectByExample(clsxExample);
            if (CollectionUtils.isEmpty(clsxList)) {
                DchyXmglChxmClsx chxmClsx = new DchyXmglChxmClsx();
                chxmClsx.setClsxid(UUIDGenerator.generate18());
                chxmClsx.setChxmid(chxmid);
                chxmClsx.setClzt("1");
                chxmClsx.setClsx(tempClsx);
                entityMapper.saveOrUpdate(chxmClsx, chxmClsx.getClsxid());
                /*?????????clsx?????????*/
                DchyXmglClsxHtxxGx htxxGx = new DchyXmglClsxHtxxGx();
                htxxGx.setGxid(UUIDGenerator.generate18());
                htxxGx.setHtxxid(htxxid);
                htxxGx.setClsxid(chxmClsx.getClsxid());
                htxxGx.setChxmid(chxmid);
                entityMapper.saveOrUpdate(htxxGx, htxxGx.getGxid());
            }
        }
    }

    private Set<String> generateClsxByHtxxid(String htxxid) {
        List<Object> clsxids = new ArrayList<>();
        Set<String> result = new HashSet<>();
        Example htxxGxExample = new Example(DchyXmglClsxHtxxGx.class);
        htxxGxExample.createCriteria().andEqualTo("htxxid", htxxid);
        List<DchyXmglClsxHtxxGx> htxxGxListList = entityMapper.selectByExample(htxxGxExample);
        if (CollectionUtils.isNotEmpty(htxxGxListList)) {
            for (DchyXmglClsxHtxxGx htxxGx : htxxGxListList) {
                clsxids.add(htxxGx.getClsxid());
            }
        }
        if (CollectionUtils.isNotEmpty(clsxids)) {
            Example clsx = new Example(DchyXmglChxmClsx.class);
            clsx.createCriteria().andIn("clsxid", clsxids);
            List<DchyXmglChxmClsx> clsxList = entityMapper.selectByExample(clsx);
            if (CollectionUtils.isNotEmpty(clsxList)) {
                clsxList.forEach(clsxid -> {
                    result.add(clsxid.getClsx());
                });
            }
        }
        return result;
    }

    /**
     * ????????????
     *
     * @return
     */
    @PostMapping(value = "/deletefile")
    @ResponseBody
    @SystemLog(czmkMc = ProLog.CZMC_HTWJSC_MC, czmkCode = ProLog.CZMC_HTWJSC_CODE, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE, ssmkid = ProLog.SSMKID_XXBA_CODE)
    public ResponseMessage deleteFile(@RequestBody Map<String, Object> param) {
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        String htxxId = CommonUtil.ternaryOperator(data.get("htxxId"));
        String wjzxId = CommonUtil.ternaryOperator(data.get("wjzxid"));
        return dchyXmglMlkService.delFile(htxxId, wjzxId);
    }

    @RequestMapping("/download")
    @ResponseBody
    @SystemLog(czmkMc = ProLog.CZMC_HTWJSC_MC, czmkCode = ProLog.CZMC_HTWJSC_CODE, czlxMc = ProLog.CZLX_DOWNLOAD_MC, czlxCode = ProLog.CZLX_DOWNLOAD_CODE, ssmkid = ProLog.SSMKID_XXBA_CODE)
    public ResponseEntity<byte[]> download(HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
        String wjzxid = httpServletRequest.getParameter("wjzxid");
        String ssmkid = httpServletRequest.getParameter("ssmkid");
        String glsxid = httpServletRequest.getParameter("ssmkid");

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
                    fileName = FileDownoadUtil.downLoadZip(zos, nodeId, null, null);
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
                return ResponseEntity
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
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @PostMapping(value = "deletefileHtxxJl")
    @ResponseBody
    @SystemLog(czmkMc = ProLog.CZMC_HTWJSC_MC, czmkCode = ProLog.CZMC_HTWJSC_CODE, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE, ssmkid = ProLog.SSMKID_XXBA_CODE)
    public ResponseMessage deletefileHtxxJl(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        try {
            fileUploadHtxxService.deleteFileHtxxJl(param);
            message = ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

}
