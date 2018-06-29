package com.yxy.oa.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxy.oa.Constant;
import com.yxy.oa.entity.SysRole;
import com.yxy.oa.exception.BizException;
import com.yxy.oa.exception.CodeMsg;
import com.yxy.oa.service.ISysPermissionService;
import com.yxy.oa.service.ISysRoleService;
import com.yxy.oa.util.CookieUtil;
import com.yxy.oa.util.StringUtil;
import com.yxy.oa.util.Toolkit;
import com.yxy.oa.vo.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * 系统角色表 前端控制器
 */
@RestController
@RequestMapping("/sysRole")
@Api(tags = {"系统角色表"}, description = "系统角色表服务")
public class SysRoleController extends BaseController {
    /**
     * 日志记录
     */
    private Logger logger = LoggerFactory.getLogger(SysRoleController.class);


    @Autowired
    private ISysRoleService sysRoleService;


    @Autowired
    private ISysPermissionService sysPermissionService;

    /**
     * 查询系统角色,带分页
     *
     * @param sysRole 系统角色对象
     * @return
     */
    @RequestMapping("/list")
    @ApiOperation(value = "查询系统角色列表", notes = "根据条件查询系统角色列表", httpMethod = "POST", response = PageInfo.class)
    @RequiresPermissions("sysRole:list")
    public PageInfo<SysRole> getList(@RequestBody SysRole sysRole) {
        PageHelper.startPage(sysRole.getPage(), sysRole.getLimit());
        List<SysRole> roles = sysRoleService.selectList(new EntityWrapper<>(sysRole));
        for (SysRole role : roles) {
            role.setPermissions(sysPermissionService.getPermissionsByRoleId(role.getId()));
        }
        PageInfo<SysRole> pageInfo = new PageInfo<>(roles);
        return pageInfo;
    }

    /**
     * 查询系统角色
     *
     * @param sysRole 系统角色对象
     * @return
     */
    @RequestMapping("/listNoPage")
    @ApiOperation(value = "查询系统角色列表", notes = "根据条件查询系统角色列表", httpMethod = "POST", response = List.class)
    public List<SysRole> getListNoPage(@RequestBody SysRole sysRole) {
        return sysRoleService.selectList(new EntityWrapper<>(sysRole));
    }


    /**
     * 新增系统角色，权限集合是把选择的权限资源id组装到一起，中间用逗号分隔
     *
     * @param sysRole 系统角色对象
     */
    @RequestMapping("/insert")
    @ApiOperation(value = "新增系统角色", notes = "新增系统角色，必填项不能为空", httpMethod = "POST", response = String.class)
    @RequiresPermissions("sysRole:insert")
    public String insert(@RequestBody SysRole sysRole) {
        if (StringUtil.hasNull(sysRole.getRoleName())) {
            throw new BizException(CodeMsg.param_note_blank);
        }
        if (sysRoleService.existRoleName(sysRole.getRoleName())) {
            throw new BizException(CodeMsg.role_name_exist);
        }
        sysRole.setRoleCode(Toolkit.randomUUID());
        sysRole.setCreateUid(getCurUserId());
        sysRole.setCreateTime(Toolkit.getCurDate());
        sysRole.setUpdateUid(getCurUserId());
        sysRole.setDisabled(0);
        sysRole.setSystemType(0);
        sysRole.setUpdateTime(Toolkit.getCurDate());
        sysRoleService.insertRole(sysRole);
        return SUCCESS;
    }

    /**
     * 修改系统角色
     * @param sysRole 系统角色对象
     */
    @RequestMapping("/update")
    @ApiOperation(value = "更新系统角色", notes = "更新系统角色，需要主键Id，必填项不能为空", httpMethod = "POST", response = String.class)
    @RequiresPermissions("sysRole:update")
    public String update(@RequestBody SysRole sysRole) {
        SysRole dbSysRole = sysRoleService.selectById(sysRole.getId());
        if (dbSysRole == null || StringUtils.isEmpty(dbSysRole.getId())) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        //判断是否为超级管理员
        if (dbSysRole.getSystemType() != 1) {
            throw new BizException(CodeMsg.user_no_permission);
        }
        if (!dbSysRole.getRoleName().equals(sysRole.getRoleName()) && sysRoleService.existRoleName(sysRole.getRoleName())) {
            throw new BizException(CodeMsg.role_name_exist);
        }
        //校验权限树子id有父id传入
        if (!sysPermissionService.existParentId(sysRole.getPermissionIds())) {
            throw new BizException(CodeMsg.param_note_blank);
        }
        dbSysRole.setRoleCode(sysRole.getRoleCode());
        dbSysRole.setRoleName(sysRole.getRoleName());
        dbSysRole.setDescription(sysRole.getDescription());
        dbSysRole.setUpdateUid(getCurUserId());
        dbSysRole.setUpdateTime(Toolkit.getCurDate());
        dbSysRole.setPermissionIds(sysRole.getPermissionIds());
        sysRoleService.updateRole(dbSysRole);
        //权限发生改变时更新当前登录用户权限缓存
        sysPermissionService.updateLoginUserPermission(getCurUserId(), CookieUtil.getCookieValue(request, Constant.USER_TOKEN));
        return SUCCESS;
    }


