package cn.gtmap.msurveyplat.serviceol.service.impl;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglJcsjsqDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.*;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.Impl.JcsjsqServiceImpl;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.serviceol.core.xsbfmapper.DchyXmglMlkXSBFMapper;
import cn.gtmap.msurveyplat.serviceol.core.xsbfmapper.JcsjsqMapper;
import cn.gtmap.msurveyplat.serviceol.core.xsbfmapper.XsbfDchyXmglCgccMapper;
import cn.gtmap.msurveyplat.serviceol.core.xsbfmapper.XsbfDchyXmglChgcMapper;
import cn.gtmap.msurveyplat.serviceol.model.PfUser;
import cn.gtmap.msurveyplat.serviceol.service.DchyXmglJcsjsqService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import cn.gtmap.msurveyplat.serviceol.web.util.FileDownoadUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import static java.lang.Integer.parseInt;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/3/8
 * @description ??????????????????
 */
@Service
public class DchyXmglJcsjsqServiceImpl implements DchyXmglJcsjsqService {

    protected final Log logger = LogFactory.getLog(DchyXmglJcsjsqServiceImpl.class);

    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapperXSBF;

    @Autowired
    private EntityMapper entityMapper;

    @Resource(name = "repositoryXSBF")
    private Repository repository;

    @Autowired
    private JcsjsqMapper jcsjsqMapper;

    @Autowired
    private JcsjsqServiceImpl jcsjsqService;
    @Autowired
    private PushDataToMqService pushDataToMqService;

    @Autowired
    private XsbfDchyXmglChgcMapper xsbfDchyXmglChgcMapper;

    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    @Autowired
    private DchyXmglMlkXSBFMapper dchyXmglMlkMapper;

    @Autowired
    private XsbfDchyXmglCgccMapper xsbfDchyXmglCgccMapper;

    @Override
    public ResponseMessage queryBasicDataApplicationInfo(Map<String, Object> map) {
        List<String> mlkidList = Lists.newArrayList();
        Example example = new Example(DchyXmglYhdw.class);
        example.createCriteria().andEqualTo("yhid", UserUtil.getCurrentUserId());
        List<DchyXmglYhdw> dchyXmglYhdwList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(dchyXmglYhdwList)) {
            DchyXmglYhdw dchyXmglYhdw = dchyXmglYhdwList.get(0);
            String dwbh = dchyXmglYhdw.getDwbh();
            Example exampleMlk = new Example(DchyXmglMlk.class);
            exampleMlk.createCriteria().andEqualTo("dwbh", dwbh);
            List<DchyXmglMlk> dchyXmglMlkList = entityMapper.selectByExample(exampleMlk);
            if (CollectionUtils.isNotEmpty(dchyXmglMlkList)) {
                for (DchyXmglMlk dchyXmglMlk : dchyXmglMlkList) {
                    mlkidList.add(dchyXmglMlk.getMlkid());
                }
            } else {
                mlkidList.add(Constants.EMPTYPARAM_VALUE);
            }
            map.put("mlkidList", mlkidList);
        }
        map.put("jsdw", CommonUtil.formatEmptyValue(MapUtils.getString(map, "jsdw")));
        map.put("gcbh", CommonUtil.formatEmptyValue(MapUtils.getString(map, "gcbh")));
        map.put("gcmc", CommonUtil.formatEmptyValue(MapUtils.getString(map, "gcmc")));
        map.put("babh", CommonUtil.formatEmptyValue(MapUtils.getString(map, "babh")));
        map.put("xmzt", CommonUtil.formatEmptyValue(MapUtils.getString(map, "xmzt")));
        map.put("bakssj",CommonUtil.formatEmptyValue(MapUtils.getString(map, "bakssj")));
        map.put("bajssj",CommonUtil.formatEmptyValue(MapUtils.getString(map, "bajssj")));
        String xmly = CommonUtil.formatEmptyValue(MapUtils.getString(map, "xmly"));
        if(StringUtils.isNotEmpty(xmly)){
            List<String> xmlyList= Lists.newArrayList();
            if(xmly.contains(",")){
                xmlyList = Lists.newArrayList(xmly.split(","));
            }else{
                xmlyList.add(xmly);
            }
            map.put("xmlylist",xmlyList);
        }

