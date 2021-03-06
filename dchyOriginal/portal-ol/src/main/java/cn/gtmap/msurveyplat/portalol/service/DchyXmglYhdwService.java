package cn.gtmap.msurveyplat.portalol.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglJsdw;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjclpz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhdw;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.vo.PfUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface DchyXmglYhdwService {

    /**
     * @param userid
     * @return
     * @description 2020/12/30 通过userid返货当前用户信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    DchyXmglYhdw getDchyXmglYhdwByUserId(String userid);

    /**
     * @param yhzjhm
     * @return
     * @description 2020/12/30 通过yhzjhm返回用户信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    DchyXmglYhdw getDchyXmglYhdwByZjhm(String yhzjhm);

    /**
     * @param tyshxydm
     * @return
     * @description 2020/12/30 通过tyshxydm返回可使用的用户信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    DchyXmglYhdw getValidDchyXmglYhdwByTyshxydm(String tyshxydm);

    /**
     * @param param
     * @param files
     * @return
     * @description 2020/12/30 用户注册
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, String> registeredYhdw(Map<String, Object> param, MultipartFile[] files);

    /**
     * @param paramMap
     * @param files
     * @return
     * @description 2020/12/30 文件上传
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage fileUpload(Map<String, Object> paramMap, MultipartFile[] files);

    /**
     * @param phone
     * @return
     * @description 2020/12/15 验证手机号是否注册过
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    boolean phoneIsRepet(String phone);

    /**
     * @param username
     * @return
     * @description 2020/12/15 验证登陆用户是否有效
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     */
    boolean usernameAvaliable(String username);

    /**
     * @param username
     * @return
     * @description 2020/12/15 验证登陆用户是否注册过
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    boolean usernameIsRepet(String username);

    /**
     * @param password
     * @param sjhm
     * @return
     * @description 2020/12/22 通过手机号重置密码
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, Object> resetPassword(String sjhm, String password, String yzm);

    /**
     * @param yzm
     * @return
     * @description 2020/12/22 验证验证码是否正确 并判断手机号是否注册
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, Object> yzmIsVaild(String sjhm, String yzm);

    /**
     * 验证用户名和密码
     *
     * @param username
     * @param password
     * @return
     */
    PfUser getLocalAuthByUsernameAndPwd(String username, String password);

    /**
     * @param clmc
     * @return
     * @description 2020/12/30 通过材料名称获取字典表所属模块配置
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    DchyXmglSjclpz getSjclpzByClmc(String clmc,String ssmkid);

    /**
     * @param tyshxydm
     * @return
     * @description 2021/5/10 通过统一社会信用代码带出建设单位信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    DchyXmglJsdw queryJsdwByTyshxydm(String tyshxydm);
}