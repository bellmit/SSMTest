package cn.gtmap.msurveyplat.portalol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.portalol.core.mapper.DchyXmglGldwMapper;
import cn.gtmap.msurveyplat.portalol.core.mapper.DchyXmglMlkMapper;
import cn.gtmap.msurveyplat.portalol.core.service.DchyXmglMlkService;
import cn.gtmap.msurveyplat.portalol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.portalol.core.service.UploadService;
import cn.gtmap.msurveyplat.portalol.model.UserInfo;
import cn.gtmap.msurveyplat.portalol.utils.Constants;
import cn.gtmap.msurveyplat.portalol.utils.UserUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfUserVo;
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.*;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/30 16:37
 * @description
 */
@Service
public class DchyXmglMlkServiceImpl implements DchyXmglMlkService, UploadService {

    @Autowired
    private DchyXmglMlkMapper mlkMapper;
    @Autowired
    private DchyXmglGldwMapper dchyXmglGldwMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private Repository repository;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;


    private static final Log logger = LogFactory.getLog(DchyXmglMlkServiceImpl.class);


    @Override
    public Page<Map<String, Object>> getMlkLikeMcByPage(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : "1");
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : "10");
        String dwmc = CommonUtil.ternaryOperator(data.get("dwmc"));
        HashMap<Object, Object> map = Maps.newHashMap();
        if (StringUtils.isNotBlank(dwmc)) {
            map.put("dwmc", dwmc.trim());
        }
        /*????????????*/
        Page<Map<String, Object>> dataPaging = repository.selectPaging("queryMlsLikeMcByPage", map, page - 1, pageSize);
        DataSecurityUtil.decryptMapList(dataPaging.getContent());
        if (CollectionUtils.isNotEmpty(dataPaging.getContent())) {
            List<Map<String, Object>> mlks = dataPaging.getContent();
            for (Map<String, Object> mlk : mlks) {
                String mlkid = CommonUtil.formatEmptyValue(MapUtils.getString(mlk, "MLKID"));

                Map resultMap = queryClsxByMlkid(mlkid);
                mlk.put("CLSXDMS", MapUtils.getString(resultMap, "clsxDms"));
                mlk.put("CLSXMCS", MapUtils.getString(resultMap, "clsxMcs"));
                mlk.put("PJ", getKpResultByMlkId(mlkid));

                if (null != mlk.get("MLKTP")) {
                    byte[] bytes = blobToBytes((Blob) mlk.get("MLKTP"));
                    if (null != bytes && bytes.length > 0) {
                        mlk.put("MLKTP", new String(bytes));
                    }
                }
            }
        }

        return dataPaging;
    }


    @Override
    public List<Map<String, Object>> getAllCyrysList() {
        return mlkMapper.getAllCyrysList();
    }

    @Override
    public List<Map<String, Object>> queryCyryByMlkId(String mlkid) {
        return mlkMapper.queryCyryByMlkId(mlkid);
    }

    @Override
    public List<Map<String, Object>> queryUploadFileBySsmkId(String ssmkid) {
        return mlkMapper.queryUploadFileBySsmkId(ssmkid);
    }

    @Override
    public String queryMlkIdByDwmc(String dwmc) {
        return null;
    }

    @Override
    @Transactional
    public int getKpResultByMlkId(String mlkid) {
        HashMap<String, Object> map = Maps.newHashMap();
        /*???????????????????????????*/
        Example kpExample = new Example(DchyXmglKp.class);
        kpExample.createCriteria().andEqualTo("mlkid", mlkid).andEqualTo("sfyx", 1);
        kpExample.setOrderByClause("kpsj desc");
        List<DchyXmglKp> kpList = entityMapper.selectByExample(kpExample);
        if (CollectionUtils.isNotEmpty(kpList) && null != kpList) {
            DchyXmglKp xmglKp = kpList.get(0);
            if (null != xmglKp && null != xmglKp.getKpjg()) {
                /*??????????????????*/
//                    map.put("kpjg", dchyXmglZdService.getDchyXmglByZdlxAndDm("KPJG", xmglKp.getKpjg()).getMc());
                map.put("kpjg", Integer.parseInt(xmglKp.getKpjg()));
            }
        }
        //??????mlkid??????????????????????????????
        Example chdwExample = new Example(DchyXmglChxmChdwxx.class);
        chdwExample.createCriteria().andEqualTo("mlkid", mlkid);
        chdwExample.setOrderByClause("pjsj desc");
        List<DchyXmglChxmChdwxx> chdwList = entityMapper.selectByExample(chdwExample);
        if (CollectionUtils.isNotEmpty(chdwList)) {
            DchyXmglChxmChdwxx chdwxx = chdwList.get(0);
            if (null != chdwxx && null != chdwxx.getFwpj()) {
                /*?????????????????????????????????????????????*/
//                    map.put("fwpj", dchyXmglZdService.getDchyXmglByZdlxAndDm("FWPJ", Math.round(chdwxx.getFwpj()) + "").getMc());
                map.put("fwpj", Math.round(chdwxx.getFwpj()));
            }
        }
        return this.calEvaluate(map);
    }


    /**
     * ??????mlkid??????mlk??????
     *
     * @param data
     * @return
     */
    @Override
    public List<Map<String, Object>> queryMlkDetails(Map<String, Object> data) {
        String mlkid = CommonUtil.ternaryOperator(data.get("mlkid"));
        DchyXmglMlk xmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
        List<Map<String, Object>> mlkList = new ArrayList<>();
        if (null != xmglMlk) {
            try {
                DataSecurityUtil.decryptSingleObject(xmglMlk);//??????
                Map<String, Object> mlkMap = (Map<String, Object>) JSONObject.toJSON(xmglMlk);
                String zzdj = MapUtils.getString(mlkMap, "zzdj");//????????????
                if (StringUtils.isNotBlank(zzdj)) {
                    mlkMap.put("zzdjmc", dchyXmglZdService.getDchyXmglByZdlxAndDm("ZZDJ", zzdj).getMc());
                }
                String dwxz = MapUtils.getString(mlkMap, "dwxz");//????????????
                if (StringUtils.isNotBlank(dwxz)) {
                    mlkMap.put("dwxzmc", dchyXmglZdService.getDchyXmglByZdlxAndDm("DWXZ", dwxz).getMc());
                }
                Example cyryExample = new Example(DchyXmglCyry.class);
                cyryExample.createCriteria().andEqualTo("mlkid", mlkid);
                List<DchyXmglCyry> cyryList = entityMapper.selectByExample(cyryExample);
                if (null != cyryList) {
                    mlkMap.put("cyrynum", cyryList.size());
                } else {
                    mlkMap.put("cyrynum", 0);
                }
                mlkList.add(mlkMap);
            } catch (Exception e) {
                logger.error("????????????:{}", e);
            }
        }

        //???????????????????????????
        if (CollectionUtils.isNotEmpty(mlkList)) {
            for (Map<String, Object> mlk : mlkList) {
                String id = CommonUtil.formatEmptyValue(MapUtils.getString(mlk, "mlkid"));
                Map resultMap = queryClsxByMlkid(id);
                mlk.put("clsxdms", MapUtils.getString(resultMap, "clsxDms"));
                mlk.put("clsxmcs", MapUtils.getString(resultMap, "clsxMcs"));
            }
        }

        return mlkList;
    }


    /**
     * ??????mlkid????????????????????????
     *
     * @param data
     * @return
     */
    @Override
    public Page<Map<String, Object>> queryCyryByMlkid(Map<String, Object> data) {
        String mlkid = CommonUtil.ternaryOperator(data.get("mlkid"));
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);
        Map<String, Object> map = Maps.newHashMap();
        map.put("mlkid", mlkid);
        map.put("page", page);
        map.put("pageSize", pageSize);
        Page<Map<String, Object>> cyryPage = repository.selectPaging("queryCyryXxByMlkidByPage", map, page - 1, pageSize);
        DataSecurityUtil.decryptMapList(cyryPage.getContent());
        return cyryPage;
    }

    /**
     * ??????mlkid??????????????????
     *
     * @param data
     * @return
     */
    @Override
    public Page<Map<String, Object>> queryCxjlByMlkid(Map<String, Object> data) {
        String mlkid = CommonUtil.ternaryOperator(data.get("mlkid"));
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);
        Map<String, Object> map = Maps.newHashMap();
        map.put("mlkid", mlkid);
        map.put("page", page);
        map.put("pageSize", pageSize);
        Page<Map<String, Object>> cxjlPage = repository.selectPaging("queryCxjlByMlkidByPage", map, page - 1, pageSize);
        return cxjlPage;
    }

    /**
     * ????????????
     *
     * @param param
     * @return
     */
    private int calEvaluate(Map<String, Object> param) {
        /*(??????????????????+????????????????????????????????????) / 2*/
        Integer kpjg = MapUtils.getInteger(param, "kpjg");
        Integer fwpj = MapUtils.getInteger(param, "fwpj");
        if (null == kpjg) {
            kpjg = 10;
        }
        if (null == fwpj) {
            fwpj = 10;
        }
        return (kpjg + fwpj) / 2;
    }

    /**
     * @param userid
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userid
     * @time 2020/12/3 19:48
     * @description ????????????id?????????????????????
     */
    @Override
    public Map<String, Object> queryMlkXxZtByUserid(String userid) {
        Map<String, Object> mlkzt = Maps.newHashMap();
        String sfyx = "???";
        String mlkid = "";
        if (StringUtils.isNotBlank(userid)) {
            List<Map<String, Object>> yxqkMapList = mlkMapper.getMlkYxztByUserid(userid);
            if (CollectionUtils.isNotEmpty(yxqkMapList)) {
                sfyx = "???";
                mlkid = MapUtils.getString(yxqkMapList.get(0), "MLKID");
            } else {
                List<Map<String, Object>> sqxxByUserid = mlkMapper.getMlkSqztByUserid(userid);
                String sqxxid = "";
                if (CollectionUtils.isNotEmpty(sqxxByUserid)) {
                    Map<String, Object> sqxxMap = sqxxByUserid.get(0);
                    if (StringUtils.equalsIgnoreCase(Constants.SHI, MapUtils.getString(sqxxMap, "SFTH"))) {
                        // ????????????
                        sqxxid = MapUtils.getString(sqxxMap, "SQXXID");
                        List<Map<String, Object>> blyjList = dchyXmglGldwMapper.getZxBlyjBySqxxid(sqxxid);
                        if (CollectionUtils.isNotEmpty(blyjList)) {
                            mlkzt.put("thyj", MapUtils.getString(blyjList.get(0), "BLYJ"));
                        }
                    }
                    mlkzt.put("sqztmc", MapUtils.getString(sqxxMap, "SQZTMC"));
                    mlkzt.put("sfbrsq", MapUtils.getString(sqxxMap, "SFBRSQ"));
                    mlkzt.put("sfth", MapUtils.getString(sqxxMap, "SFTH"));
                    if (StringUtils.equalsIgnoreCase(Constants.SHI, MapUtils.getString(sqxxMap, "SFBRSQ"))) {
                        PfUserVo pfUserVo = sysUserService.getUserVo(MapUtils.getString(sqxxMap, "SQR"));
                        mlkzt.put("sqrmc", pfUserVo != null ? pfUserVo.getUserName() : "");
                    } else {
                        UserInfo userInfo = UserUtil.getCurrentUser();
                        mlkzt.put("sqrmc", userInfo != null ? userInfo.getUsername() : "");
                    }
                    mlkid = MapUtils.getString(sqxxMap, "MLKID");
                }
            }
        }
        mlkzt.put("sfyx", sfyx);
        mlkzt.put("mlkid", mlkid);
        return mlkzt;
    }

    @Override
    public List<Map<String, Object>> getSjclXx(String mlkid) {
        return mlkMapper.getSjclXx(mlkid);
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
            entityMapper.saveOrUpdate(sjxx, sjxx.getSjxxid());

            for (Map<String, Object> map : mapList) {
                String sjclid = UUIDGenerator.generate18();
                map.put("SJCLID", sjclid);
                map.put("SJXXID", sjxxId);
                int fs = ((BigDecimal) map.get("FS")).intValue();
                String mlmc = (String) map.get("CLMC");
                DchyXmglSjcl sjcl = new DchyXmglSjcl();
                sjcl.setSjclid(sjclid);
                sjcl.setSjxxid(sjxxId);
                sjcl.setCllx("1");
                sjcl.setClmc(mlmc);
                sjcl.setFs(fs);
                sjcl.setClrq(new Date());
                sjcl.setYs(1);
                entityMapper.saveOrUpdate(sjcl, sjcl.getSjclid());
            }
        }
    }

    /**
     * ??????sjxxid????????????clxx
     *
     * @param sjxxid
     */
    @Override
    public void clearClxxBySjxxId(String sjxxid) {
        mlkMapper.clearClxxBySjxxId(sjxxid);
    }

    @Override
    @Transactional
    public boolean updateWjzxid(String wjzxid, String glsxid) {
        boolean updated = false;
        if (StringUtils.isNotBlank(glsxid)) {
            DchyXmglMlk dchyXmglMlk = new DchyXmglMlk();
            dchyXmglMlk.setMlkid(glsxid);
            dchyXmglMlk.setWjzxid(wjzxid);
            DataSecurityUtil.decryptSingleObject(dchyXmglMlk);
            int i = entityMapper.updateByPrimaryKeySelective(dchyXmglMlk);
            if (i > 0) {
                updated = true;
            }
        }
        return updated;
    }

    @Override
    public String getSsmkid() {
        return Constants.SSMKID_MLK;
    }

    @Override
    public List<DchyXmglMlk> getMlkByMc(String dwmc) {
        Example mlkExample = new Example(DchyXmglMlk.class);
        mlkExample.createCriteria().andLike("dwmc", dwmc);
        List<DchyXmglMlk> mlks = entityMapper.selectByExample(mlkExample);
        DataSecurityUtil.encryptObjectList(mlks);
        return mlks;
    }

    @Override
    public DchyXmglMlk getMlkById(String mlkid) {
        DchyXmglMlk dchyXmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
        DataSecurityUtil.decryptSingleObject(dchyXmglMlk);
        if (null != dchyXmglMlk && StringUtils.equals("1", dchyXmglMlk.getSfyx())) {
            return dchyXmglMlk;
        }
        return null;
    }

    @Override
    public Map queryClsxByMlkid(String mlkid) {
        Map resultMap = Maps.newHashMap();
        String clsxDms = "";
        String clsxMcs = "";
        if (StringUtils.isNotBlank(mlkid)) {
            Example exampleGx = new Example(DchyXmglMlkClsxGx.class);
            exampleGx.createCriteria().andEqualTo("mlkid", mlkid);
            List<DchyXmglMlkClsxGx> dchyXmglMlkClsxGxes = entityMapper.selectByExample(exampleGx);
            if (CollectionUtils.isNotEmpty(dchyXmglMlkClsxGxes)) {
                for (DchyXmglMlkClsxGx dchyXmglMlkClsxGx : dchyXmglMlkClsxGxes) {
                    String clsxDm = dchyXmglMlkClsxGx.getClsx();
                    String clsxMc = dchyXmglZdService.getZdmcByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, clsxDm);
                    if (!clsxDms.contains(clsxDm)) {
                        clsxDms = clsxDms + clsxDm + ";";
                    }
                    if (!clsxMcs.contains(clsxMc)) {
                        clsxMcs = clsxMcs + clsxMc + ";";
                    }
                }
            }
        }
        resultMap.put("clsxDms", clsxDms);
        resultMap.put("clsxMcs", clsxMcs);
        return resultMap;
    }

    /**
     * ???Blob???????????????byte????????????
     *
     * @param blob
     * @return
     */
    private byte[] blobToBytes(Blob blob) {
        if (null != blob) {
            BufferedInputStream is = null;

            try {
                is = new BufferedInputStream(blob.getBinaryStream());

                byte[] bytes = new byte[(int) blob.length()];

                int len = bytes.length;

                int offset = 0;

                int read = 0;

                if (null != is) {
                    while (offset < len && (read = is.read(bytes, offset, len - offset)) >= 0) {
                        offset += read;

                    }
                }
                return bytes;

            } catch (Exception e) {
                return null;

            } finally {
                try {
                    if (null != is) {
                        is.close();

                        is = null;
                    }

                } catch (IOException e) {
                    return null;
                }
            }
        } else {
            return null;
        }
    }
}
