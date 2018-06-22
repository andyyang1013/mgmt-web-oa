package com.yxy.oa.vo;


import com.yxy.oa.entity.SysUser;
import lombok.Data;

/**
 * 请前端传递的参数
 **/
@Data
public class SysUserVo extends SysUser {
    /**
     * 角色id
     */
    private Long roleId;

}
