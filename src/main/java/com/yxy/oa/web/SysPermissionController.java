package com.yxy.oa.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxy.oa.entity.RoleMenuBean;
import com.yxy.oa.entity.SysPermission;
import com.yxy.oa.exception.BizException;
import com.yxy.oa.exception.CodeMsg;
import com.yxy.oa.service.ISysPermissionService;
import com.yxy.oa.util.Toolkit;
import com.yxy.oa.vo.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统权限资源表 前端控制器
 */
@RestController
@RequestMapping("/sysPermission")
public class SysPermissionController extends BaseController {

    /**
     * 日志记录
     */
    private Logger logger = LoggerFactory.getLogger(SysPermissionController.class);


    @Autowired
    private ISysPermissionService sysPermissionService;

    /**
     * 查询系统权限表,带分页
     *
     * @param sysPermission 系统权限表对象
     * @return
     */
    @RequestMapping("/list")
    public PageInfo<SysPermission> getList(SysPermission sysPermission, Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), true);
        List<SysPermission> list = sysPermissionService.selectList(new EntityWrapper<>(sysPermission));
        PageInfo<SysPermission> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }


    /**
     * 查询系统权限表，按照树结构生成
     *
     * @return
     */
    @RequestMapping(value = "/treelist")
    public List<SysPermission> getTreeList() {
        PageHelper.startPage(1, 0);
        return sysPermissionService.getTreeList();
    }

    /**
     * 新增系统权限表
     *
     * @param sysPermission 系统权限表对象
     */
    @RequestMapping("/insert")
    public String insert(SysPermission sysPermission) {
        sysPermission.setCreateUid(getCurUserId());
        sysPermission.setCreateTime(Toolkit.getCurDate());
        sysPermission.setUpdateUid(getCurUserId());
        sysPermission.setUpdateTime(Toolkit.getCurDate());
        sysPermissionService.insert(sysPermission);
        return SUCCESS;
    }

    /**
     * 修改系统权限表
     *
     * @param sysPermission 系统权限表对象
     */
    @RequestMapping("/update")
    public String update(SysPermission sysPermission) {
        SysPermission dbSysPermission = sysPermissionService.selectById(sysPermission.getId());
        if (dbSysPermission == null || StringUtils.isEmpty(dbSysPermission.getId())) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        dbSysPermission.setDisabled(sysPermission.getDisabled());
        dbSysPermission.setResourceCode(sysPermission.getResourceCode());
        dbSysPermission.setName(sysPermission.getName());
        dbSysPermission.setParentId(sysPermission.getParentId());
        dbSysPermission.setResourceType(sysPermission.getResourceType());
        dbSysPermission.setUrl(sysPermission.getUrl());
        dbSysPermission.setUpdateUid(getCurUserId());
        dbSysPermission.setUpdateTime(Toolkit.getCurDate());
        sysPermissionService.updateById(dbSysPermission);
        return SUCCESS;
    }

    /**
     * 根据Id查询系统权限表
     *
     * @param id 主键id
     */
    @RequestMapping("/get")
    public SysPermission getById(Long id) {
        if (id == null) {
            throw new BizException(CodeMsg.id_param_blank);
        }
        SysPermission sysPermission = sysPermissionService.selectById(id);
        if (sysPermission == null) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        return sysPermission;
    }

    /**
     * 删除系统权限表
     *
     * @param id 主键id
     */
    @RequestMapping("/delete")
    public String deleteById(Long id) {
        if (id == null) {
            throw new BizException(CodeMsg.id_param_blank);
        }
        sysPermissionService.deleteById(id);
        return SUCCESS;
    }

    /**
     * 获取用户登录后的侧边栏菜单元素与元素权限
     * 根据当前登录用户ID获取菜单元素
     *
     * @return
     */
    @RequestMapping("/menusList")
    public Map<String, Object> selectAllMenus() {
        List<SysPermission> menuList = sysPermissionService.getTreeList();
        List<String> permissions = sysPermissionService.getUserPermissionPerms(getCurUserId());
        Map<String, Object> map = new HashMap();
        map.put("menuList", menuList);
        map.put("permissions", permissions);
        return map;
    }

    /**
     * 获取角色权限的授权菜单树
     */
    @RequestMapping("/treeList")
    public List<RoleMenuBean> selectAllMenusTree() {
        List<RoleMenuBean> treeList = sysPermissionService.selectAllMenusTree();
        return treeList;
    }
}
