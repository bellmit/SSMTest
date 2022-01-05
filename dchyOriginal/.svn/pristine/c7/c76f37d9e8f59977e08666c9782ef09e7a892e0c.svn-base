package cn.gtmap.msurveyplat.server.web.rest.event;

import cn.gtmap.msurveyplat.server.service.event.WorkflowEventService;
import cn.gtmap.msurveyplat.server.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/27
 * @description 工作流事件
 */
@RestController
@Api(tags = "工作流事件")
@RequestMapping("/rest/v1.0/event")
public class WorkflowEventController extends BaseController {
    @Autowired
    private WorkflowEventService workflowEventService;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmid 项目ID
     * @return
     * @description 办结事件
     */
    @ApiOperation(value ="办结事件")
    @ApiImplicitParams({@ApiImplicitParam(name = "xmid", value = "项目ID", required = true, paramType = "query", dataType = "string")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/complete")
    public void completeProject(String xmid) {
        workflowEventService.completeProject(xmid);
    }


}
