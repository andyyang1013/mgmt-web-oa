package com.yxy.oa.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
import com.yxy.oa.util.CookieUtil;
import com.yxy.oa.util.Md5Util;
import com.yxy.oa.util.StringUtil;
import com.yxy.oa.util.Toolkit;
import com.yxy.oa.vo.Page;
import com.yxy.oa.vo.SysUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统用户表 前端控制器
 */
@RestController
@RequestMapping("/sysUser")
@Api(tags = {"系统权限表"}, description = "系统权限表服务")
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
    @ApiOperation(value = "查询系统用户列表", notes = "根据条件查询系统用户列表", httpMethod = "POST", response = PageInfo.class)
    @RequiresPermissions("sysUser:list")
    public PageInfo<SysUser> getList(SysUserVo sysUser, Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), true);
        List<SysUser> list = sysUserService.selectList(new EntityWrapper<>(sysUser));
        PageInfo<SysUser> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }


    /**
     * 新增系统用户
     *
     * @param sysUser 系统用户对象
     */
    @RequestMapping("/insert")
    @ApiOperation(value = "新增系统用户", notes = "新增系统用户，必填项不能为空", httpMethod = "POST", response = String.class)
    @RequiresPermissions("sysUser:insert")
    public String insert(SysUser sysUser, Long[] roleIds) {
        if (StringUtil.hasNull(sysUser.getAccount(), sysUser.getPassword(), roleIds)) {
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
        sysUserService.insert(sysUser, roleIds);
        return SUCCESS;
    }

    public static void main(String[] args) {
        String s = Md5Util.md5Hex("123456");
        System.out.println(s);
        System.out.println(Toolkit.encrypt(s, "NRw3TkUowVu48HPyZ45tYg=="));
        String member_123 = Md5Util.md5Hex("member_123");
        System.out.println(Toolkit.encrypt(member_123, "NRw3TkUowVu48HPyZ45tYg=="));
    }

    /**
     * 修改系统用户
     *
     * @param sysUser 系统用户对象
     */
    @RequestMapping("/update")
    @ApiOperation(value = "更新系统用户", notes = "更新系统用户，需要主键Id，必填项不能为空", httpMethod = "POST", response = String.class)
    @RequiresPermissions("sysUser:update")
    public String update(SysUser sysUser, Long[] roleIds) {
        if (StringUtil.hasNull(roleIds, sysUser.getId())) {
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
        sysUserService.updateUser(dbSysUser, roleIds);
        //权限发生改变时更新当前登录用户权限缓存
        sysPermissionService.updateLoginUserPermission(getCurUserId(), CookieUtil.getCookieValue(request, Constant.USER_TOKEN));
        return SUCCESS;
    }

    /**
     * 修改系统用户
     *
     * @param id 系统用户对象
     */
    @RequestMapping("/updatePass")
    @ApiOperation(value = "更新系统用户", notes = "更新系统用户，需要主键Id，必填项不能为空", httpMethod = "POST", response = String.class)
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
    @ApiOperation(value = "启用禁用系统用户", notes = "启用禁用系统用户，需要主键Id，必填项不能为空", httpMethod = "POST", response = String.class)
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
        sysPermissionService.updateLoginUserPermission(getCurUserId(), CookieUtil.getCookieValue(request, Constant.USER_TOKEN));
        return SUCCESS;
    }

    /**
     * 根据Id查询系统用户
     *
     * @param id 主键id
     */
    @RequestMapping("/getById")
    @ApiOperation(value = "获取系统用户对象", notes = "根据主键Id获取系统用户对象", httpMethod = "POST", response = String.class)
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
     * @param id 主键id
     */
    @RequestMapping("/delete")
    @ApiOperation(value = "删除系统用户对象", notes = "根据主键Id删除系统用户对象", httpMethod = "POST", response = String.class)
    @RequiresPermissions("sysUser:delete")
    public String deleteById(Long id) {
        if (id == null) {
            throw new BizException(CodeMsg.id_param_blank);
        }
        SysUser sysUser = sysUserService.selectById(id);
        if (sysUser == null) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        //判断是否为超级管理员
        if (sysUser.getSystemType() == 1) {
            throw new BizException(CodeMsg.user_no_permission);
        }
        sysUserService.deleteByUser(sysUser);
        return SUCCESS;
    }

}
