package com.yxy.oa.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户表
 */
@TableName("t_sys_user")
@Data
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 登录账号
     */
    private String account;
    /**
     * 登录密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField("pwd")
    private String password;
    /**
     * 加密的盐
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;
    /**
     * 账号状态：0正常，1禁用
     */
    private Integer disabled;
    /**
     * 是否锁定：0正常，1锁定
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer locked;
    /**
     * 备注
     */
    private String description;
    /**
     * 超级管理员标记
     */
    @TableField("system_type")
    private Integer systemType;
    /**
     * 最近登录时间
     */
    @TableField("last_login_time")
    private Date lastLoginTime;
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


    /**
     * 一个用户具有多个角色
     */
    private transient List<SysRole> roles;

    /**
     * 一个用户具有多个角色
     */
    private transient List<Long> roleIds;

    /**
     * 创建用户名
     */
    private transient String createUserName;

    /**
     * 更新用户名
     */
    private transient String updateUserName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
