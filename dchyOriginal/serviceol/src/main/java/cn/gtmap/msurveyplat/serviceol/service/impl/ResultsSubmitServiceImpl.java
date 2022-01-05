package cn.gtmap.msurveyplat.serviceol.service.impl;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglCgJcDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglCgtjDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.*;
import cn.gtmap.msurveyplat.serviceol.core.service.FileUploadService;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.Impl.PushDataToMqServiceImpl;
import cn.gtmap.msurveyplat.serviceol.core.xsbfmapper.DchyXmglCltjXSBFMapper;
import cn.gtmap.msurveyplat.serviceol.service.ResultsSubmitService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import cn.gtmap.msurveyplat.serviceol.web.util.EhcacheUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.fileCenter.model.Node;
import com.gtis.fileCenter.service.FileService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil.getFileService;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/1/8
 * @description 成果提交实现类
 */
@Service
public class ResultsSubmitServiceImpl implements ResultsSubmitService {

    private static final Logger logger = LoggerFactory.getLogger(ResultsSubmitServiceImpl.class);

    @Autowired
    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapperXSBF;

    @Autowired
    private PushDataToMqServiceImpl pushDataToMqService;

    @Autowired
    private DchyXmglCltjXSBFMapper dchyXmglCltjXSBFMapper;

    @Autowired
    private PlatformUtil platformUtil;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public ResponseMessage initSqxx() {
        ResponseMessage responseMessage = new ResponseMessage();
        Map<String, Object> resultMap = Maps.newHashMap();
        DchyXmglSqxx dchyXmglSqxx = new DchyXmglSqxx();
        dchyXmglSqxx.setSqxxid(UUIDGenerator.generate18());
        dchyXmglSqxx.setSqr(UserUtil.getCurrentUserId());
        dchyXmglSqxx.setSqrmc(UserUtil.getCurrentUserId());
        dchyXmglSqxx.setSqsj(CalendarUtil.getCurHMSDate());
        dchyXmglSqxx.setSqzt(Constants.DCHY_XMGL_SQZT_DSH);
        dchyXmglSqxx.setSqbh(UUIDGenerator.generate18());

        int flag = entityMapperXSBF.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
        if (flag > 0) {
            resultMap.put("sqxxid", dchyXmglSqxx.getSqxxid());
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.setData(resultMap);
        } else {
            responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.INIT_FAIL.getMsg(), ResponseMessage.CODE.INIT_FAIL.getCode());
        }
        return responseMessage;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public ResponseMessage initCgtj(String sqxxid) {
        ResponseMessage message = new ResponseMessage();
        Map resultMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(sqxxid)) {
            List<DchyXmglClcgTjjl> dchyXmglClcgTjjls = dchyXmglCltjXSBFMapper.queryClcgTjjl(sqxxid);
            if (CollectionUtils.isNotEmpty(dchyXmglClcgTjjls)) {
                DchyXmglClcgTjjl dchyXmglClcgTjjl = dchyXmglClcgTjjls.get(0);
                if (StringUtils.equals(dchyXmglClcgTjjl.getSftj(), Constants.DCHY_XMGL_CLCG_SFTJ_FALSE)) {
                    resultMap = JSONObject.parseObject(new String(dchyXmglClcgTjjl.getCwxx()), Map.class);
                }
            }
            message = ResponseUtil.wrapSuccessResponse();
            message.setData(resultMap);

        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    @Transactional
    @Override
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public ResponseMessage checkZipFiles(String uploadFileName, MultipartFile file, String sqxxid) {
        ResponseMessage message = new ResponseMessage();
        //测量成果提交,入库,推送
        Map<String, Object> resultMap = uploadClcgFile(file, sqxxid, uploadFileName);

        if (StringUtils.equals(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "code")), ResponseMessage.CODE.SUCCESS.getCode())) {
            //删除文件中心的文件
            Example exampleSjxx = new Example(DchyXmglSjxx.class);
            exampleSjxx.createCriteria().andEqualTo("glsxid", sqxxid);
            List<DchyXmglSjxx> dchyXmglSjxxList = entityMapperXSBF.selectByExample(exampleSjxx);
            if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                DchyXmglSjxx dchyXmglSjxx = dchyXmglSjxxList.get(0);
                String sjxxid = dchyXmglSjxx.getSjxxid();
                Example exampleSjcl = new Example(DchyXmglSjcl.class);
                exampleSjcl.createCriteria().andEqualTo("sjxxid", sjxxid);
                List<DchyXmglSjcl> dchyXmglSjclList = entityMapperXSBF.selectByExample(exampleSjcl);
                if (CollectionUtils.isNotEmpty(dchyXmglSjclList)) {
                    String sjclid = dchyXmglSjclList.get(0).getSjclid();
                    String wjzxid = dchyXmglSjclList.get(0).getWjzxid();
                    deleteFile(sjclid, wjzxid);
                }
            }

