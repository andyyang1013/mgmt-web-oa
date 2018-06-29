package com.yxy.oa.service;

import com.baomidou.mybatisplus.service.IService;
import com.yxy.oa.entity.SysUser;
import com.yxy.oa.entity.SysUserRole;
import com.yxy.oa.vo.SysUserVo;

import java.util.List;

/**
 * 系统用户表 服务类
 */
public interface ISysUserService extends IService<SysUser> {


    /**
     * 查询系统用户,带分页
     *
     * @param entry
     * @return List<SysUser>
     */
    List<SysUser> queryUserList(SysUserVo entry);

    /**
     * 新增用户和角色关系表
     *
     * @param entry
     */
    void insertRoleRelation(SysUserRole entry);

    /**
     * 根据登录账号查询系统用户
     *
     * @param account
     * @return SysUser
     */
    SysUser getByAccount(String account);

    /**
     * 删除系统用户和角色关系
     *
     * @param userId
     */
    void deleteRoleRelationsByUserId(Long userId);

    /**
     * 删除角色小所有的用户
     *
     * @param roleId
     */
    void deleteUsersByRoleId(Long roleId);


    /**
     * 插入用户和用户角色关系
     *
     * @param sysUser
     */
    void insertSysUser(SysUser sysUser);


    /**
     * 用户列表,排除加密信息
     *
     * @return
     */
    List<SysUser> selectListExclude();


    /**
     * 修改用户及用户角色
     *
     * @param dbSysUser
     */
    void updateUser(SysUser dbSysUser);

    /**
     * 删除用户
     *
     * @param sysUser
     */
    void deleteByUser(SysUser sysUser);
}
