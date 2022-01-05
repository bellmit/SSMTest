package cn.gtmap.msurveyplat.server.web.rest;

import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.dto.InitDataResultDTO;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.server.service.rest.InitDataRestService;
import cn.gtmap.msurveyplat.server.service.ywxx.InitDataDealService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/4
 * @description 初始化数据Controller
 */
@RestController
@Api(tags = "初始化服务接口")
public class InitDataRestController implements InitDataRestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitDataRestController.class);

    @Autowired
    private InitDataDealService initDataDealService;


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param initDataParamDTO 初始化数据参数对象
     * @return 项目信息
     * @description 初始化相关业务信息
     */
    @ApiOperation(value ="初始化相关业务信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "initDataParamDTO", value = "初始化业务参数对象", required = true, dataType = "InitDataParamDTO")})
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public DchyCgglXmDO csh(@RequestBody InitDataParamDTO initDataParamDTO) throws Exception {
        InitDataResultDTO initDataResultDTO = initDataDealService.init(initDataParamDTO,true);
        return initDataResultDTO.getDchyCgglXmDO();
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmid 项目id
     * @return
     * @description 根据xmid删除业务信息
     * */
    @ApiOperation(value ="根据xmid删除业务信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "xmid", value = "根据xmid删除业务信息", required = true, dataType = "String")})
    @Override
    public void scywxx(@PathVariable("xmid") String xmid) throws Exception{
        initDataDealService.delYwxx(xmid);
    }
}

