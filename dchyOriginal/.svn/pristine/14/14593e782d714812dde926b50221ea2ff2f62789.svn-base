package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.dto.DchyXmglTjfxDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglTjgxFyDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglTjfxMapper;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.service.DchyXmglTjfxService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.web.utils.EhcacheUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0, 2021/4/14
 * @description
 */
@Service
public class DchyXmglTjfxServiceImpl implements DchyXmglTjfxService {


    @Autowired
    private DchyXmglTjfxMapper dchyXmglTjfxMapper;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private Repository repository;

    @Autowired
    private PushDataToMqService pushDataToMqService;

    private static final Logger logger = LoggerFactory.getLogger(DchyXmglTjfxServiceImpl.class);

    @Override
    public List<DchyXmglMlk> getChdwList() {
        Example example = new Example(DchyXmglMlk.class);
        return entityMapper.selectByExample(example);
    }

    @Override
    public List<Map<String, Object>> getXmsl(Map<String, Object> data) {
        Map<String, Object> paramMap = new HashMap<>();
        String kssj = CommonUtil.ternaryOperator(data.get("kssj"));
        String jssj = CommonUtil.ternaryOperator(data.get("jssj"));
        String chdwmc = CommonUtil.ternaryOperator(data.get("chdwmc"));
        if (StringUtils.isNotBlank(kssj)) {
            paramMap.put("kssj", kssj);
        }
        if (StringUtils.isNotBlank(jssj)) {
            paramMap.put("jssj", jssj);
        }
        if (StringUtils.isNotBlank(chdwmc)) {
            paramMap.put("chdwmc", chdwmc);
        }
        return dchyXmglTjfxMapper.getXmsl(paramMap);
    }

    @Override
    public Page<Map<String, Object>> getXmbajlByPage(Map<String, Object> data) {
        Map<String, Object> paramMap = new HashMap<>();
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(data.get("pageSize") != null ? data.get("pageSize").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);
        String kssj = CommonUtil.ternaryOperator(data.get("kssj"));
        String jssj = CommonUtil.ternaryOperator(data.get("jssj"));
        String chdwmc = CommonUtil.ternaryOperator(data.get("chdwmc"));
        if (StringUtils.isNotBlank(kssj)) {
            paramMap.put("kssj", kssj);
        }
        if (StringUtils.isNotBlank(jssj)) {
            paramMap.put("jssj", jssj);
        }
        if (StringUtils.isNotBlank(chdwmc)) {
            paramMap.put("chdwmc", chdwmc);
        }
        return repository.selectPaging("getXmbajlByPage", paramMap, page - 1, pageSize);
    }

    @Override
    public List<Map<String, Object>> getXmslByqx(Map<String, Object> data) {
        Map<String, Object> paramMap = new HashMap<>();
        String kssj = CommonUtil.ternaryOperator(data.get("kssj"));
        String jssj = CommonUtil.ternaryOperator(data.get("jssj"));
        if (StringUtils.isNotBlank(kssj)) {
            paramMap.put("kssj", kssj);
        }
        if (StringUtils.isNotBlank(jssj)) {
            paramMap.put("jssj", jssj);
        }
        return dchyXmglTjfxMapper.getXmslByQx(paramMap);
    }


    @Override
    public List<Map<String, Object>> getXmslByqxlys(Map<String, Object> data) {
        Map<String, Object> paramMap = new HashMap<>();
        String kssj = CommonUtil.ternaryOperator(data.get("kssj"));
        String jssj = CommonUtil.ternaryOperator(data.get("jssj"));
        if (StringUtils.isNotBlank(kssj)) {
            paramMap.put("kssj", kssj);
        }
        if (StringUtils.isNotBlank(jssj)) {
            paramMap.put("jssj", jssj);
        }
        return dchyXmglTjfxMapper.getXmslByQxlys(paramMap);
    }

    @Override
    public List<Map<String, Object>> getXmWtfsByQx(Map<String, Object> data) {
        Map<String, Object> paramMap = new HashMap<>();
        String kssj = CommonUtil.ternaryOperator(data.get("kssj"));
        String jssj = CommonUtil.ternaryOperator(data.get("jssj"));
        if (StringUtils.isNotBlank(kssj)) {
            paramMap.put("kssj", kssj);
        }
        if (StringUtils.isNotBlank(jssj)) {
            paramMap.put("jssj", jssj);
        }
        return dchyXmglTjfxMapper.getXmwtfsByQx(paramMap);
    }

    @Override
    public List<Map<String, Object>> getXmWtfsByQxlys(Map<String, Object> data) {
        Map<String, Object> paramMap = new HashMap<>();
        String kssj = CommonUtil.ternaryOperator(data.get("kssj"));
        String jssj = CommonUtil.ternaryOperator(data.get("jssj"));
        if (StringUtils.isNotBlank(kssj)) {
            paramMap.put("kssj", kssj);
        }
        if (StringUtils.isNotBlank(jssj)) {
            paramMap.put("jssj", jssj);
        }
        return dchyXmglTjfxMapper.getXmwtfsByQxLy(paramMap);
    }

