package com.yxy.oa.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yxy.oa.entity.SubsidiaryInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 子公司信息管理 Mapper 接口
 */
public interface SubsidiaryInfoMapper extends BaseMapper<SubsidiaryInfo> {

    SubsidiaryInfo selectByApiKey(@Param("apiKey") String apiKey);

    List<SubsidiaryInfo> selectAllAccess();

}