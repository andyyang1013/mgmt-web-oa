package com.yxy.oa.service;

import com.baomidou.mybatisplus.service.IService;
import com.yxy.oa.entity.SubsidiaryInfo;

import java.util.List;
import java.util.Map;

public interface SubsidiaryService extends IService<SubsidiaryInfo> {

    /**
     * 校验公司编码及名称
     *
     * @param subsidiaryInfo
     * @return
     */
    boolean checkCodeAndName(SubsidiaryInfo subsidiaryInfo);

    /**
     * 校验公司编码及名称排除自己
     *
     * @param subsidiaryInfo
     * @return
     */
    boolean checkCodeAndNameExcludeThis(SubsidiaryInfo subsidiaryInfo);

    /**
     * 查询公司编码名称列表
     *
     * @return
     */
    List<Map> selectSubsidiaryNo();

}
