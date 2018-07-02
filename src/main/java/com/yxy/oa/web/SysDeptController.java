package com.yxy.oa.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxy.oa.entity.SysDept;
import com.yxy.oa.exception.BizException;
import com.yxy.oa.exception.CodeMsg;
import com.yxy.oa.service.ISysDeptService;
import com.yxy.oa.util.StringUtil;
import com.yxy.oa.util.Toolkit;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统部门表 前端控制器
 */
@RestController
@RequestMapping("/sysDept")
public class SysDeptController extends BaseController {
    /**
     * 日志记录
     */
    private Logger logger = LoggerFactory.getLogger(SysDeptController.class);

    @Autowired
    private ISysDeptService sysDeptService;
    /**
     * 查询系统部门,带分页
     *
     * @param sysDept 系统部门对象
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("sysDept:list")
    public PageInfo<SysDept> getList(@RequestBody SysDept sysDept) {
        PageHelper.startPage(sysDept.getPage(), sysDept.getLimit());
        List<SysDept> depts = sysDeptService.selectList(sysDept);
        PageInfo<SysDept> pageInfo = new PageInfo<>(depts);
        return pageInfo;
    }

    /**
     * 查询系统部门
     *
     * @param sysDept 系统部门对象
     * @return
     */
    @RequestMapping("/listNoPage")
    public List<SysDept> getListNoPage(@RequestBody SysDept sysDept) {
        return sysDeptService.selectList(sysDept);
    }


    /**
     * 新增系统部门
     *
     * @param sysDept 系统部门对象
     */
    @RequestMapping("/insert")
    @RequiresPermissions("sysDept:insert")
    public String insert(@RequestBody SysDept sysDept) {
        if (StringUtil.hasNull(sysDept.getName())) {
            throw new BizException(CodeMsg.param_note_blank);
        }
        if (sysDeptService.existDeptName(sysDept.getName())) {
            throw new BizException(CodeMsg.role_name_exist);
        }
        if (getCurUserEntity().getSystemType() != 1) {
            throw new BizException(CodeMsg.user_no_permission);
        }
        sysDept.setCreateUid(getCurUserId());
        sysDept.setCreateTime(Toolkit.getCurDate());
        sysDept.setUpdateUid(getCurUserId());
        sysDept.setUpdateTime(Toolkit.getCurDate());
        sysDeptService.insertDept(sysDept);
        return SUCCESS;
    }

    /**
     * 修改系统部门
     * @param sysDept 系统部门对象
     */
    @RequestMapping("/update")
    @RequiresPermissions("sysDept:update")
    public String update(@RequestBody SysDept sysDept) {
        if (StringUtil.hasNull(sysDept.getName())) {
            throw new BizException(CodeMsg.param_note_blank);
        }
        SysDept dbSysDept = sysDeptService.selectById(sysDept.getId());
        if (dbSysDept == null) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        if (!dbSysDept.getName().equals(sysDept.getName()) && sysDeptService.existDeptName(sysDept.getName())) {
            throw new BizException(CodeMsg.role_name_exist);
        }
        if (getCurUserEntity().getSystemType() != 1) {
            throw new BizException(CodeMsg.user_no_permission);
        }
        sysDept.setUpdateUid(getCurUserId());
        sysDept.setUpdateTime(Toolkit.getCurDate());
        sysDeptService.updateDept(sysDept);
        return SUCCESS;
    }

    /**
     * 删除系统部门
     *
     * @param id 主键id
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sysDept:delete")
    public String deleteById(Long id) {
        if (id == null) {
            throw new BizException(CodeMsg.id_param_blank);
        }
        SysDept dbSysDept = sysDeptService.selectById(id);
        if (dbSysDept == null) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        if (getCurUserEntity().getSystemType() != 1) {
            throw new BizException(CodeMsg.user_no_permission);
        }
        if (sysDeptService.existRoleByDept(id)) {
            throw new BizException(CodeMsg.dept_role_exist);
        }
        sysDeptService.deleteDept(dbSysDept);
        return SUCCESS;
    }
}