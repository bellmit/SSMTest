package cn.gtmap.msurveyplat.server.service.event.impl;

import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglXmService;
import cn.gtmap.msurveyplat.server.service.event.WorkflowEventService;
import cn.gtmap.msurveyplat.server.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/27
 * @description 工作流事件服务
 */
@Service
public class WorkflowEventServiceImpl implements WorkflowEventService {
    @Autowired
    private DchyCgglXmService dchyCgglXmService;

    @Override
    public void completeProject(String xmid) {
        DchyCgglXmDO dchyCgglXmDO = dchyCgglXmService.getDchyCgglXmDOByXmid(xmid);
        if(dchyCgglXmDO != null) {
            dchyCgglXmDO.setXmzt(Constants.XMZT_YBJ_DM);
            dchyCgglXmService.saveDchyCgglXm(dchyCgglXmDO);
        }
    }

}
