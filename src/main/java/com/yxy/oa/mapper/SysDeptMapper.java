package com.yxy.oa.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yxy.oa.entity.SysDept;
import org.apache.ibatis.annotations.Param;

/**
 * 系统部门表 Mapper 接口
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {
    /**
     * 新增系统部门
     *
     * @param sysDept
     */
    void insertDept(SysDept sysDept);

    /**
     * 修改系统部门
     *
     * @param deptId
     */
    void updateDept(@Param("deptId") Long deptId);

    /**
     * 删除部门
     *
     * @param deptId
     */
    void deleteDept(@Param("deptId") Long deptId);

    int existDeptName(String deptName);

    int existRoleByDept(Long deptId);
}