package cn.gtmap.msurveyplat.server.web.rest.config;

import cn.gtmap.msurveyplat.common.domain.DchyXtMryjDO;
import cn.gtmap.msurveyplat.common.domain.ProcessDefData;
import cn.gtmap.msurveyplat.common.dto.UserTaskDto;
import cn.gtmap.msurveyplat.server.core.service.DchyXtMryjService;
import cn.gtmap.msurveyplat.server.service.feign.ExchangeSignService;
import cn.gtmap.msurveyplat.server.web.BaseController;
import feign.Param;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/4/27
 * @description 默认意见配置
 */
@Api(value="默认意见配置",description = "默认意见配置功能",tags="DchyCgglMryjController")
@RestController
@RequestMapping("/rest/v1.0/mryj")
public class DchyCgglMryjController extends BaseController {
    @Autowired
    private DchyXtMryjService dchyXtMryjService;

    @Autowired
    private ExchangeSignService exchangeSignService;

    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "gzlmc", value = "工作流名称", required = true, paramType = "path", dataType = "string"),
                        @ApiImplicitParam(name = "gzljdmc", value = "工作流节点名称", paramType = "path", dataType = "string")})
    @PostMapping(value = "yjpzList")
    @ResponseBody
    public List<DchyXtMryjDO> getYjpz(String gzldyid,String gzljdid){
        Map<String,String> param = new HashMap<>(5);
        if (StringUtils.isNotBlank(gzldyid)) {
            param.put("gzldyid", gzldyid);
        }

        if (StringUtils.isNotBlank(gzljdid)) {
            param.put("gzljdid", gzljdid);
        }
        List<DchyXtMryjDO> yjpzList = dchyXtMryjService.getYjpz(param);
        return yjpzList;
    }

    @ApiOperation(value="增加默认意见", notes="增加默认意见")
    @PostMapping(value = "insertMryj")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public String insertMryj(DchyXtMryjDO dchyXtMryjDO){
        String string = "success";
        int flag = dchyXtMryjService.addMryj(dchyXtMryjDO);
        if(flag == 0){
            string = "error";
        }
        return string;
    }

    @ApiOperation(value="批量删除默认意见", notes="批量删除默认意见")
    @ResponseBody
    @PostMapping(value = "delAllMryj")
    public String delMryj(@RequestBody List<String> yjidList){
        String string = "success";
        int flag = dchyXtMryjService.delMryj(yjidList);
        if(flag == 0){
            string = "error";
        }
        return string;
    }

    @ApiOperation(value="删除默认意见", notes="删除默认意见")
    @ApiImplicitParam(name = "yjid", value = "意见id", paramType = "query", dataType = "string")
    @ResponseBody
    @PostMapping(value = "delMryjByYjid")
    public String delMryjByYjid(String yjid){
        String string = "success";
        int flag = 0;
        try {
            flag = dchyXtMryjService.delMryjByYjid(yjid);
        } catch (Exception e) {
            LOGGER.error("DchyCgglMryjController.delMryjByYjid",e);
        }
        if(flag == 0){
            string = "error";
        }
        return string;
    }

    @ApiOperation(value="修改默认意见", notes="修改默认意见")
    @ResponseBody
    @PostMapping(value = "updMryjByYjid")
    public String updMryjByYjid(DchyXtMryjDO dchyXtMryjDO ){
        String string = "success";
        int flag = 0;
        try {
            flag = dchyXtMryjService.updMryjByYjid(dchyXtMryjDO);
        } catch (Exception e) {
            LOGGER.error("DchyCgglMryjController.updMryjByYjid",e);
        }
        if(flag == 0){
            string = "error";
        }
        return string;
    }

    /**
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     * @param
     * @return
     * @description 获取工作流信息列表
     */
    @ApiOperation(value = "获取工作流信息列表")
    @GetMapping(value = "process/list")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ProcessDefData> getWorkflowDefinitionList() {
        return exchangeSignService.getWorkflowDefinitionList();
    }

    /**
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     * @param
     * @return
     * @description 获取节点信息列表
     */
    @ApiOperation(value = "获取节点信息列表")
    @PostMapping(value = "process/jdlist")
    @ApiImplicitParams({@ApiImplicitParam(name = "gzldyid", value = "工作流定义ID", required = true, paramType = "path", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    public List<UserTaskDto> getWorkflowDefinitionListBydyid(@Param("gzldyid") String gzldyid) {
        return exchangeSignService.getWorkflowDefinitionListByid(gzldyid);
    }

    @ApiOperation(value="获取意见id", notes="根据文本框的节点名称和工作流名称")
    @ApiImplicitParams({@ApiImplicitParam(name = "gzlmc", value = "工作流名称", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "gzljdmc", value = "工作流节点名称", paramType = "path", dataType = "string")})
    @PostMapping(value = "obtainYjid")
    @ResponseStatus(code = HttpStatus.OK)
    public String obtainYjid(String gzlmc,String gzljdmc){
        String yjid = null ;
        Map<String,String> param = new HashMap<>(5);
        if (StringUtils.isNotBlank(gzlmc)) {
            param.put("gzlmc", gzlmc);
        }

        if (StringUtils.isNotBlank(gzljdmc)) {
            param.put("gzljdmc", gzljdmc);
        }
        yjid = dchyXtMryjService.getYjid(param);
        return yjid;
    }

}