        int page = Integer.parseInt(map.get("page") != null ? map.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? map.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);
        Page<Map<String, Object>> registerFileByPage = repository.selectPaging("queryBasicDataApplicationInfoByPage", map, page - 1, pageSize);
        return ResponseUtil.wrapResponseBodyByPage(registerFileByPage);
    }

    @Override
    @Transactional
    @SystemLog(czmkMc = ProLog.CZMC_JCSJSQ_MC, czmkCode = ProLog.CZMC_JCSJSQ_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_JCSJSQ_CODE)
    public ResponseMessage initBasicDataApplication(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        String babh = CommonUtil.formatEmptyValue(MapUtils.getString(map, "babh"));
        String[] babhList = babh.split(";");
        if (null != babhList && babhList.length > 0) {
            List<String> babhs = Lists.newArrayList();
            for (String string : babhList) {
                babhs.add(string);
                int flagSqxx = generateSqxxBybabh(string);
                if (flagSqxx < 0) {
                    message = ResponseUtil.wrapExceptionResponse();
                    break;
                }
                message = ResponseUtil.wrapSuccessResponse();
            }

        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }

        return message;
    }

    @Override
    public ResponseMessage applicationInfoView(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        String chxmid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "chxmid"));
        if (StringUtils.isNotBlank(chxmid)) {
            paramMap.put("chxmid", chxmid);
            List<Map<String, Object>> baxxList = jcsjsqMapper.querBaxxByChxmid(paramMap);
            for (Map<String, Object> baxx : baxxList) {
                String gcdz = "";
                if (StringUtils.isNotBlank(MapUtils.getString(baxx, "XMZT"))) {
                    String xmzt = MapUtils.getString(baxx, "XMZT");
                    DchyXmglZd gcdzssZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_ZDLX_XMZT, xmzt);
                    baxx.put("XMZT", gcdzssZd.getMc());
                }

                if (StringUtils.isNotBlank(MapUtils.getString(baxx, "GCDZS"))) {
                    String gcdzs = MapUtils.getString(baxx, "GCDZS");
                    DchyXmglZd gcdzsZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_GCDZ, gcdzs);
                    gcdz += gcdzsZd.getMc();
                }

                if (StringUtils.isNotBlank(MapUtils.getString(baxx, "GCDZSS"))) {
                    String gcdzss = MapUtils.getString(baxx, "GCDZSS");
                    DchyXmglZd gcdzssZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_GCDZ, gcdzss);
                    gcdz += gcdzssZd.getMc();
                }

                if (StringUtils.isNotBlank(MapUtils.getString(baxx, "GCDZQX"))) {
                    String gcdzqx = MapUtils.getString(baxx, "GCDZQX");
                    DchyXmglZd gcdzqxZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_GCDZ, gcdzqx);
                    gcdz += gcdzqxZd.getMc();
                }
                baxx.put("GCDZ", gcdz);
            }
            resultMap.put("baxxList", baxxList);
            DchyXmglChxm dchyXmglChxm = entityMapperXSBF.selectByPrimaryKey(DchyXmglChxm.class, chxmid);

            paramMap.put("gcbh", dchyXmglChxm.getChgcbh());
            List<Map<String, Object>> baClsxList = jcsjsqMapper.queryBaClsxListByGcbh(paramMap);
            resultMap.put("baClsxList", baClsxList);

            String glsxid = dchyXmglChxm.getSlbh();

            Example exampleSjxx = new Example(DchyXmglSjxx.class);
            exampleSjxx.createCriteria().andEqualTo("glsxid", glsxid);
            List<DchyXmglSjxx> dchyXmglSjxxList = entityMapperXSBF.selectByExample(exampleSjxx);
            DchyXmglSjcl dchyXmglSjcl = null;
            if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                DchyXmglSjxx dchyXmglSjxx = dchyXmglSjxxList.get(0);
                Example exampleSjcl = new Example(DchyXmglSjcl.class);
                exampleSjcl.createCriteria().andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                List<DchyXmglSjcl> dchyXmglSjclList = entityMapperXSBF.selectByExample(exampleSjcl);
                if (CollectionUtils.isNotEmpty(dchyXmglSjclList)) {
                    dchyXmglSjcl = dchyXmglSjclList.get(0);
                }
            }
            resultMap.put("jcsjfw", dchyXmglSjcl);

            message = ResponseUtil.wrapSuccessResponse();
            message.setData(resultMap);
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_NULL.getMsg(), ResponseMessage.CODE.QUERY_NULL.getCode());
        }
        return message;
    }

    @Override
    public ResponseMessage myProjectInfoView(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> jsdwxx = Maps.newHashMap();
        Map<String, Object> chdwxx = Maps.newHashMap();
        Map<String, Object> wtxmxx = Maps.newHashMap();
        Map<String, Object> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        String chxmid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "chxmid"));
        String chdwxxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "chdwxxid"));
        if (StringUtils.isNoneBlank(chxmid, chdwxxid)) {
            paramMap.put("chxmid", chxmid);
            paramMap.put("chdwxxid", chdwxxid);

            //??????????????????
            List<Map<String, Object>> wtxmxxList = xsbfDchyXmglChgcMapper.queryWtxxByChdwxxids(paramMap);
            //????????????
            if (CollectionUtils.isNotEmpty(wtxmxxList)) {
                wtxmxx = wtxmxxList.get(0);
                String gcdzs = CommonUtil.formatEmptyValue(MapUtils.getString(wtxmxx, "GCDZS"));
                DchyXmglZd gcdzsZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("GCDZ", gcdzs);
                String gcdzss = CommonUtil.formatEmptyValue(MapUtils.getString(wtxmxx, "GCDZSS"));
                DchyXmglZd gcdzssZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("GCDZ", gcdzss);
                String gcdzqx = CommonUtil.formatEmptyValue(MapUtils.getString(wtxmxx, "GCDZQX"));
                DchyXmglZd gcdzqxZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("GCDZ", gcdzqx);

                String gcdz = gcdzsZd.getMc() + gcdzssZd.getMc() + gcdzqxZd.getMc();
                wtxmxx.put("GCDZ", gcdz);
            }

            //?????????????????? ??????????????????
            if (null != wtxmxx) {
                String jsdwmc = CommonUtil.formatEmptyValue(MapUtils.getString(wtxmxx, "WTDW"));
                String chdwmc = CommonUtil.formatEmptyValue(MapUtils.getString(wtxmxx, "CHDWMC"));
                String jsdwLxr = CommonUtil.formatEmptyValue(MapUtils.getString(wtxmxx, "LXR"));
                String jsdwLxdh = CommonUtil.formatEmptyValue(MapUtils.getString(wtxmxx, "LXDH"));
                Example exampleJsdw = new Example(DchyXmglYhdw.class);
                Example exampleChdw = new Example(DchyXmglYhdw.class);
                exampleJsdw.createCriteria().andEqualTo("dwmc", jsdwmc);
                exampleChdw.createCriteria().andEqualTo("dwmc", chdwmc);
                List<DchyXmglYhdw> jsdwList = entityMapper.selectByExample(exampleJsdw);
                List<DchyXmglYhdw> chdwList = entityMapper.selectByExample(exampleChdw);
                if (CollectionUtils.isNotEmpty(jsdwList)) {
                    DchyXmglYhdw dchyXmglYhdw = jsdwList.get(0);
                    //???????????????????????????
                    DataSecurityUtil.decryptSingleObject(dchyXmglYhdw);
                    jsdwxx.put("DWMC", dchyXmglYhdw.getDwmc());
                    jsdwxx.put("LXR", SM4Util.decryptData_ECB(jsdwLxr));
                    jsdwxx.put("TYSHXYDM", dchyXmglYhdw.getTyshxydm());
                    jsdwxx.put("LXDH", SM4Util.decryptData_ECB(jsdwLxdh));
                }

                if (CollectionUtils.isNotEmpty(chdwList)) {
                    DchyXmglYhdw dchyXmglYhdw = chdwList.get(0);
                    //???????????????????????????
                    DataSecurityUtil.decryptSingleObject(dchyXmglYhdw);
                    String yhid = dchyXmglYhdw.getYhid();
                    PfUser pfUser = entityMapper.selectByPrimaryKey(PfUser.class, yhid);
                    chdwxx.put("DWMC", dchyXmglYhdw.getDwmc());
                    chdwxx.put("LXR", pfUser.getUserName());
                    chdwxx.put("TYSHXYDM", dchyXmglYhdw.getTyshxydm());
                    chdwxx.put("LXDH", pfUser.getMobilePhone());
                }
            }
            List<Map<String, Object>> clsxList = jcsjsqMapper.queryClsxByChxmid(paramMap);

            resultMap.put("jsdwxx", jsdwxx);
            resultMap.put("chdwxx", chdwxx);
            resultMap.put("wtxmxx", wtxmxx);
            resultMap.put("clsxList", clsxList);

            message = ResponseUtil.wrapSuccessResponse();
            message.setData(resultMap);
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_NULL.getMsg(), ResponseMessage.CODE.QUERY_NULL.getCode());
        }
        return message;
    }

    @Override
    public ResponseEntity<byte[]> downLoadFileByWjzxid(String wjzxid, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        // ????????????
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

        // ??????????????????
        bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);

        int nodeId = Integer.parseInt(wjzxid);
        String fileName = "????????????.zip";
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
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }

    @Override
    public ResponseMessage querySqxxList(Map<String, Object> map) {
        Example example = new Example(DchyXmglYhdw.class);
        example.createCriteria().andEqualTo("yhid", UserUtil.getCurrentUserId());
        List<DchyXmglYhdw> dchyXmglYhdwList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(dchyXmglYhdwList)) {
            DchyXmglYhdw dchyXmglYhdw = dchyXmglYhdwList.get(0);
            String dwmc = dchyXmglYhdw.getDwmc();
            map.put("dwmc", dwmc);
        } else {
            map.put("dwmc", Constants.EMPTYPARAM_VALUE);
        }

        int page = Integer.parseInt(map.get("page") != null ? map.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? map.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);
        logger.info("******************page:" + page);
        logger.info("******************pageSize:" + pageSize);
        Page<Map<String, Object>> querySqxxListByPage = repository.selectPaging("querySqxxListByPage", map, page - 1, pageSize);

        return ResponseUtil.wrapResponseBodyByPage(querySqxxListByPage);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer generateSqxxBybabh(String babh) {
        int flag = 0;
        DchyXmglJcsjSqxx dchyXmglJcsjSqxx = new DchyXmglJcsjSqxx();
        if (StringUtils.isNotBlank(babh)) {
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("babh", babh);
            List<Map<String, Object>> baxxList = jcsjsqMapper.queryBaxxByBabh(paramMap);
            if (CollectionUtils.isNotEmpty(baxxList)) {
                Map<String, Object> resultMap = baxxList.get(0);
                String chxmid = CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "CHXMID"));
                dchyXmglJcsjSqxx.setJcsjsqxxid(UUIDGenerator.generate18());
                dchyXmglJcsjSqxx.setBabh(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "SLBH")));
                dchyXmglJcsjSqxx.setXmbabh(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "BABH")));
                dchyXmglJcsjSqxx.setChxmid(chxmid);
                dchyXmglJcsjSqxx.setDqzt(Constants.DCHY_XMGL_JCSJSQ_DQSQZT_SHZ);
                dchyXmglJcsjSqxx.setGcbh(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "GCBH")));
                dchyXmglJcsjSqxx.setGcmc(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "GCMC")));
                dchyXmglJcsjSqxx.setJsdw(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "WTDW")));
                dchyXmglJcsjSqxx.setChdwmc(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "CHDWMC")));
                dchyXmglJcsjSqxx.setSqfs(Constants.DCHY_XMGL_SQFS_XS);
                dchyXmglJcsjSqxx.setSqsj(CalendarUtil.getCurHMSDate());

                flag = entityMapperXSBF.saveOrUpdate(dchyXmglJcsjSqxx, dchyXmglJcsjSqxx.getJcsjsqxxid());
                if (flag > 0) {
                    //???????????????????????????????????????
                    DchyXmglJcsjsqDto dchyXmglJcsjsqDto = jcsjsqService.getSingleData(chxmid);
                    dchyXmglJcsjsqDto.setDchyXmglJcsjSqxx(dchyXmglJcsjSqxx);
                    pushDataToMqService.pushJcsjMsgToMq(dchyXmglJcsjsqDto);
                }
            }
        }
        return flag;
    }

    @Override
    public List<Map<String, Object>> queryBaClsxListByGcbh(String gcbh) {
        Map<String, Object> paramMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(gcbh)) {
            paramMap.put("gcbh", gcbh);
        }
        return jcsjsqMapper.queryBaClsxListByGcbh(paramMap);
    }

    @Override
    public List<Map<String, Object>> getHtxxSjclXx(String glsxid) {
        List<Map<String, Object>> htxxList = jcsjsqMapper.getHtxxSjclXx(glsxid);
        for (Map<String, Object> htxxLists : htxxList) {
            if (org.apache.commons.lang.StringUtils.isNotEmpty(MapUtils.getString(htxxLists, "HTXXID"))) {
                String htxxid = MapUtils.getString(htxxLists, "HTXXID");
                List<Map<String, Object>> clsxList = jcsjsqMapper.getClsxByChxmid(htxxid);
                List<Map<String, Object>> chdwxxList = jcsjsqMapper.getChdwxxByChxmid(htxxid);

                if (CollectionUtils.isNotEmpty(clsxList)) {
                    htxxLists.put("CLSX", clsxList);
                }
                if (CollectionUtils.isNotEmpty(chdwxxList)) {
                    htxxLists.put("CHDWXX", chdwxxList);
                }
            }
        }
        return htxxList;
    }

    @Override
    public List<Map<String, Object>> queryUploadFileBySsmkId(String ssmkid) {
        return dchyXmglMlkMapper.queryUploadFileBySsmkId(ssmkid);
    }

    /**
     * ????????????????????????????????????
     *
     * @param glsxid
     * @param ssmkId
     * @param mapList
     */
    @Override
    @Transactional
    public void initHtxxSjxxAndClxx(String glsxid, String ssmkId, List<Map<String, Object>> mapList) {
        if (CollectionUtils.isNotEmpty(mapList)) {
            /*????????????*/
            for (Map<String, Object> map : mapList) {
                String htxxid = UUIDGenerator.generate18();
                DchyXmglHtxx htxx = new DchyXmglHtxx();
                htxx.setHtxxid(htxxid);
                htxx.setChxmid(glsxid);
                entityMapperXSBF.saveOrUpdate(htxx, htxx.getHtxxid());

                String sjxxId = UUIDGenerator.generate18();
                Date sjsj = new Date();
                String sjr = UserUtil.getCurrentUserId();
                String tjr = "";
                DchyXmglSjxx sjxx = new DchyXmglSjxx();
                sjxx.setSjxxid(sjxxId);
                sjxx.setGlsxid(htxxid);
                sjxx.setSsmkid(ssmkId);
                sjxx.setSjsj(sjsj);
                sjxx.setTjr(tjr);
                sjxx.setSjr(sjr);
                entityMapperXSBF.saveOrUpdate(sjxx, sjxx.getSjxxid());

                String sjclid = UUIDGenerator.generate18();
                map.put("SJCLID", sjclid);
                map.put("SJXXID", sjxxId);
                map.put("HTXXID", htxxid);
                int fs = map.containsKey("FS") ? (((BigDecimal) map.get("FS")).intValue()) : 1;
                String mlmc = (String) map.get("CLMC");
                DchyXmglSjcl sjcl = new DchyXmglSjcl();
                sjcl.setSjclid(sjclid);
                sjcl.setSjxxid(sjxxId);
                sjcl.setClmc(mlmc);
                sjcl.setCllx(CommonUtil.formatEmptyValue(map.get("CLLX")));
                sjcl.setFs(fs);
                sjcl.setClrq(new Date());
                sjcl.setYs(1);
                entityMapperXSBF.saveOrUpdate(sjcl, sjcl.getSjclid());
            }
        }
    }

    @Override
    public List<Map<String, Object>> getSjclXx(String glsxid) {
        return dchyXmglMlkMapper.getSjclXx(glsxid);
    }

    /**
     * ????????????????????????????????????????????????????????????
     *
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void initSjxxAndClxx(String glsxId, String ssmkId, List<Map<String, Object>> mapList) {
        if (CollectionUtils.isNotEmpty(mapList)) {
            /*????????????*/
            String sjxxId = UUIDGenerator.generate18();
            Date sjsj = new Date();
            String sjr = UserUtil.getCurrentUserId();
            String tjr = "";
            DchyXmglSjxx sjxx = new DchyXmglSjxx();
            sjxx.setSjxxid(sjxxId);
            sjxx.setGlsxid(glsxId);
            sjxx.setSsmkid(ssmkId);
            sjxx.setSjsj(sjsj);
            sjxx.setTjr(tjr);
            sjxx.setSjr(sjr);
            entityMapperXSBF.saveOrUpdate(sjxx, sjxx.getSjxxid());

            for (Map<String, Object> map : mapList) {
                String sjclid = UUIDGenerator.generate18();
                map.put("SJCLID", sjclid);
                map.put("SJXXID", sjxxId);
                int fs = map.containsKey("FS") ? (((BigDecimal) map.get("FS")).intValue()) : 1;
                String mlmc = (String) map.get("CLMC");
                DchyXmglSjcl sjcl = new DchyXmglSjcl();
                sjcl.setSjclid(sjclid);
                sjcl.setSjxxid(sjxxId);
                sjcl.setClmc(mlmc);
                sjcl.setCllx(CommonUtil.formatEmptyValue(map.get("CLLX")));
                sjcl.setFs(fs);
                sjcl.setClrq(new Date());
                sjcl.setYs(1);
                entityMapperXSBF.saveOrUpdate(sjcl, sjcl.getSjclid());
            }
        }
    }

    @Override
    public List<Map<String, Object>> getZdClsx(Map<String, Object> dataMap) {
        Map<String, Object> data = (Map<String, Object>) dataMap.get("data");
        String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("chxmid", chxmid);
        List<Map<String, Object>> dchyXmglZdList = jcsjsqMapper.getZdClsx(paramMap);
        for (Map<String, Object> dchyXmglZdLists : dchyXmglZdList) {
            String yjjf = MapUtils.getString(dchyXmglZdLists, "YJJFRQ");
            if (StringUtils.isNotBlank(yjjf)) {
                String jcrq = MapUtils.getString(dchyXmglZdLists, "JCRQ");
                Calendar cl = Calendar.getInstance();
                cl.setTime(CalendarUtil.formatDate(jcrq));
                cl.add(Calendar.DATE, parseInt(yjjf));
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String temp = df.format(cl.getTime());
                dchyXmglZdLists.put("JSRQ", temp);
            }

        }
        return dchyXmglZdList;
    }

    @Override
    public ResponseMessage evaluationCheckResults(Map<String, Object> map) {
        int page = Integer.parseInt(map.get("page") != null ? map.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? map.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);
        logger.info("******************page:" + page);
        logger.info("******************pageSize:" + pageSize);

        String chdwmc = Constants.EMPTYPARAM_VALUE;
        String mlkid = this.getMlkByUserId();
        if (StringUtils.isNotBlank(mlkid)) {
            DchyXmglMlk xmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
            if (null != xmglMlk) {
                chdwmc = xmglMlk.getDwmc();
            }
        }
        map.put("chdwmc", chdwmc);
        logger.info("*********?????????????????????????????????????????????**********" + JSON.toJSONString(map));
        Page<Map<String, Object>> evaluationCheckResultsByPage = repository.selectPaging("evaluationCheckResultsByPage", map, page - 1, pageSize);

        List<Map<String, Object>> rows = evaluationCheckResultsByPage.getContent();
        logger.info("******************rows:" + rows);
        if (CollectionUtils.isNotEmpty(rows)) {
            for (Map<String, Object> dataMap : rows) {
                String glsxid = CommonUtil.formatEmptyValue(MapUtils.getString(dataMap, "CGCCID"));
                List<String> wjzxids = Lists.newArrayList();
                if (StringUtils.isNotBlank(glsxid)) {
                    Example exampleSjxx = new Example(DchyXmglSjxx.class);
                    exampleSjxx.createCriteria().andEqualTo("glsxid", glsxid);
                    List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExample(exampleSjxx);
                    if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                        for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                            Example exampleSjcl = new Example(DchyXmglSjcl.class);
                            exampleSjcl.createCriteria().andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                            List<DchyXmglSjcl> dchyXmglSjclList = entityMapper.selectByExample(exampleSjcl);
                            if (CollectionUtils.isNotEmpty(dchyXmglSjclList)) {
                                for (DchyXmglSjcl dchyXmglSjcl : dchyXmglSjclList) {
                                    if (StringUtils.isNotBlank(dchyXmglSjcl.getWjzxid())) {
                                        wjzxids.add(dchyXmglSjcl.getWjzxid());
                                    }
                                }
                            }
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(wjzxids)) {
                    dataMap.put("SFSC", "1");
                } else {
                    dataMap.put("SFSC", "0");
                }
            }
        }

        return ResponseUtil.wrapResponseBodyByPage(evaluationCheckResultsByPage);
    }

    @Override
    public ResponseMessage evaluationCheckResultsView(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> resultMap = Maps.newHashMap();
        String cgccid = CommonUtil.formatFileName(MapUtils.getString(map, "cgccid"));
        if (StringUtils.isNotBlank(cgccid)) {
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("cgccid", cgccid);
            List<Map<String, Object>> resulrList = xsbfDchyXmglCgccMapper.queryCgccByCgccid(paramMap);
            resultMap.put("data", resulrList);
            message = ResponseUtil.wrapSuccessResponse();
            message.setData(resultMap);
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * ??????mlkid
     *
     * @param
     * @return
     */
    private String getMlkByUserId() {
        String userId = UserUtil.getCurrentUserId();
        if (StringUtils.isNotBlank(userId)) {
            Example yhdwExample = new Example(DchyXmglYhdw.class);
            yhdwExample.createCriteria().andEqualTo("yhid", userId);
            List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(yhdwExample);
            if (CollectionUtils.isNotEmpty(yhdwList)) {
                DchyXmglYhdw yhdw = yhdwList.get(0);
                if (null != yhdw) {
                    String dwbh = yhdw.getDwbh();
                    if (StringUtils.isNotBlank(dwbh)) {
                        /*??????????????????????????????mlkid*/
                        Example mlkExample = new Example(DchyXmglMlk.class);
                        mlkExample.createCriteria().andEqualTo("dwbh", dwbh);
                        List<DchyXmglMlk> mlkList = entityMapper.selectByExample(mlkExample);
                        if (CollectionUtils.isNotEmpty(mlkList)) {
                            DchyXmglMlk mlk = mlkList.get(0);
                            if (null != mlk) {
                                return mlk.getMlkid();
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    @Override
    public ResponseMessage getsjcl(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> resultMap = Maps.newHashMap();
        List<Map<String, Object>> sjclList = jcsjsqMapper.getSjcl(map);
        resultMap.put("dataList", sjclList);
        message = ResponseUtil.wrapSuccessResponse();
        message.setData(resultMap);
        return message;
    }

    @Override
    public ResponseMessage resultsDeliveryLogList(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> resultMap = Maps.newHashMap();
        List<Map<String, Object>> dchyXmglJcsjsqJfrzList = Lists.newArrayList();
        String jcsjsqxxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "jcsjsqxxid"));
        String glsxid = queryXmidByJcsjsqxxid(jcsjsqxxid);

        if (StringUtils.isNotBlank(glsxid)) {
            Example example = new Example(DchyXmglCzrz.class);
            example.createCriteria().andEqualTo("glsxid", glsxid);
            List<DchyXmglCzrz> dchyXmglCzrzList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(dchyXmglCzrzList)) {
                for (DchyXmglCzrz dchyXmglCzrz : dchyXmglCzrzList) {
                    String czcs = dchyXmglCzrz.getCzcs();
                    Map<String, Object> mapJson = (Map) JSON.parse(czcs);
                    dchyXmglJcsjsqJfrzList.add(mapJson);
                }
            }
        }
        resultMap.put("data", dchyXmglJcsjsqJfrzList);
        message = ResponseUtil.wrapSuccessResponse();
        message.setData(resultMap);
        return message;
    }

    @Override
    public ResponseMessage getProcessInfoBySlbh(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        String jcsjsqxxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "jcsjsqxxid"));
        Example exampleJdxx = new Example(DchyXmglJcsjJdxx.class);
        exampleJdxx.createCriteria().andEqualTo("jcsjsqxxid", jcsjsqxxid);
        List<DchyXmglJcsjJdxx> processInfoList = entityMapper.selectByExample(exampleJdxx);
        message = ResponseUtil.wrapSuccessResponse();
        message.getData().put("dataList", processInfoList);
        return message;
    }

    @Override
    public String queryXmidByJcsjsqxxid(String jcsjsqxxid) {
        String xmid = "";
        if (StringUtils.isNotBlank(jcsjsqxxid)) {
            Example example = new Example(DchyXmglSqxx.class);
            example.createCriteria().andEqualTo("glsxid", jcsjsqxxid);
            List<DchyXmglSqxx> dchyXmglSqxxList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(dchyXmglSqxxList)) {
                DchyXmglSqxx dchyXmglSqxx = new DchyXmglSqxx();
                xmid = dchyXmglSqxx.getSqxxid();
            }
        }
        return xmid;
    }
}
