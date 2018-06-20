package com.yxy.oa.service;

import com.baomidou.mybatisplus.service.IService;
import com.yxy.oa.entity.UserBindRelation;

/**
 * 用户账号绑定关系表 服务类
 */
public interface IUserBindRelationService extends IService<UserBindRelation> {

    /**
     * 查询当前用户ID的绑定关系
     *
     * @param curUserId 当前用户ID
     * @return 当前用户ID的绑定关系
     */
    UserBindRelation selectByUserId(Long curUserId);
}
