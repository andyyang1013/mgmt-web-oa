package com.yxy.oa.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统部门表
 */
@TableName("t_sys_department")
@Data
public class SysDept extends Model<SysDept> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 部门名称
     */
    @TableField("name")
    private String name;
    /**
     * 部门描述
     */
    private String description;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建用户Id
     */
    @TableField("create_uid")
    private Long createUid;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 更新用户id
     */
    @TableField("update_uid")
    private Long updateUid;

    private transient List<SysRole> sysRoles;

    // 当前页码
    private transient int page;
    // 每页条数
    private transient int limit;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
