package com.yxy.oa.service;

import com.baomidou.mybatisplus.service.IService;
import com.yxy.oa.entity.SysDept;

/**
 * 系统部门表 服务类
 */
public interface ISysDeptService extends IService<SysDept> {
    /**
     * 新增系统部门
     *
     * @param sysDept
     */
    void insertDept(SysDept sysDept);

    /**
     * 修改系统部门
     *
     * @param sysDept
     */
    void updateDept(SysDept sysDept);

    /**
     * 删除部门
     *
     * @param sysDept
     */
    void deleteDept(SysDept sysDept);

    boolean existDeptName(String deptName);

    /**
     * 判断部门下是否存在角色
     * @param deptId
     * @return
     */
    boolean existRoleByDept(Long deptId);
}
