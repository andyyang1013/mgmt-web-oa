package com.yxy.oa.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yxy.oa.entity.SysUser;
import com.yxy.oa.entity.SysUserRole;
import com.yxy.oa.vo.SysUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户表 Mapper 接口
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

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
    SysUser getByAccount(@Param("account") String account);

    /**
     * 删除系统用户和角色关系
     *
     * @param userId
     */
    void deleteRoleRelationsByUserId(Long userId);

    /**
     * 批量删除系统用户和角色关系
     *
     * @param userIds
     */
    void deleteRoleRelationsByUserIds(List<Long> userIds);

    /**
     * 批量删除系统用户
     *
     * @param userIds
     */
    int deleteUserById(List<Long> userIds);

    /**
     * 删除角色下所有的用户
     *
     * @param roleId
     */
    void deleteUsersByRoleId(@Param("roleId") Long roleId);

    /**
     * 用户列表,排除加密信息
     *
     * @return
     */
    List<SysUser> selectListExclude();

    /**
     * 查询用户角色所有关系列表
     *
     * @param dbSysUser
     * @return
     */
    List<SysUserRole> selectUserRoles(SysUser dbSysUser);
}