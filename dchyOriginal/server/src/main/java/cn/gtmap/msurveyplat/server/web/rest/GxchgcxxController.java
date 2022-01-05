package cn.gtmap.msurveyplat.server.web.rest;

import cn.gtmap.msurveyplat.common.dto.GxchgcClsxDTO;
import cn.gtmap.msurveyplat.common.dto.GxchgcxxDTO;
import cn.gtmap.msurveyplat.server.service.share.GxchgcxxService;
import cn.gtmap.msurveyplat.server.web.BaseController;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
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
 * @description 共享测绘工程信息
 */
@RestController
@Api(tags = "共享测绘工程信息接口")
public class GxchgcxxController extends BaseController {

    @Autowired
    private GxchgcxxService gxchgcxxService;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param page 第几页
     * @param size 每页大小
     * @param gxchgcxxDTO 共享测绘工程信息参数对象
     * @return
     * @description 获取共享测绘工程分页信息
     */
    @ApiOperation(value = "获取共享测绘工程分页信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "gxchgcxxDTO", value = "共享测绘工程信息参数对象", required = true, dataType = "GxchgcxxDTO")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gx/gxchgcxx/list")
    public JSONObject getDchyXmglChgcxxByPage(@RequestParam(value = "page", required = true) int page, @RequestParam(value = "size", required = true) int size, @RequestBody GxchgcxxDTO gxchgcxxDTO) {
        JSONObject jsonObject = new JSONObject();
        Page<Map> gxchgcxx = gxchgcxxService.getDchyXmglChgcxxByPage(page,size,gxchgcxxDTO);
        jsonObject.put("gxchgcxx",gxchgcxx);
        jsonObject.put("attachmentHost", AppConfig.getProperty("attachment.host"));
        return jsonObject;
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param page 第几页
     * @param size 每页大小
     * @param gxchgcxxDTO 共享测绘工程信息参数对象
     * @return
     * @description 测量成果数据分页信息
     */
    @ApiOperation(value = "获取测量成果数据分页信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "gxchgcxxDTO", value = "共享测绘工程信息参数对象", required = true, dataType = "GxchgcxxDTO")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gx/gxclcgxx/list")
    public Page<Map> getDchyXmglClcgxxByPage(@RequestParam(value = "page", required = true) int page, @RequestParam(value = "size", required = true) int size, @RequestBody GxchgcxxDTO gxchgcxxDTO) {
        return gxchgcxxService.getDchyXmglClcgxxByPage(page,size,gxchgcxxDTO);
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param page 第几页
     * @param size 每页大小
     * @param gxchgcxxDTO 共享测绘工程信息参数对象
     * @return
     * @description 测量成果数据分页信息
     */
    @ApiOperation(value = "获取测量成果数据分页信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "gxchgcxxDTO", value = "共享测绘工程信息参数对象", required = true, dataType = "GxchgcxxDTO")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gx/clcgxx/list")
    public Page<Map> getDchyClcgxxByPage(HttpServletRequest request,@RequestParam(value = "page", required = true) int page, @RequestParam(value = "size", required = true) int size, @RequestBody GxchgcxxDTO gxchgcxxDTO) {
        return gxchgcxxService.getDchyClcgxxByPage(page,size,gxchgcxxDTO,super.getCurrentUser(request));
    }




    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param chgcid 测绘工程ID
     * @return
     * @description 获取测绘工程测量事项
     */
    @ApiOperation(value = "获取测绘工程测量事项")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "chgcid", value = "测绘工程ID", required = true, paramType = "path", dataType = "string")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/rest/v1.0/gx/gxchgcxx/{chgcid}")
    public List<GxchgcClsxDTO> getDchyXmglChgcClsx(@PathVariable("chgcid") String chgcid) throws Exception {
        return gxchgcxxService.getGxChgcClsxListById(chgcid);
    }



    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param babh 工程编号
     * @return
     * @description 获取共享业务内容下载地址
     */
    @ApiOperation(value = "获取共享业务内容下载地址")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "gcbh", value = "测绘工程ID", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "gxywid", value = "共享业务ID", required = false, paramType = "path", dataType = "string")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = {"/rest/v1.0/gx/gxchgcclxx/{babh}","/rest/v1.0/gx/gxchgcclxx/{babh}/{gxywid}"})
    public Map getGxchgcclDownUrl(HttpServletRequest request, @PathVariable(value = "babh") String babh, @PathVariable(value = "gxywid", required = false) String gxywid) throws Exception {
        return gxchgcxxService.getGxchgcclDownUrl(babh,gxywid,super.getCurrentUser(request));
    }



}
