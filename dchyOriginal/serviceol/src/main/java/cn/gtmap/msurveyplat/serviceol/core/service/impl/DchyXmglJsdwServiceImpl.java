package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglJsdwService;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.core.service.MessagePushService;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.serviceol.core.xsbfmapper.DchyXmglJsdwXSBFMapper;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/9 13:55
 * @description
 */
@Service
public class DchyXmglJsdwServiceImpl implements DchyXmglJsdwService {

    @Autowired
    private DchyXmglJsdwXSBFMapper jsdwMapper;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;
    @Resource(name = "repositoryXSBF")
    @Autowired
    Repository repositoryXSBF;
    @Autowired
    private EntityMapper entityMapper;
    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapperXSBF;
    @Autowired
    PushDataToMqService pushDataToMqService;
    @Autowired
    MessagePushService messagePushService;

    @Override
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> queryXmPjStatusByPage(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : "1");
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : "10");
        HashMap<String, Object> map = Maps.newHashMap();
        String gcmc = CommonUtil.ternaryOperator(data.get("gcmc"));
        String chdwmc = CommonUtil.ternaryOperator(data.get("chdwmc"));
        String pjzt = CommonUtil.ternaryOperator(data.get("pjzt"));
        String babh = CommonUtil.ternaryOperator(data.get("babh"));//????????????
        String dwbh = this.getDwhbByCurrentUser(UserUtil.getCurrentUserId());
        List<String> yhidList = this.getYhidsByDwbh(dwbh);
        map.put("gcmc", gcmc.trim());
        map.put("chdwmc", chdwmc.trim());
        map.put("pjzt", pjzt.trim());
        map.put("yhidList", yhidList);
        map.put("babh",babh);
        map.put("wtdw", this.getWtdwByCurrentUser(UserUtil.getCurrentUserId()));
        Page<Map<String, Object>> xmPjPage = repositoryXSBF.selectPaging("queryXmPjStatusByPage", map, page - 1, pageSize);
        List<Map<String, Object>> content = xmPjPage.getContent();
        if(CollectionUtils.isNotEmpty(content)){
            DataSecurityUtil.decryptMapList(xmPjPage.getContent());
            content.forEach(chxm -> {
                String chxmid = MapUtils.getString(chxm, "CHXMID");
                if(StringUtils.isNotBlank(chxmid)){
                    /*??????????????????*/
                    Example clsxExample = new Example(DchyXmglChxmClsx.class);
                    clsxExample.createCriteria().andEqualTo("chxmid",chxmid);
                    List<DchyXmglChxmClsx> chxmClsxList = entityMapperXSBF.selectByExample(clsxExample);
                    if(CollectionUtils.isNotEmpty(chxmClsxList)){
                        Set<String> parClsxSet = new LinkedHashSet<>();
                      chxmClsxList.forEach(clsx -> {
                          String tempClsx = clsx.getClsx();
                          if(StringUtils.isNotBlank(tempClsx)){
                              String parClsx = tempClsx.substring(0,1);
                              parClsxSet.add(parClsx);
                          }
                      });
                      Set<String> cljdSet = new LinkedHashSet<>();
                      if(CollectionUtils.isNotEmpty(parClsxSet)){
                          parClsxSet.forEach(clsx -> {
                              DchyXmglZd zdlxAndDm = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, clsx);
                              if(null != zdlxAndDm){
                                  cljdSet.add(zdlxAndDm.getMc());
                              }
                          });
                      }
                      chxm.put("CLJD",cljdSet);
                    }
                }
            });
        }
        return xmPjPage;
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getEvalInfoByProjectId(Map<String, Object> data) {
        //??????????????????
        int flagChdwxx = 0;
        String chxmId = CommonUtil.ternaryOperator(data.get("chxmid"));
        String fwpj = CommonUtil.ternaryOperator(data.get("fwpj"));
        String pjyj = CommonUtil.ternaryOperator(data.get("pjyj"));
        String chdwxxid = CommonUtil.ternaryOperator(data.get("chdwxxid"));
        List<Map<String, Object>> mapList = jsdwMapper.queryXmPjInfoByChdwId(chxmId.trim(), chdwxxid.trim());
        if (CollectionUtils.isNotEmpty(mapList) && null != mapList.get(0)) {
            Map<String, Object> map = mapList.get(0);
            String pjzt = MapUtils.getString(map, "PJZT");
            String tempFwpj = MapUtils.getString(map, "FWPJ");
            boolean flag = (StringUtils.equals(Constants.VALID, pjzt));//1:?????????
            if (flag) {
                /*???????????????????????????????????????*/
                if (StringUtils.isNotBlank(tempFwpj)) {
                    String pjfw = MapUtils.getString(map, "FWPJ");
                    if (StringUtils.isNotBlank(pjfw)) {
                        DchyXmglZd zdlxAndDm = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_FWPJ, pjfw);
                        if (null != zdlxAndDm) {
                            map.put("FWPJMC", zdlxAndDm.getMc());
                        }
                    }
                }
            } else {
                String tempChdwxxid = MapUtils.getString(map, "CHDWXXID");
                DchyXmglChxmChdwxx chdwxx = entityMapperXSBF.selectByPrimaryKey(DchyXmglChxmChdwxx.class, tempChdwxxid);
                /*?????????????????????????????????*/
                if (null != chdwxx) {
                    chdwxx.setFwpj(Double.valueOf(fwpj));
                    chdwxx.setPjyj(pjyj);
                    chdwxx.setPjsj(new Date());
                    chdwxx.setPjr(UserUtil.getCurrentUserId());
                    chdwxx.setPjzt(Constants.VALID);//????????????????????????
                    flagChdwxx = entityMapperXSBF.saveOrUpdate(chdwxx, chdwxx.getChdwxxid());
                }
                if (flagChdwxx > 0) {
                    /*????????????????????????*/
                    //this.jsdwPjMessageReminder(chxmId, chdwxx.getMlkid() );

                    //????????????
                    String yhxxpzid = Constants.DCHY_XMGL_ZD_XXNR_JSDWPJ;
                    String wtdw = "";
                    String xmmc = "";
                    DchyXmglChxm chxm = entityMapperXSBF.selectByPrimaryKey(DchyXmglChxm.class, chxmId);
                    if (null != chxm) {
                        String chgcid = chxm.getChgcid();
                        if (StringUtils.isNotBlank(chgcid)) {
                            DchyXmglChgc dchyXmglChgc = entityMapperXSBF.selectByPrimaryKey(DchyXmglChgc.class, chgcid);
                            if (null != dchyXmglChgc) {
                                wtdw = dchyXmglChgc.getWtdw();
                                xmmc = dchyXmglChgc.getGcmc();
                            }
                        }
                    }
                    Map<String, Object> paramMap = Maps.newHashMap();
                    paramMap.put("yhxxpzid", yhxxpzid);
                    paramMap.put("jsyhid", chdwxx.getMlkid());
                    paramMap.put("glsxid", chxmId);
                    paramMap.put("xmmc", xmmc);
                    paramMap.put("jsdwmc", wtdw);
                    messagePushService.updateYhxxInfo(paramMap);

                    pushDataToMqService.pushXmpjMsgToMq(chxmId);
                }
            }
        }
        return mapList;
    }


    @Override
    public Map<String, Object> checkChxmIsFinish(Map<String, Object> data) {
        String chxmId = CommonUtil.ternaryOperator(data.get("chxmid"));
        Map<String, Object> map = new HashMap<>();
        /*???????????????????????????????????????????????????????????????*/
        DchyXmglChxm chxm = entityMapperXSBF.selectByPrimaryKey(DchyXmglChxm.class, chxmId);
        if (null != chxm) {
            /*99????????????*/
            if (StringUtils.equals("99", chxm.getXmzt())) {
                map.put("msg", "success");
            } else {
                map.put("msg", "fail");
            }
        }
        return map;
    }

    /**
     * ?????????????????????mlkid
     *
     * @param userid
     * @return
     */
    private String getMlkIdByCurrentUser(String userid) {
        Example example = new Example(DchyXmglYhdw.class);
        example.createCriteria().andEqualTo("yhid", userid);
        List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(yhdwList)) {
            String dwbh = yhdwList.get(0).getDwbh();
            if (StringUtils.isNotBlank(dwbh)) {
                /*???????????????????????????????????????*/
                Example mlkExample = new Example(DchyXmglMlk.class);
                mlkExample.createCriteria().andEqualTo("dwbh", dwbh);
                List<DchyXmglMlk> mlks = entityMapper.selectByExample(mlkExample);
                if (CollectionUtils.isNotEmpty(mlks)) {
                    DchyXmglMlk mlk = mlks.get(0);
                    if (null != mlk) {
                        return mlk.getMlkid();
                    }
                }
            }
        }
        return "";
    }

    /**
     * ????????????????????????????????????
     *
     * @param userid
     * @return
     */
    private String getWtdwByCurrentUser(String userid) {
        Example example = new Example(DchyXmglYhdw.class);
        example.createCriteria().andEqualTo("yhid", userid);
        List<DchyXmglYhdw> dchyXmglYhdwList = entityMapper.selectByExampleNotNull(example);
        if (CollectionUtils.isNotEmpty(dchyXmglYhdwList)) {
            DchyXmglYhdw yhdw = dchyXmglYhdwList.get(0);
            if (null != yhdw) {
                String dwmc = yhdw.getDwmc();
                if (StringUtils.isNotBlank(dwmc)) {
                    return dwmc;
                }
            }
        }
        return "";
    }

    /**
     * ??????????????????????????????
     */
    private String getDwhbByCurrentUser(String userid) {
        Example example = new Example(DchyXmglYhdw.class);
        example.createCriteria().andEqualTo("yhid", userid);
        List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(yhdwList)) {
            String dwbh = yhdwList.get(0).getDwbh();
            if (StringUtils.isNotBlank(dwbh)) {
                return dwbh;
            }
        }
        return "";
    }

    /**
     * ??????????????????????????????id??????
     *
     * @param dwbh
     * @return
     */
    private List<String> getYhidsByDwbh(String dwbh) {
        Example yhdwExample = new Example(DchyXmglYhdw.class);
        List<String> list = new ArrayList<>();
        yhdwExample.createCriteria().andEqualTo("dwbh", dwbh);
        List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(yhdwExample);
        if (CollectionUtils.isNotEmpty(yhdwList)) {
            for (DchyXmglYhdw yhdw : yhdwList) {
                if (null != yhdw && StringUtils.isNotBlank(yhdw.getYhid())) {
                    list.add(yhdw.getYhid());
                }
            }
        }
        return list;
    }

}
