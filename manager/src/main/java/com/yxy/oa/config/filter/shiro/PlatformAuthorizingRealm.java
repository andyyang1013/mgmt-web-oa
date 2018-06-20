package com.yxy.oa.config.filter.shiro;

import com.yxy.oa.entity.SysPermission;
import com.yxy.oa.entity.SysRole;
import com.yxy.oa.entity.SysUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlatformAuthorizingRealm extends AuthorizingRealm {

    /**
     * PlatformAuthenticationToken 类型的Token，
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof PlatformAuthenticationToken;
    }

    /**
     * 身份验证，做了一个假的身份认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        PlatformAuthenticationToken authenticationToken = (PlatformAuthenticationToken) token;
        //这里做了个假的登录认证，直接拿客户端传递过来的token去比较，所以一定是登录成功的了
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                authenticationToken.getPrincipal(),
                authenticationToken.getCredentials(),
                getName());
        return authenticationInfo;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //拿到用户对象，查询角色和授权信息
        SysUser objUser = (SysUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        if (objUser != null) {
            Set<String> curRoles = new HashSet<>();
            Set<String> curMenus = new HashSet<>();
            List<SysRole> roles = objUser.getRoles();
            for (SysRole role : roles) {
                List<SysPermission> permissions = role.getPermissions();
                for (SysPermission permission : permissions) {
                    curMenus.add(permission.getResourceCode());
                }
                curRoles.add(role.getRoleCode());
            }
            authorizationInfo.setRoles(curRoles);
            authorizationInfo.setStringPermissions(curMenus);
        }
        return authorizationInfo;
    }
}