    /**
     * 启用禁用系统角色
     *
     * @param id       系统角色id
     * @param disabled 启用禁用状态
     */
    @RequestMapping("/modifyType")
    @ApiOperation(value = "启用禁用系统角色", notes = "启用禁用系统角色，需要主键Id，必填项不能为空", httpMethod = "POST", response = String.class)
    @RequiresPermissions("sysRole:modifyType")
    public String modifyType(Long id, Integer disabled) {
        SysRole dbSysRole = sysRoleService.selectById(id);
        if (dbSysRole == null || StringUtils.isEmpty(dbSysRole.getId())) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        //判断是否为超级管理员
        if (dbSysRole.getSystemType() == 1) {
            throw new BizException(CodeMsg.user_no_permission);
        }
        dbSysRole.setDisabled(disabled);
        dbSysRole.setUpdateUid(getCurUserId());
        dbSysRole.setUpdateTime(Toolkit.getCurDate());
        sysRoleService.updateById(dbSysRole);
        //权限发生改变时更新当前登录用户权限缓存
        sysPermissionService.updateLoginUserPermission(getCurUserId(), CookieUtil.getCookieValue(request, Constant.USER_TOKEN));
        return SUCCESS;
    }

    /**
     * 根据Id查询系统角色
     *
     * @param id 主键id
     */
    @RequestMapping("/getById")
    @ApiOperation(value = "获取系统角色对象", notes = "根据主键Id获取系统角色对象", httpMethod = "POST", response = String.class)
    @RequiresPermissions("sysRole:update")
    public SysRole getById(Long id) {
        if (id == null) {
            throw new BizException(CodeMsg.id_param_blank);
        }
        SysRole sysRole = sysRoleService.selectById(id);
        if (sysRole == null) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        return sysRole;
    }

    /**
     * 删除系统角色
     *
     * @param id 主键id
     */
    @RequestMapping("/delete")
    @ApiOperation(value = "删除系统角色对象", notes = "根据主键Id删除系统角色对象", httpMethod = "POST", response = String.class)
    @RequiresPermissions("sysRole:delete")
    public String deleteById(Long id) {
        if (id == null) {
            throw new BizException(CodeMsg.id_param_blank);
        }
        SysRole sysRole = sysRoleService.selectById(id);
        if (sysRole == null) {
            throw new BizException(CodeMsg.resource_not_found);
        }
        //判断是否为超级管理员
        if (sysRole.getSystemType() == 1) {
            throw new BizException(CodeMsg.user_no_permission);
        }
        sysRoleService.deleteByRole(sysRole);
        //权限发生改变时更新当前登录用户权限缓存
        sysPermissionService.updateLoginUserPermission(getCurUserId(), CookieUtil.getCookieValue(request, Constant.USER_TOKEN));
        return SUCCESS;
    }

    /**
     * 根据用户Id查询系统角色
     *
     * @param userId 系统角色对象
     * @return
     */
    @RequestMapping("/getRolesByUserId")
    @ApiOperation(value = "根据用户Id查询系统角色", notes = "根据用户Id查询系统角色", httpMethod = "POST", response = List.class)
    public List<SysRole> getRolesByUserId(Long userId) {
        return sysRoleService.getRolesByUserId(userId);
    }

    /**
     * @param roleId 角色ID
     * @return
     */
    @RequestMapping("/getPermIdsByRoleId")
    @ApiOperation(value = "根据角色ID查询权限ID集合", notes = "根据角色ID查询权限ID集合", httpMethod = "POST", response = List.class)
    public List<Long> getPermissionIdsByRoleId(Long roleId) {
        return sysRoleService.getPermissionIdsByRoleId(roleId);
    }
}