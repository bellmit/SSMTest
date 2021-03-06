package cn.gtmap.msurveyplat.server.service.share.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.ActStProRelDo;
import cn.gtmap.msurveyplat.common.domain.DchyCgglGxywsqDO;
import cn.gtmap.msurveyplat.common.domain.DchyCgglGxywxxDO;
import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxm;
import cn.gtmap.msurveyplat.common.dto.DchyCgglGxywsqDto;
import cn.gtmap.msurveyplat.common.dto.GxywsqFycxDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.server.core.mapper.DchyCgglGxywsqMapper;
import cn.gtmap.msurveyplat.server.service.share.GxywsqService;
import cn.gtmap.msurveyplat.server.util.Constants;
import cn.gtmap.msurveyplat.server.util.ExchangeFeignUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GxywsqServiceImpl implements GxywsqService {

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private ExchangeFeignUtil exchangeFeignUtil;
    @Autowired
    private DchyCgglGxywsqMapper dchyCgglGxywsqMapper;

    private static final Logger logger = LoggerFactory.getLogger(GxywsqServiceImpl.class);

    /**
     * @param dchyCgglGxywsqDto
     * @return java.util.Map
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: dchyCgglGxywsqDto
     * @time 2021/6/10 10:20
     * @description ???????????????????????????????????????????????????
     */
    @Override
    public Map initGxywsq(DchyCgglGxywsqDto dchyCgglGxywsqDto) {
        Map map = Maps.newHashMap();
        String code = ResponseMessage.CODE.SUCCESS.getCode();
        String msg = ResponseMessage.CODE.SUCCESS.getMsg();

        if (null != dchyCgglGxywsqDto && CollectionUtils.isNotEmpty(dchyCgglGxywsqDto.getDchyCgglGxywsqDOList())) {
            StringBuilder xmxx = new StringBuilder();
            DchyXmglChxm dchyXmglChxm = null;
            DchyCgglGxywxxDO dchyCgglGxywxxDO = null;
            String xmid = null;
            for (DchyCgglGxywsqDO dchyCgglGxywsqDO : dchyCgglGxywsqDto.getDchyCgglGxywsqDOList()) {
                dchyCgglGxywsqDO.setShzt(Constants.GXYWSQ_SHZT_YSQ);
                dchyCgglGxywsqDO.setSqsj(Calendar.getInstance().getTime());
                dchyCgglGxywsqDO.setGxsqid(UUIDGenerator.generate18());
                dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, dchyCgglGxywsqDO.getChxmid());
                dchyCgglGxywxxDO = entityMapper.selectByPrimaryKey(DchyCgglGxywxxDO.class, dchyCgglGxywsqDO.getGxywid());
                if (null != dchyXmglChxm) {
                    dchyCgglGxywsqDO.setChxmslbh(dchyXmglChxm.getSlbh());
                    dchyCgglGxywsqDO.setChxmbabh(dchyXmglChxm.getBabh());
                    dchyCgglGxywsqDO.setChxmgcbh(dchyXmglChxm.getChgcbh());
                    dchyCgglGxywsqDO.setChxmgcid(dchyXmglChxm.getChgcid());
                }
                if (null != dchyCgglGxywxxDO) {
                    dchyCgglGxywsqDO.setGxywmc(dchyCgglGxywxxDO.getGxywmc());
                }
                xmid = dchyCgglGxywsqDO.getXmid();
            }
            entityMapper.insertBatchSelective(dchyCgglGxywsqDto.getDchyCgglGxywsqDOList());

            if (StringUtils.isNotBlank(xmid)) {
                DchyCgglXmDO dchyCgglXmDO = entityMapper.selectByPrimaryKey(DchyCgglXmDO.class, xmid);
                if (null != dchyCgglXmDO && StringUtils.isNotBlank(dchyCgglXmDO.getGzlslid())) {
                    ActStProRelDo actStProRelDo = new ActStProRelDo();
                    actStProRelDo.setProcInsId(dchyCgglXmDO.getGzlslid());
                    if (dchyXmglChxm != null) {
                        StringBuilder text6 = new StringBuilder();
                        text6.append(dchyXmglChxm.getBabh()).append(Constants.SPLIT_STR).append(dchyXmglChxm.getChgcbh());
                        actStProRelDo.setText6(text6.toString());
                    }
                    actStProRelDo.setText7(dchyCgglGxywsqDto.getDchyCgglGxywsqDOList().get(0).getGxywid());
                    actStProRelDo.setText8(dchyCgglGxywsqDto.getDchyCgglGxywsqDOList().get(0).getSqbmid());
                    actStProRelDo.setText10(dchyCgglGxywsqDto.getDchyCgglGxywsqDOList().get(0).getShzt());
                    exchangeFeignUtil.saveOrUpdateTaskExtendDto(actStProRelDo);
                }
            }
        } else {
            code = ResponseMessage.CODE.PARAMEMPTY_FAIL.getCode();
            msg = ResponseMessage.CODE.PARAMEMPTY_FAIL.getMsg();
        }
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }

    /**
     * @param dchyCgglGxywsqDO
     * @return java.util.Map
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: dchyCgglGxywsqDO
     * @time 2021/6/10 10:22
     * @description ????????????????????????????????????
     */
    @Override
    public String getGxywsqShzt(DchyCgglGxywsqDO dchyCgglGxywsqDO) {
        String shzt = Constants.GXYWSQ_SHZT_WSQ;
        if (null != dchyCgglGxywsqDO && StringUtils.isNoneBlank(dchyCgglGxywsqDO.getChxmid(), dchyCgglGxywsqDO.getGxywid(), dchyCgglGxywsqDO.getSqrid())) {
            Example example = new Example(DchyCgglGxywsqDO.class);
            example.createCriteria().andEqualTo("chxmid", dchyCgglGxywsqDO.getChxmid()).andEqualTo("gxywid", dchyCgglGxywsqDO.getGxywid()).andEqualTo("sqrid", dchyCgglGxywsqDO.getSqrid());
            example.setOrderByClause(" sqsj desc nulls last");
            List<DchyCgglGxywsqDO> dchyCgglGxywsqDOList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyCgglGxywsqDOList)) {
                DchyCgglGxywsqDO dchyCgglGxywsqDONew = dchyCgglGxywsqDOList.get(0);
                shzt = dchyCgglGxywsqDONew.getShzt();
                if (StringUtils.isBlank(shzt)) {
                    shzt = Constants.GXYWSQ_SHZT_YSQ;
                }
            }
        }
        return shzt;
    }

    /**
     * @param dchyCgglGxywsqDO
     * @return java.util.Map
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: dchyCgglGxywsqDO
     * @time 2021/6/10 10:22
     * @description ????????????????????????
     */
    @Override
    public Map gxywsqSh(DchyCgglGxywsqDO dchyCgglGxywsqDO) {
        Map map = Maps.newHashMap();
        String code = ResponseMessage.CODE.SUCCESS.getCode();
        String msg = ResponseMessage.CODE.SUCCESS.getMsg();
        if (null != dchyCgglGxywsqDO && StringUtils.isNoneBlank(dchyCgglGxywsqDO.getGxsqid(), dchyCgglGxywsqDO.getShzt())) {
            dchyCgglGxywsqDO.setShsj(Calendar.getInstance().getTime());
            String sqzt = Constants.GXYWSQ_SHZT_TH;
            if (StringUtils.equals(dchyCgglGxywsqDO.getShzt(), Constants.VALID)) {
                sqzt = Constants.GXYWSQ_SHZT_SHTG;
            }
            dchyCgglGxywsqDO.setShzt(sqzt);
            entityMapper.updateByPrimaryKeySelective(dchyCgglGxywsqDO);
            dchyCgglGxywsqDO = entityMapper.selectByPrimaryKey(DchyCgglGxywsqDO.class, dchyCgglGxywsqDO.getGxsqid());
            if (StringUtils.isNotBlank(dchyCgglGxywsqDO.getXmid())) {
                DchyCgglXmDO dchyCgglXmDO = entityMapper.selectByPrimaryKey(DchyCgglXmDO.class, dchyCgglGxywsqDO.getXmid());
                if (null != dchyCgglXmDO && StringUtils.isNotBlank(dchyCgglXmDO.getGzlslid())) {
                    ActStProRelDo actStProRelDo = new ActStProRelDo();
                    actStProRelDo.setProcInsId(dchyCgglXmDO.getGzlslid());
                    actStProRelDo.setText10(sqzt);
                    exchangeFeignUtil.saveOrUpdateTaskExtendDto(actStProRelDo);
                }
            }
        } else {
            code = ResponseMessage.CODE.PARAMEMPTY_FAIL.getCode();
            msg = ResponseMessage.CODE.PARAMEMPTY_FAIL.getMsg();
        }
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }

    /**
     * @param gxywsqFycxDto
     * @return org.springframework.data.domain.Page<java.util.Map>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: gxywsqFycxDto
     * @time 2021/6/10 10:26
     * @description ???????????????????????????
     */
    @Override
    public Page<Map<String, Object>> gxywsqDbSh(GxywsqFycxDto gxywsqFycxDto) {
        HashMap mapParam = Maps.newHashMap();
        Map map = Maps.newHashMap();
        //???????????????????????????????????????
        Map<String, Integer> pageable = Maps.newHashMap();
        pageable.put("pageNumber", gxywsqFycxDto.getPage());
        pageable.put("pageSize", gxywsqFycxDto.getSize());
        String userIds = gxywsqFycxDto.getUserid();
        if (StringUtils.isBlank(userIds)) {
            map.put("userIds", null);
        } else {
            userIds = "'" + userIds + "'";
            map.put("userIds", userIds.split("','"));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getKssj())){
            map.put("cjkssj",StringUtils.deleteWhitespace(gxywsqFycxDto.getKssj()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getJssj())){
            map.put("cjjssj",StringUtils.deleteWhitespace(gxywsqFycxDto.getJssj()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getSqbmid())){
            map.put("sqbmid",StringUtils.deleteWhitespace(gxywsqFycxDto.getSqbmid()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getBabh())){
            map.put("babh",StringUtils.deleteWhitespace(gxywsqFycxDto.getBabh()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getGcbh())){
            map.put("gcbh",StringUtils.deleteWhitespace(gxywsqFycxDto.getGcbh()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getGxywid())){
            map.put("gxywid",StringUtils.deleteWhitespace(gxywsqFycxDto.getGxywid()));
        }
        map.put("gzldyid", Constants.GXYWSQ_GZLDYID);
        mapParam.put("pageable", pageable);
        mapParam.put("param", map);
        //???????????????????????????????????????
        Page<Map<String, Object>> result = pageResult(exchangeFeignUtil.getTaskList(mapParam));
        logger.info("???????????????{}",result.getContent());
        getGxywsqLcxx(result);
        return result;
    }

    /**
     * @param gxywsqFycxDto
     * @return org.springframework.data.domain.Page<java.util.Map>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: dchyCgglGxywsqDO
     * @time 2021/6/10 10:26
     * @description ???????????????????????????
     */
    @Override
    public Page<Map<String, Object>> gxywsqYbSh(GxywsqFycxDto gxywsqFycxDto) {
        HashMap mapParam = Maps.newHashMap();
        Map map = Maps.newHashMap();
        //???????????????????????????????????????
        Map<String, Integer> pageable = Maps.newHashMap();
        pageable.put("pageNumber", gxywsqFycxDto.getPage());
        pageable.put("pageSize", gxywsqFycxDto.getSize());
        String userIds = gxywsqFycxDto.getUserid();
        if (StringUtils.isBlank(userIds)) {
            map.put("userIds", null);
        } else {
            userIds = "'" + userIds + "'";
            map.put("userIds", userIds.split("','"));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getKssj())){
            map.put("blkssj",StringUtils.deleteWhitespace(gxywsqFycxDto.getKssj()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getJssj())){
            map.put("bljssj",StringUtils.deleteWhitespace(gxywsqFycxDto.getJssj()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getSqbmid())){
            map.put("sqbmid",StringUtils.deleteWhitespace(gxywsqFycxDto.getSqbmid()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getBabh())){
            map.put("babh",StringUtils.deleteWhitespace(gxywsqFycxDto.getBabh()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getGcbh())){
            map.put("gcbh",StringUtils.deleteWhitespace(gxywsqFycxDto.getGcbh()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getGxywid())){
            map.put("gxywid",StringUtils.deleteWhitespace(gxywsqFycxDto.getGxywid()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getShjg())){
            String sqzt = Constants.GXYWSQ_SHZT_TH;
            if (StringUtils.equals(gxywsqFycxDto.getShjg(), Constants.VALID)) {
                sqzt = Constants.GXYWSQ_SHZT_SHTG;
            }
            map.put("shjg",sqzt);
        }
        map.put("gzldyid", Constants.GXYWSQ_GZLDYID);
        mapParam.put("pageable", pageable);
        mapParam.put("param", map);
        //???????????????????????????????????????
        Page<Map<String, Object>> result = pageResult(exchangeFeignUtil.getTaskOverList(mapParam));
        getGxywsqLcxx(result);
        return result;
    }

    /**
     * @param gxywsqFycxDto
     * @return org.springframework.data.domain.Page<java.util.Map>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: dchyCgglGxywsqDO
     * @time 2021/6/10 10:26
     * @description ???????????????????????????
     */
    @Override
    public Page<Map<String, Object>> gxywsqYbSq(GxywsqFycxDto gxywsqFycxDto) {
        HashMap mapParam = Maps.newHashMap();
        Map map = Maps.newHashMap();
        //???????????????????????????????????????
        Map<String, Integer> pageable = Maps.newHashMap();
        pageable.put("pageNumber", gxywsqFycxDto.getPage());
        pageable.put("pageSize", gxywsqFycxDto.getSize());
        String userIds = gxywsqFycxDto.getUserid();
        if (StringUtils.isBlank(userIds)) {
            map.put("userIds", null);
        } else {
            userIds = "'" + userIds + "'";
            map.put("userIds", userIds.split("','"));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getKssj())){
            map.put("cjkssj",StringUtils.deleteWhitespace(gxywsqFycxDto.getKssj()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getJssj())){
            map.put("cjjssj",StringUtils.deleteWhitespace(gxywsqFycxDto.getJssj()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getBabh())){
            map.put("babh",StringUtils.deleteWhitespace(gxywsqFycxDto.getBabh()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getGcbh())){
            map.put("gcbh",StringUtils.deleteWhitespace(gxywsqFycxDto.getGcbh()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getGxywid())){
            map.put("gxywid",StringUtils.deleteWhitespace(gxywsqFycxDto.getGxywid()));
        }
        if (StringUtils.isNotBlank(gxywsqFycxDto.getShjg())){
            map.put("shjg",StringUtils.deleteWhitespace(gxywsqFycxDto.getShjg()));
        }
        map.put("gzldyid", Constants.GXYWSQ_GZLDYID);
        mapParam.put("pageable", pageable);
        mapParam.put("param", map);
        //???????????????????????????????????????
        Page<Map<String, Object>> result = pageResult(exchangeFeignUtil.getTaskOverList(mapParam));
        getGxywsqLcxx(result);
        return result;
    }

    private Page<Map<String, Object>> pageResult(Map<String, Object> result) {
        Pageable pageable = new PageRequest(MapUtils.getInteger(result, "number", 0), MapUtils.getInteger(result, "size", 10));
        return new PageImpl((List<Map<String, Object>>) MapUtils.getObject(result, "content"), pageable, MapUtils.getInteger(result, "totalElements"));
    }

    private void getGxywsqLcxx(Page<Map<String, Object>> result) {
        List<Map<String, Object>> list = result.getContent();
        if (CollectionUtils.isNotEmpty(list)) {
            List gzlslidList = Lists.newArrayList();
            String gzlslid;
            for (Map map : list) {
                gzlslid = MapUtils.getString(map, "procInsId");
                if (StringUtils.isNotBlank(gzlslid)) {
                    gzlslidList.add(gzlslid);
                }
            }
            Map param = Maps.newHashMap();
            param.put("gzlslidList", gzlslidList);
            List<Map<String, Object>> gxywsqList = dchyCgglGxywsqMapper.getDchyCgglGxywsqByXmid(param);
            if (CollectionUtils.isNotEmpty(gxywsqList)) {
                Map<String, List<Map<String, Object>>> dividedGxywsq = CommonUtil.divideListToMap("GZLSLID", gxywsqList);
                for (Map map : list) {
                    gzlslid = MapUtils.getString(map, "procInsId");
                    List<Map<String, Object>> dividedGxywsqList = dividedGxywsq.get(gzlslid);
                    if (CollectionUtils.isNotEmpty(dividedGxywsqList)) {
                        map.putAll(dividedGxywsqList.get(0));
                    }
                }
            }
        }
    }
}
