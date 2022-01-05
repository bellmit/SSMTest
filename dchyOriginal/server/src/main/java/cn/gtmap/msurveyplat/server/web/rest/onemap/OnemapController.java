package cn.gtmap.msurveyplat.server.web.rest.onemap;

import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.vo.ProjectDetailVo;
import cn.gtmap.msurveyplat.common.vo.ProjectInformationVo;
import cn.gtmap.msurveyplat.server.service.onemap.OnemapService;
import cn.gtmap.msurveyplat.server.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/22
 * @description 一张图接口
 */
@Api(tags = "一张图接口")
@RestController
@RequestMapping("/rest/v1.0/onemap")
public class OnemapController extends BaseController {

    @Autowired
    private OnemapService onemapService;

    @ApiOperation(value = "获取项目生命线详情")
    @ApiImplicitParams({@ApiImplicitParam(name = "chxmbh", value = "测绘项目编号", required = true, paramType = "path", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/xmxq/{chxmbh}")
    public List<ProjectDetailVo> getProjectDetailVoList(@PathVariable("chxmbh") String chxmbh) throws Exception {
        LOGGER.info("获取项目详情:{} {}", CalendarUtil.getCurHMSStrDate(),chxmbh);
        return onemapService.getProjectDetailVoList(chxmbh);
    }

    @ApiOperation(value = "获取项目查看url地址")
    @ApiImplicitParams({@ApiImplicitParam(name = "xmid", value = "项目ID", required = true, paramType = "path", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/xmckdz/{xmid}")
    public Object getViewProjectUrl(@PathVariable("xmid") String xmid) throws IOException {
        LOGGER.info("获取项目查看url地址:{} {}", CalendarUtil.getCurHMSStrDate(),xmid);
        return onemapService.getViewProjectUrl(xmid);
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmbh 项目编号
     * @param xmmc 项目名称
     * @param xmdz 项目地址
     * @return 项目信息
     * @description 获取项目信息
     * */
    @ApiOperation(value = "一张图搜索项目信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "xmbh", value = "项目编号", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "xmmc", value = "项目名称", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "xmdz", value = "项目地址", required = true, paramType = "query", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/getDchyCgglXm")
    public List<DchyCgglXmDO> getDchyCgglXm(String xmbh, String xmmc, String xmdz) {
        return onemapService.getDchyCgglXm(xmbh, xmmc, xmdz);
    }

    @ApiOperation(value = "获取项目信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "dchybh", value = "多测合一编号", required = true, paramType = "path", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/xmxx/{dchybh}")
    public ProjectInformationVo getProjectInformationVoList(@PathVariable("dchybh") String dchybh) throws Exception {
        LOGGER.info("获取项目信息:{} {}", CalendarUtil.getCurHMSStrDate(),dchybh);
        return onemapService.getProjectInformationVo(dchybh);
    }

}
