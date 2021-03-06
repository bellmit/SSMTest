package cn.gtmap.msurveyplat.portalol.feign;

import cn.gtmap.msurveyplat.common.dto.*;
import cn.gtmap.msurveyplat.common.util.TokenCheckUtil;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description Exchange服务
 */
public interface ExchangeFeignService {

    /**
     * @param sjhm
     * @return
     * @description 2021/1/5 通过手机号判断该手机号是否注册过
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/user/phoneNumberIsRegistered")
    ResponseMessage phoneNumberIsRegistered(@Param("sjhm") String sjhm);

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 验证完手机验证码之后保存用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/user/saveUser")
    ResponseMessage saveUser(@RequestBody UserDto userDto);

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 通过用户名和密码获取用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/user/getLocalAuthByUsernameAndPwd")
    ResponseMessage getLocalAuthByUsernameAndPwd(@RequestBody UserDto userDto);

    /**
     * @param dwbh
     * @return
     * @description 2021/5/12  通过单位名称获取对用的organ实体
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/user/queryOrganListByDwbh/{dwbh}")
    ResponseMessage queryOrganListByDwbh(@Param("dwbh") String dwbh);

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 注册新用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/register")
    ResponseMessage register(@RequestBody UserDto userDto);

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 用户名是否重复
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/yhmcf")
    ResponseMessage yhmcf(@RequestBody UserDto userDto);

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 手机号是否重复
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/sjhcf")
    ResponseMessage sjhcf(@RequestBody UserDto userDto);

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 检查用户是否有效
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/user/isvalid")
    ResponseMessage checkUserIsValidByLoginName(@RequestBody UserDto userDto);


    /**
     * @param userDto
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 用户可用
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/uservalid")
    ResponseMessage uservalid(@RequestBody UserDto userDto);
}
