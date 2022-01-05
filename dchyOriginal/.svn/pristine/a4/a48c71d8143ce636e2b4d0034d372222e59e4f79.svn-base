package cn.gtmap.msurveyplat.server.web.rest;

import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.DchyCgglGcjsspxxDo;
import cn.gtmap.msurveyplat.common.dto.ShxxParamDTO;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;
import cn.gtmap.msurveyplat.server.service.shxx.InitShxxFormService;
import cn.gtmap.msurveyplat.server.web.BaseController;
import groovyjarjarcommonscli.MissingArgumentException;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
  * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
  * @description 审核信息接口
  */
@RestController
@Api(tags = "审核信息接口")
@RequestMapping("/rest/v1.0/shxx")
public class ShxxDataController extends BaseController {

    @Autowired
    private InitShxxFormService initShxxFormService;


   /* @ApiOperation(value ="新增审核信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "dchyCgglShxxDO", value = "新增审核信息", required = true, dataType = "DchyCgglShxxDO")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/saveShxx")
    public void saveShxx(@RequestBody DchyCgglShxxDO dchyCgglShxxDO) {
        if (dchyCgglShxxDO!=null) {
           dchyCgglShxxService.saveDchyCgglShxx(dchyCgglShxxDO);
        }
    }

    @ApiOperation(value ="查询审核信息")
    @ApiResponses(value = {@ApiResponse(code = 200,message = "请求获取成功"),@ApiResponse(code = 500,message = "请求参数错误")})
    @ApiImplicitParam(paramType="query",name="xmid",dataType="String",required=true,value="查询审核信息")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/queryShxx")
    @ResponseBody
    public DchyCgglShxxDO queryShxx(@RequestParam(value="xmid",required=true) String xmid) {
        return dchyCgglShxxService.queryDchyCgglShxxByXmid(xmid);
    }*/





    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param shxxid 审核信息ID
     * @return
     * @description 删除签名信息
     */
    @ApiOperation("删除签名信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @DeleteMapping(value = "/sign/{shxxid}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiImplicitParams(@ApiImplicitParam(name = "shxxid", value = "审核信息 ID", dataType = "string", paramType = "path"))
    public ShxxVO deleteSign(@PathVariable(name = "shxxid") String shxxid) throws MissingArgumentException {
        if (StringUtils.isBlank(shxxid)) {
            throw new MissingArgumentException("审核信息ID缺失!");
        }
        return initShxxFormService.deleteSign(shxxid);
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 获取签名意见
     **/
    @ApiOperation("获取签名意见")
    @PostMapping(value = "/list")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiImplicitParams({@ApiImplicitParam(name = "shxxParamDTO", value = "审核信息参数对象", required = true, dataType = "ShxxParamDTO")})
    @ResponseBody
    public List<ShxxVO> listShxx(@RequestBody ShxxParamDTO shxxParamDTO) {
        return initShxxFormService.init(shxxParamDTO);
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 更新审核签名信息数据
     **/
    @PutMapping(value = "/sign")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("更新审核签名信息数据")
    @ApiImplicitParams(@ApiImplicitParam(name = "shxxParamDTO", value = "审核信息参数对象", dataType = "ShxxParamDTO", paramType = "body"))
    public ShxxVO updateSign(@RequestBody ShxxParamDTO shxxParamDTO) {
        ShxxVO shxxVO = initShxxFormService.update(shxxParamDTO);
        return shxxVO;
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description
     */
    @ApiOperation("插入或者更新工程建设审批审核信息")
    @PostMapping(value = "/gcjssp")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "shxxParamDTO", value = "审核信息参数对象", required = true, dataType = "ShxxParamDTO")})
    public DchyCgglGcjsspxxDo saveOrUpdateDchyCgglGcjsspxxDo(@RequestBody ShxxParamDTO shxxParamDTO) {
        return initShxxFormService.saveOrUpdateDchyCgglGcjsspxxDo(shxxParamDTO);
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmid 项目ID
     * @return
     * @description 获取工程建设审批信息
     */
    @ApiOperation("获取工程建设审批信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @PostMapping(value = "/gcjssp/{xmid}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiImplicitParams(@ApiImplicitParam(name = "xmid", value = "项目ID", dataType = "string", paramType = "path"))
    public DchyCgglGcjsspxxDo getDchyCgglGcjsspxxDo(@PathVariable(name = "xmid") String xmid) throws MissingArgumentException {
        if (StringUtils.isBlank(xmid)) {
            throw new MissingArgumentException("项目ID缺失!");
        }
        return initShxxFormService.getDchyCgglGcjsspxxDoByXmid(xmid);
    }



}
