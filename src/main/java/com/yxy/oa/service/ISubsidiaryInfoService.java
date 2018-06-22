package com.yxy.oa.service;

import com.baomidou.mybatisplus.service.IService;
import com.yxy.oa.entity.SubsidiaryInfo;

/**
 * 子公司信息管理 服务类
 */
public interface ISubsidiaryInfoService extends IService<SubsidiaryInfo> {

    /**
     * 根据apiKey获取子公司的配置信息
     *
     * @param apiKey
     * @return
     */
    SubsidiaryInfo selectByApiKey(String apiKey);

}
