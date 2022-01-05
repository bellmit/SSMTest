package cn.gtmap.msurveyplat.server.web.rest;

import cn.gtmap.msurveyplat.common.domain.DchyXtBdbtzdDO;
import cn.gtmap.msurveyplat.server.service.check.CheckService;
import cn.gtmap.msurveyplat.server.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/4/9
 * @description 表单验证
 */
@Api(tags = "表单验证")
@RestController
public class CheckController extends BaseController {
    @Autowired
    private CheckService checkService;

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param gzldyid 工作流定义id
     * @param gzljdid 工作流节点id
     * @return 表单必填项验证
     * @description 表单必填项验证
     * */
    @ApiOperation(value ="表单必填项验证")
    @ApiImplicitParams({@ApiImplicitParam(name = "gzldyid", value = "工作流定义id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "gzljdid", value = "工作流节点id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "xmid", value = "项目id", paramType = "query", dataType = "string")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/rest/v1.0/check/checkbdbtx")
    public String checkBdbtx(String gzldyid, String gzljdid, String xmid) {
        return checkService.checkBdbtx(gzldyid,gzljdid,xmid);
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param gzldyid 工作流定义id
     * @param gzljdid 工作流节点id
     * @return 获取表单必填字段
     * @description 获取表单必填字段
     * */
    @ApiOperation(value ="获取表单必填字段")
    @ApiImplicitParams({@ApiImplicitParam(name = "gzldyid", value = "工作流定义id", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "gzljdid", value = "工作流节点id", required = true, paramType = "path", dataType = "string")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/check/bdbtzd/{gzldyid}/{gzljdid}")
    public Map<String,List<DchyXtBdbtzdDO>> getBtxZd(@PathVariable("gzldyid") String gzldyid, @PathVariable("gzljdid") String gzljdid) {
        return checkService.getBtxZd(gzldyid, gzljdid);
    }
}
