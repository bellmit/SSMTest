package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.annotion.AuditLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChxmChdwxxMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChxmClsxMapper;
import cn.gtmap.msurveyplat.promanage.core.service.FileUploadHtxxService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.PlatformUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-09
 * description
 */
@Service("fileUploadHtxx")
public class FileUploadHtxxServiceImpl implements FileUploadHtxxService {
    protected final Log logger = LogFactory.getLog(getClass());
    private final String DELETED = "delete_";
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    Repository repository;
    @Autowired
    DchyXmglChxmClsxMapper dchyXmglChxmClsxMapper;
    @Autowired
    DchyXmglChxmChdwxxMapper dchyXmglChxmChdwxxMapper;
    @Autowired
    PlatformUtil platformUtil;
    @Autowired
    PushDataToMqService pushDataToMqService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @AuditLog(czlxCode = ProLog.CZLX_SAVE_CODE, czlxMc = ProLog.CZLX_SAVE_MC, clazz = DchyXmglHtxxDto.class, czmkCode = ProLog.CZMK_XZSLDJ_CODE, czmkMc = ProLog.CZMK_XZSLDJ_MC)
    public boolean saveHtxx(DchyXmglHtxxDto dchyXmglHtxxDto) {
        try {

            //????????????
            if (null != dchyXmglHtxxDto.getDchyXmglHtxx()) {
                String htxxid = dchyXmglHtxxDto.getDchyXmglHtxx().getHtxxid();
                entityMapper.saveOrUpdate(dchyXmglHtxxDto.getDchyXmglHtxx(), htxxid);
            }

            if (null != dchyXmglHtxxDto.getDchyXmglSjcl()) {
                String sjclid = dchyXmglHtxxDto.getDchyXmglSjcl().getSjclid();
                entityMapper.saveOrUpdate(dchyXmglHtxxDto.getDchyXmglSjcl(), sjclid);
            }

            //?????????????????????????????????
            List<DchyXmglClsxChdwxxGx> clsxChdwxxGx = dchyXmglHtxxDto.getDchyXmglClsxChdwxxGxList();
            if (CollectionUtils.isNotEmpty(clsxChdwxxGx)) {
                for (DchyXmglClsxChdwxxGx clsxChdwxxGxs : clsxChdwxxGx) {
                    String gxid = clsxChdwxxGxs.getGxid();
                    if (org.apache.commons.lang3.StringUtils.indexOf(gxid, DELETED) == 0) {
                        gxid = org.apache.commons.lang3.StringUtils.substring(gxid, DELETED.length());
                        clsxChdwxxGxs.setGxid(gxid);
                        entityMapper.deleteByPrimaryKey(DchyXmglClsxChdwxxGx.class, gxid);
                    } else {
                        entityMapper.saveOrUpdate(clsxChdwxxGxs, gxid);
                    }
                }
            }

            //?????????????????????????????????
            List<DchyXmglHtxxChdwxxGx> htxxChdwxxGx = dchyXmglHtxxDto.getDchyXmglHtxxChdwxxGxList();
            if (CollectionUtils.isNotEmpty(htxxChdwxxGx)) {
                for (DchyXmglHtxxChdwxxGx htxxChdwxxGxs : htxxChdwxxGx) {
                    String gxid = htxxChdwxxGxs.getGxid();
                    if (org.apache.commons.lang3.StringUtils.indexOf(gxid, DELETED) == 0) {
                        gxid = org.apache.commons.lang3.StringUtils.substring(gxid, DELETED.length());
                        htxxChdwxxGxs.setGxid(gxid);
                        entityMapper.deleteByPrimaryKey(DchyXmglHtxxChdwxxGx.class, gxid);
                    } else {
                        entityMapper.saveOrUpdate(htxxChdwxxGxs, gxid);
                    }
                }
            }

            //?????????????????????????????????
            List<DchyXmglClsxHtxxGx> clsxHtxxGx = dchyXmglHtxxDto.getDchyXmglClsxHtxxGxList();
            if (CollectionUtils.isNotEmpty(clsxHtxxGx)) {
                for (DchyXmglClsxHtxxGx clsxHtxxGxs : clsxHtxxGx) {
                    String gxid = clsxHtxxGxs.getGxid();
                    if (org.apache.commons.lang3.StringUtils.indexOf(gxid, DELETED) == 0) {
                        gxid = org.apache.commons.lang3.StringUtils.substring(gxid, DELETED.length());
                        clsxHtxxGxs.setGxid(gxid);
                        entityMapper.deleteByPrimaryKey(DchyXmglClsxHtxxGx.class, gxid);
                    } else {
                        entityMapper.saveOrUpdate(clsxHtxxGxs, gxid);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("????????????{}???", e);
            return false;
        }
        return true;
    }

    //??????????????????????????????
    @Override
    @Transactional
    public Map<String, Object> saveClsxByChxmid(Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String chxmid = CommonUtil.formatEmptyValue(map.get("chxmid"));
            String clsx = CommonUtil.formatEmptyValue(map.get("clsx"));
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("chxmid", chxmid);
            Example dchyXmglChxmClsxExample = new Example(DchyXmglChxmClsx.class);
            dchyXmglChxmClsxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<String> dchyXmglChxmClsxList = dchyXmglChxmClsxMapper.queryClsxByChxmid(paramMap);
            List<String> clsxList = Arrays.asList(clsx.split(","));//???????????????????????????list
            if (CollectionUtils.isNotEmpty(clsxList)) {
                for (String clsxTemp : clsxList) {
                    if (!dchyXmglChxmClsxList.contains(clsxTemp)) {
                        String clsxid = UUIDGenerator.generate18();
                        DchyXmglChxmClsx dchyXmglChxmClsx = new DchyXmglChxmClsx();
                        dchyXmglChxmClsx.setClsxid(clsxid);
                        dchyXmglChxmClsx.setChxmid(chxmid);
                        dchyXmglChxmClsx.setClsx(clsxTemp);
                        dchyXmglChxmClsx.setClzt(Constants.DCHY_XMGL_CLSX_MRCLZT_ZC);
                        entityMapper.saveOrUpdate(dchyXmglChxmClsx, clsxid);
                    }
                }
//                for(String oldClsx:dchyXmglChxmClsxList){
//                    if (!clsxList.contains(oldClsx)) {
//                        Example example = new Example(DchyXmglChxmClsx.class);
//                        example.createCriteria().andEqualTo("chxmid", chxmid).andEqualTo("clsx",oldClsx);
//                        entityMapper.deleteByExampleNotNull(example);
//                    }
//                }
                resultMap.put("result", "??????");
            }
        } catch (Exception e) {
            logger.error("????????????{}???", e);
            resultMap.put("result", e);
        }
        return resultMap;
    }

    //????????????????????????????????????
    @Override
    public Map<String, Object> saveChdwxxByChxmid(Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String chxmid = CommonUtil.formatEmptyValue(map.get("chxmid"));
            String chdwid = CommonUtil.formatEmptyValue(map.get("chdwid"));
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("chxmid", chxmid);
            List<String> dchyXmglChxmChdwxxList = dchyXmglChxmChdwxxMapper.queryChdwxxByChxmid(paramMap);
            List<String> chdwidList = Arrays.asList(chdwid.split(","));//???????????????????????????list
            if (CollectionUtils.isNotEmpty(chdwidList)) {
                String mlkid;
                for (Iterator<String> chdexxLists = chdwidList.iterator(); chdexxLists.hasNext(); ) {
                    mlkid = chdexxLists.next();
                    if (!dchyXmglChxmChdwxxList.contains(mlkid)) {
                        String chdwxxid = UUIDGenerator.generate18();
                        DchyXmglChxmChdwxx dchyXmglChxmChdwxx = new DchyXmglChxmChdwxx();
                        dchyXmglChxmChdwxx.setChdwxxid(chdwxxid);
                        dchyXmglChxmChdwxx.setChxmid(chxmid);
                        dchyXmglChxmChdwxx.setMlkid(mlkid);
                        DchyXmglMlk dchyXmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
                        if (null != dchyXmglMlk) {
                            dchyXmglChxmChdwxx.setChdwmc(dchyXmglMlk.getDwmc());
                            dchyXmglChxmChdwxx.setChdwlx("1");
                        } else {
                            DchyXmglChdw dchyXmglChdw = entityMapper.selectByPrimaryKey(DchyXmglChdw.class, mlkid);
                            if (null != dchyXmglChdw) {
                                dchyXmglChxmChdwxx.setChdwmc(dchyXmglChdw.getChdwmc());
                                dchyXmglChxmChdwxx.setChdwlx("2");
                            } else {
                                dchyXmglChxmChdwxx.setChdwmc("");
                            }
                        }

                        entityMapper.saveOrUpdate(dchyXmglChxmChdwxx, chdwxxid);
                    }
                }
//                for(String oldChdwid:dchyXmglChxmChdwxxList){
//                    if (!chdwidList.contains(oldChdwid)) {
//                        Example example = new Example(DchyXmglChxmChdwxx.class);
//                        example.createCriteria().andEqualTo("chxmid", chxmid).andEqualTo("mlkid",oldChdwid);
//                        entityMapper.deleteByExampleNotNull(example);
//                    }
//                }
                resultMap.put("result", "??????");
            }
        } catch (Exception e) {
            logger.error("????????????{}???", e);
            resultMap.put("result", e);
        }
        return resultMap;
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    @Override
    public Map<String, Object> deleteFileHtxxJl(Map<String, Object> paramMap) {
        Map<String, Object> result = Maps.newHashMap();
        String msg = ResponseMessage.CODE.SAVE_FAIL.getMsg();
        String code = ResponseMessage.CODE.SAVE_FAIL.getCode();
        try {
            List<Map<String, Object>> data = (List<Map<String, Object>>) paramMap.get("data");
            if (CollectionUtils.isNotEmpty(data)) {
                for (Map<String, Object> datas : data) {
                    String htxxid = CommonUtil.ternaryOperator(datas.get("htxxid"));
                    String wjzxid = CommonUtil.ternaryOperator(datas.get("wjzxid"));
                    if (StringUtils.isNotBlank(wjzxid)) {
                        platformUtil.deleteNodeById(Integer.parseInt(wjzxid));
                    }
                    if (StringUtils.isNotBlank(htxxid)) {
                        //??????????????????
                        int htxxResult = entityMapper.deleteByPrimaryKey(DchyXmglHtxx.class, htxxid);

                        // ????????????????????????????????????
                        Example dchyXmglClsxHtxxGx = new Example(DchyXmglClsxHtxxGx.class);
                        dchyXmglClsxHtxxGx.createCriteria().andEqualTo("chxmid", htxxid);
                        int htxxClsxResult = entityMapper.deleteByExampleNotNull(dchyXmglClsxHtxxGx);

                        // ????????????????????????????????????
                        Example dchyXmglHtxxChdwxxGx = new Example(DchyXmglHtxxChdwxxGx.class);
                        dchyXmglHtxxChdwxxGx.createCriteria().andEqualTo("chxmid", htxxid);
                        int htxxChdwxxResult = entityMapper.deleteByExampleNotNull(dchyXmglHtxxChdwxxGx);

                        List<Map<String, Object>> chdwxxidList = entityMapper.selectByExampleNotNull(dchyXmglHtxxChdwxxGx);

                        if (CollectionUtils.isNotEmpty(chdwxxidList)) {
                            for (Map<String, Object> chdwxxidLists : chdwxxidList) {
                                String chdwxxid = MapUtils.getString(chdwxxidLists, "CHDWXXID");
                                Example dchyXmglClsxChdwxxGx = new Example(DchyXmglClsxChdwxxGx.class);
                                dchyXmglClsxChdwxxGx.createCriteria().andEqualTo("chdwxxid", chdwxxid);
                                entityMapper.deleteByExampleNotNull(dchyXmglClsxChdwxxGx);
                            }
                        }

                        // ??????????????????
                        Example dchyXmglSjxx = new Example(DchyXmglSjxx.class);
                        dchyXmglSjxx.createCriteria().andEqualTo("glsxid", htxxid);
                        int sjxxResult = entityMapper.deleteByExampleNotNull(dchyXmglSjxx);

                        if (htxxResult > 0 && htxxClsxResult > 0 && htxxChdwxxResult > 0 && sjxxResult > 0) {
                            DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
                            Map<String, Object> idMaps = Maps.newHashMap();
                            idMaps.put("htxxid", htxxid);
                            dchyXmglChxmDto.setIdMaps(idMaps);
                            pushDataToMqService.pushSlxxMsgToMq(dchyXmglChxmDto);
                            msg = ResponseMessage.CODE.SUCCESS.getMsg();
                            code = ResponseMessage.CODE.SUCCESS.getCode();
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("????????????{}???", e);
            msg = "????????????";
            code = ResponseMessage.CODE.DELETE_FAIL.getCode();
        }
        result.put("msg", msg);
        result.put("code", code);
        return result;
    }

}