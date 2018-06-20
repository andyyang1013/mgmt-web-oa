package com.yxy.oa.service;

import com.baomidou.mybatisplus.service.IService;
import com.yxy.oa.entity.UserTemp;

/**
 * 用户信息临时表，为了接收历史数据 服务类
 */
public interface IUserTempService extends IService<UserTemp> {

    void importHistoryData();

}
