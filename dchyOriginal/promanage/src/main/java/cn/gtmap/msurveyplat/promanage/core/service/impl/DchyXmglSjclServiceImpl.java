package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglHtxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjclpz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjxx;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglClsxHtxxGxMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglHtxxChdwxxGxMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglSjclMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglSjclpzMapper;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglMlkService;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglSjclService;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@Service
public class DchyXmglSjclServiceImpl implements DchyXmglSjclService {

    @Autowired
    private DchyXmglSjclMapper dchyXmglSjclMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    DchyXmglClsxHtxxGxMapper dchyXmglClsxHtxxGxMapper;
    @Autowired
    DchyXmglHtxxChdwxxGxMapper dchyXmglHtxxChdwxxGxMapper;
    @Autowired
    DchyXmglSjclService dchyXmglSjclService;
    @Autowired
    DchyXmglMlkService dchyXmglMlkService;
    @Autowired
    DchyXmglZdService dchyXmglZdService;
    @Autowired
    DchyXmglSjclpzMapper dchyXmglSjclpzMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * ???????????????????????????
     *
     * @param glsxid
     * @return
     */
    @Override
    public List<Map<String, Object>> getHtxxSjclXx(String glsxid) {
        List<Map<String, Object>> htxxList = dchyXmglSjclMapper.getHtxxSjclXx(glsxid);
        for (Map<String, Object> htxxLists : htxxList) {
            if (StringUtils.isNotEmpty(MapUtils.getString(htxxLists, "HTXXID"))) {
                String htxxid = MapUtils.getString(htxxLists, "HTXXID");
                List<Map<String, Object>> clsxList = dchyXmglClsxHtxxGxMapper.getClsxByChxmid(htxxid);
                List<Map<String, Object>> chdwxxList = dchyXmglHtxxChdwxxGxMapper.getChdwxxByChxmid(htxxid);

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
    public List<Map<String, Object>> querySjclList(Map<String, Object> map) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        List<Object> clsxList = (List<Object>) data.get("clsxList");
        String ssmkId = CommonUtil.ternaryOperator(data.get("ssmkid"));
        String glsxId = CommonUtil.ternaryOperator(data.get("glsxid"));
        if (CollectionUtils.isEmpty(clsxList)) {
            resultList = querySjclTableByGlsxIdAndSsmkId(glsxId, ssmkId);
            return resultList;
        } else {
            List<Map<String, Object>> sjclpzList = querySjclpzList(clsxList, ssmkId);
            List<Map<String, Object>> sjclpzTmpList = Lists.newArrayList(sjclpzList);
            List<Map<String, Object>> sjclList;
            if (StringUtils.isNotBlank(glsxId) && StringUtils.isNotEmpty(glsxId) && StringUtils.isNotBlank(ssmkId) && StringUtils.isNotEmpty(ssmkId)) {
                sjclList = dchyXmglSjclMapper.querySjclBySsmkIdAndGlsxid(ssmkId, glsxId);
                int countSjcl = sjclList.size();
                ListIterator<Map<String, Object>> sjclIterator = sjclList.listIterator();
                ListIterator<Map<String, Object>> sjclpzIterator = sjclpzList.listIterator();
                while (sjclIterator.hasNext()) {
                    Map<String, Object> sjcl = sjclIterator.next();
                    if (null != sjcl.get("SJCLPZID") && !"".equals(sjcl.get("SJCLPZID").toString())) {
                        String sjclpzIdSjcl = sjcl.get("SJCLPZID").toString();
                        while (sjclpzIterator.hasNext()) {
                            Map<String, Object> sjclpz = sjclpzIterator.next();
                            String sjclpzIdStr = sjclpz.get("SJCLPZID").toString();
                            logger.info("sjclpzIdSjcl:  " + sjclpzIdSjcl + " == " + sjclpzIdStr + "   sjclpzIdStr");
                            if (sjclpzIdSjcl.equals(sjclpzIdStr)) {
                                sjclIterator.remove();
                                sjclpzIterator.remove();
                                if (sjclIterator.hasNext()) {
                                    sjcl = sjclIterator.next();
                                    if (null != sjcl.get("SJCLPZID")) {
                                        sjclpzIdSjcl = sjcl.get("SJCLPZID").toString();
                                    }
                                } else {
                                    sjcl = null;
                                    break;
                                }
                            }
                        }
                    }
                    if (null != sjcl && null != sjcl.get("SJCLID")) {
                        //????????????????????????????????????????????????????????????
                        String sjclId = sjcl.get("SJCLID").toString();
                        //???sjclpzId??????????????????
                        if (null != sjcl.get("SJCLPZID") && !containsInPz(sjcl.get("SJCLPZID"), sjclpzList)) {
                            entityMapper.deleteByPrimaryKey(DchyXmglSjcl.class, sjclId);
                        }
                        sjclIterator.remove();
                    }
                }
                if (sjclpzIterator.hasNext()) {
                    String sjxxId;
                    if (0 < countSjcl) {
                        sjxxId = getSjxxByGlsxId(glsxId);
                    } else {
                        //?????????????????????????????????
                        sjxxId = initSjxx(glsxId, ssmkId);
                    }
                    while (sjclpzIterator.hasNext()) {
                        Map<String, Object> sjclpz = sjclpzIterator.next();
                        //????????????????????????????????????
                        initSjcl(sjxxId, sjclpz);
                    }
                } else {
                    if (sjclpzIterator.hasPrevious()) {
                        //?????????????????????????????????
                        String sjxxId;
                        if (0 < countSjcl) {
                            sjxxId = getSjxxByGlsxId(glsxId);
                        } else {
                            //?????????????????????????????????
                            sjxxId = initSjxx(glsxId, ssmkId);
                        }
                        while (sjclpzIterator.hasPrevious()) {
                            Map<String, Object> sjclpz = sjclpzIterator.previous();
                            //????????????????????????????????????
                            initSjcl(sjxxId, sjclpz);
                        }
                    }
                }
                resultList = querySjclTable(sjclpzTmpList, glsxId);
            } else {
                return resultList;
            }
        }
        return resultList;
    }

    /**
     * ????????????????????????????????????
     *
     * @param glsxid  String
     * @param ssmkId  String
     * @param mapList String
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
                entityMapper.saveOrUpdate(htxx, htxx.getHtxxid());

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
                entityMapper.saveOrUpdate(sjxx, sjxx.getSjxxid());

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
                entityMapper.saveOrUpdate(sjcl, sjcl.getSjclid());
            }
        }
    }

    /**
     * ????????????????????????
     */
    @Override
    @Transactional
    public Map<String, Object> saveSjclpz(Map<String, Object> map) {
        String code = ResponseMessage.CODE.OPERA_FAIL.getCode();
        String msg = ResponseMessage.CODE.OPERA_FAIL.getMsg();
        try {
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            String clmc = CommonUtil.formatEmptyValue(data.get("clmc"));
            String cllx = CommonUtil.formatEmptyValue(data.get("cllx"));
            String sjxxId = CommonUtil.formatEmptyValue(data.get("sjxxid"));
            String mrfs = CommonUtil.formatEmptyValue(data.get("fs"));
            List<String> ssclsxList = (List<String>) data.get("ssclsxlist");
            String ssclsxStr = String.join(",", ssclsxList);
            DchyXmglSjcl sjcl = new DchyXmglSjcl();
            sjcl.setSjclid(UUIDGenerator.generate18());
            sjcl.setSjxxid(sjxxId);
            sjcl.setClmc(clmc);
            sjcl.setFs(Integer.parseInt(mrfs));
            sjcl.setCllx(cllx);
            sjcl.setClsx(ssclsxStr);
            sjcl.setClrq(new Date());
            int result = entityMapper.saveOrUpdate(sjcl, sjcl.getSjclid());
            if (result > 0) {
                code = ResponseMessage.CODE.SUCCESS.getCode();
                msg = ResponseMessage.CODE.SUCCESS.getMsg();
            }
        } catch (Exception e) {
            logger.error("????????????{}???", e);
            code = ResponseMessage.CODE.OPERA_FAIL.getCode();
            msg = ResponseMessage.CODE.OPERA_FAIL.getMsg();
        }
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        return resultMap;
    }

    /**
     * ????????????????????????
     */
    @Override
    @Transactional
    public Map deleteSjclpz(Map<String, Object> map) {
        String code = ResponseMessage.CODE.DELETE_FAIL.getCode();
        String msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
        try {
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            String sjclpzid = CommonUtil.formatEmptyValue(data.get("sjclpzid"));
            if (StringUtils.isNotBlank(sjclpzid)) {
                int result = entityMapper.deleteByPrimaryKey(DchyXmglSjclpz.class, sjclpzid);
                if (result > 0) {
                    code = ResponseMessage.CODE.SUCCESS.getCode();
                    msg = ResponseMessage.CODE.SUCCESS.getMsg();
                }
            }
        } catch (Exception e) {
            logger.error("????????????{}???", e);
            code = ResponseMessage.CODE.DELETE_FAIL.getCode();
            msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
        }
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        return resultMap;
    }

    //region private

    private List<Map<String, Object>> querySjclTableByGlsxIdAndSsmkId(String glsxId, String ssmkId) {
        List<Map<String, Object>> resultList = dchyXmglSjclMapper.querySjclsByGlsxIdAndSsmkId(glsxId, ssmkId);
        resultList.forEach(row -> {
            if (null != row.get("CLSX")) {
                String clsxStr = row.get("CLSX").toString();
                String[] clsxIdArg = clsxStr.split(",");
                List<Object> clsxObjList = Lists.newArrayList(clsxIdArg);
                List<Map<String, Object>> clsxList = dchyXmglSjclMapper.queryZdByInDm(clsxObjList);
                row.put("CLSXS", clsxList);
            } else {
                row.put("CLSXS", null);
            }
            String userName = row.get("SJR").toString();
            //?????????????????????,???????????????
            //String userName = UserUtil.getUserNameById(row.get("SJR").toString());
            row.put("SJRMC", (null == userName) ? "" : userName);
        });
        return resultList;
    }

    private List<Map<String, Object>> querySjclTable(List<Map<String, Object>> sjclpzList, String glsxId) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        List<Map<String, Object>> rowTap = dchyXmglSjclMapper.querySjclBySjxxId(glsxId);
        rowTap.forEach(row -> {
            if (null != row) {
                if (null != row.get("SJCLPZID")) {
                    List<Map<String, Object>> ssclsxList = dchyXmglSjclMapper.querySsclsxsBySjclpzId(row.get("SJCLPZID").toString());
                    if (CollectionUtils.isNotEmpty(ssclsxList)) {
                        row.put("CLSXLIST", ssclsxList);
                    } else {
                        row.put("CLSXLIST", null);
                    }
                } else {
                    List<Object> clsxList = Lists.newArrayList(row.get("CLSX").toString().split(","));
                    List<Map<String, Object>> ssclsxList = dchyXmglSjclMapper.queryZdByInDm(clsxList);
                    if (CollectionUtils.isNotEmpty(ssclsxList)) {
                        row.put("CLSXLIST", ssclsxList);
                    } else {
                        row.put("CLSXLIST", null);
                    }
                }
                //????????????????????????
                String userName = row.get("SJR").toString();
                //?????????????????????,???????????????
                //String userName = UserUtil.getUserNameById(row.get("SJR").toString());
                row.put("SJCLPZCLMC", null);
                row.put("SJRMC", (null == userName) ? "" : userName);
                row.put("NEED", null);
                row.put("CLLXID", null);
                row.put("MRFS", null);
                resultList.add(row);
            }
        });
        sjclpzList.forEach(sjclpz -> {
            //????????????????????????
            String sjclpzId = sjclpz.get("SJCLPZID").toString();
            List<Map<String, Object>> rows = dchyXmglSjclMapper.querySjclBySjclpzIdAndSjxxId(sjclpzId, glsxId);
            rows.forEach(row -> {
                if (null != row) {
                    if (null != row.get("SJCLPZID")) {
                        List<Map<String, Object>> ssclsxList = dchyXmglSjclMapper.querySsclsxsBySjclpzId(row.get("SJCLPZID").toString());
                        if (CollectionUtils.isNotEmpty(ssclsxList)) {
                            row.put("CLSXLIST", ssclsxList);
                        } else {
                            row.put("CLSXLIST", null);
                        }
                    } else {
                        List<Object> clsxList = Lists.newArrayList(row.get("CLSX").toString().split(","));
                        List<Map<String, Object>> ssclsxList = dchyXmglSjclMapper.queryZdByInDm(clsxList);
                        if (CollectionUtils.isNotEmpty(ssclsxList)) {
                            row.put("CLSXLIST", ssclsxList);
                        } else {
                            row.put("CLSXLIST", null);
                        }
                    }
                    //????????????????????????
                    String sjclpzClmc = sjclpz.get("CLMC").toString();
                    String userName = row.get("SJR").toString();
                    //?????????????????????,???????????????
                    //String userName = UserUtil.getUserNameById(row.get("SJR").toString());
                    row.put("SJCLPZCLMC", (null == sjclpzClmc) ? "" : sjclpzClmc);
                    row.put("SJRMC", (null == userName) ? "" : userName);
                    String needStr = sjclpz.get("NEED").toString();
                    row.put("NEED", needStr);
                    String cllxStr = sjclpz.get("CLLXID").toString();
                    row.put("CLLXID", cllxStr);
                    String mrfs = sjclpz.get("MRFS").toString();
                    row.put("MRFS", mrfs);
                    resultList.add(row);
                }
            });
        });
        return resultList;
    }

    private String getSjxxByGlsxId(String glsxId) {
        return dchyXmglSjclMapper.getSjxxByGlsxId(glsxId);
    }

    private void initSjcl(String sjxxId, Map<String, Object> sjclpz) {
        if (existSjcl(sjxxId, CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "SJCLPZID")))) {
        } else {
            String sjclId = UUIDGenerator.generate18();
            String mrfs = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "MRFS"));
            Integer fs = StringUtils.isNotEmpty(mrfs) ? Integer.parseInt(mrfs) : Constants.DCHY_XMGL_MRFS;
            String cllx = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "CLLXID"));
            Integer ys = Constants.DCHY_XMGL_MRYS;
            String xh1 = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "XH"));
            Integer xh = StringUtils.isNotEmpty(xh1) ? Integer.parseInt(xh1) : Constants.DCHY_XMGL_MRXH;

            String sjclpzId = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "SJCLPZID"));
            String ssclsxId = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "clsxStr"));
            String clmcStr = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "CLMC"));
            DchyXmglSjcl dchyXmglSjcl = new DchyXmglSjcl();
            dchyXmglSjcl.setSjclid(sjclId);
            dchyXmglSjcl.setSjxxid(sjxxId);
            dchyXmglSjcl.setFs(fs);
            dchyXmglSjcl.setCllx(cllx);
            dchyXmglSjcl.setYs(ys);
            dchyXmglSjcl.setXh(xh);
            dchyXmglSjcl.setSjclpzid(sjclpzId);
            dchyXmglSjcl.setClsx(ssclsxId);
            dchyXmglSjcl.setClmc(clmcStr);
            entityMapper.saveOrUpdate(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
        }
    }

    private boolean existSjcl(String sjxxId, String sjclpzId) {
        int a = dchyXmglSjclMapper.countSjclBySjxxIdAndSjclpzId(sjxxId, sjclpzId);
        return (a > 0);
    }

    private String initSjxx(String glsxId, String ssmkId) {
        String uuid = UUIDGenerator.generate18();
        Date sjsj = new Date();
        String sjr = UserUtil.getCurrentUserId();
        String tjr = "";
        DchyXmglSjxx sjxx = new DchyXmglSjxx();
        sjxx.setSjxxid(uuid);
        sjxx.setGlsxid(glsxId);
        sjxx.setSsmkid(ssmkId);
        sjxx.setSjsj(sjsj);
        sjxx.setTjr(tjr);
        sjxx.setSjr(sjr);
        entityMapper.saveOrUpdate(sjxx, sjxx.getSjxxid());
        return uuid;
    }

    private List<Map<String, Object>> querySjclpzList(List<Object> clsxList, String ssmkId) {
        List<String> sjclpzIdList = dchyXmglSjclpzMapper.querySjclpzIdsBySsclsxIdIn(clsxList);
        List<Map<String, Object>> sjclpzList = Lists.newArrayList();
        sjclpzIdList.forEach(sjclpzId -> {
            Map<String, Object> sjclpzMap = dchyXmglSjclMapper.querySjclpzBySjclpzIdAndSsmkId(sjclpzId, ssmkId);
            List<Map<String, Object>> ssclsxList = dchyXmglSjclMapper.querySsclsxsBySjclpzId(sjclpzId);
            if (CollectionUtils.isNotEmpty(ssclsxList)) {
                sjclpzMap.put("clsx", ssclsxList);
                StringBuilder ssclsxStr = new StringBuilder();
                for (int i = 0; i < ssclsxList.size(); i++) {
                    Map<String, Object> ssclsx = ssclsxList.get(i);
                    if (i < (ssclsxList.size() - 1)) {
                        ssclsxStr.append(ssclsx.get("DM")).append(",");
                    } else {
                        ssclsxStr.append(ssclsx.get("DM"));
                    }
                }
                sjclpzMap.put("clsxStr", ssclsxStr.toString());
            } else {
                sjclpzMap.put("clsx", null);
                sjclpzMap.put("clsxStr", null);
            }
            sjclpzList.add(sjclpzMap);
        });
        return sjclpzList;
    }

    private boolean containsInPz(Object sjclpzid, List<Map<String, Object>> sjclpzList) {
        for (Map<String, Object> sjclpz : sjclpzList) {
            String sjclpzIdStr = sjclpz.get("SJCLPZID").toString();
            if (sjclpzid.toString().equals(sjclpzIdStr)) {
                return true;
            }
        }
        return false;
    }
    //endregion

}
