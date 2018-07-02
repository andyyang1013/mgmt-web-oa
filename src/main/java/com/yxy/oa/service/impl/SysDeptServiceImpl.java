package com.yxy.oa.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yxy.oa.entity.SysDept;
import com.yxy.oa.mapper.SysDeptMapper;
import com.yxy.oa.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统部门表 服务实现类
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {
    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 新增系统部门
     *
     * @param sysDept
     */
    @Override
    public void insertDept(SysDept sysDept) {
        sysDeptMapper.insert(sysDept);
    }

    /**
     * 修改系统部门
     *
     * @param sysDept
     */
    @Override
    public void updateDept(SysDept sysDept) {
        sysDeptMapper.updateById(sysDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteDept(SysDept sysDept) {
        sysDeptMapper.deleteById(sysDept.getId());
    }

    @Override
    public boolean existDeptName(String deptName) {
        int count = sysDeptMapper.existDeptName(deptName);
        return count > 0;
    }

    @Override
    public boolean existRoleByDept(Long deptId){
        int count = sysDeptMapper.existRoleByDept(deptId);
        return count > 0;
    }

    @Override
    public List<SysDept> selectList(SysDept sysDept) {
        return sysDeptMapper.selectList(sysDept);
    }
}
