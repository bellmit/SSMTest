package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repo;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglChdwMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglChdwService;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/30 14:42
 * @description
 */
@Service
public class DchyXmglChdwServiceImpl implements DchyXmglChdwService {

    @Autowired
    private DchyXmglChdwMapper dchyXmglChdwMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private Repo repository;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;
    @Resource(name = "repositoryXSBF")
    @Autowired
    Repository repositoryXSBF;

    protected final Log logger = LogFactory.getLog(getClass());


    @Override
    public DchyXmglCyry qeuryCyryByCyryId(String cyryId) {
        return entityMapper.selectByPrimaryKey(DchyXmglCyry.class, cyryId);
    }

    @Override
    public Page<Map<String, Object>> queryJsdwPlXx(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : "1");
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : "10");
        HashMap<String, Object> map = Maps.newHashMap();
        String pjkssj = CommonUtil.ternaryOperator(data.get("pjkssj"));
        String pjjssj = CommonUtil.ternaryOperator(data.get("pjjssj"));
        String fwpj = CommonUtil.ternaryOperator(data.get("fwpj"));
        String mlkid = this.getMlkIdByCurrentUser(UserUtil.getCurrentUserId());
        map.put("pjkssj", pjkssj);
        map.put("pjjssj", pjjssj);
        map.put("fwpj", fwpj);
        map.put("mlkid", mlkid);
        Page<Map<String, Object>> jsdwPlXxByPage = repositoryXSBF.selectPaging("queryJsdwPlXxByPage", map, page - 1, pageSize);
        DataSecurityUtil.decryptMapList(jsdwPlXxByPage.getContent());
        return jsdwPlXxByPage;
    }


    /**
     * 测绘单位查看对应考评记录
     *
     * @param data
     * @return
     */
    @Override
    public Page<Map<String, Object>> queryChdwKpInfo(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : "1");
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : "10");
        HashMap<String, Object> map = Maps.newHashMap();
        String kpkssj = CommonUtil.ternaryOperator(data.get("kpkssj"));
        String kpjssj = CommonUtil.ternaryOperator(data.get("kpjssj"));
        String kpjg = CommonUtil.ternaryOperator(data.get("kpjg"));
        String mlkid = this.getMlkIdByCurrentUser(UserUtil.getCurrentUserId());
        map.put("kpkssj", kpkssj);
        map.put("kpjssj", kpjssj);
        map.put("kpjg", kpjg);
        map.put("mlkid", mlkid);

        if (StringUtils.isNotBlank(mlkid)) {
            Page<Map<String, Object>> queryGldwKpxxByPage = repository.selectPaging("queryGldwKpxxByPage", map, page - 1, pageSize);
            List<Map<String, Object>> content = queryGldwKpxxByPage.getContent();
            if (CollectionUtils.isNotEmpty(content)) {
                for (Map<String, Object> objectMap : content) {
                    if (StringUtils.isNotBlank(MapUtils.getString(objectMap, "KPJG"))) {
                        String kpjgmc = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_KPJG, MapUtils.getString(objectMap, "KPJG")).getMc();
                        if (StringUtils.isNotBlank(kpjgmc)) {
                            objectMap.put("KPJGMC", kpjgmc.substring(0, kpjgmc.indexOf("（")));
                        }
                    }
                }
            }
            DataSecurityUtil.decryptMapList(queryGldwKpxxByPage.getContent());
            return queryGldwKpxxByPage;
        }
        return null;
    }

    @Override
    public Map<String, Object> getCreditRate() {
        String mlkid = this.getMlkIdByCurrentUser(UserUtil.getCurrentUserId());
        HashMap<String, Object> map = Maps.newHashMap();
        /*根据mlkid获取对应(考评表里)信用信息*/
        Example kpExample = new Example(DchyXmglKp.class);
        kpExample.createCriteria().andEqualTo("mlkid", mlkid).andEqualTo("sfyx", "1");
        List<DchyXmglKp> kpList = entityMapper.selectByExample(kpExample);
        if (CollectionUtils.isNotEmpty(kpList)) {
            DchyXmglKp xmglKp = kpList.get(0);
            if (null != xmglKp) {
                String xy = xmglKp.getXy();
                if (StringUtils.isNotBlank(xy)) {
                    String mc = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_XYD, xy).getMc();
                    if (StringUtils.isNotBlank(mc)) {
                        String xyDj = this.getXyDj(Integer.parseInt(mc));
                        map.put("XYDJ", xyDj.substring(0, xyDj.indexOf("（")));
                    }
                }
            }
        }
        return map;
    }

    /**
     * 获取当前用户的mlkid
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
                /*根据用户单位获取对应名录库*/
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


    @Override
    public List<Map<String, Object>> queryFwpjZd() {
        List<Map<String, Object>> maps = Lists.newArrayList();
        List<DchyXmglZd> dchyXmglZdList = dchyXmglZdService.getDchyXmglZdListByZdlx("KPJG");
        Set<String> kpjgmcSet = Sets.newHashSet();
        for (DchyXmglZd dchyXmglZd : dchyXmglZdList) {
            if (kpjgmcSet.add(dchyXmglZd.getMc())) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("MC", dchyXmglZd.getMc());
                map.put("kpjg", dchyXmglZd.getDm());
                maps.add(map);
            }
        }
        Collections.sort(maps, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                int ret;
                if (Integer.parseInt(o1.get("kpjg").toString()) > Integer.parseInt(o2.get("kpjg").toString())) {
                    ret = 1;
                } else {
                    ret = -1;
                }
                return ret;
            }
        });
        return maps;
    }

    private String getXyDj(int xy) {
        String value = "";
        if (xy <= 4) {
            value = "较差（4分及以下）";
        } else if (xy >= 9) {
            value = "优秀（9分及以上）";
        } else if (xy >= 7 && xy < 9) {
            value = "良好（7分及以上）";
        } else if (xy >= 5 && xy < 7) {
            value = "一般（5分及以上）";
        }
        return value;
    }

    /**
     * 非名录库机构查看
     *
     * @param
     * @return
     */
    @Override
    public Page<Map<String, Object>> queryUnmlkByPage(Map<String, Object> map) {
        Page<Map<String, Object>> unmlkList = null;
        try {
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            int page = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("page")));
            int pageSize = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("pageSize")));
            Map<String, Object> paramMap = Maps.newHashMap();
            if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(data.get("chdwmc")))) {
                String chdwmc = CommonUtil.formatEmptyValue(data.get("chdwmc"));
                paramMap.put("chdwmc", chdwmc);
            }
            if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(data.get("chdwid")))) {
                String chdwid = CommonUtil.formatEmptyValue(data.get("chdwid"));
                paramMap.put("chdwid", chdwid);
            }
            unmlkList = repositoryXSBF.selectPaging("queryUnmlkByPage", paramMap, page - 1, pageSize);
            for (Map<String, Object> unmlkLists : unmlkList) {
                if (StringUtils.isNotBlank(MapUtils.getString(unmlkLists, "BGDZS"))) {
                    String bgdzs = MapUtils.getString(unmlkLists, "BGDZS");
                    DchyXmglZd bgdzsZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("GCDZ", bgdzs);
                    unmlkLists.put("BGDZS", bgdzsZd.getMc());
                }

                if (StringUtils.isNotBlank(MapUtils.getString(unmlkLists, "BGDZSS"))) {
                    String bgdzss = MapUtils.getString(unmlkLists, "BGDZSS");
                    DchyXmglZd bgdzssZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("GCDZ", bgdzss);
                    unmlkLists.put("BGDZSS", bgdzssZd.getMc());
                }

                if (StringUtils.isNotBlank(MapUtils.getString(unmlkLists, "BGDZQX"))) {
                    String bgdzqx = MapUtils.getString(unmlkLists, "BGDZQX");
                    DchyXmglZd bgdzqxZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("GCDZ", bgdzqx);
                    unmlkLists.put("BGDZQX", bgdzqxZd.getMc());
                }
            }


            DataSecurityUtil.decryptMapList(unmlkList.getContent());
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
        }
        return unmlkList;
    }
}
