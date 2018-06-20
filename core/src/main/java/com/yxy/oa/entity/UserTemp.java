package com.yxy.oa.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息临时表，为了接收历史数据
 */
@TableName("t_user_temp")
public class UserTemp extends Model<UserTemp> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户名/账号
     */
    private String account;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 密码
     */
    @TableField("pwd")
    private String password;
    /**
     * 加密盐，只对新账号有效
     */
    private String salt;
    /**
     * 子公司标识
     */
    @TableField("subsidiary_code")
    private String subsidiaryCode;
    /**
     * 子公司用户id，新注册的和主键保持一致
     */
    @TableField("subsidiary_user_id")
    private String subsidiaryUserId;
    /**
     * 注册时间
     */
    @TableField("reg_time")
    private Date regTime;
    /**
     * 最近登录时间
     */
    @TableField("last_login_time")
    private Date lastLoginTime;
    /**
     * 用户昵称
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * 性别,0 保密1 男 2 女 默认为0
     */
    private Integer sex;
    /**
     * 头像url地址
     */
    private String image;
    /**
     * 真实姓名
     */
    @TableField("true_name")
    private String name;
    /**
     * 身份证号
     */
    @TableField("id_number")
    private String idNumber;

    /**
     * 身份证号验证状态,0 未验证 1已验证 默认 0
     */
    @TableField("id_number_state")
    private Integer idNumberState;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSubsidiaryCode() {
        return subsidiaryCode;
    }

    public void setSubsidiaryCode(String subsidiaryCode) {
        this.subsidiaryCode = subsidiaryCode;
    }

    public String getSubsidiaryUserId() {
        return subsidiaryUserId;
    }

    public void setSubsidiaryUserId(String subsidiaryUserId) {
        this.subsidiaryUserId = subsidiaryUserId;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }


    public Integer getIdNumberState() {
        return idNumberState;
    }

    public void setIdNumberState(Integer idNumberState) {
        this.idNumberState = idNumberState;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
