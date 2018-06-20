package com.yxy.oa.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxy.oa.entity.User;
import com.yxy.oa.entity.UserHistoryImportControl;
import com.yxy.oa.entity.UserInfo;
import com.yxy.oa.entity.UserTemp;
import com.yxy.oa.entity.enums.DelFlagEnum;
import com.yxy.oa.entity.enums.IdNumStateEnum;
import com.yxy.oa.mapper.UserHistoryImportControlMapper;
import com.yxy.oa.mapper.UserTempMapper;
import com.yxy.oa.service.IUserInfoService;
import com.yxy.oa.service.IUserService;
import com.yxy.oa.service.IUserTempService;
import com.yxy.oa.util.Toolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户信息临时表，为了接收历史数据 服务实现类
 */
@Service
public class UserTempServiceImpl extends ServiceImpl<UserTempMapper, UserTemp> implements IUserTempService {

    protected Logger logger = LoggerFactory.getLogger(UserTempServiceImpl.class);

    @Autowired
    private UserTempMapper userTempMapper;

    @Autowired
    private UserHistoryImportControlMapper userHistoryImportControlMapper;

    /**
     * 临时导入历史用户才同级引用，不建议这种引用方式
     */
    @Autowired
    private IUserService userService;

    /**
     * 临时导入历史用户才同级引用，不建议这种引用方式
     */
    @Autowired
    private IUserInfoService userInfoService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importHistoryData() {
        Long lastImportId = userTempMapper.selectMaxLastImportId();
        if (lastImportId == null) {
            lastImportId = 0L;
        }
        long totalElements = userTempMapper.selectCountByLastImportId(lastImportId);
        logger.info("临时表需要导入的历史数据总记录数：" + totalElements);
        if (totalElements == 0) {
            logger.info("当前没有需要导入的历史数据");
            return;
        }

        //记录导入的信息
        UserHistoryImportControl userHistoryImportControl = new UserHistoryImportControl();
        Date currentDate = Toolkit.getCurDate();
        long currentImportId = lastImportId;
        long startTime = System.currentTimeMillis();
        logger.info("导入历史数据开始时间：" + startTime);
        List<User> users = new ArrayList<>(500);
        List<UserInfo> userInfos = new ArrayList<>(500);
        int pageSize = 500;
        int pageNum = 1;
        while ((pageNum - 1) * pageSize < totalElements) {
            List<UserTemp> userTempList = userTempMapper.selectByPageSize(pageSize, currentImportId);
            User user = null;
            UserInfo userInfo = null;
            userInfos.clear();
            users.clear();
            for (UserTemp userTemp : userTempList) {
                //用户扩展信息表
                userInfo = new UserInfo();
                userInfo.setId(IdWorker.getId());
                userInfo.setNickName(userTemp.getNickName());
                userInfo.setSex(userTemp.getSex());
                userInfo.setImage(userTemp.getImage());
                userInfo.setName(userTemp.getName());
                userInfo.setIdNumber(userTemp.getIdNumber());
                userInfo.setIdNumberState(IdNumStateEnum.NO_VERIFY.getState());
                userInfo.setCreateUid(1L);
                userInfo.setCreateTime(currentDate);
                userInfo.setUpdateUid(1L);
                userInfo.setUpdateTime(currentDate);
                userInfos.add(userInfo);
                //用户认证信息表
                user = new User();
                user.setUserInfoId(userInfo.getId());
                user.setAccount(userTemp.getAccount());
                user.setEmail(userTemp.getEmail());
                user.setPhone(userTemp.getPhone());
                user.setPassword(userTemp.getPassword());
                user.setSalt(userTemp.getSalt());
                user.setEncryptType(userTemp.getSubsidiaryCode());
                user.setSubsidiaryCode(userTemp.getSubsidiaryCode());
                user.setSubsidiaryUserId(userTemp.getSubsidiaryUserId());
                user.setRegTime(userTemp.getRegTime());
                user.setLastLoginTime(userTemp.getLastLoginTime());
                user.setDelFlag(DelFlagEnum.NO_DELETE.getState());
                user.setCreateUid(1L);
                user.setCreateTime(currentDate);
                user.setUpdateUid(1L);
                user.setUpdateTime(currentDate);
                users.add(user);
            }

            userInfoService.insertBatch(userInfos);
            userService.insertBatch(users);

            if (!userInfos.isEmpty()) {
                currentImportId = userTempList.get(userTempList.size() - 1).getId();
            }
            pageNum++;
        }
        long endTime = System.currentTimeMillis();
        logger.info("导入历史数据结束时间：" + endTime + "，持续时间：" + (endTime - startTime));

        userHistoryImportControl.setId(currentImportId);
        userHistoryImportControl.setImportTime(currentDate);
        userHistoryImportControl.setConsumeTime((endTime - startTime));
        userHistoryImportControl.setState(1);
        userHistoryImportControl.setImportNum(totalElements);
        userHistoryImportControlMapper.insert(userHistoryImportControl);
    }
}
