package cn.gtmap.msurveyplat.portalol.core.service.impl;

import cn.gtmap.msurveyplat.portalol.model.UserInfo;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfRoleVo;
import com.gtis.plat.vo.PfUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于加载用户信息 实现UserDetailsService接口，或者实现AuthenticationUserDetailsService接口
 * @author szh
 */
public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private SysUserService sysUserService;

        //实现AuthenticationUserDetailsService，实现loadUserDetails方法
/*            @Override
            public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
                System.out.println("当前的用户名是："+token.getName());
                *//*这里我为了方便，就直接返回一个用户信息，实际当中这里修改为查询数据库或者调用服务什么的来获取用户信息*//*
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername("admin");
                userInfo.setName("admin");
                Set<AuthorityInfo> authorities = new HashSet<>();
                AuthorityInfo authorityInfo = new AuthorityInfo("ADMIN");
                authorities.add(authorityInfo);
                userInfo.setAuthorities(authorities);
                return userInfo;
            }*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("当前的用户名是："+ username);
        //这里我为了方便，就直接返回一个用户信息，实际当中这里修改为查询数据库或者调用服务什么的来获取用户信息
        UserInfo userInfo = new UserInfo();
        List<GrantedAuthority> grantedAuthorities = new ArrayList();
        userInfo.setAuthorities(grantedAuthorities);
        PfUserVo pfUserVo = sysUserService.getUserByloginName(username);
        userInfo.setId(pfUserVo.getUserId());
        userInfo.setUsername(pfUserVo.getUserName());
        userInfo.setLoginName(pfUserVo.getLoginName());
        userInfo.setPassword(pfUserVo.getLoginPassWord());
        List<PfRoleVo> pfRoleVoList = sysUserService.getRoleListByUser(pfUserVo.getUserId());
        if (!"0".equals(pfUserVo.getUserId())) {
            userInfo.setUsersIdAll(sysUserService.getUserAgentList(pfUserVo.getUserId()));
            userInfo.setLstRole(pfRoleVoList);
            BuildUserRoles(sysUserService, userInfo);
            for (PfRoleVo roleVo : pfRoleVoList) {
                if (roleVo.getRoleName().endsWith("系统管理员"))
                {
                    userInfo.setAdmin(true);
                    userInfo.setUsersIdAll(null);
                    userInfo.setRoleIds(null);
                    break;
                }
            }
        } else {
            userInfo.setAdmin(true);
            userInfo.setUsersIdAll(null);
            userInfo.setRoleIds(null);
        }
        return userInfo;
    }

    public static void BuildUserRoles(SysUserService userService, UserInfo userInfo)
    {
        List<String> lstAgentRoles = userService.getRoleListByAgentUser(userInfo.getId());

        List<PfRoleVo> lstRoles = userInfo.getLstRole();
        List<String> tmpList = new ArrayList();
        StringBuffer strRoles = new StringBuffer("");
        for (PfRoleVo roleVo : lstRoles)
        {
            strRoles.append("'" + roleVo.getRoleId() + "',");
            tmpList.add(roleVo.getRoleId());
        }
        for (String roleStr : lstAgentRoles) {
            if (!tmpList.contains(roleStr)) {
                strRoles.append("'" + roleStr + "',");
            }
        }
        if (strRoles.length() > 1) {
            userInfo.setRoleIds(strRoles.toString().substring(0, strRoles.toString().length() - 1));
        } else {
            userInfo.setRoleIds("");
        }
    }
}
