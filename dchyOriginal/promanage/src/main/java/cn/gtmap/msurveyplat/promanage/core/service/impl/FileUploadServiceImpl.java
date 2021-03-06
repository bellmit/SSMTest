package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglHtxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglSjclpzMapper;
import cn.gtmap.msurveyplat.promanage.core.service.FileUploadService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.utils.PlatformUtil;
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
        int result = entityMapper.saveOrUpdate(object, key);
        return result;
    }

    @Override
    public int updateHtxxByClass(DchyXmglHtxx object, String key) {
        int result = entityMapper.saveOrUpdate(object, key);
        return result;
    }

    @Override
    public DchyXmglSjcl querySjclBySjclId(Class clazz, String sjclId) {
        return (DchyXmglSjcl) entityMapper.selectByPrimaryKey(clazz, sjclId);
    }

    @Override
    public DchyXmglHtxx queryHtxxByHtxxId(Class clazz, String htxxId) {
        return (DchyXmglHtxx) entityMapper.selectByPrimaryKey(clazz, htxxId);
    }

    @Override
    public DchyXmglMlk queryMlkBySjclId(Class clazz, String sjclId) {
        return (DchyXmglMlk) entityMapper.selectByPrimaryKey(clazz, sjclId);
    }

    @Override
    public int updateMlkByClass(DchyXmglMlk obj, String key) {
        return entityMapper.saveOrUpdate(obj, key);
    }

    @Override
    public int deleteSjclById(String sjclId) {
        return entityMapper.deleteByPrimaryKey(DchyXmglSjcl.class, sjclId);
    }

    /**
     * ??????????????????????????????
     *
     * @param paramMap Map<String,Object>
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> deleteFileJl(Map<String, Object> paramMap) {
        Map<String, Object> result = Maps.newHashMap();
        String msg = ResponseMessage.CODE.SAVE_FAIL.getMsg();
        String code = ResponseMessage.CODE.SAVE_FAIL.getCode();
        try {
            List<Map<String, Object>> data = (List<Map<String, Object>>) paramMap.get("data");
            if (CollectionUtils.isNotEmpty(data)) {
                for (Map<String, Object> item : data) {
                    String sjclId = CommonUtil.formatEmptyValue(item.get("sjclid"));
                    String wjzxId = CommonUtil.formatEmptyValue(item.get("wjzxid"));
                    if (StringUtils.isNotBlank(wjzxId)) {
                        platformUtil.deleteNodeById(Integer.parseInt(wjzxId));
                    }
                    if (StringUtils.isNotBlank(sjclId)) {
                        //????????????????????????
                        entityMapper.deleteByPrimaryKey(DchyXmglSjcl.class, sjclId);
                        // ??????????????????

                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            msg = "????????????";
            code = ResponseMessage.CODE.DELETE_FAIL.getCode();
        }
        result.put("msg", msg);
        result.put("code", code);
        return result;
    }
}
