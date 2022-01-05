package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.serviceol.core.service.TaskPerformerFilterService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.vo.PfTaskVo;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/9/21
 * @description
 */
public class TaskPerformerTurnBackFilterServiceImpl implements TaskPerformerFilterService {

    private SysTaskService sysTaskService;

    public void setSysTaskService(SysTaskService sysTaskService) {
        this.sysTaskService = sysTaskService;
    }

    @Override
    public List<PfUserVo> getTaskPerformers(String wiid, String targetActivityDefinitionId, PfTaskVo sourceTask, List<PfUserVo> sourceUsers) {
        List<PfTaskVo> historyTaskList =  sysTaskService.getHistoryTaskListByDefineId(wiid,targetActivityDefinitionId);
        if(CollectionUtils.isEmpty(historyTaskList))
            return Lists.newArrayList();
        //使用Set过滤掉重复的用户
        List targetTaskPerformers = Lists.newArrayList();
        Set taskPerfomerIds = Sets.newHashSet();
        for(PfTaskVo historyTask:historyTaskList){
            if(!taskPerfomerIds.contains(historyTask.getUserVo().getUserId())) {
                taskPerfomerIds.add(historyTask.getUserVo().getUserId());
                targetTaskPerformers.add(historyTask.getUserVo());
            }
        }
        return targetTaskPerformers;
    }

}
