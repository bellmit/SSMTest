package cn.gtmap.msurveyplat.serviceol.service.impl;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repo;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhdw;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.service.DchyXmglYhdwService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class DchyXmglYhdwServiceImpl implements DchyXmglYhdwService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DchyXmglYhdwServiceImpl.class);

    @Resource(name = "entityMapper")
    EntityMapper entityMapper;

    @Resource(name = "repository")
    private Repo repository;

    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_YHZC_MC, czmkCode = ProLog.CZMC_YHZC_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_YHZC_CODE)
    public DchyXmglYhdw getDchyXmglYhdwByUserId(String userid) {
        if (StringUtils.isNotBlank(userid)) {
            Example example = new Example(DchyXmglYhdw.class);
            example.createCriteria().andEqualTo("yhid", userid);
            List<DchyXmglYhdw> dchyXmglYhdwList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglYhdwList)) {
                DchyXmglYhdw dchyXmglYhdw = dchyXmglYhdwList.get(0);
                DataSecurityUtil.decryptSingleObject(dchyXmglYhdw);
                return dchyXmglYhdw;
            }
        }
        return null;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_YHZC_MC, czmkCode = ProLog.CZMC_YHZC_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_YHZC_CODE)
    public Page<Map<String, Object>> queryYhdwUserByPage(Map<String, Object> paramdata) {
        int page = Integer.parseInt(paramdata.get("page") != null ? CommonUtil.formatEmptyValue(paramdata.get("page")) : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int size = Integer.parseInt(paramdata.get("size") != null ? CommonUtil.formatEmptyValue(paramdata.get("size")) : Constants.DCHY_XMGL_PAGINATION_SIZE);
        Map<String, String> param = Maps.newHashMap();
        String username = CommonUtil.formatEmptyValue(MapUtils.getString(paramdata, "username"));
        String yhmc = CommonUtil.formatEmptyValue(MapUtils.getString(paramdata, "yhmc"));
        String lxr = CommonUtil.formatEmptyValue(MapUtils.getString(paramdata, "lxr"));
        String yhzjhm = CommonUtil.formatEmptyValue(MapUtils.getString(paramdata, "yhzjhm"));
        String isvalid = CommonUtil.formatEmptyValue(MapUtils.getString(paramdata, "isvalid"));
        Example example = new Example(DchyXmglYhdw.class);
        example.createCriteria().andEqualTo("yhid", UserUtil.getCurrentUserId() /*UserUtil.getCurrentUserId()*/);
        List<DchyXmglYhdw> dchyXmglYhdws = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(dchyXmglYhdws)) {
            DchyXmglYhdw dchyXmglYhdw = dchyXmglYhdws.get(0);
            param.put("dwbh", dchyXmglYhdw.getDwbh());
            param.put("yhlx", dchyXmglYhdw.getYhlx());
        }
        if (StringUtils.isNotBlank(username)) {
            param.put("yhmc", username);
        } else if (StringUtils.isNotBlank(yhmc)) {
            param.put("yhmc", yhmc);
        }
        if (StringUtils.isNotBlank(lxr)) {
            param.put("lxr", lxr);
        }
        if (StringUtils.isNotBlank(isvalid)) {
            param.put("isvalid", isvalid);
        }
        if (StringUtils.isNotBlank(yhzjhm)) {
            param.put("yhzjhm", yhzjhm);
        }
        Page<Map<String, Object>> mapPage = repository.selectPaging("queryUserByPage", param, page - 1, size);
        LOGGER.info("**********解密前的数据************" + JSON.toJSONString(mapPage.getContent()));
        DataSecurityUtil.decryptMapList(mapPage.getContent());
        LOGGER.info("**********解密后的数据************" + JSON.toJSONString(mapPage.getContent()));
        return mapPage;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_YHZC_MC, czmkCode = ProLog.CZMC_YHZC_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_YHZC_CODE)
    public Map<String, String> changeUserState(String userid, String state) {
        Map resultMap = Maps.newHashMap();
        if (StringUtils.isNoneBlank(userid, state)) {
            Example example = new Example(DchyXmglYhdw.class);
            example.createCriteria().andEqualTo("yhid", userid);
            List<DchyXmglYhdw> yhdwList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(yhdwList)) {
                DchyXmglYhdw dchyXmglYhdw = new DchyXmglYhdw();
                dchyXmglYhdw.setIsvalid(state);
                entityMapper.updateByExampleSelective(dchyXmglYhdw, example);
                resultMap.put("code", ResponseMessage.CODE.SUCCESS.getCode());
                resultMap.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
            } else {
                resultMap.put("code", ResponseMessage.CODE.USERNOTEXIST_FAIL.getCode());
                resultMap.put("msg", ResponseMessage.CODE.USERNOTEXIST_FAIL.getMsg());
            }
        } else {
            resultMap.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            resultMap.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return resultMap;
    }
}
