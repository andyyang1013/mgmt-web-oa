package com.yxy.oa.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yxy.oa.entity.RoleMenuBean;
import com.yxy.oa.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统权限资源表 Mapper 接口
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {


    /**
     * 查询系统权限表,带分页
     *
     * @param entry
     * @return List<SysPermission>
     */
    List<SysPermission> getList(SysPermission entry);

    /**
     * 根据角色Id查询系统权限表
     *
     * @param roleId
     * @return List<SysPermission>
     */
    List<SysPermission> getPermissionsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户Id查询系统权限表
     *
     * @param userId
     * @return List<SysPermission>
     */
    List<SysPermission> getPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户Id查询系统权限表ID
     *
     * @param userId
     * @return List<SysPermission>
     */
    List<String> getUserPermissionIds(@Param("userId") Long userId);

    /**
     * 获取角色权限的授权菜单树数据
     *
     * @return
     */
    List<RoleMenuBean> selectAllMenusTree();
}