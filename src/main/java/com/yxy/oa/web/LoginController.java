package com.yxy.oa.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Maps;
import com.yxy.oa.Constant;
import com.yxy.oa.entity.SysPermission;
import com.yxy.oa.entity.SysUser;
import com.yxy.oa.exception.BizException;
import com.yxy.oa.exception.CodeMsg;
import com.yxy.oa.repository.IRedisRepository;
import com.yxy.oa.service.ISysPermissionService;
import com.yxy.oa.service.ISysRoleService;
import com.yxy.oa.service.ISysUserService;
import com.yxy.oa.util.ShiroUtils;
import com.yxy.oa.util.Toolkit;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 后台登录
 **/
@RestController
@RequestMapping("/admin")
public class LoginController extends BaseController {

    @Autowired
    private IRedisRepository redisRepository;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysPermissionService sysPermissionService;

    @RequestMapping("/login")
    public Map<String, Object> login(String account, String password) {
        /**请求参数空值判断*/
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            throw new BizException(CodeMsg.param_note_blank);
        }
        // 查询用户信息
        SysUser query = new SysUser();
        query.setAccount(account);
        SysUser loginUser = sysUserService.selectOne(new EntityWrapper<>(query));
        // 账号不存在
        if (loginUser == null) {
            throw new UnknownAccountException(CodeMsg.account_password_error.getMsg());
        }
        Subject subject = null;
        try {
            subject = ShiroUtils.getSubject();
            password = Toolkit.encrypt(password, loginUser.getSalt());
            UsernamePasswordToken token = new UsernamePasswordToken(account, password);
            subject.login(token);
        } catch (UnknownAccountException e) {
            throw new BizException(e.getMessage(),e);
        } catch (IncorrectCredentialsException e) {
            throw new BizException(e.getMessage(),e);
        } catch (LockedAccountException e) {
            throw new BizException(e.getMessage(),e);
        } catch (AuthenticationException e) {
            throw new BizException(CodeMsg.account_password_error.getMsg(),e);
        }
        /**记录最后登录时间*/
        Date lastLoginTime = new Date();
        loginUser.setUpdateUid(loginUser.getId());
        loginUser.setUpdateTime(lastLoginTime);
        loginUser.setLastLoginTime(lastLoginTime);
        sysUserService.updateById(loginUser);
        //生成user token
        String sessionId = subject.getSession().getId().toString();
        redisRepository.set(String.format(Constant.USER_TOKEN_REDIS_KEY, sessionId), loginUser, Constant.USER_TOKEN_EXPIRE, TimeUnit.SECONDS);
//        CookieUtil.add(response, Constant.USER_TOKEN, sessionId, Constant.USER_TOKEN_EXPIRE);
        logger.info("登录成功：userToken=" + sessionId);
        //返回用户信息
        Map<String, Object> map = Maps.newHashMap();
        map.put("token",sessionId);
        map.put("account",loginUser.getAccount());
        return map;
    }

    /**
     * 前端全局验证用户登录状态（每个页面调用一次）
     *
     * @param userToken
     * @return
     */
    @RequestMapping("/checkLoginStatus")
    public String checkLoginStatus(@CookieValue(name = Constant.USER_TOKEN, required = false) String userToken) {
        logger.info("全局验证用户登录状态:userToken=" + userToken);
        if (StringUtils.isEmpty(userToken)) {
            throw new BizException(CodeMsg.token_not_blank);
        }
        Object object = redisRepository.get(String.format(Constant.USER_TOKEN_REDIS_KEY, userToken));
        if (object == null || !(object instanceof SysUser)) {
            /**token失效，需要重新登录*/
            throw new BizException(CodeMsg.user_token_invalid);
        }
        return SUCCESS;
    }

    /**
     * 获取当前登录的用户详细信息
     *
     * @param userToken
     * @return
     */
    @RequestMapping("/getCurUser")
    public SysUser getCurUser(@CookieValue(name = Constant.USER_TOKEN) String userToken) {
        Object object = redisRepository.get(String.format(Constant.USER_TOKEN_REDIS_KEY, userToken));
        if (object == null || !(object instanceof SysUser)) {
            /**token失效，需要重新登录*/
            throw new BizException(CodeMsg.user_token_invalid);
        }
        return (SysUser) object;
    }


    /**
     * 登出
     * 1.清除captchaToken,userToken [cookie]
     * 2.清除userToken    [redis]
     */
    @RequestMapping("/loginOut")
    public String loginOut() {
        ShiroUtils.logout();
        // 从header中获取token
        String userToken = request.getHeader(Constant.USER_TOKEN);
        if (StringUtils.isNotEmpty(userToken)) {
//            CookieUtil.remove(response, Constant.USER_TOKEN);
            redisRepository.del(String.format(Constant.USER_TOKEN_REDIS_KEY, userToken));
            removeContext();
        }
        return SUCCESS;
    }

    /**
     * 通过角色ID查询菜单权限
     */
    @RequestMapping("/getMenuPermissionsMap")
    public Map<String, Boolean> getUserPermissionsMap() {
        List<SysPermission> sysPermissions = sysPermissionService.getPermissionsByUserId(getCurUserId());
        List<SysPermission> allSysPermissions = sysPermissionService.selectList(new EntityWrapper<>());
        if (sysPermissions == null || sysPermissions.isEmpty()) {
            throw new BizException(CodeMsg.user_no_permission);
        }
        Map<String, Boolean> permissionMap = new HashMap<>(allSysPermissions.size());
        for (SysPermission sysPermission : allSysPermissions) {
            if (sysPermissions.contains(sysPermission)) {
                permissionMap.put(sysPermission.getResourceCode(), true);
            } else {
                permissionMap.put(sysPermission.getResourceCode(), false);
            }
        }
        return permissionMap;
    }

}
