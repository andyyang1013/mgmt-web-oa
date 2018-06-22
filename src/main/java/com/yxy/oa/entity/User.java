package com.yxy.oa.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 账户认证表
 */
@TableName("t_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 用户基础信息id
     */
    @TableField("user_info_id")
    private Long userInfoId;
    /**
     * 用户名/账号
     */
    @Size(max = 50, message = "用户名/账号不能超过20位")
    private String account;
    /**
     * 邮箱
     */
    @Size(max = 50, message = "邮箱不能超过50位")
    @Email(message = "邮箱格式有误")
    private String email;

    /**
     * 手机号
     */
    @Pattern(regexp = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0-9])|(19[0-9]))\\d{8}$", message = "手机号码格式有误")
    private String phone;
    /**
     * 密码
     * 益彩(128)：PBKDF2
     * 一比分：MD5
     * 电竞：sha1+md5
     */
    @Size(max = 128, message = "密码最长128位")
    @TableField("pwd")
    private String password;
    /**
     * 加密盐，只对新账号有效
     */
    private String salt;
    /**
     * 加密类型
     */
    @TableField("encrypt_type")
    private String encryptType;
    /**
     * 子公司标识，注册来源
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
     * 删除状态，0 未删除 1 已删除 默认 0
     */
    @TableField("del_flag")
    private Integer delFlag;
    /**
     * 创建时间
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建用户Id
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField("create_uid")
    private Long createUid;
    /**
     * 更新时间
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField("update_time")
    private Date updateTime;
    /**
     * 更新用户id
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField("update_uid")
    private Long updateUid;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
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

    public String getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(String encryptType) {
        this.encryptType = encryptType;
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

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUid() {
        return createUid;
    }

    public void setCreateUid(Long createUid) {
        this.createUid = createUid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUid() {
        return updateUid;
    }

    public void setUpdateUid(Long updateUid) {
        this.updateUid = updateUid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "User{" +
                ", id=" + id +
                ", userInfoId=" + userInfoId +
                ", account=" + account +
                ", email=" + email +
                ", phone=" + phone +
                ", password=" + password +
                ", salt=" + salt +
                ", encryptType=" + encryptType +
                ", subsidiaryCode=" + subsidiaryCode +
                ", subsidiaryUserId=" + subsidiaryUserId +
                ", regTime=" + regTime +
                ", lastLoginTime=" + lastLoginTime +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", createUid=" + createUid +
                ", updateTime=" + updateTime +
                ", updateUid=" + updateUid +
                "}";
    }
}
