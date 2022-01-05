package cn.gtmap.msurveyplat.promanage.core.service.impl;



import cn.gtmap.msurveyplat.promanage.core.service.TaskPerformerFilterService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/9/21
 * @description
 */
@Service
public class TaskPerformerFilterServiceContext {
    public static final String USE_TURN_BACK_PERFORMER = "turnBack";
    private Map<String,TaskPerformerFilterService> taskPerformerFilterServiceMap;

    public void setTaskPerformerFilterServiceMap(Map<String, TaskPerformerFilterService> taskPerformerFilterServiceMap) {
        this.taskPerformerFilterServiceMap = taskPerformerFilterServiceMap;
    }

    public TaskPerformerFilterService getTaskPerformerFilterServiceByName(String name){
        return taskPerformerFilterServiceMap.containsKey(name)?taskPerformerFilterServiceMap.get(name):null;
    }
}
