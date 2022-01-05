package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjclpz;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglSjclpzMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.FileUploadService;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/5 13:42
 * @description
 */
@Service("fileUpload")
public class FileUploadServiceImpl implements FileUploadService {

    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    PlatformUtil platformUtil;
    @Autowired
    PushDataToMqService pushDataToMqService;
    @Autowired
    private DchyXmglSjclpzMapper dchyXmglSjclpzMapper;

    @Override
    public int updateSjclByClass(DchyXmglSjcl object, String key) {
        return entityMapper.saveOrUpdate(object, key);
    }

    @Override
    public <T> T queryDataById(Class<T> t, String sjclId) {
        return (T) entityMapper.selectByPrimaryKey(t, sjclId);
    }

    @Override
    public int deleteSjclById(String sjclId) {
        return entityMapper.deleteByPrimaryKey(DchyXmglSjcl.class, sjclId);
    }

    @Override
    public Map<String, Object> deleteFileJl(Map<String, Object> paramMap) {
        Map<String, Object> result = Maps.newHashMap();
        String msg = ResponseMessage.CODE.SAVE_FAIL.getMsg();
        String code = ResponseMessage.CODE.SAVE_FAIL.getCode();
        try {
            List<Map<String, Object>> data = (List<Map<String, Object>>) paramMap.get("data");
            if (CollectionUtils.isNotEmpty(data)) {
                for (Map<String, Object> datas : data) {
                    String sjclid = CommonUtil.formatEmptyValue(datas.get("sjclid"));
                    String sjclpzid = CommonUtil.formatEmptyValue(datas.get("sjclpzid"));
                    String wjzxid = CommonUtil.formatEmptyValue(datas.get("wjzxid"));
                    if (StringUtils.isNotBlank(wjzxid)) {
                        platformUtil.deleteNodeById(Integer.parseInt(wjzxid));
                    }
                    if (StringUtils.isNotBlank(sjclid)) {

                        DchyXmglSjcl xmglSjcl = entityMapper.selectByPrimaryKey(DchyXmglSjcl.class, sjclid);
                        if (null != xmglSjcl) {
                            /*删除对应收件材料*/
                            entityMapper.delete(xmglSjcl);
                        }
                    }
                    if (StringUtils.isNotBlank(sjclpzid)) {
                        DchyXmglSjclpz xmglSjclpz = entityMapper.selectByPrimaryKey(DchyXmglSjclpz.class, sjclpzid);
                        if (null != xmglSjclpz) {
                            /*删除对应收件材料*/
                            entityMapper.delete(xmglSjclpz);
                            dchyXmglSjclpzMapper.dropSsclsx(xmglSjclpz.getSjclpzid());
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            msg = "删除失败";
            code = ResponseMessage.CODE.DELETE_FAIL.getCode();
        }
        result.put("msg", msg);
        result.put("code", code);
        return result;
    }
}
