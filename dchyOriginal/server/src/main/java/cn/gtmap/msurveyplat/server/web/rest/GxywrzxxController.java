package cn.gtmap.msurveyplat.server.web.rest;

import cn.gtmap.msurveyplat.common.dto.GxywrzxxDTO;
import cn.gtmap.msurveyplat.server.service.share.GxywrzxxService;
import cn.gtmap.msurveyplat.server.web.BaseController;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/12
 * @description 共享业务日志信息
 */
@RestController
@Api(tags = "共享业务日志信息接口")
public class GxywrzxxController extends BaseController {

    @Autowired
    private GxywrzxxService gxywrzxxService;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param page 第几页
     * @param size 每页大小
     * @param gxywrzxxDTO 共享业务日志信息参数对象
     * @return
     * @description 获取共享业务日志分页信息
     */
    @ApiOperation(value = "获取共享业务日志分页信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "gxywrzxxDTO", value = "共享业务日志参数对象", required = true, dataType = "GxywrzxxDTO")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gx/gxywrzxx/list")
    public Page<Map> getGxywrzxxByPage(@RequestParam(value = "page", required = true) int page, @RequestParam(value = "size", required = true) int size, @RequestBody GxywrzxxDTO gxywrzxxDTO) {
        return gxywrzxxService.getGxywrzxxByPage(page,size,gxywrzxxDTO);
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param gxywrzxxDTO 共享业务日志信息参数对象
     * @return
     * @description 获取共享业务日志统计信息
     */
    @ApiOperation(value = "获取共享业务日志统计信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "gxywrzxxDTO", value = "共享业务日志参数对象", required = true, dataType = "GxywrzxxDTO")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gx/gxywrzxx/tjxxlist")
    public List<Map> getGxywrzTjxx(@RequestBody GxywrzxxDTO gxywrzxxDTO) {
        return gxywrzxxService.getGxywrzTjxx(gxywrzxxDTO);
    }



    /**
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @param page 第几页
     * @param size 每页大小
     * @param gxywrzxxDTO 共享业务日志信息参数对象
     * @return
     * @description 获取共享业务日志分页信息
     */
    @ApiOperation(value = "获取共享业务日志记录分页信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "gxywrzxxDTO", value = "共享业务日志参数对象", required = true, dataType = "GxywrzxxDTO")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gx/gxywrzjl/list")
    public Page<Map> getGxywrzjlByPage(@RequestParam(value = "page", required = true) int page, @RequestParam(value = "size", required = true) int size, @RequestBody GxywrzxxDTO gxywrzxxDTO) {
        return gxywrzxxService.getGxywrzjlByPage(page,size,gxywrzxxDTO);
    }

}
