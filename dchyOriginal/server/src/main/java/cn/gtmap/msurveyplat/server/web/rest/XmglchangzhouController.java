package cn.gtmap.msurveyplat.server.web.rest;


import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.server.service.share.XmglchangzhouService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0, 2021/3/17
 * @description 项目管理常州接口
 */
@Api(tags = "项目管理常州接口")
@RestController
public class XmglchangzhouController {

    @Autowired
    private XmglchangzhouService xmglchangzhouService;


    /**
     * @param param 项目状态参数
     * @return 字典项
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description
     */
    @ApiOperation(value = "获取项目状态字典项")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @PostMapping(value = "/rest/v1.0/zdx/getzdxx")
    public Object getAllZzdjs(@RequestBody Map<String, Object> param) {
        return xmglchangzhouService.getAllZzdjs(param);
    }


    /**
     * @param param 查询信息
     * @return 项目查看台账
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description
     */
    @ApiOperation(value = "项目查看台账")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @PostMapping(value = "/rest/v1.0/management/queryResultsManagement")
    public ResponseMessage queryResultsManagement(@RequestBody Map<String, Object> param) {
        return xmglchangzhouService.queryResultsManagement(param);
    }

    /**
     * @param param chxmid 测绘项目id
     * @return 成果提交记录
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description
     */
    @ApiOperation(value = "成果提交记录")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @PostMapping(value = "/rest/v1.0/contractfile/getprojectinfo")
    public ResponseMessage getProjectConstrctInfo(@RequestBody Map<String, Object> param) {
        return xmglchangzhouService.getProjectConstrctInfo(param);
    }


}
