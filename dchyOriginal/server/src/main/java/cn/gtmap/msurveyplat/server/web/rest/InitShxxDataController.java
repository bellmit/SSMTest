package cn.gtmap.msurveyplat.server.web.rest;

import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.DchyCgglShxxDO;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglShxxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
  * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
  * @description 审核信息接口
  */
@RestController
@Api(tags = "审核信息接口")
@RequestMapping("/v1.0/initshxx")
public class InitShxxDataController {

    @Autowired
    private DchyCgglShxxService dchyCgglShxxService;
    @Autowired
    Repository repository;

    @ApiOperation(value ="新增审核信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "dchyCgglShxxDO", value = "新增审核信息", required = true, dataType = "DchyCgglShxxDO")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/saveShxx")
    public void saveShxx(@RequestBody DchyCgglShxxDO dchyCgglShxxDO) {
        if (dchyCgglShxxDO!=null) {
           dchyCgglShxxService.saveDchyCgglShxx(dchyCgglShxxDO);
        }
    }
/*
    @ApiOperation(value ="查询审核信息")
    @ApiImplicitParam(paramType="query",name="xmid",dataType="String",required=true,value="查询审核信息")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/queryShxx")
    @ResponseBody
    public DchyCgglShxxDO queryShxx(@RequestParam(value="xmid",required=false) String xmid) {
        if (StringUtils.isNotBlank(xmid)) {
           return dchyCgglShxxService.getDchyCgglShxxListByXmid(xmid);
        }
        return null;
    }*/





}
