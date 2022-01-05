package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhxxPz;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.MessagePushService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessagePushServiceImpl implements MessagePushService {
    @Autowired
    private EntityMapper entityMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePushServiceImpl.class);

    @Override
    public int updateYhxxInfo(Map<String, Object> paramMap) {
        int result = Constants.RETURN_FAIL;
        try {
            //用户消息
            String yhxxid = UUIDGenerator.generate18();
            String fsyhid = UserUtil.getCurrentUserId();
            String yhxxpzid = MapUtils.getString(paramMap, "yhxxpzid");
            String jsyhid = MapUtils.getString(paramMap, "jsyhid");//接受用户id
            String glsxid = MapUtils.getString(paramMap, "glsxid");

            DchyXmglYhxx dchyXmglYhxx = new DchyXmglYhxx();
            dchyXmglYhxx.setYhxxid(yhxxid);
            dchyXmglYhxx.setFsyhid(fsyhid);
            dchyXmglYhxx.setFssj(CalendarUtil.getCurHMSDate());
            dchyXmglYhxx.setDqzt(Constants.INVALID);//未读
            dchyXmglYhxx.setJsyhid(jsyhid);
            dchyXmglYhxx.setGlsxid(glsxid);

            Example example = new Example(DchyXmglYhxxPz.class);
            List<DchyXmglYhxxPz> xxnrzd = Lists.newArrayList();
            if (StringUtils.equals(yhxxpzid, Constants.DCHY_XMGL_ZD_XXNR_MLKRZ_TG)) {
                example.createCriteria().andEqualTo("id", Constants.DCHY_XMGL_ZD_XXNR_MLKRZ_TG);
                xxnrzd = entityMapper.selectByExampleNotNull(example);
            } else if (StringUtils.equals(yhxxpzid, Constants.DCHY_XMGL_ZD_XXNR_MLKRZ_BTG)) {
                example.createCriteria().andEqualTo("id", Constants.DCHY_XMGL_ZD_XXNR_MLKRZ_BTG);
                xxnrzd = entityMapper.selectByExampleNotNull(example);
            } else if (StringUtils.equals(yhxxpzid, Constants.DCHY_XMGL_ZD_XXNR_JSDWPJ)) {
                example.createCriteria().andEqualTo("id", Constants.DCHY_XMGL_ZD_XXNR_JSDWPJ);
                xxnrzd = entityMapper.selectByExampleNotNull(example);
                for (DchyXmglYhxxPz xxnrzds : xxnrzd) {
                    String xmmc = MapUtils.getString(paramMap, "xmmc");
                    String jsdwmc = MapUtils.getString(paramMap, "jsdwmc");
                    String xxnr = xxnrzds.getXxnr().replaceAll("项目名称", CommonUtil.ternaryOperator(xmmc, StringUtils.EMPTY)).replaceAll("建设单位名称", CommonUtil.ternaryOperator(jsdwmc, StringUtils.EMPTY));
                    xxnrzds.setXxnr(xxnr);
                }
            } else if (StringUtils.equals(yhxxpzid, Constants.DCHY_XMGL_ZD_XXNR_GLDWPJ)) {
                example.createCriteria().andEqualTo("id", Constants.DCHY_XMGL_ZD_XXNR_GLDWPJ);
                xxnrzd = entityMapper.selectByExampleNotNull(example);
            } else if (StringUtils.equals(yhxxpzid, Constants.DCHY_XMGL_ZD_XXNR_MLKYC)) {
                example.createCriteria().andEqualTo("id", Constants.DCHY_XMGL_ZD_XXNR_MLKYC);
                xxnrzd = entityMapper.selectByExampleNotNull(example);
                for (DchyXmglYhxxPz xxnrzds : xxnrzd) {
                    String xxnr = xxnrzds.getXxnr().replaceAll("移出时间", CalendarUtil.getCurHMSStrDate());
                    xxnrzds.setXxnr(xxnr);
                }
            }

            if (CollectionUtils.isNotEmpty(xxnrzd)) {
                for (DchyXmglYhxxPz xxnrzds : xxnrzd) {
                    String xxnr = xxnrzds.getXxnr();
                    String xxlx = xxnrzds.getXxlx();
                    String sftz = xxnrzds.getSftz();
                    dchyXmglYhxx.setXxnr(xxnr); //消息内容
                    dchyXmglYhxx.setXxlx(xxlx); //消息类型事项
                    dchyXmglYhxx.setSftz(sftz); //是否跳转
                }
            }

            entityMapper.saveOrUpdate(dchyXmglYhxx, dchyXmglYhxx.getYhxxid());
            result = Constants.RETURN_SUCCESS;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            result = Constants.RETURN_FAIL;
        }
        return result;
    }
}
