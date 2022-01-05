package cn.gtmap.onemap.platform.controller;

import cn.gtmap.onemap.platform.support.spring.BaseController;
import com.alibaba.fastjson.JSONObject;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lkw on 2018/7/12.
 */
@Controller
@RequestMapping("/secretProps")
public class SecretPropQueryController extends BaseController{

    @Autowired
    private SysUserService sysUserService;

    /**
     * 属性识别-根据权限是否可查询保密属性
     * @param keyCode 待判断字段
     * @return
     * */
    @RequestMapping("/secretPropsQuery")
    @ResponseBody
    public Object secretPropsQuery(@RequestParam String keyCode,String userId) {
        Map returnMap=new HashMap();
        boolean result=false;
        String msg="对不起，该用户权限不足！";
        // 公司代码
        String userNo="";
        PfUserVo userInfo = sysUserService.getUserVo(userId);
        if (userInfo != null) {
            userNo=userInfo.getUserNo();
            if(StringUtils.equals(userNo,keyCode)){
                result=true;
                msg="";
            }
        }else{
            msg="当前尚未登录，请先登录！";
        }
        returnMap.put("result",result);
        returnMap.put("msg",msg);
        return JSONObject.toJSONString(returnMap);
    }
}
