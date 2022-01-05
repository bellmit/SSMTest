package cn.gtmap.msurveyplat.promanage.web.utils;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/util")
public class UtilController {

    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    @GetMapping("/resetzd")
    public ResponseMessage reSetZdxx() {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            dchyXmglZdService.reSetZdxx();
            responseMessage.getHead().setCode(Constants.SUCCESS);
            responseMessage.getHead().setMsg("系统字典项重置成功");
        }catch (Exception e){
            responseMessage.getHead().setCode(Constants.FAIL);
            responseMessage.getHead().setMsg("系统字典项重置失败，请联系管理员");
        }
        return responseMessage;
    }
}
