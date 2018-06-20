package com.yxy.oa.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yxy.oa.CommonConstant;
import com.yxy.oa.encrypt.EncryptFactory;
import com.yxy.oa.encrypt.util.YcEncryptUtil;
import com.yxy.oa.entity.SubsidiaryInfo;
import com.yxy.oa.entity.User;
import com.yxy.oa.entity.UserInfo;
import com.yxy.oa.entity.UserModifyRecord;
import com.yxy.oa.entity.enums.DelFlagEnum;
import com.yxy.oa.entity.enums.IdNumStateEnum;
import com.yxy.oa.entity.enums.SubsidiaryCodeEnum;
import com.yxy.oa.entity.enums.SuccessEnum;
import com.yxy.oa.exception.BizException;
import com.yxy.oa.exception.CodeMsg;
import com.yxy.oa.mapper.SubsidiaryInfoMapper;
import com.yxy.oa.mapper.UserInfoMapper;
import com.yxy.oa.mapper.UserMapper;
import com.yxy.oa.mapper.UserModifyRecordMapper;
import com.yxy.oa.repository.IRedisRepository;
import com.yxy.oa.service.INoticeService;
import com.yxy.oa.service.IUserService;
import com.yxy.oa.util.JacksonUtil;
import com.yxy.oa.util.ObjectUtil;
import com.yxy.oa.util.Toolkit;
import com.yxy.oa.vo.UserVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 账户认证表 服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserModifyRecordMapper userModifyRecordMapper;

    @Autowired
    private SubsidiaryInfoMapper subsidiaryInfoMapper;

    @Autowired
    private IRedisRepository redisRepository;

    @Autowired
    private INoticeService noticeService;

    @Override
    public void updateLastLoginTimeById(User user) {
        userMapper.updateLastLoginTimeById(user);
    }

    @Override
    public List<UserVO> selectByAccountAndSubCode(String account, String subsidiaryCode) {
        User query = new User();
        query.setAccount(account);
        query.setSubsidiaryCode(subsidiaryCode);
        return userMapper.selectByAccountAndSubCode(query);
    }

    @Cacheable(value = CommonConstant.CACHE_NAME_FOLDER, key = "#account+'_3'")
    @Override
    public List<UserVO> selectByAccount(String account) {
        return userMapper.selectByAccount(account);
    }

    @Override
    public List<UserVO> select(String phone, String email, String account) {
        int type = 3;
        String targetStr = account;
        if (StringUtils.isNotEmpty(phone)) {
            type = 1;
            targetStr = phone;
        } else if (StringUtils.isNotEmpty(email)) {
            type = 2;
            targetStr = email;
        } else if (StringUtils.isNotEmpty(account)) {
            type = 3;
            targetStr = account;
        } else {
            return null;
        }
        return userMapper.selectByAccountAndType(targetStr, type);
    }

    @Cacheable(value = CommonConstant.CACHE_NAME_FOLDER, key = "'exist_'+#account+'_'+#accountType")
    @Override
    public boolean exsistUser(String account, Integer accountType) {

        Long num = userMapper.exsistUser(account, accountType);
        if (num != null && num > 0) {
            return true;
        }
        return false;
    }

    @Cacheable(value = CommonConstant.CACHE_NAME_FOLDER, key = "#email+'_2'")
    @Override
    public List<UserVO> selectByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    @Cacheable(value = CommonConstant.CACHE_NAME_FOLDER, key = "#phone+'_1'")
    @Override
    public List<UserVO> selectByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }

    @Cacheable(value = CommonConstant.CACHE_NAME_FOLDER, key = "#account+'_13'")
    @Override
    public List<UserVO> selectByAccountOrPhone(String account) {
        return userMapper.selectByAccountOrPhone(account);
    }

    @Cacheable(value = CommonConstant.CACHE_NAME_FOLDER, key = "#id+''")
    @Override
    public UserVO queryUserInfoById(Long id) {

        return userMapper.queryUserInfoById(id);
    }

    @Override
    public int selectTotal(UserVO userVO) {
        return userMapper.selectTotal(userVO);
    }

    @Override
    public UserVO selectBySubCodeAndSubUserId(String subUserId, String subCode) {
        return userMapper.selectBySubCodeAndSubUserId(subUserId, subCode);
    }

    @Override
    public boolean insert(User userVO) {

        return false;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO register(UserVO userVO) {

        //用户扩展信息表
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(userVO.getNickName());
        userInfo.setSex(userVO.getSex());
        userInfo.setImage(userVO.getImage());
        userInfo.setName(userVO.getName());
        userInfo.setIdNumber(userVO.getIdNumber());
        userInfo.setIdNumberState(IdNumStateEnum.NO_VERIFY.getState());
        userInfo.setCreateUid(1L);
        userInfo.setCreateTime(Toolkit.getCurDate());
        userInfo.setUpdateUid(1L);
        userInfo.setUpdateTime(Toolkit.getCurDate());
        userInfoMapper.insert(userInfo);


        //用户认证信息表
        User user = new User();
        user.setUserInfoId(userInfo.getId());
        user.setAccount(userVO.getAccount());
        user.setEmail(userVO.getEmail());
        user.setPhone(userVO.getPhone());
        String salt = null;
        if (SubsidiaryCodeEnum.YC.getCode().equalsIgnoreCase(userVO.getSubsidiaryCode())) {
            try {
                salt = YcEncryptUtil.getSalt();
            } catch (NoSuchAlgorithmException e) {
                logger.error("益彩生成盐失败", e);
            }
        } else {
            salt = Toolkit.generateSalt();
        }
        user.setSalt(salt);
        String backPassword = EncryptFactory.builder().produceBackPassword(userVO.getSubsidiaryCode(), userVO.getPassword(), user.getSalt());
        user.setPassword(backPassword);
        user.setEncryptType(userVO.getSubsidiaryCode());
        user.setSubsidiaryCode(userVO.getSubsidiaryCode());
        user.setRegTime(Toolkit.getCurDate());
        user.setDelFlag(DelFlagEnum.DELETED.getState());
        user.setCreateUid(1L);
        user.setCreateTime(Toolkit.getCurDate());
        user.setUpdateUid(1L);
        user.setUpdateTime(Toolkit.getCurDate());
        userMapper.insert(user);

        //设置id，方便子公司进行映射
        userVO.setId(user.getId());
        userVO.setIdNumberState(IdNumStateEnum.NO_VERIFY.getState());
        userVO.setSalt(user.getSalt());
        userVO.setRegTime(user.getRegTime());
        userVO.setPassword(null);
        userVO.setSalt(null);
        userVO.setUserInfoId(null);

        //手动删除缓存
        clearCache(userVO);

        return userVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean registerConfirm(long id, int state) {

        User user = selectById(id);
        if (user == null) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        //根据当前用户账号，查询是否存在已经注册成功（包括注册和确认成功的记录）的记录，有的话需要提示用户
        //判断有没有重复的账号，邮箱或者手机号
        if (StringUtils.isNotEmpty(user.getAccount())) {
            List<UserVO> users = selectByAccount(user.getAccount());
            if (users != null && !users.isEmpty()) {
                throw new BizException(CodeMsg.user_account_exist);
            }
        }
        if (StringUtils.isNotEmpty(user.getEmail())) {
            List<UserVO> users = selectByEmail(user.getEmail());
            if (users != null && !users.isEmpty()) {
                throw new BizException(CodeMsg.user_email_exist);
            }
        }
        if (StringUtils.isNotEmpty(user.getPhone())) {
            List<UserVO> users = selectByPhone(user.getPhone());
            if (users != null && !users.isEmpty()) {
                throw new BizException(CodeMsg.user_phone_exist);
            }
        }
        //如果成功，需要将注册信息推送给其他子公司
        if (SuccessEnum.SUCCESS.getState() == state) {
            //更改注册用户状态为1
            user.setDelFlag(DelFlagEnum.NO_DELETE.getState());
            updateById(user);

            //推送服务，暂时不做
        } else {
            //注册失败，需要删除已经注册的用户信息
            userInfoMapper.deleteById(user.getUserInfoId());
            deleteById(id);
        }

        //手动删除缓存
        clearCache(user);

        return true;
    }

    @Override
    public boolean pushUserInfo(UserVO user) {
        List<SubsidiaryInfo> subsidiaryInfos = subsidiaryInfoMapper.selectAllAccess();
        for (SubsidiaryInfo subsidiaryInfo : subsidiaryInfos) {
            //调用推送服务

        }
        return true;
    }

    @Override
    public List<UserVO> getUserList(UserVO userVO) {
        return userMapper.selectUserList(userVO);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = CommonConstant.CACHE_NAME_FOLDER, key = "#id+''")})
    @Transactional(rollbackFor = Exception.class)
    public int resetPassword(Long id) {
        String pwd = "";
        String password = "";
        String salt = "";
        UserVO info = userMapper.selectUserInfo(id);
        if (!ObjectUtil.isBlank(info)) {
            try {
                //按规则生成随机密码
                if (!ObjectUtil.isBlank(info.getPhone()) || !ObjectUtil.isBlank(info.getEmail())) {
                    pwd = YcEncryptUtil.getRandomString(8);
                } else {
                    pwd = "hhly1234";
                }
                //生成短信内容
                Map<String, String> paramMap = new HashMap<>();
                paramMap.put("account", info.getAccount());
                paramMap.put("password", pwd);
                String param = JSON.toJSONString(paramMap);
                //推送短信或邮箱
                if (!ObjectUtil.isBlank(info.getPhone())) {
                    noticeService.sendMsg(info.getPhone(), param);
                } else if (!ObjectUtil.isBlank(info.getEmail())) {
                    noticeService.sendEmail(info.getEmail(), param);
                }
                //生成随机盐
                salt = Toolkit.generateSalt();
                //一次加密
                String fristEncrypt = EncryptFactory.builder().produceFrontPassword(info.getSubsidiaryCode(), pwd, "2f1e131cc3009026cf8991da3fd4ac38");
                //二次加密
                password = EncryptFactory.builder().produceBackPassword(info.getSubsidiaryCode(), fristEncrypt, salt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        UserVO user = new UserVO();
        user.setPassword(password);
        user.setSalt(salt);
        user.setId(id);
        int result = userMapper.updateUser(user);

        //手动删除缓存
        clearCache(info);

        return result;
    }

    @Cacheable(value = CommonConstant.CACHE_NAME_FOLDER, key = "#id+''")
    @Override
    public UserVO selectByUserId(Long id) {
        return userMapper.selectByUserId(id);
    }

    @Caching(evict = {@CacheEvict(value = CommonConstant.CACHE_NAME_FOLDER, key = "#userParam.id+''")})
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserVO updateUserInfo(Long opeUid, Date curDate, String subsidiaryCode, UserVO userParam) {

        // 根据userId查询用户是否存在
        UserVO userVO = userMapper.selectByUserId(userParam.getId());
        if (userVO == null) {
            throw new BizException(CodeMsg.record_not_exist);
        }

        // 新的手机号唯一
        if (StringUtils.isNotBlank(userParam.getPhone())) {
            List<UserVO> usersWithSamePhone = userMapper.selectByPhone(userParam.getPhone());
            // 按手机号查询到用户，排除当前用户
            if (CollectionUtils.isNotEmpty(usersWithSamePhone) && usersWithSamePhone.size() > 1) {
                throw new BizException(CodeMsg.user_phone_duplicate);
            }
        }
        // 新的邮箱唯一
        if (StringUtils.isNotBlank(userParam.getEmail())) {
            List<UserVO> usersWithSameEmail = userMapper.selectByEmail(userParam.getEmail());
            // 按邮箱查询到用户，排除当前用户
            if (CollectionUtils.isNotEmpty(usersWithSameEmail) && usersWithSameEmail.size() > 1) {
                throw new BizException(CodeMsg.user_email_duplicate);
            }
        }

        // 用户ID
        Long userId = userVO.getId();
        // 用户信息ID
        Long userInfoId = userVO.getUserInfoId();

        UserVO resourceUserVO = userMapper.selectByUserId(userId);

        User user = new User();
        // 修改除账号外的所有信息
        BeanUtils.copyProperties(userParam, user, "account");
        user.setUpdateTime(curDate);
        user.setUpdateUid(opeUid);
        userMapper.updateById(user);

        // 修改个人资料
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userParam, userInfo);
        userInfo.setId(userInfoId);
        userInfo.setUpdateTime(curDate);
        userInfo.setUpdateUid(opeUid);
        userInfoMapper.updateById(userInfo);

        UserVO modifiedUserVO = userMapper.selectByUserId(userId);

        // 记录修改记录
        UserModifyRecord modifyRecord = new UserModifyRecord();
        modifyRecord.setUserId(userId);
        modifyRecord.setSubsidiaryCode(subsidiaryCode);
        modifyRecord.setResourceContent(JacksonUtil.toJson(resourceUserVO));
        modifyRecord.setModifyContent(JacksonUtil.toJson(modifiedUserVO));
        modifyRecord.setCreateTime(curDate);
        modifyRecord.setCreateUid(opeUid);
        modifyRecord.setUpdateTime(curDate);
        modifyRecord.setUpdateUid(opeUid);
        userModifyRecordMapper.insert(modifyRecord);

        //手动删除缓存
        clearCache(userVO);

        return modifiedUserVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {@CacheEvict(value = CommonConstant.CACHE_NAME_FOLDER, key = "#id+''")})
    public UserVO modifyPassword(Long opeUid, String subsidiaryCode, Long id, String sourcePassword, String modifiedPassword) {
        UserVO userVO = userMapper.selectByUserId(id);
        // 账号不存在
        if (userVO == null) {
            throw new BizException(CodeMsg.record_not_exist);
        }
        // 新密码与原密码一致
        if (sourcePassword.equals(modifiedPassword)) {
            throw new BizException(CodeMsg.account_password_repeat);
        }

        // 原加密类型
        String encryptType = userVO.getEncryptType();
        // 加密盐
        String salt = userVO.getSalt();

        String fitPassword = EncryptFactory.builder().getFitPassword(encryptType, sourcePassword);
        // 密码不正确
        if (!EncryptFactory.builder().verifyBackPassword(encryptType, fitPassword, salt, userVO.getPassword())) {
            throw new BizException(CodeMsg.account_password_error);
        }

        // 用户ID
        Long userId = userVO.getId();
        // 操作时间
        Date curDate = Toolkit.getCurDate();

        // 新加密类型
        String modifiedEncryptType = subsidiaryCode;
        // 新密码
        String password = EncryptFactory.builder().produceBackPassword(modifiedEncryptType, modifiedPassword, salt);

        // 修改密码
        User account = new User();
        account.setId(userId);
        account.setPassword(password);
        account.setEncryptType(modifiedEncryptType);
        account.setUpdateUid(opeUid);
        account.setUpdateTime(curDate);
        userMapper.updateById(account);

        UserVO modifiedUserVO = userMapper.selectByUserId(userId);

        // 记录修改记录
        UserModifyRecord modifyRecord = new UserModifyRecord();
        modifyRecord.setUserId(userId);
        modifyRecord.setSubsidiaryCode(subsidiaryCode);
        modifyRecord.setResourceContent(JacksonUtil.toJson(userVO));
        modifyRecord.setModifyContent(JacksonUtil.toJson(modifiedUserVO));
        modifyRecord.setCreateTime(curDate);
        modifyRecord.setCreateUid(opeUid);
        modifyRecord.setUpdateTime(curDate);
        modifyRecord.setUpdateUid(opeUid);
        userModifyRecordMapper.insert(modifyRecord);

        //手动删除缓存
        clearCache(userVO);

        return modifiedUserVO;
    }


    /**
     * 手动清除缓存，通过注解无法完成的事情
     *
     * @param user
     * @return
     */
    @Override
    public void clearCache(UserVO user) {
        redisRepository.del(user.getAccount() + "_13");
        redisRepository.del(user.getAccount() + "_3");
        redisRepository.del(user.getEmail() + "_2");
        redisRepository.del(user.getPhone() + "_1");
        redisRepository.del(user.getPhone() + "_13");
        redisRepository.del("exist_" + user.getAccount() + "_3");
        redisRepository.del("exist_" + user.getEmail() + "_2");
        redisRepository.del("exist_" + user.getPhone() + "_1");
    }

    /**
     * 手动清除缓存，通过注解无法完成的事情
     *
     * @param user
     * @return
     */
    @Override
    public void clearCache(User user) {
        redisRepository.del(user.getAccount() + "_13");
        redisRepository.del(user.getAccount() + "_3");
        redisRepository.del(user.getEmail() + "_2");
        redisRepository.del(user.getPhone() + "_1");
        redisRepository.del(user.getPhone() + "_13");
        redisRepository.del("exist_" + user.getAccount() + "_3");
        redisRepository.del("exist_" + user.getEmail() + "_2");
        redisRepository.del("exist_" + user.getPhone() + "_1");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO resetPassword(Long opeUid, String subsidiaryCode, Long id, String newPassword) {
        UserVO userVO = userMapper.selectByUserId(id);
        // 账号不存在
        if (userVO == null) {
            throw new BizException(CodeMsg.record_not_exist);
        }

        return procResetPass(userVO, opeUid, subsidiaryCode, newPassword);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserVO> resetPasswordByPhone(Long opeUid, String subsidiaryCode, String phone, String newPassword) {
        List<UserVO> userVOList = userMapper.selectByPhone(phone);
        // 账号不存在
        if (CollectionUtils.isEmpty(userVOList)) {
            throw new BizException(CodeMsg.record_not_exist);
        }

        if (userVOList.size() > 1) {
            return batchProcResetPassByPhone(userVOList, opeUid, subsidiaryCode, phone, newPassword);
        }

        UserVO userVO = userVOList.get(0);
        List<UserVO> modifiedUserVOList = new ArrayList<>();
        UserVO modifiedUserVO = procResetPass(userVO, opeUid, subsidiaryCode, newPassword);
        modifiedUserVOList.add(modifiedUserVO);
        return modifiedUserVOList;
    }

    /**
     * 重置密码逻辑处理（根据手机号码）
     *
     * @param userVOList     用户信息列表
     * @param opeUid         操作用户ID
     * @param subsidiaryCode 子公司编码
     * @param phone          手机号码
     * @param newPassword    新密码
     * @return 重置密码后的用户信息列表
     */
    private List<UserVO> batchProcResetPassByPhone(List<UserVO> userVOList, Long opeUid, String subsidiaryCode, String phone, String newPassword) {
        List<UserVO> modifiedUserVOList = new ArrayList<>();
        for (UserVO userVO : userVOList) {
            UserVO modifiedUserVO = procResetPass(userVO, opeUid, subsidiaryCode, newPassword);
            modifiedUserVOList.add(modifiedUserVO);
        }
        return modifiedUserVOList;
    }

    /**
     * 重置密码逻辑处理
     *
     * @param userVO         用户信息
     * @param opeUid         操作用户ID
     * @param subsidiaryCode 子公司编码
     * @param newPassword    新密码
     * @return 重置密码后的用户信息
     */
    @Caching(evict = {@CacheEvict(value = CommonConstant.CACHE_NAME_FOLDER, key = "#userVO.getId()+''")})
    private UserVO procResetPass(UserVO userVO, Long opeUid, String subsidiaryCode, String newPassword) {
        // 用户ID
        Long userId = userVO.getId();
        // 操作时间
        Date curDate = Toolkit.getCurDate();

        // 加密盐
        String salt = userVO.getSalt();

        // 新加密类型
        String newEncryptType = subsidiaryCode;
        // 新密码
        String password = EncryptFactory.builder().produceBackPassword(newEncryptType, newPassword, salt);

        // 修改密码
        User account = new User();
        account.setId(userId);
        account.setPassword(password);
        account.setEncryptType(newEncryptType);
        account.setUpdateUid(opeUid);
        account.setUpdateTime(curDate);
        userMapper.updateById(account);

        UserVO modifiedUserVO = userMapper.selectByUserId(userId);

        // 记录修改记录
        UserModifyRecord modifyRecord = new UserModifyRecord();
        modifyRecord.setUserId(userId);
        modifyRecord.setSubsidiaryCode(subsidiaryCode);
        modifyRecord.setResourceContent(JacksonUtil.toJson(userVO));
        modifyRecord.setModifyContent(JacksonUtil.toJson(modifiedUserVO));
        modifyRecord.setCreateTime(curDate);
        modifyRecord.setCreateUid(opeUid);
        modifyRecord.setUpdateTime(curDate);
        modifyRecord.setUpdateUid(opeUid);
        userModifyRecordMapper.insert(modifyRecord);

        //手动删除缓存
        clearCache(userVO);

        return modifiedUserVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserVO> resetPasswordByEmail(Long opeUid, String subsidiaryCode, String email, String newPassword) {
        List<UserVO> userVOList = userMapper.selectByEmail(email);
        // 账号不存在
        if (CollectionUtils.isEmpty(userVOList)) {
            throw new BizException(CodeMsg.record_not_exist);
        }

        if (userVOList.size() > 1) {
            return batchProcResetPassByEmail(userVOList, opeUid, subsidiaryCode, email, newPassword);
        }

        UserVO userVO = userVOList.get(0);
        List<UserVO> modifiedUserVOList = new ArrayList<>();
        UserVO modifiedUserVO = procResetPass(userVO, opeUid, subsidiaryCode, newPassword);
        modifiedUserVOList.add(modifiedUserVO);
        return modifiedUserVOList;
    }

    /**
     * 重置密码逻辑处理（根据邮箱）
     *
     * @param userVOList     用户列表
     * @param opeUid         操作用户ID
     * @param subsidiaryCode 子公司编码
     * @param email          邮箱
     * @param newPassword    新密码
     * @return 重置密码后的用户信息列表
     */
    private List<UserVO> batchProcResetPassByEmail(List<UserVO> userVOList, Long opeUid, String subsidiaryCode, String email, String newPassword) {
        List<UserVO> modifiedUserVOList = new ArrayList<>();
        for (UserVO userVO : userVOList) {
            UserVO modifiedUserVO = procResetPass(userVO, opeUid, subsidiaryCode, newPassword);
            modifiedUserVOList.add(modifiedUserVO);
        }
        return modifiedUserVOList;
    }
}
