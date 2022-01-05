package cn.gtmap.msurveyplat.portalol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.portalol.core.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/5 13:42
 * @description
 */
@Service("fileUpload")
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private EntityMapper entityMapper;


    @Override
    public int updateSjclByClass(DchyXmglSjcl object, String key) {
        return entityMapper.saveOrUpdate(object, key);
    }


    @Override
    public DchyXmglSjcl querySjclBySjclId(Class clazz, String sjclId) {
        return (DchyXmglSjcl) entityMapper.selectByPrimaryKey(clazz, sjclId);
    }

    @Override
    public DchyXmglMlk queryMlkBySjclId(Class clazz, String sjclId) {
        DchyXmglMlk dchyXmglMlk = (DchyXmglMlk) entityMapper.selectByPrimaryKey(clazz, sjclId);
        DataSecurityUtil.decryptSingleObject(dchyXmglMlk);
        return dchyXmglMlk;
    }

    @Override
    public int updateMlkByClass(DchyXmglMlk obj, String key) {
        DataSecurityUtil.decryptSingleObject(obj);
        return entityMapper.saveOrUpdate(obj, key);
    }
}
