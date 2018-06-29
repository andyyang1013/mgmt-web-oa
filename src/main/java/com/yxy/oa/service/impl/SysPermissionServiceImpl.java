package com.yxy.oa.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yxy.oa.Constant;
import com.yxy.oa.config.filter.UserReqContextUtil;
import com.yxy.oa.entity.RoleMenuBean;
import com.yxy.oa.entity.SysPermission;
import com.yxy.oa.entity.SysRole;
import com.yxy.oa.entity.SysUser;
import com.yxy.oa.mapper.SysPermissionMapper;
import com.yxy.oa.mapper.SysRoleMapper;
import com.yxy.oa.repository.IRedisRepository;
import com.yxy.oa.service.ISysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 系统权限资源表 服务实现类
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private IRedisRepository iRedisRepository;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysPermission> getPermissionsByRoleId(Long roleId) {
        return sysPermissionMapper.getPermissionsByRoleId(roleId);
    }

    /**
     * 查询所有资源，树结构展示
     **/
    @Override
    public List<SysPermission> getTreeList() {
        List<SysPermission> returnPermissions = new ArrayList<>();
        SysPermission condSysPermission = new SysPermission();
        condSysPermission.setDisabled(0);
        condSysPermission.setComponent("-1");
        List<SysPermission> permissions = sysPermissionMapper.getList(condSysPermission);
        getTreeList(returnPermissions, permissions);
        return returnPermissions;
    }


    /**
     * 根据用户Id查询系统权限表
     *
     * @param userId
     * @return List<SysPermission>
     */
    @Override
    public List<SysPermission> getPermissionsByUserId(Long userId) {
        return sysPermissionMapper.getPermissionsByUserId(userId);
    }

    @Override
    public void updateLoginUserPermission(Long userId, String token) {
        String redisKey = String.format(Constant.USER_TOKEN_REDIS_KEY, token);
        Object object = iRedisRepository.get(redisKey);
        List<SysRole> roles = sysRoleMapper.getRolesByUserId(userId);
        for (SysRole role : roles) {
            role.setPermissions(sysPermissionMapper.getPermissionsByRoleId(role.getId()));
        }
        SysUser user = (SysUser) object;
        user.setRoles(roles);
        /**记录登录状态*/
        iRedisRepository.set(redisKey, user, Constant.USER_TOKEN_EXPIRE, TimeUnit.SECONDS);
        UserReqContextUtil.setRequestUser((SysUser) object);
        UserReqContextUtil.setToken(token);
//            PlatformAuthenticationToken userToken = new PlatformAuthenticationToken((SysUser) object,token);
//            try {
//                SecurityUtils.getSubject().login(userToken);
//            } catch (AuthenticationException e) {
//                /**就直接返回给请求者.*/
//            }
    }

    @Override
    public boolean existParentId(List<Long> permissionIds) {
        if (!permissionIds.isEmpty()) {
            for (Long permissionId : permissionIds) {
                //判断是否存在传入id是子id且没有父id传入
                SysPermission sysPermission = sysPermissionMapper.selectById(Long.valueOf(permissionId));
                if (sysPermission.getParentId() != -1 && !permissionIds.contains(sysPermission.getParentId().toString())) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 获取树列表
     **/
    private void getTreeList(List<SysPermission> returnPermissions, List<SysPermission> permissions) {
        int rootParentId = -1;
        for (SysPermission permission : permissions) {
            if (permission.getParentId() == rootParentId) {
                returnPermissions.add(permission);
            }
        }

        for (SysPermission returnPermission : returnPermissions) {
            setChild(returnPermission, permissions);
        }
    }


    /**
     * 设置子元素
     **/
    private void setChild(SysPermission curPermission, List<SysPermission> permissions) {
        if (curPermission.getChildCount() > 0) {
            List<SysPermission> childPermissions = new ArrayList<>();
            for (SysPermission permission : permissions) {
                if (Objects.equals(permission.getParentId(), curPermission.getId())) {
                    childPermissions.add(permission);
//                    setChild(permission, permissions);
                }
                if (permission.getResourceType()==2){
                    // 查询按钮的一级父菜单id
                    SysPermission sysPermission = sysPermissionMapper.selectById(permission.getParentId());
                    if (Objects.equals(sysPermission.getParentId(), curPermission.getId())) {
                        permission.setDisabled(1);
                        childPermissions.add(permission);
                    }
                }
            }
            curPermission.setChildren(childPermissions);
        }
    }

    @Override
    public List<String> getUserPermissionPerms(Long userId) {
        return sysPermissionMapper.getUserPermissionIds(userId);
    }

    @Override
    public List<RoleMenuBean> selectAllMenusTree() {
        List<RoleMenuBean> sysMenusTreeEntities = sysPermissionMapper.selectAllMenusTree();
        List<RoleMenuBean> treeList = getTreeList((long) -1, sysMenusTreeEntities, new ArrayList<RoleMenuBean>());
        setTreeListNull(treeList);
        // 查询属于系统设置的所有节点
//		setDisable(sysMenusTreeEntities);
        return treeList;
    }

    // 递归处理权限树层级
    public List<RoleMenuBean> getTreeList(Long parentId, List<RoleMenuBean> oldList, List<RoleMenuBean> newList) {
        for (RoleMenuBean sysMenusTreeEntity : oldList) {
            if (sysMenusTreeEntity.getParentId() == parentId) {
                sysMenusTreeEntity.setChildren(
                        getTreeList(sysMenusTreeEntity.getId(), oldList, new ArrayList<RoleMenuBean>()));
                newList.add(sysMenusTreeEntity);
            }
        }
        return newList;
    }

    // 递归去除权限树空数组
    public void setTreeListNull(List<RoleMenuBean> itemsMenu) {
        for (RoleMenuBean sysMenusEntity : itemsMenu) {
            if (sysMenusEntity.getChildren().size() == 0) {
                sysMenusEntity.setParentId(null);
                sysMenusEntity.setChildren(null);
            } else {
                setTreeListNull(sysMenusEntity.getChildren());
            }
        }
    }

    // 系统设置的所有节点设为disable
    public void setDisable(List<RoleMenuBean> itemsMenu) {
        for (RoleMenuBean entity : itemsMenu) {
            if (entity.getId() == Constant.SYS_PERMISSION_ID) {
                entity.setDisabled(true);
                setChildDisable(entity.getChildren());
                return;
            }
        }
    }

    public void setChildDisable(List<RoleMenuBean> itemsMenu){
        for (RoleMenuBean entity:itemsMenu) {
            entity.setDisabled(true);
            if(entity.getChildren()!=null){
                setChildDisable(entity.getChildren());
            }
        }
    }
}
