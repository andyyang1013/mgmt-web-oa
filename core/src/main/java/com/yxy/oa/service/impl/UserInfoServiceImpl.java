package com.yxy.oa.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yxy.oa.entity.UserInfo;
import com.yxy.oa.mapper.UserInfoMapper;
import com.yxy.oa.service.IUserInfoService;
import org.springframework.stereotype.Service;

/**
 * 账户扩展信息表 服务实现类
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
