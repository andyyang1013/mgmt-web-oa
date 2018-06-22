package com.yxy.oa.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yxy.oa.entity.UserTemp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户信息临时表，为了接收历史数据 Mapper 接口
 */
public interface UserTempMapper extends BaseMapper<UserTemp> {

    Long selectMaxLastImportId();

    Long selectCountByLastImportId(@Param("lastImportId") Long lastImportId);

    List<UserTemp> selectByPageSize(@Param("pageSize") int pageSize, @Param("lastImportId") Long lastImportId);

}