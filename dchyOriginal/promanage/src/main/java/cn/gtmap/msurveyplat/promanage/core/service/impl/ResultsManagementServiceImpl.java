package cn.gtmap.msurveyplat.promanage.core.service.impl;


import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmChdwxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcg;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcgShjl;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChgcMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglClcgMapper;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.core.service.ResultsManagementService;
import cn.gtmap.msurveyplat.promanage.service.impl.ExchangeFeignServiceImpl;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.PlatformUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.FileDownoadUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cn.gtmap.msurveyplat.promanage.utils.UserUtil.getCurrentUserIds;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijun1</a>
 * @version 1.0, 2021/01/05
 * @description
 */
@Service
public class ResultsManagementServiceImpl implements ResultsManagementService {

    @Autowired
    private Repository repository;

    @Autowired
    private DchyXmglChgcMapper dchyXmglChgcMapper;

    @Autowired
    private DchyXmglClcgMapper dchyXmglClcgMapper;

    @Autowired
    private ExchangeFeignServiceImpl dispatchOrderFsmServiceImpl;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    @Autowired
    private PlatformUtil platformUtil;

    private Logger logger = LoggerFactory.getLogger(ResultsManagementServiceImpl.class);


    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> queryChxmByGcid(String chgcid) {
        if (StringUtils.isNotBlank(chgcid)) {
            Map<String, Object> param = new HashMap<>();
            param.put("chgcid", chgcid);
            List<Map<String, Object>> mapList = dchyXmglChgcMapper.queryChxmByGcid(param);
            if (CollectionUtils.isNotEmpty(mapList)) {
                for (Map<String, Object> map : mapList) {
                    String chxmid = MapUtils.getString(map, "CHXMID");
                    if (StringUtils.isNotBlank(chxmid)) {
                        /*????????????*/
                        List<Map<String, Object>> clsxByChxmid = dchyXmglChgcMapper.getClsxByChxmid(chxmid);
                        if (CollectionUtils.isNotEmpty(clsxByChxmid)) {
                            StringJoiner st = new StringJoiner(" ");
                            StringJoiner sj = new StringJoiner(" ");
                            for (Map<String, Object> clsx : clsxByChxmid) {
                                DchyXmglZd byZdlxAndMc = dchyXmglZdService.getDchyXmglByZdlxAndDm("CLSX", (String) clsx.get("CLSX"));
                                if (null != byZdlxAndMc) {
                                    st.add(byZdlxAndMc.getMc());
                                    sj.add((String) clsx.get("CLSX"));
                                }
                                map.put("CGTJZT", clsx.get("CGTJZT") + " ");
                                DchyXmglZd byZdlxAndMc2 = dchyXmglZdService.getDchyXmglByZdlxAndDm("XMCGZT", (String) clsx.get("CGTJZT"));
                                if (null != byZdlxAndMc2) {
                                    map.put("CGTJZTMC", byZdlxAndMc2.getMc());
                                }
                            }
                            DchyXmglZd byZdlxAndMc3 = dchyXmglZdService.getDchyXmglByZdlxAndDm("XMZT", (String) map.get("XMZT"));
                            if (null != byZdlxAndMc3) {
                                map.put("XMZTMC", byZdlxAndMc3.getMc());
                            }
                            map.put("CLSXMC", st.toString());
                            map.put("CLSX", sj.toString());
                        }
                    }
                }
            }
            return mapList;
        }
        return new ArrayList<>();
    }

