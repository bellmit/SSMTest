package cn.gtmap.msurveyplat.server.web.rest;

import cn.gtmap.msurveyplat.common.dto.GxyyfxDto;
import cn.gtmap.msurveyplat.server.service.share.GxYyfxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0, 2021/4/12
 * @description 应用分析信息
 */
@RestController
@Api("应用分析接口")
public class GxYyfxController {

    @Autowired
    private GxYyfxService gxYyfxService;

    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取建设单位和测绘单位信息
     */
    @ApiOperation(value = "获取建设单位和测绘单位信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/yyfx/yhdw/list")
    public List<Map<String, String>> getJsdwAndChdw() {
        return gxYyfxService.getJsdwAndChdw();
    }


    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取建设单位委托项目数量
     */
    @PostMapping("/rest/v1.0/yyfx/jsdwwtxm/list")
    public List<Map> getJsdwWtxmsl() {
        return gxYyfxService.getJsdwWtxmsl();
    }


    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取测绘单位承接项目数量
     */
    @PostMapping("/rest/v1.0/yyfx/chdwcjsl/list")
    public List<Map> getChdwCjsl() {
        return gxYyfxService.getChdwCjsl();
    }

    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取项目备案记录分页信息
     */
    @PostMapping("/rest/v1.0/yyfx/xmbajl/list")
    public Page<Map> getXmbajlByPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size, @RequestBody GxyyfxDto gxyyfxDto) {
        return gxYyfxService.getXmbajlByPage(page,size,gxyyfxDto);
    }


}
