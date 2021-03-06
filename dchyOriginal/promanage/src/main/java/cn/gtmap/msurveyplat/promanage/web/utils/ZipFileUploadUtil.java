package cn.gtmap.msurveyplat.promanage.web.utils;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.common.util.SsmkidEnum;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChgcMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChxmClsxMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglClgcpzMapper;
import cn.gtmap.msurveyplat.promanage.core.service.impl.DchyXmglZdServiceImpl;
import cn.gtmap.msurveyplat.promanage.model.ErrorInfoModel;
import cn.gtmap.msurveyplat.promanage.utils.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.fileCenter.model.Node;
import com.gtis.fileCenter.service.FileService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static cn.gtmap.msurveyplat.promanage.utils.PlatformUtil.getFileService;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/1/7
 * @description ??????zip?????????
 */
public class ZipFileUploadUtil {

    private static final Logger logger = LoggerFactory.getLogger(ZipFileUploadUtil.class);

    private static PlatformUtil platformUtil = (PlatformUtil) Container.getBean("platformUtil");

    private static EntityMapper entityMapper = (EntityMapper) Container.getBean("entityMapper");

    private static DchyXmglClgcpzMapper dchyXmglClgcpzMapper = (DchyXmglClgcpzMapper) Container.getBean("dchyXmglClgcpzMapper");

    private static DchyXmglChgcMapper dchyXmglChgcMapper = (DchyXmglChgcMapper) Container.getBean("dchyXmglChgcMapper");

    private static DchyXmglChxmClsxMapper dchyXmglChxmClsxMapper = (DchyXmglChxmClsxMapper) Container.getBean("dchyXmglChxmClsxMapper");

    private static DchyXmglZdServiceImpl dchyXmglZdService = (DchyXmglZdServiceImpl) Container.getBean("dchyXmglZdServiceImpl");

