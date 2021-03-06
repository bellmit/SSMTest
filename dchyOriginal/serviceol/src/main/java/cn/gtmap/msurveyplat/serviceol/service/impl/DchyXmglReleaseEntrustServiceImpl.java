package cn.gtmap.msurveyplat.serviceol.service.impl;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.*;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglChgcMapper;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglChxmMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.core.service.UploadService;
import cn.gtmap.msurveyplat.serviceol.model.PfUser;
import cn.gtmap.msurveyplat.serviceol.service.DchyXmglReleaseEntrustService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-11-28
 * description
 */

@Service
public class DchyXmglReleaseEntrustServiceImpl implements DchyXmglReleaseEntrustService, UploadService {
    private final Log logger = LogFactory.getLog(DchyXmglReleaseEntrustServiceImpl.class);

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private Repository repository;
    @Autowired
    private DchyXmglChxmMapper dchyXmglChxmMapper;
    @Autowired
    private DchyXmglChgcMapper dchyXmglChgcMapper;
    @Autowired
    private PlatformUtil platformUtil;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    private final String DELETED = "delete_";

    @Override
    @SystemLog(czmkMc = ProLog.CZMK_XSWT_MC, czmkCode = ProLog.CZMK_XSWT_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE)
    public Page<Map<String, Object>> getEntrustByPage(Map<String, Object> map) {
        Page<Map<String, Object>> jsdwList = null;
        try {
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            String gcbh = CommonUtil.formatEmptyValue(data.get("gcxmbh"));
            String gcmc = CommonUtil.formatEmptyValue(data.get("gcxmmc"));
            String wtbh = CommonUtil.formatEmptyValue(data.get("wtbh"));
            String chdwmc = CommonUtil.formatEmptyValue(data.get("chdwmc"));
            String wtzt = CommonUtil.formatEmptyValue(data.get("wtzt"));
            String userid = UserUtil.getCurrentUserId();

            int page = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("page")));
            int pageSize = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("pageSize")));
            Map paramMap = new HashMap();
            paramMap.put("gcbh", gcbh);
            paramMap.put("gcmc", gcmc);
            paramMap.put("wtbh", wtbh);
            paramMap.put("chdwmc", chdwmc);
            paramMap.put("wtzt", wtzt);

            if (StringUtils.isNotBlank(userid)) {
                Example example = new Example(DchyXmglYhdw.class);
                example.createCriteria().andEqualTo("yhid", userid);
                List<DchyXmglYhdw> dchyXmglYhdwList = entityMapper.selectByExampleNotNull(example);
                if (CollectionUtils.isNotEmpty(dchyXmglYhdwList)) {
                    String dwbh = dchyXmglYhdwList.get(0).getDwbh();
                    if (StringUtils.isNotEmpty(dwbh)) {
                        Example yhidExample = new Example(DchyXmglYhdw.class);
                        yhidExample.createCriteria().andEqualTo("dwbh", dwbh);
                        List<DchyXmglYhdw> yhxxList = entityMapper.selectByExampleNotNull(yhidExample);
                        if (CollectionUtils.isNotEmpty(yhxxList)) {
                            List<String> usidList = Lists.newArrayList();
                            for (DchyXmglYhdw yhxxLists : yhxxList) {
                                String yhid = yhxxLists.getYhid();
                                usidList.add(yhid);
                            }
                            paramMap.put("usidList", usidList);
                        }

                        jsdwList = repository.selectPaging("queryEntrustByPage", paramMap, page - 1, pageSize);
                        logger.info("************************????????????-??????????????????****************************************");
                        logger.info("????????????:" + paramMap);

                        if (null != jsdwList) {
                            List<Map<String, Object>> content = jsdwList.getContent();
                            if (CollectionUtils.isNotEmpty(content)) {

                                for (Map<String, Object> objectMap : content) {
                                    String wtztdm = MapUtils.getString(objectMap, "WTZT");
                                    if (StringUtils.isNotBlank(wtztdm)) {
                                        /*??????????????????*/
                                        if (StringUtils.equals(wtztdm, "6")) {
                                            objectMap.put("WTZT", "?????????");
                                        } else {
                                            String wtztMc = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_WTZT, wtztdm).getMc();
                                            objectMap.put("WTZT", wtztMc);
                                        }
                                    }

                                    String chxmid = MapUtils.getString(objectMap, "CHXMID");
                                    if (StringUtils.isNotBlank(chxmid)) {
                                        /*??????????????????*/
                                        Example example1Chdwxx = new Example(DchyXmglChxmChdwxx.class);
                                        example1Chdwxx.createCriteria().andEqualTo("chxmid", chxmid);
                                        List<DchyXmglChxmChdwxx> chdwxxList = entityMapper.selectByExampleNotNull(example1Chdwxx);
                                        if (CollectionUtils.isNotEmpty(chdwxxList)) {
                                            //???????????????????????????
                                            for (DchyXmglChxmChdwxx chdwxxLists : chdwxxList) {
                                                String rechdwmc = chdwxxLists.getChdwmc();
                                                String rechdwxxid = chdwxxLists.getChdwxxid();
                                                objectMap.put("CHDWMC", rechdwmc);
                                                objectMap.put("CHDWXXID", rechdwxxid);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("????????????{}???", e);
        }

        return jsdwList;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMK_XSWT_MC, czmkCode = ProLog.CZMK_XSWT_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE)
    public List<Map<String, Object>> queryEntrustByChxmid(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));
        List<Map<String, Object>> chxxList = null;
        if (StringUtils.isNotEmpty(chxmid)) {
            Map paramMap = new HashMap();
            paramMap.put("chxmid", chxmid);
            chxxList = dchyXmglChxmMapper.queryEntrustByChxmid(paramMap);
            if (CollectionUtils.isNotEmpty(chxxList)) {
                for (Map chxxLists : chxxList) {
                    List chxmidList = Lists.newArrayList();
                    chxmidList.add(chxmid);
                    Map paramMaps = new HashMap();
                    paramMaps.put("chxmidList", chxmidList);
                    List<Map<String, Object>> clsxList = dchyXmglChgcMapper.getClsxByChxmid(paramMaps);
                    List clsxLists = new ArrayList();
                    if (CollectionUtils.isNotEmpty(clsxList)) {
                        for (Map<String, Object> mapTemp : clsxList) {
                            String clsx = MapUtils.getString(mapTemp, "CLSX");
                            clsxLists.add(clsx);
                        }
                    }
                    chxxLists.put("CLSX", clsxLists);
                    List<Map<String, Object>> chdwxxList = dchyXmglChgcMapper.getChxmChdwxxByChxmid(paramMaps);
                    String mlkid = null;
                    if (CollectionUtils.isNotEmpty(chdwxxList)) {
                        for (Map chdwxxLists : chdwxxList) {
                            mlkid = MapUtils.getString(chdwxxLists, "MLKID");
                        }
                    }
                    chxxLists.put("MLKID", mlkid);
                }

            }
            DataSecurityUtil.decryptMapList(chxxList);
        }

        return chxxList;
    }

    @Override
    public Map<String, Object> initEntrust() {
        DchyXmglChxm dchyXmglChxm = new DchyXmglChxm();
        dchyXmglChxm.setChxmid(UUIDGenerator.generate());
        entityMapper.insertSelective(dchyXmglChxm);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        String xqfbbhLsh = obtainXqfbbh();
        //????????????(??????????????????)?????????????????????????????????,???????????????0????????????
        if (StringUtils.equals("000", xqfbbhLsh)) {
            xqfbbhLsh = obtainXqfbbh();
        }
        String wtbh = ("WT" + sdf2.format(new Date()) + xqfbbhLsh).replaceAll(" ", "");
        //?????????????????????yhdw??????????????????????????????wtdw???
        String yhid = UserUtil.getCurrentUserId();
        Example example = new Example(DchyXmglYhdw.class);
        example.createCriteria().andEqualTo("yhid", yhid);
        List<DchyXmglYhdw> dchyXmglYhdwList = entityMapper.selectByExampleNotNull(example);
        String wtdw = "";
        String lxr = "";
        String lxdh = "";
        if (CollectionUtils.isNotEmpty(dchyXmglYhdwList) && StringUtils.isNotBlank(dchyXmglYhdwList.get(0).getDwmc())) {
            wtdw = dchyXmglYhdwList.get(0).getDwmc();
        }

        if (StringUtils.isNotBlank(yhid)) {
            PfUser pfUser = entityMapper.selectByPrimaryKey(PfUser.class, yhid);
            lxr = pfUser.getUserName();
            lxdh = pfUser.getMobilePhone();
        }
        Map result = Maps.newHashMap();
        result.put("chxmid", dchyXmglChxm.getChxmid());
        result.put("wtbh", wtbh);
        result.put("wtdw", wtdw);
        result.put("lxr", lxr);
        result.put("lxdh", lxdh);

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @SystemLog(czmkMc = ProLog.CZMK_XSWT_MC, czmkCode = ProLog.CZMK_XSWT_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE)
    public boolean saveEntrust(Map<String, Object> map) {
        try {
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            String chgcbh = CommonUtil.formatEmptyValue(data.get("gcbh"));
            String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));

            //????????????
            DchyXmglChgc dchyXmglChgc = new DchyXmglChgc();
            if (StringUtils.isNotEmpty(chgcbh)) {
                Example chgcExample = new Example(DchyXmglChgc.class);
                chgcExample.createCriteria().andEqualTo("gcbh", chgcbh);
                List<DchyXmglChgc> dchyXmglChgcList = entityMapper.selectByExampleNotNull(chgcExample);
                if (CollectionUtils.isNotEmpty(dchyXmglChgcList)) {
                    //????????????????????????????????????????????????
                    for (DchyXmglChgc chgc : dchyXmglChgcList) {
                        dchyXmglChgc.setChgcid(chgc.getChgcid());
                    }
                } else {
                    dchyXmglChgc.setChgcid(UUIDGenerator.generate18());
                }
            } else {
                dchyXmglChgc.setChgcid(UUIDGenerator.generate18());
            }
            dchyXmglChgc.setGcbh(CommonUtil.formatEmptyValue(data.get("gcbh")));
            dchyXmglChgc.setGcmc(CommonUtil.formatEmptyValue(data.get("gcmc")));
            dchyXmglChgc.setLxr(CommonUtil.formatEmptyValue(data.get("lxr")));
            dchyXmglChgc.setLxdh(CommonUtil.formatEmptyValue(data.get("lxdh")));
            dchyXmglChgc.setGcdzs(CommonUtil.formatEmptyValue(data.get("gcdzs")));
            dchyXmglChgc.setGcdzss(CommonUtil.formatEmptyValue(data.get("gcdzss")));
            dchyXmglChgc.setGcdzqx(CommonUtil.formatEmptyValue(data.get("gcdzqx")));
            dchyXmglChgc.setGcdzxx(CommonUtil.formatEmptyValue(data.get("gcdzxx")));
            dchyXmglChgc.setWtdw(CommonUtil.formatEmptyValue(data.get("wtdw")));
            dchyXmglChgc.setCjr(UserUtil.getCurrentUserId());
            dchyXmglChgc.setXmly(Constants.XMLY_XSFB);
            DataSecurityUtil.encryptSingleObject(dchyXmglChgc);
            entityMapper.saveOrUpdate(dchyXmglChgc, dchyXmglChgc.getChgcid());

            //????????????
            DchyXmglChxm dchyXmglChxm = new DchyXmglChxm();
            dchyXmglChxm.setChxmid(chxmid);
            dchyXmglChxm.setChgcid(dchyXmglChgc.getChgcid());
            dchyXmglChxm.setChgcbh(CommonUtil.formatEmptyValue(data.get("gcbh")));
            dchyXmglChxm.setZdxm(CommonUtil.formatEmptyValue(data.get("zdxm")));//?????????????????????
            dchyXmglChxm.setFbr(UserUtil.getCurrentUserId());
            dchyXmglChxm.setXmly(Constants.XMLY_XSWT);
            dchyXmglChxm.setIshy(Constants.ISHY_WHY);
            dchyXmglChxm.setXmzt(Constants.XMZT_DBA); //?????????
            dchyXmglChxm.setWtzt(CommonUtil.formatEmptyValue(data.get("wtzt")));
            dchyXmglChxm.setXmlx(CommonUtil.formatEmptyValue(data.get("xmlx")));
            dchyXmglChxm.setBar(UserUtil.getCurrentUserId());
            dchyXmglChxm.setBarmc(UserUtil.getUserNameById(UserUtil.getCurrentUserId()));
            dchyXmglChxm.setSfgq(Constants.NO); //????????????
            String ys = CommonUtil.formatEmptyValue(data.get("ys"));
            if (StringUtils.isNotEmpty(ys)) {
                dchyXmglChxm.setYs(Integer.parseInt(ys));
            }

            //???????????????????????????
            String wtzt = CommonUtil.formatEmptyValue(data.get("wtzt"));
            if (StringUtils.isNotEmpty(wtzt) && StringUtils.equals(wtzt, Constants.FBWT_QRWT)) {
                dchyXmglChxm.setFbsj(new Date());
            }

            //??????????????????????????????
            String wtbh = null;
            DchyXmglChxm dchyXmglChxms = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            if (dchyXmglChxms != null) {
                if (StringUtils.isNotEmpty(dchyXmglChxms.getXqfbbh())) {
                    wtbh = dchyXmglChxms.getXqfbbh();
                } else {
                    wtbh = CommonUtil.formatEmptyValue(data.get("wtbh"));
                }
            }
            dchyXmglChxm.setXqfbbh(wtbh);

            //????????????
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String cgjfrqs = CommonUtil.formatEmptyValue(data.get("gcjfrq"));
                if (StringUtils.isNotEmpty(cgjfrqs)) {
                    Date cgjfrq = simpleDateFormat.parse(CommonUtil.formatEmptyValue(data.get("gcjfrq")));
                    dchyXmglChxm.setCgjfrq(cgjfrq);
                }
            } catch (Exception e) {
                logger.error("????????????{}???", e);
            }
            entityMapper.saveOrUpdate(dchyXmglChxm, dchyXmglChxm.getChxmid());

            //????????????
            List<Map<String, Object>> clsxList = (List<Map<String, Object>>) data.get("clsx");
            List<DchyXmglChxmClsx> dchyXmglChxmClsxList = generateClsx(chxmid, clsxList);
            if(CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)){
                for(DchyXmglChxmClsx chxmClsxList:dchyXmglChxmClsxList){
                    entityMapper.saveOrUpdate(chxmClsxList,chxmClsxList.getClsxid());
                }
            }

            //????????????????????????????????????
            //String fwlx = CommonUtil.formatEmptyValue(data.get("fwxz"));
            //List<Map<String, Object>> chtlList = (List<Map<String, Object>>) data.get("chtl");
            //List<DchyXmglClsxChtl> dchyXmglClsxChtlList = generateChtl(chxmid, fwlx, chtlList);

            //????????????
            List<Map<String, Object>> chdwmcList = (List<Map<String, Object>>) data.get("chdwxx");
            if (CollectionUtils.isNotEmpty(chdwmcList)) {
                for (Map<String, Object> chdwidmap : chdwmcList) {
                    DchyXmglChxmChdwxx dchyXmglChxmChdwxx = new DchyXmglChxmChdwxx();
                    String chdwmc = MapUtils.getString(chdwidmap, "chdwmc");
                    String chdwid = MapUtils.getString(chdwidmap, "chdwid");

                    Example example = new Example(DchyXmglChxmChdwxx.class);
                    example.createCriteria().andEqualTo("mlkid", chdwid).andEqualTo("chxmid", chxmid);
                    List<DchyXmglChxmChdwxx> chdwxxList = entityMapper.selectByExampleNotNull(example);
                    String chdwxxid = null;
                    if (CollectionUtils.isNotEmpty(chdwxxList)) {
                        for (DchyXmglChxmChdwxx chdwxxLists : chdwxxList) {
                            chdwxxid = chdwxxLists.getChdwxxid();
                        }
                    } else {
                        chdwxxid = UUIDGenerator.generate18();
                    }

                    dchyXmglChxmChdwxx.setChdwxxid(chdwxxid);
                    dchyXmglChxmChdwxx.setChxmid(chxmid);
                    dchyXmglChxmChdwxx.setChdwmc(chdwmc);
                    dchyXmglChxmChdwxx.setMlkid(chdwid);
                    dchyXmglChxmChdwxx.setPjzt(Constants.PJZT_WPJ);
                    entityMapper.saveOrUpdate(dchyXmglChxmChdwxx, dchyXmglChxmChdwxx.getChdwxxid());
                }
            }

            //????????????,???????????????????????????????????????
            if (StringUtils.equals(wtzt, Constants.FBWT_QRWT)) {
                DchyXmglSqxx dchyXmglSqxx = this.initSqxx(chxmid);
                String sqxxid = dchyXmglSqxx.getSqxxid();
                Map paramMap = Maps.newHashMap();
                paramMap.put("sqxxid", sqxxid);
                paramMap.put("zrryid", UserUtil.getCurrentUserId());
                paramMap.put("sqzt", Constants.DCHY_XMGL_SQZT_DSH);
                Map resultMap = this.insertDbrw(paramMap);
                if (resultMap != null) {
                    //????????????
                    String chdw = CommonUtil.formatEmptyValue(data.get("chdw"));
                    Map<String, Object> yhxxMap = Maps.newHashMap();
                    yhxxMap.put("chdwid", chdw);
                    yhxxMap.put("wtdw", CommonUtil.formatEmptyValue(data.get("wtdw")));
                    yhxxMap.put("chxmid", chxmid);
                    yhxxMap.put("gcmc", CommonUtil.formatEmptyValue(data.get("gcmc")));
                    this.sendMessage(yhxxMap);
                }
            }
        } catch (Exception e) {
            logger.error("????????????{}???", e);
            return false;
        }
        return true;
    }

    /**
     * @return java.util.List<cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmClsx>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: chxmid
     * @param: clsx
     * @time 2020/12/13 14:19
     * @description ??????????????????
     */
    private List<DchyXmglChxmClsx> generateClsx(String chxmid, List<Map<String, Object>> clsxLists) {
        List<DchyXmglChxmClsx> dchyXmglChxmClsxList = Lists.newArrayList();
        Example dchyXmglChxmClsxExample = new Example(DchyXmglChxmClsx.class);
        dchyXmglChxmClsxExample.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglChxmClsx> oldDchyXmglChxmClsxList = entityMapper.selectByExampleNotNull(dchyXmglChxmClsxExample);
        if (CollectionUtils.isNotEmpty(clsxLists)) {
            List clsxList = Lists.newArrayList();
            for (Map<String, Object> clsxMaps : clsxLists) {
                String clsx = MapUtils.getString(clsxMaps, "clsxdm");
                clsxList.add(clsx);
            }
            // ??????????????????????????????????????????????????????id
            List matchedClsxid = Lists.newArrayList();
            List<DchyXmglChxmClsx> matchedClsxidMap = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(oldDchyXmglChxmClsxList)) {
                for (DchyXmglChxmClsx dchyXmglChxmClsx : oldDchyXmglChxmClsxList) {
                    if (clsxList.contains(dchyXmglChxmClsx.getClsx())) {
                        Map<String, Object> clsxidMaps = Maps.newHashMap();
                        clsxidMaps.put("clsxid", dchyXmglChxmClsx.getClsxid());
                        matchedClsxid.add(dchyXmglChxmClsx.getClsx());
                        matchedClsxidMap.add(dchyXmglChxmClsx);

                    } else {
                        dchyXmglChxmClsx.setClsxid(DELETED + dchyXmglChxmClsx.getClsxid());
                    }
                    dchyXmglChxmClsxList.add(dchyXmglChxmClsx);
                }
            }
            // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            List clsxLfet = (List) CollectionUtils.removeAll(clsxList, matchedClsxid);
            if (CollectionUtils.isNotEmpty(clsxLfet)) {
                for (Map<String, Object> clsxMaps : clsxLists) {
                    if (clsxLfet.contains(MapUtils.getString(clsxMaps, "clsxdm"))) {
                        DchyXmglChxmClsx dchyXmglChxmClsx = new DchyXmglChxmClsx();
                        String clsxs = MapUtils.getString(clsxMaps, "clsxdm");
                        String jcrq = MapUtils.getString(clsxMaps, "jcrq");
                        String yjjfrq = MapUtils.getString(clsxMaps, "yjjfrq");
                        String clsxid = UUIDGenerator.generate18();
                        dchyXmglChxmClsx.setClsxid(clsxid);
                        dchyXmglChxmClsx.setClsx(clsxs);
                        dchyXmglChxmClsx.setJcrq(CalendarUtil.formatDate(jcrq));
                        dchyXmglChxmClsx.setYjjfrq(yjjfrq);
                        dchyXmglChxmClsx.setChxmid(chxmid);
                        dchyXmglChxmClsx.setClzt(Constants.DCHY_XMGL_CLSX_MRCLZT_ZC);
                        dchyXmglChxmClsxList.add(dchyXmglChxmClsx);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(matchedClsxidMap)) {
                for (Map<String, Object> clsxMaps : clsxLists) {
                    for (DchyXmglChxmClsx matchedClsxidMaps : matchedClsxidMap) {
                        if (StringUtils.equals(MapUtils.getString(clsxMaps, "clsxdm"), matchedClsxidMaps.getClsx())) {
                            Date jcrq = CalendarUtil.formatDate(CommonUtil.formatEmptyValue(MapUtils.getString(clsxMaps, "jcrq")));
                            String yjjfrq = MapUtils.getString(clsxMaps, "yjjfrq");
                            String clsxid = matchedClsxidMaps.getClsxid();
                            matchedClsxidMaps.setClsxid(clsxid);
                            matchedClsxidMaps.setJcrq(jcrq);
                            matchedClsxidMaps.setYjjfrq(yjjfrq);
                            dchyXmglChxmClsxList.add(matchedClsxidMaps);
                        }
                    }
                }
            }
        } else if (CollectionUtils.isNotEmpty(oldDchyXmglChxmClsxList)) {
            // ??????????????????????????????
            for (DchyXmglChxmClsx dchyXmglChxmClsx : oldDchyXmglChxmClsxList) {
                dchyXmglChxmClsx.setClsxid(DELETED + dchyXmglChxmClsx.getClsxid());
                dchyXmglChxmClsxList.add(dchyXmglChxmClsx);
            }
        }
        return dchyXmglChxmClsxList;
    }

    /**
     * @return java.util.List<cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClsxChtl>
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @param: chxmid
     * @param: chtlMap
     * @time 2021/06/21 11:19
     * @description ??????????????????????????????
     */
    private List<DchyXmglClsxChtl> generateChtl(String chxmid,String fwlx , List<Map<String, Object>> chtlMap) {
        List<DchyXmglClsxChtl> dchyXmglClsxChtlList = Lists.newArrayList();
        //?????????????????????
        Example dchyXmglClsxChtlExample = new Example(DchyXmglClsxChtl.class);
        dchyXmglClsxChtlExample.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglClsxChtl> oldDchyXmglClsxChtl = entityMapper.selectByExampleNotNull(dchyXmglClsxChtlExample);
        if (CollectionUtils.isNotEmpty(chtlMap)) {
            DchyXmglClsxChtl dchyXmglClsxChtl = new DchyXmglClsxChtl();
            for (Map<String, Object> chtl : chtlMap) {
                String clsx = MapUtils.getString(chtl, "clsx");
                String sl = MapUtils.getString(chtl, "sl");
                String jd = MapUtils.getString(chtl, "jd");
                String dw = MapUtils.getString(chtl, "dw");
                Date jcrq =CalendarUtil.formatDate(MapUtils.getString(chtl, "jcrq"));
                dchyXmglClsxChtl.setChtlid(UUIDGenerator.generate18());
                dchyXmglClsxChtl.setChxmid(chxmid);
                dchyXmglClsxChtl.setClsx(clsx);
                dchyXmglClsxChtl.setSl(sl);
                if (StringUtils.isNotEmpty(dw)) {
                    if (StringUtils.equals(dw, Constants.CLSX_CHTL_MC_M)) {
                        dw = Constants.CLSX_CHTL_DM_M;
                    } else if (StringUtils.equals(dw, Constants.CLSX_CHTL_MC_PFM)) {
                        dw = Constants.CLSX_CHTL_DM_PFM;
                    } else if (StringUtils.equals(dw, Constants.CLSX_CHTL_MC_Z)) {
                        dw = Constants.CLSX_CHTL_DM_Z;
                    }
                    dchyXmglClsxChtl.setDw(dw);
                }
                dchyXmglClsxChtl.setJcrq(jcrq);

                //????????????
                if (StringUtils.isNotEmpty(sl) && StringUtils.isNotEmpty(jd)) {
                    String dm = null;
                    if (StringUtils.equals(jd, Constants.CLSX_JD_ZS) && StringUtils.equals(dw, Constants.CLSX_CHTL_MC_PFM)) {
                        //????????????,??????????????????
                        dm = Constants.CLSX_JD_ZS;
                    }

                    if (StringUtils.equals(jd, Constants.CLSX_JD_LX) && StringUtils.equals(dw, Constants.CLSX_CHTL_MC_PFM)) {
                        //????????????,??????????????????
                        dm = Constants.CLSX_JD_LX;
                    }

                    if (StringUtils.equals(jd, Constants.CLSX_JD_GX)) {
                        //????????????
                        if (StringUtils.equals(clsx, Constants.CLSX_XXGC)) {
                            dm = Constants.CLSX_JD_GX_XXGC;
                        } else if (StringUtils.equals(clsx, Constants.CLSX_FCYC)) {
                            dm = Constants.CLSX_JD_GX_FCYC;
                        }
                    }

                    if (StringUtils.equals(jd, Constants.CLSX_JD_SG)) {
                        //????????????
                        if (StringUtils.equals(clsx, Constants.CLSX_XXFY) && StringUtils.equals(dw, Constants.CLSX_CHTL_MC_M)) {
                            dm = Constants.CLSX_JD_SG_XXFY;
                        } else if (StringUtils.equals(clsx, Constants.CLSX_HXYX)) {
                            dm = Constants.CLSX_JD_SG_HXYX;
                        }
                    }

                    if (StringUtils.equals(jd, Constants.CLSX_JD_JG)) {
                        //????????????
                        if (StringUtils.equals(dw, Constants.CLSX_CHTL_MC_M)) {
                            dm = Constants.CLSX_JD_JG_XXGC;
                        } else if (StringUtils.equals(dw, Constants.CLSX_CHTL_MC_PFM)) {
                            if (StringUtils.isNotEmpty(fwlx) && StringUtils.equals(fwlx, Constants.FWLX_SPF)) {
                                dm = Constants.CLSX_JD_JG_SPF;
                            } else if (StringUtils.isNotEmpty(fwlx) && StringUtils.equals(fwlx, Constants.FWLX_DWF)) {
                                dm = Constants.CLSX_JD_JG_DWF;
                            }
                        }
                    }

                    DchyXmglClsxChtlPz dccyXmglClsxChtlPz = entityMapper.selectByPrimaryKey(DchyXmglClsxChtlPz.class, dm);
                    if (dccyXmglClsxChtlPz != null) {
                        String jsgs = dccyXmglClsxChtlPz.getJsgs();
                        if (StringUtils.isNotEmpty(jsgs)) {
                            if (StringUtils.equals(clsx, Constants.CLSX_XXFY) && StringUtils.equals(dw, Constants.CLSX_CHTL_MC_Z)) {
                                //????????????,???????????????????????????5???????????????
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.DATE, 5);
                                dchyXmglClsxChtl.setJfrq(c.getTime());
                            } else {
                                double jsrq = Integer.parseInt(jsgs);
                                int count = new Double(jsrq).intValue();
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.DATE, count);
                                dchyXmglClsxChtl.setJfrq(c.getTime());
                            }
                        }
                    }
                }
                dchyXmglClsxChtlList.add(dchyXmglClsxChtl);
            }
        } else if (CollectionUtils.isNotEmpty(oldDchyXmglClsxChtl)) {
            //??????????????????????????????
            for (DchyXmglClsxChtl dchyXmglClsxChtl : oldDchyXmglClsxChtl) {
                dchyXmglClsxChtl.setChtlid(DELETED + dchyXmglClsxChtl.getChtlid());
                dchyXmglClsxChtlList.add(dchyXmglClsxChtl);
            }
        }
        return dchyXmglClsxChtlList;
    }

    public int sendMessage(Map<String, Object> hyxxMap) {
        ///????????????
        Example jsdwExample = new Example(DchyXmglYhxxPz.class);
        jsdwExample.createCriteria().andEqualTo("xxlx", Constants.DCHY_XMGL_ZD_XXNR_JSDWWT);
        List<DchyXmglYhxxPz> xxnrzd = entityMapper.selectByExampleNotNull(jsdwExample);
        String jsdwxxnr = null;
        if (CollectionUtils.isNotEmpty(xxnrzd)) {
            jsdwxxnr = xxnrzd.get(0).getXxnr().replaceAll("????????????", MapUtils.getString(hyxxMap, "gcmc", StringUtils.EMPTY));
        }

        Example example = new Example(DchyXmglYhxxPz.class);
        example.createCriteria().andEqualTo("xxlx", Constants.DCHY_XMGL_ZD_XXNR_CHDWWT);
        List<DchyXmglYhxxPz> xxnrzds = entityMapper.selectByExampleNotNull(example);
        String chdwxxnr = null;
        if (CollectionUtils.isNotEmpty(xxnrzd)) {
            chdwxxnr = xxnrzds.get(0).getXxnr();
        }

        //??????????????????
        DchyXmglYhxx chdwYhxx = new DchyXmglYhxx();
        chdwYhxx.setYhxxid(UUIDGenerator.generate18());
        chdwYhxx.setJsyhid(MapUtils.getString(hyxxMap, "chdwid"));//?????????---????????????
        chdwYhxx.setGlsxid(MapUtils.getString(hyxxMap, "chxmid"));
        chdwYhxx.setXxnr(chdwxxnr);
        chdwYhxx.setFsyhid(UserUtil.getCurrentUserId());//?????????---??????????????????
        chdwYhxx.setDqzt(Constants.INVALID);
        chdwYhxx.setFssj(CalendarUtil.getCurHMSDate());
        chdwYhxx.setDqsj(null);
        chdwYhxx.setXxlx(Constants.DCHY_XMGL_ZD_XXNR_CHDWWT);
        chdwYhxx.setSftz(Constants.XXTX_SFTZ_BTZ); //??????
        entityMapper.saveOrUpdate(chdwYhxx, chdwYhxx.getYhxxid());

        //??????????????????
        DchyXmglYhxx jsdwYhxx = new DchyXmglYhxx();
        jsdwYhxx.setFsyhid(UserUtil.getCurrentUserId());
        jsdwYhxx.setFssj(CalendarUtil.getCurHMSDate());
        jsdwYhxx.setDqzt(Constants.INVALID);//??????
        jsdwYhxx.setYhxxid(UUIDGenerator.generate18());
        jsdwYhxx.setJsyhid(MapUtils.getString(hyxxMap, "gcmc")); //?????????---????????????
        jsdwYhxx.setGlsxid(MapUtils.getString(hyxxMap, "chxmid"));
        jsdwYhxx.setXxnr(jsdwxxnr); //????????????
        jsdwYhxx.setXxlx(Constants.DCHY_XMGL_ZD_XXNR_JSDWWT); //????????????
        jsdwYhxx.setSftz(Constants.XXTX_SFTZ_BTZ); //??????
        return entityMapper.saveOrUpdate(jsdwYhxx, jsdwYhxx.getYhxxid());
    }

    public DchyXmglSqxx initSqxx(String chxmid) {
        //??????????????????
        DchyXmglSqxx dchyXmglSqxx = new DchyXmglSqxx();
        /*??????sqxxid*/
        String sqxxid = UUIDGenerator.generate18();
        String blsx = Constants.BLSX_FBWT; //????????????
        String sqr = UserUtil.getCurrentUserId();
        String sqzt = Constants.DCHY_XMGL_SQZT_DSH;//1:?????????
        String sqbh = Calendar.getInstance().getTimeInMillis() + sqxxid;
        Date sqsj = new Date();
        dchyXmglSqxx.setSqxxid(sqxxid);
        //????????????id???dchy_xmgl_yhdw?????????dwmc??????
        dchyXmglSqxx.setSqjgmc(getDwmc());
        dchyXmglSqxx.setBlsx(blsx);
        dchyXmglSqxx.setSqr(sqr);
        dchyXmglSqxx.setSqrmc(dchyXmglChxmMapper.queryUsername(UserUtil.getCurrentUserId()));
        dchyXmglSqxx.setSqsj(sqsj);
        dchyXmglSqxx.setSqzt(sqzt);
        dchyXmglSqxx.setGlsxid(chxmid);
        dchyXmglSqxx.setSqbh(sqbh);
        int result = entityMapper.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
        if (result > 0) {
            return dchyXmglSqxx;
        }
        return null;
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    private String getDwmc() {
        String yhId = UserUtil.getCurrentUserId();
        Example example = new Example(DchyXmglYhdw.class);
        example.createCriteria().andEqualTo("yhid", yhId);
        List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(yhdwList)) {
            String dwmc = yhdwList.get(0).getDwmc();
            if (StringUtils.isNotBlank(dwmc)) {
                return dwmc;
            }
        }
        return "";
    }

    public Map insertDbrw(Map<String, Object> paramMap) {
        String zrryid = CommonUtil.formatEmptyValue(paramMap.get("zrryid"));
        String sqxxid = CommonUtil.formatEmptyValue(paramMap.get("sqxxid"));
        String sqzt = CommonUtil.formatEmptyValue(paramMap.get("sqzt"));
        Map map = Maps.newHashMap();
        String code = ResponseMessage.CODE.DELETE_FAIL.getCode();
        String msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
        if (StringUtils.isNoneEmpty(zrryid, sqxxid)) {
            Example example = new Example(DchyXmglDbrw.class);
            example.createCriteria().andEqualTo("sqxxid", sqxxid);
            List<DchyXmglDbrw> dchyXmglDbrwList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglDbrwList)) {
                msg = "??????????????????";
            } else {
                DchyXmglDbrw dchyXmglDbrw = new DchyXmglDbrw();
                dchyXmglDbrw.setDbrwid(UUIDGenerator.generate18());
                dchyXmglDbrw.setSqxxid(sqxxid);
                dchyXmglDbrw.setZrryid(zrryid);
                dchyXmglDbrw.setZrsj(new Date());
                dchyXmglDbrw.setDqjd(Constants.DQJD_SH);
                entityMapper.insertSelective(dchyXmglDbrw);

                DchyXmglSqxx dchyXmglSqxx = new DchyXmglSqxx();
                dchyXmglSqxx.setSqxxid(sqxxid);
                dchyXmglSqxx.setSqzt(sqzt);

                entityMapper.updateByPrimaryKeySelective(dchyXmglSqxx);
                code = ResponseMessage.CODE.SUCCESS.getCode();
                msg = ResponseMessage.CODE.SUCCESS.getMsg();
            }
        }
        map.put("msg", msg);
        map.put("code", code);
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean retrieveEntrust(Map<String, Object> map) {
        try {
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));
            String wtzt = CommonUtil.formatEmptyValue(data.get("wtzt"));
            Example chxmExample = new Example(DchyXmglChxm.class);
            chxmExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExampleNotNull(chxmExample);
            if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                for (DchyXmglChxm dchyXmglChxmLists : dchyXmglChxmList) {
                    String ishy = dchyXmglChxmLists.getIshy();
                    if (StringUtils.equals(ishy, "0")) {
                        DchyXmglChxm chxm = new DchyXmglChxm();
                        chxm.setWtzt(wtzt);
                        chxm.setChxmid(chxmid);
                        entityMapper.saveOrUpdate(chxm, chxm.getChxmid());
                    } else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("????????????{}???", e);
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    @SystemLog(czmkMc = ProLog.CZMK_XSWT_MC, czmkCode = ProLog.CZMK_XSWT_CODE, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE)
    public synchronized Map<String, Object> deleteEntrust(Map<String, Object> paramMap) {
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        String code = ResponseMessage.CODE.DELETE_FAIL.getCode();
        String msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
        if (null != paramMap && paramMap.containsKey("data")) {
            Map data = MapUtils.getMap(paramMap, "data");
            String chxmid = MapUtils.getString(data, "chxmid");
            if (StringUtils.isNotBlank(chxmid)) {
                DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
                if (null != dchyXmglChxm) {
                    if (StringUtils.isNoneBlank(dchyXmglChxm.getChgcbh(), dchyXmglChxm.getChgcid())) {
                        Example dchyXmglChxmExample = new Example(DchyXmglChxm.class);
                        dchyXmglChxmExample.createCriteria().andEqualTo("chgcbh", dchyXmglChxm.getChgcbh());
                        List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExampleNotNull(dchyXmglChxmExample);
                        if (CollectionUtils.size(dchyXmglChxmList) == 1 && StringUtils.isNotBlank(dchyXmglChxm.getChgcid())) {
                            // ????????????????????????????????????????????????????????????
                            DchyXmglChgc dchyXmglChgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, dchyXmglChxm.getChgcid());
                            dchyXmglChxmDto.setDchyXmglChgc(dchyXmglChgc);
                            entityMapper.deleteByPrimaryKey(DchyXmglChgc.class, dchyXmglChxm.getChgcid());
                        }
                    }
                    // ??????????????????
                    Example dchyXmglChxmClsxExample = new Example(DchyXmglChxmClsx.class);
                    dchyXmglChxmClsxExample.createCriteria().andEqualTo("chxmid", dchyXmglChxm.getChxmid());
                    entityMapper.deleteByExampleNotNull(dchyXmglChxmClsxExample);

                    // ??????????????????
                    dchyXmglChxmDto.setDchyXmglChxm(dchyXmglChxm);
                    entityMapper.deleteByPrimaryKey(DchyXmglChxm.class, chxmid);

                    //???????????????????????????????????????
                    Example dchyXmglChxmChdwxxExample = new Example(DchyXmglChxmChdwxx.class);
                    dchyXmglChxmChdwxxExample.createCriteria().andEqualTo("chxmid", dchyXmglChxm.getChxmid());
                    entityMapper.deleteByExampleNotNull(dchyXmglChxmChdwxxExample);
                    dchyXmglChxmDto.setDchyXmglChxmChdwxxList(entityMapper.selectByExample(dchyXmglChxmChdwxxExample));

                    //???????????????????????????????????????
                    Example dchyXmglClsxChdwxxGxExample = new Example(DchyXmglClsxChdwxxGx.class);
                    dchyXmglClsxChdwxxGxExample.createCriteria().andEqualTo("chxmid", dchyXmglChxm.getChxmid());
                    entityMapper.deleteByExampleNotNull(dchyXmglClsxChdwxxGxExample);
                    dchyXmglChxmDto.setDchyXmglClsxChdwxxGxList(entityMapper.selectByExample(dchyXmglClsxChdwxxGxExample));

                    // ??????????????????
                    Example dchyXmglSjxxExample = new Example(DchyXmglSjxx.class);
                    dchyXmglSjxxExample.createCriteria().andEqualTo("glsxid", chxmid);
                    List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExampleNotNull(dchyXmglSjxxExample);
                    dchyXmglChxmDto.setDchyXmglSjxxList(dchyXmglSjxxList);
                    if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                        Example dchyXmglSjclExample = new Example(DchyXmglSjcl.class);
                        for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                            // ??????????????????
                            dchyXmglSjclExample.clear();
                            dchyXmglSjclExample.createCriteria().andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                            dchyXmglChxmDto.setDchyXmglSjclList(entityMapper.selectByExample(dchyXmglSjclExample));
                            entityMapper.deleteByExampleNotNull(dchyXmglSjclExample);

                            // todo ??????filecenter
                            // ??????????????????
                            entityMapper.deleteByPrimaryKey(DchyXmglSjxx.class, dchyXmglSjxx.getSjxxid());
                        }
                    }
                    /*fileCenter????????????*/
                    int node = platformUtil.creatNode(chxmid);
                    platformUtil.deleteNodeById(node);
                    code = ResponseMessage.CODE.SUCCESS.getCode();
                    msg = ResponseMessage.CODE.SUCCESS.getMsg();
                } else {
                    code = ResponseMessage.CODE.DELETE_FAIL.getCode();
                    msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
                }
            } else {
                code = ResponseMessage.CODE.DELETE_FAIL.getCode();
                msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
            }
        } else {
            code = ResponseMessage.CODE.DELETE_FAIL.getCode();
            msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
        }
        Map resultMap = Maps.newHashMap();
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        return resultMap;
    }

    public String obtainXqfbbh() {
        String xqfbbh = "";
        String xqfbbhLsh = dchyXmglChxmMapper.queryMaxXqfbbh();
        if (xqfbbhLsh.length() > 3) {
            xqfbbh = xqfbbhLsh.substring(xqfbbhLsh.length() - 3, xqfbbhLsh.length());
        } else {
            xqfbbh = xqfbbhLsh;
        }
        return xqfbbh;
    }

    @Override
    @Transactional
    public boolean updateWjzxid(String wjzxid, String glsxid) {
        boolean updated = false;
        if (StringUtils.isNotBlank(glsxid)) {
            DchyXmglChxm dchyXmglChxm = new DchyXmglChxm();
            dchyXmglChxm.setChxmid(glsxid);
            dchyXmglChxm.setWjzxid(wjzxid);
            int i = entityMapper.updateByPrimaryKeySelective(dchyXmglChxm);
            if (i > 0) {
                updated = true;
            }
        }
        return updated;
    }

    @Override
    public String getSsmkid() {
        return SsmkidEnum.JSDWWT.getCode();
    }

}