    /**
     * ???????????????????????????
     *
     * @param param
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> queryChgcForTree(Map<String, Object> param) {
        int page = Integer.parseInt(param.get("page") != null ? param.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(param.get("size") != null ? param.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("page", page);
        paramMap.put("pageSize", pageSize);
        /*????????????????????????????????????????????????*/
        String rksj = CommonUtil.formatEmptyValue(param.get("rksj"));//????????????
        String xmdz = CommonUtil.formatEmptyValue(param.get("xmdz"));//????????????
        String wtdw = CommonUtil.formatEmptyValue(param.get("wtdw"));//??????????????????
        String kssj = CommonUtil.formatEmptyValue(param.get("kssj"));//????????????
        String jssj = CommonUtil.formatEmptyValue(param.get("jssj"));//????????????
        String gcbh = CommonUtil.formatEmptyValue(param.get("gcbh"));//????????????
        String gcmc = CommonUtil.formatEmptyValue(param.get("gcmc"));//????????????
        String chdwmc = CommonUtil.formatEmptyValue(param.get("chdwmc"));//??????????????????
        String jsdwmc = CommonUtil.formatEmptyValue(param.get("jsdwmc"));//??????????????????
        String gcdzs = CommonUtil.formatEmptyValue(param.get("gcdzs"));//???????????????
        String gcdzss = CommonUtil.formatEmptyValue(param.get("gcdzss"));//??????????????????
        String gcdzqx = CommonUtil.formatEmptyValue(param.get("gcdzqx"));//??????????????????
        String gcdzxx = CommonUtil.formatEmptyValue(param.get("gcdzxx"));//??????????????????
        if (StringUtils.isNotBlank(rksj)) {
            paramMap.put("rksj", rksj);
        }
        if (StringUtils.isNotBlank(xmdz)) {
            paramMap.put("xmdz", xmdz);
        }
        if (StringUtils.isNotBlank(wtdw)) {
            paramMap.put("wtdw", wtdw);
        }
        if (StringUtils.isNotBlank(kssj)) {
            paramMap.put("kssj", kssj);
        }
        if (StringUtils.isNotBlank(jssj)) {
            paramMap.put("jssj", jssj);
        }
        if (StringUtils.isNotBlank(gcbh)) {
            paramMap.put("gcbh", gcbh);
        }
        if (StringUtils.isNotBlank(gcmc)) {
            paramMap.put("gcmc", gcmc);
        }
        if (StringUtils.isNotBlank(chdwmc)) {
            paramMap.put("chdwmc", chdwmc);
        }
        if (StringUtils.isNotBlank(jsdwmc)) {
            paramMap.put("jsdwmc", jsdwmc);
        }
        if (StringUtils.isNotBlank(gcdzxx)) {
            paramMap.put("gcdzxx", gcdzxx);
        }
        if (StringUtils.isNotBlank(gcdzqx)) {
            paramMap.put("gcdzqx", gcdzqx);
        }
        if (StringUtils.isNotBlank(gcdzs)) {
            paramMap.put("gcdzs", gcdzs);
        }
        if (StringUtils.isNotBlank(gcdzss)) {
            paramMap.put("gcdzss", gcdzss);
        }
        Page<Map<String, Object>> resultList = null;
        resultList = repository.selectPaging("queryChgcForTreeByPage", paramMap, page - 1, pageSize);
        Optional<Page<Map<String, Object>>> list = Optional.ofNullable(resultList);
        if (list.isPresent()) {
            if (CollectionUtils.isNotEmpty(resultList.getContent())) {
                List<Map<String, Object>> content = resultList.getContent();
                content.forEach(chgc -> {
                    DchyXmglZd dchyXmglSS = dchyXmglZdService.getDchyXmglByZdlxAndDm("GCDZ", (String) chgc.get("GCDZSS"));
                    DchyXmglZd dchyXmglQX = dchyXmglZdService.getDchyXmglByZdlxAndDm("GCDZ", (String) chgc.get("GCDZQX"));
                    DchyXmglZd dchyXmglS = dchyXmglZdService.getDchyXmglByZdlxAndDm("GCDZ", (String) chgc.get("GCDZS"));
                    if (null != dchyXmglSS) {
                        chgc.put("GCDZSSMC", dchyXmglSS.getMc());
                    }
                    if (null != dchyXmglQX) {
                        chgc.put("GCDZQXMC", dchyXmglQX.getMc());
                    }
                    if (null != dchyXmglS) {
                        chgc.put("GCDZSMC", dchyXmglS.getMc());
                    }
                });
            }
        }
        return resultList;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @Override
    public Page<Map<String, Object>> queryResultsManagement(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String gcbh = CommonUtil.formatEmptyValue(data.get("gcbh"));
        String gcmc = CommonUtil.formatEmptyValue(data.get("gcmc"));
        String chdwmc = CommonUtil.formatEmptyValue(data.get("chdwmc"));
        String xmzt = CommonUtil.formatEmptyValue(data.get("xmzt"));
        String xmcgzt = CommonUtil.formatEmptyValue(data.get("xmcgzt"));
        String babh = CommonUtil.formatEmptyValue(data.get("babh"));
        int page = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("page")));
        int pageSize = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("pageSize")));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("gcbh", gcbh);
        paramMap.put("gcmc", gcmc);
        paramMap.put("xmcgzt", xmcgzt);
        List<String> ztArr = new ArrayList<>();
        if (StringUtils.isNotBlank(xmzt)) {
            if (xmzt.indexOf(",") != -1) {//?????????
                String[] xmztArray = xmzt.split(",");
                for (String zt : xmztArray) {
                    ztArr.add(zt);
                }
                if (CollectionUtils.isNotEmpty(ztArr)) {
                    paramMap.put("dba", ztArr.get(0));//?????????
                    paramMap.put("yba", ztArr.get(1));//?????????
                }
            } else {//99????????????
                paramMap.put("xmzt", xmzt);
            }
        }

        paramMap.put("babh", babh);
        //???????????????????????????????????????
        if (StringUtils.isNotEmpty(chdwmc)) {
            Example example = new Example(DchyXmglChxmChdwxx.class);
            example.createCriteria().andLike("chdwmc", chdwmc);
            List<DchyXmglChxmChdwxx> chdwxxList = entityMapper.selectByExampleNotNull(example);
            List chxmidList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(chdwxxList)) {
                for (DchyXmglChxmChdwxx chdwxxLists : chdwxxList) {
                    String chxmid = chdwxxLists.getChxmid();
                    chxmidList.add(chxmid);
                }
                paramMap.put("chxmidList", chxmidList);
            }
        }

        Page<Map<String, Object>> resultList = null;
        try {
            resultList = repository.selectPaging("queryChgcxxByPage", paramMap, page - 1, pageSize);
            if (resultList != null) {
                for (Map<String, Object> resultLists : resultList) {
                    String chxmid = MapUtils.getString(resultLists, "CHXMID");
                    //??????????????????
                    Example chdwxxExample = new Example(DchyXmglChxmChdwxx.class);
                    chdwxxExample.createCriteria().andEqualTo("chxmid", chxmid);
                    List<DchyXmglChxmChdwxx> chdwxxList = entityMapper.selectByExampleNotNull(chdwxxExample);
                    if (CollectionUtils.isNotEmpty(chdwxxList)) {
                        List<String> chdwmcList = Lists.newArrayList();
                        for (DchyXmglChxmChdwxx chdwxxLists : chdwxxList) {
                            String chdwmcxx = chdwxxLists.getChdwmc();
                            chdwmcList.add(chdwmcxx);
                        }
                        resultLists.put("CHDWMC", chdwmcList);
                    }

                    //??????????????????
                    if (StringUtils.isNotEmpty(CommonUtil.ternaryOperator(resultLists.get("XMZT")))) {
                        String xmztdm = CommonUtil.ternaryOperator(resultLists.get("XMZT"));
                        if (StringUtils.equals(xmztdm, Constants.DCHY_XMGL_CHXM_XMZT_YBJ)) {
                            resultLists.put("XMZT", "?????????");
                        } else {
                            resultLists.put("XMZT", "?????????");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("????????????{}???", e);
        }
        return resultList;
    }

    /**
     * @param map
     * @return
     * @description 2021/6/1 ????????????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public ResponseMessage queryResultsManagementDb(Map<String, Object> map) {
        ResponseMessage message;

        int page = Integer.parseInt(map.get("page") != null ? map.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? map.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);

        HashMap mapParam = Maps.newHashMap();
        //???????????????????????????????????????
        Pageable pageable = new Pageable() {
            @Override
            public int getPageNumber() {
                return page;
            }

            @Override
            public int getPageSize() {
                return pageSize;
            }

            @Override
            public int getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };

        String userIds = getCurrentUserIds();
        if (StringUtils.isBlank(userIds)) {
            map.put("userIds", null);
        } else {
            map.put("userIds", userIds.split(","));
        }
        map.put("lcmc", Constants.DCHY_XMGL_CZCGTJ_LCMC);
        mapParam.put("pageable", pageable);
        mapParam.put("param", map);
        //???????????????????????????????????????
        Map<String, Object> result = dispatchOrderFsmServiceImpl.getTaskList(mapParam);
        List<Map<String, Object>> content = (List<Map<String, Object>>) result.get("content");

        if (CollectionUtils.isNotEmpty(content)) {
            for (Map<String, Object> m1 : content) {
                String xmid = CommonUtil.formatEmptyValue(MapUtils.getString(m1, "text9"));
                String taskid = CommonUtil.formatEmptyValue(MapUtils.getString(m1, "taskId"));
                String gzlslid = CommonUtil.formatEmptyValue(MapUtils.getString(m1, "procInsId"));
                //??????sqxxid???????????????????????????
                if (StringUtils.isNotBlank(xmid)) {
                    Example exampleClcg = new Example(DchyXmglClcg.class);
                    exampleClcg.createCriteria().andEqualTo("sqxxid", xmid);
                    List<DchyXmglClcg> dchyXmglClcgList = entityMapper.selectByExample(exampleClcg);
                    if (CollectionUtils.isNotEmpty(dchyXmglClcgList)) {
                        Map<String, Object> paramMap = Maps.newHashMap();
                        List<String> chxmidList = Lists.newArrayList();
                        for (DchyXmglClcg dchyXmglClcg : dchyXmglClcgList) {
                            chxmidList.add(dchyXmglClcg.getChxmid());
                        }
                        paramMap.put("chxmidList", chxmidList);
                        List<Map<String, Object>> sqxxList = dchyXmglChgcMapper.queryChgcLcxx(paramMap);
                        if (CollectionUtils.isNotEmpty(sqxxList)) {
                            Map<String, Object> map2 = sqxxList.get(0);
//                            String sqxxid = CommonUtil.formatEmptyValue(map2.get("SQXXID"));
//                            if (StringUtils.isNotBlank(sqxxid)) {
                            Example example = new Example(DchyXmglClcgShjl.class);
                            example.createCriteria().andEqualTo("sqxxid", xmid);
                            List<DchyXmglClcgShjl> dchyXmglClcgShjlList = entityMapper.selectByExample(example);
                            if (CollectionUtils.isNotEmpty(dchyXmglClcgShjlList)) {
                                DchyXmglClcgShjl dchyXmglClcgShjl = dchyXmglClcgShjlList.get(0);
                                map2.put("CGTJZTMC", convertShzt(dchyXmglClcgShjl.getShzt()));
                            }
//                            }
                            m1.putAll(map2);
                        }
                    }
                }
            }
        }
        message = ResponseUtil.wrapSuccessResponse();
        message.setData(result);
        return message;
    }

    /**
     * @param map
     * @return
     * @description 2021/6/1 ????????????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public ResponseMessage queryResultsManagementYb(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();

        int page = Integer.parseInt(map.get("page") != null ? map.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? map.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);

        HashMap mapParam = Maps.newHashMap();
        //???????????????????????????????????????
        Pageable pageable = new Pageable() {
            @Override
            public int getPageNumber() {
                return page;
            }

            @Override
            public int getPageSize() {
                return pageSize;
            }

            @Override
            public int getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };

        String userIds = getCurrentUserIds();
        if (StringUtils.isBlank(userIds)) {
            map.put("userIds", null);
        } else {
            map.put("userIds", userIds.split(","));
        }
        map.put("lcmc", Constants.DCHY_XMGL_CZCGTJ_LCMC);
        mapParam.put("pageable", pageable);
        mapParam.put("param", map);
        //???????????????????????????????????????
        Map<String, Object> result = dispatchOrderFsmServiceImpl.getTaskOverList(mapParam);
        List<Map<String, Object>> content = (List<Map<String, Object>>) result.get("content");

        if (CollectionUtils.isNotEmpty(content)) {
            for (Map<String, Object> m1 : content) {
                String xmid = CommonUtil.formatEmptyValue(MapUtils.getString(m1, "text9"));
                String taskid = CommonUtil.formatEmptyValue(MapUtils.getString(m1, "taskId"));
                String gzlslid = CommonUtil.formatEmptyValue(MapUtils.getString(m1, "procInsId"));
                //??????sqxxid???????????????????????????
                if (StringUtils.isNotBlank(xmid)) {
                    Example exampleClcg = new Example(DchyXmglClcg.class);
                    exampleClcg.createCriteria().andEqualTo("sqxxid", xmid);
                    List<DchyXmglClcg> dchyXmglClcgList = entityMapper.selectByExample(exampleClcg);
                    if (CollectionUtils.isNotEmpty(dchyXmglClcgList)) {
                        Map<String, Object> paramMap = Maps.newHashMap();
                        List<String> chxmidList = Lists.newArrayList();
                        for (DchyXmglClcg dchyXmglClcg : dchyXmglClcgList) {
                            chxmidList.add(dchyXmglClcg.getChxmid());
                        }
                        paramMap.put("chxmidList", chxmidList);
                        List<Map<String, Object>> sqxxList = dchyXmglChgcMapper.queryChgcLcxx(paramMap);
                        if (CollectionUtils.isNotEmpty(sqxxList)) {
                            Map<String, Object> map2 = sqxxList.get(0);
//                            String sqxxid = CommonUtil.formatEmptyValue(map2.get("SQXXID"));
//                            if (StringUtils.isNotBlank(sqxxid)) {
                            Example example = new Example(DchyXmglClcgShjl.class);
                            example.createCriteria().andEqualTo("sqxxid", xmid);
                            List<DchyXmglClcgShjl> dchyXmglClcgShjlList = entityMapper.selectByExample(example);
                            if (CollectionUtils.isNotEmpty(dchyXmglClcgShjlList)) {
                                DchyXmglClcgShjl dchyXmglClcgShjl = dchyXmglClcgShjlList.get(0);
                                map2.put("CGTJZTMC", convertShzt(dchyXmglClcgShjl.getShzt()));
                            }
//                            }
                            m1.putAll(map2);
                        }
                    }
                }
            }
        }
        message = ResponseUtil.wrapSuccessResponse();
        message.setData(result);
        return message;
    }

    @Override
    public String getChxmClcgWjzxidByGcbh(String gcbh) {
        StringJoiner stringJoiner = new StringJoiner("-");
        if (StringUtils.isNotBlank(gcbh)) {
            List<String> babhList = dchyXmglClcgMapper.queryCgrkChxmBabhList(null, gcbh);
            if (CollectionUtils.isNotEmpty(babhList)) {
                for (String babhTemp : babhList) {
                    Integer parentId = platformUtil.creatNode(babhTemp);
                    int fileNum = FileDownoadUtil.getFileNumberByNodeId(parentId);
                    if (fileNum > 0) {
                        stringJoiner.add(parentId.toString());
                    }
                }
            }
        }
        return stringJoiner.toString();
    }

    @Override
    public String getChxmClcgWjzxidByBabh(String babh) {
        StringJoiner stringJoiner = new StringJoiner("-");
        if (StringUtils.isNotBlank(babh)) {
            List<String> babhList = dchyXmglClcgMapper.queryCgrkChxmBabhList(babh, null);
            if (CollectionUtils.isNotEmpty(babhList)) {
                for (String babhTemp : babhList) {
                    Integer parentId = platformUtil.creatNode(babhTemp);
                    int fileNum = FileDownoadUtil.getFileNumberByNodeId(parentId);
                    if (fileNum > 0) {
                        stringJoiner.add(parentId.toString());
                    }
                }
            }
        }
        return stringJoiner.toString();
    }

    //?????????????????????????????????
    private List removeDuplicates(List list) {
        List resultList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object object : list) {
                if (!resultList.contains(object)) {
                    resultList.add(object);
                }
            }
        }
        return resultList;
    }

    private String convertShzt(String dm) {
        String mc = "/";
        if (StringUtils.isNotBlank(dm)) {
            switch (dm) {
                case Constants.DCHY_XMGL_SHZT_DSH:
                    mc = "?????????";
                    break; //??????
                case Constants.DCHY_XMGL_SHZT_SHZ:
                    mc = "?????????";
                    break; //??????
                case Constants.DCHY_XMGL_SHZT_TH:
                    mc = "?????????";
                    break; //??????
                case Constants.DCHY_XMGL_SHZT_SHTG:
                    mc = "?????????";
                    break; //??????
                default: //??????
                    mc = "/";
            }
        }
        return mc;
    }

}
