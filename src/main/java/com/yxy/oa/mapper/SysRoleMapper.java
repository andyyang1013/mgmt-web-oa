package com.yxy.oa.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yxy.oa.entity.SysRole;
import com.yxy.oa.entity.SysRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统角色表 Mapper 接口
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {


    /**
     * 批量插入角色和权限的关系表
     *
     * @param sysRolePermissions
     */
    void insertPermRelations(@Param("permissions") List<SysRolePermission> sysRolePermissions);

    /**
     * 根据用户Id查询系统角色
     *
     * @param userId
     * @return List<SysRole>
     */
    List<SysRole> getRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据角色id删除角色和权限表的信息
     *
     * @param roleId
     */
    void deletePermRelationsByRoleId(@Param("roleId") Long roleId);


    /**
     * 根据角色id删除角色和用户表信息
     *
     * @param roleId
     */
    void deleteUsersByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据名称查询角色
     *
     * @param roleName
     * @return
     */
    SysRole selectByRoleName(@Param("roleName") String roleName);
}