    /**
     * @param xmid
     * @return
     * @description 2021/1/28 ???????????????????????????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public static Map initCgtj(String xmid) {
        Map map = Maps.newHashMap();
        if (StringUtils.isNotBlank(xmid)) {
            List<DchyXmglClcgTjjl> dchyXmglClcgTjjls = dchyXmglClgcpzMapper.queryClcgTjjl(xmid);
            if (CollectionUtils.isNotEmpty(dchyXmglClcgTjjls)) {
                DchyXmglClcgTjjl dchyXmglClcgTjjl = dchyXmglClcgTjjls.get(0);
                if (StringUtils.equals(dchyXmglClcgTjjl.getSftj(), Constants.DCHY_XMGL_CLCG_SFTJ_FALSE)) {
                    String str = new String(dchyXmglClcgTjjl.getCwxx());
                    JSONObject jsonObject = JSONObject.parseObject(str);
                    map = (Map) jsonObject;
                    map.put("gcbh", dchyXmglClcgTjjl.getChgcbh());
                }
            } else {
                //??????????????????????????????????????????????????????clsx
                List<String> clsxList = Lists.newArrayList();
                Example exampleClcg = new Example(DchyXmglClcg.class);
                exampleClcg.createCriteria().andEqualTo("sqxxid", xmid);
                List<DchyXmglClcg> dchyXmglClcgList = entityMapper.selectByExample(exampleClcg);
                if (CollectionUtils.isNotEmpty(dchyXmglClcgList)) {
                    for (DchyXmglClcg dchyXmglClcg : dchyXmglClcgList) {
                        if (!clsxList.contains(dchyXmglClcg.getClsx())) {
                            clsxList.add(dchyXmglZdService.getZdmcByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, dchyXmglClcg.getClsx()));
                        }
                    }
                    map.put("clsxList", clsxList);
                }
            }
        }
        return map;
    }

    /**
     * @param uploadFileName
     * @param inputStream
     * @param map
     * @return
     * @description 2021/1/8 ??????????????????(?????????(??????????????????))
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public static ResponseMessage checkZipFiles(String uploadFileName, InputStream inputStream, Map map) throws IOException {
        ResponseMessage message = new ResponseMessage();
        Map resultMap = Maps.newHashMap();
        Map paramMap = Maps.newHashMap();
        List<ErrorInfoModel> errorInfoModels = Lists.newArrayList();

        String bh = uploadFileName.substring(0, uploadFileName.indexOf("."));
        String gcbh = null;
        String slbh = null;
        String chxmid = null;
        if (StringUtils.equalsIgnoreCase(ResultsSubmitServiceUtil.getCgtjMode(), Constants.CGTJ_MODE_CHGC)) {
            gcbh = bh;
        } else if (StringUtils.equalsIgnoreCase(ResultsSubmitServiceUtil.getCgtjMode(), Constants.CGTJ_MODE_CHXM)) {
            slbh = bh;
            if (projectIsCompletion(slbh)) {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.FILESUBMIT_FAIL.getMsg(), ResponseMessage.CODE.FILESUBMIT_FAIL.getCode());
                return message;
            }
            Example example = new Example(DchyXmglChxm.class);
            example.createCriteria().andEqualTo("slbh", slbh);
            List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                chxmid = dchyXmglChxmList.get(0).getChxmid();
            } else {
                chxmid = Constants.EMPTYPARAM_VALUE;
            }
        }
        paramMap.put("gcbh", gcbh);
        paramMap.put("slbh", slbh);
        List<Map<String, Object>> clcgList = dchyXmglChgcMapper.queryClcgByGcbh(paramMap);
        //??????chxm???????????????gcbh
        if (CollectionUtils.isNotEmpty(clcgList)) {
            //???????????????
            List<Map<String, Object>> uploadFiles = Lists.newArrayList();
            List<String> clsxListShow = Lists.newArrayList();

            String glsxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "sqxxid"));
            paramMap.put("sqxxid", glsxid);
            generateClml(glsxid, bh);
            Cache.ValueWrapper valueWrapper = EhcacheUtil.getDataFromEhcache(glsxid);
            Map mycache = (Map) valueWrapper.get();
            Map mlMap = MapUtils.getMap(mycache, "mlMap");
            logger.info("***************??????????????????????????????:" + JSON.toJSONString(mlMap));
            List<ErrorInfoModel> fileListSc = Lists.newArrayList();
            List<String> clsxList = Lists.newArrayList();
            List<String> wbaClsxList = Lists.newArrayList();

            //??????zip??????????????????????????????????????????
            ZipInputStream zin = new ZipInputStream(inputStream, Charset.forName("GBK"));
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                ErrorInfoModel errorInfoModel = new ErrorInfoModel();
                String zipEntryName = entry.getName();
                byte[] bytes = IOUtils.toByteArray(zin);
                System.out.println(zipEntryName);
                String[] strings = zipEntryName.split("/");
                if (!StringUtils.equals(strings[0], bh)) {
                    message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.RESULT_SUBMIT_NOT_CONSISTENCY_FAIL.getMsg(), ResponseMessage.CODE.RESULT_SUBMIT_NOT_CONSISTENCY_FAIL.getCode());
                    return message;
                }

                if (strings.length < 2) {
                    continue;
                } else {
                    if (MapUtils.isNotEmpty(mlMap) && mlMap.containsKey(strings[1])) {
                        Map yjmlMap = MapUtils.getMap(mlMap, strings[1]);
                        if (strings.length > 2) {
                            if (!clsxList.contains(strings[2])) {
                                String clsx = dchyXmglZdService.getZddmByZdlxAndMc(Constants.DCHY_XMGL_CHXM_CLSX, strings[2]);
                                logger.info("**********??????????????????????????????:" + clsx + "," + slbh + "," + gcbh + "**********");
                                boolean clsxSdba = clsxSfba(gcbh, slbh, clsx);
                                logger.info("**********??????????????????????????????????????????:" + clsxSdba + "," + clsx + "," + slbh + "," + gcbh + "**********");
                                //???????????????  xx?????????????????????????????????????????????
                                if (!clsxSdba && !wbaClsxList.contains(clsx)) {
                                    wbaClsxList.add(clsx);
                                }
                                clsxList.add(strings[2]);
                            }
                            if (MapUtils.isNotEmpty(yjmlMap) && yjmlMap.containsKey(strings[2])) {
                                Map ejmlMap = MapUtils.getMap(yjmlMap, strings[2]);
                                //zxce20290001/????????????????????????/??????????????????/?????????????????????????????????.pdf
                                //zxce20290001/????????????????????????/??????????????????/????????????/???????????????????????????.pdf
                                List<String> clsxMlList = Lists.newArrayList();
                                if (strings.length == 3) {
                                    clsxMlList.add(zipEntryName);
                                }
                                if (strings.length > 3) {
                                    //??????????????????????????????????????????????????????????????????,??????????????????
                                    if (!clsxListShow.contains(strings[2])) {
                                        clsxListShow.add(strings[2]);
                                    }
                                    if (strings[3].contains(".")) {
                                        String fileName = strings[3];
                                        Map uploadMap = Maps.newHashMap();
                                        uploadMap.put("bytes", bytes);
                                        uploadMap.put("glsxid", glsxid);
                                        uploadMap.put("mlmc", zipEntryName);
                                        uploadMap.put("fileName", fileName);
                                        uploadFiles.add(uploadMap);
                                        errorInfoModel.setClsx(strings[2]);
                                        errorInfoModel.setWjmc(fileName);
                                        errorInfoModel.setMlmc(zipEntryName);
                                        errorInfoModel.setWjzt(queryShztByClcg(strings[2], gcbh, chxmid));
                                        fileListSc.add(errorInfoModel);
                                        if (fileIsExist(zipEntryName)) {
                                            errorInfoModel.setClsx(strings[2]);
                                            errorInfoModel.setMsxx(Constants.DCHY_XMGL_CGSC_MSXX_WJCF);
                                            errorInfoModel.setWjmc(fileName);
                                            errorInfoModel.setWjzt(queryShztByClcg(strings[2], gcbh, chxmid));
                                            errorInfoModels.add(errorInfoModel);
                                        }
                                    } else {
                                        if (MapUtils.isNotEmpty(ejmlMap) && ejmlMap.containsKey(strings[3])) {
                                            List<String> filenameList = Lists.newArrayList();
                                            if (null != ejmlMap.get(strings[3]) && "" != ejmlMap.get(strings[3])) {
                                                filenameList = (List<String>) ejmlMap.get(strings[3]);
                                            }
                                            if (CollectionUtils.isNotEmpty(filenameList)) {
                                                if (strings.length > 4) {
                                                    if (CollectionUtils.isNotEmpty(filenameList)) {
                                                        //???????????????
                                                        List<String> fileList = Lists.newArrayList();
                                                        for (String str : filenameList) {
                                                            fileList.add(str.substring(0, str.indexOf(".")));
                                                        }
                                                        if (fileList.contains(strings[4].substring(0, strings[4].indexOf(".")))) {
                                                            String fileName = strings[strings.length - 1];
                                                            Map uploadMap = Maps.newHashMap();
                                                            uploadMap.put("bytes", bytes);
                                                            uploadMap.put("glsxid", glsxid);
                                                            uploadMap.put("mlmc", zipEntryName);
                                                            uploadMap.put("fileName", fileName);
                                                            uploadFiles.add(uploadMap);
                                                            errorInfoModel.setClsx(strings[2]);
                                                            errorInfoModel.setWjmc(fileName);
                                                            errorInfoModel.setMlmc(zipEntryName);
                                                            fileListSc.add(errorInfoModel);
                                                            if (fileIsExist(zipEntryName)) {
                                                                errorInfoModel.setClsx(strings[2]);
                                                                errorInfoModel.setMsxx(Constants.DCHY_XMGL_CGSC_MSXX_WJCF);
                                                                errorInfoModel.setWjmc(fileName);
                                                                errorInfoModel.setWjzt(queryShztByClcg(strings[2], gcbh, chxmid));
                                                                errorInfoModels.add(errorInfoModel);
                                                            }
                                                        } else {
                                                            String fileName = strings[strings.length - 1];
                                                            Map uploadMap = Maps.newHashMap();
                                                            uploadMap.put("bytes", bytes);
                                                            uploadMap.put("glsxid", glsxid);
                                                            uploadMap.put("mlmc", zipEntryName);
                                                            uploadMap.put("fileName", fileName);
                                                            uploadFiles.add(uploadMap);
                                                            errorInfoModel.setClsx(strings[2]);
                                                            errorInfoModel.setWjmc(fileName);
                                                            errorInfoModel.setMsxx(Constants.DCHY_XMGL_CGSC_MSXX_GDWWYQWJ);
                                                            errorInfoModel.setMlmc(zipEntryName);
                                                            errorInfoModel.setWjzt(queryShztByClcg(strings[2], gcbh, chxmid));
                                                            errorInfoModels.add(errorInfoModel);
                                                        }
                                                    } else {
                                                        String fileName = strings[strings.length - 1];
                                                        Map uploadMap = Maps.newHashMap();
                                                        uploadMap.put("bytes", bytes);
                                                        uploadMap.put("glsxid", glsxid);
                                                        uploadMap.put("mlmc", zipEntryName);
                                                        uploadMap.put("fileName", fileName);
                                                        uploadFiles.add(uploadMap);
                                                    }
                                                } else {
                                                    Map uploadMap = Maps.newHashMap();
                                                    uploadMap.put("glsxid", glsxid);
                                                    uploadMap.put("mlmc", zipEntryName);
                                                    uploadFiles.add(uploadMap);
                                                }
                                            } else {
                                                //????????????????????????(????????????????????????????????????????????????)
                                                if (strings.length > 4) {
                                                    String fileName = strings[strings.length - 1];
                                                    Map uploadMap = Maps.newHashMap();
                                                    uploadMap.put("bytes", bytes);
                                                    uploadMap.put("glsxid", glsxid);
                                                    uploadMap.put("mlmc", zipEntryName);
                                                    uploadMap.put("fileName", fileName);
                                                    uploadFiles.add(uploadMap);

                                                    errorInfoModel.setClsx(strings[2]);
                                                    errorInfoModel.setMsxx(Constants.DCHY_XMGL_CGSC_MSXX_GDWWYQWJ);
                                                    errorInfoModel.setWjmc(strings[4]);
                                                    errorInfoModel.setMlmc(zipEntryName);
                                                    errorInfoModel.setWjzt(queryShztByClcg(strings[2], gcbh, chxmid));
                                                    errorInfoModels.add(errorInfoModel);
                                                }
                                            }
                                        } else {
                                            String fileName = strings[strings.length - 1];
                                            Map uploadMap = Maps.newHashMap();
                                            uploadMap.put("bytes", bytes);
                                            uploadMap.put("glsxid", glsxid);
                                            uploadMap.put("mlmc", zipEntryName);
                                            uploadMap.put("fileName", fileName);
                                            uploadFiles.add(uploadMap);
                                            errorInfoModel.setClsx(strings[2]);
                                            errorInfoModel.setMsxx(Constants.DCHY_XMGL_CGSC_MSXX_GDWWYQWJ);
                                            errorInfoModel.setWjmc(fileName.contains(".") ? fileName : "/");
                                            errorInfoModel.setMlmc(zipEntryName);
                                            errorInfoModel.setWjzt(queryShztByClcg(strings[2], gcbh, chxmid));
                                            errorInfoModels.add(errorInfoModel);
                                        }
                                    }
                                } else {
                                    Map uploadMap = Maps.newHashMap();
                                    uploadMap.put("glsxid", glsxid);
                                    uploadMap.put("mlmc", zipEntryName);
                                    uploadFiles.add(uploadMap);
                                }
                            } else {
                                errorInfoModel.setClsx(strings[1]);
                                errorInfoModel.setMsxx(Constants.DCHY_XMGL_CGSC_MSXX_GDWWYQWJ);
                                errorInfoModel.setWjmc("/");
                                errorInfoModel.setMlmc(zipEntryName);
                                errorInfoModel.setWjzt(queryShztByClcg(strings[2], gcbh, chxmid));
                                errorInfoModels.add(errorInfoModel);
                            }
                        } else {
                            Map uploadMap = Maps.newHashMap();
                            uploadMap.put("glsxid", glsxid);
                            uploadMap.put("mlmc", zipEntryName);
                            uploadFiles.add(uploadMap);
                        }
                    } else {
                        if (strings.length > 2) {
                            errorInfoModel.setClsx(strings[2]);
                        } else {
                            errorInfoModel.setClsx("/");
                        }
                        errorInfoModel.setMsxx(Constants.DCHY_XMGL_CGSC_MSXX_GDWWYQWJ);
                        if (strings.length > 4) {
                            errorInfoModel.setWjmc(strings[4]);
                        } else {
                            errorInfoModel.setWjmc("/");
                        }
                        errorInfoModel.setMlmc(zipEntryName);
//                        errorInfoModel.setWjzt(queryShztByClcg(strings[2], gcbh, chxmid));
                        errorInfoModels.add(errorInfoModel);
                    }
                }
            }

            Map clsxMapList = checkGcbh(gcbh, chxmid);
            logger.info("???????????????????????????????????????????????????????????????????????????:" + JSON.toJSONString(clsxMapList));
            List<String> bjClsxList = (List<String>) clsxMapList.get("bjClsxList");

            //???????????????????????????????????????????????????
            for (String string : clsxListShow) {
                if (bjClsxList.contains(string)) {
                    resultMap.put("code", ResponseMessage.CODE.FILESUBMIT_FAIL.getCode());
                    resultMap.put("msg", ResponseMessage.CODE.FILESUBMIT_FAIL.getMsg());
                    message = ResponseUtil.wrapResponseBodyByCodeMap(resultMap);
                    return message;
                }
            }

            mycache.put("fileListSc", fileListSc);
            mycache.put("uploadFiles", filterFiles(uploadFiles, wbaClsxList));
            mycache.put("key", glsxid);
            mycache.put("clsxListShow", clsxListShow);
            EhcacheUtil.putDataToEhcache(glsxid, mycache);
            logger.info("************???????????????--fileListSc:" + JSON.toJSONString(fileListSc));
            logger.info("************???????????????clsx--clsxListShow:" + JSON.toJSONString(fileListSc));
            logger.info("************???????????????xmid--key:" + JSON.toJSONString(glsxid));
            List<ErrorInfoModel> errorInfoModelList = checkMissingFile(glsxid, gcbh, chxmid);
            if (CollectionUtils.isNotEmpty(errorInfoModelList)) {
                for (ErrorInfoModel errorInfoModel : errorInfoModelList) {
                    ErrorInfoModel errorInfoModelTemp = null;
                    if (CollectionUtils.isNotEmpty(errorInfoModels)) {
                        for (ErrorInfoModel model : errorInfoModels) {
                            if (model.getWjmc().contains(".") && StringUtils.equals(errorInfoModel.getWjmc().substring(0, errorInfoModel.getWjmc().indexOf(".")), model.getWjmc().substring(0, model.getWjmc().indexOf(".")))) {
                                errorInfoModelTemp = new ErrorInfoModel();
                                errorInfoModelTemp.setClsx(model.getClsx());
                                errorInfoModelTemp.setWjmc(model.getWjmc());
//                                errorInfoModelTemp.setMsxx(model.getMsxx());
                                errorInfoModelTemp.setMsxx(errorInfoModel.getMsxx());
                                errorInfoModelTemp.setMlmc(model.getMlmc());
                                errorInfoModelTemp.setWjzt(model.getWjzt());
                                break;
                            }
                        }
                    }
                    if (null == errorInfoModelTemp) {
                        errorInfoModels.add(errorInfoModel);
                    } else {
                        removeFile(errorInfoModels, errorInfoModelTemp);
                        String clsxdm = dchyXmglZdService.getZddmByZdlxAndMc(Constants.DCHY_XMGL_CHXM_CLSX, errorInfoModelTemp.getClsx());
                        if (!wbaClsxList.contains(clsxdm)) {
                            errorInfoModels.add(errorInfoModelTemp);
                        }
                    }
                }
            }

            resultMap.put("data", formatErrorInfoModel(errorInfoModels, paramMap, clsxList, wbaClsxList));
            message.setData(resultMap);
            message = ResponseUtil.wrapSuccessResponse();
            System.out.println("******************????????????********************");

        } else {
            resultMap.put("code", ResponseMessage.CODE.FILENAME_FAIL.getCode());
            resultMap.put("msg", ResponseMessage.CODE.FILENAME_FAIL.getMsg());
            message = ResponseUtil.wrapResponseBodyByCodeMap(resultMap);
        }
        return message;
    }

    /**
     * @param
     * @return
     * @description 2021/1/13 ????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public static ResponseMessage zipUpload(String glsxid, List<Map<String, String>> errorInfoModels) {
        ResponseMessage message = new ResponseMessage();
        if (StringUtils.isNotBlank(glsxid)) {
            List<DchyXmglClcgTjjl> dchyXmglClcgTjjls = dchyXmglClgcpzMapper.queryClcgTjjl(glsxid);
            if (CollectionUtils.isNotEmpty(dchyXmglClcgTjjls)) {
                DchyXmglClcgTjjl dchyXmglClcgTjjl = dchyXmglClcgTjjls.get(0);
                dchyXmglClcgTjjl.setSftj(Constants.DCHY_XMGL_CLCG_SFTJ_TRUE);
                JSONObject jsonObject = JSONObject.parseObject(new String(dchyXmglClcgTjjl.getCwxx()));
                Map map = (Map) jsonObject;
                map.put("sftj", Constants.DCHY_XMGL_CLCG_SFTJ_TRUE);
                try {
                    dchyXmglClcgTjjl.setCwxx(JSON.toJSONString(map).getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
                    logger.error("????????????{}???", e);
                }
                entityMapper.saveOrUpdate(dchyXmglClcgTjjl, dchyXmglClcgTjjl.getTjjlid());
//                dchyXmglClgcpzMapper.updateDchyXmglClcgTjjl(dchyXmglClcgTjjl);
            }
        }
        List<String> notCoverFiles = generateNotCoverFiles(errorInfoModels);
        Cache.ValueWrapper valueWrapper = EhcacheUtil.getDataFromEhcache(glsxid);
        List<Map<String, Object>> uploadFiles = null;
        if (null != valueWrapper) {
            Map mycache = (Map) valueWrapper.get();
            uploadFiles = (List<Map<String, Object>>) mycache.get("uploadFiles");
        }
        if (CollectionUtils.isNotEmpty(uploadFiles)) {
            message = ResponseUtil.wrapSuccessResponse();
            for (Map<String, Object> m : uploadFiles) {
                DchyXmglClcg dchyXmglClcg = null;
                DchyXmglSjcl dchyXmglSjcl = null;
                String fileName = CommonUtil.formatEmptyValue(MapUtils.getString(m, "fileName"));
                String mlmc = CommonUtil.formatEmptyValue(MapUtils.getString(m, "mlmc"));
                byte[] bytes = (byte[]) m.get("bytes");
                if (StringUtils.isNotBlank(fileName) && fileName.contains(".") && !notCoverFiles.contains(mlmc)) {
                    InputStream in = write(bytes);
                    String[] strings = mlmc.split("/");
                    String bh = strings[0];
                    String gcbh = null;
                    String slbh = null;
                    String chxmid = null;
                    if (StringUtils.equalsIgnoreCase(ResultsSubmitServiceUtil.getCgtjMode(), Constants.CGTJ_MODE_CHGC)) {
                        gcbh = bh;
                    } else if (StringUtils.equalsIgnoreCase(ResultsSubmitServiceUtil.getCgtjMode(), Constants.CGTJ_MODE_CHXM)) {
                        slbh = bh;
                        Example example = new Example(DchyXmglChxm.class);
                        example.createCriteria().andEqualTo("slbh", slbh);
                        List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExampleNotNull(example);
                        if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                            chxmid = dchyXmglChxmList.get(0).getChxmid();
                        }
                    }
                    Map map1 = uploadZip(in, mlmc, fileName);
                    String parentId = CommonUtil.formatEmptyValue(MapUtils.getString(map1, "parentId"));
                    String folderId = CommonUtil.formatEmptyValue(MapUtils.getString(map1, "folderId"));
                    //?????????????????????????????????
                    if (StringUtils.isNotBlank(glsxid)) {
                        dchyXmglSjcl = generateSjclAndSjxx(glsxid);
                        dchyXmglClcg = generateClcg(fileName, mlmc, gcbh, chxmid, glsxid);
                        dchyXmglSjcl.setClmc(fileName);
                        dchyXmglSjcl.setWjzxid(parentId);

                        dchyXmglClcg.setWjzxid(folderId);
                        dchyXmglClcg.setSjclid(dchyXmglSjcl.getSjclid());
                        dchyXmglClcg.setSjxxid(dchyXmglSjcl.getSjxxid());
                        entityMapper.saveOrUpdate(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
                        entityMapper.saveOrUpdate(dchyXmglClcg, dchyXmglClcg.getClcgid());
                        changeXmzt(dchyXmglClcg.getChxmid(), dchyXmglClcg.getClsx());
                    }
                }
            }
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.RESULT_SUBMIT_NO_FILE_FAIL.getMsg(), ResponseMessage.CODE.RESULT_SUBMIT_NO_FILE_FAIL.getCode());
        }
        delTjjl(glsxid);
        return message;
    }

    //?????????????????????zip????????????  ??????????????????????????????
    public static void generateClml(String glsxid, String bh) {
        Map resultMap = Maps.newHashMap();
        //???????????????
        List<ErrorInfoModel> fileListPz = Lists.newArrayList();
        Map mlMap = Maps.newHashMap();
        List<DchyXmglClcgpz> dchyXmglClcgpzList1 = dchyXmglClgcpzMapper.queryclcgpz();
        //????????????
        if (CollectionUtils.isNotEmpty(dchyXmglClcgpzList1)) {
            for (DchyXmglClcgpz dchyXmglClcgpz : dchyXmglClcgpzList1) {
                generateDir(dchyXmglClcgpz, mlMap, fileListPz, bh + "/" + dchyXmglClcgpz.getClmc() + "/");
            }
        }
        resultMap.put("mlMap", mlMap);
        resultMap.put("fileListPz", fileListPz);
        EhcacheUtil.putDataToEhcache(glsxid, resultMap);
    }

    //byte[] ??????InputStream   zip?????????InputStream?????????????????????byte[]??????
    public static InputStream write(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    //????????????
    public static void removeFile(List<ErrorInfoModel> errorInfoModels, ErrorInfoModel errorInfoModel) {
        if (CollectionUtils.isNotEmpty(errorInfoModels) && null != errorInfoModel) {
            for (ErrorInfoModel errorInfoModel1 : errorInfoModels) {
                if (StringUtils.equals(errorInfoModel1.getWjmc(), errorInfoModel.getWjmc())) {
                    errorInfoModels.remove(errorInfoModel1);
                    break;
                }
            }
        }
    }

    //??????chxmid???clsx????????????????????????clsx
    public static Map convertClsx(String clsx, String gcbh, String chxmid) {
        Map resultMap = Maps.newHashMap();

        DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndMc(Constants.DCHY_XMGL_CHXM_CLSX, clsx);
        if (StringUtils.isNotBlank(dchyXmglZd.getDm()) && (StringUtils.isNotBlank(gcbh) || StringUtils.isNotBlank(chxmid))) {
            resultMap.put("clsx", dchyXmglZd.getDm());
            resultMap.put("gcbh", gcbh);
            Map clsxMap = queryChxmidByClsx(dchyXmglZd.getDm(), gcbh, chxmid);
            resultMap.put("clsxid", CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "clsxid")));
            resultMap.put("chxmid", CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "chxmid")));
        }
        return resultMap;

    }

    //???????????????????????????????????????
    public static DchyXmglSjcl generateSjclAndSjxx(String glsxid) {
        DchyXmglSjxx dchyXmglSjxx = new DchyXmglSjxx();
        dchyXmglSjxx.setGlsxid(glsxid);
        dchyXmglSjxx.setSjxxid(UUIDGenerator.generate18());
        dchyXmglSjxx.setSsmkid(SsmkidEnum.CHXMSLHTXX.getCode());
        dchyXmglSjxx.setSjr(UserUtil.getCurrentUserId());
        dchyXmglSjxx.setSjsj(CalendarUtil.getCurHMSDate());

        DchyXmglSjcl dchyXmglSjcl = new DchyXmglSjcl();
        dchyXmglSjcl.setSjclid(UUIDGenerator.generate18());
        dchyXmglSjcl.setSjxxid(dchyXmglSjxx.getSjxxid());
        dchyXmglSjcl.setClrq(CalendarUtil.getCurHMSDate());

        entityMapper.saveOrUpdate(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
        entityMapper.saveOrUpdate(dchyXmglSjxx, dchyXmglSjxx.getSjxxid());

        return dchyXmglSjcl;
    }

    //????????????????????????????????????
    public static List<ErrorInfoModel> checkMissingFile(String glsxid, String gcbh, String chxmid) {
        Cache.ValueWrapper valueWrapper = EhcacheUtil.getDataFromEhcache(glsxid);
        Map mycache = (Map) valueWrapper.get();
        List<ErrorInfoModel> fileListPz = (List<ErrorInfoModel>) mycache.get("fileListPz");
        List<ErrorInfoModel> fileListSc = (List<ErrorInfoModel>) mycache.get("fileListSc");
        List<String> clsxListShow = (List<String>) mycache.get("clsxListShow");

        List<String> clsxDmListShow = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(clsxListShow)) {
            for (String string : clsxListShow) {
                DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndMc(Constants.DCHY_XMGL_CHXM_CLSX, string);
                if (null != dchyXmglZd) {
                    clsxDmListShow.add(dchyXmglZd.getDm());
                }
            }
        }
        List<ErrorInfoModel> missFileList = Lists.newArrayList();
        ErrorInfoModel errorInfoModel = null;
        if (CollectionUtils.isNotEmpty(fileListPz)) {
            for (ErrorInfoModel errorInfoModelPz : fileListPz) {
                if (clsxDmListShow.contains(errorInfoModelPz.getClsx())) {
                    ErrorInfoModel errorInfoModelTemp = null;
                    for (ErrorInfoModel errorInfoModelSc : fileListSc) {
                        String wjmcpz = errorInfoModelPz.getWjmc();
                        int index = wjmcpz.indexOf(".");//???????????????"."?????????
                        wjmcpz = wjmcpz.substring(0, index);

                        String wjmcSc = errorInfoModelSc.getWjmc();
                        int index1 = wjmcSc.indexOf(".");//???????????????"."?????????
                        wjmcSc = wjmcSc.substring(0, index1);
                        if (StringUtils.equals(wjmcpz, wjmcSc) &&
                                StringUtils.equals(errorInfoModelPz.getMlmc().substring(0, errorInfoModelPz.getMlmc().lastIndexOf("/")),
                                        errorInfoModelSc.getMlmc().substring(0, errorInfoModelPz.getMlmc().lastIndexOf("/")))) {
                            errorInfoModelTemp = new ErrorInfoModel();
                            errorInfoModelTemp.setClsx(errorInfoModelSc.getClsx());
                            errorInfoModelTemp.setWjmc(errorInfoModelSc.getWjmc());
                            errorInfoModelTemp.setMlmc(errorInfoModelSc.getMlmc());
                            break;
                        }
                    }
                    if (null == errorInfoModelTemp) {
                        errorInfoModel = new ErrorInfoModel();
                        errorInfoModel.setMsxx(Constants.DCHY_XMGL_CGSC_MSXX_WJQS);
                        errorInfoModel.setWjmc(errorInfoModelPz.getWjmc());
                        errorInfoModel.setClsx(getClsxMcByDm(errorInfoModelPz.getClsx()));
                        errorInfoModel.setMlmc(errorInfoModelPz.getMlmc());
                        errorInfoModel.setWjzt(queryShztByClcg(errorInfoModel.getClsx(), gcbh, chxmid));
                        missFileList.add(errorInfoModel);
                    } else if (!StringUtils.equals(errorInfoModelPz.getWjmc(), errorInfoModelTemp.getWjmc())) {
                        errorInfoModel = new ErrorInfoModel();
                        errorInfoModel.setMsxx(Constants.DCHY_XMGL_CGSC_MSXX_GSCW);
                        errorInfoModel.setWjmc(errorInfoModelTemp.getWjmc());
                        errorInfoModel.setClsx(errorInfoModelTemp.getClsx());
                        errorInfoModel.setMlmc(errorInfoModelTemp.getMlmc());
                        errorInfoModel.setWjzt(errorInfoModelTemp.getWjzt());
                        missFileList.add(errorInfoModel);
                    }
                }
            }
        }
        return missFileList;
    }

    //???????????????????????????(????????????)
    public static boolean fileIsExist(String mlmc) {
        boolean fileisExist = false;
//        glsxid = "test20210113";
//        mlmc = "zxce20290001/????????????????????????/??????????????????/?????????/metadata.Xml";
//        int index = mlmc.indexOf("/");//???????????????"/"?????????
//        mlmc = mlmc.substring(index + 1);
        String[] strings = mlmc.split("/");
        Integer parentId = platformUtil.creatNode(strings[0]);
        List<Node> nodeList = platformUtil.getChildNodeListByParentId(parentId);
        if (CollectionUtils.isNotEmpty(nodeList)) {
            for (int i = 0; i < strings.length - 1; i++) {
                Node node = null;
                if (!strings[i + 1].contains(".")) {
                    try {
                        node = platformUtil.getChildNodeListByParentIdAndMlmc(parentId, strings[i + 1]);
                    } catch (Exception e) {
                        logger.error("????????????:{}", e);
//                        e.printStackTrace();
                    }
                    if (null != node && null != node.getId()) {
                        parentId = node.getId();
                    }
                } else {
                    nodeList = platformUtil.getChildNodeListByParentId(parentId);
                    if (fileIsExistWjzx(nodeList, strings[i + 1])) {
                        node = platformUtil.getChildNodeListByParentIdAndMlmc(parentId, strings[i + 1]);
                        if (node.getName().contains(".") && StringUtils.equals(node.getName(), strings[i + 1])) {
                            fileisExist = true;
                        }
                    }
                }
            }
        }
        return fileisExist;
    }

    /**
     * ??????map??????
     *
     * @param paramsMap ???????????????
     * @return resultMap
     * ??????????????????
     * @explain ???paramsMap??????????????????????????????resultMap??????
     * paramsMap???????????????????????????resultMap???????????????
     */
    public static Map mapCopy(Map paramsMap) {
        Map resultMap = Maps.newHashMap();
        if (MapUtils.isNotEmpty(paramsMap)) {
            Iterator it = paramsMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Object key = entry.getKey();
                resultMap.put(key, paramsMap.get(key) != null ? paramsMap.get(key) : "");

            }
            return resultMap;
        } else {
            return null;
        }
    }

    //?????????????????????????????????
    public static List<String> generateNotCoverFiles(List<Map<String, String>> errorInfoModels) {
        List<String> notCoverFiles = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(errorInfoModels)) {
            for (Map map : errorInfoModels) {
                if (StringUtils.equals(MapUtils.getString(map, "msxx"), Constants.DCHY_XMGL_CGSC_MSXX_WJCF)) {
                    notCoverFiles.add(MapUtils.getString(map, "mlmc"));
                }
            }
        }
        return notCoverFiles;
    }

    //?????????????????????????????????
    public static DchyXmglClcg generateClcg(String fileName, String mlmc, String gcbh, String chxmid, String glsxid) {
        logger.info("**********fileName:" + fileName + "*********");
        logger.info("**********mlmc:" + mlmc + "*********");
        logger.info("**********gcbh:" + gcbh + "*********");
        logger.info("**********chxmid:" + chxmid + "*********");
        Map map = generateChcg(gcbh, chxmid);
        String chgcbh = CommonUtil.formatEmptyValue(MapUtils.getString(map, "chgcbh"));
        String chgcid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "chgcid"));
        DchyXmglClcg dchyXmglClcg = new DchyXmglClcg();
        if (StringUtils.isNoneBlank(fileName, mlmc) && (StringUtils.isNoneBlank(gcbh) || StringUtils.isNoneBlank(chxmid))) {
            dchyXmglClcg = new DchyXmglClcg();
            String[] strings = mlmc.split("/");
            dchyXmglClcg.setClcgid(UUIDGenerator.generate18());
            dchyXmglClcg.setShzt(Constants.DCHY_XMGL_SHZT_DSH);
            dchyXmglClcg.setSqxxid(glsxid);
            dchyXmglClcg.setChgcbh(chgcbh);
            dchyXmglClcg.setChgcid(chgcid);
            dchyXmglClcg.setClcgmc(fileName);
            dchyXmglClcg.setTjr(UserUtil.getCurrentUserId());
            dchyXmglClcg.setTjrmc(UserUtil.getCurrentUser().getUsername());
            dchyXmglClcg.setTjsj(CalendarUtil.getCurHMSDate());
            dchyXmglClcg.setRksj(CalendarUtil.getCurHMSDate());
            Map clsxMap = convertClsx(strings[2], gcbh, chxmid);
            if (MapUtils.isNotEmpty(clsxMap)) {
                dchyXmglClcg.setClsx(CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "clsx")));
                dchyXmglClcg.setClsxid(CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "clsxid")));
                dchyXmglClcg.setChxmid(CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "chxmid")));
            }
        }
        return dchyXmglClcg;
    }

    //???????????????????????????????????????id
    public static Map generateChcg(String gcbh, String chxmid) {
        Map map = Maps.newHashMap();
        Map param = Maps.newHashMap();
        param.put("gcbh", gcbh);
        param.put("chxmid", chxmid);
        List<DchyXmglChgc> chgcList = dchyXmglChgcMapper.queryChgcxx(param);
        if (CollectionUtils.isNotEmpty(chgcList)) {
            for (DchyXmglChgc chgcxx : chgcList) {
                if (StringUtils.equals(chgcxx.getGcbh(), gcbh)) {
                    map.put("chgcbh", chgcxx.getGcbh());
                    map.put("chgcid", chgcxx.getChgcid());
                    break;
                }
            }
        }
        return map;
    }

    //??????zip
    public static Map<String, Object> uploadZip(InputStream inputStream, String mlmc, String fileName) {
        String[] strings = mlmc.split("/");
        HashMap<String, Object> map = Maps.newHashMap();
        try {
            FileService fileService = getFileService();
            Integer parentId = null;
            Integer folderId = null;
            if (strings.length > 1) {
                // ??????????????????
                parentId = platformUtil.creatNode(strings[0]);
                for (int i = 1; i < strings.length - 1; i++) {
                    folderId = platformUtil.createFileFolderByclmc(parentId, strings[i]);
                    parentId = folderId;
                }
                if (folderId == null) {
                    folderId = platformUtil.creatNode(strings[0]);
                } else {
                    Node node = null;
                    try {
                        node = platformUtil.getChildNodeListByParentIdAndMlmc(parentId, fileName);
                        if (null != node && null != node.getId()) {
                            platformUtil.deleteNodeById(node.getId());
                        }
                    } catch (Exception e) {
                        logger.error("????????????:{}", e);
                    }
                }

                Node node = fileService.uploadFile(inputStream, folderId, fileName);
                if (null != node) {
                    parentId = folderId;
                    folderId = node.getId();
                    map.put("parentId", parentId + "");
                    map.put("folderId", folderId + "");
                }
            }
        } catch (Exception e) {
            logger.error("????????????:{}", e);
        }
        return map;
    }

    //?????????????????????????????????
    public static boolean fileIsExistWjzx(List<Node> nodeList, String filename) {
        boolean isExist = false;
        if (CollectionUtils.isNotEmpty(nodeList)) {
            for (Node node : nodeList) {
                if (StringUtils.equals(filename, node.getName())) {
                    isExist = true;
                    break;
                }
            }
        }
        return isExist;
    }

    //??????????????????????????????
    private static Map formatErrorInfoModel(List<ErrorInfoModel> errorInfoModels, Map map, List<String> clsxList, List<String> wbaClsxList) {
        String sqxxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "sqxxid"));
        String sftj = Constants.DCHY_XMGL_CLCG_SFTJ_FALSE;

        if (StringUtils.isNotBlank(sqxxid)) {
            List<DchyXmglClcgTjjl> dchyXmglClcgTjjls = dchyXmglClgcpzMapper.queryClcgTjjl(sqxxid);
            if (CollectionUtils.isNotEmpty(dchyXmglClcgTjjls)) {
                DchyXmglClcgTjjl dchyXmglClcgTjjl = dchyXmglClcgTjjls.get(0);
                sftj = dchyXmglClcgTjjl.getSftj();
            }
        }
        Map resultMap = Maps.newHashMap();

        //?????????????????????
        List<ErrorInfoModel> wjcfList = Lists.newArrayList();
        List<ErrorInfoModel> wjqsList = Lists.newArrayList();
        List<ErrorInfoModel> gscwList = Lists.newArrayList();
        List<ErrorInfoModel> gdwwyqwjList = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(errorInfoModels)) {
            for (ErrorInfoModel errorInfoModel : errorInfoModels) {
                String msxx = errorInfoModel.getMsxx();
                switch (msxx) {
                    case Constants.DCHY_XMGL_CGSC_MSXX_WJCF:
                        wjcfList.add(errorInfoModel);
                        break; //??????
                    case Constants.DCHY_XMGL_CGSC_MSXX_WJQS:
                        wjqsList.add(errorInfoModel);
                        break; //??????
                    case Constants.DCHY_XMGL_CGSC_MSXX_GSCW:
                        gscwList.add(errorInfoModel);
                        break; //??????
                    case Constants.DCHY_XMGL_CGSC_MSXX_GDWWYQWJ:
                        gdwwyqwjList.add(errorInfoModel);
                        break; //??????
                    default: //??????
                        break;
                }
            }
        }

        //????????????????????????????????????????????????
        List<String> wbaClsxMcList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(wbaClsxList)) {
            for (String string : wbaClsxList) {
                String clsxMc = dchyXmglZdService.getZdmcByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, string);
                wbaClsxMcList.add(clsxMc);
            }
        }

        //??????????????????????????????clsx
        clsxList.removeAll(wbaClsxMcList);
        resultMap.put("clsx", clsxList);
        resultMap.put("wbaClsxList", wbaClsxMcList);
        resultMap.put("wjcfList", wjcfList);
        resultMap.put("wjqsList", wjqsList);
        resultMap.put("gscwList", gscwList);
        resultMap.put("gdwwyqwjList", gdwwyqwjList);
        resultMap.put("sftj", sftj);

        DchyXmglClcgTjjl dchyXmglClcgTjjl = null;

        Example exampleTjjl = new Example(DchyXmglClcgTjjl.class);
        exampleTjjl.createCriteria().andEqualTo("sqxxid", CommonUtil.formatEmptyValue(MapUtils.getString(map, "sqxxid")));
        List<DchyXmglClcgTjjl> dchyXmglClcgTjjlList = entityMapper.selectByExample(exampleTjjl);
        if (CollectionUtils.isNotEmpty(dchyXmglClcgTjjlList)) {
            dchyXmglClcgTjjl = dchyXmglClcgTjjlList.get(0);
            dchyXmglClcgTjjl.setChgcbh(CommonUtil.formatEmptyValue(MapUtils.getString(map, "gcbh")));
            dchyXmglClcgTjjl.setChgcid(CommonUtil.formatEmptyValue(MapUtils.getString(map, "chgcid")));
            dchyXmglClcgTjjl.setChxmid(CommonUtil.formatEmptyValue(MapUtils.getString(map, "chxmid")));
            dchyXmglClcgTjjl.setSqxxid(sqxxid);
            dchyXmglClcgTjjl.setCwxx(JSON.toJSONString(resultMap).getBytes(Charsets.UTF_8));
            dchyXmglClcgTjjl.setTjsj(CalendarUtil.getCurHMSDate());
            dchyXmglClcgTjjl.setSftj(Constants.DCHY_XMGL_CLCG_SFTJ_FALSE);
//                dchyXmglClgcpzMapper.updateDchyXmglClcgTjjl(dchyXmglClcgTjjl);
            entityMapper.saveOrUpdate(dchyXmglClcgTjjl, dchyXmglClcgTjjl.getTjjlid());
        } else {
            dchyXmglClcgTjjl = new DchyXmglClcgTjjl();
            dchyXmglClcgTjjl.setTjjlid(UUIDGenerator.generate18());
            dchyXmglClcgTjjl.setChgcbh(CommonUtil.formatEmptyValue(MapUtils.getString(map, "gcbh")));
            dchyXmglClcgTjjl.setChgcid(CommonUtil.formatEmptyValue(MapUtils.getString(map, "chgcid")));
            dchyXmglClcgTjjl.setChxmid(CommonUtil.formatEmptyValue(MapUtils.getString(map, "chxmid")));
            dchyXmglClcgTjjl.setSqxxid(sqxxid);
            dchyXmglClcgTjjl.setCwxx(JSON.toJSONString(resultMap).getBytes(Charsets.UTF_8));
            dchyXmglClcgTjjl.setTjsj(CalendarUtil.getCurHMSDate());
            dchyXmglClcgTjjl.setSftj(Constants.DCHY_XMGL_CLCG_SFTJ_FALSE);
//            dchyXmglClgcpzMapper.insertDchyXmglClcgTjjl(dchyXmglClcgTjjl);
            entityMapper.saveOrUpdate(dchyXmglClcgTjjl, dchyXmglClcgTjjl.getTjjlid());
        }

        return resultMap;
    }

    //???????????????clcgpz???????????????????????????
    private static Map generateDir(DchyXmglClcgpz dchyXmglClcgpz, Map mlMap, List<ErrorInfoModel> fileListPz, String ml) {
        ErrorInfoModel errorInfoModel;
        String mlmc = dchyXmglClcgpz.getClmc();
        String clcgpzid = dchyXmglClcgpz.getClcgpzid();
        List<DchyXmglClcgpz> dchyXmglClcgpzList = dchyXmglClgcpzMapper.queryclcgpzByPclcgpzid(clcgpzid);
        if (CollectionUtils.isNotEmpty(dchyXmglClcgpzList)) {
            Map mapTemp = Maps.newHashMap();
            List<String> fileList = Lists.newArrayList();
            Map map = Maps.newHashMap();
            for (DchyXmglClcgpz dchyXmglClcgpzTemp : dchyXmglClcgpzList) {
                String mlmcTemp = dchyXmglClcgpzTemp.getClmc();
                fileList.add(mlmcTemp);
                if (mlmcTemp.contains(".")) {
                    String ml1 = ml + mlmcTemp;
                    errorInfoModel = new ErrorInfoModel();
                    errorInfoModel.setClsx(dchyXmglClcgpzTemp.getClsx());
                    errorInfoModel.setWjmc(mlmcTemp);
                    errorInfoModel.setMlmc(ml1);
                    fileListPz.add(errorInfoModel);
                    if (dchyXmglClcgpzTemp.getClmc().equals(dchyXmglClcgpzList.get(dchyXmglClcgpzList.size() - 1).getClmc())) {
                        map.put(mlmc, fileList);
                    }
                } else {
                    String ml1 = ml + mlmcTemp + "/";
                    map = mapCopy(generateDir(dchyXmglClcgpzTemp, mapTemp, fileListPz, ml1));
                }
            }
            mlMap.put(mlmc, map.containsKey(mlmc) ? map.get(mlmc) : map);
        } else {
            mlMap.put(mlmc, "");
        }
        return mlMap;
    }

    //??????clsx??????clsxmc
    private static String getClsxMcByDm(String dm) {
        String mc = "";
        if (StringUtils.isNotBlank(dm)) {
            DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, dm);
            if (null != dchyXmglZd) {
                return dchyXmglZd.getMc();
            }
        }
        return mc;
    }

    /**
     * @param string ?????????
     * @param ch     ??????????????????
     * @param index  ?????????
     * @return
     * @description 2021/1/29  Java ?????????????????????N????????????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public static int getCharacterPosition(String string, String ch, int index) {
        //???????????????"/"???????????????
        Matcher slashMatcher = Pattern.compile(ch).matcher(string);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            //???"/"??????????????????????????????
            if (mIdx == index) {
                break;
            }
        }
        return slashMatcher.start() + 1;
    }

    //???????????????????????????????????????????????????????????????????????????
    public static Map checkGcbh(String gcbh, String chxmid) {
        Map resultMap = Maps.newHashMap();
        List<String> bjClsxList = Lists.newArrayList();
        List<Map> clsxMapList = Lists.newArrayList();

        if (StringUtils.isNotBlank(gcbh) || StringUtils.isNotBlank(chxmid)) {
            Example exampleChxm = new Example(DchyXmglChxm.class);
            Example.Criteria criteria = exampleChxm.createCriteria();
            if (StringUtils.isNotBlank(gcbh)) {
                criteria.andEqualTo("chgcbh", gcbh);
            }
            if (StringUtils.isNotBlank(chxmid)) {
                criteria.andEqualTo("chxmid", chxmid);
            }
            List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExampleNotNull(exampleChxm);
            if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                List<String> ybjChxmidList = Lists.newArrayList();
                List<String> yslChxmidList = Lists.newArrayList();
                for (DchyXmglChxm dchyXmglChxm : dchyXmglChxmList) {
                    if (StringUtils.equals(dchyXmglChxm.getXmzt(), Constants.DCHY_XMGL_CHXM_XMZT_YSL)) {
                        yslChxmidList.add(dchyXmglChxm.getChxmid());
                    } else if (StringUtils.equals(dchyXmglChxm.getXmzt(), Constants.DCHY_XMGL_CHXM_XMZT_YBJ)) {
                        ybjChxmidList.add(dchyXmglChxm.getChxmid());
                    }
                }
                if (CollectionUtils.isNotEmpty(ybjChxmidList)) {
                    Map param = Maps.newHashMap();
                    param.put("chxmidList", ybjChxmidList);
                    List<DchyXmglChxmClsx> dchyXmglChxmClsxList = dchyXmglChxmClsxMapper.getChxmClsxByHtxxGhxmid(param);
                    if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
                        for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                            bjClsxList.add(dchyXmglChxmClsx.getClsx());
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(yslChxmidList)) {
                    Map param = Maps.newHashMap();
                    param.put("chxmidList", yslChxmidList);
                    List<DchyXmglChxmClsx> dchyXmglChxmClsxList = dchyXmglChxmClsxMapper.getChxmClsxByHtxxGhxmid(param);
                    if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
                        for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                            if (!bjClsxList.contains(dchyXmglChxmClsx.getClsx())) {
                                Map map1 = Maps.newHashMap();
                                map1.put("clsx", dchyXmglChxmClsx.getClsx());
                                map1.put("chxmid", dchyXmglChxmClsx.getChxmid());
                                map1.put("clsxid", dchyXmglChxmClsx.getClsxid());
                                clsxMapList.add(map1);
                            }
                        }
                    }
                }
            }
        }
        resultMap.put("bjClsxList", bjClsxList);
        resultMap.put("clsxMapList", clsxMapList);
        return resultMap;
    }

    //??????clsx???????????????chxmid???clsxid
    public static Map queryChxmidByClsx(String clsx, String gcbh, String chxmid) {
        Map clsxMap = checkGcbh(gcbh, chxmid);
        List<Map> clsxMapList = (List<Map>) clsxMap.get("clsxMapList");
        Map resultMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(clsxMapList)) {
            for (Map map : clsxMapList) {
                String clsx1 = CommonUtil.formatEmptyValue(MapUtils.getString(map, "clsx"));
                if (StringUtils.equals(clsx1, clsx)) {
                    resultMap.put("chxmid", CommonUtil.formatEmptyValue(MapUtils.getString(map, "chxmid")));
                    resultMap.put("clsxid", CommonUtil.formatEmptyValue(MapUtils.getString(map, "clsxid")));
                    break;
                }
            }
        }
        return resultMap;
    }

    //?????????????????????????????????
    public static void changeXmzt(String chxmid, String clsx) {
        if (StringUtils.isNoneBlank(chxmid, clsx)) {
            Example exampleChxmClsx = new Example(DchyXmglChxmClsx.class);
            exampleChxmClsx.createCriteria().andEqualTo("clsx", clsx).andEqualTo("chxmid", chxmid);
            List<DchyXmglChxmClsx> dchyXmglChxmClsxList = entityMapper.selectByExample(exampleChxmClsx);
            if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
                for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                    dchyXmglChxmClsx.setCgtjzt(Constants.DCHY_XMGL_XMCGZT_DSH);
                    dchyXmglChxmClsx.setCgjfrq(CalendarUtil.getCurHMSDate());
                    entityMapper.saveOrUpdate(dchyXmglChxmClsx, dchyXmglChxmClsx.getClsxid());
                }
            }
        }
    }

    //??????????????????????????????
    public static void delTjjl(String glsxid) {
        Example exampleTjjl = new Example(DchyXmglClcgTjjl.class);
        exampleTjjl.createCriteria().andEqualTo("sqxxid", glsxid);
        entityMapper.deleteByExample(exampleTjjl);
    }

    //?????????????????????????????????,???????????????????????????????????????
    public static String queryShztByClcg(String clsx, String gcbh, String chxmid) {
        String shzt = Constants.DCHY_XMGL_SQZT_DSH;
        if (StringUtils.isNotBlank(clsx) && (StringUtils.isNotBlank(gcbh) || StringUtils.isNotBlank(chxmid))) {
            DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndMc(Constants.DCHY_XMGL_CHXM_CLSX, clsx);
            if (null != dchyXmglZd) {
                String clsxDm = dchyXmglZd.getDm();
                Example example = new Example(DchyXmglClcg.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("clsx", clsxDm);
                if (StringUtils.isNotBlank(gcbh)) {
                    criteria.andEqualTo("chgcbh", gcbh);
                }
                if (StringUtils.isNotBlank(chxmid)) {
                    criteria.andEqualTo("chxmid", chxmid);
                }
                List<DchyXmglClcg> dchyXmglClcgList = entityMapper.selectByExampleNotNull(example);
                if (CollectionUtils.isNotEmpty(dchyXmglClcgList)) {
                    DchyXmglClcg dchyXmglClcg = dchyXmglClcgList.get(0);
                    shzt = dchyXmglClcg.getShzt();
                }
            }
        }
        return shzt;
    }

    //?????????????????????????????????,?????????????????????????????????
    private static boolean clsxSfba(String gcbh, String slbh, String clsx) {
        boolean flag = false;
        Map paramMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(clsx) && (StringUtils.isNotBlank(gcbh) || StringUtils.isNotBlank(slbh))) {
            paramMap.put("slbh", slbh);
            paramMap.put("gcbh", gcbh);
            paramMap.put("clsx", clsx);
        }
        List<Map> mapList = dchyXmglChgcMapper.queryHtxxByChgcbhAndClsx(paramMap);
        if (CollectionUtils.isNotEmpty(mapList)) {
            flag = true;
        }
        return flag;
    }

    //?????????????????????????????????????????????
    private static List<Map<String, Object>> filterFiles(List<Map<String, Object>> uploadFiles, List<String> wbaClsxList) {
        List<Map<String, Object>> result = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(uploadFiles)) {
            for (Map<String, Object> map : uploadFiles) {
                String mlmc = MapUtils.getString(map, "mlmc");
                String[] strings = mlmc.split("/");
                if (strings.length > 2) {
                    String clsxDm = dchyXmglZdService.getZddmByZdlxAndMc(Constants.DCHY_XMGL_CHXM_CLSX, strings[2]);
                    if (!wbaClsxList.contains(clsxDm)) {
                        result.add(map);
                    }
                } else {
                    result.add(map);
                }

            }
        }
        return result;
    }

    //???????????????????????????????????????????????????????????????
    private static boolean projectIsCompletion(String slbh) {
        boolean flag = false;
        if (StringUtils.isNotBlank(slbh)) {
            Example exampleChxm = new Example(DchyXmglChxm.class);
            exampleChxm.createCriteria().andEqualTo("slbh", slbh);
            List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExample(exampleChxm);
            if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                DchyXmglChxm dchyXmglChxm = dchyXmglChxmList.get(0);
                if (StringUtils.equals(dchyXmglChxm.getXmzt(), Constants.DCHY_XMGL_CHXM_XMZT_YBJ)) {
                    flag = true;
                }
            }
        }
        return flag;
    }
}
