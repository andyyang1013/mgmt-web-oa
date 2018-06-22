package com.yxy.oa.entity.enums;

/**
 * 操作成功/失败
 **/
public enum SuccessEnum {
    /**
     * 操作失败
     */
    FAILURE(0, "操作失败"),

    /**
     * 操作成功
     */
    SUCCESS(1, "操作成功");


    private int state; //状态
    private String remark;//描述

    SuccessEnum(int state, String remark) {
        this.state = state;
        this.remark = remark;
    }

    public int getState() {
        return state;
    }

    public String getRemark() {
        return remark;
    }


    /**
     * 获取名称
     *
     * @param state
     * @return
     */
    public String getNameByState(int state) {
        for (SuccessEnum curEnum : values()) {
            if (curEnum.getState() == state) {
                return curEnum.getRemark();
            }
        }
        return null;
    }
}
