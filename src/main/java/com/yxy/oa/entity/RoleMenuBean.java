package com.yxy.oa.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 用于前端角色授权菜单数据渲染
 * */
@Getter
@Setter
public class RoleMenuBean implements Serializable{
    /**
     * 菜单Id
     */
    private Long id;

    /**
     * 父菜单Id
     */
    private Long parentId;

    /**
     * 父菜单名
     */
    private String label;

    /**
     * 是否禁用
     */
    private boolean disabled;

    /**
     * 字菜单
     */
    private List<RoleMenuBean> children;
}
