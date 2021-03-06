package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxm;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMqTswj;
import cn.gtmap.msurveyplat.promanage.core.service.mq.receive.ReceiveXsxmwjFromBsdtServiceImpl;
import cn.gtmap.msurveyplat.promanage.service.FileUploadCheckService;
import com.google.common.base.Charsets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2020/2/27
 * @description
 */
@Service
public class FileUploadCheckServiceImpl implements FileUploadCheckService {

    @Autowired
    EntityMapper entityMapper;

    @Autowired
    ReceiveXsxmwjFromBsdtServiceImpl receiveXsxmwjFromBsdtServiceImpl;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void fileUploadCheck() {
        try {
            Example exampleTswj = new Example(DchyXmglMqTswj.class);
            List<DchyXmglMqTswj> tswjList = entityMapper.selectByExample(exampleTswj);
            if (CollectionUtils.isNotEmpty(tswjList)) {
                for (DchyXmglMqTswj tswjLists : tswjList) {
                    String xqfbbh = tswjLists.getXqfbbh();
                    if (StringUtils.isNotBlank(xqfbbh)) {
                        Example exampleChxm = new Example(DchyXmglChxm.class);
                        exampleChxm.createCriteria().andEqualTo("xqfbbh", xqfbbh);
                        List<DchyXmglChxm> chxmList = entityMapper.selectByExampleNotNull(exampleChxm);
                        if (CollectionUtils.isNotEmpty(chxmList)) {
                            String wjnr = new String(tswjLists.getWjnr(), Charsets.UTF_8);
                            receiveXsxmwjFromBsdtServiceImpl.saveOrDeleteWjData(wjnr);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("????????????:{}", e);
        }
    }

}
