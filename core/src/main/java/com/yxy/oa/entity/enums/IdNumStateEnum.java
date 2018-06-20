package com.yxy.oa.entity.enums;

/**
 * 身份证验证状态
 **/
public enum IdNumStateEnum {
    /**
     * 未验证
     */
    NO_VERIFY(0, "未验证"),
    /**
     * 已验证
     */
    VERIFIED(1, "已验证");


    private int state; //状态
    private String remark;//描述

    IdNumStateEnum(int state, String remark) {
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
        for (IdNumStateEnum curEnum : values()) {
            if (curEnum.getState() == state) {
                return curEnum.getRemark();
            }
        }
        return null;
    }

    /**
     * 获取实例
     *
     * @param state 状态
     * @return 实例
     */
    public static IdNumStateEnum getInstance(int state) {
        for (IdNumStateEnum ins : IdNumStateEnum.values()) {
            if (ins.state == state) {
                return ins;
            }
        }
        return null;
    }
}
