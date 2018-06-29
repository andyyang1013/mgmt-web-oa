package com.yxy.oa.config.filter.shiro;

import com.yxy.oa.entity.SysPermission;
import com.yxy.oa.entity.SysRole;
import com.yxy.oa.entity.SysUser;
import com.yxy.oa.exception.CodeMsg;
import com.yxy.oa.mapper.SysPermissionMapper;
import com.yxy.oa.mapper.SysRoleMapper;
import com.yxy.oa.mapper.SysUserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PlatformAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    /**
     * PlatformAuthenticationToken 类型的Token，
     */
//    @Override
//    public boolean supports(AuthenticationToken token) {
//        return token instanceof PlatformAuthenticationToken;
//    }

    /**
     * 身份验证，做了一个假的身份认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String account = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());

        // 查询用户信息
        SysUser query = new SysUser();
        query.setAccount(account);
        SysUser loginUser = sysUserMapper.selectOne(query);
        // 账号不存在
        if (loginUser == null) {
            throw new UnknownAccountException(CodeMsg.account_password_error.getMsg());
        }
        // 密码错误
        if (!password.equals(loginUser.getPassword())) {
            throw new UnknownAccountException(CodeMsg.account_password_error.getMsg());
        }
        // 账号锁定
        if (loginUser.getDisabled() == 1) {
            throw new LockedAccountException(CodeMsg.user_has_disabled.getMsg());
        }
        List<SysRole> roles = sysRoleMapper.getRolesByUserId(loginUser.getId());
        for (SysRole role : roles) {
            role.setPermissions(sysPermissionMapper.getPermissionsByRoleId(role.getId()));
        }
        loginUser.setRoles(roles);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(loginUser, password, getName());
        return info;
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