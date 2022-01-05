package cn.gtmap.msurveyplat.exchange.service.user;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.dto.RoleDto;
import cn.gtmap.msurveyplat.common.dto.UserDto;
import com.gtis.plat.vo.PfUserVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface PlatUserService {
    /**
     * @return boolean
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: loginName
     * @time 2021/5/7 16:51
     * @description 用户名已存在检查
     */
    boolean loginNameExists(String loginName);

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param: loginName
      * @return boolean
      * @time 2021/5/13 14:07
      * @description 用户账号可以使用
      */
    boolean userIsValid(String loginName);

    /**
     * @return boolean
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: mobilePhone
     * @time 2021/5/7 16:51
     * @description 绑定手机号已存在
     */
    boolean mobilePhoneExists(String mobilePhone);

    /**
     * @return
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userDto
     * @time 2021/5/7 16:51
     * @description 新用户注册
     */
    Map registerNewUser(UserDto userDto);

    /**
     * @return java.util.Map
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userDto
     * @time 2021/5/7 16:53
     * @description 用户更新
     */
    Map updateUser(UserDto userDto);

    List<PfUserVo> getUserListByOrgan(UserDto userDto);

    /**
     * @param loginname
     * @return
     * @description 2021/5/12 通过用户名判断用户是否有效
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    boolean checkUserIsValidByLoginName(String loginname);

    /**
     * @param map
     * @return
     * @description 2021/1/6 用户管理台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Page<Map<String, Object>> queryUserByUsername(Map map);

    /**
     * @param username
     * @param password
     * @return
     * @description 2021/1/5 修改密码
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, String> changePassword(String username, String password, String passwordNew);

    /**
     * @param sjhm
     * @return
     * @description 2021/5/12 验证手机号是否已经注册过
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage phoneNumberIsRegistered(String sjhm);

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 保存用户(通过手机号修改密码)
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage saveUser(UserDto userDto);

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 通过用户名和密码获取当前用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage getLocalAuthByUsernameAndPwd(UserDto userDto);

    /**
     * @param userid
     * @param state
     * @return
     * @description 2021/1/5 启用或者暂用用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, String> changeUserState(String userid, String state);

    /**
     * @param userid 用户id
     * @param roles  角色
     * @return
     * @description 2021/1/5 用户分配权限
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, String> distributionRoleAuthority(String userid, String roles);

    /**
     * @param roleDto
     * @return
     * @description 2021/3/9 所有的角色
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage queryAllRole(RoleDto roleDto);

    /**
     * @param dwbh
     * @return
     * @description 2021/5/12 通过单位名称获取对用的organ实体
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage queryOrganListByDwbh(String dwbh);

}
