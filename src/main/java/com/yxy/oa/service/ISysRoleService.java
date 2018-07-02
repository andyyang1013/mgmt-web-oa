package com.yxy.oa.service;

import com.baomidou.mybatisplus.service.IService;
import com.yxy.oa.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统角色表 服务类
 */
public interface ISysRoleService extends IService<SysRole> {


    /**
     * 新增系统角色,批量插入角色和权限的关系表
     *
     * @param sysRole
     */
    void insertRole(SysRole sysRole);

    /**
     * 修改系统角色,批量修改角色和权限的关系表
     *
     * @param sysRole
     */
    void updateRole(SysRole sysRole);

    /**
     * 根据用户Id查询系统角色
     *
     * @param userId
     * @return List<SysRole>
     */
    List<SysRole> getRolesByUserId(@Param("userId") Long userId);


    /**
     * 删除角色
     *
     * @param sysRole
     */
    void deleteByRole(SysRole sysRole);

    /**
     * 判断用户组名称是否存在
     *
     * @param roleName
     * @return
     */
    boolean existRoleName(String roleName);

    /**
     * 查询角色对应的所有按钮权限ID
     * @param roleId
     * */
    List<Long> getPermissionIdsByRoleId(Long roleId);

    boolean existUserByRole(Long roleId);

    List<SysRole> selectList(SysRole sysRole);
}
