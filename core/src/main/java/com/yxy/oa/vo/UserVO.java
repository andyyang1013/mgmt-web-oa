package com.yxy.oa.vo;/**
 * Created by dell on 2018/2/26.
 */

import com.yxy.oa.entity.User;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 用户基本信息
 **/
@Data
public class UserVO extends User {

    /**
     * 导出页数
     */
    private Integer exportNum;

    /**
     * 导出数量
     */
    private Integer exportSize;

    /**
     * 用户昵称
     */
    @Size(max = 50, message = "昵称不能超过20位")
    private String nickName;
    /**
     * 性别,0 保密1 男 2 女 默认为0
     */
    @Min(value = 0, message = "性别枚举值只能为0/1/2")
    @Max(value = 2, message = "性别枚举值只能为0/1/2")
    private Integer sex;

    /**
     * 性别全称
     */
    private String sexStr;

    /**
     * 头像url地址
     */
    @Size(max = 255, message = "头像url地址超过最大长度限制")
    @Pattern(regexp = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?", message = "头像地址格式有误")
    private String image;
    /**
     * 真实姓名
     */
    @Size(max = 20, message = "真实姓名不能超过20位")
    private String name;
    /**
     * 身份证号
     */
    @Size(min = 15, max = 18, message = "身份证号不能超过18位")
    private String idNumber;
    /**
     * 身份证号验证状态,0 未验证 1已验证 默认 0
     */
    private Integer idNumberState;

    private Date startLastLoginTime;

    private Date endLastLoginTime;

    private Date startRegTime;

    private Date endRegTime;

    /**
     * 子公司名字
     */
    private String subsidiaryName;


}
