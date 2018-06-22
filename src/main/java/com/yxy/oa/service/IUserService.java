package com.yxy.oa.service;

import com.baomidou.mybatisplus.service.IService;
import com.yxy.oa.entity.User;
import com.yxy.oa.vo.UserVO;

import java.util.Date;
import java.util.List;

/**
 * 账户认证表 服务类
 */
public interface IUserService extends IService<User> {

    /**
     * 更新租后登录时间
     *
     * @param user 用户对象
     * @return
     */
    void updateLastLoginTimeById(User user);

    /**
     * 根据账号和子公司编码查询
     *
     * @param account        账号
     * @param subsidiaryCode 子公司编码
     * @return
     */
    List<UserVO> selectByAccountAndSubCode(String account, String subsidiaryCode);


    /**
     * 根据账号获取用户对象集合
     *
     * @param account 用户账号
     * @return
     */
    List<UserVO> selectByAccount(String account);

    /**
     * 通过邮箱，手机号，账户中的一个查询用户信息
     *
     * @param phone   手机号
     * @param email   邮箱
     * @param account 账户
     * @return 用户信息
     */
    List<UserVO> select(String phone, String email, String account);

    /**
     * 判断用户是否存在
     *
     * @param account     邮箱，手机号，账户中的一个
     * @param accountType 账户类型
     * @return true/false
     */

    boolean exsistUser(String account, Integer accountType);


    /**
     * 根据邮箱获取用户对象集合
     *
     * @param email 用户邮箱
     * @return
     */
    List<UserVO> selectByEmail(String email);

    /**
     * 根据手机号获取用户对象集合
     *
     * @param phone 手机号
     * @return
     */
    List<UserVO> selectByPhone(String phone);

    /**
     * 根据账号/手机号获取用户对象集合
     *
     * @param account 用户账号/手机号
     * @return
     */
    List<UserVO> selectByAccountOrPhone(String account);

    UserVO selectBySubCodeAndSubUserId(String subUserId, String subCode);

    /**
     * 获取用户信息列表
     *
     * @param userVO
     * @return
     */
    List<UserVO> getUserList(UserVO userVO);

    /**
     * 重置密码
     *
     * @param id
     * @return
     */
    int resetPassword(Long id);

    /**
     * 用户注册
     *
     * @param param 用户对象
     * @return
     */
    UserVO register(UserVO param);

    /**
     * 用户注册确认
     *
     * @param id    用户对象id
     * @param state 状态，1 成功 其他失败
     * @return
     */
    boolean registerConfirm(long id, int state);

    /**
     * 用户信息推送服务
     *
     * @param user 用户对象
     * @return
     */
    boolean pushUserInfo(UserVO user);

    /**
     * 根据用户ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    UserVO selectByUserId(Long id);

    /**
     * 修改个人资料
     * 可修改除账号外的所有信息，新的手机号和邮箱号唯一
     *
     * @param opeUid         操作用户ID
     * @param curDate        操作时间
     * @param subsidiaryCode 子公司标识
     * @param userParam      个人资料参数
     * @return 更新后的个人资料
     */
    UserVO updateUserInfo(Long opeUid, Date curDate, String subsidiaryCode, UserVO userParam);

    /**
     * 修改密码
     *
     * @param opeUid           操作用户ID
     * @param subsidiaryCode   子公司标识
     * @param id               用户ID
     * @param sourcePassword   原密码
     * @param modifiedPassword 新密码
     * @return 用户信息
     */
    UserVO modifyPassword(Long opeUid, String subsidiaryCode, Long id, String sourcePassword, String modifiedPassword);

    /**
     * 查询会员信息
     *
     * @param id 用户ID
     * @return
     */
    UserVO queryUserInfoById(Long id);

    /**
     * 条件查询总记录
     *
     * @param userVO
     * @return
     */
    int selectTotal(UserVO userVO);

    /**
     * 重置密码
     *
     * @param opeUid         操作用户ID
     * @param subsidiaryCode 子公司编码
     * @param id             用户ID
     * @param newPassword    新密码
     * @return 用户信息
     */
    UserVO resetPassword(Long opeUid, String subsidiaryCode, Long id, String newPassword);

    /**
     * 手动清除缓存，通过注解无法完成的事情
     *
     * @param user
     * @return
     */
    void clearCache(UserVO user);


    /**
     * 手动清除缓存，通过注解无法完成的事情
     *
     * @param user
     * @return
     */
    void clearCache(User user);

    /**
     * 重置密码（根据手机号码）
     *
     * @param opeUid         操作用户ID
     * @param subsidiaryCode 子公司编码
     * @param phone          手机号码
     * @param newPassword    新密码
     * @return 用户信息列表
     */
    List<UserVO> resetPasswordByPhone(Long opeUid, String subsidiaryCode, String phone, String newPassword);

    /**
     * 重置密码（根据邮箱）
     *
     * @param opeUid         操作用户ID
     * @param subsidiaryCode 子公司编码
     * @param email          邮箱
     * @param newPassword    新密码
     * @return 用户信息列表
     */
    List<UserVO> resetPasswordByEmail(Long opeUid, String subsidiaryCode, String email, String newPassword);
}
