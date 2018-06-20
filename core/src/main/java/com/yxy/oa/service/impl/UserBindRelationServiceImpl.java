package com.yxy.oa.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yxy.oa.entity.UserBindRelation;
import com.yxy.oa.mapper.UserBindRelationMapper;
import com.yxy.oa.service.IUserBindRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户账号绑定关系表 服务实现类
 */
@Service
public class UserBindRelationServiceImpl extends ServiceImpl<UserBindRelationMapper, UserBindRelation> implements IUserBindRelationService {

    @Autowired
    private UserBindRelationMapper userBindRelationMapper;

    @Override
    public UserBindRelation selectByUserId(Long curUserId) {
        return userBindRelationMapper.selectByUserId(curUserId);
    }
}
