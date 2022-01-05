package cn.gtmap.msurveyplat.portal.mapper;


import cn.gtmap.msurveyplat.portal.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/7/8
 * @description 用户表操作
 */
@Mapper
@Component
public interface UserMapper {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param user
     * @return
     * @description 插入用户表数据
     */
    void insertUser(User user);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param id
     * @return
     * @description 根据id获取用户信息
     */
    User getUserById(String id);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param id
     * @return
     * @description 根据id删除用户信息
     */
    void deleteUserById(String id);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param user
     * @return
     * @description 根据id删除用户信息
     */
    void updateUser(User user);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 获取所有的用户信息
     */
    List<User> getAllUser();


}
