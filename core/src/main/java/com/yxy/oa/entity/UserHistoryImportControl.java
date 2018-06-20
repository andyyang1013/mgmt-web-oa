package com.yxy.oa.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户历史数据导入控制表
 */
@TableName("t_user_history_import_control")
public class UserHistoryImportControl extends Model<UserHistoryImportControl> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，由于是底层，可以作为业务条件传入
     */
    private Long id;
    /**
     * 导入时间
     */
    @TableField("import_time")
    private Date importTime;
    /**
     * 导入记录条数
     */
    @TableField("import_num")
    private Long importNum;
    /**
     * 导入状态，0失败 1成功
     */
    private Integer state;
    /**
     * 导入耗时，单位毫秒
     */
    @TableField("consume_time")
    private Long consumeTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getImportTime() {
        return importTime;
    }

    public void setImportTime(Date importTime) {
        this.importTime = importTime;
    }

    public Long getImportNum() {
        return importNum;
    }

    public void setImportNum(Long importNum) {
        this.importNum = importNum;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Long consumeTime) {
        this.consumeTime = consumeTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserHistoryImportControl{" +
                ", id=" + id +
                ", importTime=" + importTime +
                ", importNum=" + importNum +
                ", state=" + state +
                ", consumeTime=" + consumeTime +
                "}";
    }
}
