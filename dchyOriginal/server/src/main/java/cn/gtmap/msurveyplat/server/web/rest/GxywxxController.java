package cn.gtmap.msurveyplat.server.web.rest;

import cn.gtmap.msurveyplat.common.dto.GxywxxDTO;
import cn.gtmap.msurveyplat.common.dto.YhGxywxxDTO;
import cn.gtmap.msurveyplat.server.service.share.GxywxxService;
import cn.gtmap.msurveyplat.server.web.BaseController;
import com.gtis.plat.vo.PfOrganVo;
import com.gtis.plat.vo.PfRoleVo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/11
 * @description 共享业务信息
 */
@RestController
@Api(tags = "共享业务信息接口")
public class GxywxxController extends BaseController {

    @Autowired
    private GxywxxService gxywxxService;

    /**
     * @param page      第几页
     * @param size      每页大小
     * @param gxywxxDTO 共享业务信息参数对象
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取共享业务分页信息
     */
    @ApiOperation(value = "获取共享业务分页信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "gxywxxDTO", value = "共享业务信息参数对象", required = true, dataType = "GxywxxDTO")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gx/gxywxx/list")
    public Page<Map> getGxywxxByPage(@RequestParam(value = "page", required = true) int page, @RequestParam(value = "size", required = true) int size, @RequestBody GxywxxDTO gxywxxDTO) {
        return gxywxxService.getGxywxxByPage(page, size, gxywxxDTO);
    }

    /**
     * @param gxywxxid 共享业务信息ID
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 禁用或者启用共享业务信息
     */
    @ApiOperation(value = "禁用或者启用共享业务信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "gxywxxid", value = "共享业务信息ID", required = true, paramType = "path", dataType = "string")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/rest/v1.0/gx/gxywxx/disable/{gxywxxid}")
    public void disableEnableGxywxx(@PathVariable("gxywxxid") String gxywxxid) throws Exception {
        gxywxxService.disableEnableGxywxxById(gxywxxid);
    }


    /**
     * @param gxywxxDTO 共享业务信息参数对象
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 新增或修改共享业务信息
     */
    @ApiOperation(value = "新增或修改共享业务信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "gxywxxDTO", value = "共享业务信息参数对象", required = true, dataType = "GxywxxDTO")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/rest/v1.0/gx/gxywxx")
    public void saveOrUpdateGxywxx(@RequestBody GxywxxDTO gxywxxDTO) throws Exception {
        gxywxxService.saveOrUpdateDchyCgglGxywxxDO(gxywxxDTO);
    }


    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取共享业务内容配置信息
     */
    @ApiOperation(value = "获取共享业务内容配置信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/rest/v1.0/gx/gxywnrpz/list")
    public List<Map> getGxywnrpzxx() throws Exception {
        return gxywxxService.getGxywnrpzxx();
    }


    /**
     * @param gxywxxid 共享业务信息ID
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取共享业务信息
     */
    @ApiOperation(value = "获取共享业务信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/rest/v1.0/gx/gxywxx/{gxywxxid}")
    public GxywxxDTO getGxywxx(@PathVariable("gxywxxid") String gxywxxid) throws Exception {
        return gxywxxService.getGxywxx(gxywxxid);
    }


    /**
     * @param yhGxywxxDTO 用户共享业务信息参数对象
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取用户共享业务信息
     */
    @ApiOperation(value = "获取用户共享业务信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "yhGxywxxDTO", value = "用户共享业务信息参数对象", required = true, dataType = "YhGxywxxDTO")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gx/gxywxx/user/list")
    public List<YhGxywxxDTO> getYhGxywxx(@RequestBody YhGxywxxDTO yhGxywxxDTO, HttpServletRequest request) {
        return gxywxxService.getYhGxywxx(yhGxywxxDTO, super.getCurrentUser(request));
    }

    /**
     * @param yhGxywxxDTO 用户共享业务信息参数对象
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取用户共享业务信息
     */
    @ApiOperation(value = "获取所有共享业务信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "yhGxywxxDTO", value = "用户共享业务信息参数对象", required = true, dataType = "YhGxywxxDTO")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gx/gxywxx/all/list")
    public List<YhGxywxxDTO> getAllGxywxx(@RequestBody YhGxywxxDTO yhGxywxxDTO, HttpServletRequest request) {
        return gxywxxService.getAllGxywxx(yhGxywxxDTO);
    }


    @ApiOperation(value = "获取部门信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gx/department/list")
    public List<PfOrganVo> getPfOrganVoList() {
        return gxywxxService.getPfOrganVoList();
    }


    @ApiOperation(value = "获取部门下角色信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "organid", value = "部门id", required = true, dataType = "String")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/rest/v1.0/gx/role/list/{organid}")
    public List<PfRoleVo> getRoleListByOrganid(@PathVariable("organid") String organid) {
        return gxywxxService.getRoleListByOrganid(organid);
    }

}
