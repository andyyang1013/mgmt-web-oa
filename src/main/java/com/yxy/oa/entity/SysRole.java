package com.yxy.oa.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统角色表
 */
@TableName("t_sys_role")
@Data
public class SysRole extends Model<SysRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 角色状态：0 正常 1 禁用，默认0
     */
    private Integer disabled;
    /**
     * 角色编码
     */
    @TableField("role_code")
    private String roleCode;
    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;
    /**
     * 角色描述
     */
    private String description;

    /**
     * 超级管理员标记
     */
    @TableField("system_type")
    private Integer systemType;
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

    private transient List<SysPermission> permissions;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
