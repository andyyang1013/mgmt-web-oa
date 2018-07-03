package com.yxy.oa.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxy.oa.Constant;
import com.yxy.oa.entity.SysRole;
import com.yxy.oa.entity.SysUser;
import com.yxy.oa.exception.BizException;
import com.yxy.oa.exception.CodeMsg;
import com.yxy.oa.service.ISysPermissionService;
import com.yxy.oa.service.ISysRoleService;
import com.yxy.oa.service.ISysUserService;
import com.yxy.oa.util.StringUtil;
import com.yxy.oa.util.Toolkit;
import com.yxy.oa.vo.SysUserVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统用户表 前端控制器
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController extends BaseController {


    /**
     * 日志记录
     */
    private Logger logger = LoggerFactory.getLogger(SysUserController.class);


    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysPermissionService sysPermissionService;

    /**
     * 查询系统用户,带分页
     *
     * @param sysUser 系统用户对象
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("sysUser:list")
    public PageInfo<SysUser> getList(@RequestBody SysUserVo sysUser) {
        PageHelper.startPage(sysUser.getPage(), sysUser.getLimit());
        List<SysUser> list = sysUserService.queryUserList(sysUser);
        PageInfo<SysUser> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }


    /**
     * 新增系统用户
     *
     * @param sysUser 系统用户对象
     */
    @RequestMapping("/insert")
    @RequiresPermissions("sysUser:insert")
    public String insert(@RequestBody SysUser sysUser) {
        if (StringUtil.hasNull(sysUser.getAccount(), sysUser.getPassword(), sysUser.getRoleIds())) {
            throw new BizException(CodeMsg.param_note_blank);
        }
        SysUser dbSysUser = sysUserService.getByAccount(sysUser.getAccount());
        if (dbSysUser != null) {
            throw new BizException(CodeMsg.user_account_exist);
        }
        //加盐
        sysUser.setSalt(Toolkit.generateSalt());
        //二次加密
        sysUser.setPassword(Toolkit.encrypt(sysUser.getPassword(), sysUser.getSalt()));
        sysUser.setSystemType(0);
        sysUser.setDisabled(0);
        sysUser.setLocked(0);
        sysUser.setCreateUid(getCurUserId());
        sysUser.setLastLoginTime(Toolkit.getCurDate());
        sysUser.setCreateTime(Toolkit.getCurDate());
        sysUser.setUpdateUid(getCurUserId());
        sysUser.setUpdateTime(Toolkit.getCurDate());
        sysUserService.insertSysUser(sysUser);
        return SUCCESS;
    }

    /**
     * 修改系统用户
     *
     * @param sysUser 系统用户对象
     */
    @RequestMapping("/update")
    @RequiresPermissions("sysUser:update")
    public String update(@RequestBody SysUser sysUser) {
        if (StringUtil.hasNull(sysUser.getRoleIds(), sysUser.getId())) {
            throw new BizException(CodeMsg.param_note_blank);
        }
        SysUser dbSysUser = sysUserService.selectById(sysUser.getId());
        if (dbSysUser == null || StringUtils.isEmpty(dbSysUser.getId())) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        //判断是否为超级管理员
        if (dbSysUser.getSystemType() == 1) {
            throw new BizException(CodeMsg.user_no_permission);
        }
        dbSysUser.setDescription(sysUser.getDescription());
        dbSysUser.setUpdateUid(getCurUserId());
        dbSysUser.setUpdateTime(Toolkit.getCurDate());
        dbSysUser.setRoleIds(sysUser.getRoleIds());
        dbSysUser.setDisabled(sysUser.getDisabled());
        sysUserService.updateUser(dbSysUser);
        //权限发生改变时更新当前登录用户权限缓存
        sysPermissionService.updateLoginUserPermission(getCurUserId(), request.getHeader(Constant.USER_TOKEN));
        return SUCCESS;
    }

    /**
     * 修改系统用户密码
     *
     * @param id 系统用户对象
     */
    @RequestMapping("/updatePass")
    @RequiresPermissions("sysUser:updatePass")
    public String updatePass(Long id, String passWord) {
        if (StringUtil.hasNull(id, passWord)) {
            throw new BizException(CodeMsg.param_note_blank);
        }
        SysUser dbSysUser = sysUserService.selectById(id);
        if (dbSysUser == null || StringUtils.isEmpty(dbSysUser.getId())) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        //判断是否为超级管理员
        if (dbSysUser.getSystemType() == 1) {
            throw new BizException(CodeMsg.user_no_permission);
        }
        dbSysUser.setPassword(Toolkit.encrypt(passWord, dbSysUser.getSalt()));
        dbSysUser.setUpdateUid(getCurUserId());
        dbSysUser.setUpdateTime(Toolkit.getCurDate());
        sysUserService.updateById(dbSysUser);
        return SUCCESS;
    }


    /**
     * 启用禁用系统用户
     *
     * @param id       用户id
     * @param disabled 启用禁用状态
     */
    @RequestMapping("/modifyType")
    @RequiresPermissions("sysUser:modifyType")
    public String modifyType(Long id, Integer disabled) {
        SysUser dbSysUser = sysUserService.selectById(id);
        if (dbSysUser == null || StringUtils.isEmpty(dbSysUser.getId())) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        //判断是否为超级管理员
        if (dbSysUser.getSystemType() == 1) {
            throw new BizException(CodeMsg.user_no_permission);
        }
        dbSysUser.setDisabled(disabled);
        dbSysUser.setUpdateUid(getCurUserId());
        dbSysUser.setUpdateTime(Toolkit.getCurDate());
        sysUserService.updateById(dbSysUser);
        sysPermissionService.updateLoginUserPermission(getCurUserId(), request.getHeader(Constant.USER_TOKEN));
        return SUCCESS;
    }

    /**
     * 根据Id查询系统用户
     *
     * @param id 主键id
     */
    @RequestMapping("/getById")
    @RequiresPermissions("sysUser:update")
    public SysUser getById(Long id) {
        if (id == null) {
            throw new BizException(CodeMsg.id_param_blank);
        }
        SysUser sysUser = sysUserService.selectById(id);
        List<SysRole> roles = sysRoleService.getRolesByUserId(id);
        sysUser.setRoles(roles);
        if (sysUser == null) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        return sysUser;
    }

    /**
     * 删除系统用户
     *
     * @param ids 主键id集合
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sysUser:delete")
    public String deleteUserById(@RequestBody  List<Long> ids) {
        if (ids.isEmpty()) {
            throw new BizException(CodeMsg.id_param_blank);
        }
        sysUserService.deleteUserById(ids);
        return SUCCESS;
    }

}
