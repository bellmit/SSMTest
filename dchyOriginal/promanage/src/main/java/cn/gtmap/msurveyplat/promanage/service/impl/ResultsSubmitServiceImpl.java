package cn.gtmap.msurveyplat.promanage.service.impl;

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
import cn.gtmap.msurveyplat.promanage.web.utils.EhcacheUtil;
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

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static cn.gtmap.msurveyplat.promanage.utils.PlatformUtil.getFileService;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/4/21
 * @description 成果提交工具类
 */
public class ResultsSubmitServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultsSubmitServiceImpl.class);

    private static PlatformUtil platformUtil = (PlatformUtil) Container.getBean("platformUtil");

    private static EntityMapper entityMapper = (EntityMapper) Container.getBean("entityMapper");

    private static DchyXmglClgcpzMapper dchyXmglClgcpzMapper = (DchyXmglClgcpzMapper) Container.getBean("dchyXmglClgcpzMapper");

    private static DchyXmglChgcMapper dchyXmglChgcMapper = (DchyXmglChgcMapper) Container.getBean("dchyXmglChgcMapper");

    private static DchyXmglChxmClsxMapper dchyXmglChxmClsxMapper = (DchyXmglChxmClsxMapper) Container.getBean("dchyXmglChxmClsxMapper");

    private static DchyXmglZdServiceImpl dchyXmglZdService = (DchyXmglZdServiceImpl) Container.getBean("dchyXmglZdServiceImpl");

    /**
     * @param xmid
     * @return
     * @description 2021/1/28 初始化展示已提交的错误信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public static Map<String, Object> initCgtj(String xmid) {
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotBlank(xmid)) {
            List<DchyXmglClcgTjjl> dchyXmglClcgTjjls = dchyXmglClgcpzMapper.queryClcgTjjl(xmid);
            if (CollectionUtils.isNotEmpty(dchyXmglClcgTjjls)) {
                DchyXmglClcgTjjl dchyXmglClcgTjjl = dchyXmglClcgTjjls.get(0);
                if (StringUtils.equals(dchyXmglClcgTjjl.getSftj(), Constants.DCHY_XMGL_CLCG_SFTJ_FALSE)) {
                    String str = new String(dchyXmglClcgTjjl.getCwxx());
                    JSONObject jsonObject = JSONObject.parseObject(str);
                    map = (Map<String, Object>) jsonObject;
                    map.put("gcbh", dchyXmglClcgTjjl.getChgcbh());
                }
            } else {
                //当无法获取提交记录获取测量成果表中的clsx
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
     * @description 2021/1/8 测量成果检查(界首市(单位工程编号))
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public static ResponseMessage checkZipFiles(String uploadFileName, InputStream inputStream, Map map) throws IOException {
        //字节流读取完关闭,用bytes存储
        byte[] zipBytes = read(inputStream);
        inputStream = new ByteArrayInputStream(zipBytes);
        ResponseMessage message = new ResponseMessage();
        Map resultMap = Maps.newHashMap();
        Map<String, Object> paramMap = Maps.newHashMap();
        List<ErrorInfoModel> errorInfoModels = Lists.newArrayList();

        String bh = uploadFileName.substring(0, uploadFileName.indexOf("."));
        String gcbh = null;
        String babh = null;
        String chxmid = null;
        if (StringUtils.equalsIgnoreCase(ResultsSubmitServiceUtil.getCgtjMode(), Constants.CGTJ_MODE_CHGC)) {
            gcbh = bh;
        } else if (StringUtils.equalsIgnoreCase(ResultsSubmitServiceUtil.getCgtjMode(), Constants.CGTJ_MODE_CHXM)) {
            babh = bh;
            if (projectIsCompletion(babh)) {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.FILESUBMIT_FAIL.getMsg(), ResponseMessage.CODE.FILESUBMIT_FAIL.getCode());
                return message;
            }

            //判断是否挂起
            if (projectIsGq(babh)) {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.XMGL_GQ.getMsg(), ResponseMessage.CODE.XMGL_GQ.getCode());
                return message;
            }

            Example example = new Example(DchyXmglChxm.class);
            example.createCriteria().andEqualTo("babh", babh);
            List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                chxmid = dchyXmglChxmList.get(0).getChxmid();
            } else {
                chxmid = Constants.EMPTYPARAM_VALUE;
            }
        }
        paramMap.put("gcbh", gcbh);
        paramMap.put("babh", babh);
        List<Map<String, Object>> clcgList = dchyXmglChgcMapper.queryClcgByGcbh(paramMap);
        //判断chxm中是否有此gcbh
        if (CollectionUtils.isNotEmpty(clcgList)) {
            //上传的目录
            List<Map<String, Object>> uploadFiles = Lists.newArrayList();
            List<String> clsxListShow = Lists.newArrayList();

            String glsxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "sqxxid"));
            String codes = queryCheckConfig();
            paramMap.put("sqxxid", glsxid);
            generateClml(glsxid, bh);
            Cache.ValueWrapper valueWrapper = EhcacheUtil.getDataFromEhcache(glsxid);
            Map<String, Object> mycache = (Map) valueWrapper.get();
            Map<String, Object> mlMap = MapUtils.getMap(mycache, "mlMap");
            //如果clcgpz表中没有数据直接提示
            if (MapUtils.isEmpty(mlMap)) {
                return ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.RESULT_SUBMIT_CONFIG_EMPTY.getMsg(), ResponseMessage.CODE.RESULT_SUBMIT_CONFIG_EMPTY.getCode());
            }
            LOGGER.info("***************配置表中的成果包配置:" + JSON.toJSONString(mlMap));
            List<ErrorInfoModel> fileListSc = Lists.newArrayList();
            List<String> clsxList = Lists.newArrayList();
            List<String> wbaClsxList = Lists.newArrayList();

            //解决zip文件中有中文目录或者中文文件
            ZipInputStream zin = new ZipInputStream(inputStream, Charset.forName("GBK"));

            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                ErrorInfoModel errorInfoModel = new ErrorInfoModel();
                String zipEntryName = entry.getName();
                byte[] bytes = IOUtils.toByteArray(zin);
                LOGGER.info(zipEntryName);
                String[] strings = zipEntryName.split("/");
                if (!StringUtils.equals(strings[0], bh)) {
                    message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.RESULT_SUBMIT_NOT_CONSISTENCY_FAIL.getMsg(), ResponseMessage.CODE.RESULT_SUBMIT_NOT_CONSISTENCY_FAIL.getCode());
                    return message;
                }

                if (strings.length < 2) {
                    continue;
                } else {
                    if (MapUtils.isNotEmpty(mlMap) && mlMap.containsKey(strings[1])) {
                        Map<String, Object> yjmlMap = MapUtils.getMap(mlMap, strings[1]);
                        if (strings.length > 2) {
                            if (!clsxList.contains(strings[2])) {
                                String clsx = dchyXmglZdService.getZddmByZdlxAndMc(Constants.DCHY_XMGL_CHXM_CLSX, strings[2]);
                                LOGGER.info("**********当前检测的测量事项为:" + clsx + "," + babh + "," + gcbh + "**********");
                                boolean clsxSdba = clsxSfba(gcbh, babh, clsx);
                                LOGGER.info("**********当前检测的测量事项检测结果为:" + clsxSdba + "," + clsx + "," + babh + "," + gcbh + "**********");
                                //如果未备案  xx测绘事项尚未备案，成果无法提交
                                if (!clsxSdba && !wbaClsxList.contains(clsx)) {
                                    wbaClsxList.add(clsx);

                                }
                                clsxList.add(strings[2]);
                            }
                            if (MapUtils.isNotEmpty(yjmlMap) && yjmlMap.containsKey(strings[2])) {
                                Map<String, Object> ejmlMap = MapUtils.getMap(yjmlMap, strings[2]);
                                //zxce20290001/立项用地规划许可/土地勘测定界/全国土地第三次调查报告.pdf
                                //zxce20290001/立项用地规划许可/土地勘测定界/扫描资料/不动产权籍调查报告.pdf
                                List<String> clsxMlList = Lists.newArrayList();
                                if (strings.length == 3) {
                                    clsxMlList.add(zipEntryName);
                                }
                                if (strings.length > 3) {
                                    //测量事项下没有任何文件则认为未备案该测量事项,不报文件缺失
                                    if (!clsxListShow.contains(strings[2])) {
                                        clsxListShow.add(strings[2]);
                                    }
                                    if (strings[3].contains(".")) {
                                        String fileName = strings[3];
                                        Map<String, Object> uploadMap = Maps.newHashMap();
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
                                    } else {
                                        if (MapUtils.isNotEmpty(ejmlMap) && ejmlMap.containsKey(strings[3])) {
                                            List<String> filenameList = Lists.newArrayList();
                                            if (null != ejmlMap.get(strings[3]) && "" != ejmlMap.get(strings[3])) {
                                                filenameList = (List<String>) ejmlMap.get(strings[3]);
                                            }
                                            if (CollectionUtils.isNotEmpty(filenameList)) {
                                                if (strings.length > 4) {
                                                    if (CollectionUtils.isNotEmpty(filenameList)) {
                                                        //去除后缀名
                                                        List<String> fileList = Lists.newArrayList();
                                                        for (String str : filenameList) {
                                                            fileList.add(str.substring(0, str.indexOf(".")));
                                                        }
                                                        if (fileList.contains(strings[4].substring(0, strings[4].indexOf(".")))) {
                                                            String fileName = strings[strings.length - 1];
                                                            Map<String, Object> uploadMap = Maps.newHashMap();
                                                            uploadMap.put("bytes", bytes);
                                                            uploadMap.put("glsxid", glsxid);
                                                            uploadMap.put("mlmc", zipEntryName);
                                                            uploadMap.put("fileName", fileName);
                                                            uploadFiles.add(uploadMap);
                                                            errorInfoModel.setClsx(strings[2]);
                                                            errorInfoModel.setWjmc(fileName);
                                                            errorInfoModel.setMlmc(zipEntryName);
                                                            fileListSc.add(errorInfoModel);
                                                        } else {
                                                            String fileName = strings[strings.length - 1];
                                                            Map<String, Object> uploadMap = Maps.newHashMap();
                                                            uploadMap.put("bytes", bytes);
                                                            uploadMap.put("glsxid", glsxid);
                                                            uploadMap.put("mlmc", zipEntryName);
                                                            uploadMap.put("fileName", fileName);
                                                            uploadFiles.add(uploadMap);
                                                        }
                                                    } else {
                                                        String fileName = strings[strings.length - 1];
                                                        Map<String, Object> uploadMap = Maps.newHashMap();
                                                        uploadMap.put("bytes", bytes);
                                                        uploadMap.put("glsxid", glsxid);
                                                        uploadMap.put("mlmc", zipEntryName);
                                                        uploadMap.put("fileName", fileName);
                                                        uploadFiles.add(uploadMap);
                                                    }
                                                } else {
                                                    Map<String, Object> uploadMap = Maps.newHashMap();
                                                    uploadMap.put("glsxid", glsxid);
                                                    uploadMap.put("mlmc", zipEntryName);
                                                    uploadFiles.add(uploadMap);
                                                }
                                            } else {
                                                //排除第四层空目录(这种情况是数据库中没有配置的文件)
                                                if (strings.length > 4) {
                                                    String fileName = strings[strings.length - 1];
                                                    Map<String, Object> uploadMap = Maps.newHashMap();
                                                    uploadMap.put("bytes", bytes);
                                                    uploadMap.put("glsxid", glsxid);
                                                    uploadMap.put("mlmc", zipEntryName);
                                                    uploadMap.put("fileName", fileName);
                                                    uploadFiles.add(uploadMap);
                                                }
                                            }
                                        } else {
                                            String fileName = strings[strings.length - 1];
                                            Map<String, Object> uploadMap = Maps.newHashMap();
                                            uploadMap.put("bytes", bytes);
                                            uploadMap.put("glsxid", glsxid);
                                            uploadMap.put("mlmc", zipEntryName);
                                            uploadMap.put("fileName", fileName);
                                            uploadFiles.add(uploadMap);
                                        }
                                    }
                                } else {
                                    Map<String, Object> uploadMap = Maps.newHashMap();
                                    uploadMap.put("glsxid", glsxid);
                                    uploadMap.put("mlmc", zipEntryName);
                                    uploadFiles.add(uploadMap);
                                }
                            }
                        } else {
                            Map<String, Object> uploadMap = Maps.newHashMap();
                            uploadMap.put("glsxid", glsxid);
                            uploadMap.put("mlmc", zipEntryName);
                            uploadFiles.add(uploadMap);
                        }
                    }
                }
            }

            Map<String, Object> clsxMapList = checkGcbh(gcbh, chxmid);
            LOGGER.info("通过工程编号获取已经办结的测量事项和所有的测量事项:" + JSON.toJSONString(clsxMapList));
            List<String> bjClsxList = (List<String>) clsxMapList.get("bjClsxList");

            //如果成果包中包含已经办结的测量事项
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
            LOGGER.info("************上传的文件--fileListSc:" + JSON.toJSONString(fileListSc));
            LOGGER.info("************可以展示的clsx--clsxListShow:" + JSON.toJSONString(fileListSc));
            LOGGER.info("************流程对应的xmid--key:" + JSON.toJSONString(glsxid));

            if (StringUtils.isNotBlank(codes) && null != zipBytes) {
                String[] codeList = codes.split(";");
                if (null != codeList && codeList.length > 0) {
                    for (String string : codeList) {
                        List<ErrorInfoModel> errorInfoModelList1 = Lists.newArrayList();
                        if (null != CheckZipFileServiceImpl.getResultsSubmitServiceByCode(string)) {
                            errorInfoModelList1 = CheckZipFileServiceImpl.getResultsSubmitServiceByCode(string).checkZipFiles(zipBytes, bh, mlMap, babh, gcbh, chxmid, glsxid);
                        }
                        if (CollectionUtils.isNotEmpty(errorInfoModelList1)) {
                            errorInfoModels.addAll(errorInfoModelList1);
//                            if (StringUtils.equals(string, Constants.DCHY_XMGL_YWYZXX_CGTJ_GSCW) || StringUtils.equals(string, Constants.DCHY_XMGL_YWYZXX_CGTJ_WJQS)) {
//                                removeSameErrorInfoModel(errorInfoModels, errorInfoModelList1, wbaClsxList);
//                            }
                        }
                    }
                }
            }

            resultMap.put("data", formatErrorInfoModel(errorInfoModels, paramMap, clsxList, wbaClsxList));
            message = ResponseUtil.wrapSuccessResponse();
            message.setData(resultMap);
            LOGGER.info("******************解压完毕********************");

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
     * @description 2021/1/13 整体提交
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
                Map<String, Object> map = (Map<String, Object>) jsonObject;
                map.put("sftj", Constants.DCHY_XMGL_CLCG_SFTJ_TRUE);
                try {
                    dchyXmglClcgTjjl.setCwxx(JSON.toJSONString(map).getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("错误原因{}：", e);
                }
                entityMapper.saveOrUpdate(dchyXmglClcgTjjl, dchyXmglClcgTjjl.getTjjlid());
            }
        }
        List<String> notCoverFiles = generateNotCoverFiles(errorInfoModels);
        Cache.ValueWrapper valueWrapper = EhcacheUtil.getDataFromEhcache(glsxid);
        List<Map<String, Object>> uploadFiles = null;
        if (null != valueWrapper) {
            Map<String, Object> mycache = (Map<String, Object>) valueWrapper.get();
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
                    LOGGER.info("*********当前提交的目录名称***********" + mlmc);
                    InputStream in = write(bytes);
                    String[] strings = mlmc.split("/");
                    String bh = strings[0];
                    String gcbh = null;
                    String babh = null;
                    String chxmid = null;
                    if (StringUtils.equalsIgnoreCase(ResultsSubmitServiceUtil.getCgtjMode(), Constants.CGTJ_MODE_CHGC)) {
                        gcbh = bh;
                    } else if (StringUtils.equalsIgnoreCase(ResultsSubmitServiceUtil.getCgtjMode(), Constants.CGTJ_MODE_CHXM)) {
                        babh = bh;
                        Example example = new Example(DchyXmglChxm.class);
                        example.createCriteria().andEqualTo("babh", babh);
                        List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExampleNotNull(example);
                        if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                            chxmid = dchyXmglChxmList.get(0).getChxmid();
                        }
                    }
                    Map<String, Object> map1 = uploadZip(in, mlmc, fileName);
                    String parentId = CommonUtil.formatEmptyValue(MapUtils.getString(map1, "parentId"));
                    String folderId = CommonUtil.formatEmptyValue(MapUtils.getString(map1, "folderId"));
                    //同步收件材料和收件信息
                    if (StringUtils.isNotBlank(glsxid)) {
                        dchyXmglSjcl = generateSjclAndSjxx(glsxid);
                        dchyXmglClcg = generateClcg(fileName, mlmc, gcbh, chxmid, glsxid);
                        dchyXmglSjcl.setClmc(fileName);
                        dchyXmglSjcl.setWjzxid(parentId);

                        dchyXmglClcg.setWjzxid(folderId);
                        dchyXmglClcg.setSjclid(dchyXmglSjcl.getSjclid());
                        dchyXmglClcg.setSjxxid(dchyXmglSjcl.getSjxxid());
                        LOGGER.info("*******成果提交时间********" + dchyXmglClcg.getRksj());
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

    //从配置表中读取zip文件目录  和表中配置文件的目录
    public static void generateClml(String glsxid, String bh) {
        Map<String, Object> resultMap = Maps.newHashMap();
        //配置中文件
        List<ErrorInfoModel> fileListPz = Lists.newArrayList();
        Map<String, Object> mlMap = Maps.newHashMap();
        List<DchyXmglClcgpz> dchyXmglClcgpzList1 = dchyXmglClgcpzMapper.queryclcgpz();
        //一级目录
        if (CollectionUtils.isNotEmpty(dchyXmglClcgpzList1)) {
            for (DchyXmglClcgpz dchyXmglClcgpz : dchyXmglClcgpzList1) {
                generateDir(dchyXmglClcgpz, mlMap, fileListPz, bh + "/" + dchyXmglClcgpz.getClmc() + "/");
            }
        }
        resultMap.put("mlMap", mlMap);
        resultMap.put("fileListPz", fileListPz);
        EhcacheUtil.putDataToEhcache(glsxid, resultMap);
    }

    //byte[] 转为InputStream   zip会关闭InputStream无法保存所以用byte[]中转
    public static InputStream write(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    //移除对象
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

    //通过chxmid和clsx在表里读取对应的clsx
    public static Map<String, Object> convertClsx(String clsx, String gcbh, String chxmid) {
        Map<String, Object> resultMap = Maps.newHashMap();

        DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndMc(Constants.DCHY_XMGL_CHXM_CLSX, clsx);
        if (StringUtils.isNotBlank(dchyXmglZd.getDm()) && (StringUtils.isNotBlank(gcbh) || StringUtils.isNotBlank(chxmid))) {
            resultMap.put("clsx", dchyXmglZd.getDm());
            resultMap.put("gcbh", gcbh);
            Map<String, Object> clsxMap = queryChxmidByClsx(dchyXmglZd.getDm(), gcbh, chxmid);
            resultMap.put("clsxid", CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "clsxid")));
            resultMap.put("chxmid", CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "chxmid")));
        }
        return resultMap;

    }

    //生成新的收件材料和收件信息
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

    //验证该文件是否存在(文件中心)
    public static boolean fileIsExist(String mlmc) {
        boolean fileisExist = false;
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
                        LOGGER.error("错误原因:{}", e);
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
     * 复制map对象
     *
     * @param paramsMap 被拷贝对象
     * @return resultMap
     * 拷贝后的对象
     * @explain 将paramsMap中的键值对全部拷贝到resultMap中；
     * paramsMap中的内容不会影响到resultMap（深拷贝）
     */
    public static Map<String, Object> mapCopy(Map<String, Object> paramsMap) {
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

    //获取页面中不覆盖的文件
    public static List<String> generateNotCoverFiles(List<Map<String, String>> errorInfoModels) {
        List<String> notCoverFiles = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(errorInfoModels)) {
            for (Map<String, String> map : errorInfoModels) {
                if (StringUtils.equals(MapUtils.getString(map, "msxx"), Constants.DCHY_XMGL_CGSC_MSXX_WJCF)) {
                    notCoverFiles.add(MapUtils.getString(map, "mlmc"));
                }
            }
        }
        return notCoverFiles;
    }

    //生成入库的测量成果数据
    public static DchyXmglClcg generateClcg(String fileName, String mlmc, String gcbh, String chxmid, String glsxid) {
        LOGGER.info("**********fileName:" + fileName + "*********");
        LOGGER.info("**********mlmc:" + mlmc + "*********");
        LOGGER.info("**********gcbh:" + gcbh + "*********");
        LOGGER.info("**********chxmid:" + chxmid + "*********");
        Map<String, Object> map = generateChcg(gcbh, chxmid);
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
            Map<String, Object> clsxMap = convertClsx(strings[2], gcbh, chxmid);
            if (MapUtils.isNotEmpty(clsxMap)) {
                dchyXmglClcg.setClsx(CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "clsx")));
                dchyXmglClcg.setClsxid(CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "clsxid")));
                dchyXmglClcg.setChxmid(CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "chxmid")));
            }
        }
        return dchyXmglClcg;
    }

    //通过工程编号获取工程名称和id
    public static Map<String, Object> generateChcg(String gcbh, String chxmid) {
        Map<String, Object> map = Maps.newHashMap();
        Map<String, Object> param = Maps.newHashMap();
        param.put("gcbh", gcbh);
        param.put("chxmid", chxmid);
        List<DchyXmglChgc> chgcList = dchyXmglChgcMapper.queryChgcxx(param);
        if (CollectionUtils.isNotEmpty(chgcList)) {
            for (DchyXmglChgc chgcxx : chgcList) {
                map.put("chgcbh", chgcxx.getGcbh());
                map.put("chgcid", chgcxx.getChgcid());
                break;
            }
        }
        return map;
    }

    //上传zip
    public static Map<String, Object> uploadZip(InputStream inputStream, String mlmc, String fileName) {
        String[] strings = mlmc.split("/");
        HashMap<String, Object> map = Maps.newHashMap();
        try {
            FileService fileService = getFileService();
            Integer parentId = null;
            Integer folderId = null;
            if (strings.length > 1) {
                // 文件目录创建
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
                        LOGGER.error("错误原因:{}", e);
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
            LOGGER.error("错误原因:{}", e);
        }
        return map;
    }

    //判断文件是否在文件中心
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

    //格式化错误信息并入库
    public static Map<String, Object> formatErrorInfoModel(List<ErrorInfoModel> errorInfoModels, Map<String, Object> map, List<String> clsxList, List<String> wbaClsxList) {
        String sqxxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "sqxxid"));
        String sftj = Constants.DCHY_XMGL_CLCG_SFTJ_FALSE;

        if (StringUtils.isNotBlank(sqxxid)) {
            List<DchyXmglClcgTjjl> dchyXmglClcgTjjls = dchyXmglClgcpzMapper.queryClcgTjjl(sqxxid);
            if (CollectionUtils.isNotEmpty(dchyXmglClcgTjjls)) {
                DchyXmglClcgTjjl dchyXmglClcgTjjl = dchyXmglClcgTjjls.get(0);
                sftj = dchyXmglClcgTjjl.getSftj();
            }
        }
        Map<String, Object> resultMap = Maps.newHashMap();
        List<String> mlmcList = Lists.newArrayList();

        //四种错误的情况
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
                        mlmcList.add(errorInfoModel.getMlmc());
                        break; //可选
                    case Constants.DCHY_XMGL_CGSC_MSXX_WJQS:
                        wjqsList.add(errorInfoModel);
                        break; //可选
                    case Constants.DCHY_XMGL_CGSC_MSXX_GSCW:
                        gscwList.add(errorInfoModel);
                        break; //可选
                    case Constants.DCHY_XMGL_CGSC_MSXX_GDWWYQWJ:
                        gdwwyqwjList.add(errorInfoModel);
                        break; //可选
                    default: //可选
                        break;
                }
            }
        }

        //测量事项代码换成名称方便前台展示
        List<String> wbaClsxMcList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(wbaClsxList)) {
            for (String string : wbaClsxList) {
                String clsxMc = dchyXmglZdService.getZdmcByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, string);
                wbaClsxMcList.add(clsxMc);
            }
        }

        //成果包中去除未备案的clsx
        clsxList.removeAll(wbaClsxMcList);
        resultMap.put("clsx", clsxList);
        resultMap.put("wbaClsxList", wbaClsxMcList);
        resultMap.put("wjcfList", wjcfList);
        resultMap.put("wjqsList", wjqsList);
        resultMap.put("gscwList", filterRepet(gscwList, mlmcList));
        resultMap.put("gdwwyqwjList", filterRepet(gdwwyqwjList, mlmcList));
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
            entityMapper.saveOrUpdate(dchyXmglClcgTjjl, dchyXmglClcgTjjl.getTjjlid());
        }
        return resultMap;
    }

    //用递归取出clcgpz表中的成果包的配置
    private static Map<String, Object> generateDir(DchyXmglClcgpz dchyXmglClcgpz, Map<String, Object> mlMap, List<ErrorInfoModel> fileListPz, String ml) {
        ErrorInfoModel errorInfoModel;
        String mlmc = dchyXmglClcgpz.getClmc();
        String clcgpzid = dchyXmglClcgpz.getClcgpzid();
        List<DchyXmglClcgpz> dchyXmglClcgpzList = dchyXmglClgcpzMapper.queryclcgpzByPclcgpzid(clcgpzid);
        if (CollectionUtils.isNotEmpty(dchyXmglClcgpzList)) {
            Map<String, Object> mapTemp = Maps.newHashMap();
            List<String> fileList = Lists.newArrayList();
            Map<String, Object> map = Maps.newHashMap();
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

    //通过工程编号获取已经办结的测量事项和所有的测量事项
    public static Map<String, Object> checkGcbh(String gcbh, String chxmid) {
        Map<String, Object> resultMap = Maps.newHashMap();
        List<String> bjClsxList = Lists.newArrayList();
        List<Map<String, Object>> clsxMapList = Lists.newArrayList();

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
                    Map<String, Object> param = Maps.newHashMap();
                    param.put("chxmidList", ybjChxmidList);
                    List<DchyXmglChxmClsx> dchyXmglChxmClsxList = dchyXmglChxmClsxMapper.getChxmClsxByHtxxGhxmid(param);
                    if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
                        for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                            bjClsxList.add(dchyXmglChxmClsx.getClsx());
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(yslChxmidList)) {
                    Map<String, Object> param = Maps.newHashMap();
                    param.put("chxmidList", yslChxmidList);
                    List<DchyXmglChxmClsx> dchyXmglChxmClsxList = dchyXmglChxmClsxMapper.getChxmClsxByHtxxGhxmid(param);
                    if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
                        for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                            if (!bjClsxList.contains(dchyXmglChxmClsx.getClsx())) {
                                Map<String, Object> map1 = Maps.newHashMap();
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

    //通过clsx获取对应的chxmid和clsxid
    public static Map<String, Object> queryChxmidByClsx(String clsx, String gcbh, String chxmid) {
        Map<String, Object> clsxMap = checkGcbh(gcbh, chxmid);
        List<Map<String, Object>> clsxMapList = (List<Map<String, Object>>) clsxMap.get("clsxMapList");
        Map<String, Object> resultMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(clsxMapList)) {
            for (Map<String, Object> map : clsxMapList) {
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

    //成果提交后修改成果状态
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

    //提交成功后删除记录表
    public static void delTjjl(String glsxid) {
        Example exampleTjjl = new Example(DchyXmglClcgTjjl.class);
        exampleTjjl.createCriteria().andEqualTo("sqxxid", glsxid);
        entityMapper.deleteByExample(exampleTjjl);
    }

    //根据工程编号和测量事项,获取当前测量成果的审核状态
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

    //根据工程编号和测量事项,判断该测量事项是否备案
    public static boolean clsxSfba(String gcbh, String babh, String clsx) {
        boolean flag = false;
        Map<String, Object> paramMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(clsx) && (StringUtils.isNotBlank(gcbh) || StringUtils.isNotBlank(babh))) {
            paramMap.put("babh", babh);
            paramMap.put("gcbh", gcbh);
            paramMap.put("clsx", clsx);
        }
        List<Map<String, Object>> mapList = dchyXmglChgcMapper.queryHtxxByChgcbhAndClsx(paramMap);
        if (CollectionUtils.isNotEmpty(mapList)) {
            flag = true;
        }
        return flag;
    }

    //过滤未备案的测量事项的测量成果
    public static List<Map<String, Object>> filterFiles(List<Map<String, Object>> uploadFiles, List<String> wbaClsxList) {
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

    //常州的以受理编号为单位的成果包判断是否办结
    public static boolean projectIsCompletion(String babh) {
        boolean flag = false;
        if (StringUtils.isNotBlank(babh)) {
            Example exampleChxm = new Example(DchyXmglChxm.class);
            exampleChxm.createCriteria().andEqualTo("babh", babh);
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

    //常州的以受理编号为单位的成果包判断是否挂起
    public static boolean projectIsGq(String babh) {
        boolean flag = false;
        if (StringUtils.isNotBlank(babh)) {
            Example exampleChxm = new Example(DchyXmglChxm.class);
            exampleChxm.createCriteria().andEqualTo("babh", babh);
            List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExample(exampleChxm);
            if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                DchyXmglChxm dchyXmglChxm = dchyXmglChxmList.get(0);
                if (StringUtils.equals(dchyXmglChxm.getSfgq(), Constants.DCHY_XMGL_GQ)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    //去掉重复的问题
    public static void removeSameErrorInfoModel(List<ErrorInfoModel> errorInfoModels, List<ErrorInfoModel> errorInfoModelList, List<String> wbaClsxList) {
        if (CollectionUtils.isNotEmpty(errorInfoModelList) && CollectionUtils.isNotEmpty(errorInfoModels)) {
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
    }

    //从配置表中获取配置项
    public static String queryCheckConfig() {
        String code = "";
        Example exampleYwyzxx = new Example(DchyXmglYwyzxx.class);
        exampleYwyzxx.createCriteria().andEqualTo("ssmkid", SsmkidEnum.CHTJCGSH.getCode()).andEqualTo("sfqy", Constants.DCHY_XMGL_CHTJCGSH_SFQY_QY);
        List<DchyXmglYwyzxx> dchyXmglYwyzxxList = entityMapper.selectByExample(exampleYwyzxx);
        if (CollectionUtils.isNotEmpty(dchyXmglYwyzxxList)) {
            for (DchyXmglYwyzxx dchyXmglYwyzxx : dchyXmglYwyzxxList) {
                code = code + dchyXmglYwyzxx.getYwyzxxid() + ";";
            }
        }
        return code;
    }

    //InputStream转bytes
    public static byte[] read(InputStream inputStream) throws IOException {
        if (null != inputStream) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int num = inputStream.read(buffer);
                while (num != -1) {
                    baos.write(buffer, 0, num);
                    num = inputStream.read(buffer);
                }
                baos.flush();
                return baos.toByteArray();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } else {
            return null;
        }

    }

    //去掉重复的问题
    public static List<ErrorInfoModel> filterRepet(List<ErrorInfoModel> errorInfoModels, List<String> mlmcList) {
        List<ErrorInfoModel> errorInfoModels1 = Lists.newArrayList();
        for (ErrorInfoModel errorInfoModel : errorInfoModels) {
            if (!mlmcList.contains(errorInfoModel.getMlmc())) {
                errorInfoModels1.add(errorInfoModel);
            }
        }
        return errorInfoModels1;
    }
}
