package cn.gtmap.msurveyplat.promanage.core.service;


import cn.gtmap.msurveyplat.promanage.model.CztyptUserDto;

public interface CztyptSsoService {

    /**
     * @param token
     * @return
     * @description 2021/5/12 通过token获取用户的所有信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    CztyptUserDto getTokenInfo(String token);

    /**
     * @param cztyptUserDto
     * @return
     * @description 2021/5/12  使用用户的身份证号作为用户名,查询是否存在 若存在  取出用户名和密码,若不存在,用身份证号作为账号,倒过来作为密码进行注册
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void isEsxit(CztyptUserDto cztyptUserDto);

}
