package com.yxy.oa.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yxy.oa.entity.UserBindRelation;

/**
 * 用户账号绑定关系表 Mapper 接口
 */
public interface UserBindRelationMapper extends BaseMapper<UserBindRelation> {

    /**
     * 查询当前用户ID的绑定关系
     *
     * @param curUserId 当前用户ID
     * @return 当前用户ID的绑定关系
     */
    UserBindRelation selectByUserId(Long curUserId);
}