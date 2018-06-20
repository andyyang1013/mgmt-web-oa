package com.yxy.oa.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统权限资源表
 */
@TableName("t_sys_permission")
@Data
public class SysPermission extends Model<SysPermission> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 状态：0 正常 1 禁用，默认 0
     */
    private Integer disabled;
    /**
     * 资源编码
     */
    @TableField("resource_code")
    private String resourceCode;
    /**
     * 资源名称
     */
    @TableField("resource_name")
    private String resourceName;
    /**
     * 资源父ID
     */
    @TableField("parent_id")
    private Long parentId;
    /**
     * 资源类型
     */
    @TableField("resource_type")
    private Integer resourceType;
    /**
     * 资源url，类似/login
     */
    private String url;
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
     * 子资源数
     */
    private transient int childCount;

    /**
     * 子资源列表
     */
    private transient List<SysPermission> childs;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysPermission that = (SysPermission) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
