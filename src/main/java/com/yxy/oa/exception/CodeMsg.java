package com.yxy.oa.exception;

/**
 * 异常枚举类
 **/
public enum CodeMsg {
    //1、异常码定义区
    success("成功", "200"),
    system_error("系统出错，请稍后再试", "500"),
    url_not_effective("URL不合法，请确认", "501"),
    not_allow_visit("不允许访问", "401"),
    resource_not_found("你访问的资源不存在", "404"),
    param_type_not_match("参数类型不匹配,请检查参数的格式", "500"),
    account_format_error("用户名不能包含@符号", "500"),
    business_error("业务异常，请稍后再试", "400"),

    //业务异常
    id_param_blank("参数id不能为空", "biz_001"),
    account_password_error("用户名或密码出错", "biz_002"),
    param_note_blank("参数不能为空", "biz_003"),
    record_not_exist("记录数据不存在", "biz_004"),
    token_not_blank("token不能为空", "biz_005"),
    apiKey_not_empty("apiKey不能为空", "biz_006"),
    apiKey_is_invalid("apiKey无效", "biz_007"),
    subsidiary_not_access("当前公司没有接入，请在管理后台修改", "biz_0071"),
    sign_is_invalid("签名数据无效", "biz_008"),
    user_token_blank("token不能为空", "biz_009"),
    user_token_expire_time_too_long("过期时间不能超过1年", "biz_0091"),
    user_token_invalid("token无效", "biz_0092"),
    user_no_permission("权限不足", "biz_010"),
    user_not_effective("用户未生效", "biz_011"),
    user_has_disabled("用户已禁用", "biz_012"),
    user_has_lapsed("用户已过期", "biz_013"),
    user_account_not_blank("用户名不能为空", "biz_014"),
    user_password_not_blank("密码不能为空", "biz_015"),
    user_password_too_long("密码长度最大128位", "biz_0151"),
    user_account_exist("账号已存在", "biz_016"),
    user_account_too_long("账号不能超过20个字符", "biz_0161"),
    subsidiary_find_name_code("子公司名称或标识已存在", "biz_017"),
    user_batch_query_num_out("超出用户批量查询个数限制", "biz_018"),
    user_phone_duplicate("用户手机号重复", "biz_019"),
    user_email_duplicate("用户邮箱重复", "biz_020"),
    account_password_repeat("新密码不能与旧密码重复", "biz_021"),
    password_format_error("密码格式有误", "biz_022"),
    user_email_exist("邮箱已存在", "biz_023"),
    user_phone_exist("手机号已存在", "biz_024"),
    user_sex_error("性别错误", "biz_025"),
    user_email_error("邮箱格式错误", "biz_026"),
    user_phone_error("手机号格式错误", "biz_027"),
    user_idNumberState_error("身份证号验证状态错误", "biz_028"),
    user_bind_over_max("绑定的用户数超过最大限制", "biz_029"),
    role_name_exist("用户组已存在", "biz_030");


    //2、属性
    private String code; //返回码
    private String msg;     //前端提示信息（默认显示内容）

    //3、构造方法
    CodeMsg(String msg, String code) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
