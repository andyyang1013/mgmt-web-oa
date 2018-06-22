package com.yxy.oa.entity.enums;

/**
 * 子公司标识枚举
 **/
public enum SubsidiaryCodeEnum {

    /**
     * 深圳市一比分科技有限公司
     */
    YBF("ybf", "一比分"),
    /**
     * 深圳市益彩网络科技有限公司
     */
    YC("yc", "益彩网络"),
    /**
     * 深圳市乐盈电竞科技有限公司
     */
    DJ("dj", "乐盈电竞"),
    /**
     * 深圳市华体星空体育科技有限公司
     */
    HT("ht", "华体星空"),
    /**
     * 深圳市法义网络科技有限公司
     */
    FY("fy", "法义网络"),
    /**
     * 深圳市柒壹思诺科技有限公司
     */
    QYSN("qysn", "柒壹思诺");


    /**
     * 子公司编码标识
     */
    private String code;

    /**
     * 子公司名称
     */
    private String name;

    SubsidiaryCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
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
    public String getNameByCode(String code) {
        for (SubsidiaryCodeEnum curEnum : values()) {
            if (curEnum.getCode().equals(code)) {
                return curEnum.getName();
            }
        }
        return null;
    }
}