    @Override
    //发送mq获取数据
    public Map getXmslFromBdst(Map<String, Object> data) {
        DchyXmglTjfxDto dchyXmglTjfxDto = new DchyXmglTjfxDto();
        DchyXmglTjgxFyDto dchyXmglTjgxFyDto = new DchyXmglTjgxFyDto();
        String kssj = CommonUtil.ternaryOperator(data.get("kssj"));
        String jssj = CommonUtil.ternaryOperator(data.get("jssj"));
        String wtkssj = CommonUtil.ternaryOperator(data.get("wtkssj"));
        String wtjssj = CommonUtil.ternaryOperator(data.get("wtjssj"));
        String year = CommonUtil.ternaryOperator(data.get("year"));
        String month = CommonUtil.ternaryOperator(data.get("month"));
        String quarter = CommonUtil.ternaryOperator(data.get("quarter"));//季度
        String jsdwmc = CommonUtil.ternaryOperator(data.get("jsdwmc"));
        String chdwmc = CommonUtil.ternaryOperator(data.get("chdwmc"));
        String xmzt = CommonUtil.ternaryOperator(data.get("xmzt"));
        String exportflag = CommonUtil.ternaryOperator(data.get("exportflag"));
        String pageflag = CommonUtil.ternaryOperator(data.get("pageflag"));
        if (data.get("page") != null && data.get("pageSize") != null) {
            int page = Integer.parseInt(CommonUtil.ternaryOperator(data.get("page")));
            int pageSize = Integer.parseInt(CommonUtil.ternaryOperator(data.get("pageSize")));
            dchyXmglTjgxFyDto.setPage(page);
            dchyXmglTjgxFyDto.setPageSize(pageSize);
            dchyXmglTjfxDto.setDchyXmglTjgxFyDto(dchyXmglTjgxFyDto);
        }
        //推送参数到线上
        String key = UUIDGenerator.generate18();
        dchyXmglTjfxDto.setKey(key);

        if (StringUtils.isNotBlank(kssj)) {
            dchyXmglTjfxDto.setKssj(kssj);
        }
        if (StringUtils.isNotBlank(jssj)) {
            dchyXmglTjfxDto.setJssj(jssj);
        }
        if (StringUtils.isNotBlank(wtkssj)) {
            dchyXmglTjfxDto.setWtkssj(wtkssj);
        }
        if (StringUtils.isNotBlank(wtjssj)) {
            dchyXmglTjfxDto.setWtjssj(wtjssj);
        }
        if (StringUtils.isNotBlank(year)) {
            dchyXmglTjfxDto.setYear(year);
        }
        if (StringUtils.isNotBlank(jsdwmc)) {
            dchyXmglTjfxDto.setJsdwmc(jsdwmc);
        }
        if (StringUtils.isNotBlank(chdwmc)) {
            dchyXmglTjfxDto.setChdwmc(chdwmc);
        }
        dchyXmglTjfxDto.setMonth(month);
        dchyXmglTjfxDto.setQuarter(quarter);
        if (StringUtils.isNotBlank(xmzt)) {
            switch (xmzt) {
                case "1":
                    dchyXmglTjfxDto.setWtzt("待接受");
                    break;
                case "2":
                    dchyXmglTjfxDto.setWtzt("已接受");
                    break;
                case "3":
                    dchyXmglTjfxDto.setXmzt("已备案");
                    break;
                case "4":
                    dchyXmglTjfxDto.setXmzt("已办结");
                    break;
                default:
                    break;
            }
        }
        if (StringUtils.isNotBlank(exportflag)) {
            dchyXmglTjfxDto.setExportflag(exportflag);
        }
        if (StringUtils.isNotBlank(pageflag)) {
            dchyXmglTjfxDto.setPageflag(pageflag);
        }
        pushDataToMqService.pushTjfxMsgToMq(dchyXmglTjfxDto);
        //从缓存中取数据
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> mycache = null;
        try {
            mycache = queryXxData(key);
            if (StringUtils.equals(MapUtils.getString(mycache, "code"), ResponseMessage.CODE.SUCCESS.getCode())) {
                resultMap.put("data", mycache.get("data"));
                resultMap.put("code", MapUtils.getString(mycache, "code"));
                resultMap.put("msg", MapUtils.getString(mycache, "msg"));
            } else {
                resultMap.put("code", MapUtils.getString(mycache, "code"));
                resultMap.put("msg", MapUtils.getString(mycache, "msg"));
            }
        } catch (InterruptedException e) {
            logger.error("错误原因:{}", e);
        }
        return resultMap;
    }

    //获取缓存里的值,当null时,休眠3s再次请求,最多请求10次
    public Map<String, Object> queryXxData(String key) throws InterruptedException {
        Map<String, Object> resultMap = Maps.newHashMap();
        Cache.ValueWrapper valueWrapper = EhcacheUtil.getDataFromEhcache(key);
        for (int i = 0; i < 10; i++) {
            if (null != valueWrapper) {
                DchyXmglTjfxDto mycache = (DchyXmglTjfxDto) valueWrapper.get();
                resultMap.put("data", mycache);
                resultMap.put("code", ResponseMessage.CODE.SUCCESS.getCode());
                resultMap.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
                break;
            } else {
                resultMap.put("code", ResponseMessage.CODE.RESULT_SUBMIT_REQUEST_TIMED_OUT.getCode());
                resultMap.put("msg", ResponseMessage.CODE.RESULT_SUBMIT_REQUEST_TIMED_OUT.getMsg());
                Thread.sleep(3000);
                valueWrapper = EhcacheUtil.getDataFromEhcache(key);
            }
        }
        return resultMap;
    }

}
