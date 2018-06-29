package com.yxy.oa.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxy.oa.entity.SysUser;
import com.yxy.oa.entity.SysUserRole;
import com.yxy.oa.mapper.SysUserMapper;
import com.yxy.oa.service.ISysUserService;
import com.yxy.oa.vo.SysUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统用户表 服务实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysUser> queryUserList(SysUserVo entry) {
        return sysUserMapper.queryUserList(entry);
    }

    @Override
    public void insertRoleRelation(SysUserRole entry) {
        sysUserMapper.insertRoleRelation(entry);
    }

    @Override
    public SysUser getByAccount(String account) {
        return sysUserMapper.getByAccount(account);
    }

    @Override
    public void deleteRoleRelationsByUserId(Long userId) {
        sysUserMapper.deleteRoleRelationsByUserId(userId);
    }

    @Override
    public void deleteUsersByRoleId(Long roleId) {
        sysUserMapper.deleteUsersByRoleId(roleId);
    }

    /**
     * 插入用户和用户角色关系
     **/
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void insertSysUser(SysUser sysUser) {
        sysUserMapper.insert(sysUser);
        /**增加用户和角色的关系*/
        insertUserRole(sysUser);
    }

    @Override
    public List<SysUser> selectListExclude() {
        return sysUserMapper.selectListExclude();
    }

    @Override
    @Transactional(rollbackFor = Exception.class , propagation = Propagation.REQUIRED)
    public void updateUser(SysUser dbSysUser) {
        sysUserMapper.updateById(dbSysUser);
        //先删除之前所有角色关系
        sysUserMapper.deleteRoleRelationsByUserId(dbSysUser.getId());
        //新增用户角色关系
        insertUserRole(dbSysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUser(SysUser sysUser) {
        sysUserMapper.deleteRoleRelationsByUserId(sysUser.getId());
        sysUserMapper.deleteById(sysUser.getId());
    }

    private void insertUserRole(SysUser user) {
        for (Long roleId : user.getRoleIds()) {
            if (roleId != null && roleId.longValue() != 0) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setId(IdWorker.getId());
                sysUserRole.setRoleId(roleId);
                sysUserRole.setUserId(user.getId());
                sysUserRole.setCreateUid(user.getUpdateUid());
                sysUserRole.setCreateTime(user.getCreateTime());
                sysUserRole.setUpdateUid(user.getUpdateUid());
                sysUserRole.setUpdateTime(user.getUpdateTime());
                sysUserMapper.insertRoleRelation(sysUserRole);
            }

        }

    }
}
