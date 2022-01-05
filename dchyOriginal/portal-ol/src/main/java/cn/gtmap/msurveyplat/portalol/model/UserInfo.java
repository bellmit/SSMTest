package cn.gtmap.msurveyplat.portalol.model;

import com.gtis.plat.vo.PfRoleVo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserInfo implements UserDetails {

    /**
     * 用户ID
     */
    private String id;

    private String username;

    private String loginName;

    private String usersIdAll;

    private boolean isAdmin =false;

    private String roleIds;

    private List<PfRoleVo> lstRole;

    private String password;

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

    public String getUsersIdAll() {
        return usersIdAll;
    }

    public void setUsersIdAll(String usersIdAll) {
        this.usersIdAll = usersIdAll;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public List<PfRoleVo> getLstRole() {
        return lstRole;
    }

    public void setLstRole(List<PfRoleVo> lstRole) {
        this.lstRole = lstRole;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
