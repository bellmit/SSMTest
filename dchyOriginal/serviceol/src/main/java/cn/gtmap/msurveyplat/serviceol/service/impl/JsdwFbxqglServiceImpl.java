package cn.gtmap.msurveyplat.serviceol.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.SsmkidEnum;
import cn.gtmap.msurveyplat.serviceol.core.mapper.*;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.core.service.UploadService;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.send.SendXmxgxxToXmglServiceImpl;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.Impl.JsdwFbxfwServiceImpl;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.serviceol.core.xsbfmapper.XsbfDchyXmglChgcMapper;
import cn.gtmap.msurveyplat.serviceol.core.xsbfmapper.XsbfDchyXmglHtxxMapper;
import cn.gtmap.msurveyplat.serviceol.service.JsdwFbxqglService;
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

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-11-28
 * description
 */

@Service
public class JsdwFbxqglServiceImpl implements JsdwFbxqglService, UploadService {
    private final Log logger = LogFactory.getLog(JsdwFbxqglServiceImpl.class);


    @Autowired
    EntityMapper entityMapper;
    @Autowired
    XsbfDchyXmglChgcMapper xsbfDchyXmglChgcMapper;
    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapperXSBF;
    @Autowired
    private Repository repository;
    @Resource(name = "repositoryXSBF")
    @Autowired
    private Repository repositoryXSBF;
    @Autowired
    private DchyXmglChxmMapper dchyXmglChxmMapper;
    @Autowired
    private DchyXmglChgcMapper dchyXmglChgcMapper;
    @Autowired
    private DchyXmglChxmChdwxxMapper dchyXmglChxmChdwxxMapper;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;
    @Autowired
    private DchyXmglChdwMapper dchyXmglChdwMapper;
    @Autowired
    private PlatformUtil platformUtil;
    @Autowired
    private PushDataToMqService pushDataToMqService;
    @Autowired
    private SendXmxgxxToXmglServiceImpl sendXmxgxxToXmglService;
    @Autowired
    private JsdwFbxfwServiceImpl obtainOnlinDataService;
    @Autowired
    private XsbfDchyXmglHtxxMapper xsbfDchyXmglHtxxMapper;
    @Autowired
    private DchyXmglOnlineDelegationMapper onlineDelegationMapper;

