package cn.gtmap.msurveyplat.serviceol.service.impl;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglTzgg;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglTzggService;
import cn.gtmap.msurveyplat.serviceol.service.DchyGldwTzggglService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
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
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/8
 * @description 管理单位通知公告
 */
@Service
public class DchyGldwTzggglServiceImpl implements DchyGldwTzggglService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DchyGldwTzggglServiceImpl.class);

    @Autowired
    EntityMapper entityMapper;
    @Autowired
    Repository repository;
    @Resource(name = "repositoryXSBF")
    @Autowired
    Repository repositoryXSBF;

    @Autowired
    PlatformUtil platformUtil;

    @Autowired
    DchyXmglTzggService dchyXmglTzggService;

    @Override
    public Map<String, Object> initGldwTzgggl() {
        DchyXmglTzgg dchyXmglTzgg = new DchyXmglTzgg();
        dchyXmglTzgg.setTzggid(UUIDGenerator.generate());
        entityMapper.insertSelective(dchyXmglTzgg);
        Map result = Maps.newHashMap();
        result.put("tzggid", dchyXmglTzgg.getTzggid());
        return result;
    }

    @Override
    @Transactional
    public synchronized Map<String, String> deleteGldwTzggglAndFjByTzggid(Map<String, Object> paramMap) {
        Map<String, String> mapResult = Maps.newHashMap();
        if (null != paramMap && paramMap.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(paramMap, "data");
            String tzggid = MapUtils.getString(data, "tzggid");
            if (StringUtils.isNotBlank(tzggid)) {
                entityMapper.deleteByPrimaryKey(DchyXmglTzgg.class, tzggid);
                // 查询收件信息
                Example dchyXmglSjxxExample = new Example(DchyXmglSjxx.class);
                dchyXmglSjxxExample.createCriteria().andEqualTo("glsxid", tzggid);
                List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExampleNotNull(dchyXmglSjxxExample);
                if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                    Example dchyXmglSjclExample = new Example(DchyXmglSjcl.class);
                    for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                        // 删除收件材料
                        dchyXmglSjclExample.clear();
                        dchyXmglSjclExample.createCriteria().andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                        entityMapper.deleteByExampleNotNull(dchyXmglSjclExample);

                        //  删除filecenter,删除收件信息
                        entityMapper.deleteByPrimaryKey(DchyXmglSjxx.class, dchyXmglSjxx.getSjxxid());
                    }
                }
                /*fileCenter数据删除*/
                int node = 0;
                try {
                    node = platformUtil.creatNode(tzggid);
                    platformUtil.deleteNodeById(node);
                    mapResult.put("code", ResponseMessage.CODE.SUCCESS.getCode());
                    mapResult.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
                } catch (Exception e) {
                    LOGGER.error("错误信息:{}", e);
                    mapResult.put("code", ResponseMessage.CODE.EXCEPTION_MGS.getCode());
                    mapResult.put("msg", ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
                }

            } else {
                mapResult.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
                mapResult.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
            }
        } else {
            mapResult.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            mapResult.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return mapResult;
    }

    @Override
    public Page<Map<String, Object>> getTzggByBtAndGglx(Map<String, Object> map) {
        int page = Integer.parseInt(map.get("page") != null ? CommonUtil.formatEmptyValue(map.get("page")) : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int size = Integer.parseInt(map.get("size") != null ? CommonUtil.formatEmptyValue(map.get("size")) : Constants.DCHY_XMGL_PAGINATION_SIZE);
        return repository.selectPaging("getAllTzggListByPage", map, page - 1, size);
    }

    /**
     * @param
     * @return
     * @description 2020/12/2 添加新的公告
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public int saveOrUpdateTzgg(Map<String, Object> data) {
        String bt = CommonUtil.formatEmptyValue(data.get("bt"));
        String tzggid = CommonUtil.formatEmptyValue(data.get("tzggid"));
        String gglx = CommonUtil.formatEmptyValue(data.get("gglx"));
        String sfzd = CommonUtil.formatEmptyValue(data.get("sfzd"));
        String ggnr = CommonUtil.formatEmptyValue(data.get("ggnr"));
        String zz = CommonUtil.formatEmptyValue(data.get("zz"));
        String wjzxid = CommonUtil.formatEmptyValue(data.get("wjzxid"));

        DchyXmglTzgg dchyXmglTzgg = new DchyXmglTzgg();

        dchyXmglTzgg.setTzggid(StringUtils.equals(tzggid, "") ? UUIDGenerator.generate18() : tzggid);
        dchyXmglTzgg.setBt(bt);
        dchyXmglTzgg.setGglx(gglx);
        dchyXmglTzgg.setSfzd(sfzd);
        dchyXmglTzgg.setSfyx(Constants.VALID);
        dchyXmglTzgg.setGgnr(ggnr.getBytes(Charsets.UTF_8));

        dchyXmglTzgg.setZz(zz);
        dchyXmglTzgg.setFbr(UserUtil.getCurrentUserId());
        dchyXmglTzgg.setFbsj(CalendarUtil.getCurHMSDate());
        dchyXmglTzgg.setWjzxid(wjzxid);

        return entityMapper.saveOrUpdate(dchyXmglTzgg, dchyXmglTzgg.getTzggid());
    }

    @Override
    public Map<String, String> deleteGldwTzggglByTzggid(Map<String, Object> paramMap) {
        Map<String, String> mapResult = Maps.newHashMap();
        if (null != paramMap && paramMap.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(paramMap, "data");
            String tzggid = MapUtils.getString(data, "tzggid");
            if (StringUtils.isNotBlank(tzggid)) {
                int flag = entityMapper.deleteByPrimaryKey(DchyXmglTzgg.class, tzggid);
                if (flag > 0) {
                    mapResult.put("code", ResponseMessage.CODE.SUCCESS.getCode());
                    mapResult.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
                }
            } else {
                mapResult.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
                mapResult.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
            }
        } else {
            mapResult.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            mapResult.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return mapResult;
    }

    @Override
    public Map<String, Object> queryGldwTzggglByTzggid(Map<String, Object> paramMap) {
        Map<String, Object> mapResult = Maps.newHashMap();
        Map<String, Object> map = Maps.newHashMap();
        if (null != paramMap && paramMap.containsKey("data")) {
            Map data = MapUtils.getMap(paramMap, "data");
            String tzggid = MapUtils.getString(data, "tzggid");
            if (StringUtils.isNotBlank(tzggid)) {
                DchyXmglTzgg dchyXmglTzgg = dchyXmglTzggService.getDchyXmglTzggByid(tzggid);
                byte[] bytes = dchyXmglTzgg.getGgnr();
                String json = JSONObject.toJSONString(dchyXmglTzgg);
                map = JSONObject.parseObject(json, Map.class);
                String ggnr = null;
                ggnr = new String(bytes, Charsets.UTF_8);
                map.put("ggnr", ggnr);
                if (dchyXmglTzgg != null) {
                    mapResult.put("code", ResponseMessage.CODE.SUCCESS.getCode());
                    mapResult.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
                }
            } else {
                mapResult.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
                mapResult.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
            }
        } else {
            mapResult.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            mapResult.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }

        mapResult.put("dataList", map);
        return mapResult;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_TZGG_MC, czmkCode = ProLog.CZMC_TZGG_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_TZGG_CODE)
    public Map<String, Object> queryGldwTzgggl(String tzggid) {
        String code = ResponseMessage.CODE.SUCCESS.getCode();
        String msg = ResponseMessage.CODE.SUCCESS.getMsg();
        Map map = Maps.newHashMap();
        try {
            DchyXmglTzgg dchyXmglTzgg = dchyXmglTzggService.getDchyXmglTzggByid(tzggid);
            byte[] bytes = dchyXmglTzgg.getGgnr();
            String json = JSONObject.toJSONString(dchyXmglTzgg);
            map = JSONObject.parseObject(json, Map.class);
            String ggnr = new String(bytes, Charsets.UTF_8);
            map.put("ggnr", ggnr);
        } catch (Exception e) {
            LOGGER.error("错误信息:{}", e);
            code = ResponseMessage.CODE.PARAMEMPTY_FAIL.getCode();
            msg = ResponseMessage.CODE.PARAMEMPTY_FAIL.getMsg();
        }
        Map resultMap = Maps.newHashMap();
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        resultMap.put("dataList", map);
        return resultMap;
    }

    /**
     * @param map
     * @return
     * @description 2020/12/1 管理单位 通知公告 办事指南 管理台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    @SystemLog(czmkMc = ProLog.CZMC_TZGG_MC, czmkCode = ProLog.CZMC_TZGG_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_TZGG_CODE)
    public Page<Map<String, Object>> getTzggByBszn(Map map) {
        int page = Integer.parseInt(map.get("page") != null ? CommonUtil.formatEmptyValue(map.get("page")) : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int size = Integer.parseInt(map.get("size") != null ? CommonUtil.formatEmptyValue(map.get("size")) : Constants.DCHY_XMGL_PAGINATION_SIZE);

        return repository.selectPaging("getBsznTzggListByPage", map, page - 1, size);
    }

    /**
     * @param map
     * @return
     * @description 2020/12/1 管理单位 通知公告管理台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    @SystemLog(czmkMc = ProLog.CZMC_TZGG_MC, czmkCode = ProLog.CZMC_TZGG_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_TZGG_CODE)
    public Page<Map<String, Object>> getOtherTzgg(Map map) {
        int page = Integer.parseInt(map.get("page") != null ? CommonUtil.formatEmptyValue(map.get("page")) : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int size = Integer.parseInt(map.get("size") != null ? CommonUtil.formatEmptyValue(map.get("size")) : Constants.DCHY_XMGL_PAGINATION_SIZE);

        return repository.selectPaging("getOtherTzggListByPage", map, page - 1, size);
    }
}
