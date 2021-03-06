package cn.gtmap.msurveyplat.serviceol.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhdw;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhxxPz;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.serviceol.service.MessageReminderService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
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

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-11-28
 * description
 */

@Service
public class MessageReminderServiceImpl implements MessageReminderService {
    @Autowired
    Repository repository;
    @Autowired
    EntityMapper entityMapper;
    private Logger logger = LoggerFactory.getLogger(MessageReminderServiceImpl.class);

    @Override
    public Page<Map<String, Object>> getMessageByPage(Map<String, Object> param) {
        Page<Map<String, Object>> resultList = null;
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            String dqzt = CommonUtil.formatEmptyValue(data.get("dqzt"));
            String jssjq = CommonUtil.formatEmptyValue(data.get("jssjq"));
            String jssjz = CommonUtil.formatEmptyValue(data.get("jssjz"));
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("dqzt", dqzt);
            paramMap.put("jssjq", jssjq);
            paramMap.put("jssjz", jssjz);
            int page = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("page"), Constants.DCHY_XMGL_PAGINATION_PAGE));
            int pageSize = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("pageSize"), Constants.DCHY_XMGL_PAGINATION_SIZE));
            /*????????????????????????*/
            String userid = UserUtil.getCurrentUserId();
            Example yhdwExample = new Example(DchyXmglYhdw.class);
            yhdwExample.createCriteria().andEqualTo("yhid", userid);
            List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(yhdwExample);
            if (CollectionUtils.isNotEmpty(yhdwList)) {
                String dwbh = yhdwList.get(0).getDwbh();
                String yhlx = yhdwList.get(0).getYhlx(); //1????????????
                if (StringUtils.equals(yhlx, Constants.DCHY_XMGL_YHLX_JSDW)) {
                    String dwmc = yhdwList.get(0).getDwmc();
                    paramMap.put("mlkid", dwmc);
                    if (StringUtils.isNotBlank(dwmc)) {
                        resultList = repository.selectPaging("queryYhxxByMlkidByPage", paramMap, page - 1, pageSize);

                    }
                } else if (StringUtils.equals(yhlx, Constants.DCHY_XMGL_YHLX_CHDW)) {//2????????????
                    Example mlkExample = new Example(DchyXmglMlk.class);
                    mlkExample.createCriteria().andEqualTo("dwbh", dwbh);
                    List<DchyXmglMlk> mlkList = entityMapper.selectByExample(mlkExample);
                    if (CollectionUtils.isNotEmpty(mlkList)) {
                        String mlkid = mlkList.get(0).getMlkid();
                        paramMap.put("mlkid", mlkid);
                        if (StringUtils.isNotBlank(mlkid)) {
                            resultList = repository.selectPaging("queryYhxxByMlkidByPage", paramMap, page - 1, pageSize);
                        }
                    }
                }
                if (resultList != null) {
                    for (Map<String, Object> resultLists : resultList) {
                        if (StringUtils.isNotBlank(MapUtils.getString(resultLists, "XXLX"))) {
                            String xxlx = MapUtils.getString(resultLists, "XXLX");

                            Example example = new Example(DchyXmglYhxxPz.class);
                            example.createCriteria().andEqualTo("xxlx", xxlx);
                            List<DchyXmglYhxxPz> xxtzList = entityMapper.selectByExampleNotNull(example);
                            if (CollectionUtils.isNotEmpty(xxtzList)) {
                                for (DchyXmglYhxxPz xxtzLists : xxtzList) {
                                    resultLists.put("XXLX", xxtzLists.getXxlx());
                                    resultLists.put("XXSXMC", xxtzLists.getXxsxmc());
                                    resultLists.put("TZURL", xxtzLists.getTzurl());
                                    resultLists.put("SFTZ", xxtzLists.getSftz());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean changeStatus(Map<String, Object> data) {
        boolean flag = true;
        try {
            List<String> yhxxidlist = (List<String>) data.get("yhxxidlist");
            if (CollectionUtils.isNotEmpty(yhxxidlist)) {
                if (yhxxidlist.size() > 1) {//????????????
                    for (String xxid : yhxxidlist) {
                        if (StringUtils.isNotBlank(xxid)) {
                            DchyXmglYhxx dchyXmglYhxx = entityMapper.selectByPrimaryKey(DchyXmglYhxx.class, xxid);
                            if (null != dchyXmglYhxx) {
                                dchyXmglYhxx.setDqzt(Constants.VALID);//????????????????????????
                                dchyXmglYhxx.setDqsj(CalendarUtil.getCurHMSDate());//?????????????????????????????????
                                int i = entityMapper.saveOrUpdate(dchyXmglYhxx, xxid);
                                if (i <= 0) {
                                    flag = false;
                                    break;
                                }
                            }
                        }
                    }
                } else {//????????????
                    DchyXmglYhxx dchyXmglYhxx = entityMapper.selectByPrimaryKey(DchyXmglYhxx.class, yhxxidlist.get(0));
                    if (null != dchyXmglYhxx) {
                        dchyXmglYhxx.setDqzt(Constants.VALID);//????????????????????????
                        dchyXmglYhxx.setDqsj(CalendarUtil.getCurHMSDate());//?????????????????????????????????
                        int i = entityMapper.saveOrUpdate(dchyXmglYhxx, yhxxidlist.get(0));
                        if (i <= 0) {
                            flag = false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("????????????:{}", e);
        }
        return flag;
    }


}
