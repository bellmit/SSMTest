package cn.gtmap.msurveyplat.exchange.web.rest;

import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.exchange.service.onemap.OnemapService;
import cn.gtmap.msurveyplat.exchange.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @ApiOperation(value = "获取附件一棵树信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "slbh", value = "项目ID", required = true, paramType = "path", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/fjxx/{slbh}")
    public Object getAttachmentTree(@PathVariable("slbh") String slbh) throws IOException {
        LOGGER.info("获取附件一棵树信息:{} {}", CalendarUtil.getCurHMSStrDate(), slbh);
        return onemapService.getAttachmentTree(slbh);
    }
}
