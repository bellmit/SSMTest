package cn.gtmap.msurveyplat.server.web.rest;

import cn.gtmap.msurveyplat.common.dto.DchyCgglCgtjDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.server.service.share.GxcgtjService;
import cn.gtmap.msurveyplat.server.web.BaseController;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0, 2021/4/8
 * @description 成果统计信息
 */
@RestController
@Api(tags = "成果统计信息接口")
public class GxcgtjController extends BaseController {

    @Autowired
    private GxcgtjService gxcgtjService;


    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取测绘单位信息
     */
    @ApiOperation(value = "获取测绘单位信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/cgtj/chdwxx/list")
    public List<Map<String, String>> getChdwxx() {
        return gxcgtjService.getChdwxx();
    }


    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取成果满意度
     */
    @ApiOperation(value = "获取成果满意度")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/cgtj/cgmyd/list")
    public List<Map> getCgmyd(@RequestBody DchyCgglCgtjDto dchyCgglCgtjDto ) {
        return gxcgtjService.getCgmyd(dchyCgglCgtjDto);
    }

    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取成果满意度
     */
    @ApiOperation(value = "获取成果满意度")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/cgtj/cgzl/list")
    public List<Map> getCgzl(@RequestBody DchyCgglCgtjDto dchyCgglCgtjDto ) {
        return gxcgtjService.getCgZl(dchyCgglCgtjDto);
    }


    /**
     * @param page            第几页
     * @param size            每页大小
     * @param dchyCgglCgtjDto 成果管理成果统计查询参数信息
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 建设单位评价记录分页信息
     */
    @ApiOperation(value = "获取建设单位评价记录分页信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "dchyCgglCgtjDto", value = "成果管理成果统计查询参数信息", required = true, dataType = "DchyCgglCgtjDto")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/cgtj/jsdwpjjl/list")
    Page<Map> getJsdwPjjlBypage(@RequestParam(value = "page", required = true) int page, @RequestParam(value = "size", required = true) int size, @RequestBody DchyCgglCgtjDto dchyCgglCgtjDto) {
        return gxcgtjService.getJsdwPjjlBypage(page, size, dchyCgglCgtjDto);
    }


    /**
     * @param page            第几页
     * @param size            每页大小
     * @param dchyCgglCgtjDto 成果管理成果统计查询参数信息
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 建设单位评价记录分页信息
     */
    @ApiOperation(value = "获取建设单位评价记录分页信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "dchyCgglCgtjDto", value = "成果管理成果统计查询参数信息", required = true, dataType = "DchyCgglCgtjDto")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/cgtj/gldwccjg/list")
    Page<Map> getGldwCcjgBypage(@RequestParam(value = "page", required = true) int page, @RequestParam(value = "size", required = true) int size, @RequestBody DchyCgglCgtjDto dchyCgglCgtjDto) {
        return gxcgtjService.getGldwCcjgBypage(page, size, dchyCgglCgtjDto);
    }

}