    @Override
    public Page<Map<String, Object>> getJsdwCkxqList(Map<String, Object> map) {
        Page<Map<String, Object>> jsdwList = null;
        try {
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            String gcbh = CommonUtil.formatEmptyValue(data.get("gcbh"));
            String gcmc = CommonUtil.formatEmptyValue(data.get("gcmc"));
            String xmzt = CommonUtil.formatEmptyValue(data.get("xmzt"));
            String userid = UserUtil.getCurrentUserId();

            int page = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("page")));
            int pageSize = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("pageSize")));
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("gcbh", gcbh);
            paramMap.put("gcmc", gcmc);
            paramMap.put("xmzt", xmzt);

            if (StringUtils.isNotBlank(userid)) {
                Example example = new Example(DchyXmglYhdw.class);
                example.createCriteria().andEqualTo("yhid", userid);
                List<DchyXmglYhdw> dchyXmglYhdwList = entityMapper.selectByExampleNotNull(example);
                if (CollectionUtils.isNotEmpty(dchyXmglYhdwList)) {
                    String dwbh = dchyXmglYhdwList.get(0).getDwbh();
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
                }
            }

            jsdwList = repository.selectPaging("queryChgcxxByXmmcOrZtByPage", paramMap, page - 1, pageSize);
            List chxmidList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(jsdwList.getContent())) {
                List<Map<String, Object>> jsdwLists = jsdwList.getContent();
                for (Map<String, Object> jsdwListss : jsdwLists) {
                    String chxmid = MapUtils.getString(jsdwListss, "CHXMID");
                    chxmidList.add(chxmid);
                }
                Map<String, Object> paramMaps = Maps.newHashMap();
                paramMaps.put("chxmidList", chxmidList);
                List<Map<String, Object>> clsxList = dchyXmglChgcMapper.getClsxByChxmid(paramMaps);
                if (CollectionUtils.isNotEmpty(clsxList)) {
                    Map<String, List<Map<String, Object>>> divideClsxlist = CommonUtil.divideListToMap("CHXMID", clsxList);
                    for (Map<String, Object> mapTemp : jsdwList.getContent()) {
                        String chxmid = CommonUtil.ternaryOperator(mapTemp.get("CHXMID"));
                        List<String> clsxmcList = Lists.newArrayList();
                        if (divideClsxlist.containsKey(chxmid)) {
                            List<Map<String, Object>> clsxListTemp = divideClsxlist.get(chxmid);
                            for (Map<String, Object> clsxMap : clsxListTemp) {
                                String clsxdm = MapUtils.getString(clsxMap, "CLSX");
                                // ???????????????
                                // dm+zdlx ->mc
                                DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("CLSX", clsxdm);
                                if (dchyXmglZd != null) {
                                    clsxmcList.add(dchyXmglZd.getMc());
                                } else {
                                    clsxmcList.add(clsxdm);
                                }
                            }
                        }
                        mapTemp.put("CLSX", StringUtils.join(clsxmcList, ","));
                        if (StringUtils.isNotEmpty(CommonUtil.ternaryOperator(mapTemp.get("XMZT")))) {
                            String xmztdm = CommonUtil.ternaryOperator(mapTemp.get("XMZT"));
                            DchyXmglZd zdXmzt = dchyXmglZdService.getDchyXmglByZdlxAndDm("XMZT", xmztdm);
                            if (zdXmzt != null) {
                                mapTemp.put("XMZT", zdXmzt.getMc());
                            }
                        }
                    }
                }
            }
            DataSecurityUtil.decryptMapList(jsdwList.getContent());
        } catch (Exception e) {
            logger.error("????????????{}???", e);
        }

        return jsdwList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean saveChgcxx(Map<String, Object> map) {
        try {
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            String chgcbh = CommonUtil.formatEmptyValue(data.get("gcbh"));
            String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));
            Example chgcExample = new Example(DchyXmglChgc.class);
            chgcExample.createCriteria().andEqualTo("gcbh", chgcbh);
            List<DchyXmglChgc> dchyXmglChgcList = entityMapper.selectByExampleNotNull(chgcExample);

            DchyXmglChgc dchyXmglChgc;
            if (CollectionUtils.isNotEmpty(dchyXmglChgcList)) {
                dchyXmglChgc = dchyXmglChgcList.get(0);
            } else {
                dchyXmglChgc = new DchyXmglChgc();
                dchyXmglChgc.setChgcid(UUIDGenerator.generate18());
            }

            //????????????
            dchyXmglChgc.setGcbh(CommonUtil.formatEmptyValue(data.get("gcbh")));
            dchyXmglChgc.setGcmc(CommonUtil.formatEmptyValue(data.get("gcmc")));
            dchyXmglChgc.setLxr(CommonUtil.formatEmptyValue(data.get("lxr")));
            dchyXmglChgc.setLxdh(CommonUtil.formatEmptyValue(data.get("lxdh")));
            dchyXmglChgc.setGcdzs(CommonUtil.formatEmptyValue(data.get("gcdzs")));
            dchyXmglChgc.setGcdzss(CommonUtil.formatEmptyValue(data.get("gcdzss")));
            dchyXmglChgc.setGcdzqx(CommonUtil.formatEmptyValue(data.get("gcdzqx")));
            dchyXmglChgc.setGcdzxx(CommonUtil.formatEmptyValue(data.get("gcdzxx")));
            dchyXmglChgc.setCjr(UserUtil.getCurrentUserId());
            dchyXmglChgc.setXmly(Constants.XMLY_XSFB);
            //?????????????????????yhdw??????????????????????????????wtdw???
            String yhid = UserUtil.getCurrentUserId();
            Example example = new Example(DchyXmglYhdw.class);
            example.createCriteria().andEqualTo("yhid", yhid);
            List<DchyXmglYhdw> dchyXmglYhdwList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglYhdwList) && StringUtils.isNotBlank(dchyXmglYhdwList.get(0).getDwmc())) {
                String wtdw = dchyXmglYhdwList.get(0).getDwmc();
                dchyXmglChgc.setWtdw(wtdw);
            }
            DataSecurityUtil.encryptSingleObject(dchyXmglChgc);
            entityMapper.saveOrUpdate(dchyXmglChgc, dchyXmglChgc.getChgcid());

            //????????????
            DchyXmglChxm dchyXmglChxm = new DchyXmglChxm();
            dchyXmglChxm.setChxmid(chxmid);
            dchyXmglChxm.setChgcid(dchyXmglChgc.getChgcid());
            dchyXmglChxm.setChgcbh(CommonUtil.formatEmptyValue(data.get("gcbh")));
            dchyXmglChxm.setFbsj(new Date());
            dchyXmglChxm.setFbr(UserUtil.getCurrentUserId());
            dchyXmglChxm.setXmly(Constants.XMLY_XSFB);
            dchyXmglChxm.setCgfs(CommonUtil.formatEmptyValue(data.get("cgfs")));
            dchyXmglChxm.setClrzgyq(CommonUtil.formatEmptyValue(data.get("zgyq")));
            dchyXmglChxm.setXmzt("1");//???????????????
            //????????????
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date cgjfrq = simpleDateFormat.parse(CommonUtil.formatEmptyValue(data.get("cgjfrq")));
                dchyXmglChxm.setCgjfrq(cgjfrq);
            } catch (Exception e) {
                logger.error("????????????{}???", e);
            }
            dchyXmglChxm.setXqfbbh(CommonUtil.formatEmptyValue(data.get("xqfbbh")));
            entityMapper.saveOrUpdate(dchyXmglChxm, dchyXmglChxm.getChxmid());

            //????????????
            Example clsxExample = new Example(DchyXmglChxmClsx.class);
            clsxExample.createCriteria().andEqualTo("chxmid", chxmid);
            entityMapper.deleteByExampleNotNull(clsxExample);
            String clsx = CommonUtil.formatEmptyValue(data.get("clsx"));
            if (StringUtils.isNotEmpty(clsx)) {
                List<String> clsxList = Arrays.asList(clsx.split(";"));//???????????????????????????list
                DchyXmglChxmClsx dchyXmglChxmClsx = new DchyXmglChxmClsx();
                for (Iterator<String> it = clsxList.iterator(); it.hasNext(); ) {
                    String clsxs = it.next();
                    String clsxid = UUIDGenerator.generate18();
                    dchyXmglChxmClsx.setClsxid(clsxid);
                    dchyXmglChxmClsx.setClsx(clsxs);
                    dchyXmglChxmClsx.setChxmid(chxmid);
                    entityMapper.insertSelective(dchyXmglChxmClsx);
                }
            }
            pushDataToMqService.pushJsdwFbfwMsgToMqSave(chxmid);
        } catch (Exception e) {
            logger.error("????????????{}???", e);
            return false;
        }
        return true;
    }

    @Override
    public Page<Map<String, Object>> getJsdwCkxmList(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String gcmc = CommonUtil.formatEmptyValue(data.get("gcmc"));
        String xmzt = CommonUtil.formatEmptyValue(data.get("xmzt"));
        String gcxmbh = CommonUtil.formatEmptyValue(data.get("gcxmbh"));
        String babh = CommonUtil.formatEmptyValue(data.get("babh"));
        String bakssj = CommonUtil.formatEmptyValue(data.get("bakssj"));
        String bajssj = CommonUtil.formatEmptyValue(data.get("bajssj"));
        String xmly = CommonUtil.formatEmptyValue(data.get("xmly"));
        String ssjd = CommonUtil.formatEmptyValue(data.get("ssjd"));
        int page = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("page")));
        int pageSize = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("pageSize")));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("gcmc", gcmc);
        paramMap.put("xmzt", xmzt);
        paramMap.put("babh",babh);
        paramMap.put("gcxmbh",gcxmbh);
        paramMap.put("bakssj",bakssj);
        paramMap.put("bajssj",bajssj);
        paramMap.put("ssjd",ssjd);
        if(StringUtils.isNotEmpty(xmly)){
            List<String> xmlyList= Lists.newArrayList();
            if(xmly.contains(",")){
                xmlyList = Lists.newArrayList(xmly.split(","));
            }else{
                xmlyList.add(xmly);
            }
            paramMap.put("xmlylist",xmlyList);
        }
        String fbr = UserUtil.getCurrentUserId();
        Example example = new Example(DchyXmglYhdw.class);
        example.createCriteria().andEqualTo("yhid", fbr);
        List<DchyXmglYhdw> dchyXmglYhdwList = entityMapper.selectByExampleNotNull(example);
        if (CollectionUtils.isNotEmpty(dchyXmglYhdwList)) {

            for (DchyXmglYhdw dchyXmglYhdwLists : dchyXmglYhdwList) {
                if (StringUtils.isNotBlank(dchyXmglYhdwLists.getDwmc())) {
                    String wtdw = dchyXmglYhdwLists.getDwmc();
                    paramMap.put("wtdw", wtdw);
                }

                if (StringUtils.isNotBlank(dchyXmglYhdwLists.getDwbh())) {
                    String dwbh = dchyXmglYhdwLists.getDwbh();
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
                }

                Page<Map<String, Object>> chxmList = repositoryXSBF.selectPaging("queryJsdwCkxmByPage", paramMap, page - 1, pageSize);
                DataSecurityUtil.decryptMapList(chxmList.getContent());
                if(CollectionUtils.isNotEmpty(chxmList.getContent())){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (Map<String, Object> mapTemp : chxmList.getContent()) {
                        String chxmid = MapUtils.getString(mapTemp,"CHXMID");
                        if(StringUtils.isNotBlank(chxmid)){
                            DchyXmglChxm xmglChxmXsbf = entityMapperXSBF.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
                            Optional<DchyXmglChxm> chxmOptional = Optional.ofNullable(xmglChxmXsbf);
                            /*??????????????????*/
                            List<Map<String, Object>> list = onlineDelegationMapper.getLatelyCgTjjlByChxmid(chxmid);
                            if(chxmOptional.isPresent()){
                                /*???????????????*/
                                if(StringUtils.isNotBlank(xmglChxmXsbf.getXmzt()) && StringUtils.equals(xmglChxmXsbf.getXmzt(),"99")){
                                    Date bjsj = xmglChxmXsbf.getBjsj();//????????????
                                    mapTemp.put("DQJD","????????????");//????????????  -> ????????????
                                    mapTemp.put("JDSJ",sdf.format(bjsj));//????????????
                                }
                                else if(CollectionUtils.isNotEmpty(list)){/*??????????????????*/
                                    Map<String, Object> cgMap = list.get(list.size() - 1);
                                    if(MapUtils.isNotEmpty(cgMap)){
                                        Date tjsj = (Date) cgMap.get("TJSJ");
                                        if(null != tjsj){
                                            mapTemp.put("DQJD","????????????");//????????????  --> ????????????
                                            mapTemp.put("JDSJ",sdf.format(tjsj));//????????????
                                        }
                                    }
                                }
                                else if(StringUtils.isNotBlank(xmglChxmXsbf.getXmzt()) && ! StringUtils.equals(xmglChxmXsbf.getXmzt(),"1")){
                                    Date slsj = xmglChxmXsbf.getSlsj();//????????????
                                    Date basj = xmglChxmXsbf.getBasj();
                                    if(null != basj){
                                        mapTemp.put("DQJD","????????????");//????????????  -> ????????????
                                        mapTemp.put("JDSJ",sdf.format(basj));//????????????
                                    }
                                    else if(null != slsj){
                                        mapTemp.put("DQJD","????????????");//????????????  -> ????????????
                                        mapTemp.put("JDSJ",sdf.format(slsj));//????????????
                                    }
                                }
                            }
                        }
                        if (StringUtils.isNotEmpty(CommonUtil.ternaryOperator(mapTemp.get("XMZT")))) {
                            String xmztdm = CommonUtil.ternaryOperator(mapTemp.get("XMZT"));
                            DchyXmglZd zdXmzt = dchyXmglZdService.getDchyXmglByZdlxAndDm("XMZT", xmztdm);
                            if (zdXmzt != null) {
                                mapTemp.put("XMZT", zdXmzt.getMc());
                            }
                        }
                        if (StringUtils.isNotEmpty(chxmid)) {
                            //??????????????????????????????
                            Calendar ca = Calendar.getInstance();
                            ca.add(Calendar.DATE, -1);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String yjjfrq = dateFormat.format(ca.getTime());
                            //???????????????????????????????????????
                            Map<String, Object> paramMaps = Maps.newHashMap();
                            paramMaps.put("yjjfrq", yjjfrq);
                            paramMaps.put("chxmid", chxmid);
                            List<Map<String, Object>> chxmclsxList = xsbfDchyXmglChgcMapper.queryClsxByChqx(paramMaps);
                            if (CollectionUtils.isNotEmpty(chxmclsxList)) {
                                mapTemp.put("SFCQ", "1");
                            } else {
                                mapTemp.put("SFCQ", "0");
                            }
                        }
                    }
                }
                return chxmList;
            }
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> queryChxmxxByChxmid(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("chxmid", chxmid);
        List<Map<String, Object>> chxxList = dchyXmglChgcMapper.queryChxmxxByChxmid(paramMap);
        DataSecurityUtil.decryptMapList(chxxList);
        return chxxList;
    }

    @Override
    public List<Map<String, Object>> queryClsxByChxmid(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));
        Example exampleMap = new Example(DchyXmglChxmClsx.class);
        exampleMap.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglChxmClsx> resultList = entityMapper.selectByExample(exampleMap);
        List clsxList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(resultList)) {
            for (int i = 0; i < resultList.size(); i++) {
                String clsx = resultList.get(i).getClsx();
                clsxList.add(clsx);
            }
        }
        return clsxList;
    }

    @Override
    public DchyXmglChgc queryGcmcByGcbh(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String gcbh = CommonUtil.formatEmptyValue(data.get("gcbh"));
        Example exampleMap = new Example(DchyXmglChgc.class);
        exampleMap.createCriteria().andEqualTo("gcbh", gcbh);
        List<DchyXmglChgc> gcmcList = entityMapper.selectByExample(exampleMap);
        DchyXmglChgc dchyXmglChgc = null;
        if (CollectionUtils.isNotEmpty(gcmcList)) {
            dchyXmglChgc = gcmcList.get(0);
        }
        DataSecurityUtil.decryptSingleObject(dchyXmglChgc);
        return dchyXmglChgc;
    }

    @Override
    public Map<String, Object> initJsdwFbxq() {
        DchyXmglChxm dchyXmglChxm = new DchyXmglChxm();
        dchyXmglChxm.setChxmid(UUIDGenerator.generate());
        dchyXmglChxm.setXmzt("0");
        entityMapper.insertSelective(dchyXmglChxm);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        String xqfbbhLsh = obtainXqfbbh();
        //???????????????????????????????????????????????????,???????????????0????????????
        if (StringUtils.equals("000", xqfbbhLsh)) {
            xqfbbhLsh = obtainXqfbbh();
        }
        String xqfbbh = ("XQFB" + sdf2.format(new Date()) + xqfbbhLsh).replaceAll(" ", "");

        Map<String, Object> result = Maps.newHashMap();
        result.put("chxmid", dchyXmglChxm.getChxmid());
        result.put("xqfbbh", xqfbbh);

        return result;
    }

    @Override
    @Transactional
    public synchronized Map<String, Object> deleteJsdwFbxqByChxmid(Map<String, Object> paramMap) {
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        String code = ResponseMessage.CODE.DELETE_FAIL.getCode();
        String msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
        if (null != paramMap && paramMap.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(paramMap, "data");
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

                            //  ??????filecenter
                            // ??????????????????
                            entityMapper.deleteByPrimaryKey(DchyXmglSjxx.class, dchyXmglSjxx.getSjxxid());
                        }
                    }
                    /*fileCenter????????????*/
                    int node = platformUtil.creatNode(chxmid);
                    platformUtil.deleteNodeById(node);
                    code = ResponseMessage.CODE.SUCCESS.getCode();
                    msg = ResponseMessage.CODE.SUCCESS.getMsg();
                    pushDataToMqService.pushJsdwFbfwMsgToMqDel(dchyXmglChxmDto);
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
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        return resultMap;
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
        return SsmkidEnum.FBXQWJ.getCode();
    }

    /**
     * @param
     * @return
     * @description 2020/12/10 ????????????????????????  ????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public List<String> queryHtxxByChxmid(Map<String, Object> paramMap) {
        List<String> wjzxidList = new ArrayList<>();
        String chxmid = MapUtils.getString(paramMap, "chxmid");
        String chdwxxid = MapUtils.getString(paramMap, "chdwxxid");
        if (StringUtils.isNotBlank(chxmid) && StringUtils.isNotBlank(chdwxxid)) {
            List<Map<String, Object>> dchyXmglHtxxList = xsbfDchyXmglHtxxMapper.queryHtxxByChxmidAndChdwxxid(paramMap);
            if (CollectionUtils.isNotEmpty(dchyXmglHtxxList)) {
                for (Map<String, Object> dchyXmglHtxx : dchyXmglHtxxList) {
                    wjzxidList.add(MapUtils.getString(dchyXmglHtxx, "WJZXID"));
                }
            }
        }
        return wjzxidList;
    }

    @Override
    public List<Map<String, Object>> queryWtxxByChdwxxid(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String chdwxxid = CommonUtil.formatEmptyValue(data.get("chdwxxid"));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("chdwxxid", chdwxxid);
        List<Map<String, Object>> chxxList = xsbfDchyXmglChgcMapper.queryWtxxByChdwxxids(paramMap);
        for (Map<String, Object> chxxLists : chxxList) {
            if (StringUtils.isNotBlank(MapUtils.getString(chxxLists, "GCDZS"))) {
                String gcdzs = MapUtils.getString(chxxLists, "GCDZS");
                DchyXmglZd gcdzsZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("GCDZ", gcdzs);
                chxxLists.put("GCDZS", gcdzsZd.getMc());
            }

            if (StringUtils.isNotBlank(MapUtils.getString(chxxLists, "GCDZSS"))) {
                String gcdzss = MapUtils.getString(chxxLists, "GCDZSS");
                DchyXmglZd gcdzssZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("GCDZ", gcdzss);
                chxxLists.put("GCDZSS", gcdzssZd.getMc());
            }

            if (StringUtils.isNotBlank(MapUtils.getString(chxxLists, "GCDZQX"))) {
                String gcdzqx = MapUtils.getString(chxxLists, "GCDZQX");
                DchyXmglZd gcdzqxZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("GCDZ", gcdzqx);
                chxxLists.put("GCDZQX", gcdzqxZd.getMc());
            }
        }

        List chdwxxidList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(chxxList)) {
            for (Map<String, Object> chxmLists : chxxList) {
                String chdwxxids = MapUtils.getString(chxmLists, "CHDWXXID");
                chdwxxidList.add(chdwxxids);
            }
        }
        Map<String, Object> paramMaps = Maps.newHashMap();
        paramMaps.put("chdwxxidList", chdwxxidList);
        List<Map<String, Object>> clsxList = dchyXmglChgcMapper.getClsxByChdwxxid(paramMaps);
        if (CollectionUtils.isNotEmpty(clsxList)) {
            Map<String, List<Map<String, Object>>> divideClsxlist = CommonUtil.divideListToMap("CHDWXXID", clsxList);
            for (Map<String, Object> mapTemp : chxxList) {
                String chdwxxids = CommonUtil.ternaryOperator(mapTemp.get("CHDWXXID"));
                List<String> clsxmcList = Lists.newArrayList();
                if (divideClsxlist.containsKey(chdwxxids)) {
                    List<Map<String, Object>> clsxListTemp = divideClsxlist.get(chdwxxids);
                    for (Map<String, Object> clsxMap : clsxListTemp) {
                        String clsxdm = MapUtils.getString(clsxMap, "CLSX");
                        // ???????????????
                        // dm+zdlx ->mc
                        DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("CLSX", clsxdm);
                        if (dchyXmglZd != null) {
                            clsxmcList.add(dchyXmglZd.getMc());
                        } else {
                            clsxmcList.add(clsxdm);
                        }
                    }
                }
                mapTemp.put("CLSX", StringUtils.join(clsxmcList, ","));

                //????????????
                String chxmid = CommonUtil.ternaryOperator(mapTemp.get("CHXMID"));
                if(StringUtils.isNotEmpty(chxmid)){
                    Map<String,Object> chtlMap = Maps.newHashMap();
                    chtlMap.put("chxmid",chxmid);
                    List<Map<String, Object>> chtlList = dchyXmglChgcMapper.queryClsxChtlByChxmid(chtlMap);
                    if(CollectionUtils.isNotEmpty(chtlList)){
                        for(Map<String, Object> chtl:chtlList){
                            String dw  = MapUtils.getString(chtl,"DW");
                            if(StringUtils.isNotEmpty(dw) && StringUtils.equals(dw,Constants.CLSX_CHTL_DM_M)){
                                dw=Constants.CLSX_CHTL_MC_M;
                            }else if(StringUtils.isNotEmpty(dw) && StringUtils.equals(dw,Constants.CLSX_CHTL_DM_PFM)){
                                dw=Constants.CLSX_CHTL_MC_PFM;
                            }else if(StringUtils.isNotEmpty(dw) && StringUtils.equals(dw,Constants.CLSX_CHTL_DM_Z)){
                                dw=Constants.CLSX_CHTL_MC_Z;
                            }
                            chtl.put("DW",dw);
                        }
                    }
                    mapTemp.put("CHTLLIST", chtlList);
                }
            }
        }

        return chxxList;
    }

    @Override
    public List<Map<String, Object>> queryChdwxxByChdwxxid(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String chdwxxid = CommonUtil.formatEmptyValue(data.get("chdwxxid"));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("chdwxxid", chdwxxid);
        //???????????????????????????????????????
        DchyXmglChxmChdwxx dchyXmglChxmChdwxx = entityMapperXSBF.selectByPrimaryKey(DchyXmglChxmChdwxx.class, chdwxxid);

        List<Map<String, Object>> chxxList = null;
        if (dchyXmglChxmChdwxx != null && StringUtils.isNotEmpty(dchyXmglChxmChdwxx.getChdwlx())) {
            String chdwlx = dchyXmglChxmChdwxx.getChdwlx();
            //1????????? 2???????????????
            if (StringUtils.equals(chdwlx, "1")) {
                chxxList = dchyXmglChdwMapper.queryChxxByChdwxxids(paramMap);
            } else if (StringUtils.equals(chdwlx, "2")) {
                chxxList = xsbfDchyXmglChgcMapper.queryChdwxxByChdwxxids(paramMap);
            }
        }
        return chxxList;
    }

    @Override
    public List<Map<String, Object>> queryQtblxxByChxmid(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("chxmid", chxmid);
        List<Map<String, Object>> resultList = xsbfDchyXmglChgcMapper.queryQtblxxByChxmid(paramMap);
        if (CollectionUtils.isNotEmpty(resultList)) {
            for (Map<String, Object> mapTemp : resultList) {
                String xmzt = MapUtils.getString(mapTemp, "CHXMID");
                DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("XMZT", xmzt);
                if (dchyXmglZd != null) {
                    mapTemp.put("XMZT", dchyXmglZd.getMc());
                }
            }
        }
        return resultList;
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

}
