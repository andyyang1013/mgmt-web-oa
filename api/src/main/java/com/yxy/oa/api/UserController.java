package com.yxy.oa.api;

import com.yxy.oa.Constant;
import com.yxy.oa.config.filter.UserReqContextUtil;
import com.yxy.oa.encrypt.EncryptFactory;
import com.yxy.oa.entity.SubsidiaryInfo;
import com.yxy.oa.entity.User;
import com.yxy.oa.entity.UserBindRelation;
import com.yxy.oa.entity.enums.AccountTypeEnum;
import com.yxy.oa.entity.enums.IdNumStateEnum;
import com.yxy.oa.entity.enums.SexEnum;
import com.yxy.oa.exception.BizException;
import com.yxy.oa.exception.CodeMsg;
import com.yxy.oa.service.IUserBindRelationService;
import com.yxy.oa.service.IUserInfoService;
import com.yxy.oa.service.IUserModifyRecordService;
import com.yxy.oa.service.IUserService;
import com.yxy.oa.util.CookieUtil;
import com.yxy.oa.util.JacksonUtil;
import com.yxy.oa.util.Toolkit;
import com.yxy.oa.vo.UserVO;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户相关api接口
 **/
@RestController
@RequestMapping("/api")
@Api
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IUserModifyRecordService userModifyRecordService;
    @Autowired
    private IUserBindRelationService userBindRelationService;

    /**
     * 手机/邮箱验证码登录，不需要会员中心验证。
     *
     * @param account 手机号/邮箱
     * @return
     */
    @PostMapping("/loginWithVerifyCode")
    public UserVO loginWithVerifyCode(String account) {
        logger.info("验证码登录：account=" + account + ",token过期时间：" + request.getHeader("tokenExpireTime"));
        UserVO userVO = new UserVO();
        if (Toolkit.isEmail(account)) {
            userVO.setEmail(account);
        } else {
            userVO.setPhone(account);
        }
        long tokenExpireTime = Constant.USER_TOKEN_EXPIRE;
        try {
            tokenExpireTime = Long.parseLong(request.getHeader("tokenExpireTime"));
        } catch (NumberFormatException e) {
            logger.info("客户端传递的过期时间格式，无法解析成long类型", e);
        }

        //如果超过了最大时间，1年，需要提示用户
        if (tokenExpireTime > Constant.MAX_EXPIRE_TIME) {
            throw new BizException(CodeMsg.user_token_expire_time_too_long);
        }

        // 生成user token
        String token = Toolkit.makeToken();
        redisRepository.set(String.format(Constant.USER_TOKEN_REDIS_KEY, token), userVO, tokenExpireTime, TimeUnit.SECONDS);
        CookieUtil.add(response, Constant.USER_TOKEN, token, Constant.USER_TOKEN_EXPIRE);
        logger.info("登录成功：userToken=" + token);

        return userVO;
    }

    /**
     * 由子公司去识别是账号、邮箱或者手机号登录，兼容有些子公司对账号、手机号、邮箱登录有限制的
     *
     * @param account     手机/邮箱/用户名
     * @param password    前端加密后的密码，按照md5;pdkdf2格式存储
     * @param accountType 账户类型，1 手机 2 邮箱 3 用户名,如果不传则按照账号匹配
     * @return
     */
    @PostMapping("/loginDiff")
    public UserVO loginDiff(String account, String password, int accountType) {
        logger.info("登录：account=" + account + ",accountType=" + accountType + ",password=" + password + ",token过期时间：" + request.getHeader("tokenExpireTime"));

        List<UserVO> matchUsers = new ArrayList<>();

        /**请求参数空值判断*/
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            throw new BizException(CodeMsg.param_note_blank);
        }

        if (AccountTypeEnum.PHONE.getCode() == accountType) {
            SubsidiaryInfo curSubsidiaryInfo = UserReqContextUtil.getSubsidiaryInfo();
            List<UserVO> users = userService.selectByPhone(account);
            verifyUsers(password, matchUsers, curSubsidiaryInfo, users);
        } else if (AccountTypeEnum.EMAIL.getCode() == accountType) {
            SubsidiaryInfo curSubsidiaryInfo = UserReqContextUtil.getSubsidiaryInfo();
            List<UserVO> users = userService.selectByEmail(account);
            //根据账号查询
            verifyUsers(password, matchUsers, curSubsidiaryInfo, users);
        } else {
            SubsidiaryInfo curSubsidiaryInfo = UserReqContextUtil.getSubsidiaryInfo();
            List<UserVO> users = userService.selectByAccount(account);
            //根据账号查询
            verifyUsers(password, matchUsers, curSubsidiaryInfo, users);
        }

        //如果为空，表示账号密码错误，提示用户
        if (matchUsers.isEmpty()) {
            throw new BizException(CodeMsg.account_password_error);
        }

        //默认取最后登录时间最晚的一条记录，查询已经按照最后登录时间倒叙，所以取第一条记录即可。
        UserVO loginUser = matchUsers.get(0);
        //记录最后登录时间和token
        recordToken(loginUser);

        return loginUser;
    }

    /**
     * 自动识别是账号、邮箱或者手机号
     *
     * @param account  手机/邮箱/用户名
     * @param password 前端加密后的密码，按照md5;pdkdf2格式存储
     * @return
     */
    @PostMapping("/login")
    public UserVO login(String account, String password) {
        logger.info("登录：account=" + account + ",password=" + password + ",token过期时间：" + request.getHeader("tokenExpireTime"));

        List<UserVO> matchUsers = new ArrayList<>();

        /**请求参数空值判断*/
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            throw new BizException(CodeMsg.param_note_blank);
        }

        //如果是邮箱格式
        if (Toolkit.isEmail(account)) {
            SubsidiaryInfo curSubsidiaryInfo = UserReqContextUtil.getSubsidiaryInfo();
            List<UserVO> users = userService.selectByEmail(account);
            //根据账号查询
            verifyUsers(password, matchUsers, curSubsidiaryInfo, users);
        } else {
            SubsidiaryInfo curSubsidiaryInfo = UserReqContextUtil.getSubsidiaryInfo();
            List<UserVO> users = userService.selectByAccountOrPhone(account);
            //根据账号查询
            verifyUsers(password, matchUsers, curSubsidiaryInfo, users);
        }

        //如果为空，表示账号密码错误，提示用户
        if (matchUsers.isEmpty()) {
            throw new BizException(CodeMsg.account_password_error);
        }

        //默认取最后登录时间最晚的一条记录，查询已经按照最后登录时间倒叙，所以取第一条记录即可。
        UserVO loginUser = matchUsers.get(0);
        //记录最后登录时间和token
        recordToken(loginUser);

        return loginUser;
    }

    /**
     * 修改token过期时间
     *
     * @param expireTime token过期时间，可以为正数/负数，单位秒
     * @return
     */
    @PostMapping("/modifyTokenExpireTime")
    public boolean modifyTokenExpireTime(long expireTime) {
        long tokenExpireTime = expireTime;
        String token = UserReqContextUtil.getToken();
        redisRepository.expire(String.format(Constant.USER_TOKEN_REDIS_KEY, token), tokenExpireTime, TimeUnit.SECONDS);
        CookieUtil.add(response, Constant.USER_TOKEN, token, (int) tokenExpireTime);

        return true;
    }

    /**
     * 用户注册
     *
     * @param param 用户基础信息
     * @return
     */
    @PostMapping("/register")
    public UserVO register(@Valid UserVO param) {
        logger.info("注册用户信息：" + param.toString());

        /**账号/邮箱/手机号同时为空，不接收*/
        if (StringUtils.isEmpty(param.getAccount()) && StringUtils.isEmpty(param.getEmail()) && StringUtils.isEmpty(param.getPhone())) {
            throw new BizException(CodeMsg.param_note_blank);
        }

        //用户名格式，由于杂乱不一，暂时只验证不包含@符号即可
        if (StringUtils.isNotEmpty(param.getAccount()) && param.getAccount().contains("@")) {
            throw new BizException(CodeMsg.account_format_error);
        }
        /**密码为空，不接收*/
        if (StringUtils.isEmpty(param.getPassword())) {
            throw new BizException(CodeMsg.param_note_blank);
        }

        //判断有没有重复的账号，邮箱或者手机号
        if (StringUtils.isNotEmpty(param.getAccount())) {
            List<UserVO> users = userService.selectByAccount(param.getAccount());
            if (users != null && !users.isEmpty()) {
                throw new BizException(CodeMsg.user_account_exist);
            }
        }
        if (StringUtils.isNotEmpty(param.getEmail())) {
            List<UserVO> users = userService.selectByEmail(param.getEmail());
            if (users != null && !users.isEmpty()) {
                throw new BizException(CodeMsg.user_email_exist);
            }
        }
        if (StringUtils.isNotEmpty(param.getPhone())) {
            List<UserVO> users = userService.selectByPhone(param.getPhone());
            if (users != null && !users.isEmpty()) {
                throw new BizException(CodeMsg.user_phone_exist);
            }
        }
        SubsidiaryInfo curSubsidiaryInfo = UserReqContextUtil.getSubsidiaryInfo();
        param.setSubsidiaryCode(curSubsidiaryInfo.getSubsidiaryCode());
        return userService.register(param);
    }

    /**
     * 用户注册结果确认
     *
     * @param id    用户id
     * @param state 子公司处理状态
     * @return
     */
    @PostMapping("/registerConfirm")
    public boolean registerConfirm(Long id, int state) {

        boolean result = userService.registerConfirm(id, state);
        logger.info("注册用户id：" + id + ",确认结果：" + (result ? "成功" : "失败"));

        return result;
    }

    /**
     * 记录最后登录时间和token
     *
     * @param loginUser
     * @return
     */

    private void recordToken(UserVO loginUser) {
        long tokenExpireTime = Constant.USER_TOKEN_EXPIRE;
        try {
            tokenExpireTime = Long.parseLong(request.getHeader("tokenExpireTime"));
        } catch (NumberFormatException e) {
            logger.info("客户端传递的过期时间格式，无法解析成long类型", e);
        }

        //如果超过了最大时间，1年，需要提示用户
        if (tokenExpireTime > Constant.MAX_EXPIRE_TIME) {
            throw new BizException(CodeMsg.user_token_expire_time_too_long);
        }

        //记录最后登录时间
        Date lastLoginTime = Toolkit.getCurDate();
        User curUser = new User();
        curUser.setId(loginUser.getId());
        curUser.setLastLoginTime(lastLoginTime);
        curUser.setUpdateTime(lastLoginTime);
        curUser.setUpdateUid(curUser.getId());
        userService.updateLastLoginTimeById(curUser);

        // 生成user token
        String token = Toolkit.makeToken();
        redisRepository.set(String.format(Constant.USER_TOKEN_REDIS_KEY, token), loginUser, tokenExpireTime, TimeUnit.SECONDS);
        CookieUtil.add(response, Constant.USER_TOKEN, token, (int) tokenExpireTime);
        logger.info("登录成功：userToken=" + token);
    }

    /**
     * 根据请求参数，获取合法的用户
     *
     * @param password
     * @param resultUsers
     * @param curSubsidiaryInfo
     * @param users
     * @return
     */
    private void verifyUsers(String password, List<UserVO> resultUsers, SubsidiaryInfo curSubsidiaryInfo, List<UserVO> users) {
        //根据账号查询
        if (users != null && !users.isEmpty()) {
            for (UserVO user : users) {
                if (curSubsidiaryInfo.getSubsidiaryCode().equals(user.getSubsidiaryCode())) {
                    String fitPassword = EncryptFactory.builder().getFitPassword(user.getEncryptType(), password);
                    if (EncryptFactory.builder().verifyBackPassword(user.getEncryptType(), fitPassword, user.getSalt(), user.getPassword())) {
                        resultUsers.add(user);
                    }
                    users.remove(user);
                    break;
                }
            }

            //如果resultUsers集合为空，表示从当前的子公司中找不到数据
            if (resultUsers.isEmpty()) {
                //由于历史数据原因，可能会存在多条记录，需要全部返回给前端，供用户选择登录哪一个子系统的
                for (UserVO user : users) {
                    String fitPassword = EncryptFactory.builder().getFitPassword(user.getEncryptType(), password);
                    if (EncryptFactory.builder().verifyBackPassword(user.getEncryptType(), fitPassword, user.getSalt(), user.getPassword())) {
                        resultUsers.add(user);
                    }
                }
            }
        }
    }

    /**
     * 前端全局验证用户登录状态（每个页面调用一次）
     *
     * @param userToken
     * @return
     */
    @RequestMapping("/checkLoginStatus")
    public boolean checkLoginStatus(@CookieValue(name = Constant.USER_TOKEN, required = false) String userToken) {
        if (StringUtils.isEmpty(userToken)) {
            throw new BizException(CodeMsg.token_not_blank);
        }
        Object object = redisRepository.get(String.format(Constant.USER_TOKEN_REDIS_KEY, userToken));
        if (object == null || !(object instanceof User)) {
            /** token失效，需要重新登录 */
            throw new BizException(CodeMsg.user_token_invalid);
        }
        return TRUE;
    }

    /**
     * 根据token获取当前登录的用户,当前用户信息已经在拦截器中处理过
     *
     * @return
     */
    @RequestMapping("/getCurLoginUser")
    public User getCurLoginUser() {
        return UserReqContextUtil.getRequestUser();
    }


    /**
     * 登出
     * 1.清除captchaToken,userToken [cookie]
     * 2.清除userToken    [redis]
     */
    @RequestMapping("/loginOut")
    public boolean loginOut() {
        String userToken = CookieUtil.getCookieValue(request, Constant.USER_TOKEN);
        if (StringUtils.isNotEmpty(userToken)) {
            CookieUtil.remove(response, Constant.USER_TOKEN);
            redisRepository.del(String.format(Constant.USER_TOKEN_REDIS_KEY, userToken));
        }
        return TRUE;
    }


    /**
     * 批量查询用户信息
     */
    @PostMapping("/batchQueryUserInfo")
    public Map<String, List<UserVO>> batchQueryUserInfo(String accounts, Integer accountType) {
        if (StringUtils.isEmpty(accounts) || accountType == null) {
            throw new BizException(CodeMsg.param_note_blank);
        }
        String[] accountArr = accounts.split(",");
        int allowNum = 10;
        if (accountArr.length > allowNum) {
            throw new BizException(CodeMsg.user_batch_query_num_out);
        }
        Map<String, List<UserVO>> res = new HashMap<String, List<UserVO>>(allowNum);
        for (String account : accountArr) {
            List<UserVO> voList = null;
            switch (accountType) {
                case 1:
                    voList = userService.selectByPhone(account);
                    break;
                case 2:
                    voList = userService.selectByEmail(account);
                    break;
                case 3:
                    voList = userService.selectByAccount(account);
                    break;

                default:
                    break;
            }
            if (!CollectionUtils.isEmpty(voList)) {
                res.put(account, voList);
            }
        }
        if (res == null || res.isEmpty()) {
            throw new BizException(CodeMsg.record_not_exist);
        }

        return res;
    }

    /**
     * 查询用户信息
     *
     * @param account
     * @param accountType 账户类型  1 手机号 2 邮箱 3 账号
     * @return 用户信息
     */
    @PostMapping("/queryUserInfo")
    public List<UserVO> queryUserInfo(String account, Integer accountType) {
        if (StringUtils.isEmpty(account) || accountType == null) {
            throw new BizException(CodeMsg.param_note_blank);
        }

        List<UserVO> voList = null;
        switch (accountType) {
            case 1:
                voList = userService.selectByPhone(account);
                break;
            case 2:
                voList = userService.selectByEmail(account);
                break;
            case 3:
                voList = userService.selectByAccount(account);
                break;

            default:
                break;
        }
        if (voList == null) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        return voList;
    }

    /**
     * 通过id查询用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    @PostMapping("/queryUserInfoById")
    public UserVO queryUserInfoById(Long id) {
        if (id == null) {
            throw new BizException(CodeMsg.param_note_blank);
        }
        UserVO vo = userService.queryUserInfoById(id);
        if (vo == null) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        return vo;
    }


    /**
     * 查询用户是否存在
     *
     * @param account     用户名
     * @param accountType 账户类型 1手机 2邮箱 3用户名
     * @return
     */
    @PostMapping("/exsistUser")
    public boolean exsistUser(String account, Integer accountType) {
        if (StringUtils.isEmpty(account) || accountType == null) {
            throw new BizException(CodeMsg.param_note_blank);
        }

        return userService.exsistUser(account, accountType);
    }

    /**
     * 修改个人资料
     * 可修改除账号外的所有信息，新的手机号和邮箱号唯一
     *
     * @param param 个人资料
     * @return 更新后的个人资料
     */
    @PostMapping(path = "/userinfo/update")
    public UserVO updateUserInfo(@Valid UserVO param) {

        logger.info("修改个人资料:param=" + JacksonUtil.toJson(param));

        if (param.getId() == null) {
            throw new BizException(CodeMsg.id_param_blank);
        }

        if (StringUtils.isBlank(param.getPhone()) && StringUtils.isBlank(param.getEmail()) && StringUtils.isBlank(param.getNickName()) && StringUtils.isBlank(param.getImage()) && StringUtils.isBlank(param.getName()) && StringUtils.isBlank(param.getIdNumber()) && param.getSex() == null && param.getIdNumberState() == null) {
            throw new BizException(CodeMsg.param_note_blank);
        }

        if (param.getSex() != null) {
            if (SexEnum.getInstance(param.getSex()) == null) {
                throw new BizException(CodeMsg.user_sex_error);
            }
        }

        if (StringUtils.isNotBlank(param.getEmail())) {
            boolean isEmail = Toolkit.isEmail(param.getEmail());
            if (!isEmail) {
                throw new BizException(CodeMsg.user_email_error);
            }
        }

        if (StringUtils.isNotBlank(param.getPhone())) {
            boolean isPhone = Toolkit.isPhoneNum(param.getPhone());
            if (!isPhone) {
                throw new BizException(CodeMsg.user_phone_error);
            }
        }

        if (param.getIdNumberState() != null) {
            if (IdNumStateEnum.getInstance(param.getIdNumberState()) == null) {
                throw new BizException(CodeMsg.user_idNumberState_error);
            }
        }

        // 获取子公司信息
        SubsidiaryInfo curSubsidiaryInfo = UserReqContextUtil.getSubsidiaryInfo();
        String subsidiaryCode = curSubsidiaryInfo.getSubsidiaryCode();

        // 操作用户ID
        Long opeUid = UserReqContextUtil.getRequestUserId();
        // 操作时间
        Date curDate = Toolkit.getCurDate();

        UserVO userParam = new UserVO();
        BeanUtils.copyProperties(param, userParam);

        UserVO modifiedUserVO = userService.updateUserInfo(opeUid, curDate, subsidiaryCode, userParam);

        logger.info("修改个人资料成功");
        return modifiedUserVO;
    }

    /**
     * 账号关联接口
     * 当前登录的账号可关联手机号或者邮箱匹配的账号
     *
     * @param curUserId   当前用户ID
     * @param bindUserIds 绑定的用户ID集合，用逗号分隔
     * @return 关联账号信息
     */
    @PostMapping("/accounts/bind")
    public String bindAccounts(@RequestParam Long curUserId, @RequestParam String bindUserIds) {
        logger.info("账号关联接口:curUserId=" + curUserId + ",bindUserIds=" + bindUserIds);

        if (curUserId == null || StringUtils.isBlank(bindUserIds)) {
            throw new BizException(CodeMsg.param_note_blank);
        }

        int max = 200;
        if (bindUserIds.length() > max) {
            throw new BizException(CodeMsg.user_bind_over_max);
        }

        UserBindRelation curUserBind = userBindRelationService.selectByUserId(curUserId);
        if (curUserBind == null || curUserBind.getId() == null) {
            UserBindRelation userBind = new UserBindRelation();
            userBind.setUserId(curUserId);
            userBind.setBindUserIds(bindUserIds);
            userBind.setCreateTime(Toolkit.getCurDate());
            userBind.setCreateUid(curUserId);
            userBind.setUpdateTime(Toolkit.getCurDate());
            userBind.setUpdateUid(curUserId);
            userBindRelationService.insert(userBind);
        } else {
            curUserBind.setBindUserIds(bindUserIds);
            curUserBind.setUpdateTime(Toolkit.getCurDate());
            curUserBind.setUpdateUid(curUserId);
            userBindRelationService.updateById(curUserBind);
        }
        logger.info("账号关联成功");
        return SUCCESS;
    }

    /**
     * 修改密码
     *
     * @param id               用户ID
     * @param sourcePassword   原密码,md5和PBKDF2(明文，固定盐)，中间用分号隔开，MD5在前，PBDF2在后
     * @param modifiedPassword 新密码
     * @return 用户信息
     */
    @PostMapping("/account/modifyPassword")
    public UserVO modifyPassword(@RequestParam Long id, @RequestParam String sourcePassword, @RequestParam String modifiedPassword) {
        logger.info("修改密码:id=" + id + ",sourcePassword=" + sourcePassword + ",modifiedPassword=" + modifiedPassword);

        if (id == null || StringUtils.isBlank(sourcePassword) || StringUtils.isBlank(modifiedPassword)) {
            throw new BizException(CodeMsg.param_note_blank);
        }

        if (modifiedPassword.length() > Constant.MAX_PASSWORD_LENGTH) {
            throw new BizException(CodeMsg.user_password_too_long);
        }

        // 获取子公司信息
        SubsidiaryInfo curSubsidiaryInfo = UserReqContextUtil.getSubsidiaryInfo();
        String subsidiaryCode = curSubsidiaryInfo.getSubsidiaryCode();

        // 操作用户ID
        Long opeUid = UserReqContextUtil.getRequestUserId();

        UserVO modifiedUserVO = userService.modifyPassword(opeUid, subsidiaryCode, id, sourcePassword, modifiedPassword);

        logger.info("修改密码成功");
        return modifiedUserVO;
    }

    /**
     * 重置密码
     *
     * @param id          用户ID
     * @param newPassword 新密码
     * @return 用户信息
     */
    @PostMapping("/account/resetPassword")
    public UserVO resetPassword(@RequestParam Long id, @RequestParam String newPassword) {
        logger.info("重置密码:id=" + id + ",newPassword=" + newPassword);

        if (id == null || StringUtils.isBlank(newPassword)) {
            throw new BizException(CodeMsg.param_note_blank);
        }

        if (newPassword.length() > Constant.MAX_PASSWORD_LENGTH) {
            throw new BizException(CodeMsg.user_password_too_long);
        }

        // 获取子公司信息
        SubsidiaryInfo curSubsidiaryInfo = UserReqContextUtil.getSubsidiaryInfo();
        String subsidiaryCode = curSubsidiaryInfo.getSubsidiaryCode();

        // 操作用户ID
        Long opeUid = UserReqContextUtil.getRequestUserId();

        UserVO modifiedUserVO = userService.resetPassword(opeUid, subsidiaryCode, id, newPassword);

        logger.info("重置密码成功");
        return modifiedUserVO;
    }

    /**
     * 重置密码（根据手机号码）
     *
     * @param phone       手机号码
     * @param newPassword 新密码
     * @return 用户信息列表
     */
    @PostMapping("/account/resetPassByPhone")
    public List<UserVO> resetPasswordByPhone(@RequestParam String phone, @RequestParam String newPassword) {
        logger.info("重置密码:phone=" + phone + ",newPassword=" + newPassword);

        if (StringUtils.isBlank(newPassword)) {
            throw new BizException(CodeMsg.param_note_blank);
        }

        // 获取子公司信息
        SubsidiaryInfo curSubsidiaryInfo = UserReqContextUtil.getSubsidiaryInfo();
        String subsidiaryCode = curSubsidiaryInfo.getSubsidiaryCode();

        // 操作用户ID
        Long opeUid = UserReqContextUtil.getRequestUserId();

        List<UserVO> modifiedUserVOList = userService.resetPasswordByPhone(opeUid, subsidiaryCode, phone, newPassword);

        logger.info("重置密码成功");
        return modifiedUserVOList;
    }

    /**
     * 重置密码（根据邮箱）
     *
     * @param email       邮箱
     * @param newPassword 新密码
     * @return 用户信息列表
     */
    @PostMapping("/account/resetPassByEmail")
    public List<UserVO> resetPasswordByEmail(@RequestParam String email, @RequestParam String newPassword) {
        logger.info("重置密码:email=" + email + ",newPassword=" + newPassword);

        if (StringUtils.isBlank(newPassword)) {
            throw new BizException(CodeMsg.param_note_blank);
        }

        // 获取子公司信息
        SubsidiaryInfo curSubsidiaryInfo = UserReqContextUtil.getSubsidiaryInfo();
        String subsidiaryCode = curSubsidiaryInfo.getSubsidiaryCode();

        // 操作用户ID
        Long opeUid = UserReqContextUtil.getRequestUserId();

        List<UserVO> modifiedUserVOList = userService.resetPasswordByEmail(opeUid, subsidiaryCode, email, newPassword);

        logger.info("重置密码成功");
        return modifiedUserVOList;
    }

}
