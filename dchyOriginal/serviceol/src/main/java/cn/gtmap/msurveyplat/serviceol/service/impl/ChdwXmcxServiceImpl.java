package cn.gtmap.msurveyplat.serviceol.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglChdwMapper;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglChgcMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.core.xsbfmapper.XsbfDchyXmglChgcMapper;
import cn.gtmap.msurveyplat.serviceol.model.PfUser;
import cn.gtmap.msurveyplat.serviceol.service.ChdwXmcxService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-11-28
 * description
 */

@Service
public class ChdwXmcxServiceImpl implements ChdwXmcxService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChdwXmcxServiceImpl.class);

    @Autowired
    private EntityMapper entityMapper;

    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapperXSBF;

    @Autowired
    private Repository repository;

    @Resource(name = "repositoryXSBF")
    private Repository repositoryXSBF;

    @Autowired
    private DchyXmglChgcMapper dchyXmglChgcMapper;

    @Autowired
    private XsbfDchyXmglChgcMapper xsbfDchyXmglChgcMapper;

    @Autowired
    private DchyXmglChdwMapper dchyXmglChdwMapper;

    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    @Override
    public Page<Map<String, Object>> getChdwXmcxList(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String gcmc = CommonUtil.formatEmptyValue(MapUtils.getString(data, "gcmc"));
        String xmzt = CommonUtil.formatEmptyValue(MapUtils.getString(data, "xmzt"));
        int page = Integer.parseInt(CommonUtil.formatEmptyValue(MapUtils.getString(data, "page")));
        int pageSize = Integer.parseInt(CommonUtil.formatEmptyValue(MapUtils.getString(data, "pageSize")));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("gcmc", gcmc);
        paramMap.put("xmzt", xmzt);
        Page<Map<String, Object>> chxmList = repository.selectPaging("queryChgcxxByXmmcOrZtByPage", paramMap, page - 1, pageSize);
        List chxmidList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(chxmList.getContent())) {
            List<Map<String, Object>> jsdwLists = chxmList.getContent();
            for (Map<String, Object> jsdwListss : jsdwLists) {
                String chxmid = MapUtils.getString(jsdwListss, "CHXMID");
                chxmidList.add(chxmid);
            }
            Map<String, Object> paramMaps = Maps.newHashMap();
            paramMaps.put("chxmidList", chxmidList);
            List<Map<String, Object>> clsxList = dchyXmglChgcMapper.getClsxByChxmid(paramMaps);
            if (CollectionUtils.isNotEmpty(clsxList)) {
                Map<String, List<Map<String, Object>>> divideClsxlist = CommonUtil.divideListToMap("CHXMID", clsxList);
                for (Map<String, Object> mapTemp : chxmList.getContent()) {
                    String chxmid = CommonUtil.ternaryOperator(mapTemp.get("CHXMID"));
                    List<String> clsxmcList = Lists.newArrayList();
                    if (divideClsxlist.containsKey(chxmid)) {
                        List<Map<String, Object>> clsxListTemp = divideClsxlist.get(chxmid);
                        for (Map<String, Object> clsxMap : clsxListTemp) {
                            String clsxdm = MapUtils.getString(clsxMap, "CLSX");
                            // 字典表处理
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
        return chxmList;
    }

    @Override
    public List<Map<String, Object>> getXmcxxqList(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("chxmid", chxmid);
        return dchyXmglChgcMapper.queryChxmxxByChxmid(paramMap);
    }

    @Override
    public Page<Map<String, Object>> getChdwCkxmList(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String gcmc = CommonUtil.formatEmptyValue(data.get("gcmc"));
        String xmzt = CommonUtil.formatEmptyValue(data.get("xmzt"));
        String jsdwmc = CommonUtil.formatEmptyValue(data.get("jsdwmc"));
        int page = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("page")));
        int pageSize = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("pageSize")));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("jsdwmc", jsdwmc);
        paramMap.put("gcmc", gcmc);
        paramMap.put("xmzt", xmzt);

        String userid = UserUtil.getCurrentUserId();
        Page<Map<String, Object>> chxmList = null;
        if (StringUtils.isNotBlank(userid)) {
            Example yhidExample = new Example(DchyXmglYhdw.class);
            yhidExample.createCriteria().andEqualTo("yhid", userid);
            List<DchyXmglYhdw> dchyXmglYhdw = entityMapper.selectByExampleNotNull(yhidExample);
            if (CollectionUtils.isNotEmpty(dchyXmglYhdw)) {
                for (DchyXmglYhdw dchyXmglYhdws : dchyXmglYhdw) {
                    if (StringUtils.isNotBlank(dchyXmglYhdws.getDwbh())) {
                        String dwbh = dchyXmglYhdws.getDwbh();
                        Example mlkExample = new Example(DchyXmglMlk.class);
                        mlkExample.createCriteria().andEqualTo("dwbh", dwbh);
                        List<DchyXmglMlk> dchyXmglMlk = entityMapper.selectByExample(mlkExample);
                        if (CollectionUtils.isNotEmpty(dchyXmglMlk)) {
                            List<String> mlkidList = Lists.newArrayList();
                            for (DchyXmglMlk dchyXmglMlks : dchyXmglMlk) {
                                String mlkid = dchyXmglMlks.getMlkid();
                                mlkidList.add(mlkid);
                            }
                            paramMap.put("mlkidList", mlkidList);
                            chxmList = repositoryXSBF.selectPaging("queryChdwCkxmByPage", paramMap, page - 1, pageSize);
                            List chxmidList = new ArrayList();
                            if (CollectionUtils.isNotEmpty(chxmList.getContent())) {
                                List<Map<String, Object>> chxmLists = chxmList.getContent();
                                for (Map<String, Object> chxmListss : chxmLists) {
                                    String chxmids = MapUtils.getString(chxmListss, "CHXMID");
                                    chxmidList.add(chxmids);
                                }
                            }
                            Map<String, Object> paramMaps = Maps.newHashMap();
                            paramMaps.put("chxmidList", chxmidList);
                            List<Map<String, Object>> clsxList = xsbfDchyXmglChgcMapper.getClsxByChxmid(paramMaps);
                            if (CollectionUtils.isNotEmpty(clsxList)) {
                                Map<String, List<Map<String, Object>>> divideClsxlist = CommonUtil.divideListToMap("CHXMID", clsxList);
                                for (Map<String, Object> mapTemp : chxmList.getContent()) {
                                    String chxmids = CommonUtil.ternaryOperator(mapTemp.get("CHXMID"));
                                    List<String> clsxmcList = Lists.newArrayList();
                                    if (divideClsxlist.containsKey(chxmids)) {
                                        List<Map<String, Object>> clsxListTemp = divideClsxlist.get(chxmids);
                                        for (Map<String, Object> clsxMap : clsxListTemp) {
                                            String clsxdm = MapUtils.getString(clsxMap, "CLSX");
                                            // 字典表处理
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
                    }
                }
            }
        }
        return chxmList;
    }

    @Override
    public String queryChxmCount() {
        String userid = UserUtil.getCurrentUserId();
        Example yhidExample = new Example(DchyXmglYhdw.class);
        yhidExample.createCriteria().andEqualTo("yhid", userid);
        List<DchyXmglYhdw> dchyXmglYhdw = entityMapper.selectByExampleNotNull(yhidExample);
        String chdwmc = null;
        String chxmsl = null;
        if (CollectionUtils.isNotEmpty(dchyXmglYhdw)) {
            chdwmc = dchyXmglYhdw.get(0).getDwmc();
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("chdwmc", chdwmc);
            chxmsl = xsbfDchyXmglChgcMapper.queryChxmCount(paramMap);
        }
        return chxmsl;
    }

    @Override
    public List<Map<String, Object>> queryWtxxByChdwxxid(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String chdwxxid = CommonUtil.formatEmptyValue(data.get("chdwxxid"));
        String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("chdwxxid", chdwxxid);
        paramMap.put("chxmid", chxmid);
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
            //测绘体量
            if(StringUtils.isNotEmpty(chxmid)){
                List<Map<String, Object>> chtlList = xsbfDchyXmglChgcMapper.queryClsxChtlByChxmid(paramMap);
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
                chxxLists.put("CHTLLIST", chtlList);
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
        List<Map<String, Object>> clsxList = xsbfDchyXmglChgcMapper.getClsxByChdwxxid(paramMaps);
        if (CollectionUtils.isNotEmpty(clsxList)) {
            Map<String, List<Map<String, Object>>> divideClsxlist = CommonUtil.divideListToMap("CHDWXXID", clsxList);
            for (Map<String, Object> mapTemp : chxxList) {
                String chdwxxids = CommonUtil.ternaryOperator(mapTemp.get("CHDWXXID"));
                List<String> clsxmcList = Lists.newArrayList();
                if (divideClsxlist.containsKey(chdwxxids)) {
                    List<Map<String, Object>> clsxListTemp = divideClsxlist.get(chdwxxids);
                    for (Map<String, Object> clsxMap : clsxListTemp) {
                        String clsxdm = MapUtils.getString(clsxMap, "CLSX");

                        clsxmcList.add(clsxdm);
                    }
                }
                mapTemp.put("CLSX", StringUtils.join(clsxmcList, ","));
            }
        } else {
            List chxmidList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(chxxList)) {
                for (Map<String, Object> chxmLists : chxxList) {
                    String chxmids = MapUtils.getString(chxmLists, "CHXMID");
                    chxmidList.add(chxmids);
                }
            }
            Map<String, Object> paramMapss = Maps.newHashMap();
            paramMapss.put("chxmidList", chxmidList);
            List<Map<String, Object>> clsxLists = xsbfDchyXmglChgcMapper.getClsxByChxmid(paramMapss);

            if (CollectionUtils.isNotEmpty(clsxLists)) {
                Map<String, List<Map<String, Object>>> divideClsxlist = CommonUtil.divideListToMap("CHXMID", clsxLists);
                for (Map<String, Object> mapTemp : chxxList) {
                    String chxmids = CommonUtil.ternaryOperator(mapTemp.get("CHXMID"));
                    List<String> clsxmcList = Lists.newArrayList();
                    if (divideClsxlist.containsKey(chxmids)) {
                        List<Map<String, Object>> clsxListTemp = divideClsxlist.get(chxmids);
                        for (Map<String, Object> clsxMap : clsxListTemp) {
                            String clsxdm = MapUtils.getString(clsxMap, "CLSX");
                            clsxmcList.add(clsxdm);
                        }
                    }
                    mapTemp.put("CLSX", StringUtils.join(clsxmcList, ","));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(chxxList)) {
            DataSecurityUtil.decryptMapList(chxxList);
        }
        //给测绘单位和建设单位加上统一社会用代码和联系人联系电话
        formatWTXMXX(chxxList);
        return chxxList;
    }

    @Override
    public List<Map<String, Object>> queryChdwxxByChdwxxid(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String chdwxxid = CommonUtil.formatEmptyValue(data.get("chdwxxid"));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("chdwxxid", chdwxxid);
        //判断是线上库还是线上备份库
        DchyXmglChxmChdwxx dchyXmglChxmChdwxx = null;
        if (StringUtils.isNotBlank(chdwxxid)) {
            dchyXmglChxmChdwxx = entityMapperXSBF.selectByPrimaryKey(DchyXmglChxmChdwxx.class, chdwxxid);
        }
        List<Map<String, Object>> chxxList = null;
        if (dchyXmglChxmChdwxx != null && StringUtils.isNotEmpty(dchyXmglChxmChdwxx.getChdwlx())) {
            String chdwlx = dchyXmglChxmChdwxx.getChdwlx();
            //1线上库 2线上备份库
            if (StringUtils.equals(chdwlx, "1")) {
                paramMap.put("mlkid", dchyXmglChxmChdwxx.getMlkid());
                chxxList = dchyXmglChdwMapper.queryChxxByChdwxxids(paramMap);
                if (CollectionUtils.isNotEmpty(chxxList)) {
                    String cyrs = dchyXmglChdwMapper.queryCyrsByChdwxxids(paramMap);
                    if (StringUtils.isNotBlank(cyrs)) {
                        for (Map<String, Object> chxxLists : chxxList) {
                            chxxLists.put("CYRS", cyrs);
                        }
                    }
                }
            } else if (StringUtils.equals(chdwlx, "2")) {
                paramMap.put("mlkid", dchyXmglChxmChdwxx.getMlkid());
                chxxList = xsbfDchyXmglChgcMapper.queryChdwxxByChdwxxids(paramMap);
                if (CollectionUtils.isNotEmpty(chxxList)) {
                    for (Map<String, Object> chxxLists : chxxList) {
                        chxxLists.put("CYRS", 0);
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(chxxList)) {
            DataSecurityUtil.decryptMapList(chxxList);
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
                String xmzt = MapUtils.getString(mapTemp, "XMZT");
                DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("XMZT", xmzt);
                if (dchyXmglZd != null) {
                    mapTemp.put("XMZT", dchyXmglZd.getMc());
                    if (StringUtils.equals(dchyXmglZd.getMc(), "未受理")) {
                        mapTemp.put("SLZT", "未受理");
                    } else {
                        mapTemp.put("SLZT", "已受理");
                    }

                }
            }
        }
        return resultList;
    }

    /**
     * @param chxmid
     * @param mlkid
     * @return
     * @description 2020/12/10 测绘单位 合同下载
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    @Transactional(readOnly = true)
    public List<String> queryHtxxByChxmidAndHtxxid(String chxmid, String mlkid) {
        List<String> wjzxidList = new ArrayList<>();
        if (StringUtils.isNotBlank(chxmid)) {
            DchyXmglChxm dchyXmglChxmNew = entityMapperXSBF.selectByPrimaryKey(DchyXmglChxm.class, chxmid);

            if (dchyXmglChxmNew != null) {
                /*线上库与线上备份库的wjzxid保持一致，直接取线上备课库拆分后的数据即可*/
                Example htxxExample = new Example(DchyXmglHtxx.class);
                htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
                List<DchyXmglHtxx> htxxList = entityMapperXSBF.selectByExample(htxxExample);
                if (CollectionUtils.isNotEmpty(htxxList)) {
                    htxxList.forEach(htxx -> {
                        wjzxidList.add(htxx.getWjzxid());
                    });
                }
            } else {
                List<Object> sjxxidList = new ArrayList<>();
                Example htxxExample = new Example(DchyXmglHtxx.class);
                htxxExample.createCriteria().andEqualTo("chxmid", chxmid).andIsNotNull("wjzxid");
                List<DchyXmglHtxx> htxxList = entityMapperXSBF.selectByExample(htxxExample);
                if (CollectionUtils.isNotEmpty(htxxList)) {
                    List<Object> htxxidList = new ArrayList<>();
                    htxxList.forEach(htxx -> {
                        htxxidList.add(htxx.getHtxxid());
                    });
                    Example sjxxExample = new Example(DchyXmglSjxx.class);
                    sjxxExample.createCriteria().andIn("glsxid", htxxidList);
                    List<DchyXmglSjxx> sjxxList = entityMapperXSBF.selectByExample(sjxxExample);
                    if (CollectionUtils.isNotEmpty(sjxxList)) {
                        sjxxList.forEach(sjxx -> {
                            sjxxidList.add(sjxx.getSjxxid());
                        });
                    }
                    Example sjclExample = new Example(DchyXmglSjcl.class);
                    sjclExample.createCriteria().andIn("sjxxid", sjxxidList);
                    List<DchyXmglSjcl> sjclList = entityMapperXSBF.selectByExample(sjclExample);
                    if (CollectionUtils.isNotEmpty(sjclList)) {
                        sjclList.forEach(sjcl -> {
                            if (StringUtils.isNotEmpty(sjcl.getWjzxid())) {
                                wjzxidList.add(sjcl.getWjzxid());
                            }
                        });
                    }
                }
            }
        }
        return wjzxidList;
    }

    @Override
    public void formatWTXMXX(List<Map<String, Object>> wtxmxxList) {
        if (CollectionUtils.isNotEmpty(wtxmxxList)) {
            for (Map<String, Object> wtxmxx : wtxmxxList) {

                String jsdwmc = CommonUtil.formatEmptyValue(MapUtils.getString(wtxmxx, "WTDW"));
                String chdwmc = CommonUtil.formatEmptyValue(MapUtils.getString(wtxmxx, "CHDWMC"));
                String zdxm = CommonUtil.formatEmptyValue(MapUtils.getString(wtxmxx, "ZDXM"));
                //库中可能存在脏数据,zdxm非1即为重大项目
                if (!StringUtils.equals(zdxm, Constants.DCHY_XMGL_CHXM_ZDXM)) {
                    wtxmxx.put("ZDXM", Constants.DCHY_XMGL_CHXM_FZDXM);
                }
                Example exampleJsdw = new Example(DchyXmglYhdw.class);
                Example exampleChdw = new Example(DchyXmglYhdw.class);
                exampleJsdw.createCriteria().andEqualTo("dwmc", jsdwmc);
                exampleChdw.createCriteria().andEqualTo("dwmc", chdwmc);
                List<DchyXmglYhdw> jsdwList = entityMapper.selectByExample(exampleJsdw);
                List<DchyXmglYhdw> chdwList = entityMapper.selectByExample(exampleChdw);
                if (CollectionUtils.isNotEmpty(jsdwList)) {
                    DchyXmglYhdw dchyXmglYhdw = jsdwList.get(0);
                    //解密联系人联系电话
                    DataSecurityUtil.decryptSingleObject(dchyXmglYhdw);
                    wtxmxx.put("TYSHXYDM", dchyXmglYhdw.getTyshxydm());
                }

                if (CollectionUtils.isNotEmpty(chdwList)) {
                    DchyXmglYhdw dchyXmglYhdw = chdwList.get(0);
                    //解密联系人联系电话
                    DataSecurityUtil.decryptSingleObject(dchyXmglYhdw);
                    String yhid = dchyXmglYhdw.getYhid();
                    PfUser pfUser = entityMapper.selectByPrimaryKey(PfUser.class, yhid);
                    if (null != pfUser) {
                        wtxmxx.put("CHJGLXR", pfUser.getUserName());
                    } else {
                        wtxmxx.put("CHJGLXR", "");
                    }
                    wtxmxx.put("CHJGTYSHXYDM", dchyXmglYhdw.getTyshxydm());
                    wtxmxx.put("CHJGLXDH", pfUser.getMobilePhone());
                }
            }
        }
    }
}
