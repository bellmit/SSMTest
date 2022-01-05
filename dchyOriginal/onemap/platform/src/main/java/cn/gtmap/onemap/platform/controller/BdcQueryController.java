package cn.gtmap.onemap.platform.controller;

import cn.gtmap.onemap.platform.bdc.entity.BdcZt;
import cn.gtmap.onemap.platform.bdc.service.BdcQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Bdc Query
 *
 * @author monarchCheng
 * @create 2017-06-19 15:35
 **/
@Controller
@RequestMapping("/bdcQuery")
public class BdcQueryController {
    @Autowired
    private BdcQueryService bdcQueryService;

    /**
     * 查询不动产登记状态
     * @param bdcdybh
     * @return
     */
    @RequestMapping("/bdczt")
    @ResponseBody
    public BdcZt queryBdcZt(@RequestParam String bdcdybh) {
        BdcZt bdcZt = bdcQueryService.findBdcZt(bdcdybh);
        if (bdcZt.getBdcdyzt() != null){
            bdcZt.setXs(Integer.parseInt(bdcZt.getBdcdyzt()));
        }
        return bdcZt;
    }
}
