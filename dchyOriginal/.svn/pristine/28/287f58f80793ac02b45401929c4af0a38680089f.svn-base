package cn.gtmap.msurveyplat.common.dto;

import com.gtis.plat.vo.PfRoleVo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0，2021/2/4
 * @description
 */
public class UserInfo implements UserDetails {
    private String id; //用户ID
    private String username; //用户名
    private String loginName; //登录用户名
    private boolean isAdmin = false; //是否管理员
    private List<PfRoleVo> pfRoleVoList; //角色集合
    private String password; //密码

    private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PfRoleVo> getPfRoleVoList() {
        return pfRoleVoList;
    }

    public void setPfRoleVoList(List<PfRoleVo> pfRoleVoList) {
        this.pfRoleVoList = pfRoleVoList;
    }

}