            //获取成果提交的返回信息
            Map<String, Object> mycache = null;
            try {
                mycache = queryXxData(sqxxid + "cgjc");
                logger.info("************线下返回值****************" + JSON.toJSONString(mycache));
                if (StringUtils.equals(MapUtils.getString(mycache, "code"), ResponseMessage.CODE.SUCCESS.getCode())) {
                    message.setData(MapUtils.getMap(mycache, "data"));
                    message.getHead().setCode(MapUtils.getString(mycache, "code"));
                    message.getHead().setMsg(MapUtils.getString(mycache, "msg"));
                } else {
                    message.getHead().setCode(MapUtils.getString(mycache, "code"));
                    message.getHead().setMsg(MapUtils.getString(mycache, "msg"));
                }
            } catch (InterruptedException e) {
                logger.error("错误原因:{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }


        } else {
            message.getHead().setCode(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "code")));
            message.getHead().setMsg(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "msg")));
        }
        return message;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public ResponseMessage zipUpload(String glsxid, List<Map<String, String>> errorInfoModels) {
        ResponseMessage message = new ResponseMessage();
        String dwmc = "";
        //线上提交,提交人为当前用户的单位名称
        String yhid = UserUtil.getCurrentUserId();
        Example exampleYhdw = new Example(DchyXmglYhdw.class);
        exampleYhdw.createCriteria().andEqualTo("yhid", yhid);
        List<DchyXmglYhdw> dchyXmglYhdws = entityMapper.selectByExample(exampleYhdw);
        if (CollectionUtils.isNotEmpty(dchyXmglYhdws)) {
            dwmc = dchyXmglYhdws.get(0).getDwmc();
        }

        DchyXmglCgtjDto dchyXmglCgtjDto = new DchyXmglCgtjDto();
        dchyXmglCgtjDto.setDwmc(dwmc);
        dchyXmglCgtjDto.setErrorInfoModels(errorInfoModels);
        dchyXmglCgtjDto.setGlsxid(glsxid);

        try {
            //推送处理完的线上错误信息到线下
            pushDataToMqService.pushCgtjMsgToMq(dchyXmglCgtjDto);
            //获取成果提交的返回信息
            Map<String, Object> mycache = queryXxData(glsxid + "cgtj");

            if (StringUtils.equals(MapUtils.getString(mycache, "code"), ResponseMessage.CODE.SUCCESS.getCode())) {
                changeClcgztBySlbh(glsxid);
                message.setData(MapUtils.getMap(mycache, "data"));
                message.getHead().setCode(MapUtils.getString(mycache, "code"));
                message.getHead().setMsg(MapUtils.getString(mycache, "msg"));
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.RESULT_SUBMIT_FAIL.getMsg(), ResponseMessage.CODE.RESULT_SUBMIT_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    //获取线上错误列表的返回值,当请求失败时,休眠3s再次请求,最多请求10次
    public Map<String, Object> queryXxData(String key) throws InterruptedException {
        Map<String, Object> resultMap = Maps.newHashMap();
        Cache.ValueWrapper valueWrapper = EhcacheUtil.getDataFromEhcache(key);
        for (int i = 0; i < 20; i++) {
            if (null != valueWrapper) {
                Map mycache = (Map) valueWrapper.get();
                EhcacheUtil.putDataToEhcache(key, null);
                resultMap.put("data", mycache);
                if (StringUtils.isNotBlank(MapUtils.getString(mycache,"code"))){
                    resultMap.put("code", MapUtils.getString(mycache,"code"));
                    resultMap.put("msg", MapUtils.getString(mycache,"msg"));
                }else {
                    resultMap.put("code", ResponseMessage.CODE.SUCCESS.getCode());
                    resultMap.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
                }

                break;
            } else {
                resultMap.put("code", ResponseMessage.CODE.RESULT_SUBMIT_REQUEST_TIMED_OUT.getCode());
                resultMap.put("msg", ResponseMessage.CODE.RESULT_SUBMIT_REQUEST_TIMED_OUT.getMsg());
                Thread.sleep(3000);
                valueWrapper = EhcacheUtil.getDataFromEhcache(key);
            }

        }
        return resultMap;
    }

    /**
     * @param file   成果包文件
     * @param glsxid sqxxid申请信息id
     * @param mlmc   mlmc 目录名称
     * @return
     * @description 2021/3/13 成果包上传文件
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_UPLOAD_MC, czlxCode = ProLog.CZLX_UPLOAD_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public Map<String, Object> uploadClcgFile(MultipartFile file, String glsxid, String mlmc) {
        Map<String, Object> resultMap = Maps.newHashMap();

        try {
            //1,文件上传至文件中心
            FileService fileService = getFileService();
            Integer parentId = platformUtil.creatNode(glsxid);
            Integer folderId = platformUtil.createFileFolderByclmc(parentId, mlmc);
            List<Node> nodeList = platformUtil.getChildNodeListByParentId(folderId);
            if (CollectionUtils.isNotEmpty(nodeList)) {
                for (Node node : nodeList) {
                    platformUtil.deleteNodeById(node.getId());
                }
            }

            String fileName = file.getOriginalFilename();
            if (StringUtils.isNotBlank(fileName)) {
                fileService.uploadFile(file.getInputStream(), folderId, fileName);
            }
            //2,保存数据到数据库
            DchyXmglSjxx dchyXmglSjxx = new DchyXmglSjxx();
            DchyXmglSjcl dchyXmglSjcl = new DchyXmglSjcl();
            dchyXmglSjxx.setSjsj(CalendarUtil.getCurHMSDate());
            dchyXmglSjxx.setSjxxid(UUIDGenerator.generate18());
            dchyXmglSjxx.setTjr(UserUtil.getCurrentUserId());
            dchyXmglSjxx.setGlsxid(glsxid);
            dchyXmglSjxx.setSsmkid(SsmkidEnum.CGCCPJ.getCode());

            dchyXmglSjcl.setSjclid(UUIDGenerator.generate18());
            dchyXmglSjcl.setSjxxid(dchyXmglSjxx.getSjxxid());
            dchyXmglSjcl.setClrq(CalendarUtil.getCurHMSDate());
            dchyXmglSjcl.setWjzxid(folderId + "");
            dchyXmglSjcl.setClmc(mlmc);
            dchyXmglSjcl.setCllx("1");
            dchyXmglSjcl.setYs(Constants.DCHY_XMGL_MRYS);
            dchyXmglSjcl.setFs(Constants.DCHY_XMGL_MRFS);

            entityMapperXSBF.saveOrUpdate(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
            entityMapperXSBF.saveOrUpdate(dchyXmglSjxx, dchyXmglSjxx.getSjxxid());

            //3,推送文件到线下
            Map map = Maps.newHashMap();
            DchyXmglCgJcDto dchyXmglCgJcDto = new DchyXmglCgJcDto();
            byte[] bytes = file.getBytes();
            String clcgFiles = Base64.getEncoder().encodeToString(bytes);
            map.put("clcgFiles", AESOperatorUtil.encrypt(clcgFiles));
            map.put("uploadFileName", mlmc);
            map.put("glsxid", glsxid);

            dchyXmglCgJcDto.setClcgFiles(AESOperatorUtil.encrypt(clcgFiles));
            dchyXmglCgJcDto.setGlsxid(glsxid);
            dchyXmglCgJcDto.setUploadFileName(mlmc);

            //为了后面调用统一的方法
            Map data = Maps.newHashMap();
            data.put("data", map);
            EhcacheUtil.putDataToEhcache(glsxid + "slbh", map);
            pushDataToMqService.pushCgjcMsgToMq(dchyXmglCgJcDto);

            resultMap.put("code", ResponseMessage.CODE.SUCCESS.getCode());
            resultMap.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());

        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            resultMap.put("code", ResponseMessage.CODE.EXCEPTION_MGS.getCode());
            resultMap.put("msg", ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
        }
        return resultMap;
    }

    /**
     * 文件删除
     *
     * @param sjclId
     * @param wjzxId
     * @return
     */
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public ResponseMessage deleteFile(String sjclId, String wjzxId) {
        ResponseMessage message = new ResponseMessage();
        try {
            platformUtil.deleteNodeById(Integer.parseInt(wjzxId));
            /*根据sjclid删除对应材料记录*/
            int result = fileUploadService.deleteSjclById(sjclId);
            if (result > 0) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("wjzxid", wjzxId);
            }
        } catch (Exception e) {
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public ResponseMessage changeClcgztBySlbh(String glsxid) {
        ResponseMessage message = new ResponseMessage();
        try {
            if (StringUtils.isNotBlank(glsxid)) {
                Map<String, Object> mycache = queryXxData(glsxid + "slbh");
                if (StringUtils.equals(MapUtils.getString(mycache, "code"), ResponseMessage.CODE.SUCCESS.getCode())) {
                    Map map = MapUtils.getMap(mycache, "data");
                    String mlmc = CommonUtil.formatEmptyValue(MapUtils.getString(map, "uploadFileName"));
                    String slbh = mlmc.substring(0, mlmc.indexOf("."));

                    Example exampleChxm = new Example(DchyXmglChxm.class);
                    exampleChxm.createCriteria().andEqualTo("slbh", slbh);
                    List<DchyXmglChxm> dchyXmglChxmList = entityMapperXSBF.selectByExample(exampleChxm);
                    if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                        String chxmid = dchyXmglChxmList.get(0).getChxmid();
                        Example exampleClsx = new Example(DchyXmglChxmClsx.class);
                        exampleClsx.createCriteria().andEqualTo("chxmid", chxmid);
                        List<DchyXmglChxmClsx> dchyXmglChxmClsxList = entityMapperXSBF.selectByExample(exampleClsx);
                        if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                            for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                                dchyXmglChxmClsx.setCgtjzt(Constants.DCHY_XMGL_XMCGZT_YRK);
                                entityMapperXSBF.saveOrUpdate(dchyXmglChxmClsx, dchyXmglChxmClsx.getClsxid());
                            }

                        }
                    }
                } else {
                    message.getHead().setCode(MapUtils.getString(mycache, "code"));
                    message.getHead().setMsg(MapUtils.getString(mycache, "msg"));
                }
            }
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    @Override
    public boolean isMajorProject(String slbh) {
        boolean flag = false;
        if (StringUtils.isNotBlank(slbh)) {
            Example exampleChxm = new Example(DchyXmglChxm.class);
            exampleChxm.createCriteria().andEqualTo("slbh", slbh);
            List<DchyXmglChxm> dchyXmglChxmList = entityMapperXSBF.selectByExample(exampleChxm);
            if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                DchyXmglChxm dchyXmglChxm = dchyXmglChxmList.get(0);
                if (StringUtils.equals(dchyXmglChxm.getZdxm(), Constants.DCHY_XMGL_CHXM_ZDXM)) {
                    flag = true;
                }
            }
        }
        return flag;
    }
}
