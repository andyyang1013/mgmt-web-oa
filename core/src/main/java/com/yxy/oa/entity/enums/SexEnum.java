package com.yxy.oa.entity.enums;

/**
 * 性别状态
 **/
public enum SexEnum {
    /**
     * 保密
     */
    SECRET(0, "保密"),
    /**
     * 男
     */
    MALE(1, "男"),
    /**
     * 女
     */
    FEMALE(2, "女");


    private int code; //性别
    private String name;//描述

    SexEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * 获取名称
     *
     * @param code
     * @return
     */
    public String getNameByCode(int code) {
        for (SexEnum sexEnum : values()) {
            if (sexEnum.getCode() == code) {
                return sexEnum.getName();
            }
        }
        return null;
    }

    /**
     * 获取实例
     *
     * @param code 性别
     * @return 实例
     */
    public static SexEnum getInstance(int code) {
        for (SexEnum ins : SexEnum.values()) {
            if (ins.code == code) {
                return ins;
            }
        }
        return null;
    }
}
