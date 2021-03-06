package cn.gtmap.msurveyplat.portalol.core.service.impl;

import cn.gtmap.msurveyplat.common.domain.ActStProRelDo;
import cn.gtmap.msurveyplat.common.dto.*;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;
import cn.gtmap.msurveyplat.common.vo.SurveyItemVo;
import cn.gtmap.msurveyplat.portalol.feign.ExchangeFeignService;
import com.gtis.config.AppConfig;
import com.gtis.plat.vo.PfRoleVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description
 */
@Service
public class ExchangeFeignServiceImpl {


    private static ExchangeFeignService exchangeFeignService = null;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 初始化ExchangeFeignService接口
     * */
    static {
        exchangeFeignService = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(35000, 35000))
                .retryer(new Retryer.Default(35000, 35000, 3))
                .target(ExchangeFeignService.class, AppConfig.getProperty("exchange.url"));
    }

    /**
     * @param sjhm
     * @return
     * @description 2021/1/5 通过手机号判断该手机号是否注册过
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public ResponseMessage phoneNumberIsRegistered(String sjhm) {
        return exchangeFeignService.phoneNumberIsRegistered(sjhm);
    }

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 验证完手机验证码之后保存用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public ResponseMessage saveUser(UserDto userDto) {
        return exchangeFeignService.saveUser(userDto);
    }

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 通过用户名和密码获取用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public ResponseMessage getLocalAuthByUsernameAndPwd(UserDto userDto) {
        return exchangeFeignService.getLocalAuthByUsernameAndPwd(userDto);
    }

    /**
     * @param dwbh
     * @return
     * @description 2021/5/12  通过单位名称获取对用的organ实体
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public ResponseMessage queryOrganListByDwbh(String dwbh) {
        return exchangeFeignService.queryOrganListByDwbh(dwbh);
    }

    /**
     * @param userDto 材料名称
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 新用户注册
     */
    public ResponseMessage register(UserDto userDto) {
        return exchangeFeignService.register(userDto);
    }

    /**
     * @param userDto
     * @return 待办列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 用户名重复检查
     */
    public ResponseMessage  yhmcf(UserDto userDto) {
        return exchangeFeignService.yhmcf(userDto);
    }

    /**
     * @param userDto
     * @return
     * @description 2021/3/30 注册手机号重复检查
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public ResponseMessage sjhcf(UserDto userDto) {
        return exchangeFeignService.sjhcf(userDto);
    }

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 检查用户是否有效
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public ResponseMessage checkUserIsValidByLoginName(UserDto userDto) {
        return exchangeFeignService.checkUserIsValidByLoginName(userDto);
    }

}
