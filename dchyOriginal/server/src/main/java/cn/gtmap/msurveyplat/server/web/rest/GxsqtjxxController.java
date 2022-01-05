package cn.gtmap.msurveyplat.server.web.rest;

import cn.gtmap.msurveyplat.common.dto.GxsqtjxxDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.server.service.share.GxsqtjxxService;
import cn.gtmap.msurveyplat.server.web.BaseController;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/1/12
 * @description 共享申请统计信息接口
 */
@RestController
@Api(tags = "共享申请统计信息接口")
public class GxsqtjxxController extends BaseController {

    @Autowired
    private GxsqtjxxService gxsqtjxxService;

    @ApiOperation(value = "共享申请统计信息台账接口")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "GxsqtjxxDto", value = "共享申请统计信息参数对象", required = true, dataType = "GxsqtjxxDto")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gxsqtjxx/list")
    public ResponseMessage gxsqtjxxByPage(@RequestBody GxsqtjxxDto gxsqtjxxDto) {
        ResponseMessage message;
        if (null != gxsqtjxxDto) {
            try {
                Page<Map<String, Object>> pageList = gxsqtjxxService.gxsqtjxxByPage(gxsqtjxxDto);
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("data", pageList);
            } catch (Exception e) {
                message = ResponseUtil.wrapExceptionResponse(e);
                LOGGER.error("错误原因:{}", e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    @ApiOperation(value = "共享申请统计信息echarts图接口")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "GxsqtjxxDto", value = "共享申请统计信息参数对象", required = true, dataType = "GxsqtjxxDto")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gxsqtjxx/shjgdata")
    public ResponseMessage gxsqtjxxDataByShjg(@RequestBody GxsqtjxxDto gxsqtjxxDto) {
        ResponseMessage message;
        if (null != gxsqtjxxDto) {
            try {
                List<Map<String, Object>> mapList = gxsqtjxxService.gxsqtjxxDataByShjg(gxsqtjxxDto);
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("data", mapList);
            } catch (Exception e) {
                message = ResponseUtil.wrapExceptionResponse(e);
                LOGGER.error("错误原因:{}", e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    @ApiOperation(value = "共享申请统计信息echarts图接口")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "GxsqtjxxDto", value = "共享申请统计信息参数对象", required = true, dataType = "GxsqtjxxDto")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gxsqtjxx/ywmcdata")
    public ResponseMessage gxsqtjxxDataByYwmc(@RequestBody GxsqtjxxDto gxsqtjxxDto) {
        ResponseMessage message;
        if (null != gxsqtjxxDto) {
            try {
                List<Map<String, Object>> mapList = gxsqtjxxService.gxsqtjxxDataByYwmc(gxsqtjxxDto);
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("data", mapList);
            } catch (Exception e) {
                message = ResponseUtil.wrapExceptionResponse(e);
                LOGGER.error("错误原因:{}", e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

}
